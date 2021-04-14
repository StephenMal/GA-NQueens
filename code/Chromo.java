/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public String chromoStr;
	public int[] chromoInt;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  If using binary string, set gene values to a randum sequence of 1's and 0's
		if (Parameters.geneDataType == 1){
			char geneBit;
			chromoStr = "";
			for (int i=0; i<Parameters.numGenes; i++){
				for (int j=0; j<Parameters.geneSize; j++){
					randnum = Search.r.nextDouble();
					if (randnum > 0.5) geneBit = '0';
					else geneBit = '1';
					this.chromoStr = chromoStr + geneBit;
				}
			}
		}
		// If using an integer array, set gene values to a random int value 0 - upperBound
		if (Parameters.geneDataType == 2){
			chromoInt = int[Parameters.numGenes];
			for (int i = 0; i < Parameters.numGenes; i++){
				chromoInt[i] = Search.r.nextInt(Parameters.upperBound);
			}
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/


	//  Get Alpha (binary string) Represenation of a Gene
	public String getGeneAlpha(int geneID){
		// If dealing with binary string, cut needed pieces
		if(Parameters.geneDataType == 1){
			int start = geneID * Parameters.geneSize;
			int end = (geneID+1) * Parameters.geneSize;
			String geneAlpha = this.chromoStr.substring(start, end);
			return (geneAlpha);
		}
		if(Parameters.geneDataType == 2){
			return intToBinaryStr(getIntGeneValue(geneID));
		}
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment)  (Binary String)
	public int getIntGeneValue(int geneID){
		// If dealing with Binary Strings
		if(Parameters.geneDataType == 1){
			String geneAlpha = "";
			int geneValue;
			char geneSign;
			char geneBit;
			geneValue = 0;
			geneAlpha = getGeneAlpha(geneID);
			for (int i=Parameters.geneSize-1; i>=1; i--){
				geneBit = geneAlpha.charAt(i);
				if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
			}
			geneSign = geneAlpha.charAt(0);
			if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
			return (geneValue);
		}
		// If dealing with integers
		if(Parameters.geneDataType == 2){
			return this.chromoInt[geneID];
		}
	}

	//  Get Integer Value of a Gene (Positive only)
	public int getPosIntGeneValue(int geneID){
		if (Parameters.geneDataType == 1){
			String geneAlpha = "";
			int geneValue;
			char geneBit;
			geneValue = 0;
			geneAlpha = getGeneAlpha(geneID);
			for (int i=Parameters.geneSize-1; i>=0; i--){
				geneBit = geneAlpha.charAt(i);
				if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
			}
			return (geneValue);
		}
		if (Parameters.geneDataType == 2){
			return Math.abs(getIntGeneValue(int geneID));
		}
	}


	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		String mutChromo = "";
		char x;

		switch (Parameters.mutationType){

		case 1:     //  Replace with new random number

			for (int j=0; j<(Parameters.geneSize * Parameters.numGenes); j++){
				x = this.chromoStr.charAt(j);
				randnum = Search.r.nextDouble();
				if (randnum < Parameters.mutationRate){
					if (x == '1') x = '0';
					else x = '1';
				}
				mutChromo = mutChromo + x;
			}
			this.chromoStr = mutChromo;
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			//  Create child chromosome from parental material
			child1.chromoStr = parent1.chromoStr.substring(0,xoverPoint1) + parent2.chromoStr.substring(xoverPoint1);
			child2.chromoStr = parent2.chromoStr.substring(0,xoverPoint1) + parent1.chromoStr.substring(xoverPoint1);
			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromoStr = parent.chromoStr;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromoStr = sourceB.chromoStr;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

	// Change an integer value into a binary string of usable size
	public static void intToBinaryStr(int value){
		return String.format("%" + len + "s", Integer.toBinaryString(value)).replaceAll(" ", "0");
	}
}   // End of Chromo.java ******************************************************
