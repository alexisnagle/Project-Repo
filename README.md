# Project Repo
This repository contains a collection of my projects from throughout college. The projects in a variety of languages including MASM Assembly, Ada, Haskell, Python, and Java; with Python and Java being the main languages used. 

## Java
### Calander
In this program, I was tasked with adding, removing, and returning events on a calander. To accomplish this I used a skip list map which allowed from me to easily locate the location to add, remove, or return events at a certain time. 

### Doctor Patient Queue
In this program, I was tasked with creating a priority queue to assign docotrs to patients in a hospital. I determined the priority of the patients based upon the patients Emergency Severity Index (ESI). The patient with the highest ESI would be place at the top of the priority queue and would be the next to be treated whe a doctor becomes avaliable. 

### Hurricane Data
In this program, I was given a heirarchy of hurricane data and then was prompted to retrieve hurricane based upon their category, state, etc. To do so, I read in the heirarchy and stored it into a tree. Then read in the prompt and traversed the tree as needed to find the correct category, state, etc listed within the tree and returned the leaf nodes of that current node.

### Pacman Simulator
In this program, I was tasked with crating a Pacman game. Ths game would take user input to move pacman and used Breadth First Search to move each ghost using the shortest path between them and Pacman. To achieve this I used a graph to track the location of each ghost and pacman. The graph is what i then used to calculate the shortest path using BFS.

### Password Decryption
In this program, I was tasked with creating a brute force recurrsive algorith to decrypt passwords given the encryptor and the max length,n. To do so I recursively generated passwords of length 1 to n and encrypted them so that I could then compare them to the given encrypted password.

### Sudoku Solver
In this program, my colleague and I were tasked with making a sudoku solver, only to solve based on the rows and columns but not the nonets. To complete this task we used a backitracking algorithm along with constraint usage. To achieve the backtracking we used a tree with nodes, each containg the current board, the current position on the board, and a list of values for that position. We would observe each opening as approached left to right and top to bottom. When reaching a blank space, we would create a lsit of any possible value that would be valid to put in the space and we would insert the first square. We would continue this by creating children nodes by copying the gameboard and moving to the next blank space. We would then traverse up the tree anytime a blank space had no valid valuse to be inputted. 

## Python
### Calculator
In this program I was tasked with creating a calculator with a UI. To implement the calulation of expressions I utilized a stack to be able to push and pop operands to use when an operator is found. I the utilized PyQt5 to create the user interface.

### Doctor UI
In this program I was given a database of doctors and was tasked with displaying the doctors in a user interface. First I processed the database by spliting each row of data to obtain the wanted information and storing it to an array. I then used PyQt5 to create the user interface which allows users to click through the list of doctors. 

### Recipe UI
In this program I was given a JSON file containing recipies and was tasked with creating a user interface to display the recipies. First I processed the JSON file and stored the needed information for each recipe into a list. I then created a user interface to display the first 16 recipies in the list. The user interface also has an option for users to search the recipies and that will display the first 16 recipies that match the description.

## Ada
### Tower Connection
In this program I was tasked with inputting a list of one way tower connection and queries and outputting whether or not the queries were connected. To do so I implemented a singly linked list that held tower nodes, containg a name and a list of their connections. Each time a connection was read in the program searched for the tower with the specified name, if one didnt exist it then made a new node, and it would then list the connection under the list of connections. When a query was made it would once again search for the "from" node, if not found it automatically states false, then would look within that nodes connection to see if the "to" node was listed.

## Haskell 
### Predicate Logic Calculator
In this program, my colleague and I were tasked with converting a Postfix predicate logic statement into infix then into conjunctive normal form to then determine the tautology of the expression. To do so, we simulated a stack using a list and used the stack to create a tree that expressed the predicate logic statement. From here, we recursively went through the tree and used pattern matching to remove implications, xors, nands, and equivalances. We then went throught a similar pattern matching process to distribute any and all negations and to distribute the or's over the and's to reach CNF. After converting to CNF, we recursively went through the tree to determine its tautology, meaning that the expression will always evaluate to true. We did this by checking to make sure each side of the and equations contained a variable and its negation. 

## MASM Assembly
### Stacker Game
In this program, my colleague and I wanted to recreate the classic Stacker arcade game in assembly for a class competition. To do so, we stored various values in memory such as current and previous x positions of the blocks and the x positions of the edge of the gameboard. Using these values we calculated the position of where to print the blocks on the screen. In addition to printing on the screen we used user input, the spacebar being pressed, to trigger the game to stop the blocks. We then used the x values listed above to calculate what boxes would get dropped off and which ones will stay. In addition to the game features, we utilized reading and writing to files to include the sidebar on the right and a leaderboard on the left. 
