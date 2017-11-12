package sample.helpers;

public class Pair{
    private Integer row;
    private Integer column;

    public Pair(Integer f, Integer s) {
        row = f;
        column = s;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }
}