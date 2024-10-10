package Sudoku;

import GameEngineV1.Tools.Methods;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuSolver {

    private SudokuGrid sudokuGrid;
    private GridTile[][] grid;

    SudokuSolver(SudokuGrid sudokuGrid) {
        this.sudokuGrid = sudokuGrid;
        this.grid = sudokuGrid.grid;
    }

    void solve() {
        autoPencil();
        boolean changeMade = true;
        boolean changeMade2;
        while (changeMade) {
            changeMade = false;
            changeMade2 = true;
            while (changeMade2) {
                changeMade2 = fillSingleCandidates();
                if (changeMade2) changeMade = true;
            }
            changeMade2 = true;
            while (changeMade2) {
                changeMade2 = fillUniqueSingles();
                if (changeMade2) changeMade = true;
            }
            changeMade2 = true;
            while (changeMade2) {
                changeMade2 = removeBoxAndRowColInteractionCandidates(false);
                if (changeMade2) changeMade = true;
            }
            changeMade2 = true;
            while (changeMade2) {
                changeMade2 = removeNakedSubsetCandidates(false);
                if (changeMade2) changeMade = true;
            }


            updatePencil();
        }
    }

    int pencilCount(GridTile t) {
        int count = 0;
        for (int i: t.pencil) {
            if (i!=0) count++;
        }
        return count;
    }
    int[] filteredPencil(GridTile t) {
        int[] pen = new  int[pencilCount(t)];
        int i = 0;
        for (int p: t.pencil) {
            if (p!=0) {
                pen[i] = p;
                i++;
            }
        }
        return pen;
    }

    void updatePencil() {
        for (int y = 0;y<9;y++) {
            for (int x = 0;x<9;x++) {
                GridTile t = grid[y][x];
                if (t.value==0) {
                    int[] p = t.pencil;
                    for (GridTile tempTile: sudokuGrid.getRow(t)) {
                        if (tempTile!=t&tempTile.value!=0) {
                            if (Methods.arrayContains(p, tempTile.value)) {
                                p[tempTile.value-1]=0;
                            }
                        }
                    }
                    for (GridTile tempTile: sudokuGrid.getColumn(t)) {
                        if (tempTile!=t&tempTile.value!=0) {
                            if (Methods.arrayContains(p, tempTile.value)) {
                                p[tempTile.value-1]=0;
                            }
                        }
                    }
                    for (GridTile tempTile: sudokuGrid.getBox(t)) {
                        if (tempTile!=t&tempTile.value!=0) {
                            if (Methods.arrayContains(p, tempTile.value)) {
                                p[tempTile.value-1]=0;
                            }
                        }
                    }
                    t.pencil = p;
                }
            }
        }
    }
    void autoPencil() {
        for (int y = 0;y<9;y++) {
            for (int x = 0;x<9;x++) {
                GridTile t = grid[y][x];
                if (t.value==0) {
                    int[] p = {1,2,3,4,5,6,7,8,9};
                    for (GridTile tempTile: sudokuGrid.getRow(t)) {
                        if (tempTile!=t&tempTile.value!=0) {
                            if (Methods.arrayContains(p, tempTile.value)) {
                                p[tempTile.value-1]=0;
                            }
                        }
                    }
                    for (GridTile tempTile: sudokuGrid.getColumn(t)) {
                        if (tempTile!=t&tempTile.value!=0) {
                            if (Methods.arrayContains(p, tempTile.value)) {
                                p[tempTile.value-1]=0;
                            }
                        }
                    }
                    for (GridTile tempTile: sudokuGrid.getBox(t)) {
                        if (tempTile!=t&tempTile.value!=0) {
                            if (Methods.arrayContains(p, tempTile.value)) {
                                p[tempTile.value-1]=0;
                            }
                        }
                    }
                    t.pencil = p;
                }
            }
        }
    }


    boolean fillSingleCandidates() {
        updatePencil();
        boolean changeMade = false;
        for (GridTile[] row : grid) {
            for (GridTile tile : row) {
                if (tile.value == 0) {
                    int single = 0;
                    for (int i = 0; i < 9; i++) {
                        if (tile.pencil[i] != 0) {
                            if (single == 0) {
                                single = tile.pencil[i];
                            } else {
                                single = 0;
                                break;
                            }
                        }

                    }
                    if (single != 0) {
                        tile.value = single;
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }

    boolean fillUniqueSingles() {
        updatePencil();
        boolean changeMade = false;
        int count;
        for (int i = 0;i<9;i++) {
            for (int y = 0;y<9;y++) {
                count = 0;
                GridTile currentTile = null;
                for (GridTile tile: sudokuGrid.getRow(y)) {
                    if (tile.value==0) {
                        if (tile.pencil[i]==i+1) {
                            count++;
                            currentTile = tile;
                        }
                    }
                }
                if (count == 1) {
                    currentTile.value = i+1;
                    updatePencil();
                    changeMade = true;
                }
            }
            for (int x = 0;x<9;x++) {
                count = 0;
                GridTile currentTile = null;
                for (GridTile tile: sudokuGrid.getColumn(x)) {
                    if (tile.value==0) {
                        if (tile.pencil[i]==i+1) {
                            count++;
                            currentTile = tile;
                        }
                    }
                }
                if (count == 1) {
                    currentTile.value = i+1;
                    updatePencil();
                    changeMade = true;
                }
            }
            for (int y = 0;y<9;y+=3) {
                for (int x = 0; x < 9; x+=3) {
                    count = 0;
                    GridTile currentTile = null;
                    for (GridTile tile : sudokuGrid.getBox(x,y)) {
                        if (tile.value == 0) {
                            if (tile.pencil[i] == i + 1) {
                                count++;
                                currentTile = tile;
                            }
                        }
                    }
                    if (count == 1) {
                        currentTile.value = i + 1;
                        updatePencil();
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }

    boolean removeBoxAndRowColInteractionCandidates(boolean print) {
        updatePencil();
        boolean changeMade = false;
        for (int i = 0;i<9;i++) {
            for (int y = 0; y < 9; y += 3) {
                for (int x = 0; x < 9; x += 3) {
                    GridTile[] box = sudokuGrid.getBox(x, y);
                    // Box to Row
                    int mainPos = -1;
                    int numCount = 0;
                    for (int y2 = 0; y2 < 3; y2++) {
                        for (int x2 = 0; x2 < 3; x2++) {
                            if (box[y2 * 3 + x2].value==0) {
                                if (box[y2 * 3 + x2].pencil[i] == i + 1) {
                                    if (mainPos == -1 || y2 == mainPos) {
                                        numCount++;
                                        mainPos = y2;
                                    } else {
                                        mainPos = -2;
                                    }
                                }
                            }
                        }
                    }
                    if (mainPos!=-1&mainPos!=-2&numCount>1) {
                        if (print) System.out.println("i: " + (i+1) + "\tX: " + x/3 + "\tY: " + y/3 + "\tRow: " +mainPos + "\tCount: " + numCount);
                        for (GridTile t: sudokuGrid.getRow(y+mainPos)) {
                            if (!Methods.arrayContains(box, t)) {
                                if (t.value==0&t.pencil[i]!=0) {
                                    t.pencil[i] = 0;
                                    changeMade = true;
                                    if (print) System.out.println(t.x + ", " + t.y);
                                }
                            }
                        }
                    }
                    // Box to Column
                    mainPos = -1;
                    numCount = 0;
                    for (int x2 = 0; x2 < 3; x2++) {
                        for (int y2 = 0; y2 < 3; y2++) {
                            if (box[y2 * 3 + x2].value==0) {
                                if (box[y2 * 3 + x2].pencil[i] == i + 1) {
                                    if (mainPos == -1 || x2 == mainPos) {
                                        numCount++;
                                        mainPos = x2;
                                    } else {
                                        mainPos = -2;
                                    }
                                }
                            }
                        }
                    }
                    if (mainPos!=-1&mainPos!=-2&numCount>1) {
                        if (print) System.out.println("i: " + (i+1) + "\tX: " + x/3 + "\tY: " + y/3 + "\tCol: " +mainPos + "\tCount: " + numCount);
                        for (GridTile t: sudokuGrid.getColumn(x+mainPos)) {
                            if (!Methods.arrayContains(box, t)) {
                                if (t.value==0&t.pencil[i]!=0) {
                                    t.pencil[i] = 0;
                                    changeMade = true;
                                    if (print) System.out.println(t.x + ", " + t.y);
                                }
                            }
                        }
                    }
                }
            }
            // Row to Box
            for (int y = 0;y<9;y++) {
                GridTile[] row = sudokuGrid.getRow(y);

                int mainPos = -1;
                int numCount = 0;
                for (int x = 0;x<9;x++) {
                    if (row[x].value==0) {
                        if (row[x].pencil[i] == i + 1) {
                            if (mainPos==-1|mainPos==x/3) {
                                numCount++;
                                mainPos = x/3;
                            }
                            else mainPos = -2;
                        }
                    }
                }
                if (mainPos!=-1&mainPos!=-2&numCount>1) {
                    if (print) System.out.println("i: " + (i+1) + "\tY: " + y + "\tBoxX: " +mainPos + "\tBoxY: " + y/3 + "\tCount: " + numCount);
                    for (GridTile t: sudokuGrid.getBox(mainPos*3,y)) {
                        if (!Methods.arrayContains(row, t)) {
                            if (t.value==0&t.pencil[i]!=0) {
                                t.pencil[i] = 0;
                                changeMade = true;
                                if (print) System.out.println(t.x + ", " + t.y);
                            }
                        }
                    }
                }
            }
            // Column to Box
            for (int x = 0;x<9;x++) {
                GridTile[] col = sudokuGrid.getColumn(x);

                int mainPos = -1;
                int numCount = 0;
                for (int y = 0;y<9;y++) {
                    if (col[y].value==0) {
                        if (col[y].pencil[i] == i + 1) {
                            if (mainPos==-1|mainPos==y/3) {
                                numCount++;
                                mainPos = y/3;
                            }
                            else mainPos = -2;
                        }
                    }
                }
                if (mainPos!=-1&mainPos!=-2&numCount>1) {
                    if (print) System.out.println("i: " + (i+1) + "\tX: " + x + "\tBoxX: " +x/3 + "\tBoxY: " + mainPos + "\tCount: " + numCount);
                    for (GridTile t: sudokuGrid.getBox(x,mainPos*3)) {
                        if (!Methods.arrayContains(col, t)) {
                            if (t.value==0&t.pencil[i]!=0) {
                                t.pencil[i] = 0;
                                changeMade = true;
                                if (print) System.out.println(t.x + ", " + t.y);
                            }
                        }
                    }
                }
            }
        }
        return changeMade;
    }

    boolean removeNakedSubsetCandidates(boolean print) {
        updatePencil();
        boolean changeMade = false;

        for (int i = 2;i<=4;i++) {
            for (int xy = 0; xy < 9; xy++) {
                if (solveNakedSubset(i, sudokuGrid.getRow(xy),print)) changeMade = true;
                if (solveNakedSubset(i, sudokuGrid.getColumn(xy),print)) changeMade = true;
            }
            for (int y = 0;y<9;y+=3) {
                for (int x = 0;x<9;x+=3) {
                    if (solveNakedSubset(i, sudokuGrid.getBox(x,y),print)) changeMade = true;
                }
            }
        }
        return changeMade;
    }
    private boolean solveNakedSubset(int i, GridTile[] tileList, boolean print) {
        boolean changeMade = false;
        for (GridTile tile: tileList) {
            if (tile.value==0) {
                if (pencilCount(tile) == i) {
                    ArrayList<GridTile> tiles = new ArrayList<>();
                    tiles.add(tile);
                    for (GridTile t: tileList) {
                        if (t!=tile&t.value==0) {
                            if (Arrays.equals(filteredPencil(tile),filteredPencil(t))) {
                                tiles.add(t);
                            }
                        }
                    }
                    if (tiles.size()==i) {
                        for (GridTile t: tileList) {
                            if (!tiles.contains(t)) {
                                for (int p: filteredPencil(tile)) {
                                    if (t.pencil[p-1] != 0) {
                                        if (print) System.out.println(t.x + ", " + t.y);
                                        t.pencil[p-1] = 0;
                                        changeMade = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changeMade;
    }


}
