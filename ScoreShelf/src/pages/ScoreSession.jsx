import { useEffect, useState } from "react";
import { useLocation, useParams, useNavigate } from "react-router-dom";
import React from "react";
import Background from "../components/Background";
import NavBar from "../components/NavBar";
import { apiFetch } from "../services/api";

import "./ScoreSession.css";

function ScoreSession({ user, onLogout, openLogin }) {
    const { gameSessionId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const scoreSheetId = location.state?.scoreSheetId;
    const gameId = location.state?.gameId;
    const [players, setPlayers] = useState([]);
    const [rows, setRows] = useState([]);
    const [scores, setScores] = useState({});
    const [error, setError] = useState("");

    useEffect(() => {
        apiFetch(`/game-session-player/game-session/${gameSessionId}`)
            .then(setPlayers)
            .catch(() => setError("Could not load players."));

        if (scoreSheetId) {
            apiFetch(`/score-sheet-row/score-sheet/${scoreSheetId}`)
                .then(setRows)
                .catch(() => setError("Could not load score sheet rows."));
        }
    }, [gameSessionId, scoreSheetId]);

    function updateScore(playerId, rowId, value) {
        setScores({
            ...scores,
            [`${playerId}-${rowId}`]: value
        });
    }

    function getScore(playerId, rowId) {
        return parseInt(scores[`${playerId}-${rowId}`]) || 0;
    }

    function calculateRow(playerId, row) {
        if (row.rowType === "INPUT" || row.rowType === "ACHIEVEMENT") {
            return getScore(playerId, row.scoreSheetRowId);
        }

        if (row.rowType === "CALCULATED") {
            const ids = JSON.parse(row.expression || "[]");

            return ids.reduce((total, rowId) => {
                return total + getScore(playerId, rowId);
            }, 0);
        }

        if (row.rowType === "TOTAL") {
            return rows
                .filter((existingRow) =>
                    existingRow.rowType === "INPUT" ||
                    existingRow.rowType === "ACHIEVEMENT" ||
                    existingRow.rowType === "CALCULATED"
                )
                .reduce((total, existingRow) => {
                    return total + calculateRow(playerId, existingRow);
                }, 0);
        }

        return 0;
    }

    function isReadonlyRow(row) {
        return row.rowType === "CALCULATED" || row.rowType === "TOTAL";
    }

    async function handleFinishScoring() {
        const results = players.map((player) => {
            const totalRow = rows.find((row) => row.rowType === "TOTAL");

            return {
                ...player,
                totalScore: totalRow
                    ? calculateRow(player.gameSessionPlayerId, totalRow)
                    : 0
            };
        });

        const maxScore = Math.max(...results.map((player) => player.totalScore));

        const resultsWithWinners = results.map((player) => ({
            ...player,
            isWinner: player.totalScore === maxScore
        }));

        navigate(`/sessions/${gameSessionId}/results`, {
            state: {
                results: resultsWithWinners,
                scoreSheetId,
                players,
                gameId: location.state?.gameId
            }
        });
    }

    return (
        <Background>
            <main className="score-session-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="score-session-content">
                    <h1>Score Session</h1>

                    {error && <p className="form-error">{error}</p>}

                    <div className="score-grid" style={{ "--player-count": players.length }}>
                        <div className="score-grid-header">Category</div>

                        {players.map((player) => (
                            <div
                                className="score-grid-header"
                                key={player.gameSessionPlayerId}
                            >
                                {player.playerName}
                            </div>
                        ))}

                        {rows.map((row) => (
                            <React.Fragment key={row.scoreSheetRowId}>
                                <div className="score-row-label">
                                    {row.rowName}
                                </div>

                                {players.map((player) =>
                                    isReadonlyRow(row) ? (
                                        <div
                                            className="score-readonly-cell"
                                            key={`${player.gameSessionPlayerId}-${row.scoreSheetRowId}`}
                                        >
                                            {calculateRow(player.gameSessionPlayerId, row)}
                                        </div>
                                    ) : (
                                        <input
                                            key={`${player.gameSessionPlayerId}-${row.scoreSheetRowId}`}
                                            type="number"
                                            value={
                                                scores[
                                                `${player.gameSessionPlayerId}-${row.scoreSheetRowId}`
                                                ] || ""
                                            }
                                            onChange={(event) =>
                                                updateScore(
                                                    player.gameSessionPlayerId,
                                                    row.scoreSheetRowId,
                                                    event.target.value
                                                )
                                            }
                                        />
                                    )
                                )}
                            </React.Fragment>
                        ))}
                    </div>
                    <button
                        className="finish-scoring-button"
                        type="button"
                        onClick={handleFinishScoring}
                    >
                        Finish Scoring
                    </button>
                </section>
            </main>
        </Background>
    );
}

export default ScoreSession