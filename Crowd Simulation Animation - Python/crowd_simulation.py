from vedo import *
import numpy as np
import matplotlib.pyplot as plt
import time

def F(d, R):
    # If 0 < d <= R then f = ln(R/d)
    d = np.where(d == 0, 0.001, d)  # Handle division by zero
    f = np.log(R / d)

    # If d > R then f(d) = 0
    f[d > R] = 0

    return f

def displaySampledGradient(X1, Y1, DX, DY, C, plotTitle, arrowColor):
    plt.figure()
    plt.imshow(C, origin='lower')
    plt.contour(C, linewidths=2)
    plt.quiver(X1, Y1, -DX, -DY, linewidth=2, color=arrowColor)
    plt.title(plotTitle)
    plt.xlabel('X')
    plt.ylabel('Y')
    plt.xticks(fontsize=18)
    plt.yticks(fontsize=18)
    plt.gca().set_aspect('equal', adjustable='box')
    plt.show()

def displaySurfPlot(C, plotTitle, Xmax, Ymax):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    X = np.arange(0, Xmax + 1)
    Y = np.arange(0, Ymax + 1)
    X, Y = np.meshgrid(X, Y)
    surf = ax.plot_surface(X, Y, C, cmap='viridis', edgecolor='none')
    ax.set_xlabel('x', fontsize=18)
    ax.set_ylabel('y', fontsize=18)
    ax.set_zlabel('C', fontsize=18)
    ax.set_xlim([0, Xmax])
    ax.set_ylim([0, Ymax])
    ax.set_zlim([np.min(C), np.max(C)])
    ax.view_init(-50, 30)
    plt.title(plotTitle, fontsize=18)
    plt.xticks(fontsize=18)
    plt.yticks(fontsize=18)
    #plt.gca().set_zticks(fontsize=18)
    fig.colorbar(surf, shrink=0.5, aspect=5)
    plt.show()


def board1():
    floor = Box(pos=(50, 50, 0),length=100,width=100,height=.5,size=(),c='blue',alpha=1.0)
    wall1 = Box(pos=(22.5, 50, 5),length=45,width=5,height=10,size=(),c='red',alpha=1.0)
    wall2 = Box(pos=(77.5, 50, 5),length=45,width=5,height=10,size=(),c='red',alpha=1.0)
    obstacle = Cylinder(r=5, height=10, pos = (50,30,5), c="red", alpha=1.0, axis=(0,0,1))

    # Combine all parts into a single object
    board = floor + wall1 + wall2 + obstacle

    #-------------- Calculate Cost of Goal ----------------
    # Create a grid to represent the spatial landscape
    X, Y = np.meshgrid(np.arange(0, 101), np.arange(0, 101))
    delta = 5

    # Create a grid with the goal location at every point
    g = np.array([50, 75])
    Gx = np.full_like(X, g[0])
    Gy = np.full_like(Y, g[1])

    # Attraction term (distance to goal)
    C1 = np.sqrt((X - Gx)**2 + (Y - Gy)**2)

    #----------------Calculate Cost of Obstacles-------------------
    # Obstacle position
    o = np.array([50,30])
    R = 20  # Obstacle radius

    Ox = np.full_like(X, o[0])
    Oy = np.full_like(Y, o[1])

    # Distance to the obstacle
    d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
    obstacleCost = F(d2, R)  # Calculate cost term Using the previously defined F function

    ## 0 - 45 ; 55 - 100
    for x in [0, 5, 10, 15, 20, 25, 30, 35, 40, 60, 65, 70, 75, 80, 85, 90, 95, 100]:
        # Obstacle position
        o = np.array([x,50])
        R = 10  # Obstacle radius

        Ox = np.full_like(X, o[0])
        Oy = np.full_like(Y, o[1])

        # Distance to the obstacle
        d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
        obstacleCost += F(d2, R)  # Calculate cost term Using the previously defined F function


    C = C1 + 6*obstacleCost
    # displaySurfPlot(C, 'Cost Function', 100, 100)
    DY, DX = np.gradient(C)
    X1, Y1 = np.meshgrid(np.arange(0, 101, delta), np.arange(0, 101, delta))
    displaySampledGradient(X1, Y1, DX[::delta, ::delta], DY[::delta, ::delta], C, 'Gradient (Cost Function)', 'w')
    return board, C

