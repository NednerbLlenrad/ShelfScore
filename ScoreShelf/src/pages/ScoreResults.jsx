import { useLocation, useNavigate, useParams } from "react-router-dom";
import { apiFetch } from "../services/api";
import Background from "../components/Background";
import NavBar from "../components/NavBar";

import "./ScoreResults.css";

function ScoreResults({ user, onLogout, openLogin }) {
    const { gameSessionId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const results = location.state?.results || [];

    const winners = results.filter((player) => player.isWinner);

    async function handlePlayAgain() {
        const previousPlayers = location.state?.players || [];
        const scoreSheetId = location.state?.scoreSheetId;

        const session = await apiFetch("/game-session", {
            method: "POST",
            body: JSON.stringify({
                gameId: location.state?.gameId,
                appUserId: user.appUserId,
                playedAt: new Date().toISOString()
            })
        });

        for (const player of previousPlayers) {
            await apiFetch("/game-session-player", {
                method: "POST",
                body: JSON.stringify({
                    gameSessionId: session.gameSessionId,
                    playerId: player.playerId || null,
                    playerName: player.playerName,
                    totalScore: 0,
                    isWinner: false
                })
            });
        }

        navigate(`/sessions/${session.gameSessionId}/score`, {
            state: {
                scoreSheetId
            }
        });
    }

    return (
        <Background>
            <main className="score-results-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="score-results-card">
                    <h1>Final Scores</h1>

                    {winners.length > 0 && (
                        <h2>
                            Winner: {winners.map((winner) => winner.playerName).join(", ")}
                        </h2>
                    )}

                    <div className="results-list">
                        {results
                            .sort((a, b) => b.totalScore - a.totalScore)
                            .map((player, index) => (
                                <article className="result-row" key={player.gameSessionPlayerId}>
                                    <span>#{index + 1}</span>
                                    <strong>{player.playerName}</strong>
                                    <span>{player.totalScore} points</span>
                                </article>
                            ))}
                    </div>

                    <div className="score-results-actions">
                        <button type="button" onClick={handlePlayAgain}>
                            Play Again
                        </button>

                        <button
                            type="button"
                            onClick={() => navigate("/stats")}
                        >
                            Stats
                        </button>
                    </div>
                </section>
            </main>
        </Background>
    );
}

export default ScoreResults;