
public class ShortestPathFinder {
	static int shorty = -1;
	
	public static void main(String[]args){
		
		boolean[][] maze = 
			{		{true, false, false, false, false},
					{true, true, true, false, false},
					{true, false, true, false, false},
					{true, true, true, true, true},
					{true, false, true, false, false},
					{true, false, false, false, false},
			};

		int startingX = 2; //colulmn 2
		int startingY = 1; //row 1

		int endingX = 0; //column 0
		int endingY =5; //row 4
		


		bfs(startingY, startingX, endingY, endingX, maze, 1);
		System.out.println(shorty);
		
	}

	public static int bfs(int currentY, int currentX, int endingY, int endingX, boolean[][]maze, int shortest){
		
		boolean checked [][] = maze; //creating a pbv as to not mess up. Used to keep track of the current stream being searched

		int columnSize = maze.length;
		int rowSize = maze[0].length;
		System.out.println("Coordinates at: {" + currentX+ ", " + currentY + "}");
		System.out.println(shortest);
		
		checked[currentY][currentX] = false;
		
		if((currentY == endingY)&&(currentX == endingX)){
			shorty = shortest;
			return -1;
		}
		

		if(currentX>0){//check for left border
			if((maze[currentY][currentX-1] == true) && (checked[currentY][currentX-1] == true)){
				bfs(currentY, currentX-1, endingY, endingX, maze, shortest+1);
			}
		}

		if(currentX<rowSize-1){ //check for right border
			if((maze[currentY][currentX+1] == true)&& (checked[currentY][currentX+1] == true)){
				bfs(currentY, currentX+1, endingY, endingX, maze, shortest+1);
			}
		}

		if(currentY>0){ //check up

			if((maze[currentY-1][currentX] == true) &&(checked[currentY-1][currentX] == true)){
				bfs(currentY-1, currentX, endingY, endingX, maze, shortest+1);
			}

		}
		
		
		if(currentY<columnSize-1){ //check down
			if((maze[currentY+1][currentX] == true)&& (checked[currentY+1][currentX] == true)){

				bfs(currentY+1, currentX, endingY, endingX, maze, shortest+1);
			}
		}

		return -1;

	}
}
