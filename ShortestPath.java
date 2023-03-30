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

		System.out.println("SHORTEST PATH");
		ArrayList<int[]> pathCoords = new ArrayList<int[]>();
		ArrayList<int[]> bestPathSave = new ArrayList<int[]>();
		boolean [][] temp = maze;
		System.out.println(dfs(startPosition[0], startPosition[1], exit[0], exit[1], temp, 0, pathCoords, bestPathSave));
		System.out.println("coords");
		for(int i = 0; i < bestPathSave.size(); i++) {
			System.out.println("Y Coords: "+bestPathSave.get(i)[0]);
			System.out.println("X Coords: " + bestPathSave.get(i)[1]);
		}

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
		generateExitTile(rows, columns);



		maze[exit[0]][exit[1]] = true; // add the exit tile to the path

		// generate main branch
		generateMainPath(maze);


		startPosition = generateStartingPosition(maze);

		maze[startPosition[0]][startPosition[1]] = true;

		return maze;
	}

	public static String [][] setUpStringMaze() {
		String [][] stringMaze = new String [maze.length][maze[0].length];

		for (int x=0;x<stringMaze.length;x++) {
			for (int y=0; y<stringMaze[x].length;y++) {	
				stringMaze [x][y]="B";

				if (maze[x][y]) {
					stringMaze [x][y]="O";
				}
			}
			stringMaze [exit[0]][exit[1]] = "X";
			stringMaze [startPosition[0]][startPosition[1]]="S";


		}

		return stringMaze;
	}

	public static void generateExitTile(int rows, int columns) {
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


	}

	public static void generateMainPath( boolean[][] maze) throws InterruptedException {
		int count = 0;

		Random rand = new Random();
		int[] currentPath = new int [2];
		currentPath [0] = exit [0];
		currentPath [1] = exit [1];


		int rows = maze.length;
		int columns = maze[0].length;
		int centerSquares = (rows * columns) - 2 * rows - 2 * columns + 4;
		int mainLength = rand.nextInt(centerSquares / 4) + centerSquares / 4;

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
				if(maze[x][y]){
					System.out.print("O");
				}else{
					System.out.print("-");
				}
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

	static int shortest = Integer.MAX_VALUE;
	public static int dfs(int currentX, int currentY, int endingX, int endingY, boolean[][] maze, int currentLen, ArrayList<int[]> pathCoords, ArrayList<int[]> bestPathSave) {
		int columnSize = maze.length;
		int rowSize = maze[0].length;

		
		//the two terminating/go back conditions are whether it reaches the end or gets too long
		
		if(currentLen>=shortest) {
			if(!pathCoords.isEmpty()) {
				pathCoords.remove(pathCoords.size()-1);
			}
			return -1;
		}
		if ((currentX == endingX) && (currentY == endingY)) {
			bestPathSave.clear();
			bestPathSave.addAll(pathCoords); //saving the arraylist because if it passes the first two conditions, it is gaurenteed the shorter than previous making it the new best
			shortest = currentLen;
			return currentLen;
		}
		
		int[]coords = {currentX, currentY};
		pathCoords.add(coords);
		
		System.out.println(shortest);

		int branchLen = -1;
		maze[currentX][currentY] = false;
		

		// check for left border
		if (currentY > 0 && maze[currentX][currentY - 1]) {
			//does not need to call update shortest path because it sets the bar
			branchLen = dfs(currentX, currentY - 1, endingX, endingY, maze, currentLen + 1, pathCoords, bestPathSave);
		}

		// check for right border
		if (currentY < rowSize - 1 && maze[currentX][currentY + 1]) {
			int fetchLen = dfs(currentX, currentY + 1, endingX, endingY, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
		}

		// check up
		if (currentX > 0 && maze[currentX - 1][currentY]) {
			int fetchLen = dfs(currentX - 1, currentY, endingX, endingY, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
		}

		// check down
		if (currentX < columnSize - 1 && maze[currentX + 1][currentY]) {
			int fetchLen = dfs(currentX + 1, currentY, endingX, endingY, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
		}

		if(!pathCoords.isEmpty()) {
			pathCoords.remove(pathCoords.size()-1);
		}
		return branchLen;
	}

	public static int updateShortestPath(int fetchLen, int branchLen) {
		if (fetchLen != -1 && (branchLen == -1 || fetchLen < branchLen)) {
			branchLen = fetchLen;

		}
		return branchLen;
	}
}
