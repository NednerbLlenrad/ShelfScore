import { useState } from "react";

function ScoreSheetRowList({ rows, onDeleteRow, onUpdateRow }) {
    const [editingRowId, setEditingRowId] = useState(null);
    const [editRow, setEditRow] = useState(null);
    const [openDropdownRowId, setOpenDropdownRowId] = useState(null);

    if (rows.length === 0) {
        return <p className="empty-editor">No rows yet. Add one to start building.</p>;
    }

    function startEdit(row) {
        setEditingRowId(row.scoreSheetRowId);
        setEditRow({ ...row });
    }

    function cancelEdit() {
        setEditingRowId(null);
        setEditRow(null);
    }

    function handleChange(event) {
        setEditRow({
            ...editRow,
            [event.target.name]: event.target.value
        });
    }

    function handleSave() {
        const expression =
            editRow.rowType === "TOTAL"
                ? "ALL"
                : editRow.expression;

        onUpdateRow({
            ...editRow,
            expression,
            displayOrder: parseInt(editRow.displayOrder)
        });

        cancelEdit();
    }

    function toggleEditExpressionRow(rowId) {
        const selectedIds = JSON.parse(editRow.expression || "[]");

        const updatedIds = selectedIds.includes(rowId)
            ? selectedIds.filter((id) => id !== rowId)
            : [...selectedIds, rowId];

        setEditRow({
            ...editRow,
            expression: JSON.stringify(updatedIds)
        });
    }

    function getExpressionDisplay(row) {
        if (row.rowType === "TOTAL") {
            return "Includes all rows";
        }

        if (row.rowType !== "CALCULATED" || !row.expression) {
            return null;
        }

        const selectedIds = JSON.parse(row.expression);

        const selectedRows = rows.filter((existingRow) =>
            selectedIds.includes(existingRow.scoreSheetRowId)
        );

        return selectedRows.map((row) => row.rowName).join(", ");
    }

    return (
        <div className="score-sheet-row-list">
            {rows.map((row) => (
                <article className="score-sheet-row-card" key={row.scoreSheetRowId}>
                    {editingRowId === row.scoreSheetRowId ? (
                        <>
                            <input name="rowName" value={editRow.rowName} onChange={handleChange} />

                            <select name="rowType" value={editRow.rowType} onChange={handleChange}>
                                <option value="INPUT">Input</option>
                                <option value="CALCULATED">Calculated</option>
                                <option value="RANKED">Ranked</option>
                                <option value="ACHIEVEMENT">Achievement</option>
                                <option value="TOTAL">Total</option>
                            </select>

                            <input
                                type="number"
                                name="displayOrder"
                                min="1"
                                value={editRow.displayOrder}
                                onChange={handleChange}
                            />

                            {editRow.rowType === "CALCULATED" && (
                                <div className="calculation-dropdown">
                                    <button
                                        type="button"
                                        className="calculation-dropdown-button"
                                        onClick={() =>
                                            setOpenDropdownRowId(
                                                openDropdownRowId === editRow.scoreSheetRowId
                                                    ? null
                                                    : editRow.scoreSheetRowId
                                            )
                                        }
                                    >
                                        Select rows to include
                                    </button>

                                    {openDropdownRowId === editRow.scoreSheetRowId && (
                                        <div className="calculation-dropdown-menu">
                                            {rows
                                                .filter((existingRow) => existingRow.rowType === "INPUT")
                                                .map((existingRow) => (
                                                    <label
                                                        className="calculation-dropdown-option"
                                                        key={existingRow.scoreSheetRowId}
                                                    >
                                                        <input
                                                            type="checkbox"
                                                            checked={JSON.parse(editRow.expression || "[]").includes(
                                                                existingRow.scoreSheetRowId
                                                            )}
                                                            onChange={() =>
                                                                toggleEditExpressionRow(existingRow.scoreSheetRowId)
                                                            }
                                                        />

                                                        {existingRow.rowName}
                                                    </label>
                                                ))}
                                        </div>
                                    )}
                                </div>
                            )}

                            <button type="button" onClick={handleSave}>Save</button>
                            <button type="button" onClick={cancelEdit}>Cancel</button>
                        </>
                    ) : (
                        <>
                            <div>
                                <h3>{row.rowName}</h3>
                                <p>{row.rowType}</p>

                                {getExpressionDisplay(row) && (
                                    <small>{getExpressionDisplay(row)}</small>
                                )}
                            </div>

                            <span>Order: {row.displayOrder}</span>

                            <button type="button" onClick={() => startEdit(row)}>Edit</button>

                            <button type="button" onClick={() => onDeleteRow(row.scoreSheetRowId)}>
                                Delete
                            </button>
                        </>
                    )}
                </article>
            ))}
        </div>
    );
}

export default ScoreSheetRowList;