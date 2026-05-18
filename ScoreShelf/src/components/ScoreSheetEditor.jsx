import { useEffect, useState } from "react";

import ScoreSheetRowForm from "./ScoreSheetRowForm";
import ScoreSheetRowList from "./ScoreSheetRowList";
import { apiFetch } from "../services/api";
import "./ScoreSheetEditor.css";

function ScoreSheetEditor({ scoreSheet, onDeleteScoreSheet }) {
    const [rows, setRows] = useState([]);
    const [error, setError] = useState("");

    function loadRows() {
        if (!scoreSheet) {
            return;
        }

        apiFetch(`/score-sheet-row/score-sheet/${scoreSheet.scoreSheetId}`)
            .then((data) => {
                setRows(data);
                setError("");
            })
            .catch(() => setError("Could not load rows."));
    }

    useEffect(() => {
        if (!scoreSheet) {
            setRows([]);
            return;
        }

        loadRows();
    }, [scoreSheet]);

    function handleAddRow(row) {
        setError("");

        apiFetch("/score-sheet-row", {
            method: "POST",
            body: JSON.stringify(row)
        })
            .then(() => {
                setError("");
                loadRows();
            })
            .catch(() => setError("Could not add row."));
    }

    function handleDeleteRow(rowId) {
        apiFetch(`/score-sheet-row/${rowId}`, {
            method: "DELETE"
        })
            .then(() => loadRows())
            .catch(() => setError("Could not delete row."));
    }

    function handleUpdateRow(row) {
        apiFetch(`/score-sheet-row/${row.scoreSheetRowId}`, {
            method: "PUT",
            body: JSON.stringify(row)
        })
            .then(() => loadRows())
            .catch(() => setError("Could not update row."));
    }

    function handleDeleteScoreSheet() {
        if (!scoreSheet) {
            return;
        }

        const scoreSheetId = scoreSheet.scoreSheetId;

        apiFetch(`/score-sheet/${scoreSheetId}`, {
            method: "DELETE"
        })
            .then(() => {
                onDeleteScoreSheet();
            })
            .catch(() => setError("Could not delete score sheet."));
    }

    if (!scoreSheet) {
        return (
            <section className="score-sheet-editor empty-editor">
                <p>Select a score sheet to edit its rows.</p>
            </section>
        );
    }

    return (
        <section className="score-sheet-editor">
            <div className="score-sheet-editor-header">
                <div>
                    <h2>{scoreSheet.scoreSheetName}</h2>
                    <p>Add rows that define how this game is scored.</p>
                </div>

                <button
                    className="delete-score-sheet-button"
                    type="button"
                    onClick={handleDeleteScoreSheet}
                >
                    Delete Sheet
                </button>
            </div>

            {error && <p className="form-error">{error}</p>}

            <ScoreSheetRowForm
                scoreSheetId={scoreSheet.scoreSheetId}
                rows={rows}
                onAddRow={handleAddRow}
            />

            <ScoreSheetRowList
                rows={rows}
                onDeleteRow={handleDeleteRow}
                onUpdateRow={handleUpdateRow}
            />
        </section>
    );
}

export default ScoreSheetEditor;