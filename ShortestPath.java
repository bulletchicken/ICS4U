import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import java.awt.*;

public class ShortestPath extends JFrame {

    static int invalidCounter;
    static int rows;
    static int columns;

    static int[] exit = new int[2];
    static int[] startPosition = new int[2];

    static boolean[][] maze;

    JPanel map = new JPanel();

    public ShortestPath() throws IOException, InterruptedException {
        setTitle("Maze");
        setSize(1000, 1000);

        maze = setUpMaze();
        printMapOnConsole(maze);

        
        GridLayout layout2 = new GridLayout(rows, columns); // Create a GridLayout
        map.setLayout(layout2);
        JButton buttonMaze [][] = new JButton[rows][columns];
        
        convertToButton(maze, buttonMaze);
        for(int x = 0; x < maze.length; x++){
        	for(int y = 0; y < maze[x].length; y++){
        		map.add(buttonMaze[x][y]);
        	}
        }
        
        
        add(map);
        setVisible(true);
        
    }
    
    public static void convertToButton(boolean[][]maze, JButton [][] buttonMaze){
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if ((maze[x][y]) == true) {
                    JButton path = new JButton("O");
                    
                    path.setBackground(Color.YELLOW);
                    path.setOpaque(true);
                    buttonMaze[x][y] = path;
                }else {
                    JButton wall = new JButton("B");
                    wall.setBackground(Color.darkGray);
                    wall.setOpaque(true);
                    buttonMaze[x][y] = wall;
                }
            }
        }
        
        JButton exitButton = new JButton("X");
        exitButton.setBackground(Color.GREEN);
        exitButton.setOpaque(true);
        buttonMaze[exit[0]][exit[1]]=exitButton;
        
        JButton startButton = new JButton("S");
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setOpaque(true);
        buttonMaze[startPosition[0]][startPosition[1]] = startButton;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ShortestPath gM = new ShortestPath();
        
        System.out.println(dfs(startPosition[0], startPosition[1], exit[0], exit[1], maze, 0));

    }
    // return a boolean array, false for wall, true for path

    public static boolean[][] setUpMaze() throws IOException, InterruptedException {

        System.out.println("Please enter how many rows");
        rows = getInput();
        System.out.println("Please enter how many columns");
        columns = getInput();

        maze = new boolean[rows][columns];
        //fillArrayTemp(maze);

        // generate the exit tile
        exit = generateExitTile(rows, columns);
        System.out.println("1");

       maze[exit[0]][exit[1]] = true; // add the exit tile to the path

        // generate main branch
        generateMainPath(exit, maze);
        startPosition = generateStartingPosition(maze);
        maze[startPosition[0]][startPosition[1]] = true;

        return maze;
    }

    public static int[] generateExitTile(int rows, int columns) {
        Random rand = new Random();

        exit[0] = rand.nextInt(rows); // first start by generating the X coordinate

        // there are 2 cases, the exit cell being on the top/bottom or the sides
        if (exit[0] == 0 || exit[0] == rows - 1) { // if exit is on top/bottom
            exit[1] = rand.nextInt(columns - 2) + 1;
        } else {
            int side = rand.nextInt(2);
            if (side == 0) {
                exit[1] = 0;
            } else {
                exit[1] = columns - 1;
            }
        }
        return exit;
    }

    public static void generateMainPath(int[] exit, boolean[][] maze) throws InterruptedException {
        int count = 0;

        Random rand = new Random();
        int[] currentPath = exit;

        int rows = maze.length;
        int columns = maze[0].length;
        int centerSquares = (rows * columns) - 2 * rows - 2 * columns + 4;
        int mainLength = rand.nextInt(centerSquares / 4) + centerSquares / 4;
        System.out.println("mainlength: " + mainLength);

        invalidCounter = 0;

        while (count < mainLength) {
            int seed = rand.nextInt(4);

            switch (seed) {
                case 0:
                    if (addPathUp(currentPath, maze)) {
                        count++;
                    } else {

                        invalidCounter++;
                        break;
                    }
                    break;
                case 1:
                    if (addPathDown(currentPath, maze)) {
                        count++;

                    } else {

                        invalidCounter++;
                        break;
                    }
                    break;
                case 2:
                    if (addPathRight(currentPath, maze)) {
                        count++;
                    } else {

                        invalidCounter++;
                        break;
                    }
                    break;
                case 3:
                    if (addPathLeft(currentPath, maze)) {
                        count++;
                    } else {

                        invalidCounter++;
                        break;
                    }
                    break;
            }
        }
    }

    public static boolean addPathUp(int[] currentPath, boolean[][] maze) {
        try {

            if (currentPath[0] - 1 != 0 && currentPath[1] != 0 && currentPath[1] != maze[0].length - 1
                    && (maze[currentPath[0] - 1][currentPath[1]])==true) {
                maze[currentPath[0] - 1][currentPath[1]] = true;
                currentPath[0] = currentPath[0] - 1;
                return true;
            } else if (currentPath[0] - 1 != 0 && currentPath[1] != 0 && currentPath[1] != maze[0].length - 1
                    && invalidCounter > 20) {
                maze[currentPath[0] - 1][currentPath[1]] = true;
                currentPath[0] = currentPath[0] - 1;
                invalidCounter = 0;
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean addPathDown(int[] currentPath, boolean[][] maze) {
        try {
            if (currentPath[0] + 1 != maze.length - 1 && currentPath[1] != maze[0].length - 1 && currentPath[1] != 0
                    && !(maze[currentPath[0] + 1][currentPath[1]])==true) {
                maze[currentPath[0] + 1][currentPath[1]] = true;
                currentPath[0] = currentPath[0] + 1;
                return true;
            } else if (currentPath[0] + 1 != maze.length - 1 && currentPath[1] != maze[0].length - 1
                    && currentPath[1] != 0 && invalidCounter > 20) {
                maze[currentPath[0] + 1][currentPath[1]] = true;
                currentPath[0] = currentPath[0] + 1;
                invalidCounter = 0;
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean addPathLeft(int[] currentPath, boolean[][] maze) {

        try {

            if (currentPath[1] - 1 != 0 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
                    && !(maze[currentPath[0]][currentPath[1] - 1])==true) {
                maze[currentPath[0]][currentPath[1] - 1] = true;
                currentPath[1] = currentPath[1] - 1;
                return true;
            } else if (currentPath[1] - 1 != 0 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
                    && invalidCounter > 20) {
                maze[currentPath[0]][currentPath[1] - 1] = true;
                currentPath[1] = currentPath[1] - 1;
                invalidCounter = 0;
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean addPathRight(int[] currentPath, boolean[][] maze) {
        try {
            if (currentPath[1] + 1 != maze[0].length - 1 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
                    && !(maze[currentPath[0]][currentPath[1] + 1])==true) {
                maze[currentPath[0]][currentPath[1] + 1] = true;
                currentPath[1] = currentPath[1] + 1;
                return true;
            } else if (currentPath[1] + 1 != maze[0].length - 1 && currentPath[0] != maze.length - 1
                    && currentPath[0] != 0 && invalidCounter > 20) {
                maze[currentPath[0]][currentPath[1] + 1] =true;
                currentPath[1] = currentPath[1] + 1;
                invalidCounter = 0;
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {

            return false;
        }
    }

    public static int getInput() throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean validInput = false;

        while (!validInput) {
            try {
                int input = Integer.parseInt(br.readLine());
                validInput = true;
                return input;

            } catch (Exception e) {
                System.out.println("Invalid, please reenter");
            }

        }

        return 0;
    }

    public static void fillArrayTemp(String[][] array) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                array[x][y] = "B";
            }
        }
    }

    public static void printMapOnConsole(boolean[][] maze) {
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                System.out.print(maze[x][y]);
            }
            System.out.println();
        }
    }

    public static int[] generateStartingPosition(boolean[][] maze) {
        Random rand = new Random();
        int count = 0;
        int[][] listOfOpenSquares = new int[maze.length * maze[0].length][2];
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y] == true) {
                    listOfOpenSquares[count][0] = x;
                    listOfOpenSquares[count][1] = y;
                    count++;
                }
            }

        }

        int seed = rand.nextInt(listOfOpenSquares.length);

        while (listOfOpenSquares[seed][0] == 0 && listOfOpenSquares[seed][1] == 0) {
            seed = rand.nextInt(listOfOpenSquares.length);

        }
        return listOfOpenSquares[seed];

    }
    static int columnSize = maze.length;
    static int rowSize = maze[0].length;
    public static ArrayList<int []> backtrack = new ArrayList<int []>();
    public static int dfs(int currentY, int currentX, int endingY, int endingX, boolean[][] maze, int shortest) {
    

    
        maze[currentY][currentX] = false;
        int [] coords = new int[2];
        coords[1] = currentX;
        coords[0] = currentY;
        backtrack.add(coords);
    
        if ((currentY == endingY) && (currentX == endingX)) {
            return shortest; // found the shortest path and should exit the dfs
        }
    
        int shortestPath = -1;
    
        if (currentX > 0) { // check for left border
            if ((maze[currentY][currentX - 1] == true)) {
                maze[currentY][currentX - 1] = false;
                int res = dfs(currentY, currentX - 1, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                    backtrack.
                }
                maze[currentY][currentX - 1] = true; // backtrack
            }
        }
    
        if (currentX < rowSize - 1) { // check for right border
            if ((maze[currentY][currentX + 1] == true)) {
                maze[currentY][currentX + 1] = false;
                int res = dfs(currentY, currentX + 1, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY][currentX + 1] = true; // backtrack
            }
        }
    
        if (currentY > 0) { // check up
            if ((maze[currentY - 1][currentX] == true)) {
                maze[currentY - 1][currentX] = false;
                int res = dfs(currentY - 1, currentX, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY - 1][currentX] = true; // backtrack
            }
        }
    
        if (currentY < columnSize - 1) { // check down
            if ((maze[currentY + 1][currentX] == true)) {
                maze[currentY + 1][currentX] = false;
                int res = dfs(currentY + 1, currentX, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY + 1][currentX] = true; // backtrack
            }
        }
    
        return shortestPath;
    }
    
    
/* 
    public static int dfs(int currentY, int currentX, int endingY, int endingX, String[][] maze, int shortest) {

        int columnSize = maze.length;
        int rowSize = maze[0].length;

        maze[currentY][currentX] = "B";

        if ((currentY == endingY) && (currentX == endingX)) {
            return shortest; // found the shortest path and should exit the dfs
        }

        int shortestPath = -1;

        if (currentX > 0) { // check for left border
            String left = maze[currentY][currentX - 1];
            if ((left.equals("O") || left.equals("X"))) {
                maze[currentY][currentX - 1] = "B";
                int res = dfs(currentY, currentX - 1, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY][currentX - 1] = left; // backtrack
            }
        }

        if (currentX < rowSize - 1) { // check for right border
            String right = maze[currentY][currentX + 1];
            if ((right.equals("O") || right.equals("X"))) {
                maze[currentY][currentX + 1] = "B";
                int res = dfs(currentY, currentX + 1, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY][currentX + 1] = right; // backtrack
            }
        }

        if (currentY > 0) { // check up
            String up = maze[currentY - 1][currentX];
            if (up.equals("O") || up.equals("X")) {
                maze[currentY - 1][currentX] = "B";
                int res = dfs(currentY - 1, currentX, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY - 1][currentX] = up; // backtrack
            }
        }

        if (currentY < columnSize - 1) { // check down
            String down = maze[currentY + 1][currentX];
            if (down.equals("O") || down.equals("X")) {
                maze[currentY + 1][currentX] = "B";
                int res = dfs(currentY + 1, currentX, endingY, endingX, maze, shortest + 1);
                if (res != -1 && (shortestPath == -1 || res < shortestPath)) {
                    shortestPath = res;
                }
                maze[currentY + 1][currentX] = down; // backtrack
            }
        }

        return shortestPath;
    }

 */

    /*
     * public static int dfs(int currentY, int currentX, String[][] maze, int
     * currentLen, int shortest) {
     * 
     * int columnLen = maze.length;
     * int rowLen = maze[0].length;
     * 
     * maze[currentY][currentX] = "B";
     * 
     * //if the current length of the branch longer than the current shortest, go
     * back early and give up on that branch
     * //this block of conditional will act as the comparison for shortest
     * 
     * printMapOnConsole(maze);
     * System.out.println(shortest);
     * if(shortest<currentLen){
     * return -1;
     * } else if(maze[currentY][currentX].equals("X")){ //if the code makes it this
     * far, the currentLen is shorter than shortest so immedietly set new shortest
     * return currentLen;
     * }
     * 
     * if(currentX > 0) {
     * if(maze[currentY][currentX-1].equals("O") ||
     * maze[currentY][currentX-1].equals("X")){
     * int fetchLen = dfs(currentY, currentX - 1, maze, currentLen+1, shortest);
     * 
     * if(fetchLen!=-1 && (shortest==0 || fetchLen < shortest)){ //if shortest path
     * has not been found yet, set found as the current shortest
     * shortest = fetchLen;
     * }
     * 
     * maze[currentY][currentX-1] = "O";
     * }
     * }
     * 
     * if(currentX < rowLen-1) {
     * if(maze[currentY][currentX+1].equals("O") ||
     * maze[currentY][currentX+1].equals("X")){
     * int fetchLen = dfs(currentY, currentX + 1, maze, currentLen+1, shortest);
     * if(fetchLen!=-1 && (shortest==0 || fetchLen < shortest)){ //if shortest path
     * has not been found yet, set found as the current shortest
     * shortest = fetchLen;
     * }
     * }
     * }
     * 
     * if(currentY > 0) {
     * if(maze[currentY-1][currentX].equals("O") ||
     * maze[currentY-1][currentX].equals("X")){
     * int fetchLen = dfs(currentY- 1, currentX, maze, currentLen+1, shortest);
     * if(fetchLen!=-1 && (shortest==0 || fetchLen < shortest)){ //if shortest path
     * has not been found yet, set found as the current shortest
     * shortest = fetchLen;
     * }
     * }
     * }
     * 
     * if(currentY < columnLen-1) {
     * if(maze[currentY+1][currentX].equals("O") ||
     * maze[currentY+1][currentX].equals("X")){
     * int fetchLen = dfs(currentY+1, currentX, maze, currentLen+1, shortest);
     * if(fetchLen!=-1 && (shortest==0 || fetchLen < shortest)){ //if shortest path
     * has not been found yet, set found as the current shortest
     * shortest = fetchLen;
     * }
     * }
     * }
     * 
     * //when finishing all searches, this code has passed the first condition which
     * returned -1, or has found a shorter path;
     * return currentLen;
     * }
     */
}