def board2():
    floor = Box(pos=(50, 50, 0),length=100,width=100,height=.5,size=(),c='blue',alpha=1.0)
    wall1 = Box(pos=(20, 50, 5),length=40,width=2.5,height=10,size=(),c='red',alpha=1.0)
    wall2 = Box(pos=(80, 50, 5),length=40,width=2.5,height=10,size=(),c='red',alpha=1.0)
    wall3 = Box(pos=(50, 30, 5),length=100,width=2.5,height=10,size=(),c='red',alpha=1.0)
    wall4 = Box(pos=(40, 73.75, 5),length=2.5,width=50,height=10,size=(),c='red',alpha=1.0)
    wall5 = Box(pos=(60, 73.75, 5),length=2.5,width=50,height=10,size=(),c='red',alpha=1.0)

    # Combine all parts into a single object
    board = floor + wall1 + wall2 + wall3 + wall4 + wall5

    #-------------- Calculate Cost of Goal ----------------
    # Create a grid to represent the spatial landscape
    X, Y = np.meshgrid(np.arange(0, 101), np.arange(0, 101))
    delta = 5

    # Create a grid with the goal location at every point
    g = np.array([10, 30])
    Gx = np.full_like(X, g[0])
    Gy = np.full_like(Y, g[1])

    # Attraction term (distance to goal)
    C1 = np.sqrt((X - Gx)**2 + (Y - Gy)**2)

    # Create a grid with the goal location at every point
    g = np.array([50, 30])
    Gx = np.full_like(X, g[0])
    Gy = np.full_like(Y, g[1])

    # Attraction term (distance to goal)
    C2 = np.sqrt((X - Gx)**2 + (Y - Gy)**2)

    #----------------Calculate Cost of Obstacles-------------------
    o = np.array([0,30])
    R = 10  # Obstacle radius

    Ox = np.full_like(X, o[0])
    Oy = np.full_like(Y, o[1])

    # Distance to the obstacle
    d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
    obstacleCost = F(d2, R)  # Calculate cost term Using the previously defined F function

    for x in [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100]:
        # Obstacle position
        o = np.array([x,30])
        Ox = np.full_like(X, o[0])
        Oy = np.full_like(Y, o[1])

        # Distance to the obstacle
        d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
        obstacleCost += F(d2, R)  # Calculate cost term Using the previously defined F function
    
    for x in [0, 5, 10, 15, 20, 25, 30, 35, 38, 62, 65, 70, 75, 80, 85, 90, 95, 100]:
        # Obstacle position
        o = np.array([x,50])
        Ox = np.full_like(X, o[0])
        Oy = np.full_like(Y, o[1])

        # Distance to the obstacle
        d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
        obstacleCost += F(d2, R)  # Calculate cost term Using the previously defined F 

        
    for y in [50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100]:
        # Obstacle position
        o = np.array([40,y])
        Ox = np.full_like(X, o[0])
        Oy = np.full_like(Y, o[1])

        # Distance to the obstacle
        d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
        obstacleCost += F(d2, R)  # Calculate cost term Using the previously defined F function

        o = np.array([60,y])
        R = 5  # Obstacle radius

        Ox = np.full_like(X, o[0])
        Oy = np.full_like(Y, o[1])

        # Distance to the obstacle
        d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
        obstacleCost += F(d2, R)  # Calculate cost term Using the previously defined F function


    C = C1 + 0.5*C2 + 5*obstacleCost
    displaySurfPlot(C, 'Cost Function', 100, 100)
    DY, DX = np.gradient(C)
    X1, Y1 = np.meshgrid(np.arange(0, 101, delta), np.arange(0, 101, delta))
    displaySampledGradient(X1, Y1, DX[::delta, ::delta], DY[::delta, ::delta], C, 'Gradient (Cost Function)', 'w')
    return board, C

