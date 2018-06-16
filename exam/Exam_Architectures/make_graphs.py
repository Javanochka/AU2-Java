
# coding: utf-8

# In[1]:


import pandas as pd
import matplotlib.pyplot as plt
from matplotlib import rcParams
rcParams["figure.figsize"] = (10, 10)


def getData(filename):
    x = []
    y = []
    with open(filename, 'r') as f:
        for line in f.readlines()[6:]:
            x1, y1 = line.split()
            x.append(int(x1))
            y.append(int(y1))
    return (x, y)

server_type = ["THREAD_EACH", "THREAD_POOL", "NONBLOCKING"]
colors = {"THREAD_EACH": "red", "THREAD_POOL": "green", "NONBLOCKING": "blue"}
metrics = ["time_on_client", "time_processing", "time_sorting"]
params = ["clients", "elements", "delta"]

for metric in metrics:
    for i in range(3):
        param = params[i]
        plt.cla()
        lines = []
        xmax = 0
        ymax = 0
        
        for stype in server_type:
            x, y = getData("logs/" + stype + "_" + metric + "_" + str(i) + ".txt")
            line, = plt.plot(x, y, linestyle = '-', marker = 'o', color=colors[stype])
            lines.append(line)
            xmax = max(xmax, max(x))
            ymax = max(ymax, max(y))
        plt.legend(lines, server_type, loc='best')
        plt.xlim(-1, xmax + 1)
        plt.ylim(-1, ymax + 1)
        plt.xlabel(param)
        plt.ylabel(metric)
        plt.savefig("graphs/" + metric + "_" + param + ".png")


