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
		int queenPlacement[] = getQueenPositions(X);

		// Switch between the fitness function types
		switch(Parameters.fitnessFunctionType){
			case 1: // All or Nothing
				X.rawFitness = 1;
				for (int col = 0; col < board_size; col++){
					int row = X.getPosIntGeneValue(col);
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
	public static int[] getQueenPositions(Chromo X){
		switch(Parameters.valueRepresentation == 1){
			case 1:{ // Normal Column Representation
				return X.chromo;
			}
			break;
			/*case 2:{ // Key Representation

			}
			break;
			*/
		}
		return null;
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

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
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
