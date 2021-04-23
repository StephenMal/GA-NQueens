from geneticalgorithm import geneticalgorithm as ga
import numpy as np
import os
from multiprocessing import Process
import time
import math
import sys

'''
WAIT TIME
'''
# Base Wait Time
BASE_TIME = 30
# Population Multiplier (Adds population*Pop_MUL to the wait time)
POP_MUL = 0.01
# N-Queens Multiplier (Adds Queen# * Queen_Mul to the BASE_TIME)
QUEEN_MUL = 1
# Calculates termination time
def calcWaitTime(args, queen):
    return math.floor(BASE_TIME + POP_MUL*args['pop'] + QUEEN_MUL*queen)

'''
File Reading
'''
# Generate params file
def genParamsFile(pop, select, scale, xover_type, xover_rate, mut_type, mut_rate, nQueens, rep_type, fit_fxn):
    paramFile = open("generatedParams/temp.params","w")
    paramFile.write("Experiment ID                :nqueens\n")
    paramFile.write("Problem Type                 :NQ\n")
    paramFile.write("Data Input File Name         :NA\n")
    paramFile.write("Number of Runs               :1\n")
    paramFile.write("Generations per Run          :1000\n")
    paramFile.write("Population Size              :" + str(pop) + "\n")
    paramFile.write("Selection Method (1)         :" + str(select) + "\n")
    paramFile.write("Fitness Scaling Type (2)     :" + str(scale) + "\n")
    paramFile.write("Crossover Type (3)           :" + str(xover_type) + "\n")
    paramFile.write("Crossover Rate (4)           :" + str(xover_rate) + "\n")
    paramFile.write("Mutation Type (5)            :" + str(mut_type) + "\n")
    paramFile.write("Mutation Rate (6)            :" + str(mut_rate) + "\n")
    paramFile.write("Random Number Seed           :75982\n")
    paramFile.write("Number of Genes(# queens)(7) :" + str(nQueens) + "\n")
    paramFile.write("Gene Value Rep  (8)          :" + str(rep_type) + "\n")
    paramFile.write("Fitness Function  (9)        :" + str(fit_fxn) + "\n")
    paramFile.close();

'''
Run Fxn
'''
def runSearch(arg, nQueens):
    genParamsFile(arg['pop'], arg['select'], arg['scale'], arg['xover_type'], arg['xover_rate']/100, arg['mut_type'], arg['mut_rate']/100, int(sys.argv[1]), arg['rep_type'], arg['fit_fxn'])
    os.system("java Search generatedParams/temp.params")

'''
Fitness Functions
'''
cachedFitnesses = {}
# Fitness function for minimizing generations
def minRuntime(X):
    print(X)
    # Return cached value if exists
    if str(X) in cachedFitnesses.keys():
        return cachedFitnesses[str(X)]

    # Turn chromosome into arguments
    arguments = {}
    arguments['pop'] = int(X[0])*2
    arguments['select'] = int(X[1])
    arguments['scale'] = int(X[2])
    arguments['xover_type'] = int(X[3])
    arguments['xover_rate'] = int(X[4])
    arguments['mut_type'] = int(X[5])
    arguments['mut_rate'] = int(X[6])
    arguments['rep_type'] = int(X[5])
    arguments['fit_fxn'] = int(X[6])

    # Set up value trackers
    fitness = 0

    for run in range(0,10):

        # Clear results file
        f = open("results.csv","w")
        f.close()

        # set up a search
        queenSearch = Process(target=runSearch, name = "QueenSearch", args=(arguments,int(sys.argv[1])))
        queenSearch.start()

        # Calculate time till termination
        termTime = calcWaitTime(arguments,int(sys.argv[1]))

        # Continue checking if it is still alive until it either finishes or
        # it hits its ending time then return with the termination time
        print(queenSearch.is_alive())
        time.sleep(1)
        while(queenSearch.is_alive()):
            if (time.time() > termTime):
                queenSearch.terminate()
                fitness = fitness + calcWaitTime(arguments,int(sys.argv[1]))
                print("Terminating queen")

        # Otherwise, the queen search must have completed so read in results
        f = open("results.csv", "r")
        data = f.readline().split(",")
        f.close()
        if data[0] == '':
            return calcWaitTime(arguments,queenNum)

        print(data)
        fitness = fitness + int(float(data[1]))

    # Cache the final fitness and return it
    cachedFitnesses[str(X)] = fitness
    return fitness

# Fitness function for minimizing runtime
def minGenTime():
    # Return cached value if exists
    if chromo in cachedFitnesses[str(chromo)]:
        return cachedFitnesses[str(chromo)]


# Runs the meta GA
def main():
    varBound = np.array([[0,500],[1,5],[0,1],[2,9],[0,100],[1,7],[0,100],[1,2],[0,6]])

    algorithm_param = {'max_num_iteration': None,\
                       'population_size':250,\
                       'mutation_probability':0.1,\
                       'elit_ratio': 0.01,\
                       'crossover_probability': 0.5,\
                       'parents_portion': 0.3,\
                       'crossover_type':'two_point',\
                       'max_iteration_without_improv':None,\
                       'func_timeout': 10000000}


    model = ga(function=minRuntime,dimension=9,variable_type='int',variable_boundaries=varBound, algorithm_parameters=algorithm_param)

    model.run()

if __name__ == '__main__':
    main()
