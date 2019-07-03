package uk.robevans;

import java.awt.Point;

public class Game {

    private final String[][] cells;
    private Point mouseLocation;
    private final RandomPointGenerator pointGenerator;

    public Game(int width, int height, RandomPointGenerator pointGenerator) {
        if (height >1 && width >1) {
            this.cells = new String[height][width];
        } else {
            throw new RuntimeException("The game grid must be at least 2 rows by 2 columns");
        }
        this.pointGenerator = pointGenerator;
        this.mouseLocation = pointGenerator.getPlayerStartLocation(width, height);
    }

    public String[][] getGameGrid() {
        return cells;
    }

    public int getGameWidth() {
        return cells[0].length;
    }

    public int getGameHeight() {
        return cells.length;
    }

    public Point getMouseLocation() {
        return mouseLocation;
    }

    public SpanningTree createAMaze() {
        SpanningTree newMaze = new SpanningTree();
        return newMaze.build(cells);
    }
}
