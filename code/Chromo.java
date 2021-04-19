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
		for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
			this.chromo[geneID] = geneID;
		}

		// Shuffle the list
		for (int geneID = Parameters.numGenes - 1; geneID > 0; geneID--){

			// Pick random inedx
			int j = Search.r.nextInt(geneID+1);

			// Swap
			swapGenes(geneID, j);
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
	public void swapGenes(int geneA, int geneB){

		int temp = this.chromo[geneA];
		this.chromo[geneA] = this.chromo[geneB];
		this.chromo[geneB] = temp;
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){
		// Switch to different mutation types dependent on the parameters
		switch(Parameters.mutationType){
			case 1:{	// Displacement

			}
			break;
			case 2:{	// Exchange (Random Swap)
				// Iterate through genes
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// See if should mutate
					if (Search.r.nextDouble() < Parameters.mutationRate){
						swapGenes(geneID, Search.r.nextInt(Parameters.numGenes));
					}
				}
			}
			break;
			case 3:{	// Insertion

				// Create arraylist of current int[]
				ArrayList<Integer> newChromo = new ArrayList<Integer>();
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					newChromo.add(this.chromo[geneID]);
				}

				// Go through and mutate
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// If should mutate
					if (Search.r.nextDouble() < Parameters.mutationRate){
						int geneVal = newChromo.get(geneID); // Save value
						int newIndex = Search.r.nextInt(Parameters.numGenes); // Gen new index
						newChromo.remove(geneID); // Remove value
						newChromo.add(newIndex, geneVal); // Readd at new index
					}
				}

				// Put newChromo genes into our chromosome
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					this.chromo[geneID] = newChromo.get(geneID);
				}
			}
			break;
			case 4:{	// Inversion
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					if (Search.r.nextDouble() < Parameters.mutationRate){
						// Generate proper length
						int length = Search.r.nextInt((int)(Parameters.numGenes * 0.30));
						while (geneID + length >= Parameters.numGenes){
							length = Search.r.nextInt((int)(Parameters.numGenes * 0.30));
						}
						// Inverse the order
						for (int i = geneID; i < geneID+(length-i); i++){
							swapGenes(i, geneID+(length-i));
						}
					}
				}
			}
			break;
			case 5:{	// Local Swap
				// Iterate through genes
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// See if should mutate
					if (Search.r.nextDouble() < Parameters.mutationRate){
						swapGenes(geneID, (geneID+1)%Parameters.numGenes);
					}
				}
			}
			break;
			case 6:{	// Replace Chromosome
				if (Search.r.nextDouble() < Parameters.mutationRate){
					createChromo();
				}
			}
			break;
			case 7:{	// Plus/Minus 1 (would need key rep)
				// Iterate through the genes
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// See if should mutate
					if (Search.r.nextDouble() < Parameters.mutationRate){
						// See if should increment or decrement
						if (Search.r.nextDouble() < 0.5){ // Increment & modulo (n+1 -> 1)
							this.chromo[geneID] = (this.chromo[geneID] + 1) % Parameters.numGenes;
						} // Subtract and absolute value (-1 -> 1)
						else{
							this.chromo[geneID] = Math.abs(this.chromo[geneID] - 1);
						}
					}
				}
			}
			break;
			case 8:{	// Random Replacement (would need key rep)
				// Iterate through the genes
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// See if should mutate
					if (Search.r.nextDouble() < Parameters.mutationRate){
						// If so, replace with random integers 0 - (n-1)
						this.chromo[geneID] = Search.r.nextInt(Parameters.numGenes);
					}
				}
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

		case 1:{     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
		}

		case 2:{     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);
		}

		case 3:{     //  Tournament Selection (size = 2)
			int A = Search.r.nextInt(Parameters.popSize);
			int B = Search.r.nextInt(Parameters.popSize);
			if (Search.member[A].proFitness > Search.member[B].proFitness){
				return A;
			}
			return B;
		}

		case 4:{	// Tournament Selection (size = 5)
			double bestFitness = -1;
			int bestFitIndex = -1;
			int curIndex;
			for (int i = 0; i < 5; i++){
				curIndex = Search.r.nextInt(Parameters.popSize);
				if (Search.member[curIndex].proFitness > bestFitness){
					bestFitness = Search.member[curIndex].proFitness;
					bestFitIndex = curIndex;
				}
			}
			return bestFitIndex;
		}

		case 5:{	// Tournament Selection (size = 10)
			double bestFitness = -1;
			int bestFitIndex = -1;
			int curIndex;
			for (int i = 0; i < 10; i++){
				 curIndex = Search.r.nextInt(Parameters.popSize);
				if (Search.member[curIndex].proFitness > bestFitness){
					bestFitness = Search.member[curIndex].proFitness;
					bestFitIndex = curIndex;
				}
			}
			return bestFitIndex;
		}

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
			case 6:{ // Single Point (would need key rep)

			}
			break;
			case 7:{ // Double Point (would need key rep)

			}
			break;
			case 8:{ // Keep intersections, fill rest w/ randoms
				// Iterate through and record the intersecting genes
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// If the same, record in children
					if (parent1.chromo[geneID] == parent2.chromo[geneID]){
						child1.chromo[geneID] = parent1.chromo[geneID];
						child2.chromo[geneID] = parent2.chromo[geneID];
					}
					else{ // Otherwise replace with random numbers in range
						child1.chromo[geneID] = Search.r.nextInt(Parameters.numGenes);
						child2.chromo[geneID] = Search.r.nextInt(Parameters.numGenes);
					}
				}
			}
			break;
			case 9:{ // Keep intersections, fill rest w/ random nonduplicates
				// Tracks integers we used
				Set<Integer> usedInts1 = new HashSet<Integer>(Parameters.numGenes);
				Set<Integer> usedInts2 = new HashSet<Integer>(Parameters.numGenes);
				// Iterate through the genes
				for (int geneID = 0; geneID < Parameters.numGenes; geneID++){
					// If the same, record in children
					if (parent1.chromo[geneID] == parent2.chromo[geneID]){
						child1.chromo[geneID] = parent1.chromo[geneID];
						child2.chromo[geneID] = parent2.chromo[geneID];
						usedInts1.add(child1.chromo[geneID]);
						usedInts2.add(child2.chromo[geneID]);
					}
					else { // Otherwise find a number we haven't used and place
						// Child 1
						int newGeneVal = Search.r.nextInt(Parameters.numGenes);
						while(usedInts1.contains(newGeneVal) == true){
							newGeneVal = Search.r.nextInt(Parameters.numGenes);
						}
						child1.chromo[geneID] = newGeneVal;
						usedInts1.add(child1.chromo[geneID]);
						// Child 2
						newGeneVal = Search.r.nextInt(Parameters.numGenes);
						while(usedInts2.contains(newGeneVal) == true){
							newGeneVal = Search.r.nextInt(Parameters.numGenes);
						}
						child2.chromo[geneID] = newGeneVal;
						usedInts2.add(child2.chromo[geneID]);
					}
				}
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
