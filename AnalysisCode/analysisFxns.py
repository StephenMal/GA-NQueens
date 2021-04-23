import os
import sys
import math

# Get the name of whatever the column is representing
def getColMeaning(colNum):
    switcher = {
        0: "NQ-Size",
        1: "Runtime",
        2: "Generations",
        3: "Population",
        4: "Selection",
        5: "Scaling",
        6: "Xover Type",
        7: "Xover Rate",
        8: "Mut Type",
        9: "Mut Rate",
        10: "Representation",
        11: "Fit Fxn",
        12: "Fitness",
        13: "Chromo",
    }
    return switcher.get(colNum)

# Create empty dictionaries with keys for each column
def getEmptyDictToDicts():
    dict = {}
    for i in range(0,14):
        dict[getColMeaning(i)] = {}}
    return dict

# Sort the data by smallest to highest runtime
def sortByRuntime(data):
    print(sorted(data,key=lambda x: x[1]))
    return

# Sort the data by smallest to highest number of generations
def sortByNumGens(data):
    print(sorted(data,key=lambda x: x[2]))
    return

# find frequencies of data
def findFreq(data):
    frequencyDict = {}
    frequencyDict = getEmptyDictToDicts()
    for parameterList in data:
        for col in range(0,len(parameterList)):
            if parameterList[col] not in frequencyDict[getColMeaning(col)].keys():
                frequencyDict[getColMeaning(col)][parameterList[col]] = 0
            frequencyDict[getColMeaning(col)][parameterList[col]] = frequencyDict[getColMeaning(col)][parameterList[col]] + 1
    return frequencyDict

# Find frequency of other parameters given one
def findFreq(data):
    frequencyDict = {}
    frequencyDict = getEmptyDictToDicts()
    for i in frequencyDict.keys():
        frequencyDict[i] = getEmptyDictToDicts()
        frequencyDict[i]


# get top x% of solution runs by runtime
def getTopGAsByRuntime(data, percent):
    sorted_data = sortByRuntime(data)
    return sorted_data[0:Math.floor(len(sorted_data)*(percent/100))]

# get top x% of solution runs by number of Generations until solution
def getTopGAsByRuntime(data, percent):
    sorted_data = sortByNumGens(data)
    return sorted_data[0:Math.floor(len(sorted_data)*(percent/100))]
