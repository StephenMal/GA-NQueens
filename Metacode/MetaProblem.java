/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class MetaProblem extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public MetaProblem(){
		name = "Meta NQueens Problem";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS

	public void doRawFitness(MetaChromo X){

		// Increment number of queens we have ran
		//	(So we can print each run summaries)
		MetaSearch.numberOfNQueenRuns++;
		// Put this current Meta Chromosome in the nqueens search
		Search.currentMeta = X;
		// Run the search
		String[] arguments = new String[] {"NQueens.params"};
		Search.main(arguments);

		// If it was able to find a solution, set to how long it took
		if(Search.found_sol == true){
			X.rawFitness = Search.first_sol_time_ns;
			if(NQueensUtilDebugTool.validateSolution(Search.first_solution_board) == false){
				System.out.println("Invalid solution marked as correct");
			}
		}
		else{ // Otherwise punish for no solution
			// Punish by multiplying the time it took by 10
			X.rawFitness = 10 * Search.dur_of_run;
		}
	}



//	PRINT CHROMOSOME
	public static void printChromo(MetaChromo X){
		System.out.print("[");
		for (int i = 0; i < MetaParameters.numGenes; i++){
			System.out.print("(" + i + "," + X.getGeneInt(i) + ")");
		}
		System.out.print("\n");
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE
	public void doPrintGenes(MetaChromo X, FileWriter output) throws java.io.IOException{

		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<MetaParameters.numGenes; i++){
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
