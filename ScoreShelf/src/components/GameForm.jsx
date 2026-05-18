import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { useParams } from "react-router-dom";
import Background from "../components/Background";
import NavBar from "../components/NavBar";
import { apiFetch } from "../services/api";

import "./GameForm.css";

function GameForm({ user, onLogout, openLogin }) {
    const navigate = useNavigate();
    const [imageFile, setImageFile] = useState(null);
    const [game, setGame] = useState({
        gameName: "",
        imageUrl: "",
        category: "",
        minPlayers: 1,
        maxPlayers: 1,
        isPrivate: false
    });

    const [error, setError] = useState("");
    const { id } = useParams();

    const isEdit = id !== undefined;
    function handleChange(event) {
        const { name, value, type, checked } = event.target;

        setGame({
            ...game,
            [name]: type === "checkbox" ? checked : value
        });
    }

    function handleFileChange(event) {
        const file = event.target.files[0];

        if (!file) {
            return;
        }

        if (file.type !== "image/png") {
            setError("Only PNG files are allowed.");
            return;
        }

        const maxSize = 2 * 1024 * 1024;

        if (file.size > maxSize) {
            setError("Image must be under 2MB.");
            return;
        }

        setError("");
        setImageFile(file);
    }

    function handleSubmit(event) {
        event.preventDefault();

        const formData = new FormData();

        formData.append("gameName", game.gameName);
        formData.append("category", game.category);
        formData.append("minPlayers", parseInt(game.minPlayers));
        formData.append("maxPlayers", parseInt(game.maxPlayers));
        formData.append("isPrivate", game.isPrivate);
        formData.append("appUserId", user.appUserId);
        formData.append("imageUrl", game.imageUrl || "");
        if (imageFile) {
            formData.append("image", imageFile);
        }

        apiFetch(
            isEdit ? `/game/${id}` : "/game",
            {
                method: isEdit ? "PUT" : "POST",
                body: formData
            }
        )
            .then(() => navigate("/my-games"))
            .catch(() => setError("Could not submit game."));
    }

    function handleDelete() {
        apiFetch(`/game/${id}`, {
            method: "DELETE"
        })
            .then(() => navigate("/my-games"))
            .catch(() => setError("Could not delete game."));
    }

    useEffect(() => {
        if (!isEdit) {
            return;
        }

        apiFetch(`/game/${id}`)
            .then((data) =>
                setGame({
                    gameName: "",
                    imageUrl: "",
                    category: "",
                    minPlayers: 1,
                    maxPlayers: 1,
                    isPrivate: false,
                    ...data
                })
            )
            .catch(() => setError("Could not load game."));
    }, [id, isEdit]);

    return (
        <Background>
            <main className="game-form-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="game-form-card">
                    <h1>{isEdit ? "Edit Game" : "Add Game"}</h1>

                    <p>
                        {isEdit
                            ? "Update your game details."
                            : "Create a game for your personal library."}
                    </p>

                    {error && <p className="form-error">{error}</p>}

                    <form className="game-form" onSubmit={handleSubmit}>
                        <label>
                            Game Name
                            <input
                                type="text"
                                name="gameName"
                                value={game.gameName}
                                onChange={handleChange}
                            />
                        </label>

                        <label>
                            Game Image

                            <input
                                type="file"
                                name="image"
                                accept=".png"
                                onChange={handleFileChange}
                            />
                            {(game.imageUrl || imageFile) && (
                                <img
                                    className="game-image-preview"
                                    src={
                                        imageFile
                                            ? URL.createObjectURL(imageFile)
                                            : `http://localhost:8080${game.imageUrl}`
                                    }
                                    alt="Game preview"
                                />
                            )}
                        </label>

                        <select
                            name="category"
                            value={game.category}
                            onChange={handleChange}
                        >
                            <option value="">Select Category</option>

                            <option value="Strategy">Strategy</option>
                            <option value="Party">Party</option>
                            <option value="Cooperative">Cooperative</option>
                            <option value="Card Game">Card Game</option>
                            <option value="Trivia">Trivia</option>
                            <option value="Deduction">Deduction</option>
                            <option value="Family">Family</option>
                            <option value="Abstract">Abstract</option>
                            <option value="War">War</option>
                            <option value="Eurogame">Eurogame</option>
                        </select>

                        <label>
                            Min Players
                            <input
                                type="number"
                                name="minPlayers"
                                min="1"
                                value={game.minPlayers}
                                onChange={handleChange}
                            />
                        </label>

                        <label>
                            Max Players
                            <input
                                type="number"
                                name="maxPlayers"
                                min="1"
                                value={game.maxPlayers}
                                onChange={handleChange}
                            />
                        </label>

                        <label className="checkbox-label">
                            <input
                                type="checkbox"
                                name="isPrivate"
                                checked={game.isPrivate}
                                onChange={handleChange}
                            />
                            Private game
                        </label>
                        {isEdit && (
                            <button
                                type="button"
                                onClick={() => navigate(`/my-games/${id}/score-sheets`)}
                            >
                                Manage Score Sheets
                            </button>
                        )}
                        <button type="submit">{isEdit ? "Save Changes" : "Add Game"}</button>
                        {isEdit && (
                            <button
                                className="delete-game-button"
                                type="button"
                                onClick={handleDelete}
                            >
                                Delete Game
                            </button>
                        )}
                    </form>
                </section>
            </main>
        </Background>
    );
}

export default GameForm;