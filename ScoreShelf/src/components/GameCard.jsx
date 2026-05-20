import { Link } from "react-router-dom";
import DefaultImage from "../assets/default-image.png";
import "./GameCard.css";

function getImageSource(imageUrl) {
  if (!imageUrl) {
    return DefaultImage;
  }

  if (imageUrl.startsWith("http")) {
    return imageUrl;
  }

  return `http://localhost:8080${imageUrl}`;
}

function GameCard({ game, showOwnerActions = false, showAddToLibrary, onAddToLibrary }) {
  return (
    <article className="game-card">
      <img
        className="game-card-image"
        src={getImageSource(game.imageUrl)}
        alt={game.gameName}
      />

      <h2>{game.gameName}</h2>

      <p>{game.category}</p>

      <p>
        {game.minPlayers}-{game.maxPlayers} players
      </p>

      {showOwnerActions && (
        <div className="game-card-actions">
          <Link className="game-card-play" to={`/my-games/${game.gameId}/play`}>
            Play
          </Link>

          <Link className="game-card-edit" to={`/my-games/edit/${game.gameId}`}>
            Edit
          </Link>

        </div>
      )}

      {showAddToLibrary && (
        <button
          className="game-card-library"
          type="button"
          onClick={() => onAddToLibrary(game.gameId)}
        >
          Add to Library
        </button>
      )}
    </article>
  );
}

export default GameCard;