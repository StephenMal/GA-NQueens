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
		// arrays for tracking queens' positions & bad areas
		int board_size = Parameters.numGenes;
    boolean chessBoard[][] = new boolean[board_size][board_size];
    int usedRow[] = new int[board_size*2];
    int downDiag[] = new int[board_size*2];
    int upDiag[] = new int[board_size*2];

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
			case 2: // Reward 1 point for every queen until we find an invalid placed queen
				X.rawFitness = 0;
				for (int col = 0; col < board_size; col++){
					int row = X.getPosIntGeneValue(col);
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, col, board_size) == false){
						return;
					}
					X.rawFitness++;
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, col, board_size);
				}
				recordSolutionTime();
				break;
			case 3: // Reward 1 point for every queen until we find an invalid placed queen
				X.rawFitness = 0;
				for (int col = 0; col < board_size; col++){
					int row = X.getPosIntGeneValue(col);
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, col, board_size) == false){
						return;
					}
					X.rawFitness += col;
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, col, board_size);
				}
				recordSolutionTime();
				break;
			case 4: // Reward 1 point for every valid placement, do not place invalid placements
				X.rawFitness = 0;
				for (int col = 0; col < board_size; col++){
					int row = X.getPosIntGeneValue(col);
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, col, board_size) == true){
						X.rawFitness++;
						NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, col, board_size);
					}
				}
				if (X.rawFitness == Parameters.numGenes){
					recordSolutionTime();
				}
				break;
			case 5: // Reward 1 point for every valid placement, do not place invalid placements
				X.rawFitness = Parameters.numGenes;
				for (int col = 0; col < board_size; col++){
					int row = X.getPosIntGeneValue(col);
					if (NQueensUtil.checkPlacementValidity(chessBoard, usedRow, downDiag, upDiag, row, col, board_size) == false){
						X.rawFitness--;
					}
					NQueensUtil.placeQueen(chessBoard, usedRow, downDiag, upDiag, row, col, board_size);
				}
				if (X.rawFitness == Parameters.numGenes){
					recordSolutionTime();
				}
				break;
		}

	}

	public static void recordSolutionTime(){
		if (Search.found_sol == false){
			Search.found_sol = true;
			Search.first_sol_time_ns = System.nanoTime();
		}

	}
//	PRINT CHROMOSOME
	public static void printChromo(Chromo X){
		if (Parameters.geneDataType == 1){
			System.out.println("("+X.chromoStr+")");
			}
		System.out.print("[");
		for (int i = 0; i < Parameters.numGenes; i++){
			System.out.print("(" + i + "," + X.getPosIntGeneValue(i) + ")");
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
			Hwrite.right(X.getPosIntGeneValue(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of OneMax.java ******************************************************
