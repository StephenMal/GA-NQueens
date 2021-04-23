/*
Stephen Maldonado
Utility functions to assist with N-Queens solving problem
To be utilized by both the Genetic Algorithm and the backtracking benchmark
*/

import java.io.*;
import java.util.*;
import java.text.*;

public class NQueensUtil{

//  PRINT THE CHESS BOARD
	public static void printBoard(boolean chessBoard[][]){
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

//  CHECK IF PLACING A QUEEN WOULD INVALIDATE A BOARD
	public static boolean checkPlacementValidity(boolean chessBoard[][], int usedRow[], int downDiag[], int upDiag[], int sel_row, int sel_col, int board_size){
		// If row is already being used, return false
		if (usedRow[sel_row] > 0){
			return false;
		}
		// Check downward diagnol
		int diff = sel_row - sel_col;
		if (diff < 0){
			if (downDiag[board_size + (diff*-1)] > 0){
				return false;
			}
		}
		else{
			if (downDiag[diff] > 0){
				return false;
			}
		}
		// Check upward diagnol
		int sum = sel_row + sel_col;
		if (sum >= board_size){
			if (upDiag[sel_row+sel_col+1] > 0){
				return false;
			}
		}
		else{
			if(upDiag[sel_row+sel_col] > 0){
				return false;
			}
		}

		// Return true since this is a valid placement
		return true;
	}

//  PLACE QUEEN
	public static void placeQueen(boolean chessBoard[][], int usedRow[], int downDiag[], int upDiag[], int sel_row, int sel_col, int board_size){
		chessBoard[sel_row][sel_col] = true;
		usedRow[sel_row]++;
		int diff = sel_row - sel_col;
		if (diff < 0){
			downDiag[board_size + (diff*-1)]++;
		}
		else{
			downDiag[diff]++;
		}
		int sum = sel_row + sel_col;
		if (sum >= board_size){
			upDiag[sel_row+sel_col+1]++;
		}
		else{
			upDiag[sel_row+sel_col]++;
		}
	}

// 	REMOVE QUEEN
	public static void removeQueen(boolean chessBoard[][], int usedRow[], int downDiag[], int upDiag[], int sel_row, int sel_col, int board_size){
		// If queen does not exist here, just return
		if (chessBoard[sel_row][sel_col] == false){
			System.out.println("Tried removing a queen that was not placed");
			return;
		}
		// Otherwise, remove from position and decrement all of the validity arrays
		chessBoard[sel_row][sel_col] = false;
		usedRow[sel_row]--;
		int diff = sel_row - sel_col;
		if (diff < 0){
			downDiag[board_size + (diff*-1)]--;
		}
		else{
			downDiag[diff]--;
		}
		int sum = sel_row + sel_col;
		if (sum >= board_size){
			upDiag[sel_row+sel_col+1]--;
		}
		else{
			upDiag[sel_row+sel_col]--;
		}
	}

//  Verify solution
	public static boolean validateSolution(boolean chessBoard[][]){
		int board_size = chessBoard.length;
		// verify square shape and only one queen per row
		for(int row = 0; row < board_size; row++){
			// Check each column length to see it is the same as row length
			if (chessBoard[row].length != board_size){
				System.out.println("Board size inconsistent");
				return false;
			}
			// Verify only one queen per row
			boolean rowUsed = false;
			// iterate through the columns of that row
			for(int col = 0; col < board_size; col++){
				// If queen is present
				if (chessBoard[row][col] == true){
					// See if a queen has already been in that row
					if (rowUsed == true){
						System.out.println("Board contains two queens on row " + row);
						return false;
					}
					// Otherwise set this row to used
					rowUsed = true;
				}
			}
		}

		// Verify Diagnols, first find the queens
		for(int row = 0; row < board_size; row++){
			for(int col = 0; col < board_size; col++){
				if (chessBoard[row][col] == true){
					int i, j;
					// Check upward diagonal on left side
					for (i = row, j = col; i >= 0 && j >= 0; i--, j--){
						if (chessBoard[i][j] == true && i != row && j != col){
							System.out.println("Board contains two queens in same diagnol: (" + i +"," + j + ") and (" + row + "," + col + ")");
							return false;
						}
					}
					// Check downward diagonal on left side
					for (i = row, j = col; j >= 0 && i < board_size; i++, j--){
						if (chessBoard[i][j] == true && i != row && j != col){
							System.out.println("Board contains two queens in same diagnol: (" + i +"," + j + ") and (" + row + "," + col + ")");
							return false;
						}
					}
				}
			}
		}
		System.out.println("Board verified");
		return true;
	}
}
