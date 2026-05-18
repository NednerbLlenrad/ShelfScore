import { useEffect, useState } from "react";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import GameCard from "../components/GameCard";
import { apiFetch } from "../services/api";

import "./GameList.css";

function GameList({ user, onLogout, openLogin }) {
  const [games, setGames] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    apiFetch("/game")
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
          <h1>All Games</h1>

          {error && <p>{error}</p>}

          <div className="games-grid">
            {games.map((game) => (
              <GameCard key={game.gameId} game={game} />
            ))}
          </div>
        </section>
      </main>
    </Background>
  );
}

export default GameList;