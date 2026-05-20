import { useState } from "react";

function ScoreSheetRowForm({ scoreSheetId, rows, onAddRow }) {
    const [row, setRow] = useState({
        rowName: "",
        rowType: "INPUT",
        displayOrder: 1,
        expression: ""
    });

    const [showRowPicker, setShowRowPicker] = useState(false);

    function handleChange(event) {
        setRow({
            ...row,
            [event.target.name]: event.target.value
        });
    }

    function handleSubmit(event) {
        event.preventDefault();

        const expression =
            row.rowType === "TOTAL"
                ? "ALL"
                : row.expression;

        onAddRow({
            ...row,
            expression,
            scoreSheetId,
            displayOrder: parseInt(row.displayOrder)
        });

        setRow({
            rowName: "",
            rowType: "INPUT",
            displayOrder: parseInt(row.displayOrder) + 1,
            expression: ""
        });
    }

    function toggleExpressionRow(rowId) {
        const selectedIds = JSON.parse(row.expression || "[]");

        const updatedIds = selectedIds.includes(rowId)
            ? selectedIds.filter((id) => id !== rowId)
            : [...selectedIds, rowId];

        setRow({
            ...row,
            expression: JSON.stringify(updatedIds)
        });
    }

    function setRatioField(field, value) {
        const config = JSON.parse(row.expression || "{}");

        const updatedConfig = {
            ...config,
            [field]: value
        };

        setRow({
            ...row,
            expression: JSON.stringify(updatedConfig)
        });
    }

    function setRankedPlaces(value) {
        const config = JSON.parse(row.expression || "{}");

        const places = value
            .split(",")
            .map((place) => parseInt(place.trim()) || 0);

        setRow({
            ...row,
            expression: JSON.stringify({
                ...config,
                places
            })
        });
    }

    return (
        <form className="score-sheet-row-form" onSubmit={handleSubmit}>
            <input
                type="text"
                name="rowName"
                placeholder="Row name"
                value={row.rowName}
                onChange={handleChange}
            />

            <select name="rowType" value={row.rowType} onChange={handleChange}>
                <option value="INPUT">Input</option>
                <option value="CALCULATED">Calculated</option>
                <option value="RATIO">Ratio</option>
                <option value="RANKED">Ranked</option>
                <option value="ACHIEVEMENT">Achievement</option>
                <option value="PENALTY">Penalty</option>
                <option value="TOTAL">Total</option>
            </select>

            <input
                type="number"
                name="displayOrder"
                min="1"
                value={row.displayOrder}
                onChange={handleChange}
            />
            {row.rowType === "RATIO" && (
                <div className="ratio-rule-builder">
                    <select
                        value={JSON.parse(row.expression || "{}").sourceRowId || ""}
                        onChange={(event) =>
                            setRatioField("sourceRowId", parseInt(event.target.value))
                        }
                    >
                        <option value="">Choose source row</option>

                        {rows
                            .filter((existingRow) => existingRow.rowType === "INPUT")
                            .map((existingRow) => (
                                <option
                                    key={existingRow.scoreSheetRowId}
                                    value={existingRow.scoreSheetRowId}
                                >
                                    {existingRow.rowName}
                                </option>
                            ))}
                    </select>

                    <input
                        type="number"
                        min="1"
                        placeholder="Every"
                        value={JSON.parse(row.expression || "{}").amount || ""}
                        onChange={(event) =>
                            setRatioField("amount", parseInt(event.target.value))
                        }
                    />

                    <input
                        type="number"
                        placeholder="Points"
                        value={JSON.parse(row.expression || "{}").points || ""}
                        onChange={(event) =>
                            setRatioField("points", parseInt(event.target.value))
                        }
                    />
                </div>
            )}
            {row.rowType === "RANKED" && (
                <div className="ranked-rule-builder">

                    <select
                        value={JSON.parse(row.expression || "{}").sourceRowId || ""}
                        onChange={(event) => {
                            const config =
                                JSON.parse(row.expression || "{}");

                            setRow({
                                ...row,
                                expression: JSON.stringify({
                                    ...config,
                                    sourceRowId: parseInt(event.target.value)
                                })
                            });
                        }}
                    >
                        <option value="">
                            Choose ranking row
                        </option>

                        {rows
                            .filter((existingRow) =>
                                existingRow.rowType === "INPUT"
                            )
                            .map((existingRow) => (
                                <option
                                    key={existingRow.scoreSheetRowId}
                                    value={existingRow.scoreSheetRowId}
                                >
                                    {existingRow.rowName}
                                </option>
                            ))}
                    </select>

                    <input
                        type="text"
                        placeholder="5,2,1"
                        value={
                            (
                                JSON.parse(row.expression || "{}").places || []
                            ).join(",")
                        }
                        onChange={(event) =>
                            setRankedPlaces(event.target.value)
                        }
                    />

                </div>
            )}
            {row.rowType === "ACHIEVEMENT" && (
                <input
                    type="number"
                    name="expression"
                    placeholder="Points awarded"
                    value={row.expression}
                    onChange={handleChange}
                />
            )}
            {(row.rowType === "CALCULATED") && (
                <div className="calculation-dropdown">
                    <button
                        type="button"
                        className="calculation-dropdown-button"
                        onClick={() => setShowRowPicker(!showRowPicker)}
                    >
                        Select rows to include
                    </button>

                    {showRowPicker && (
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
                                            checked={JSON.parse(row.expression || "[]").includes(
                                                existingRow.scoreSheetRowId
                                            )}
                                            onChange={() =>
                                                toggleExpressionRow(existingRow.scoreSheetRowId)
                                            }
                                        />

                                        {existingRow.rowName}
                                    </label>
                                ))}
                        </div>
                    )}
                </div>
            )}

            <button type="submit">Add Row</button>
        </form>
    );
}

export default ScoreSheetRowForm;