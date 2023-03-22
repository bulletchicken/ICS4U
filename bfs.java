
public class ShortestPathFinder {
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

		int endingX = 2; //column 0
		int endingY =2; //row 4
		


		System.out.println(bfs(startingY, startingX, endingY, endingX, maze, 0, false));
		
	}

	public static int bfs(int currentY, int currentX, int endingY, int endingX, boolean[][]maze, int shortest, boolean found){
		
        
		boolean checked [][] = maze; //creating a pbv as to not mess up. Used to keep track of the current stream being searched

		int columnSize = maze.length;
		int rowSize = maze[0].length;
		System.out.println("Coordinates at: {" + currentX+ ", " + currentY + "}");
		System.out.println(shortest);
		
		checked[currentY][currentX] = false;
		if(found){
            return shortest; //zero base
        }
        if((currentY == endingY)&&(currentX == endingX)){
            System.out.println("found");
            found = true;
            return shortest; //found the shortest path and should exit the bfs
        }
        
    
        int shorty = -1;
        if(currentX>0){//check for left border
            if((maze[currentY][currentX-1] == true)){
                int res = bfs(currentY, currentX-1, endingY, endingX, checked, shortest+1, found);
                if (res != -1 && (shorty == -1 || res < shorty)) {
                    shorty = res;
                }
                if(found){
                    return shortest; //zero base
                }
            }
        }
    
        if(currentX<rowSize-1){ //check for right border
            if((maze[currentY][currentX+1] == true)){
                int res = bfs(currentY, currentX+1, endingY, endingX, checked, shortest+1, found);
                if (res != -1 && (shorty == -1 || res < shorty)) {
                    shorty = res;
                }
                if(found){
                    return shortest; //zero base
                }
            }
        }
    
        if(currentY>0){ //check up
    
            if((maze[currentY-1][currentX] == true)){
                int res = bfs(currentY-1, currentX, endingY, endingX, checked, shortest+1, found);
                if (res != -1 && (shorty == -1 || res < shorty)) {
                    shorty = res;
                }
                if(found){
                    return shortest; //zero base
                }
            }
    
        }
    
    
        if(currentY<columnSize-1){ //check down
            if((maze[currentY+1][currentX] == true)){
                int res = bfs(currentY+1, currentX, endingY, endingX, checked, shortest+1, found);
                if (res != -1 && (shorty == -1 || res < shorty)) {
                    shorty = res;
                }
                if(found){
                    return shortest; //zero base
                }
            }
        }
    
        return shorty;
        //shorty is the shortest for each branch essentially after reaching the end
    }
}