def createPeople(coord):
    people = Sphere(r=2).pos(coord[0][0],coord[0][1],2).color("green").alpha(1)
    for i in range(1,coord.shape[0]):
        people += Sphere(r=2).pos(coord[i][0],coord[i][1],2).color("green").alpha(1)
    return people

def peopleCost(coord):
    X, Y = np.meshgrid(np.arange(0, 101), np.arange(0, 101))
    
    o = np.array([coord[0][0],coord[0][1]])
    R = 7 # Obstacle radius
    Ox = np.full_like(X, o[0])
    Oy = np.full_like(Y, o[1])
    d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
    cost = F(d2, R)  # Calculate cost term Using the previously defined F function
    for i in range(1, coord.shape[0]):
        # Obstacle position
        o = np.array([coord[i][0],coord[i][1]])
        Ox = np.full_like(X, o[0])
        Oy = np.full_like(Y, o[1])
        d2 = np.sqrt((X - Ox)**2 + (Y - Oy)**2)
        cost += F(d2, R)  # Calculate cost term Using the previously defined F function

    return cost


def movePeople(peopleCoordinates, stationaryCost):
    currentCoordinates = peopleCoordinates
    for i in range(0, peopleCoordinates.shape[0]):
        costP = peopleCost(currentCoordinates)
        totalCost = stationaryCost + 3*costP
        DY, DX = np.gradient(totalCost)

        currX = peopleCoordinates[i][0]
        currY = peopleCoordinates[i][1]

        # determine new location of person
        newX = currX - 1.5*DX[currY][currX]
        newY = currY - 1.5*DY[currY][currX]
        currentCoordinates[i] = [newX, newY]
    return currentCoordinates

def main():
    # declare the class instance
    plt = Plotter(bg='beige', bg2='lb', axes=10, offscreen=False, interactive=False)
    video = Video("C:/Users/alexi/Documents/School/Senior/Graphics/crowd_animation1.mp4", backend = 'imageio', fps = 5)
    board, stationaryCost = board1()
    peopleCoordinates = np.array([[60,15], [65,22], [70,12], [75,18], [80,14], [85,9]])
    people = createPeople(peopleCoordinates)

    plt.show([board, people])
    plt.render()
    video.add_frame()

    for i in range(45):
        peopleCoordinates = movePeople(peopleCoordinates, stationaryCost)
        people = createPeople(peopleCoordinates)
        plt.clear()
        plt.show([board, people])
        plt.render()
        video.add_frame()
        time.sleep(.5)
    
    video.close()
    plt.clear()

    video = Video("C:/Users/alexi/Documents/School/Senior/Graphics/crowd_animation2.mp4", backend = 'imageio', fps = 5)
    board, stationaryCost = board2()
    peopleCoordinates = np.array([[45, 70], [50,70], [55,70], [70, 45], [70,40], [70,35],
                                  [45, 80], [50,80], [55,80], [80, 45], [80,40], [80,35]])
    people = createPeople(peopleCoordinates)

    plt.show([board, people])
    plt.render()
    video.add_frame()

    for i in range(40):
        peopleCoordinates = movePeople(peopleCoordinates, stationaryCost)
        people = createPeople(peopleCoordinates)
        plt.clear()
        plt.show([board, people])
        plt.render()
        video.add_frame()
        time.sleep(.5)
    video.close()
    plt.interactive().show()
	



if __name__ == '__main__':
    main()

## C:/Users/alexi/anaconda3/python.exe C:/Users/alexi/Documents/School/Senior/Graphics/crowd_simulation.py
