
public class backtrackingSolution {
  // Print the board
  void printBoard(boolean chessBoard[][]){
    System.out.println("Chess Board:");
    for (int i = 0; i < chessBoard.length; i++){
      for (int j = 0; j < chessBoard[i].length; j++){
        if (chessBoard[i][j] == true){
          System.out.print("|_Q_");
        }
        else{
          System.out.print("|___");
        }
      }
      System.out.print("|\n");
    }
  }

  boolean checkPlacementValidity(boolean chessBoard[][], int sel_row, int sel_col){

    // Checks if row is valid
    for (int i = 0; i < sel_col; i++){
      if (chessBoard[row][i] == true){
        return false;
      }
    }

    // Checks upper diagonal
    for (int i = 0; i < sel_row; i++)

  }

  public static void main(String args[])
    {
      boolean chessBoard[][] = new boolean[5][5];
      chessBoard[2][2] = true;
      chessBoard[4][4] = true;
      chessBoard[1][4] = true;
      backtrackingSolution Solver = new backtrackingSolution();
      Solver.printBoard(chessBoard);
    }
}
