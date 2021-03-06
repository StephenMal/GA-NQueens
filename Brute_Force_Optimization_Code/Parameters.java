/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Parameters
{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public static String expID;
	public static String problemType;

	public static String dataInputFileName;

	public static int numRuns;
	public static int generations;
	public static int popSize;

	public static int genCap;
	public static int fitCap;

	public static String minORmax;
	public static int selectType;
	public static int scaleType;

	public static int xoverType;
	public static double xoverRate;
	public static int mutationType;
	public static double mutationRate;

	public static long seed;
	public static int numGenes;

// New parameters
	public static int valueRepresentation;
	public static int fitnessFunctionType;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Parameters(String parmfilename) throws java.io.IOException{

		String readLine;
		BufferedReader parmInput = new BufferedReader(new FileReader (parmfilename));

		expID = parmInput.readLine().substring(30);
		problemType = parmInput.readLine().substring(30);

		dataInputFileName = parmInput.readLine().substring(30);

		numRuns = Integer.parseInt(parmInput.readLine().substring(30).trim());
		generations = Integer.parseInt(parmInput.readLine().substring(30).trim());
		popSize = Integer.parseInt(parmInput.readLine().substring(30).trim());

		selectType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		scaleType = Integer.parseInt(parmInput.readLine().substring(30).trim());

		xoverType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		xoverRate = Double.parseDouble(parmInput.readLine().substring(30).trim());
		mutationType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		mutationRate = Double.parseDouble(parmInput.readLine().substring(30).trim());

		seed = Long.parseLong(parmInput.readLine().substring(30).trim());
		numGenes = Integer.parseInt(parmInput.readLine().substring(30).trim());

		valueRepresentation = Integer.parseInt(parmInput.readLine().substring(30).trim());
		fitnessFunctionType = Integer.parseInt(parmInput.readLine().substring(30).trim());

		parmInput.close();

		// Find scale type
		if (fitnessFunctionType==1 || fitnessFunctionType==2){
			 minORmax = "min";
			 if (scaleType == 0){ // Scale
				 Parameters.scaleType = 1;
			 }
			 else{ // Rank
				 Parameters.scaleType = 3;
			 }
		 }
		else{
			 minORmax = "max";
			 if (scaleType == 1){ // Scale
				 Parameters.scaleType = 0;
			 }
			 else{ // Rank
				 Parameters.scaleType = 2;
			 }
		 }

	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/


/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	public static void outputParameters(FileWriter output) throws java.io.IOException{


		output.write("Experiment ID                :  " + expID + "\n");
		output.write("Problem Type                 :  " + problemType + "\n");

		output.write("Data Input File Name         :  " + dataInputFileName + "\n");

		output.write("Number of Runs               :  " + numRuns + "\n");
		output.write("Generations per Run          :  " + generations + "\n");
		output.write("Population Size              :  " + popSize + "\n");

		output.write("Selection Method             :  " + selectType + "\n");
		output.write("Fitness Scaling Type         :  " + scaleType + "\n");
		output.write("Min or Max Fitness           :  " + minORmax + "\n");

		output.write("Crossover Type               :  " + xoverType + "\n");
		output.write("Crossover Rate               :  " + xoverRate + "\n");
		output.write("Mutation Type                :  " + mutationType + "\n");
		output.write("Mutation Rate                :  " + mutationRate + "\n");

		output.write("Random Number Seed           :  " + seed + "\n");
		output.write("Number of Genes/Points       :  " + numGenes + "\n");

		output.write("Value Representation         :  " + valueRepresentation + "\n");
		output.write("Fitness Function Type        :  " + fitnessFunctionType + "\n");

		output.write("\n\n");

	}
}   // End of Parameters.java **************************************************
