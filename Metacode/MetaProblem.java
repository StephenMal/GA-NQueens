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
		Parameters.
		Search.
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
