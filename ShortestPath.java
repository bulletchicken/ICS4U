import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShortestPath extends JFrame implements ActionListener {

	static int invalidCounter;
	static int rows;
	static int columns;
	static ArrayList<int[]>bestPathSave = new ArrayList<int[]>();
	static int shortest = Integer.MAX_VALUE;
	static String barrier = "B";
	static String open = "O";
	static String start ="S";
	static String exitSymbol = "X";

	static int[] exit = new int[2];
	static int[] startPosition = new int[2];
	static boolean valid = false;
	static boolean[][] maze;
	static JButton buttonMaze [][];

	JPanel map = new JPanel();
	JPanel userInterface = new JPanel();


	JPanel u1 = new JPanel ();
	JPanel u2 = new JPanel ();
	JPanel u3 = new JPanel ();
	JPanel u4 = new JPanel ();
	JPanel u5 = new JPanel ();
	JPanel u6 = new JPanel ();



	JLabel welcomeMessage = new JLabel ("Welcome to the Maze Solver! (V 3.0)");
	JLabel rowPrompt = new JLabel ("Enter rows:");
	JTextField rowField = new JTextField (5);
	JLabel columnPrompt = new JLabel("Enter columns:");
	JTextField columnField = new JTextField (5);
	JButton generateMaze = new JButton ("Generate Maze");
	JCheckBox generateValid = new JCheckBox ("Guarantee Valid Maze");
	JCheckBox generatePour = new JCheckBox("FloodFill Generation");
	JCheckBox fullyConnected = new JCheckBox("Fully Connected");

	JLabel filePrompt = new JLabel("Enter file name");
	JTextField importMaze = new JTextField (10);
	JButton fetchMaze = new JButton ("Upload");
	JLabel line = new JLabel ("________________________________________________________________Click the path to relocate the starting position! (Expiremental) _______________________________________________________________");

	JButton findShortest = new JButton ("Find Shortest Path");
	JButton saveToFile = new JButton ("Save to File");
	JLabel result = new JLabel ();
	JPanel options = new JPanel();

	public ShortestPath() throws IOException, InterruptedException {
		setTitle("Maze");
		setSize(800, 1000);
		setResizable(true);


		//printMapOnConsole(maze);

		GridLayout layout1 = new GridLayout(2,0); //main layout
		GridLayout layout3 = new GridLayout (6,0);

		ToolTipManager.sharedInstance().setInitialDelay(100);
		rowField.setToolTipText("larger mazes up to 100x100 are recommended");
		columnField.setToolTipText("larger mazes up to 100x100 are recommended");
		fullyConnected.setToolTipText("always a valid maze***");
		importMaze.setToolTipText("make sure your maze file is in the same folder as the program");
		saveToFile.setToolTipText("Save a generated maze into your file");


		rowField.addActionListener(this);
		columnField.addActionListener(this);
		generateMaze.addActionListener(this);
		importMaze.addActionListener(this);
		fetchMaze.addActionListener(this);
		findShortest.addActionListener(this);
		saveToFile.addActionListener(this);
		generateValid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(!generateValid.isSelected()) {
					fullyConnected.setSelected(false);
				}
			}
		});


		generateValid.setSelected(true);
		generatePour.addActionListener(this);
		generatePour.setSelected(false);
		fullyConnected.addActionListener(this);
		fullyConnected.setSelected(true);

		setLayout(layout1);

		userInterface.setLayout(layout3);

		options.add(generateValid);
		options.add(generatePour);
		options.add(fullyConnected);
		options.setLayout(layout1);

		u1.add(welcomeMessage);
		u2.add(rowPrompt);
		u2.add(rowField);
		u2.add(columnPrompt);
		u2.add(columnField);
		u2.add(options);
		u2.add(generateMaze);
		u3.add(filePrompt);
		u3.add(importMaze);
		u3.add(fetchMaze);
		u3.add(saveToFile);
		u4.add(line);
		u5.add(findShortest);
		u6.add(result);

		userInterface.add(u1);
		userInterface.add(u2);
		userInterface.add(u3);
		userInterface.add(u4);
		userInterface.add(u5);
		userInterface.add(u6);



		add(userInterface);

		setVisible(true);


	}



	public void actionPerformed(ActionEvent event){
		map.removeAll();

		String command = event.getActionCommand();

		if (command.equals("Generate Maze")) {
			resetToDefaultLegend();
			String rowsInput = rowField.getText();
			String columnsInput = columnField.getText();

			if (rowsInput.equals("2") || rowsInput.equals("1") || columnsInput.equals("2") || columnsInput.equals("1")) {
				result.setText("Please enter dimensions of minimum 3");
			}
			else {


				try {
					rows = Integer.parseInt(rowsInput);
					columns = Integer.parseInt(columnsInput);
					GridLayout layout2 = new GridLayout(rows, columns);
					map.setLayout(layout2);

					System.out.println(rows);
					System.out.println(columns);

					if (generateValid.isSelected()){
						maze = setUpMaze(generatePour.isSelected(), fullyConnected.isSelected());
						result.setText(rows+" by "+columns+" maze has been generated");
					}else{
						maze = setUpChaoticMaze(generatePour.isSelected());
						result.setText(rows+" by "+columns+" maze has been generated (Valid Maze Guarantee is OFF)");
					}

					buttonMaze = new JButton[rows][columns];

					convertToButton(maze,buttonMaze);


					for(int x = 0; x < maze.length; x++){
						for(int y = 0; y < maze[x].length; y++){
							map.add(buttonMaze[x][y]);

						}
					}
				}catch(Exception e) {
					result.setText("Please enter appropriate, numeric values");
				}
			}
		}



		if (command.equals("Upload")) {

			String fileName = importMaze.getText();


			try {
				maze=readFile(fileName);
				result.setText(fileName+" has been successfully uploaded");
				System.out.println(rows);
				System.out.println(columns);


				System.out.println("startX"+startPosition[0]);
				System.out.println("startY"+startPosition[1]);
				System.out.println("endX"+exit[0]);
				System.out.println("endY"+exit[1]);


				GridLayout layout2 = new GridLayout(rows, columns);
				map.setLayout(layout2);
				buttonMaze  = new JButton[rows][columns];

				convertToButton(maze,buttonMaze);




				displayMaze();
			} catch (IOException e) {

				result.setText("Sorry, that file cannot be found");
			}
		}

		if(command.equals("Save to File")) {
			String fileName = importMaze.getText();
			try {
				writeFile(fileName);
			} catch (IOException e) {
				result.setText("Please add a file or generate a maze before saving");
				e.printStackTrace();
			}
		}

		add(map);
		setVisible(true);

		if (command.equals("Find Shortest Path")) searchAndDeploy();


	}

	public void searchAndDeploy() {


		try {
			ArrayList<int[]>pathCoords = new ArrayList<int[]>();
			boolean [][] mazeCopy = maze;
			shortest = Integer.MAX_VALUE;
			bestPathSave.clear();


			int shortestPathResult = dfs(startPosition[0], startPosition[1], exit[0], exit[1], mazeCopy, 0, pathCoords, bestPathSave);

			System.out.println(shortestPathResult);


			if (shortestPathResult==-1){
				result.setText("Sorry, no path has been found");
			}else{
				revealPath(buttonMaze);

				result.setText("The shortest possible path is "+shortestPathResult+" tiles");
			}
			//they get deleted from doing dfs
			maze[exit[0]][exit[1]] = true;
			maze[startPosition[0]][startPosition[1]] = true;
			displayMaze();

			result.setText("Successfully Saved to File");
		}catch (Exception e) {
			System.out.println(e);
			result.setText("Please generate or import a maze before finding shortest path");
		}

	}

	public void convertToButton(boolean[][]maze, JButton [][] buttonMaze){



		for (int x = 0; x < maze.length; x++) {
			for (int y = 0; y < maze[x].length; y++) {
				if ((maze[x][y]) == true) {

					JButtonV2 path = new JButtonV2();
					path.button.setBackground(Color.YELLOW);
					path.button.setOpaque(true);
					path.button.setText(open);
					path.x = x;
					path.y = y;

					path.button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							startPosition[1] = path.y;
							startPosition[0] = path.x;
							searchAndDeploy();
						}
					});




					buttonMaze[x][y] = path.button;


				}else {
					JButtonV2 wall = new JButtonV2();
					Color brown = new Color (153,102,51,255);
					wall.button.setText(barrier);
					wall.button.setBackground(brown);
					wall.button.setOpaque(true);

					wall.x = x;
					wall.y = y;
					buttonMaze[x][y] = wall.button;
				}
			}
		}

		JButton exitButton = new JButton(exitSymbol);
		exitButton.setBackground(Color.GREEN);
		exitButton.setOpaque(true);
		buttonMaze[exit[0]][exit[1]]=exitButton;


		JButtonV2 startButton = new JButtonV2();
		startButton.button.setText(start);
		startButton.button.setBackground(Color.LIGHT_GRAY);
		startButton.button.setOpaque(true);

		buttonMaze[startPosition[0]][startPosition[1]] = startButton.button;

	}

	public void revealPath(JButton [][] buttonMaze) {
		convertToButton(maze,buttonMaze);
		map.removeAll();
		convertToButton(maze, buttonMaze);
		for(int x = 0; x < bestPathSave.size(); x++) {

			JButtonV2 reveal = new JButtonV2();
			reveal.button.setText("+");
			Color mrABlue = new Color(0,176,240,255);
			reveal.button.setBackground(mrABlue);
			reveal.button.setOpaque(true);
			buttonMaze[bestPathSave.get(x)[1]][bestPathSave.get(x)[0]] = reveal.button;

			reveal.x = bestPathSave.get(x)[1];
			reveal.y = bestPathSave.get(x)[0];

			reveal.button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					startPosition[1] = reveal.y;
					startPosition[0] = reveal.x;
					searchAndDeploy();
				}
			});
		}



		//override reveal path with exit and start again
		JButton exitButton = new JButton(exitSymbol);
		exitButton.setBackground(Color.GREEN);
		exitButton.setOpaque(true);
		buttonMaze[exit[0]][exit[1]]=exitButton;

		JButtonV2 startButton = new JButtonV2();
		startButton.button.setText(start);
		startButton.button.setBackground(Color.LIGHT_GRAY);
		startButton.button.setOpaque(true);

		buttonMaze[startPosition[0]][startPosition[1]] = startButton.button;


	}

	public void displayMaze() {
		for(int x = 0; x < maze.length; x++){
			for(int y = 0; y < maze[x].length; y++){
				map.add(buttonMaze[x][y]);

			}
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		ShortestPath gM = new ShortestPath();

		System.out.println("SHORTEST PATH");
		//System.out.println(dfs(startPosition[0], startPosition[1], exit[0], exit[1], maze, 0));

	}
	// return a boolean array, false for wall, true for path


	public static boolean [][] readFile (String fileName) throws IOException{
		File file = new java.io.File(fileName);
		BufferedReader input = new BufferedReader(new FileReader(file));

		rows = Integer.parseInt(input.readLine());
		columns = Integer.parseInt(input.readLine());
		System.out.println(rows);
		System.out.println(columns);


		boolean [][]maze = new boolean [rows][columns];
		barrier=input.readLine();
		open=input.readLine();
		start=input.readLine();
		exitSymbol=input.readLine();

		for (int x=0;x<maze.length;x++) {
			for (int y=0;y<maze[x].length;y++) {
				char character=(char)input.read();
				if((Character.toString(character)).equals(barrier)) {


					maze[x][y]=false;
				}else {
					maze[x][y]=true;

				}


				if((Character.toString(character)).equals(exitSymbol)) {
					exit[0]=x;
					exit[1]=y;
					maze[x][y] = true;
				}

				if((Character.toString(character)).equals(start))  {
					startPosition[0]=x;
					startPosition[1]=y;
					maze[x][y] = true;
				}
			}
			input.readLine();

		}
		input.close();
		return maze;

	}

	public void writeFile(String fileName) throws IOException{

		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.write(String.valueOf(rows));
		writer.newLine();
		writer.write(String.valueOf(columns));
		writer.newLine();
		writer.write(barrier);
		writer.newLine();
		writer.write(open);
		writer.newLine();
		writer.write(start);
		writer.newLine();
		writer.write(exitSymbol);
		writer.newLine();
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				if(maze[i][j] == true) {
					writer.write(open);
				} else {
					writer.write(barrier);
				}
			}
			writer.newLine();
		}

		writer.close();


	}

	public static boolean[][] setUpMaze(boolean generatePour, boolean fullyConnect) {

		maze = new boolean[rows][columns];
		generateExitTile(rows, columns);
		maze[exit[0]][exit[1]] = true; // add the exit tile to the path
		if(generatePour) {


			generateMainPath(maze);


			startPosition = generateStartingPosition(maze);
			maze[startPosition[0]][startPosition[1]] = true;
		} 
		else if(fullyConnect){

			generateEdgePath();
			if(exit[0]!= rows-1 && exit[0]!=0) {
				if(exit[1]==0) {
					maze[exit[0]][exit[1]+1] = true;
					if(columns>3 && rows>3) {
						maze[exit[0]][exit[1]+2] = true;
					}

				}
				else {
					maze[exit[0]][exit[1]-1] = true;
					if(columns>3 && rows>3) {
						maze[exit[0]][exit[1]-2] = true;
					}
				}
			}
			else if(exit[1]!=columns-1&&exit[1]!=0) {
				if(exit[0]==0) {
					maze[exit[0]+1][exit[1]] = true;
					if(columns>3 && rows>3) {
						maze[exit[0]+2][exit[1]] = true;
					}
				}
				else {
					maze[exit[0]-1][exit[1]] = true;
					if(columns>3 && rows>3) {
						maze[exit[0]-2][exit[1]] = true;
					}
				}
			}
			startPosition = generateStartingPosition(maze);
			maze[startPosition[0]][startPosition[1]] = true;
		} else{

			generatePath();
			//determining starting direction and sticking out one from exit to always attach

			if(exit[0]!= rows-1 && exit[0]!=0) {
				if(exit[1]==0) {
					maze[exit[0]][exit[1]+1] = true;
				}
				else {
					maze[exit[0]][exit[1]-1] = true;
				}
			}
			else if(exit[1]!=columns-1&&exit[1]!=0) {
				if(exit[0]==0) {
					maze[exit[0]+1][exit[1]] = true;
				}
				else {
					maze[exit[0]-1][exit[1]] = true;
				}
			}
			boolean [][] pbv = maze;

			while(!valid) {
				validStarting(exit[0], exit[1], pbv, startPosition, 0);
			}
			valid = false; //reset for next use;

			maze[startPosition[0]][startPosition[1]] = true;
		}



		return maze;
	}

	public static void generateEdgePath() {
		for(int i = 1; i < rows-1; i++) {
			for(int j = 1; j < columns-1; j++) {
				maze[i][j] = true;
			}
		}
		for (int i = 2; i < rows; i += 2) {
			for (int j = 2; j < columns; j += 2) {
				maze[i][j] = false;
				maze[i][j] = false;
				// Carve out random connecting path
				if (j < columns - 1 && Math.random()>0.5) {
					maze[i][j + 1] = false;
				} else if (i < rows - 2) {
					maze[i + 1][j] = false;
				}
			}
		}


	}

	public static void generatePath() {
		for (int i = 1; i < rows-1; i += 2) {
			for (int j = 1; j < columns-1; j += 2) {
				maze[i][j] = true;
				maze[i][j] = true;
				// Carve out random connecting path
				if (j < columns - 2 && Math.random()>0.5) {
					maze[i][j + 1] = true;
				} else if (i < rows - 2) {
					maze[i + 1][j] = true;
				}
			}
		}
	}

	public static void validStarting(int currentY, int currentX, boolean[][] mazeCopy, int[] deployStart, int branchLen) {

		int columnSize = mazeCopy.length;
		int rowSize = mazeCopy[0].length;

		Random rand = new Random();

		if((currentX!=exit[1] || currentY!=exit[0])&& rand.nextInt(columnSize) < 2) {
			System.out.println("hit at: " + branchLen);
			System.out.println("x: " + currentX + " y: " + currentY);
			deployStart[0] = currentY;
			deployStart[1] = currentX;
			valid = true;
		}

		if (currentX > 1 && mazeCopy[currentY][currentX - 1] == true) { // check for left border
			mazeCopy[currentY][currentX - 1] = false;
			validStarting(currentY, currentX - 1, mazeCopy, deployStart, branchLen+1);
			mazeCopy[currentY][currentX - 1] = true; // backtrack
		}

		if (currentX < rowSize - 1 && mazeCopy[currentY][currentX + 1] == true) { // check for right border
			mazeCopy[currentY][currentX + 1] = false;
			validStarting(currentY, currentX + 1, mazeCopy, deployStart, branchLen+1);
			mazeCopy[currentY][currentX + 1] = true; // backtrack
		}

		if (currentY > 1 && mazeCopy[currentY - 1][currentX] == true) { // check up
			mazeCopy[currentY - 1][currentX] = false;
			validStarting(currentY - 1, currentX, mazeCopy, deployStart, branchLen+1);
			mazeCopy[currentY - 1][currentX] = true; // backtrack
		}

		if (currentY < columnSize - 1 && mazeCopy[currentY + 1][currentX] == true) { // check down
			mazeCopy[currentY + 1][currentX] = false;
			validStarting(currentY + 1, currentX, mazeCopy, deployStart, branchLen+1);
			mazeCopy[currentY + 1][currentX] = true; // backtrack
		}

	}



	public static boolean [][] setUpChaoticMaze(boolean generatePour){
		int seed;
		Random rand = new Random();

		maze = new boolean[rows][columns];

		generateExitTile(rows, columns);
		maze[exit[0]][exit[1]] = true;
		if(generatePour) {
			for (int x=1;x<maze.length-1;x++){
				for (int y=1;y<maze[x].length-1;y++){
					seed=rand.nextInt(3);
					if (seed==0){
						maze[x][y]=true;
					}else{
						maze[x][y]=false;
					}
				}
			}

		} else {
			generatePath();
		}

		maze[rand.nextInt(maze.length-2)+1][rand.nextInt(maze[0].length-2)+1]=true;  //ensure that at least 1 non border cell is a path

		startPosition = generateStartingPosition(maze);

		maze[startPosition[0]][startPosition[1]] = true;

		return maze;


	}

	public static void generateExitTile(int rows, int columns) {
		Random rand = new Random();


		int wall = rand.nextInt(4);
		System.out.println("wall" + wall);
		if(wall<2) { //left and right
			exit[0] = rand.nextInt(rows-2)+1;
			if(wall==0) { //right
				exit[1] = columns - 1;
			} else { //left
				exit[1] = 0;
			}
		}else { //top and bottom
			exit[1] = rand.nextInt(columns-2)+1;
			if(wall==2) { //left
				exit[0] = rows - 1;
			} else {
				exit[0] = 0;
			}

		}


	}

	public static void resetToDefaultLegend (){
		barrier = "B";
		open = "O";
		start ="S";
		exitSymbol = "X";
	}

	public static void generateMainPath( boolean[][] maze)  {
		System.out.println("method called");
		int count = 0;

		Random rand = new Random();
		int[] currentPath = new int [2];
		currentPath [0] = exit [0];
		currentPath [1] = exit [1];

		System.out.println("next 4 lines");


		int rows = maze.length;
		System.out.println("-");
		int columns = maze[0].length;
		System.out.println("-");
		int centerSquares = (rows * columns) - 2 * rows - 2 * columns + 4;
		System.out.println("Centersquares:");
		System.out.println(centerSquares);
		int mainLength = (rand.nextInt((centerSquares / 4)+1) + centerSquares / 4)+1;


		System.out.println("Mainlength");
		System.out.println(mainLength);


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
		if (currentPath[0]-1 == -1) {
			return false;
		}

		if (currentPath[0] - 1 != 0 && currentPath[1] != 0 && currentPath[1] != maze[0].length - 1
				&& (maze[currentPath[0] - 1][currentPath[1]])==true) {
			maze[currentPath[0] - 1][currentPath[1]] = true;
			currentPath[0] = currentPath[0] - 1;
			return true;
		} else if (currentPath[0] - 1 != 0 && currentPath[1] != 0 && currentPath[1] != maze[0].length - 1
				&& invalidCounter > 10) {
			maze[currentPath[0] - 1][currentPath[1]] = true;
			currentPath[0] = currentPath[0] - 1;
			invalidCounter = 0;
			return true;
		} else {

			return false;
		}


	}

	public static boolean addPathDown(int[] currentPath, boolean[][] maze) {

		if (currentPath[0]+1 == maze.length) {
			return false;
		}

		if (currentPath[0] + 1 != maze.length - 1 && currentPath[1] != maze[0].length - 1 && currentPath[1] != 0
				&& !(maze[currentPath[0] + 1][currentPath[1]])==true) {
			maze[currentPath[0] + 1][currentPath[1]] = true;
			currentPath[0] = currentPath[0] + 1;
			return true;
		} else if (currentPath[0] + 1 != maze.length - 1 && currentPath[1] != maze[0].length - 1
				&& currentPath[1] != 0 && invalidCounter > 10) {
			maze[currentPath[0] + 1][currentPath[1]] = true;
			currentPath[0] = currentPath[0] + 1;
			invalidCounter = 0;
			return true;
		} else {

			return false;
		}


	}

	public static boolean addPathLeft(int[] currentPath, boolean[][] maze) {


		if (currentPath[1]-1 == -1) {
			return false;
		}



		if (currentPath[1] - 1 != 0 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
				&& !(maze[currentPath[0]][currentPath[1] - 1])==true) {
			maze[currentPath[0]][currentPath[1] - 1] = true;
			currentPath[1] = currentPath[1] - 1;
			return true;
		} else if (currentPath[1] - 1 != 0 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
				&& invalidCounter > 10) {
			maze[currentPath[0]][currentPath[1] - 1] = true;
			currentPath[1] = currentPath[1] - 1;
			invalidCounter = 0;
			return true;
		} else {

			return false;
		}


	}

	public static boolean addPathRight(int[] currentPath, boolean[][] maze) {

		if (currentPath[1]+1 == maze[0].length) {
			return false;
		}

		if (currentPath[1] + 1 != maze[0].length - 1 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
				&& !(maze[currentPath[0]][currentPath[1] + 1])==true) {

			maze[currentPath[0]][currentPath[1] + 1] = true;

			currentPath[1] = currentPath[1] + 1;

			return true;

		} else if (currentPath[1] + 1 != maze[0].length - 1 && currentPath[0] != maze.length - 1
				&& currentPath[0] != 0 && invalidCounter > 10) {
			maze[currentPath[0]][currentPath[1] + 1] =true;
			currentPath[1] = currentPath[1] + 1;
			invalidCounter = 0;
			return true;

		} else {
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

		while ((listOfOpenSquares[seed][0] == 0 && listOfOpenSquares[seed][1] == 0) || (listOfOpenSquares[seed][0] == exit[0] && listOfOpenSquares[seed][1] == exit[1])) {
			seed = rand.nextInt(listOfOpenSquares.length);

		}

		return listOfOpenSquares[seed];

	}


	public static int dfs(int currentY, int currentX, int endingY, int endingX, boolean[][] mazeCopy, int currentLen,  ArrayList<int[]> pathCoords, ArrayList<int[]> bestPathSave) {

		int columnSize = mazeCopy.length;
		int rowSize = mazeCopy[0].length;

		mazeCopy[currentY][currentX] = false;
		if(currentLen>=shortest) {
			return -1;
		}

		if ((currentY == endingY) && (currentX == endingX)) {
			System.out.println(shortest);
			bestPathSave.clear();
			bestPathSave.addAll(pathCoords); //saving the arraylist because if it passes the first two conditions, it is gaurenteed the shorter than previous making it the new best

			shortest = currentLen;
			return currentLen; // found the shortest path and should exit the dfs
		}

		int[]coords = {currentX, currentY};
		pathCoords.add(coords);

		int branchLen = -1;
		if (currentX > 0 && (mazeCopy[currentY][currentX - 1] == true)) { // check for left border
			mazeCopy[currentY][currentX - 1] = false;
			int fetchLen = dfs(currentY, currentX - 1, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
			mazeCopy[currentY][currentX - 1] = true; // backtrack
		}
		if (currentX < rowSize - 1 && (mazeCopy[currentY][currentX + 1] == true)) { // check for right border
			mazeCopy[currentY][currentX + 1] = false;
			int fetchLen = dfs(currentY, currentX + 1, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
			mazeCopy[currentY][currentX + 1] = true; // backtrack

		}
		if (currentY > 0 && (mazeCopy[currentY - 1][currentX] == true)) { // check up
			mazeCopy[currentY - 1][currentX] = false;
			int fetchLen = dfs(currentY - 1, currentX, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
			mazeCopy[currentY - 1][currentX] = true; // backtrack

		}
		if (currentY < columnSize - 1 && (mazeCopy[currentY + 1][currentX] == true)) { // check down
			mazeCopy[currentY + 1][currentX] = false;
			int fetchLen = dfs(currentY + 1, currentX, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave);
			branchLen = updateShortestPath(fetchLen, branchLen);
			mazeCopy[currentY + 1][currentX] = true; // backtrack
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

class JButtonV2{
	JButton button = new JButton();
	int x;
	int y;
}
