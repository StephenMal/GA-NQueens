
import os
from multiprocessing import Process
import time
import math

'''
Used for testing various generations
'''

# Base Wait Time
BASE_TIME = 30
# Population Multiplier (Adds population*Pop_MUL to the wait time)
POP_MUL = 0.01
# N-Queens Multiplier (Adds Queen# * Queen_Mul to the BASE_TIME)
QUEEN_MUL = 1

# Generate params file
def genParamsFile(pop, select, scale, xover_type, xover_rate, mut_type, mut_rate, nQueens, rep_type, fit_fxn):
    paramFile = open("tempParams/NQueensTemp" + str(nQueens) + ".params","w")
    paramFile.write("Experiment ID                :nqueens\n")
    paramFile.write("Problem Type                 :NQ\n")
    paramFile.write("Data Input File Name         :NA\n")
    paramFile.write("Number of Runs               :5\n")
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

def genOutputFiles(nQueens):
    solutionFile = open("results/solutions" + str(nQueens) + ".csv","w")
    badResults = open("results/badResults" + str(nQueens) + ".csv","w")
    probParams = open("results/badParams.csv","w")
    solutionFile.write("NQ-Size,Runtime until solved,Gens until solved, Pop Size,Select Type,Fit Scale Type,Xover Type,Xover Rate,Mut Type,Mut Rate,Rep Type,Fit Fxn, Fitness Score, Chromo,\n")
    badResults.write("NQ-Size,Runtime until solved,Gens until solved, Pop Size,Select Type,Fit Scale Type,Xover Type,Xover Rate,Mut Type,Mut Rate,Rep Type,Fit Fxn, Fitness Score, Chromo,\n")
    probParams.close()
    solutionFile.close()
    badResults.close()

def calcWaitTime(args, queen):
    return math.floor(BASE_TIME + POP_MUL*args['pop'] + QUEEN_MUL*queen)

def runSearch(arg, nQueens):
    genParamsFile(arg['pop'], arg['select'], arg['scale'], arg['xover_type'], arg['xover_rate']/100, arg['mut_type'], arg['mut_rate']/100, nQueens, arg['rep_type'], arg['fit_fxn'])
    os.system("java Search tempParams/NQueensTemp"+str(nQueens)+".params")

def recordProbParams(parameters, queen):
    probParams = open("results/badParams.csv","a")
    probParams.write("Queens: " + str(queen) + "parameters: \n")
    probParams.write(str(parameters))
    probParams.write('\n')
    probParams.close()

def main():
    # Generate list of populations to test
    popList = [10, 20, 50, 100, 500] #7
    selectionList = [1,2,3,4,5] #5
    scaleList = [0,1] #2
    xoverTypeList_reg = [2,3,4,9] # 9
    xoverTypeList_key = [6,7,8]
    xoverRateList = [1, 5, 10, 20, 50] #5
    mutTypeList_reg = [2,3,4,5]
    mutTypeList_key = [6,7]
    mutRateList = [1, 5, 10, 20, 50] #5
    nQueensList = [8,9,10,11,12]
    repList = [1,2] #2
    fitFxnList = [1,2,3,4,5] #6

    testsDone = 0
    totalTests = len(popList) * len(selectionList) * len(scaleList) * (len(xoverTypeList_reg)+len(xoverTypeList_key)) * len(xoverRateList) * (len(mutTypeList_reg)+len(mutTypeList_key)) * len(mutRateList) * len(repList) * len(fitFxnList)
    print("Total tests to be performed:" + str(totalTests) + "\n")


    # Generate output files for every queen & set placement index
    print("Generating fresh output files for queens")
    qArgIndex = {} # Stores current placement of the argument index
    for queen in nQueensList:
        genOutputFiles(queen)
        qArgIndex[queen] = 0

    # Generate all possible arguments
    print("Generating all possible arguments")
    arguments = []
    for select in selectionList:
        for scale in scaleList:
            for fit_fxn in fitFxnList:
                for mut_rate in mutRateList:
                    for xover_rate in xoverRateList:
                        for pop in popList:
                            # Goes through xovers and muts for col/row rep
                            for xover_type in xoverTypeList_reg:
                                for mut_type in mutTypeList_reg:
                                    rep_type = 1
                                    arg = {}
                                    arg['pop'] = pop
                                    arg['select'] = select
                                    arg['scale'] = scale
                                    arg['xover_type'] = xover_type
                                    arg['xover_rate'] = xover_rate
                                    arg['mut_type'] = mut_type
                                    arg['mut_rate'] = mut_rate
                                    arg['rep_type'] = rep_type
                                    arg['fit_fxn'] = fit_fxn
                                    arguments.append(arg)
                            # Goes through xovers and muts for key rep
                            for xover_type in xoverTypeList_key:
                                for mut_type in mutTypeList_key:
                                    rep_type = 2
                                    arg = {}
                                    arg['pop'] = pop
                                    arg['select'] = select
                                    arg['scale'] = scale
                                    arg['xover_type'] = xover_type
                                    arg['xover_rate'] = xover_rate
                                    arg['mut_type'] = mut_type
                                    arg['mut_rate'] = mut_rate
                                    arg['rep_type'] = rep_type
                                    arg['fit_fxn'] = fit_fxn
                                    arguments.append(arg)


    qProc = {} # Stores process for a certain queen
    qTime = {} # Stores current time for queen process
    queenComplete = {} # Check queen completeness

    # Create first processes and start them
    print("Starting Queens")
    for queen in nQueensList:
        qProc[queen] = Process(target=runSearch, name = "Queen"+str(queen), args=(arguments[qArgIndex[queen]],queen))
        qProc[queen].start()
        qTime[queen] = time.time() + calcWaitTime(arguments[qArgIndex[queen]],queen)
        qArgIndex[queen] = qArgIndex[queen] + 1
        queenComplete[queen] = False



    notComplete = True
    while(notComplete):
        # Iterate through queen processes
        for queen in nQueensList:
            # Check if queen is already completed
            if (queenComplete[queen] is True):
                notCompleted = False # Set all queens equal to done
                continue
            notCompleted = True # Set to true, means that this queen isn't complete
            # If queen is alive, check if it has hit its time limit
            if qProc[queen].is_alive():
                if (qTime[queen]) < time.time():
                    qProc[queen].terminate()
                    print("Queen #" + str(queen) + " has been terminated")
                    recordProbParams(arguments[qArgIndex[queen]], queen)
            else:
                print("New Queen #" + str(queen) + " Progress: " + str(qArgIndex[queen]/len(arguments)*100) + "%")
                #print(str(arguments[qArgIndex[queen]]))
                qProc[queen] = Process(target=runSearch, name = "Queen"+str(queen), args=(arguments[qArgIndex[queen]],queen))
                qProc[queen].start()
                qTime[queen] = time.time() + calcWaitTime(arguments[qArgIndex[queen]],queen)
                qArgIndex[queen] = qArgIndex[queen] + 1
                if qArgIndex == len(arguments):
                    queenComplete[queen] = True




if __name__ == '__main__':
    main()
