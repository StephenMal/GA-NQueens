
public class backtrackingSolution {
  public static boolean[][] solveNQueensWithBacktracking(int board_size){
    // arrays for tracking queens' positions & bad areas
    boolean chessBoard[][] = new boolean[board_size][board_size];
    int usedRow[] = new int[board_size*2];
    int downDiag[] = new int[board_size*2];
    int upDiag[] = new int[board_size*2];

    // start the recursion
    if (continueBT(chessBoard, usedRow, downDiag, upDiag, 0, board_size) == true){
      NQueensUtil.printBoard(chessBoard);
      return chessBoard;
    }
    else{
      // Means a combination could not be found
      System.out.println("Failed to create a solution");
      return chessBoard;
    }

  }
  public static boolean continueBT(boolean chessBoard[][], int usedRow[], int downDiag[], int upDiag[], int sel_col, int board_size){
    // If we have reached the board size, a solution has been found
    if (sel_col == board_size){
      return  true;
    }

    // Iterate through the rows of this column and see if we can place a queen
    for (int row = 0; row < board_size; row++){
      // If found safe, place a queen
      if(NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, sel_col, board_size) == true){
        // Placing queen
        NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, sel_col, board_size);
        // Move on to next column, if that works return true
        if (continueBT(chessBoard, usedRow, downDiag, upDiag, sel_col+1, board_size) == true){
          return true;
        }
        // If we did not return before, that means we are backtracking:
        //   Remove the queen placed now and try another row
        NQueensUtil.removeQueen(chessBoard, usedRow, downDiag, upDiag, row, sel_col, board_size);
      }
    }
    // Return false if nothing worked
    return false;
  }

  public static void main(String args[])
    {
      for(int board_size = 1; board_size < 100; board_size++) {
        long start_time = System.nanoTime();
        boolean[][] chessBoard = solveNQueensWithBacktracking(board_size);
        long end_time = System.nanoTime();
        System.out.println("Backtracking Time(N = " + board_size + "): "+(end_time - start_time) + "ns");
        NQueensUtil.validateSolution(chessBoard);
      }
    }
}
