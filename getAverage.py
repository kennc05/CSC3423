import sys
import numpy as np
import matplotlib.pyplot as plt

#storing the values of each type of fitness
valueListsBest = {}
valueListsAvg = {}
valueListsWorse = {}

#storing the lowest values of each fitness

lowestBestFitness = {}
lowestAvgFitness = {}
lowestWorseFitness = {}


filelist = []



#137x28 for output screenshots


#currentScenairo = "GA Scenario 1/"
currentScenario = "GA Scenario 2/"

##currentParamChanged = "numsurvivors"
##currentParamChanged = "tournamentsize"
##currentParamChanged = "probmutationgaussian"
currentParamChanged = "probmutationnormal"
##currentParamChanged = "probsinglepointcrossover"
##currentParamChanged = "probmeanalterer"


#For pop size iterations optimisation 
##currentDirectory = "GA Scenario 2/popsizenumiters"
##popsize = 1
##for i in range(0,5):
##    filelist.append(currentDirectory+"/%s.txt" %popsize)
##    popsize=popsize*10


#Tournament Size

##for i in range(1,11):
##
##    #for tournament size& numsurvivors only
##    j = i*2
##    filelist.append(currentScenario+currentParamChanged+"/%s.txt" %j)
##
##    #for popsize
##    #filelist.append(currentScenario+currentParamChanged+"/%s.txt" %i)


#Probabilities
for i in range(1,11):
    if (i == 10):
        filelist.append(currentScenario+currentParamChanged+"/0.10.txt")
        break
        
    filelist.append(currentScenario+currentParamChanged+"/0.0%s.txt" %i)


#Individual 
#filelist.append("GA Scenario 2/optimisedparams/2.txt")

fig, (ax1, ax2, ax3) = plt.subplots(1,3)
ax1.set_title('Best fitness')
ax2.set_title('Mean fitness')
ax3.set_title('Worse fitness')



for file in filelist:
    currentfile = open(file, "r")
    for line in currentfile:
        values = [float(i) for i in line.split(" ")]

        if not values[0] in valueListsBest:
                valueListsBest[values[0]]=[]

        if not values[0] in valueListsAvg:
                valueListsAvg[values[0]]=[]

        if not values[0] in valueListsWorse:
                valueListsWorse[values[0]]=[]
                
        valueListsBest[values[0]].append(values[1])
        valueListsAvg[values[0]].append(values[2])
        valueListsWorse[values[0]].append(values[3])


    

    #Sort out the averages

    for key in sorted(valueListsBest.keys()):
        valueListsBest[key] = np.mean(valueListsBest[key])
        valueListsAvg[key] = np.mean(valueListsAvg[key])
        valueListsWorse[key] = np.mean(valueListsWorse[key])


    #Get the lowest fitness for each type in that configuration after averaging the 30 iterations

    
    lowestBestFitness[file]= [valueListsBest[min(valueListsBest.keys(), key =(lambda k: valueListsBest[k]))]]
    lowestAvgFitness[file]= [valueListsAvg[min(valueListsBest.keys(), key = (lambda k: valueListsBest[k]))]]
    lowestWorseFitness[file] = [valueListsWorse[min(valueListsBest.keys(), key = (lambda k: valueListsBest[k]))]]

    #plot it -4 removes .txt from the legend
    #Where generations is on the x axis
    
    lists = sorted(valueListsBest.items())
    x, y = zip(*lists) 
    ax1.plot(x,y, label=file[:-4])
    

    lists = sorted(valueListsAvg.items())
    x, y = zip(*lists) 
    ax2.plot(x,y, label=file[:-4])

    lists = sorted(valueListsWorse.items())
    x, y = zip(*lists) 
    ax3.plot(x,y, label=file[:-4])
    

    #clear out for the next file
    valueListsBest = {}
    valueListsAvg = {}
    valueListsWorse = {}


#Print out the lowest average fitness from all generations for each config

print ("{:<40} {:<30} {:<30} {:<30}".format('Config','Best','Avg','Worse'))




for key in lowestBestFitness.keys():
    print ("{:<40} {:<30} {:<30} {:<30}".format(key, lowestBestFitness[key][0], lowestAvgFitness[key][0], lowestWorseFitness[key][0]))

plt.legend(loc = 'upper right')
ax1.set_xlabel('Number of iterations (Generations)')
ax1.set_ylabel('Average best fitness')
ax2.set_xlabel('Number of iterations (Generations)')
ax2.set_ylabel('Average mean fitness')
ax3.set_xlabel('Number of iterations (Generations)')
ax3.set_ylabel('Average worse fitness')
plt.show()

