package Sudoku;

import GameEngineV1.Game;
import GameEngineV1.GameInterface;
import GameEngineV1.Tools.Methods;
import GameEngineV1.Tools.OptionBox;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Sudoku implements GameInterface {

    Game game;
    SudokuGrid grid;
    private int currentGrid = -1;
    static File direc = new File("src/Sudoku/SavedGrids");

    private boolean findingNewGrid = false;
    private ArrayList<ArrayList<File>> potGrids= new ArrayList<>();
    private int gridGroupNum = 0;
    private ArrayList<OptionBox> currentPotGrids = new ArrayList<>();

    public static Color color0 = Color.white;
    public static Color color1 = new Color(31, 114, 203);//new Color(237, 207, 114);
    public static Color color2 = new Color(78, 186, 113);//new Color(237, 207, 114);
    public static Color color3 = new Color(202, 203, 109);//new Color(237, 207, 114);
    public static boolean darkMode = false;

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.game = new Game(sudoku,800,600,"Sudoku Solver");
        sudoku.start();
    }

    public void start() {
        if (darkMode) {
            //game.setBackgroundColor(Color.black);
            //color0 = Color.black;
        }
        newGrid();
        setupBoxes();
    }
    private void setupBoxes() {
        int blockX = 600;
        int blockY = 32;

        OptionBox setUp = new OptionBox(blockX, blockY, 175, 30);
        setUp.setClickBlock(()->{
            if (grid.settingUp) {
                grid.settingUp = false;
                grid.setNum = 1;
                grid.setInitGrid();
            }
            else {
                grid.clearGrid();
                grid.gridName = "";
                grid.settingUp = true;
            }
        });
        game.addBox(setUp);
        blockY+=35;

        OptionBox clearGrid = new OptionBox(blockX, blockY, 175, 30);
        clearGrid.setText("Clear Grid");
        clearGrid.setClickBlock(()->{
            grid.clearGrid();
            currentGrid = -1;
        });
        game.addBox(clearGrid);
        blockY+=35;

        OptionBox saveSetup = new OptionBox(blockX, blockY, 175, 30);
        saveSetup.setText("Save Setup");
        saveSetup.setClickBlock(()-> {
            grid.saveInitialSetup();
        });
        game.addBox(saveSetup);
        blockY+=35;

        OptionBox resetGrid = new OptionBox(blockX, blockY, 175, 30);
        resetGrid.setText("Reset Grid");
        resetGrid.setClickBlock(()-> grid.resetGrid());
        game.addBox(resetGrid);
        blockY+=35;

        OptionBox newGrid = new OptionBox(blockX, blockY, 175, 30);
        newGrid.setText("New Grid");
        newGrid.setClickBlock(()-> {
            findingNewGrid = !findingNewGrid;
            newGrid.setText(findingNewGrid ? "Cancel":"New Grid");
            if (findingNewGrid) {
                findNewGrid();
            }
            else {
                for (OptionBox b: currentPotGrids) {
                    game.removeBox(b);
                }
            }

        });//newGrid());
        game.addBox(newGrid);

        for (OptionBox box: game.getBoxes()) {
            box.setColor(color1);
            box.setFontColor(color0);
            box.setFont(new Font("Century Gothic",Font.BOLD,22));
            box.setTickBlock(() -> {
                if (box.mouseOver) box.setColor(color1.darker());
                else box.setColor(color1);
            });
        }
        setUp.setTickBlock(() -> {
            if (setUp.mouseOver) setUp.setColor(color1.darker());
            else setUp.setColor(color1);
            if (grid.settingUp) {
                setUp.setText("Confirm");
            }
            else setUp.setText("Setup Grid");
        });
        newGrid.setTickBlock(()-> {
            if (newGrid.mouseOver) newGrid.setColor(color1.darker());
            else newGrid.setColor(color1);
            newGrid.setText(findingNewGrid ? "Cancel":"New Grid");
        });

        blockY = 222;
        OptionBox autoPencil = new OptionBox(blockX, blockY, 175, 60);
        autoPencil.setText("Auto Pencil");
        autoPencil.setClickBlock(()-> grid.autoPencil());
        autoPencil.setColor(color2);
        autoPencil.setFontColor(color0);
        autoPencil.setFont(new Font("Century Gothic",Font.BOLD,30));
        autoPencil.setTickBlock(() -> {
            autoPencil.clickable = !findingNewGrid;
            if (findingNewGrid) {
                autoPencil.setColor(Methods.brighter(autoPencil.getColor(),.8));
            }
            else {
                if (autoPencil.mouseOver) autoPencil.setColor(color2.darker());
                else autoPencil.setColor(color2);
            }
        });

        game.addBox(autoPencil);
        blockY+=66;

        OptionBox solve = new OptionBox(blockX, blockY, 175, 60);
        solve.setText("Solve Grid");
        solve.setClickBlock(()-> grid.solve());
        solve.setColor(color2);
        solve.setFontColor(color0);
        solve.setFont(new Font("Century Gothic",Font.BOLD,30));
        solve.setTickBlock(() -> {
            solve.clickable = !findingNewGrid;
            if (findingNewGrid) {
                solve.setColor(Methods.brighter(solve.getColor(),.8));
            }
            else {
                if (solve.mouseOver) solve.setColor(color2.darker());
                else solve.setColor(color2);
            }
        });

        game.addBox(solve);

        setupControls();
    }
    private void setupControls() {
        int blockX = 600;
        int blockY = 368;

        OptionBox pencil = new OptionBox(blockX, blockY, 85, 35);
        pencil.setText("Pencil");
        pencil.setBorderColor(color3);
        pencil.setFontColor(color3);
        pencil.borderOn(true);
        pencil.setBorderThickness(3);
        pencil.setFont(new Font("Century Gothic",Font.BOLD,25));
        pencil.setClickBlock(()-> {
            if (grid.pencilMode) {
                grid.pencilMode = false;
            }
            else {
                grid.pencilMode = true;
            }
        });
        pencil.setTickBlock(()-> {
            pencil.clickable = !findingNewGrid;
            if (findingNewGrid) {
                if (!grid.pencilMode) {
                    pencil.setBorderColor(Methods.brighter(pencil.getBorderColor(),.9));
                    pencil.setFontColor(Methods.brighter(pencil.getFontColor(),.9));
                }
                else {
                    pencil.setColor(Methods.brighter(pencil.getColor(),.9));
                }
            }
            else {
                if (!grid.pencilMode) {
                    pencil.setBorderColor(color3);
                    pencil.setFontColor(color3);
                    pencil.borderOn(true);
                    if (pencil.mouseOver) pencil.setColor(color3.brighter().brighter());
                    else pencil.setColor(color0);
                } else {
                    pencil.setColor(color3);
                    pencil.setFontColor(color0);
                    pencil.borderOn(false);
                }
            }
        });
        game.addBox(pencil);
        blockX+=90;

        OptionBox erase = new OptionBox(blockX, blockY, 85, 35);
        erase.setText("Erase");
        erase.setBorderColor(color3);
        erase.setFontColor(color3);
        erase.borderOn(true);
        erase.setBorderThickness(3);
        erase.setFont(new Font("Century Gothic",Font.BOLD,25));
        erase.setTickBlock(()-> {
            erase.clickable = !findingNewGrid;
            if (findingNewGrid) {
                if (!grid.pencilMode) {
                    erase.setBorderColor(Methods.brighter(erase.getBorderColor(),.9));
                    erase.setFontColor(Methods.brighter(erase.getFontColor(),.9));
                }
                else {
                    erase.setColor(Methods.brighter(erase.getColor(),.9));
                }
            }
            else {
                if (grid.eraseMode) {
                    erase.setColor(color3);
                    erase.setFontColor(color0);
                    erase.borderOn(false);
                } else {
                    erase.setBorderColor(color3);
                    erase.setFontColor(color3);
                    erase.borderOn(true);
                    if (erase.mouseOver) erase.setColor(color3.brighter().brighter());
                    else erase.setColor(color0);
                }
            }
        });
        erase.setClickBlock(()-> {
            if (grid.eraseMode) {
                grid.eraseMode = false;
                erase.setBorderColor(color3);
                erase.setColor(Color.white);
                erase.setFontColor(color3);
                erase.borderOn(true);
            }
            else {
                grid.eraseMode = true;
                erase.setColor(color3);
                erase.setFontColor(color0);
                erase.borderOn(false);
            }
        });
        game.addBox(erase);
        blockX-=90;
        blockY+=44;
        OptionBox[] boxes = new OptionBox[9];
        for (int y = 0;y<3;y++) {
            for (int x = 0;x<3;x++) {
                boxes[3*y+x] = new OptionBox(blockX,blockY,55,48);
                blockX+=60;
            }
            blockX-=180;
            blockY+=53;
        }
        for (int i = 0;i<9;i++) {
            boxes[i].setText(Integer.toString(i+1));
            boxes[i].setBorderColor(color3);
            boxes[i].setFontColor(color3);
            boxes[i].borderOn(true);
            boxes[i].setBorderThickness(3);
            boxes[i].setFont(new Font("Century Gothic",Font.BOLD,30));
            int finalI = i;
            boxes[i].setClickBlock(()-> grid.currentNum = finalI + 1);
            boxes[i].setTickBlock(()-> {
                boxes[finalI].clickable = !findingNewGrid;
                if (findingNewGrid) {
                    if (grid.currentNum != finalI + 1) {
                        boxes[finalI].setBorderColor(Methods.brighter(boxes[finalI].getBorderColor(),.9));
                        boxes[finalI].setFontColor(Methods.brighter(boxes[finalI].getFontColor(),.9));
                    }
                    else {
                        boxes[finalI].setColor(Methods.brighter(boxes[finalI].getColor(),.9));
                    }
                }
                else {
                    if (grid.currentNum != finalI + 1) {
                        boxes[finalI].setBorderColor(color3);
                        boxes[finalI].setFontColor(color3);
                        boxes[finalI].borderOn(true);
                        if (boxes[finalI].mouseOver) boxes[finalI].setColor(color3.brighter().brighter());
                        else boxes[finalI].setColor(color0);
                    } else {
                        boxes[finalI].setColor(color3);
                        boxes[finalI].setFontColor(color0);
                        boxes[finalI].borderOn(false);
                    }
                }
            });
            game.addBox(boxes[i]);
        }
    }

    public void tick(Game game) {
    }

    public void render(Graphics g) {

    }

    private void resetPotGrids() {
        potGrids.clear();
        int i = 0;
        int i2 = 0;
        ArrayList<File> arr = null;
        while (i*8+i2<direc.listFiles().length) {
            if (i2==0) {
                arr = new ArrayList<>();
            }
            arr.add(direc.listFiles()[i*8+i2]);
            i2++;
            if (i2==8) {
                i++;
                i2=0;
                potGrids.add(arr);
            }
        }
        if (i2!=0) potGrids.add(arr);
//        for (ArrayList<File> files: potGrids) {
//            for (File file: files) {
//                System.out.println(file.getName());
//            }
//            System.out.println();
//        }
    }
    private void findNewGrid() {
        resetPotGrids();
        currentPotGrids.clear();
        int boxY = 207;
        if (gridGroupNum==0) {
            OptionBox random = new OptionBox(605, boxY, 165, 30, "Random Grid");
            random.setClickBlock(() -> {
                newGrid();
                findingNewGrid = false;
                for (OptionBox b : currentPotGrids) {
                    game.removeBox(b);
                }
            });
            currentPotGrids.add(random);
            boxY+=35;
        }
        for (int file = 0;file<potGrids.get(gridGroupNum).size();file++) {
            OptionBox b = new OptionBox(605,boxY,165,30,potGrids.get(gridGroupNum).get(file).getName().split(".ser")[0]);
            String finalName = potGrids.get(gridGroupNum).get(file).getName();
            b.setClickBlock(()-> {
                newGrid(finalName);
                findingNewGrid = false;
                for (OptionBox box: currentPotGrids) {
                    game.removeBox(box);
                }
            });
            boxY+=35; //522 Last
            currentPotGrids.add(b);
        }
        if (gridGroupNum>0) {
            OptionBox b = new OptionBox(605,522,80,48,"<-");
            b.setClickBlock(()-> {
                gridGroupNum--;
                for (OptionBox box: currentPotGrids) {
                    game.removeBox(box);
                }
                findNewGrid();
            });
            currentPotGrids.add(b);
        }
        if (potGrids.size()-1-gridGroupNum>0) {
            OptionBox b = new OptionBox(690,522,80,48,"->");
            b.setClickBlock(()-> {
                gridGroupNum++;
                for (OptionBox box: currentPotGrids) {
                    game.removeBox(box);
                }
                findNewGrid();
            });
            currentPotGrids.add(b);
        }
        for (OptionBox box: currentPotGrids) {
            box.setColor(Methods.brighter(color1, .85));
            box.setFontColor(color0);
            box.setTickBlock(()-> {
                if (box.mouseOver) box.setColor(Methods.brighter(color1, .8).darker());
                else box.setColor(Methods.brighter(color1, .8));
            });
            game.addBox(box);
        }
    }

    private void newGrid(String fileName) {
        int i = 0;
        int r = 0;
        for (String f: direc.list()) {
            if (f.equals(fileName)) {
                r = i;
                break;
            }
            i++;
        }
        currentGrid = r;
        game.removeObject(grid);
        grid = new SudokuGrid(InitialGrid.deserialized(direc.listFiles()[r]));
        grid.gridName = direc.listFiles()[currentGrid].getName().split(".ser")[0];
        game.addObject(grid);

    }
    private void newGrid() {
        InitialGrid init = null;
        InitialGrid g = null;
        File file;
        int r = currentGrid;

        if (direc.listFiles().length>0) {
            System.out.print("Getting Backup Grid: ");
            if (currentGrid==-1||direc.listFiles().length==1) {
                r = (int) (Math.random() * direc.listFiles().length);
            }
            else {
                while (r==currentGrid) {
                    r = (int) (Math.random() * direc.listFiles().length);
                }
            }
            file = direc.listFiles()[r];
            System.out.println(file.getName());
            g = InitialGrid.deserialized(file);
            if (g!=null) {
                init = g;
                currentGrid = r;
            }
            else System.out.println(file.getPath() + " - failed deserialization");
        }
        if (g==null) {
            init = new InitialGrid(new int[][]{
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9}

            });
            game.removeObject(grid);
            grid = new SudokuGrid(init);
            grid.gridName = "noGrid";
            game.addObject(grid);
        }
        else {
            game.removeObject(grid);
            grid = new SudokuGrid(init);
            grid.gridName = direc.listFiles()[currentGrid].getName().split(".ser")[0];
            game.addObject(grid);

        }
    }
}
