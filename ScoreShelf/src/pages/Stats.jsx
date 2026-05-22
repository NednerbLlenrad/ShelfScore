import { useEffect, useState } from "react";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import { apiFetch } from "../services/api";

import "./Stats.css";

function Stats({ user, onLogout, openLogin }) {
    const [stats, setStats] = useState([]);
    const [error, setError] = useState("");
    const [openGame, setOpenGame] = useState(null);

    const groupedStats = stats.reduce((groups, stat) => {
        if (!groups[stat.gameName]) {
            groups[stat.gameName] = [];
        }

        groups[stat.gameName].push(stat);

        return groups;
    }, {});

    useEffect(() => {
        if (!user) {
            return;
        }

        apiFetch(`/stats/${user.appUserId}`)
            .then(setStats)
            .catch(() => setError("Could not load stats."));
    }, [user]);

    return (
        <Background>
            <main className="stats-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="stats-content">
                    <h1>Stats</h1>

                    {error && <p className="form-error">{error}</p>}
                    <p className="stats-subtitle">View your plays and wins across your game library.</p>
                    <div className="stats-game-list">
                        {Object.entries(groupedStats).map(([gameName, playerStats]) => {
                            const totalPlays = playerStats.reduce(
                                (total, stat) => total + stat.playCount,
                                0
                            );

                            const totalWins = playerStats.reduce(
                                (total, stat) => total + stat.winCount,
                                0
                            );

                            return (
                                <article className="stats-game-card" key={gameName}>
                                    <button
                                        className="stats-game-summary"
                                        type="button"
                                        onClick={() =>
                                            setOpenGame(openGame === gameName ? null : gameName)
                                        }
                                    >
                                        <span>{gameName}</span>
                                        <span>{totalPlays} plays</span>
                                        <span>{totalWins} wins</span>
                                        <span>{openGame === gameName ? "▲" : "▼"}</span>
                                    </button>

                                    {openGame === gameName && (
                                        <div className="stats-player-list">
                                            {playerStats.map((stat) => (
                                                <div className="stats-player-row" key={stat.playerName}>
                                                    <span>{stat.playerName}</span>
                                                    <span>{stat.playCount} plays</span>
                                                    <span>{stat.winCount} wins</span>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </article>
                            );
                        })}
                    </div>
                </section>
            </main>
        </Background>
    );
}

export default Stats;