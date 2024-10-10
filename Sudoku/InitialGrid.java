package Sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class InitialGrid implements java.io.Serializable {

    private static final long serialVersionUID = -6496115800967177428L;

    public int[][] grid;

    public InitialGrid(int[][] grid) {
        this.grid = grid;
    }

    public static InitialGrid deserialized(File file) {
        InitialGrid g;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            g = (InitialGrid) in.readObject();
            in.close();
            fileIn.close();
            return g;
        }
        catch (IOException i) {
            i.printStackTrace();
        }
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return null;
    }


}
