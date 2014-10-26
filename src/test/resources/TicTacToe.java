
//fuente http://www.coderslexicon.com/a-beginner-tic-tac-toe-class-for-java/
//NO BORRAR POR AHORA
public class TicTacToe {

   
    private static String currentPlayerMark;
    private static String player="Player1, type <row><column>";
    
    //tablero de 3*3
    private static String c00;
    private static String c01;
    private static String c02;
    private static String c10;
    private static String c11;
    private static String c12;
    private static String c20;
    private static String c21;
    private static String c22;
    
    
    
    
    
    public TicTacToe() {
       
        currentPlayerMark = "x";
        initializeBoard();
        
    }
	
    public static void putPos(int row , int col ,String s){
    	
			if(row==0){
			    		
			    		if(col==0){
			    			c00=s;
			    		}
			    		if(col==1){
			    			c01=s;
			    		}
			    		if(col==2){
			    			c02=s;
			    		}
			    	}

			if(row==1){
			    		
			    		if(col==0){
			    			c10=s;
			    		}
			    		if(col==1){
			    			c11=s;
			    		}
			    		if(col==2){
			    			c12=s;
			    		}
			    	}

			if(row==2){
			    		
			    		if(col==0){
			    			c20=s;
			    		}
			    		if(col==1){
			    			c21=s;
			    		}
			    		if(col==2){
			    			c22=s;
			    		}
			    	}
    	
    }
    
    
    public static String getPos(int row, int col){
    	String ans="";
    	
    	if(row==0){
    		
    		if(col==0){
    			ans=c00;
    		}
    		if(col==1){
    			ans= c01;
    		}
    		if(col==2){
    			ans= c02;
    		}
    	}
    	

    	else if(row==1){
    		
    		if(col==0){
    			ans= c10;
    		}
    		if(col==1){
    			return c11;
    		}
    		if(col==2){
    			ans= c12;
    		}
    	} else if(row==2){
    		
    		if(col==0){
    			ans= c20;
    		}
    		if(col==1){
    			ans= c21;
    		}
    		if(col==2){
    			ans= c22;
    		}
    	}
    	
    	return ans;
    	
    }
    
	public static void main(String[] args) {
		
        currentPlayerMark = "x";
        initializeBoard();
        System.out.println("Starting game.");
        
        boolean end=false;
        
	    while(!end){
		        
		        System.out.println(player);
		        String input = System.console().readLine();
		        int row = Integer.parseInt(""+input.charAt(0));
		        int column = Integer.parseInt(""+input.charAt(1));
		        
		        placeMark(row,column);
		
		     // Lets print the board
		     printBoard();
		
		     // Did we have a winner?
		     if (checkForWin()) {
		        System.out.println("We have a winner! Congrats!");
		        end=true;
		     }
		     else if (isBoardFull()) {
		        System.out.println("Appears we have a draw!");
		       end=true;
		     }
		
		     // No winner or draw, switch players to 'o'
		     System.out.println("");
		     changePlayer();
        
	    }
        
	}
    // Set/Reset the board back to all empty values.
    public static void initializeBoard() {
		
        // Loop through rows
        for (int i = 0; i < 3; i++) {
			
            // Loop through columns
            for (int j = 0; j < 3; j++) {
               // board[i][j] = '-';
            	putPos(i, j, "-");
            }
        }
    }
	
	
    // Print the current board (may be replaced by GUI implementation later)
    public static void printBoard() {
        System.out.println("-------------");
		
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {

            	System.out.print(getPos(i, j) + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }
	
	
    // Loop through all cells of the board and if one is found to be empty (contains char '-') then return false.
    // Otherwise the board is full.
    public static boolean isBoardFull() {
        boolean isFull = true;
		
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (getPos(i, j) == "-") {
                    isFull = false;
                }
            }
        }
		
        return isFull;
    }
	
	
    // Returns true if there is a win, false otherwise.
    // This calls our other win check functions to check the entire board.
    public static boolean checkForWin() {
        return (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());
    }
	
	
    // Loop through rows and see if any are winners.
    private static boolean checkRowsForWin() {
        for (int i = 0; i < 3; i++) {
            //if (checkRowCol(board[i][0], board[i][1], board[i][1]) == true) {
        	if(checkRowCol(getPos(i, 0),getPos(i, 1) ,getPos(i, 1) )){
                return true;
            }
        }
        return false;
    }
	
	
    // Loop through columns and see if any are winners.
    private static boolean checkColumnsForWin() {
        for (int i = 0; i < 3; i++) {
            //if (checkRowCol(board[0][i], board[1][i], board[2][i]) == true) {
        	if(checkRowCol(getPos(0, 1), getPos(1, i), getPos(2, i))){
                return true;
            }
        }
        return false;
    }
	
	
    // Check the two diagonals to see if either is a win. Return true if either wins.
    private static boolean checkDiagonalsForWin() {
        return 
        		checkRowCol(getPos(0, 0), getPos(1, 1), getPos(2, 2)) 
        		|| (checkRowCol(getPos(0, 2), getPos(1,1), getPos(2, 0))
        		);
    }
	
	
    // Check to see if all three values are the same (and not empty) indicating a win.
    private static boolean checkRowCol(String	 c1, String c2, String c3) {
        return ((c1 != "-") && (c1 == c2) && (c2 == c3));
    }
	
	
    // Change player marks back and forth.
    public static void changePlayer() {
        if (currentPlayerMark == "x") {
            currentPlayerMark = "o";
            player="Player2, type <row><column>";
        }
        else {
            currentPlayerMark = "x";
            player="Player1, type <row><column>";
        }
    }
	
	
    // Places a mark at the cell specified by row and col with the mark of the current player.
    public static boolean placeMark(int row, int col) {
		
        // Make sure that row and column are in bounds of the board.
        if ((row >= 0) && (row < 3)) {
            if ((col >= 0) && (col < 3)) {
                if (getPos(row, col) == "-") {
                    
                	
                	
                    putPos(row, col, currentPlayerMark);
                	
                	return true;
                }
            }
        }
		
        return false;
    }
}