package Sudoku;

public class GridTile {
    public boolean initialTile;
    public int value;
    public int[] pencil = new int[9];
    public int x, y;

    public GridTile(int x, int y, int value, boolean isInitial) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.initialTile = isInitial;
    }

}
