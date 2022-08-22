//Name: Maleke Hanson
//Date: March 15, 2022
//Version: 5.0
public class Nonogram{
  
	//Instance variables to be used in constructor method
    public static int numberOfRows;
    public static int numberOfCols;
    public static boolean[][] board;
    public static int[][] columns;
    public static int[][] rows;
   
    /*
    //Constructor for our nonogram board
    public Nonogram(int row, int cols, int[][] columns, int[][] rows) {
    	board = new boolean[row][cols]; //We create and set our board's size to the input given by the user's rows array and columns array sizes
    	this.numberOfRows = row; //We set the total number of rows in the board to be the size of the rows array given by the user
    	this.numberOfCols = cols; //We set the total number of columns in the board to be the size of the rows array given by the user
    	this.rows = rows; 
    	this.columns = columns;
    }  
    
    */
    
    /**
     * nonogram method solves the puzzle with the given two parameters
     * @param rows: double int array given by the user that represents the rows of the board
     * @param columns: double int array given by the user that represents the columns of the board
     * @param blockIndex: For each board there are two block indexes that we will view that are present in the rows before the squares to pain. Here we input 0 to begin solving the board
     * @return If the board can be completely solved, we will return the solved nonogram board, if not, we will return null
     */
    public static boolean[][] solveNonogram(int[][] rows, int[][]columns){
    	board = new boolean[rows.length][columns.length];
    	numberOfRows = rows.length;
    	numberOfCols = columns.length;
    	boolean[][] solvedBoard = solveNonogram2(rows, columns, 0);
    	return solvedBoard;
    }
    
