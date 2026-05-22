import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";


import Background from "../components/Background";
import NavBar from "../components/NavBar";
import { apiFetch } from "../services/api";

import "./PlaySetup.css";

function PlaySetup({ user, onLogout, openLogin }) {
    const { gameId } = useParams();

    const [game, setGame] = useState(null);
    const [scoreSheets, setScoreSheets] = useState([]);
    const [selectedScoreSheetId, setSelectedScoreSheetId] = useState("");
    const [players, setPlayers] = useState([""]);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        apiFetch(`/game/${gameId}`)
            .then(setGame)
            .catch(() => setError("Could not load game."));

        apiFetch(`/score-sheet/game/${gameId}`)
            .then(setScoreSheets)
            .catch(() => setError("Could not load score sheets."));
    }, [gameId]);

    function updatePlayerName(index, value) {
        const copy = [...players];
        copy[index] = value;
        setPlayers(copy);
    }

    function addPlayer() {
        setPlayers([...players, ""]);
    }

    function removePlayer(index) {
        setPlayers(players.filter((_, i) => i !== index));
    }

    async function handleStart() {
        try {
            setError("");

            const cleanPlayers = players
                .map((player) => player.trim())
                .filter((player) => player.length > 0);

            if (!selectedScoreSheetId) {
                setError("Select a score sheet.");
                return;
            }

            if (cleanPlayers.length === 0) {
                setError("Add at least one player.");
                return;
            }

            if (cleanPlayers.length < game.minPlayers) {
                setError(`This game requires at least ${game.minPlayers} players.`);
                return;
            }

            if (cleanPlayers.length > game.maxPlayers) {
                setError(`This game supports at most ${game.maxPlayers} players.`);
                return;
            }

            const session = await apiFetch("/game-session", {
                method: "POST",
                body: JSON.stringify({
                    gameId: parseInt(gameId),
                    appUserId: user.appUserId,
                    playedAt: new Date().toISOString()
                })
            });

            for (const playerName of cleanPlayers) {
                await apiFetch("/game-session-player", {
                    method: "POST",
                    body: JSON.stringify({
                        gameSessionId: session.gameSessionId,
                        playerId: null,
                        playerName,
                        totalScore: 0,
                        isWinner: false
                    })
                });
            }

            navigate(`/sessions/${session.gameSessionId}/score`, {
                state: {
                    scoreSheetId: parseInt(selectedScoreSheetId),
                    gameId: parseInt(gameId)
                }
            });
        } catch {
            setError("Could not start game session.");
        }
    }

    return (
        <Background>
            <main className="play-setup-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="play-setup-card">
                    <h1>Play {game?.gameName}</h1>
                    <p>Choose a score sheet and add players.</p>

                    {error && <p className="form-error">{error}</p>}

                    <label>
                        Score Sheet
                        <select
                            value={selectedScoreSheetId}
                            onChange={(event) =>
                                setSelectedScoreSheetId(event.target.value)
                            }
                        >
                            <option value="">Select Score Sheet</option>

                            {scoreSheets.map((sheet) => (
                                <option
                                    key={sheet.scoreSheetId}
                                    value={sheet.scoreSheetId}
                                >
                                    {sheet.scoreSheetName}
                                </option>
                            ))}
                        </select>
                    </label>

                    <div className="player-list">
                        <h2>Players</h2>

                        {players.map((player, index) => (
                            <div className="player-input-row" key={index}>
                                <input
                                    type="text"
                                    placeholder={`Player ${index + 1}`}
                                    value={player}
                                    onChange={(event) =>
                                        updatePlayerName(index, event.target.value)
                                    }
                                />

                                {players.length > 1 && (
                                    <button
                                        type="button"
                                        onClick={() => removePlayer(index)}
                                    >
                                        Remove
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>

                    <button type="button" onClick={addPlayer}>
                        Add Player
                    </button>

                    <button type="button" onClick={handleStart}>
                        Start Scoring
                    </button>
                </section>
            </main>
        </Background>
    );
}

export default PlaySetup;