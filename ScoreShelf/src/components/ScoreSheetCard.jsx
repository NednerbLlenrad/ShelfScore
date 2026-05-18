function ScoreSheetCard({
  scoreSheet,
  selected,
  onSelect
}) {
  return (
    <article
      className={`score-sheet-card ${
        selected ? "selected-score-sheet" : ""
      }`}
      onClick={() => onSelect(scoreSheet)}
    >
      <h2>{scoreSheet.scoreSheetName}</h2>
    </article>
  );
}

export default ScoreSheetCard;