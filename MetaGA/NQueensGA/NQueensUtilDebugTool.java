/*
Stephen Maldonado
Used to manually fill a board and test all the functions
*/

import java.io.*;
import java.util.*;
import java.text.*;


public class NQueensUtilDebugTool{
  public static void main(String args[]){

    // Scanner for input
    Scanner in = new Scanner(System.in);

    // Input for board size
    System.out.println("Input chessboard size: ");
    int board_size = in.nextInt();

    // Create board and board trackers
    boolean chessBoard[][] = new boolean[board_size][board_size];
    int usedRow[] = new int[board_size];
    int downDiag[] = new int[2*board_size];
    int upDiag[] = new int[2*board_size];

    // Create incrementinga rrays to print wih tracking arrays
    int[] incrementingToBoardSize = new int[chessBoard.length];
    for (int i = 0; i < chessBoard.length; i++){
      incrementingToBoardSize[i] = i;
    }
    int[] incrementingToTwoBoardSize = new int[2*chessBoard.length];
    for (int i = 0; i < 2*chessBoard.length; i++){
      incrementingToTwoBoardSize[i] = i;
    }

    // Print empty board
    debugPrintBoard(chessBoard);

    int col = 0;
    System.out.println("Placing queens (enter -1 to end)");
    while(true){
      System.out.println("Next Queen: ");
      int nextQueen = in.nextInt();
      if (nextQueen == -1){
        break;
      }
      System.out.println("Valid = " + NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, nextQueen, col, board_size)+"\n");
      NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, nextQueen, col, board_size);
      System.out.print("          " + Arrays.toString(incrementingToBoardSize) + "\n");
      System.out.print(" usedRow: " + Arrays.toString(usedRow) + "\n\n");
      System.out.print("          " + Arrays.toString(incrementingToTwoBoardSize) + "\n");
      System.out.print("downDiag: " + Arrays.toString(downDiag) + "\n");
      System.out.print("  upDiag: " + Arrays.toString(upDiag) + "\n");

      debugPrintBoard(chessBoard);
      col++;
    }


  }

  // Prints a debug board (contains indices at the sides)
  public static void debugPrintBoard(boolean chessBoard[][]){
    System.out.println("Chess Board:");

    // Print out numbers on top
    System.out.print("   "); // Spaces before top numbers
    for (int i = 0; i < chessBoard.length; i++){
      int numPrint = i % 1000;
      if (numPrint < 10){
        System.out.print("__" + numPrint + "_");
      }
      if (numPrint > 9 && numPrint < 100){
        System.out.print("_" + numPrint + "_");
      }
      if (numPrint > 99 && numPrint < 1000){
        System.out.print("_" + numPrint);
      }
    }
    System.out.print("\n");

    // Iterate through the queens
    for (int i = 0; i < chessBoard.length; i++){
      // At the start of each row, print a number on the side
      int numPrint = i % 1000;
      if (numPrint < 10){
        System.out.print(" " + numPrint + " ");
      }
      if (numPrint > 9 && numPrint < 100){
        System.out.print(" " + numPrint);
      }
      if (numPrint > 99 && numPrint < 1000){
        System.out.print(numPrint);
      }
      for (int j = 0; j < chessBoard[i].length; j++){
        if (chessBoard[i][j] == true){
          System.out.print("|_Q_");
        }
        else{
          System.out.print("|___");
        }
      }
      System.out.print("| " + numPrint + "\n"); // print number at end too
    }

    // Print bottom layer of numbers too
    // Print out numbers on top
    System.out.print("   "); // Spaces before top numbers
    for (int i = 0; i < chessBoard.length; i++){
      int numPrint = i % 1000;
      if (numPrint < 10){
        System.out.print("  " + numPrint + " ");
      }
      if (numPrint > 9 && numPrint < 100){
        System.out.print(" " + numPrint + " ");
      }
      if (numPrint > 99 && numPrint < 1000){
        System.out.print(" " + numPrint);
      }
    }
    System.out.print("\n");
  }
}
