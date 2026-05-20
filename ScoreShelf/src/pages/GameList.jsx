import { useEffect, useState } from "react";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import GameCard from "../components/GameCard";
import { apiFetch } from "../services/api";
import SearchBar from "../components/SearchBar";
import "./GameList.css";

function GameList({ user, onLogout, openLogin }) {
  const [games, setGames] = useState([]);
  const [error, setError] = useState("");
  const [searchText, setSearchText] = useState("");

  const filteredGames = games.filter((game) =>
    game.gameName.toLowerCase().includes(searchText.toLowerCase())
  );
  useEffect(() => {
    apiFetch("/game")
      .then(setGames)
      .catch(() => setError("Could not load games."));
  }, []);

  function handleCopyToLibrary(gameId) {
    apiFetch(`/game/${gameId}/copy`, {
      method: "POST"
    })
      .then(() => {
        setError("");
      })
      .catch(() => setError("Could not add game to library."));
  }

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
            <h1>All Games</h1>

            {error && <p>{error}</p>}
            <SearchBar searchText={searchText} setSearchText={setSearchText} />
          </div>
          <div className="games-grid">
            {filteredGames.map((game) => (
              <GameCard key={game.gameId} game={game} showAddToLibrary={user !== null} onAddToLibrary={handleCopyToLibrary} />
            ))}
          </div>
        </section>
      </main>
    </Background>
  );
}

export default GameList;