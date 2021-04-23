
import os
from multiprocessing import Process
import time
import math

'''
Used for testing various generations
'''

# Wait time before suspending a GA
WAIT_TIME = 20
CHECK_FREQ = 0.01

# Generate params file
def genParamsFile(pop, select, scale, xover_type, xover_rate, mut_type, mut_rate, nQueens, rep_type, fit_fxn, fileNum):
    paramFile = open("NQueensTemp" + str(fileNum) + ".params","w")
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
    solutionFile.write("NQ-Size,Runtime until solved,Gens until solved, Pop Size,Select Type,Fit Scale Type,Xover Type,Xover Rate,Mut Type,Mut Rate,Rep Type,Fit Fxn, Fitness Score, Chromo,\n")
    badResults.write("NQ-Size,Runtime until solved,Gens until solved, Pop Size,Select Type,Fit Scale Type,Xover Type,Xover Rate,Mut Type,Mut Rate,Rep Type,Fit Fxn, Fitness Score, Chromo,\n")
    solutionFile.close()
    badResults.close()

def runSearch(pop, select, scale, xover_type, xover_rate, mut_type, mut_rate, nQueens, rep_type, fit_fxn, fileNum):
    genParamsFile(pop, select, scale, xover_type, xover_rate/100, mut_type, mut_rate/100, nQueens, rep_type, fit_fxn, fileNum)
    os.system("java Search NQueensTemp"+str(fileNum)+".params")

def main():
    # Generate list of populations to test
    popList = [10, 20, 50, 100, 500, 2000, 5000] #7
    selectionList = [1,2,3,4,5] #5
    scaleList = [1,2] #2
    xoverTypeList = [1,2,3,4,5,6,7,8,9] # 9
    xoverRateList = [1, 5, 10, 20, 50] #5
    mutTypeList = [2,3,4,5,6,8] #8
    mutRateList = [1, 5, 10, 20, 50] #5
    nQueensList = [4,5,6,7]
    repList = [1,2] #2
    fitFxnList = [1,2,3,4,5,6] #6

    testsDone = 0
    totalTests = len(popList) * len(selectionList) * len(scaleList) * len(xoverTypeList) * len(xoverRateList) * len(mutTypeList) * len(mutRateList) * len(nQueensList) * len(repList) * len(fitFxnList)
    print("Total tests to be performed:" + str(totalTests) + "\n")


    # Generate output files for every queen
    for queen in nQueensList:
        genOutputFiles(queen)

    for pop in popList:
        print("Pop: " + str(pop))
        for select in selectionList:
            print("selection: " + str(select))
            print("Completion: " + str(testsDone/totalTests) + "%")
            for scale in scaleList:
                print("Scale: " + str(scale))
                for xover_type in xoverTypeList:
                    print("xover: " + str(xover_type))
                    for xover_rate in xoverRateList:
                        for mut_type in mutTypeList:
                            print("mutation: " + str(mut_type))
                            for mut_rate in mutRateList:
                                for rep_type in repList:
                                    print("Representation: " + str(rep_type))
                                    for fit_fxn in fitFxnList:
                                        print("FitFxn: " + str(fit_fxn))

                                        # Create multi processes
                                        processList = []
                                        for queen in nQueensList
                                            fileNum = queen
                                            processList.append(Process(target=runSearch, name = "Search1", args=(pop, select, scale, xover_type, xover_rate/100, mut_type, mut_rate/100, nQueens, rep_type, fit_fxn, fileNum)))

                                        # Start searchs
                                        for i in range(len(nQueensList)):
                                            processList[i].start()

                                        # Wait WAIT_TIME
                                        for i in range(0,math.floor(WAIT_TIME/CHECK_FREQ)):
                                            allDone = True # Assumes all are done
                                            # If not done, set flag to false
                                            for j in range(len(nQueensList)):
                                                if processList[j].is_alive() is True:
                                                    allDone = False
                                            # If all done, break
                                            if allDone is True:
                                                print("Broke early")
                                                break;
                                            else:
                                                time.sleep(CHECK_FREQ)







if __name__ == '__main__':
    main()
