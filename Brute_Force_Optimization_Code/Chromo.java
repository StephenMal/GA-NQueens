
/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo {
	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	public int chromo[];
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

		// Create the chromosome
		createChromo();

		// Initialize other values
		this.rawFitness = -1; // Fitness not yet evaluated
		this.sclFitness = -1; // Fitness not yet scaled
		this.proFitness = -1; // Fitness not yet proportionalized
	}

	// Creates chromosome
	public void createChromo() {
		// Create the array
		this.chromo = new int[Parameters.numGenes];

		// Fill the array with incrementing numbers (0 - (n-1))
		for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
			this.chromo[geneID] = geneID;
		}

		// Shuffle the list
		for (int geneID = Parameters.numGenes - 1; geneID > 0; geneID--) {

			// Pick random inedx
			int j = Search.r.nextInt(geneID + 1);

			// Swap
			swapGenes(geneID, j);
		}
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
		switch (Parameters.mutationType) {
		case 1: { // Displacement
			int chunkStart, chunkEnd, temp, index, j, k;
			int[] chunkq;
			int[] restq;

			chunkStart = Search.r.nextInt(Parameters.numGenes);
			chunkEnd = Search.r.nextInt(Parameters.numGenes);
			index = Search.r.nextInt(Parameters.numGenes);

			if (chunkStart > chunkEnd)
			{
				temp = chunkEnd;
				chunkEnd = chunkStart;
				chunkStart = temp;
			}



			chunkq = new int[chunkEnd - chunkStart];
			restq = new int[this.chromo.length - chunkq.length];
			j = chunkStart;

			for(int i = 0; i < chunkq.length; i++)
			{
				chunkq[i] = this.chromo[j];
				j++;
			}

			j = 0;
			for(int i = 0; i < this.chromo.length; i++)
			{
				if(i < chunkStart || i >= chunkEnd)
				{
					restq[j] = this.chromo[i];
					j++;
				}
			}

			j = 0;
			k = 0;
			for(int i = 0; i < this.chromo.length && k < restq.length; i++)
			{
				if(i == index)
				{
					while(j < chunkq.length)
					{
						this.chromo[i] = chunkq[j];
						j++;
						i++;
					}
					i--;
				}
				else
				{
					this.chromo[i] = restq[k];
					k++;
				}
			}
		}
		break;
		case 2: { // Exchange (Random Swap)
			// Iterate through genes
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// See if should mutate
				if (Search.r.nextDouble() < Parameters.mutationRate) {
					swapGenes(geneID, Search.r.nextInt(Parameters.numGenes));
				}
			}
		}
			break;
		case 3: { // Insertion

			// Create arraylist of current int[]
			ArrayList<Integer> newChromo = new ArrayList<Integer>();
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				newChromo.add(this.chromo[geneID]);
			}

			// Go through and mutate
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// If should mutate
				if (Search.r.nextDouble() < Parameters.mutationRate) {
					int geneVal = newChromo.get(geneID); // Save value
					int newIndex = Search.r.nextInt(Parameters.numGenes); // Gen new index
					newChromo.remove(geneID); // Remove value
					newChromo.add(newIndex, geneVal); // Readd at new index
				}
			}

			// Put newChromo genes into our chromosome
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				this.chromo[geneID] = newChromo.get(geneID);
			}
		}
			break;
		case 4: { // Inversion
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				if (Search.r.nextDouble() < Parameters.mutationRate) {
					// Generate proper length
					int length = Search.r.nextInt((int) (Parameters.numGenes * 0.30));
					while (geneID + length >= Parameters.numGenes) {
						length = Search.r.nextInt((int) (Parameters.numGenes * 0.30));
					}
					// Inverse the order
					for (int i = geneID; i < geneID + (length - i); i++) {
						swapGenes(i, geneID + (length - i));
					}
				}
			}
		}
			break;
		case 5: { // Local Swap
			// Iterate through genes
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// See if should mutate
				if (Search.r.nextDouble() < Parameters.mutationRate) {
					swapGenes(geneID, (geneID + 1) % Parameters.numGenes);
				}
			}
		}
			break;
		case 6: { // Replace Chromosome
			if (Search.r.nextDouble() < Parameters.mutationRate) {
				createChromo();
			}
		}
			break;
		case 7: { // Plus/Minus 1 (would need key rep)
			// Iterate through the genes
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// See if should mutate
				if (Search.r.nextDouble() < Parameters.mutationRate) {
					// See if should increment or decrement
					if (Search.r.nextDouble() < 0.5) { // Increment & modulo (n+1 -> 1)
						this.chromo[geneID] = (this.chromo[geneID] + 1) % Parameters.numGenes;
					} // Subtract and absolute value (-1 -> 1)
					else {
						this.chromo[geneID] = Math.abs(this.chromo[geneID] - 1);
					}
				}
			}
		}
			break;
		case 8: { // Random Replacement (would need key rep)
			// Iterate through the genes
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// See if should mutate
				if (Search.r.nextDouble() < Parameters.mutationRate) {
					// If so, replace with random integers 0 - (n-1)
					this.chromo[geneID] = Search.r.nextInt(Parameters.numGenes);
				}
			}
		}
			break;
		}
	}

	/*******************************************************************************
	 * STATIC METHODS *
	 *******************************************************************************/
	public static void printChromo(Chromo X) {
		for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
			System.out.print(X.chromo[geneID] + " ");
		}
		System.out.println("");
	}

	// Select a parent for crossover
	public static int selectParent() {

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType) {

		case 1: { // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j = 0; j < Parameters.popSize; j++) {
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel)
					return (j);
			}
		}

		case 2: { // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return (j);
		}

		case 3: { // Tournament Selection (size = 2)
			int A = Search.r.nextInt(Parameters.popSize);
			int B = Search.r.nextInt(Parameters.popSize);
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
				curIndex = Search.r.nextInt(Parameters.popSize);
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
				curIndex = Search.r.nextInt(Parameters.popSize);
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
		// Find crossover type from parameters
		switch (Parameters.xoverType) {
		case 1: { // Partially Mapped: PMX
			int xoverPoint1 = Search.r.nextInt(Parameters.numGenes);
			int xoverPoint2 = Search.r.nextInt(Parameters.numGenes);
			// Swap values if 2 is before 1
			if (xoverPoint2 < xoverPoint1) {
				int temp = xoverPoint1;
				xoverPoint1 = xoverPoint2;
				xoverPoint2 = temp;
			}
			// System.out.println("XP1: " + xoverPoint1 + ", XP2: " + xoverPoint2);
			// Map to keep track of maped pair of two parents
			HashMap<Integer, Integer> map1 = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> map2 = new HashMap<Integer, Integer>();

			// Copy parent to each child
			for (int index = 0; index < Parameters.numGenes; index++) {
				child1.chromo[index] = parent1.chromo[index];
				child2.chromo[index] = parent2.chromo[index];
			}

			// Add xp1 to xp2 portion into the offscprings and generate the mapping
			for (int index = xoverPoint1; index <= xoverPoint2; index++) {
				map1.put(parent1.chromo[index], parent2.chromo[index]);
				map2.put(parent2.chromo[index], parent1.chromo[index]);

				child1.chromo[index] = parent2.chromo[index];
				child2.chromo[index] = parent1.chromo[index];
			}

			// Populate rest of the values in the offsprings
			for (int index = 0; index < Parameters.numGenes; index++) {
				if (index >= xoverPoint1 && index <= xoverPoint2)
					continue;

				// Populate values for child 1
				while (map2.containsKey(child1.chromo[index])){
					child1.chromo[index] = map2.remove(child1.chromo[index]);
				}
				// Populate values for child 2
				while (map1.containsKey(child2.chromo[index])){
					child2.chromo[index] = map1.remove(child2.chromo[index]);
				}
			}
		}
			break;
		case 2: { // Order
			int xoverPoint1 = Search.r.nextInt(Parameters.numGenes);
			int xoverPoint2 = Search.r.nextInt(Parameters.numGenes);
			// Swap values if 2 is before 1
			if (xoverPoint2 < xoverPoint1) {
				int temp = xoverPoint1;
				xoverPoint1 = xoverPoint2;
				xoverPoint2 = temp;
			}
			// System.out.println("XP1: " + xoverPoint1 + ", XP2: " + xoverPoint2);
			// Record order & perform pass down cross over area
			// Queues record orderings
			Queue<Integer> parent1_order = new LinkedList<>();
			Queue<Integer> parent2_order = new LinkedList<>();
			// Sets record numbers used already used in cross over zones
			Set<Integer> child1_set = new HashSet<Integer>(xoverPoint2 - xoverPoint1);
			Set<Integer> child2_set = new HashSet<Integer>(xoverPoint2 - xoverPoint1);
			for (int index = 0; index < Parameters.numGenes; index++) {
				int geneID = (xoverPoint2 + index) % Parameters.numGenes;
				// record ordering
				parent1_order.add(parent1.chromo[geneID]);
				parent2_order.add(parent2.chromo[geneID]);
				// If in crossover point, record cross & add to set
				if (geneID >= xoverPoint1 && geneID < xoverPoint2) {
					child1.chromo[geneID] = parent1.chromo[geneID];
					child1_set.add(parent1.chromo[geneID]);
					child2.chromo[geneID] = parent2.chromo[geneID];
					child2_set.add(parent2.chromo[geneID]);
				}
			}
			// Fill in child 2
			int geneID = xoverPoint2;
			while (parent1_order.peek() != null) {
				// if already exists in the child, skip it
				if (child2_set.contains(parent1_order.peek())) {
					parent1_order.remove();
				} else { // Otherwise place in chromo and increment the index
					child2.chromo[geneID] = parent1_order.poll();
					geneID = (geneID + 1) % Parameters.numGenes;
				}
			}
			// Fill in child 1
			geneID = xoverPoint2;
			while (parent2_order.peek() != null) {
				// if already exists in the child, skip it
				if (child1_set.contains(parent2_order.peek())) {
					parent2_order.remove();
				} else { // Otherwise place in chromo and increment the index
					child1.chromo[geneID] = parent2_order.poll();
					geneID = (geneID + 1) % Parameters.numGenes;
				}
			}
		}
			break;
		case 3: { // Order-based: OX2
			// How many random indices to select
			int k = Search.r.nextInt(Parameters.numGenes) + 1;
			// Get the random positions
			ArrayList<Integer> positionList = new ArrayList<Integer>();
			for (int i = 0; i < k; i++) {
				int ran = Search.r.nextInt(Parameters.numGenes);
				if (!positionList.contains(ran))
					positionList.add(ran);
			}
			k = positionList.size();
			Collections.sort(positionList);
			// Collect genes from the random positions
			ArrayList<Integer> itemList1 = new ArrayList<Integer>();
			ArrayList<Integer> itemList2 = new ArrayList<Integer>();
			for (int i = 0; i < k; i++) {
				itemList1.add(parent1.chromo[positionList.get(i)]);
				itemList2.add(parent2.chromo[positionList.get(i)]);
			}

			// Generate offsprings
			int i1 = 0;
			int i2 = 0;
			for (int index = 0; index < Parameters.numGenes; index++) {
				if (!itemList2.contains(parent1.chromo[index])) {
					child1.chromo[index] = parent1.chromo[index];
				} else {
					child1.chromo[index] = itemList2.get(i2++);
				}

				if (!itemList1.contains(parent2.chromo[index])) {
					child2.chromo[index] = parent2.chromo[index];
				} else {
					child2.chromo[index] = itemList1.get(i1++);
				}
			}
		}
			break;
		case 4: { // Position-Based: POS
			// How many random indices to select
			int k = Search.r.nextInt(Parameters.numGenes) + 1;
			// Get the random positions
			ArrayList<Integer> positionList = new ArrayList<Integer>();
			for (int i = 0; i < k; i++) {
				int ran = Search.r.nextInt(Parameters.numGenes);
				if (!positionList.contains(ran))
					positionList.add(ran);
			}
			k = positionList.size();
			Collections.sort(positionList);
			// System.out.println(k + " random positions: " + positionList);
			// Collect genes from the random positions
			ArrayList<Integer> itemList1 = new ArrayList<Integer>();
			ArrayList<Integer> itemList2 = new ArrayList<Integer>();
			for (int i = 0; i < k; i++) {
				itemList1.add(parent1.chromo[positionList.get(i)]);
				itemList2.add(parent2.chromo[positionList.get(i)]);
			}
			ArrayList<Integer> remainingItemList1 = new ArrayList<Integer>();
			ArrayList<Integer> remainingItemList2 = new ArrayList<Integer>();
			for (int index = 0; index < Parameters.numGenes; index++) {
				if (!positionList.contains(index)) {
					remainingItemList1.add(parent1.chromo[index]);
					remainingItemList2.add(parent2.chromo[index]);
				}
			}

			// Generate offsprings
			int i1 = 0;
			int i2 = 0;
			for (int index = 0; index < Parameters.numGenes; index++) {
				if (positionList.contains(index)) {
					child1.chromo[index] = parent2.chromo[index];
					child2.chromo[index] = parent1.chromo[index];
				} else {
					child1.chromo[index] = remainingItemList1.get(i1++);
					child2.chromo[index] = remainingItemList2.get(i2++);
				}
			}
		}
			break;
		case 5: { // Cycle Crossover: CX
			// Inspired from
			// https://codereview.stackexchange.com/questions/226179/easiest-way-to-implement-cycle-crossover
			boolean swap = true; // To swap the source parent
			int count = 0; // Keep track of taken genes
			int position = 0; // Keep track of current position
			int[] p1_copy = new int[Parameters.numGenes];
			int[] p2_copy = new int[Parameters.numGenes];
			int[] c1_copy = new int[Parameters.numGenes];

			HashMap<Integer, Integer> index_map1 = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> index_map2 = new HashMap<Integer, Integer>();

			for (int index = 0; index < Parameters.numGenes; index++) {
				// child1.chromo[index] = -1; // Initialize all gene to -1
				// child2.chromo[index] = -1;
				c1_copy[index] = -1;
			}

			for (int index = 0; index < Parameters.numGenes; index++) {
				p1_copy[index] = parent1.chromo[index];
				index_map1.put(parent1.chromo[index], index);
				p2_copy[index] = parent2.chromo[index];
				index_map2.put(parent2.chromo[index], index);
			}

			while (true) {
				if (count >= Parameters.numGenes)
					break;

				// Get first index to start the cycle
				for (int index = 0; index < Parameters.numGenes; index++) {
					if (c1_copy[index] == -1) {
						position = index;
						break;
					}
				}
				// start cycle from parent1
				if (swap) {
					while (true) {
						c1_copy[position] = parent1.chromo[position];
						p1_copy[position] = -1;
						count++;
						position = index_map2.get(parent1.chromo[position]);
						if (p1_copy[position] == -1) {
							swap = false;
							break;
						}
					}
				}
				// start cycle from parent2
				else {
					while (true) {
						c1_copy[position] = parent2.chromo[position];
						p2_copy[position] = -1;
						count++;
						position = index_map1.get(parent2.chromo[position]);
						if (p2_copy[position] == -1) {
							swap = true;
							break;
						}
						p2_copy[position] = -1;
					}
				}
			}
			// Populate child1
			for (int index = 0; index < Parameters.numGenes; index++) {
				child1.chromo[index] = c1_copy[index];
			}
			// Populate child2
			for (int index = 0; index < Parameters.numGenes; index++) {
				if (child1.chromo[index] == parent1.chromo[index]) {
					child2.chromo[index] = parent2.chromo[index];
				} else {
					child2.chromo[index] = parent1.chromo[index];
				}
			}
			// Special case
			for (int index = 0; index < Parameters.numGenes; index++) {
				if (child1.chromo[index] == -1) {
					// if (p1List.get(index) == -1) { // the ith gene from p1 has been already transfered
					if(p1_copy[index] == -1){
						child1.chromo[index] = parent2.chromo[index];
					} else {
						child1.chromo[index] = parent1.chromo[index];
					}
				}
			}
		}
			break;
		case 6: { // Single Point (would need key rep)
			int xoverPoint1, len = parent1.chromo.length;
			xoverPoint1 = 1 + (int) (Search.r.nextDouble() * (Parameters.numGenes));

			// Create child chromosomes from parent genes
			for (int i = 0; i < xoverPoint1; i++) {
				child1.chromo[i] = parent1.chromo[i];
				child2.chromo[i] = parent2.chromo[i];
			}

			for (int i = xoverPoint1; i < len; i++) {
				child1.chromo[i] = parent2.chromo[i];
				child2.chromo[i] = parent1.chromo[i];
			}

		}
			break;
		case 7: { // Double Point (would need key rep)
			int xoverPoint1, xoverPoint2, len = parent1.chromo.length;
			xoverPoint1 = 1 + (int) (Search.r.nextDouble() * (Parameters.numGenes));
			xoverPoint2 = 1 + (int) (Search.r.nextDouble() * (Parameters.numGenes));

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

		}
			break;
		case 8: { // Keep intersections, fill rest w/ randoms
			// Iterate through and record the intersecting genes
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// If the same, record in children
				if (parent1.chromo[geneID] == parent2.chromo[geneID]) {
					child1.chromo[geneID] = parent1.chromo[geneID];
					child2.chromo[geneID] = parent2.chromo[geneID];
				} else { // Otherwise replace with random numbers in range
					child1.chromo[geneID] = Search.r.nextInt(Parameters.numGenes);
					child2.chromo[geneID] = Search.r.nextInt(Parameters.numGenes);
				}
			}
		}
			break;
		case 9: { // Keep intersections, fill rest w/ random nonduplicates
			// Tracks integers we used
			Set<Integer> usedInts1 = new HashSet<Integer>(Parameters.numGenes);
			Set<Integer> usedInts2 = new HashSet<Integer>(Parameters.numGenes);
			// Iterate through the genes
			for (int geneID = 0; geneID < Parameters.numGenes; geneID++) {
				// If the same, record in children
				if (parent1.chromo[geneID] == parent2.chromo[geneID]) {
					child1.chromo[geneID] = parent1.chromo[geneID];
					child2.chromo[geneID] = parent2.chromo[geneID];
					usedInts1.add(child1.chromo[geneID]);
					usedInts2.add(child2.chromo[geneID]);
				} else { // Otherwise find a number we haven't used and place
							// Child 1
					int newGeneVal = Search.r.nextInt(Parameters.numGenes);
					while (usedInts1.contains(newGeneVal) == true) {
						newGeneVal = Search.r.nextInt(Parameters.numGenes);
					}
					child1.chromo[geneID] = newGeneVal;
					usedInts1.add(child1.chromo[geneID]);
					// Child 2
					newGeneVal = Search.r.nextInt(Parameters.numGenes);
					while (usedInts2.contains(newGeneVal) == true) {
						newGeneVal = Search.r.nextInt(Parameters.numGenes);
					}
					child2.chromo[geneID] = newGeneVal;
					usedInts2.add(child2.chromo[geneID]);
				}
			}
		}
			break;
		}

		if (testCode) {
			System.out.println("Offsprings");
			printChromo(child1);
			printChromo(child2);
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
