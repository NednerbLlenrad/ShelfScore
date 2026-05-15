import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Background from "../components/Background";
import NavBar from "../components/NavBar";
import GameCard from "../components/GameCard";
import { apiFetch } from "../services/api";

import "./MyGames.css";

function MyGames({ user, onLogout, openLogin }) {
    const [games, setGames] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        apiFetch("/game/my")
            .then(setGames)
            .catch(() => setError("Could not load games."));
    }, []);

    return (
        <Background>
            <main className="games-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="games-content">
                    <div className="games-header">
                        <div>
                            <h1>My Games</h1>
                        </div>

                        <Link className="add-game-button" to="/my-games/add">
                            Add Game
                        </Link>
                    </div>

                    {error && <p>{error}</p>}
                    <div className="games-grid">
                        {games.map((game) => (
                            <GameCard key={game.gameId} game={game} showOwnerActions={true}/>
                        ))}
                    </div>
                </section>
            </main>
        </Background>
    );
}


export default MyGames;