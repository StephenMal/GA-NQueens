/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class NQueens extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public NQueens(){
		name = "NQueens Problem";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS

	public void doRawFitness(Chromo X){
		/*
		NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, col, board_size)
		^ Will return true if placing a queen at (row, col) would create invalidate the board
			Will return false if the board would be fine

		NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, col, board_size)
		^ Will place a queen on the tracking arrays for (row, col)

		recordSolutionTime()
		^ Records the time it took to find a solution.  Will not update the time after
			the first solution is found.
		*/

		// arrays for tracking queens' positions & bad areas
		int board_size = Parameters.numGenes;
    boolean chessBoard[][] = new boolean[board_size][board_size];
    int usedRow[] = new int[board_size*2];
    int downDiag[] = new int[board_size*2];
    int upDiag[] = new int[board_size*2];

		// Returns the queens positions according to representation from genes
		int queenPos[] = new int[Parameters.numGenes];
		getQueenPositions(X, queenPos);

		// Switch between the fitness function types
		switch(Parameters.fitnessFunctionType){
			case 1:{ // Same Diagnol Punishment
				X.rawFitness = 0;
				// Place queens
				for (int col = 0; col < board_size; col++){
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size);
				}
				// Sum all queens on shared diagnols
				for (int i = 0; i < board_size*2; i++){
					if (downDiag[i] > 1){
						X.rawFitness += downDiag[i] - 1;
					}
					if (upDiag[i] > 1){
						X.rawFitness += downDiag[i] - 1;
					}
				}
				// If rawfitness is 0, solution found record it
				if (X.rawFitness == 0){
					recordSolutionTime();
				}
			}
			break;
			case 2:{ // Number of Conflicts
				X.rawFitness = 0;
				// Place queens, increment for every invalid placed queen
				for (int col = 0; col < board_size; col++){
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size) == false){
						X.rawFitness++;
					}
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size);
				}
				// If rawfitness is 0, solution found record it
				if (X.rawFitness == 0){
					recordSolutionTime();
				}
			}
			break;
			case 3:{ // Reward until invalid queen
				X.rawFitness = 0;
				for (int col = 0; col < board_size; col++){
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size) == false){
						return;
					}
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size);
					X.rawFitness++;
				}
				// If this is true, we have 1 good queen per column and have found
				// a solution
				if (X.rawFitness == board_size){
					recordSolutionTime();
				}
			}
			break;
			case 4:{ // Incrementing reward until invalid queen
				X.rawFitness = 0;
				for (int col = 0; col < board_size; col++){
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size) == false){
						return;
					}
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size);
					X.rawFitness += col;
				}
				// Find the sum of all the values from 0 to boardsize, that should give
				// the answer
				int solutionFitness = (board_size*(board_size-1))/2;
				if (X.rawFitness == solutionFitness){
					recordSolutionTime();
				}
			}
			break;
			case 5:{ // Reward consecutive queens
				int consecutivity = 0; // Keep track of how many consecutive queens
				// Iterate through positionings
				for (int col = 0; col < board_size; col++){
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, queenPos[col], col, board_size) == false){
						consecutivity = 0;
					}
					else{
						consecutivity++;
					}
					X.rawFitness += consecutivity;
				}
				// Find the sum of all the values from 0 to boardsize, that should give
				// the answer
				int solutionFitness = (board_size*(board_size-1))/2;
				if (X.rawFitness == solutionFitness){
					recordSolutionTime();
				}
			}
			break;
			case 6: // All or Nothing
				X.rawFitness = 1;
				for (int col = 0; col < board_size; col++){
					int row = queenPos[col];
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, col, board_size) == false){
						X.rawFitness = 0;
						return;
					}
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, col, board_size);
				}
				recordSolutionTime();
			break;
		}
	}

//	Apply representation type to genes to get queen positions
	public static void getQueenPositions(Chromo X, int[] queenPos){
		switch(Parameters.valueRepresentation){
			case 1:{ // Normal Column Representation
				for(int geneID = 0; geneID < Parameters.numGenes; geneID++){
					queenPos[geneID] = X.chromo[geneID];
				}
				return;
			}
			case 2:{ // Key Representation
				// If value repeated or too high, replace w/ the next highest unused value
				// excluding the value directly above it (due to diagnols)
				Set<Integer> usedSet = new HashSet<Integer>(Parameters.numGenes);
				int temp;
				System.out.println(Arrays.toString(X.chromo));
				for(int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// If this number has already been used or is too high
					if (usedSet.contains(X.chromo[geneID]) || X.chromo[geneID] >= Parameters.numGenes){
						temp = (X.chromo[geneID] + 2) % Parameters.numGenes;
						while(usedSet.contains(temp)){
							temp = (temp + 1) % Parameters.numGenes;
							System.out.println(temp);
						}
						queenPos[geneID] = temp;
						usedSet.add(temp);
					}
					// Otherwise
					else{
						queenPos[geneID] = X.chromo[geneID];
						usedSet.add(X.chromo[geneID]);
					}
				}
				System.out.println(Arrays.toString(queenPos));
				System.out.println("==============");
				return;
			}
		}
		return;
	}

//	Used when recording a solution to get the time took
	public static void recordSolutionTime(){
		if (Search.found_sol == false){
			Search.found_sol = true;
			Search.first_sol_time_ns = System.nanoTime();
		}

	}

//	PRINT CHROMOSOME
	public static void printChromo(Chromo X){
		System.out.print("[");
		for (int i = 0; i < Parameters.numGenes; i++){
			System.out.print("(" + i + "," + X.getGeneInt(i) + ")");
		}
		System.out.print("\n");
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE
	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneInt(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of OneMax.java ******************************************************