    private static boolean[][] solveNonogram2(int[][] rows, int[][] columns, int blockIndex){
        if (solve(board, rows, columns, 0, 0, true) == false) { 
            return null;
        }
        return board;
    }
    /**
     * Method to solve the nonogram board using backtracking, this will call the safe function to be sure where the board is attempting to solve is safe or not, if it is not, we will backtrack within this method by unpainting/
     * turning squares back into false
     * @param board Takes in the instance of the nonogram board needed to be solved
     * @param rows double int array to represent the rows of the board
     * @param columns double int array to represent the columns of the board
     * @param the row we are current on of the board
     * @param colStart the starting position of where we are in the row to begin solving from
     * @param blockIndex_0 boolean to check if we are at the first blockIndex or not
     * @return boolean true/false
     */
    public static boolean solve(boolean[][] board, int[][] rows, int[][] columns, int row, int col, boolean blockIndex_0) { //This is called in Solve Nonogram
    	//System.out.println(columns.length);
    	//System.out.println(rows.length);
    //	System.out.println(board.length);
    	//Base Case
    	if (row == rows.length) { //If the row we are currently on is equal to the number of the total rows in the board, then we have solved the board and can just return true
    		return true;
        }
    	//Recursive Case
        if(isSafe1(board, rows, columns, row) == true){ //Check to see if where we currently are at in the board is safe to play on
        	int blockIndex1 = 0; //Variable to represent the first block index in the row
        	int blockIndex2 = 1; //Variable to repesent the second block index in the row
        	boolean atBlockIndex1 = false;  //These are used to check whether we are at blockIndex 1 or at blockIndex 2. These will be called when calling solve again later
        	boolean atBlockIndex2 = false;
        	if(blockIndex1 == 0) { //Since solve is just intially being called, we are going to be at blockIndex 1 so we will set it to be true here
        		atBlockIndex1 = true;
        	}
        	//Variables to store the values that are actually present at the indexes to be used to know how much to paint/turn True
        	int blockIndex1Value = rows[row][blockIndex1]; 
        	int blockIndex2Value = rows[row][blockIndex2];
        	
            int currentColumnInRow = 0; //Variable to see where we currently are at in the row by columns
            
            if (atBlockIndex1 && blockIndex1Value != 0) { //If we are at blockIndex 1 and the value that is present at blockIndex 1 is greater than 0/exits, then we the column that we will add the values of both blockIndexes together to know how far we will be going
            	currentColumnInRow = blockIndex1Value + blockIndex2Value; //blockIndex2 will always exist no matter what, so we can automatically add without needing to check 
            }
            else { 
            	atBlockIndex1 = false; //If both those conditions aren't met then we can automatically assume that we can just move onto blockIndex2 and not worry about blockIndex1
            	if(atBlockIndex1 == false) {
            		atBlockIndex2 = true; //We will turn blockIndex2 to true as we have "moved/finished" with blockIndex1
            	}
            }
            if(atBlockIndex1 == true) { //If blockIndex1 remained true after checking, then the currentColumn we will be moving to will be the value starting column we are at + the value of blockIndex 2
            	currentColumnInRow = col + blockIndex2Value  ;
            }
            
            int i = 0;
            boolean detrmineIfBlocksArePainted = false;
            while (i < columns.length - currentColumnInRow) { //We will loop through the remaining columns in the row
                int blockIndex = 0; 
                if (atBlockIndex2 == true) {  //We will check if we are at blockIndex1 or 2
                	blockIndex = 1;
                }
                
                for(int j = 0; j < rows[row][blockIndex]; j++) { //This will change the required about of squares within the row to true through looping
                	board[row][col + i + j ] = true;
                } 
                if (blockIndex_0 == true) { //Check if at blockIndex 0
                	atBlockIndex1 = true;
                	if(atBlockIndex1) {
                		atBlockIndex2 = false;
                	}
                    int columnIndexToStart;
                    if (blockIndex1Value == 0) { //If the value of the index at blockIndex1 is 0, then the column we will start with will be at 0
                    	columnIndexToStart = 0;
                    }
                    else { 
                    	columnIndexToStart = blockIndex1Value + 1 + i; //If the valeu is greater than 0, then the column we will start at in the row will be the value of the blockIndex +1 + the iteration we currently are at
                    }
                    detrmineIfBlocksArePainted = solve(board, rows, columns, row, columnIndexToStart, atBlockIndex2);
                }
                else { 
                	atBlockIndex1 = false; 
                	if(atBlockIndex1 == false) {
                		atBlockIndex2 = true;
                	}
                	detrmineIfBlocksArePainted = solve(board, rows, columns, row + 1, 0, atBlockIndex2);
                }
                if (detrmineIfBlocksArePainted == true) { 
                    return true; 
                }
                
                for(int j = 0; j < rows[row][blockIndex]; j++) {  //Backtrack/unpaint the blocks that need to be unpainted in order to solve the board again
                	board[row][col + i + j] = false;
                }
                i++;
            }
        }
       
        else {
        	return false;
        }
        return false;
    }
    /**
     * Function to determine if the current index we are at in the board is safe or not to paint. Will check the blockValues and use to see if we can actually paint the appropraite amount needed 
     * @param board nonogram board to be passed through to determine if it is solvable
     * @param columns double int array to be passed though that represents the columns of the board 
     * @param intialRow The starting row that we will begin with to determine if it is safe or not to paint the board 
     * @return boolean true/false
     */
    public static boolean isSafe1(boolean[][] board, int[][] rows, int[][] columns, int intialRow) {
    	int cols  = 0; // The current column that we are at in the board in the row
    	 while (cols < columns.length-1) { //While the column we are at is less than the total amount of columns in the board we will loop through  //CHECK AGAIN!!
    		 cols++;
    		 int blockValueAtIndex1 = columns[cols][0]; //Create a varaible to equal the value that is actually present within the index
    		 if (0 < blockValueAtIndex1) {  //if the value is greater than 0/exists, then we will call isSafe2 to check the second blockIndex
            	isSafe2(board, rows, columns, 0, cols); 
            	}
    		 int blocksToPaint = 0; 
    		 int indexOfRow = 0;  //The current row index we are at
    		 if (blockValueAtIndex1 == 0) { //If the value at index1 is 0, then we will call isSafe2 to check the second blockIndex
    			 if(isSafe2(board, rows, columns, indexOfRow, cols) == false) { //if isSafe2 returns a false, then that means it's not safe to paint
    				 return false;
    			 }
    		 }
    		 if(blockValueAtIndex1 != 0) { //If the block index exists then we will loop through the board and use a counter to see how much paint
    			 while(indexOfRow < rows.length) {  //While the current row we are at is less than the total amount of rows, we will iterate through the rows
    				 if (board[indexOfRow][cols] == true) {  //If the current block that we are at is painted, then we will add to our count
    					 blocksToPaint++; 
    				 }
    				 else { 
    					 blocksToPaint = 0; 
    				 }
    				 if (blocksToPaint == blockValueAtIndex1) { //If we reach the amount needed to paint then we can increment the row index we are at
    					 indexOfRow++;
    					 if (board[indexOfRow][cols] == true) { //If the index square we reach is already painted true, then we will have to return false as we either went to far or probably need to back track
    						 return false; 
    					 }
    					 else { 
    						 break; 
    					 }
    				 }
    				 indexOfRow++; //
    				 }	
    			 }
    		 }
    	 return true;
    	}

