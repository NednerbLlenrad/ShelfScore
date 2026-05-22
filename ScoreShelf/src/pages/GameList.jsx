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
<<<<<<< Updated upstream

=======
  const [searchText, setSearchText] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const filteredGames = games.filter((game) =>
    game.gameName.toLowerCase().includes(searchText.toLowerCase())
  );
>>>>>>> Stashed changes
  useEffect(() => {
    apiFetch("/game")
      .then(setGames)
      .catch(() => setError("Could not load games."));
  }, []);

<<<<<<< Updated upstream
=======
  function handleCopyToLibrary(gameId) {
    apiFetch(`/game/${gameId}/copy`, {
      method: "POST"
    })
      .then(() => {
        setError("");
        setSuccessMessage("Game added to your library!");

        setTimeout(() => {
          setSuccessMessage("");
        }, 3000);
      })
      .catch(() => {
        setSuccessMessage("");
        setError("Could not add game to library.");
      });
  }
>>>>>>> Stashed changes
  return (
    <Background>
      <main className="games-page">
        <NavBar
          isLoggedIn={user !== null}
          onLoginClick={openLogin}
          onLogout={onLogout}
        />

        <section className="games-content">
<<<<<<< Updated upstream
          <h1>All Games</h1>

          {error && <p>{error}</p>}

=======
          <div className="games-header">
            <h1>Community Games</h1>

            {error && <p className="error-message">{error}</p>}
            {successMessage && (
              <p className="success-message">{successMessage}</p>
            )}

            <SearchBar searchText={searchText} setSearchText={setSearchText} />
          </div>
>>>>>>> Stashed changes
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