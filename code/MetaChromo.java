
/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class MetaChromo {
	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	public int chromo[];
	public int minGene[];
	public int maxGene[];
	public double rawFitness;
	public double sclFitness;
	public double proFitness;
	public static boolean testCode = false;

	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	private static double randnum;

	/*******************************************************************************
	 * CONSTRUCTORS *
	 *******************************************************************************/

	public Chromo() {

		// Create gene boundaries
		this.minGene = new int[MetaParameters.numGenes];
		this.maxGene = new int[MetaParameters.numGenes];

		// 0: Generations Per Run (10 - 1000)
		this.minGene[0] = 10;
		this.maxGene[0] = 1000;
		// 1: Population Size (10 - 1000)
		this.minGene[1] = 10;
		this.maxGene[1] = 1000;
		// 2: Selection Method (1 - 5)
		this.minGene[2] = 1;
		this.maxGene[2] = 5;
		// 3: Fitness Scaling Type (0 - 1)
		this.minGene[3] = 0;
		this.maxGene[3] = 1;
		// 4: Crossover Type (1 - 9)
		this.minGene[4] = 1;
		this.maxGene[4] = 9;
		// 5: Crossover Rate (0 - 1000)
		this.minGene[5] = 0;
		this.maxGene[5] = 1000;
		// 6: Mutation Type (1-8)
		this.minGene[6] = 1;
		this.maxGene[6] = 8;
		// 7: Mutation Rate (0 - 1000)
		this.minGene[7] = 0;
		this.maxGene[7] = 1000;
		// 8: Rep Type (1 - 2)
		this.minGene[8] = 1;
		this.maxGene[8] = 2;
		// 9 Fitness Function (1 - 6)
		this.minGene[9] = 1;
		this.maxGene[9] = 6;


		// Create the chromosome
		createChromo();

		// Initialize other values
		this.rawFitness = -1; // Fitness not yet evaluated
		this.sclFitness = -1; // Fitness not yet scaled
		this.proFitness = -1; // Fitness not yet proportionalized
	}

	// Creates chromosome
	public void createChromo() {
		// Create chromo
		this.chromo = new float[MetaParameters.numGenes];
		// Find values
		for (int geneID = 0; geneID < MetaParameters.numGenes; geneID++){
			this.generateGene(geneID);
		}
	}

	public void generateGene(int geneID){
		int max = this.maxGene[geneID];
		int min = this.minGene[geneID];

		this.chromo[geneID] = MetaSearch.r.nextInt(max - min + 1) + min;
	}

	/*******************************************************************************
	 * MEMBER METHODS *
	 *******************************************************************************/

	// Get Gene Value
	public int getGeneInt(int geneID) {

		return this.chromo[geneID];

	}

	// Swap genes
	public void swapGenes(int geneA, int geneB) {

		int temp = this.chromo[geneA];
		this.chromo[geneA] = this.chromo[geneB];
		this.chromo[geneB] = temp;
	}

	// Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation() {
		// Switch to different mutation types dependent on the parameters
		for (int geneID = 0; geneID < MetaParameters.numGenes; geneID++){
			if (MetaSearch.r.nextDouble() < MetaParameters.mutationRate){
				this.generateGene(geneID);
			}
		}
	}

	/*******************************************************************************
	 * STATIC METHODS *
	 *******************************************************************************/
	public static void printChromo(Chromo X) {
		for (int geneID = 0; geneID < MetaParameters.numGenes; geneID++) {
			System.out.print(X.chromo[geneID] + " ");
		}
		System.out.println("");
	}

	// Select a parent for crossover
	public static int selectParent() {

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (MetaParameters.selectType) {

		case 1: { // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j = 0; j < MetaParameters.popSize; j++) {
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel)
					return (j);
			}
		}

		case 2: { // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * MetaParameters.popSize);
			return (j);
		}

		case 3: { // Tournament Selection (size = 2)
			int A = Search.r.nextInt(MetaParameters.popSize);
			int B = Search.r.nextInt(MetaParameters.popSize);
			if (Search.member[A].proFitness > Search.member[B].proFitness) {
				return A;
			}
			return B;
		}

		case 4: { // Tournament Selection (size = 5)
			double bestFitness = -1;
			int bestFitIndex = -1;
			int curIndex;
			for (int i = 0; i < 5; i++) {
				curIndex = Search.r.nextInt(MetaParameters.popSize);
				if (Search.member[curIndex].proFitness > bestFitness) {
					bestFitness = Search.member[curIndex].proFitness;
					bestFitIndex = curIndex;
				}
			}
			return bestFitIndex;
		}

		case 5: { // Tournament Selection (size = 10)
			double bestFitness = -1;
			int bestFitIndex = -1;
			int curIndex;
			for (int i = 0; i < 10; i++) {
				curIndex = Search.r.nextInt(MetaParameters.popSize);
				if (Search.member[curIndex].proFitness > bestFitness) {
					bestFitness = Search.member[curIndex].proFitness;
					bestFitIndex = curIndex;
				}
			}
			return bestFitIndex;
		}

		default:
			System.out.println("ERROR - No selection method selected");
		}
		return (-1);
	}

	// Produce a new child from two parents
	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2) {

		if (testCode) {
			System.out.println("Parents");
			printChromo(parent1);
			printChromo(parent2);
		}

		int xoverPoint1, xoverPoint2, len = parent1.chromo.length;
		xoverPoint1 = 1 + (int) (Search.r.nextDouble() * (MetaParameters.numGenes));
		xoverPoint2 = 1 + (int) (Search.r.nextDouble() * (MetaParameters.numGenes));
			if (xoverPoint1 > xoverPoint2) {
			int temp = xoverPoint1;
			xoverPoint1 = xoverPoint2;
			xoverPoint2 = temp;
		}
		// Create child chromosomes from parent genes
		for (int i = 0; i < xoverPoint1; i++) {
			child1.chromo[i] = parent1.chromo[i];
			child2.chromo[i] = parent2.chromo[i];
		}
		for (int i = xoverPoint1; i < xoverPoint2; i++) {
			child1.chromo[i] = parent2.chromo[i];
			child2.chromo[i] = parent1.chromo[i];
		}
		for (int i = xoverPoint2; i < len; i++) {
			child1.chromo[i] = parent1.chromo[i];
			child2.chromo[i] = parent2.chromo[i];
		}
		// Set fitness values back to zero
		child1.rawFitness = -1; // Fitness not yet evaluated
		child1.sclFitness = -1; // Fitness not yet scaled
		child1.proFitness = -1; // Fitness not yet proportionalized
		child2.rawFitness = -1; // Fitness not yet evaluated
		child2.sclFitness = -1; // Fitness not yet scaled
		child2.proFitness = -1; // Fitness not yet proportionalized
	}

	// Produce a new child from a single parent
	public static void mateParents(int pnum, Chromo parent, Chromo child) {

		// Create child chromosome from parental material
		child.chromo = parent.chromo;

		// Set fitness values back to zero
		child.rawFitness = -1; // Fitness not yet evaluated
		child.sclFitness = -1; // Fitness not yet scaled
		child.proFitness = -1; // Fitness not yet proportionalized
	}

	// Copy one chromosome to another
	public static void copyB2A(Chromo targetA, Chromo sourceB) {

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

} // End of Chromo.java ******************************************************
