package uk.robevans;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SpanningTree {

    private final List<Point> processedNodes;
    private final List<Point> nodesToProcess;
    private final Map<Point, Map<Direction, Integer>> bidirectionalEdgeMap;
    private final Map<Point, List<Point>> mazePaths;
    private final Random random;


    public SpanningTree() {
        this.processedNodes = new ArrayList<>();
        this.nodesToProcess = new ArrayList<>();
        this.bidirectionalEdgeMap = new HashMap<>();
        this.mazePaths = new HashMap<>();
        random = new Random();
    }

    private void init(String[][] cells) {
        resetState();

        addAllNodesToProcessingList(cells);
//        initEdgeMap(cells);
    }

    public void resetState() {
        processedNodes.clear();
        nodesToProcess.clear();
        bidirectionalEdgeMap.clear();
        mazePaths.clear();
    }

    private void addAllNodesToProcessingList(String[][] cells) {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[0].length; col++) {
                nodesToProcess.add(new Point(row, col));
            }
        }
    }

    public SpanningTree build(String[][] cells) {
        init(cells);

        // starting point does not matter here - we will always get the minimal graph regardless of where we start
        // We'll start by going in reverse order so we are always looking at the tail of the array...
//        initEdgeMap(cells);


        return this;
    }

    private boolean isEdgePoint(Point point, String[][] cells) {
        return point.x == 0
                || point.y == 0
                || isLastCol(point, cells)
                || point.y == cells.length - 1;
    }

    /**
     * As part of a minimal spanning tree, we apply random weights/distances to the
     * edges of our graph so that we can choose the minimal ones to create a new maze each time.
     * In this way we are guaranteed to have a path from the mouse to the cheese each and every time
     * since every node will end up connected via some path to every other node.
     * Without randomized weights we could just select a random edge connecting our node set
     * to the other nodes until all nodes were connected.
     */
    private void initEdgeMap(String[][] cells) {
        // bidirectionalEdgeMap fills from top left, down to the right
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[0].length; col++) {
                Point point = new Point(row, col);
                if (isTopLeftCorner(point)) {
                    addTopLeftCornerEdgesToEdgeMap(point);

                } else if (isTopRightCorner(point, cells)) {
                    addTopRightCornerEdgesToEdgeMap(point);

                } else if (isTopEdgeOnly(point, cells)) {
                    addTopEdges(point);

                } else if (isBottomLeftCorner(point, cells)) {
                    addBottomLeftCornerEdges(point);

                } else if (isBottomRightCorner(point, cells)) {

                } else if (isLeftEdgeOnly(point, cells)) {
                    addLeftEdges(point);
                } else if (isRightEdgeOnly(point, cells)) {

                } else if (isBottomEdgeOnly(point, cells)) {

                } else { // somewhere in the middle - not an edge/corner

                }
            }
        }
    }


    public boolean isRightEdgeOnly(Point point, String[][] cells) {
        return point.x == (cells[0].length - 1) && point.y != 0 && point.y != (cells.length - 1);
    }

    public boolean isLeftEdgeOnly(Point point, String[][] cells) {
        return point.x == 0 && point.y != 0 && point.y != (cells.length - 1);
    }

    public boolean isBottomRightCorner(Point point, String[][] cells) {
        return point.x == (cells[0].length - 1) && point.y == (cells.length - 1);
    }

    public boolean isBottomLeftCorner(Point point, String[][] cells) {
        return point.x == 0 && point.y == cells.length - 1;
    }

    public Point offsetPoint(Point point, Direction direction) {
        return new Point(point.x + direction.xOffset, point.y + direction.yOffset);
    }

    private boolean isTopEdgeOnly(Point point, String[][] cells) {
        return point.y == 0 && point.x != 0 && !isLastCol(point, cells);
    }

    public boolean isLastCol(Point point, String[][] cells) {
        return point.x == cells[0].length - 1;
    }

    public boolean isTopRightCorner(Point point, String[][] cells) {
        return point.y == 0 && isLastCol(point, cells);
    }

    public boolean isTopLeftCorner(Point point) {
        return point.x == 0 && point.y == 0;
    }

    public void addTopLeftCornerEdgesToEdgeMap(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        permittedWeightedDirections.put(Direction.DOWN, random.nextInt());
        permittedWeightedDirections.put(Direction.RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addTopEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeToLeft = offsetPoint(point, Direction.LEFT);
        permittedWeightedDirections.put(Direction.DOWN, random.nextInt());
        permittedWeightedDirections.put(Direction.LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(Direction.RIGHT));
        permittedWeightedDirections.put(Direction.RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addTopRightCornerEdgesToEdgeMap(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeToLeft = offsetPoint(point, Direction.LEFT);
        permittedWeightedDirections.put(Direction.DOWN, random.nextInt());
        permittedWeightedDirections.put(Direction.LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(Direction.RIGHT));

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addBottomLeftCornerEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeAbove = offsetPoint(point, Direction.UP);
        permittedWeightedDirections.put(Direction.UP, bidirectionalEdgeMap.get(nodeAbove).get(Direction.DOWN));
        permittedWeightedDirections.put(Direction.RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public List<Point> getAllTreeNodes() {
        return nodesToProcess;
    }

    public List<Point> getProcessedPoints() {
        return processedNodes;
    }

    public Map<Point, Map<Direction, Integer>> getBidirectionalEdgeMap() {
        return bidirectionalEdgeMap;
    }

    public List<Point> getPointsToProcess() {
        return nodesToProcess;
    }

    public void addLeftEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeAbove = offsetPoint(point, Direction.UP);
        permittedWeightedDirections.put(Direction.UP, bidirectionalEdgeMap.get(nodeAbove).get(Direction.DOWN));
        permittedWeightedDirections.put(Direction.DOWN, random.nextInt());
        permittedWeightedDirections.put(Direction.RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public boolean isBottomEdgeOnly(Point point, String[][] cells) {
        return point.x != 0 && point.x != (cells[0].length - 1) && point.y == (cells.length - 1);
    }

    public void addCentrePointEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeAbove = offsetPoint(point, Direction.UP);
        Point nodeToLeft = offsetPoint(point, Direction.LEFT);
        permittedWeightedDirections.put(Direction.UP, bidirectionalEdgeMap.get(nodeAbove).get(Direction.DOWN));
        permittedWeightedDirections.put(Direction.DOWN, random.nextInt());
        permittedWeightedDirections.put(Direction.LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(Direction.RIGHT));
        permittedWeightedDirections.put(Direction.RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addRightEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeAbove = offsetPoint(point, Direction.UP);
        Point nodeToLeft = offsetPoint(point, Direction.LEFT);
        permittedWeightedDirections.put(Direction.UP, bidirectionalEdgeMap.get(nodeAbove).get(Direction.DOWN));
        permittedWeightedDirections.put(Direction.DOWN, random.nextInt());
        permittedWeightedDirections.put(Direction.LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(Direction.RIGHT));

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addBottomEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeAbove = offsetPoint(point, Direction.UP);
        Point nodeToLeft = offsetPoint(point, Direction.LEFT);
        permittedWeightedDirections.put(Direction.UP, bidirectionalEdgeMap.get(nodeAbove).get(Direction.DOWN));
        permittedWeightedDirections.put(Direction.LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(Direction.RIGHT));
        permittedWeightedDirections.put(Direction.RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addBottomRightCornerEdges(Point point) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();
        Point nodeAbove = offsetPoint(point, Direction.UP);
        Point nodeToLeft = offsetPoint(point, Direction.LEFT);
        permittedWeightedDirections.put(Direction.UP, bidirectionalEdgeMap.get(nodeAbove).get(Direction.DOWN));
        permittedWeightedDirections.put(Direction.LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(Direction.RIGHT));

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }
}