    /**
     * Function to be called by isSafe1. This function will determine if the second blockIndex's value is safe to be painted/allows for us to solve the board correctly... uses very similar logic to isSafe1, check comments if needed
     * @param board boolean board to be passed though
     * @param columns 
     * @param indexOfRow This is the current column within the row to determine where we need to check if we are safe at painting
     * @param cols The amount of columns iterated through already 
     * @return boolean true/false
     */
    public static boolean isSafe2(boolean[][] board, int[][] rows, int[][] columns, int indexOfRow, int cols) { 
        while ( cols < columns.length-1) { //CHECK AGAIN!!
        	cols++;
            int blockValueAtIndex2 = columns[cols][1];
            int blocksToPaint = 0; 
            while (indexOfRow < rows.length) { 
                if (board[indexOfRow][cols] == true) { 
                	blocksToPaint++; 
                }
                if (blocksToPaint == blockValueAtIndex2) { 
                	indexOfRow++; 
                    indexOfRow = Integer.MAX_VALUE - 1; 
                }
                indexOfRow++;
            }
            if (0 < blockValueAtIndex2) { 
                continue; 
            }
            while (indexOfRow < rows.length) { 
                if (board[indexOfRow][cols] == true) { 
                    return false;
                }
                indexOfRow++;
            }
            boolean isRowComplete = true;
            if (isRowComplete && blocksToPaint != blockValueAtIndex2) { 
                return false; 
            }
        }
        return true; 
    }

    
    /**
     * This function will simply just print out the board for the user to see the correct results 
     * @param newBoard takes in a new instance of Lab02 better known as a new instance of a nonogram board
     */
    public static void printArray(boolean[][] newBoard) { 
        for (int i = 0; i < newBoard.length; i++) { 
            for (int x = 0; x < newBoard[i].length; x++) { 
                if (newBoard[i][x] == true) { 
                    System.out.print("True|"); 
                }
                else { 
                    System.out.print("False|"); 
                }
            }
            System.out.println(); 
        }
    }
    public static void main(String[] args){
        int[][] columns = {{0,1}, {0,1}, {0,1}, {0,1}, {0,1}}; 
        int[][] rows = {{0,5}}; 
       // int[][] columns = {{0,2}, {0,2}, {0,2}, {0,1}, {0,1}, {0,1}, {0,2}, {0,2}, {0,1}}; 
       // int[][] rows = {{4,3}, {3,4}};
      // int[][] columns = {{0,2}, {2,1}, {0,4}, {0,3}, {0,1}}; 
     // int[][] rows = {{0,4}, {0,4}, {0,3}, {0,1}, {0,1}};
     //int[][] columns = {{1,1}, {2,1}, {0,2}, {1,1}, {1,1}}; 
    //  int[][] rows = {{1,1}, {0,1}, {0,5}, {0,1}, {1,1}};
    //    Lab02 newBoard = new Lab02(rows.length, columns.length, rows, columns);
     //   newBoard.solveNonogram(rows, columns);
     //   newBoard.printArray(newBoard);
        boolean[][] board = solveNonogram(rows, columns);
     //   System.out.println(columns.length);
    //    System.out.println(rows.length);
        printArray(board);
    }
}
