package learn.scoreshelf.models;

import jakarta.validation.constraints.*;

public class ScoreSheetRow {

    private int scoreSheetRowId;

    @Min(value = 1, message = "Score sheet ID is required.")
    private int scoreSheetId;

    @NotBlank(message = "Row name is required.")
    @Size(max = 50, message = "Row name must be 50 characters or less.")
    private String rowName;

    @Min(value = 1, message = "Display order must be greater than 0.")
    private int displayOrder;

    @NotNull(message = "Row type is required.")
    private RowType rowType;

    private String expression;

    public int getScoreSheetRowId() {
        return scoreSheetRowId;
    }

    public void setScoreSheetRowId(int scoreSheetRowId) {
        this.scoreSheetRowId = scoreSheetRowId;
    }

    public int getScoreSheetId() {
        return scoreSheetId;
    }

    public void setScoreSheetId(int scoreSheetId) {
        this.scoreSheetId = scoreSheetId;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public RowType getRowType() {
        return rowType;
    }

    public void setRowType(RowType rowType) {
        this.rowType = rowType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}