# The Mouse Cheese Maze Game

I recently attended an interview at a large well known company, and they came up with a coding challenge
to build an algorithm to solve the problem of a mouse in a maze attempting to get some cheese. First thoughts were bredth/depth first search problem. Unfortunately that thought never made it out of my head as this was the most fun/novel coding challenge I've come across to date.

For one, I liked the simplicity of it. Having tried to come up with a good coding challenge myself for interviews, this one seemed to resonate. It was fun, there was technical complexity to it (in a computer sciencey way), and even if you're algorithm can't solve the puzzle, the game could be developed in a number of unusual ways -- one way paths, for instance. So as a coding challenge I think its the best I've come across to date (I've never said that about any coding challenge before). 

So, here it is! My attempt at a solution AND an entire Test Driven Development (TDD'd) - mostly - implementation of "the mouse in a maze trying to get some cheese" game.

Initial thoughts - can you TDD an algorithm? Yes and No. First you need to know the algorithm. Then you need to know the end state of that algorithm. I'm sure its possible to TDD it if you start with small graphs and build up - 2x2, 3x2, 2x3, 3x3... etc. but for anything significantly larger your algorithm has to work with space/time complexity that is efficient.

So, here's my initial thought process.

1. Start with a Game object
2. make it possible to configure a 2D array's size
3. add a mouse with a current position at a random location within the maze. The start location could be random or it could be 0, 0 (top left).
3. build a fully connected graph that connects the mouse to every cell of the 2D array. I'm using a bidirectionally created graph as this makes it easier to navigate the maze from the point you're currently at, and also opens up some interesting possibilities relating to the valid/permitted directions you could move the mouse
4. hide the cheese somewhere in the maze - there are some interesting game elements that could be used here - perhaps the cheese is disguised as/within something else, perhaps there are multiple cheeses, or obstacles in the way. The cheese should have a minimum Euclidian distance from the mouse 25% the height/width of the maze (perhaps pythagoras' theorem will be useful here for measuring the distance)
5. build an algorithm that ensures the mouse always finds the cheese and enable a cheat button so the human player can click the button and watch the mouse find the cheese.

Some gotchas:
* Graphs can allow cycles (loops), or be acyclic. I'm starting with an acyclic graph (a minimum spanning tree). I remember Djikstra's algorithm from my computer science days, but my initial research shows Prim's algorithm for creating Minimum Spanning Trees (MSTs) is a possible way for creating the MST. The end result is much the same.
* The maze should be different each time - for this I'm using randomized values for the edges/distances or weights (because I've studied AI) between the vertices of the MST I'm building.
* We will almost certainly have to back track when navigating the maze - we want to be as efficient as possible - A* algorithm would work well here

