package Sudoku;

import GameEngineV1.Game;
import GameEngineV1.GameObject;
import GameEngineV1.Tools.Methods;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Arrays;

public class SudokuGrid extends GameObject {

    GridTile[][] grid = new GridTile[9][9];
    InitialGrid initialGrid;

    private SudokuSolver sudokuSolver = new SudokuSolver(this);

    private static int X = 25, Y = 25, SIZE = 550;

    boolean pencilMode = false;
    boolean eraseMode = false;
    int currentNum = 0;

    String gridName;

    boolean settingUp = false;
    int setNum = 1;
    private int currentKey = -1;
    private String currentLetter = "";
    private boolean mousePressed = false;
    
    private int mouseX, mouseY;

    int gap = 7;
    int gap2 = 4;

    public SudokuGrid(InitialGrid grid) {
        initialGrid = grid;
        resetGrid();
    }

    public void tick(Game game) {
        mouseX = game.mouseX;
        mouseY = game.mouseY;
        if (settingUp) {
            if (getKey(game)!=currentKey|!currentLetter.equals(getLetter(game).toLowerCase())) {
                currentKey = getKey(game);
                currentLetter = getLetter(game).toLowerCase();
                if (currentKey!=-1|!currentLetter.equals("")) {
                    if (currentKey==-2) {
                        if ((setNum>1&setNum<82)|gridName.length()==0) {
                            setNum--;
                            grid[(setNum - 1) / 9][(setNum - 1) % 9] = new GridTile((setNum - 1) % 9, (setNum - 1) / 9, 0, true);
                        }
                        else {
                            gridName = gridName.substring(0,gridName.length()-1);
                        }
                    }
                    else {
                        if (setNum<82) {
                            if (currentKey == 0) grid[(setNum - 1) / 9][(setNum - 1) % 9] = new GridTile((setNum - 1) % 9, (setNum - 1) / 9, currentKey, false);
                            else grid[(setNum - 1) / 9][(setNum - 1) % 9] = new GridTile((setNum - 1) % 9, (setNum - 1) / 9, currentKey, true);
                            setNum++;
                        }
                        else {
                            if (!getLetter(game).equals("")) {
                                gridName+=getLetter(game);

                            }
                        }
                    }
                }
            }

        }
        else {
            for (int y = 0;y<3;y++) {
                for (int x = 0;x<3;x++) {
                    int size = (SIZE-gap*4)/3;
                    int px = X+(gap*(x+1))+x*size;
                    int py = Y+(gap*(y+1))+y*size;

                    for (int y2 = 0;y2<3;y2++) {
                        for (int x2 = 0; x2 < 3; x2++) {
                            int size2 = ((size - gap2 * 4) / 3);
                            int px2 = px + (gap2 * (x2 + 1)) + x2 * size2;
                            int py2 = py+(gap2*(y2+1))+ y2 * size2;

                            if (mouseX > px2 & mouseX < px2 + size2 & mouseY > py2 & mouseY < py2 + size2) {
                                if (game.mouseDown[0]) {
                                    if (!mousePressed) {
                                        if (!grid[y * 3 + y2][x * 3 + x2].initialTile) {
                                            if (eraseMode) {
                                                if (pencilMode) {
                                                    if (grid[y * 3 + y2][x * 3 + x2].value == 0) {
                                                        grid[y * 3 + y2][x * 3 + x2].pencil[currentNum - 1] = 0;
                                                    }
                                                } else {
                                                    grid[y * 3 + y2][x * 3 + x2].value = 0;
                                                }
                                                eraseMode = false;
                                            } else if (currentNum != 0) {
                                                if (pencilMode) {
                                                    if (grid[y * 3 + y2][x * 3 + x2].value == 0) {
                                                        grid[y * 3 + y2][x * 3 + x2].pencil[currentNum - 1] = currentNum;
                                                    }
                                                } else {
                                                    grid[y * 3 + y2][x * 3 + x2].value = currentNum;
                                                    updatePencil();
                                                }
                                            }
                                        }
                                    }
                                    mousePressed = true;
                                }
                                else {
                                    mousePressed = false;
                                }
                                if (getKey(game)!=currentKey) {
                                    currentKey = getKey(game);
                                    if (currentKey!=-1&currentKey!=-2) {
                                        if (!grid[y * 3 + y2][x * 3 + x2].initialTile) {
                                            if (currentKey == 0) {
                                                grid[y * 3 + y2][x * 3 + x2].value = 0;
                                                eraseMode = false;
                                            }
                                            else {
                                                if (pencilMode) {
                                                    if (eraseMode) {
                                                        if (grid[y * 3 + y2][x * 3 + x2].value == 0) {
                                                            grid[y * 3 + y2][x * 3 + x2].pencil[currentKey - 1] = 0;
                                                            eraseMode = false;
                                                        }
                                                    }
                                                    else if (grid[y * 3 + y2][x * 3 + x2].value == 0) {
                                                        if (grid[y * 3 + y2][x * 3 + x2].pencil[currentKey-1]==currentKey) {
                                                            grid[y * 3 + y2][x * 3 + x2].pencil[currentKey - 1] = 0;
                                                        }
                                                        else {
                                                            grid[y * 3 + y2][x * 3 + x2].pencil[currentKey - 1] = currentKey;
                                                            currentNum = currentKey;
                                                        }
                                                    }
                                                }
                                                else {
                                                    grid[y * 3 + y2][x * 3 + x2].value = currentKey;
                                                    currentNum=currentKey;
                                                    updatePencil();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int getKey(Game game) {
        if (game.keysDown[KeyEvent.VK_0]||game.keysDown[KeyEvent.VK_SPACE]||game.keysDown[KeyEvent.VK_NUMPAD0]) return 0;
        if (game.keysDown[KeyEvent.VK_1]||game.keysDown[KeyEvent.VK_NUMPAD1]) return 1;
        if (game.keysDown[KeyEvent.VK_2]||game.keysDown[KeyEvent.VK_NUMPAD2]) return 2;
        if (game.keysDown[KeyEvent.VK_3]||game.keysDown[KeyEvent.VK_NUMPAD3]) return 3;
        if (game.keysDown[KeyEvent.VK_4]||game.keysDown[KeyEvent.VK_NUMPAD4]) return 4;
        if (game.keysDown[KeyEvent.VK_5]||game.keysDown[KeyEvent.VK_NUMPAD5]) return 5;
        if (game.keysDown[KeyEvent.VK_6]||game.keysDown[KeyEvent.VK_NUMPAD6]) return 6;
        if (game.keysDown[KeyEvent.VK_7]||game.keysDown[KeyEvent.VK_NUMPAD7]) return 7;
        if (game.keysDown[KeyEvent.VK_8]||game.keysDown[KeyEvent.VK_NUMPAD8]) return 8;
        if (game.keysDown[KeyEvent.VK_9]||game.keysDown[KeyEvent.VK_NUMPAD9]) return 9;
        if (game.keysDown[KeyEvent.VK_BACK_SPACE]) return -2;
        return -1;
    }
    private String getLetter(Game game) {
        if (game.keysDown[KeyEvent.VK_0]||game.keysDown[KeyEvent.VK_NUMPAD0]) return "0";
        if (game.keysDown[KeyEvent.VK_1]||game.keysDown[KeyEvent.VK_NUMPAD1]) return "1";
        if (game.keysDown[KeyEvent.VK_2]||game.keysDown[KeyEvent.VK_NUMPAD2]) return "2";
        if (game.keysDown[KeyEvent.VK_3]||game.keysDown[KeyEvent.VK_NUMPAD3]) return "3";
        if (game.keysDown[KeyEvent.VK_4]||game.keysDown[KeyEvent.VK_NUMPAD4]) return "4";
        if (game.keysDown[KeyEvent.VK_5]||game.keysDown[KeyEvent.VK_NUMPAD5]) return "5";
        if (game.keysDown[KeyEvent.VK_6]||game.keysDown[KeyEvent.VK_NUMPAD6]) return "6";
        if (game.keysDown[KeyEvent.VK_7]||game.keysDown[KeyEvent.VK_NUMPAD7]) return "7";
        if (game.keysDown[KeyEvent.VK_8]||game.keysDown[KeyEvent.VK_NUMPAD8]) return "8";
        if (game.keysDown[KeyEvent.VK_9]||game.keysDown[KeyEvent.VK_NUMPAD9]) return "9";

        if (game.keysDown[KeyEvent.VK_A]&game.keysDown[KeyEvent.VK_SHIFT]) return "A";
        if (game.keysDown[KeyEvent.VK_A]) return "a";
        if (game.keysDown[KeyEvent.VK_B]&game.keysDown[KeyEvent.VK_SHIFT]) return "B";
        if (game.keysDown[KeyEvent.VK_B]) return "b";
        if (game.keysDown[KeyEvent.VK_C]&game.keysDown[KeyEvent.VK_SHIFT]) return "C";
        if (game.keysDown[KeyEvent.VK_C]) return "c";
        if (game.keysDown[KeyEvent.VK_D]&game.keysDown[KeyEvent.VK_SHIFT]) return "D";
        if (game.keysDown[KeyEvent.VK_D]) return "d";
        if (game.keysDown[KeyEvent.VK_E]&game.keysDown[KeyEvent.VK_SHIFT]) return "E";
        if (game.keysDown[KeyEvent.VK_E]) return "e";
        if (game.keysDown[KeyEvent.VK_F]&game.keysDown[KeyEvent.VK_SHIFT]) return "F";
        if (game.keysDown[KeyEvent.VK_F]) return "f";
        if (game.keysDown[KeyEvent.VK_G]&game.keysDown[KeyEvent.VK_SHIFT]) return "G";
        if (game.keysDown[KeyEvent.VK_G]) return "g";
        if (game.keysDown[KeyEvent.VK_H]&game.keysDown[KeyEvent.VK_SHIFT]) return "H";
        if (game.keysDown[KeyEvent.VK_H]) return "h";
        if (game.keysDown[KeyEvent.VK_I]&game.keysDown[KeyEvent.VK_SHIFT]) return "I";
        if (game.keysDown[KeyEvent.VK_I]) return "i";
        if (game.keysDown[KeyEvent.VK_J]&game.keysDown[KeyEvent.VK_SHIFT]) return "J";
        if (game.keysDown[KeyEvent.VK_J]) return "j";
        if (game.keysDown[KeyEvent.VK_K]&game.keysDown[KeyEvent.VK_SHIFT]) return "K";
        if (game.keysDown[KeyEvent.VK_K]) return "k";
        if (game.keysDown[KeyEvent.VK_L]&game.keysDown[KeyEvent.VK_SHIFT]) return "L";
        if (game.keysDown[KeyEvent.VK_L]) return "l";
        if (game.keysDown[KeyEvent.VK_M]&game.keysDown[KeyEvent.VK_SHIFT]) return "M";
        if (game.keysDown[KeyEvent.VK_M]) return "m";
        if (game.keysDown[KeyEvent.VK_N]&game.keysDown[KeyEvent.VK_SHIFT]) return "N";
        if (game.keysDown[KeyEvent.VK_N]) return "n";
        if (game.keysDown[KeyEvent.VK_O]&game.keysDown[KeyEvent.VK_SHIFT]) return "O";
        if (game.keysDown[KeyEvent.VK_O]) return "o";
        if (game.keysDown[KeyEvent.VK_P]&game.keysDown[KeyEvent.VK_SHIFT]) return "P";
        if (game.keysDown[KeyEvent.VK_P]) return "p";
        if (game.keysDown[KeyEvent.VK_Q]&game.keysDown[KeyEvent.VK_SHIFT]) return "Q";
        if (game.keysDown[KeyEvent.VK_Q]) return "q";
        if (game.keysDown[KeyEvent.VK_R]&game.keysDown[KeyEvent.VK_SHIFT]) return "R";
        if (game.keysDown[KeyEvent.VK_R]) return "r";
        if (game.keysDown[KeyEvent.VK_S]&game.keysDown[KeyEvent.VK_SHIFT]) return "S";
        if (game.keysDown[KeyEvent.VK_S]) return "s";
        if (game.keysDown[KeyEvent.VK_T]&game.keysDown[KeyEvent.VK_SHIFT]) return "T";
        if (game.keysDown[KeyEvent.VK_T]) return "t";
        if (game.keysDown[KeyEvent.VK_U]&game.keysDown[KeyEvent.VK_SHIFT]) return "U";
        if (game.keysDown[KeyEvent.VK_U]) return "u";
        if (game.keysDown[KeyEvent.VK_V]&game.keysDown[KeyEvent.VK_SHIFT]) return "V";
        if (game.keysDown[KeyEvent.VK_V]) return "v";
        if (game.keysDown[KeyEvent.VK_W]&game.keysDown[KeyEvent.VK_SHIFT]) return "W";
        if (game.keysDown[KeyEvent.VK_W]) return "w";
        if (game.keysDown[KeyEvent.VK_X]&game.keysDown[KeyEvent.VK_SHIFT]) return "X";
        if (game.keysDown[KeyEvent.VK_X]) return "x";
        if (game.keysDown[KeyEvent.VK_Y]&game.keysDown[KeyEvent.VK_SHIFT]) return "Y";
        if (game.keysDown[KeyEvent.VK_Y]) return "y";
        if (game.keysDown[KeyEvent.VK_Z]&game.keysDown[KeyEvent.VK_SHIFT]) return "Z";
        if (game.keysDown[KeyEvent.VK_Z]) return "z";

        return "";
    }

    public void render(Graphics g) {
        g.setColor(Sudoku.color1);
        g.fillRoundRect(X,Y,SIZE,SIZE, 20,20);

        g.setColor(Sudoku.color1);
        g.setFont(new Font("Century Gothic",Font.BOLD,22));
        if (setNum>81&System.currentTimeMillis() % 800 > 400) g.drawString("Current Grid: " + gridName + "_",25,21);
        else g.drawString("Current Grid: " + gridName,25,21);



        for (int y = 0;y<3;y++) {
            for (int x = 0;x<3;x++) {
                int size = (SIZE-gap*4)/3;
                int px = X+(gap*(x+1))+x*size;
                int py = Y+(gap*(y+1))+y*size;
                g.setColor(Sudoku.color0);
                g.fillRoundRect(px,py,size,size,10,10);
                g.setColor(Sudoku.color1);
                g.drawRoundRect(px,py,size,size,10,10); //Maybe get rid of?

                for (int y2 = 0;y2<3;y2++) {
                    for (int x2 = 0; x2 < 3; x2++) {
                        int size2 = ((size - gap2 * 4) / 3);
                        int px2 = px + (gap2 * (x2 + 1)) + x2 * size2;
                        int py2 = py+(gap2*(y2+1))+ y2 * size2;
                        if (!grid[y*3+y2][x*3+x2].initialTile&mouseX>px2&mouseX<px2+size2&mouseY>py2&mouseY<py2+size2) {
                            if (Sudoku.darkMode) g.setColor(Sudoku.color1.brighter());
                            else g.setColor(Sudoku.color1.darker());
                        }
                        else if (grid[y*3+y2][x*3+x2].value!=0&grid[y*3+y2][x*3+x2].value==currentNum) {
                            if (Sudoku.darkMode) g.setColor(Sudoku.color1.darker());
                            else g.setColor(Sudoku.color1.brighter());
                        }
                        else g.setColor(Sudoku.color1);
                        g.fillRoundRect(px2, py2,size2,size2,10,10);
                        if (currentNum!=0&grid[y*3+y2][x*3+x2].value==0&Methods.arrayContains(grid[y*3+y2][x*3+x2].pencil,currentNum)) {
                            ((Graphics2D) g).setStroke(new BasicStroke(3));
                            if (Sudoku.darkMode) g.setColor(Sudoku.color1.brighter());
                                else g.setColor(Sudoku.color1.darker());
                            g.drawRoundRect(px2+1, py2+1,size2-2,size2-2,10,10);
                        }

                        if (grid[y*3+y2][x*3+x2].value!=0) {
                            if (grid[y*3+y2][x*3+x2].initialTile) {
                                if (Sudoku.darkMode) g.setColor(Sudoku.color1.darker().darker());
                                else g.setColor(Sudoku.color1.brighter().brighter());
                                //g.setColor(new Color(215, 215, 215));
                            }
                            else if (Sudoku.darkMode) g.setColor(Color.black);
                            else {
                                g.setColor(Sudoku.color0);
                            }
                            g.setFont(new Font("Century Gothic",Font.PLAIN,40));
                            Methods.drawCenteredString(g,Integer.toString(grid[y*3+y2][x*3+x2].value),new Rectangle(px2, py2,size2,size2));
                        }
                        else {
                            g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
                            g.setColor(Sudoku.color0);
                            int[] pencil = grid[y*3+y2][x*3+x2].pencil;
                            int[][] pen1 = {{pencil[0],pencil[1],pencil[2]},
                                    {pencil[3],pencil[4],pencil[5]},
                                    {pencil[6],pencil[7],pencil[8]}
                            };
                            drawCenteredString(g, pen1, new Rectangle(px2, py2, size2, size2), 4, -5);
                        }
                        if (settingUp) {
                            g.setColor(Color.white);
                            g.setFont(new Font("Century Gothic",Font.PLAIN,40));
                            if ((setNum - 1) / 9 == (y * 3 + y2) & (setNum - 1) % 9 == (x * 3 + x2)) {
                                if (System.currentTimeMillis() % 800 > 400)
                                    Methods.drawCenteredString(g, "_", new Rectangle(px2, py2, size2, size2));
                            }
                        }
                    }
                }
            }
        }
    }
    GridTile[] getRow(int y) {
        return grid[y];
    }
    GridTile[] getRow(GridTile t) {
        return getRow(t.y);
    }
    GridTile[] getColumn(int x) {
        GridTile[] col = new GridTile[9];
        for (int y = 0;y<9;y++) {
            col[y] = grid[y][x];
        }
        return col;
    }
    GridTile[] getColumn(GridTile t) {
        return getColumn(t.x);
    }
    GridTile[] getBox(int x, int y) {
        GridTile[] box = new GridTile[9];
        x = (x/3)*3;
        y = (y/3)*3;
        for (int y2 = 0;y2<3;y2++) {
            for (int x2 = 0;x2<3;x2++) {
                box[3*y2+x2] = grid[y+y2][x+x2];
            }
        }
        return box;
    }
    GridTile[] getBox(GridTile t) {
        return getBox(t.x,t.y);
    }
    public void clearGrid() {
        for (int y = 0;y<9;y++) {
            for (int x = 0;x<9;x++) {
                grid[y][x] = new GridTile(x,y,0,false);
            }
        }
    }
    public void resetGrid() {
        for (int y = 0;y<9;y++) {
            for (int x = 0;x<9;x++) {
                boolean initial = (initialGrid.grid[y][x]!=0);
                this.grid[y][x] = new GridTile(x,y,initialGrid.grid[y][x],initial);
            }
        }
    }
    void setInitGrid() {
        int[][] initGrid = new int[9][9];
        for (int y = 0;y<9;y++) {
            for (int x = 0;x<9;x++) {
                initGrid[y][x]=grid[y][x].value;
            }
        }
        initialGrid = new InitialGrid(initGrid);
    }
    public void saveInitialSetup() {
        try {
            FileOutputStream fileOut;
            if (gridName.equals("")) fileOut = new FileOutputStream("C:\\Users\\wadsworthb\\Desktop\\Java Projects\\SudokuSolver\\src\\Sudoku\\SavedGrids\\Grid" + Sudoku.direc.listFiles().length + ".ser");
            else fileOut = new FileOutputStream("C:\\Users\\wadsworthb\\Desktop\\Java Projects\\SudokuSolver\\src\\Sudoku\\SavedGrids\\" + gridName + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(initialGrid);
            out.close();
            fileOut.close();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
        System.out.println("Grid Setup Saved");
        for (File file: Sudoku.direc.listFiles()) {
            System.out.println(Arrays.deepToString(InitialGrid.deserialized(file).grid));
        }


    }
    void drawCenteredString(Graphics g, int[][] s, Rectangle rect, int xGap, int yGap) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        for (int y = 0;y<s.length;y++) {
            for (int x = 0;x<s[y].length;x++) {

                int xp = rect.x + (xGap * (x + 1)) + (metrics.stringWidth(Integer.toString(s[y][x])) * (x + 1));
                int yp = rect.y + yGap*(y+1) + metrics.getHeight()*(y+1);

                if (s[y][x]!=0) {
                    g.drawString(Integer.toString(s[y][x]), xp-5, yp);
                }
            }
        }
    }


    void updatePencil() {
        sudokuSolver.updatePencil();
    }
    void autoPencil() {
        sudokuSolver.autoPencil();
    }

    public void solve() {
        sudokuSolver.solve();
    }


}
