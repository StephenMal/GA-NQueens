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
				String curGene = "";
				for (int j=0; j<Parameters.geneSize; j++){
					randnum = Search.r.nextDouble();
					if (randnum > 0.5) geneBit = '0';
					else geneBit = '1';
					curGene = curGene + geneBit;
				}
				chromoStr += applyBounds(curGene);
			}
			//NQueens.printChromo(this);
		}
		// If using an integer array, set gene values to a random int value 0 - upperBound
		if (Parameters.geneDataType == 2){
			chromoInt = new int[Parameters.numGenes];
			for (int i = 0; i < Parameters.numGenes; i++){
				chromoInt[i] = Search.r.nextInt(Parameters.numGenes);
			}
			//NQueens.printChromo(this);
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
		// Print out error statement
		System.out.println("Error: Unset Gene Data Type");
		return "";
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
		// Print out error statement
		System.out.println("Error: Unset Gene Data Type");
		return -1;
	}

	//  Get Integer Value of a Gene (Positive only)
	public int getPosIntGeneValue(int geneID){
		if (Parameters.geneDataType == 1){
			return binaryStrToPosInt(getGeneAlpha(geneID));
		}
		if (Parameters.geneDataType == 2){
			return Math.abs(getIntGeneValue(geneID));
		}
		// Print out error statement
		System.out.println("Error: Unset Gene Data Type");
		return -1;
	}


	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){
		// Mutation when dealing with binary string
		if (Parameters.geneDataType == 1){
			String mutChromo = "";
			char x;
			switch (Parameters.mutationType){
				case 1:     //  Replace with new random number
					for (int gene = 0; gene < Parameters.numGenes; gene++){
						String mutGene = "";
						for (int bit = 0; bit < Parameters.geneSize; bit++){
							x = this.chromoStr.charAt((gene*Parameters.geneSize)+bit);
							randnum = Search.r.nextDouble();
							if (randnum < Parameters.mutationRate){
								if (x == '1') x= '0';
								else x = '1';
							}
							mutGene += x;
						}
						mutChromo += applyBounds(mutGene);
					}
					this.chromoStr = mutChromo;
					break;
				default:
					System.out.println("ERROR - No mutation method selected");
			}
		}

		// Mutation when dealing with integer array
		if (Parameters.geneDataType == 2){
			switch (Parameters.mutationType){
				case 1:     //  Replace with new random number
					for (int gene=0; gene<Parameters.numGenes; gene++){
						randnum = Search.r.nextDouble();
						if (randnum < Parameters.mutationRate){
							this.chromoInt[gene] = applyBounds(Search.r.nextInt());
						}
					}
					break;

				default:
					System.out.println("ERROR - No mutation method selected");
			}
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

		// For Binary String
		if (Parameters.geneDataType == 1){
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
		}

		// For Integer Array
		if (Parameters.geneDataType == 2){
			switch (Parameters.xoverType){
				case 1:     //  Single Point Crossover

					//  Select crossover point
					xoverPoint1 = 1 + Search.r.nextInt(Parameters.numGenes - 1);
					// Create children chromosomes from parental material
					for (int gene = 0; gene < Parameters.numGenes; gene++){
						// before cross over point
						if (gene < xoverPoint1){
							child1.chromoInt[gene] = parent1.chromoInt[gene];
							child2.chromoInt[gene] = parent2.chromoInt[gene];
						}
						else {
							child1.chromoInt[gene] = parent2.chromoInt[gene];
							child2.chromoInt[gene] = parent1.chromoInt[gene];
						}
					}
					break;

				case 2:     //  Two Point Crossover

				case 3:     //  Uniform Crossover

				default:
					System.out.println("ERROR - Bad crossover method selected");
			}
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
		if (Parameters.geneDataType == 1){
			child.chromoStr = parent.chromoStr;
		}
		if (Parameters.geneDataType == 2){
			child.chromoInt = parent.chromoInt;
		}

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		if (Parameters.geneDataType == 1){
			targetA.chromoStr = sourceB.chromoStr;
		}
		if (Parameters.geneDataType == 2){
			targetA.chromoInt = sourceB.chromoInt;
		}

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

	// Change an integer value into a binary string of usable size
	public static String intToBinaryStr(int value){
		return String.format("%" + Parameters.geneSize + "s", Integer.toBinaryString(value)).replaceAll(" ", "0");
	}

	// Change binary string into a positive integer value
	public static int binaryStrToPosInt(String binaryStr){
		int geneValue;
		char geneBit;
		geneValue = 0;
		for (int i=Parameters.geneSize-1; i>=0; i--){
			geneBit = binaryStr.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		return (geneValue);
	}

	// Apply boundary protection
	public static String applyBounds(String geneStr){
		// If no boundary enforcement, return original value
		if (Parameters.boundaryEnforcement == 0){
			return geneStr;
		}
		 // Otherwise apply enforcement
		if (Parameters.boundaryEnforcementType == 1){
			return intToBinaryStr(binaryStrToPosInt(geneStr) % Parameters.numGenes);
		}
		if (Parameters.boundaryEnforcementType == 2){
			int val = binaryStrToPosInt(geneStr);
			if (val > Parameters.numGenes){
				return intToBinaryStr(Parameters.numGenes-1);
			}
		}
		System.out.println("Error applying bounds");
		return geneStr;
	}

	public static int applyBounds(int geneInt){
		System.out.print("Gene Int: " + geneInt);
		// If no boundary enforcement, return original value
		if (Parameters.boundaryEnforcement == 0){
			System.out.print(" returned as is\n");
			return geneInt;
		}
		// Otherwise apply enforcement
	 if (Parameters.boundaryEnforcementType == 1){
		 System.out.print(" % " + Parameters.numGenes + " = " + (geneInt % Parameters.numGenes) + "\n");
		 return geneInt % Parameters.numGenes;
	 }
	 if (Parameters.boundaryEnforcementType == 2){
		 if (geneInt > Parameters.numGenes){
			 System.out.print(" capped at " + (Parameters.numGenes-1) + " = " + (Parameters.numGenes-1) + "\n");
			 return Parameters.numGenes-1;
		 }
	 }
	 System.out.println("Error applying bounds");
	 return geneInt;
	}
}   // End of Chromo.java ******************************************************
