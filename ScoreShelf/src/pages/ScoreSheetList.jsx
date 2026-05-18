import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import ScoreSheetAddForm from "../components/ScoreSheetAddForm";
import ScoreSheetCard from "../components/ScoreSheetCard";
import ScoreSheetEditor from "../components/ScoreSheetEditor";
import { apiFetch } from "../services/api";

import "./ScoreSheetList.css";

function ScoreSheetList({ user, onLogout, openLogin }) {
    const { gameId } = useParams();

    const [scoreSheets, setScoreSheets] = useState([]);
    const [error, setError] = useState("");
    const [selectedScoreSheet, setSelectedScoreSheet] = useState(null);
    const [newScoreSheetName, setNewScoreSheetName] = useState("");

    function loadScoreSheets() {
        apiFetch(`/score-sheet/game/${gameId}`)
            .then(setScoreSheets)
            .catch(() => setError("Could not load score sheets."));
    }

    useEffect(() => {
        loadScoreSheets();
    }, [gameId]);

    function handleAddScoreSheet(event) {
        event.preventDefault();

        const scoreSheet = {
            gameId: parseInt(gameId),
            scoreSheetName: newScoreSheetName
        };

        apiFetch("/score-sheet", {
            method: "POST",
            body: JSON.stringify(scoreSheet)
        })
            .then((data) => {
                setNewScoreSheetName("");
                setSelectedScoreSheet(data);
                loadScoreSheets();
            })
            .catch(() => setError("Could not add score sheet."));
    }

    return (
        <Background>
            <main className="score-sheet-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="score-sheet-content">
                    <div className="score-sheet-header">
                        <div>
                            <h1>Score Sheets</h1>
                            <p>Build a score sheet for this game.</p>
                        </div>

                        <ScoreSheetAddForm
                            newScoreSheetName={newScoreSheetName}
                            setNewScoreSheetName={setNewScoreSheetName}
                            onSubmit={handleAddScoreSheet}
                        />
                    </div>

                    {error && <p className="form-error">{error}</p>}

                    <div className="score-sheet-grid">
                        {scoreSheets.map((scoreSheet) => (
                            <ScoreSheetCard
                                key={scoreSheet.scoreSheetId}
                                scoreSheet={scoreSheet}
                                selected={
                                    selectedScoreSheet?.scoreSheetId ===
                                    scoreSheet.scoreSheetId
                                }
                                onSelect={setSelectedScoreSheet}
                            />
                        ))}
                    </div>

                    <ScoreSheetEditor
                        scoreSheet={selectedScoreSheet}
                        onDeleteScoreSheet={() => {
                            setSelectedScoreSheet(null);
                            loadScoreSheets();
                        }}
                    />
                </section>
            </main>
        </Background>
    );
}

export default ScoreSheetList;