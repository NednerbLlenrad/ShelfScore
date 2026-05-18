function ScoreSheetAddForm({
  newScoreSheetName,
  setNewScoreSheetName,
  onSubmit
}) {
  return (
    <form className="score-sheet-add-form" onSubmit={onSubmit}>
      <input
        type="text"
        placeholder="New score sheet name"
        value={newScoreSheetName}
        onChange={(event) => setNewScoreSheetName(event.target.value)}
      />

      <button type="submit">Add</button>
    </form>
  );
}

export default ScoreSheetAddForm;