
public class ShortestPathFinder {
	public static void main(String[]args){
		boolean[][] maze = 
			{ {true, false, false, false, false},
					{true, true, true, false, false},
					{true, false, true, false, false},
					{true, true, true, true, true},
					{true, false, true, false, false},
					{true, false, false, false, false},
			};

		int startingX = 2; //colulmn 2
		int startingY = 1; //row 1

		int endingX = 0; //column 0
		int endingY = 4; //row 4

		int shortestPath = bfs(startingY, startingX, endingY, endingX, maze, 0);
		System.out.println(shortestPath);
	}

	public static int bfs(int currentY, int currentX, int endingY, int endingX, boolean[][]maze, int shortest){
		shortest = 0;

		boolean checked [][] = maze; //creating a pbv as to not mess up. Used to keep track of the current stream being searched

		int columnSize = maze.length;
		int rowSize = maze[0].length;

		if((currentY == endingY)&&(currentX == endingX)){
			return shortest;
		}
		System.out.println(currentX);
		System.out.println(currentY);

		if(currentX>0){//check for left border
			if((maze[currentY][currentX-1] == true) && (checked[currentY][currentX-1] == false)){
				shortest++;
				checked[currentY][currentX-1] = true;
				bfs(currentY, currentX-1, endingY, endingX, maze, shortest);
			}
		}

		if(currentX<rowSize){ //check for right border
			if((maze[currentY][currentX+1] == true)&& (checked[currentY][currentX+1] == false)){

				shortest++;
				bfs(currentY, currentX+1, endingY, endingX, maze, shortest);
			}
		}

		if(currentY>0){
			if((maze[currentY-1][currentX] == true) &&(checked[currentY][currentX+1] == false)){

			}
			shortest++;
			bfs(currentY, currentX-1, endingY, endingX, maze, shortest);
		}
		if(currentY<columnSize){
			if((maze[currentY+1][currentX] == true)&& (checked[currentY][currentX+1] == false)){
				shortest++;
				bfs(currentY+1, currentX, endingY, endingX, maze, shortest);
			}
		}



		return -1;

	}
}
