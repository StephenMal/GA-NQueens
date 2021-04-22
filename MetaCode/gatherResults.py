
import os

'''
Used for testing various generations
'''

# Generate params file
def genParamsFile(pop, select, scale, xover_type, xover_rate, mut_type, mut_rate, nQueens, rep_type, fit_fxn):
    paramFile = open("NQueensTemp.params","w")
    paramFile.write("Experiment ID                :nqueens\n")
    paramFile.write("Problem Type                 :NQ\n")
    paramFile.write("Data Input File Name         :NA\n")
    paramFile.write("Number of Runs               :10\n")
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


# Generate list of populations to test
popList = list(range(10,100,10)) + list(range(100,1000,100)) + list(range(1000,5000,250))
selectionList = list(range(1,6))
scaleList = list(range(0,2))
xoverTypeList = list(range(1,10))
xoverRateList = list(range(0,100,2))
mutTypeList = list(range(0,9))
mutRateList = list(range(0,100,2))
nQueensList = list(range(4,10))
repList = list(range(1,3))
fitFxnList = list(range(1,7))

for nQueens in nQueensList:
    print("nQueens: " + str(nQueens))
    for pop in popList:
        for select in selectionList:
            for scale in scaleList:
                for xover_type in xoverTypeList:
                    for xover_rate in xoverRateList:
                        for mut_type in mutTypeList:
                            for mut_rate in mutRateList:
                                for rep_type in repList:
                                    for fit_fxn in fitFxnList:
                                        genParamsFile(pop, select, scale, xover_type, xover_rate/100, mut_type, mut_rate/100, nQueens, rep_type, fit_fxn)
                                        os.system("java Search NQueensTemp.params")
