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

	public int chromo[];
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

		// Create the chromosome
		createChromo();

		// Initialize other values
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}

	// Creates chromosome
	public void createChromo(){
		// Create the array
		this.chromo = new int[Parameters.numGenes];

		// Fill the array with incrementing numbers (0 - (n-1))
		for (int i = 0; i < Parameters.numGenes; i++){
			this.chromo[i] = i;
		}

		// Shuffle the list
		for (int i = Parameters.numGenes - 1; i < 0; i--){

			// Pick random inedx
			int j = Search.r.nextInt(geneID+1);

			// Swap
			swapGenes(int geneA, int geneB);
		}
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	// Get Gene Value
	public int getGeneInt(int geneID){

		return this.chromo[geneID];

	}

	//	Swap genes
	public int swapGenes(int geneA, int geneB){

		this.chromo[geneA] = this.chromo[geneA] + this.chromo[geneB];
		this.chromo[geneB] = this.chromo[geneA] - this.chromo[geneB];
		this.chromo[geneA] = this.chromo[geneA] - this.chromo[geneB];

	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){
		// Switch to different mutation types dependent on the parameters
		switch(Parameters.mutationType){
			case 1:{	// Displacement

			}
			break;
			case 2:{	// Exchange

			}
			break;
			case 3:{	// Insertion

			}
			break;
			case 4:{	// Inversion

			}
			break;
			case 5:{	// Local Swap

			}
			break;
			case 6:{	// Random Swap

			}
			break;
			case 7:{	// Total Replacement

			}
			break;
			case 8:{	// Flip Bit (would need key rep)

			}
			break;
			case 9:{	// Plus/Minus 1 (would need key rep)

			}
			break;
			case 10:{	// Random Replacement (would need key rep)

			}
			break;

		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover
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

	//  Produce a new child from two parents
	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		// Find crossover type from parameters
		switch(Parameters.xoverType){
			case 1:{ // Partially Mapped

			}
			break;
			case 2:{ // Order

			}
			break;
			case 3:{ // Order-based

			}
			break;
			case 4:{ // Position-Based

			}
			break;
			case 5:{ // Cycle Crossover

			}
			break;
			case 6:{ // Single Point Int (would need key rep)

			}
			break;
			case 7:{ // Single Point Binary (would need key rep)

			}
			break;
			case 8:{ // Double Point Int (would need key rep)

			}
			break;
			case 9:{ // Double Point Binary (would need key rep)

			}
			break;
			case 10:{ // Uniform (would need key rep)

			}
			break;
			case 11:{ // Keep intersections, fill rest w/ randoms

			}
			break;
			case 12:{ // Keep intersections, fill rest w/ random nonduplicates

			}
			break;
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent
	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another
	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
