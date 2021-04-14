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

	public String chromo; // Chromo if in binary string form
	public int[] intChromo; // Chromo if in integer form
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

		// If gene data type set to binary string (1)
		if (Parameters.geneDataType == 1){
			//  Set gene values to a randum sequence of 1's and 0's
			char geneBit;
			chromo = "";
			for (int i=0; i<Parameters.numGenes; i++){
				for (int j=0; j<Parameters.geneSize; j++){
					randnum = Search.r.nextDouble();
					if (randnum > 0.5) geneBit = '0';
					else geneBit = '1';
					this.chromo = chromo + geneBit;
				}
			}
		}

		// If gene data type set to integer list (2)
		if (Parameters.geneDataType == 2){
			intChromo = new int[Parameters.numGenes]
			for (int i = 0; i < Parameters.numGenes; i++){
				randnum = Search.r.nextInt() % Parameters.numGenes;
			}
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		int start = geneID * Parameters.geneSize;
		int end = (geneID+1) * Parameters.geneSize;
		String geneAlpha = this.chromo.substring(start, end);
		return (geneAlpha);
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
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

	//  Get Integer Value of a Gene (Positive only) ****************************

	public int getPosIntGeneValue(int geneID){
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

	// Turn integer value into binary string of same size as gene size
	public String intToBinaryStr(int intVal){
		return String.format("%" + Parameters.geneSize + "s", Integer.toBinaryString(intVal)).replaceAll(" ","0");
	}

	//  Check and fix boundaries for genes depending on gene data type & boundary settings

	public String fixBoundaries(String geneStr){
		int integerGeneValue = Integer.parseInt(geneStr, 2);
		// Check if above upper bound
		// Enforce with cap on boardSize
		if (Parameters.enforceBoundaries == 2){
			if (integerGeneValue > Parameters.upperBound){
				return intToBinaryStr(Parameters.upperBound);
			}
			if (integerGeneValue < 0)
		}
		else{ // otherwise must enforce w/ modulo either for board size of gene size
			return intToBinaryStr(integerGeneValue % Parameters.upperBound);
		}
		// Otherwise return with no changes
		return geneStr;
	}

	public int fixBoundaries(int geneInt){
		// Enforce with cap on boardSize
		if (Parameters.enforceBoundaries == 2){
			if (geneInt > Parameters.upperBound){
				return Parameters.upperBound;
			}
			if (geneInt < 0){
				return 0;
			}
		}
		else{ // otherwise must enforce w/ modulo either for board size of gene size
			if (geneInt > Parameters.upperBound){
				return geneInt % Parameters.upperBound;
			}
			if (geneInt < 0){
				return Math.abs(geneInt) % Parameters.upperBound;
			}
		}
		// Otherwise return with no changes
		return geneInt;
	}
	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		String mutChromo = "";
		char x;

		switch (Parameters.mutationType){

		case 1:     //  Replace with new random number
			// If using binary string representation (1)
			if (Parameters.geneDataType == 1){
				for (int i = 0; i < Parameters.numGenes; i++){
					mutGene = ""
					for (int j=0; j<(Parameters.geneSize); j++){
						x = this.chromo.charAt((i*Parameters.geneSize)+j);
						randnum = Search.r.nextDouble();
						if (randnum < Parameters.mutationRate){
							if (x == '1') x = '0';
							else x = '1';
						}
						mutGene = mutGene + x;
					}
					mutChromo = mutChromo + fixBoundaries(mutGene);
				}
				this.chromo = mutChromo
			}
			// If using integer list representation (2)
			if (Parameters.geneDateType == 2){
				for(int j=0; j<Parameters.numGenes; j++){
					randnum = Search.r.nextDouble();
					if (randnum < Parameters.mutationRate){
						this.intChromo[j] = fixBoundaries(Search.r.nextInt());
					}
				}
			}
		case 2:		// Plus or Minus 1
			// If using binary string representation (1)
			if (Parameters.geneDataType == 1){
				for (int j=0; j<(Parameters.numGenes); j++){
					randnum = Search.r.nextDouble();
					// If mutation
					if (randnum < Parameters.mutationRate){
						randnum = Search.r.nextDouble();
						if (randnum < 0.50){ // Subtract 1
							int intGeneValue = getIntGeneValue(j);
							if (intGeneValue == 0){ // Fix bounds before going into negative binary
								if (Parameters.enforceBoundaries == 2){
									mutChromo += intToBinaryStr(0);
								}
								else{
									mutChromo += intToBinaryStr(Math.abs(intGeneValue - 1) % Parameters.upperBound);
								}
							}
							else{ //otherwise not zero, go ahead and do normal ops
								mutChromo += fixBoundaries(Integer.toBinaryString(intGeneValue - 1));
							}
						}
						if (randnum >= 0.50){ // Add 1
							mutChromo += fixBoundaries(Integer.toBinaryString(intGeneValue + 1));
						}
					}
					else { // Otherwise just add the normal value
						mutChromo += getGeneAlpha(j);
					}
				}
				this.chromo = mutChromo;
			}
			// If using integer list representation (2)
			if (Parameters.geneDateType == 2){
				for(int j=0; j<Parameters.numGenes; j++){
					randnum = Search.r.nextDouble();
					if (randnum < Parameters.mutationRate){
						randnum = Search.r.nextDouble();
						if (randnum < 0.50){ // subtract 1
							this.intChromo[j]--;
						}
						else { // add 1
							this.intChromo[j]++;
						}
						this.intChromo[j] = fixBoundaries(this.intChromo[j]);
					}
				}
			}
		case 3: // Local Swap
		// If using binary string representation (1)
		if (Parameters.geneDataType == 1){
			for (int j=0; j<(Parameters.numGenes); j++){
				mutChromo += lastGene;
				randnum = Search.r.nextDouble();
				if (randnum < Parameters.mutationRate){
					randnum = Search.r.nextDouble();
					if (randnum < 0.50){ // Swap with left (ignore if leftmost gene)
						if (j != 0){
							lastGene = getGeneAlpha(j-1)
						}
					}
					if (randnum >= 0.50){ // Swap with right

					}
				}

			}

			this.chromo = fixBoundaries(mutChromo);
		}

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
			child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
			child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
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
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
