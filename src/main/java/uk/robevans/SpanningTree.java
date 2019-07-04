package uk.robevans;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static uk.robevans.Direction.*;
import static uk.robevans.Direction.UP;

public class SpanningTree {

    private final List<Point> processedNodes;
    private final List<Point> nodesToProcess;
    private final List<Point> visitedNodes;
    private final List<Point> unvisitedNeighbouringNodes;
    private final Map<Point, Map<Direction, Integer>> bidirectionalEdgeMap;
    private final Map<Point, List<Point>> mazePaths;
    private final Random random;


    public SpanningTree() {
        this.processedNodes = new ArrayList<>();
        this.nodesToProcess = new ArrayList<>();
        this.bidirectionalEdgeMap = new HashMap<>();
        this.visitedNodes = new ArrayList<>();
        this.unvisitedNeighbouringNodes = new ArrayList<>();
        this.mazePaths = new HashMap<>();
        random = new Random();
    }

    private void init(String[][] cells) {
        resetState();

        addAllNodesToProcessingList(cells);
    }

    public void resetState() {
        processedNodes.clear();
        nodesToProcess.clear();
        bidirectionalEdgeMap.clear();
        mazePaths.clear();
        visitedNodes.clear();
        unvisitedNeighbouringNodes.clear();
        bidirectionalEdgeMap.clear();
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


        return this;
    }

    private boolean isEdgePoint(Point point, String[][] cells) {
        return isFirstCol(point.x)
                || isTopRow(point)
                || isLastCol(point, cells)
                || isBottomRow(point, cells);
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
                    addTopLeftCornerEdges(point);
                } else if (isTopRightCorner(point, cells)) {
                    addTopRightCornerEdges(point);
                } else if (isTopEdgeOnly(point, cells)) {
                    addTopEdges(point);
                } else if (isBottomLeftCorner(point, cells)) {
                    addBottomLeftCornerEdges(point);
                } else if (isBottomRightCorner(point, cells)) {
                    addBottomRightCornerEdges(point);
                } else if (isLeftEdgeOnly(point, cells)) {
                    addLeftEdges(point);
                } else if (isRightEdgeOnly(point, cells)) {
                    addRightEdges(point);
                } else if (isBottomEdgeOnly(point, cells)) {
                    addBottomEdges(point);
                } else { // somewhere in the middle - not an edge/corner
                    addCentrePointEdges(point);
                }
            }
        }
    }

    public boolean isRightEdgeOnly(Point point, String[][] cells) {
        return isLastCol(point, cells) && !isTopRow(point) && !isBottomRow(point, cells);
    }

    public boolean isLeftEdgeOnly(Point point, String[][] cells) {
        return isFirstCol(point.x) && !isTopRow(point) && !isBottomRow(point, cells);
    }

    public boolean isBottomRightCorner(Point point, String[][] cells) {
        return isLastCol(point, cells) && isBottomRow(point, cells);
    }

    private boolean isBottomRow(Point point, String[][] cells) {
        return point.y == (cells.length - 1);
    }

    public boolean isBottomLeftCorner(Point point, String[][] cells) {
        return isFirstCol(point.x) && isBottomRow(point, cells);
    }

    public Point offsetPoint(Point point, Direction direction) {
        return new Point(point.x + direction.xOffset, point.y + direction.yOffset);
    }

    private boolean isTopEdgeOnly(Point point, String[][] cells) {
        return isTopRow(point) && !isFirstCol(point.x) && !isLastCol(point, cells);
    }

    private boolean isTopRow(Point point) {
        return point.y == 0;
    }

    public boolean isTopRightCorner(Point point, String[][] cells) {
        return isTopRow(point) && isLastCol(point, cells);
    }

    public boolean isTopLeftCorner(Point point) {
        return isFirstCol(point.x) && isTopRow(point);
    }

    private boolean isFirstCol(int x) {
        return x == 0;
    }

    public boolean isLastCol(Point point, String[][] cells) {
        return point.x == cells[0].length - 1;
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
        Point nodeAbove = offsetPoint(point, UP);
        permittedWeightedDirections.put(UP, bidirectionalEdgeMap.get(nodeAbove).get(DOWN));
        permittedWeightedDirections.put(DOWN, random.nextInt());
        permittedWeightedDirections.put(RIGHT, random.nextInt());

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public boolean isBottomEdgeOnly(Point point, String[][] cells) {
        return point.x != 0 && point.x != (cells[0].length - 1) && isBottomRow(point, cells);
    }

    public void addEdges(Point point, List<Direction> directions) {
        Map<Direction, Integer> permittedWeightedDirections = new HashMap<>();

        Point nodeAbove = offsetPoint(point, UP);
        Point nodeToLeft = offsetPoint(point, LEFT);

        for (Direction direction : directions) {
            switch (direction) {
                case UP:
                    permittedWeightedDirections.put(UP, bidirectionalEdgeMap.get(nodeAbove).get(DOWN));
                    break;
                case DOWN:
                    permittedWeightedDirections.put(DOWN, random.nextInt());
                    break;
                case LEFT:
                    permittedWeightedDirections.put(LEFT, bidirectionalEdgeMap.get(nodeToLeft).get(RIGHT));
                    break;
                case RIGHT:
                    permittedWeightedDirections.put(RIGHT, random.nextInt());
                    break;
            }
        }

        bidirectionalEdgeMap.put(point, permittedWeightedDirections);
    }

    public void addCentrePointEdges(Point point) {
        addEdges(point, Arrays.asList(Direction.values()));
    }

    public void addRightEdges(Point point) {
        addEdges(point, Arrays.asList(UP, DOWN, LEFT));
    }

    public void addBottomEdges(Point point) {
        addEdges(point, Arrays.asList(UP, LEFT, RIGHT));
    }

    public void addBottomRightCornerEdges(Point point) {
        addEdges(point, Arrays.asList(UP, LEFT));
    }

    public void addTopLeftCornerEdges(Point point) {
        addEdges(point, Arrays.asList(DOWN, RIGHT));
    }

    public void addTopEdges(Point point) {
        addEdges(point, Arrays.asList(DOWN, LEFT, RIGHT));
    }

    public void addTopRightCornerEdges(Point point) {
        addEdges(point, Arrays.asList(DOWN, LEFT));
    }

    public void addBottomLeftCornerEdges(Point point) {
        addEdges(point, Arrays.asList(UP, RIGHT));
    }

    public void visit(Point point) {
        visitedNodes.add(point);
        nodesToProcess.remove(point);
        bidirectionalEdgeMap.get(point).keySet().stream()
                .forEach(direction -> {
                    Point aPoint = offsetPoint(point, direction);
                    if (!visitedNodes.contains(aPoint)) {
                        unvisitedNeighbouringNodes.add(aPoint);
                    }
                });
    }

    public List<Point> getVisitedNodes() {
        return visitedNodes;
    }

    public void visitNextClosestNodeThatHasNotAlreadyBeenVisited(Point point) {
        // get validDirections from input point
        Map<Direction, Integer> directionsToDistanceMap = bidirectionalEdgeMap.get(point);
        List<Direction> permittedDirections = directionsToDistanceMap.keySet().stream().collect(toList());
        Direction nearestNodeDirection = null;
        for (int i=0; i<permittedDirections.size(); i++) {
            Direction directionBeingReviewed = permittedDirections.get(i);
            if (nearestNodeDirection == null) {
                nearestNodeDirection = directionBeingReviewed;
            } else if (directionsToDistanceMap.get(nearestNodeDirection)>directionsToDistanceMap.get(directionBeingReviewed)){
                nearestNodeDirection = directionBeingReviewed;
            }
        }
        visit(offsetPoint(point, nearestNodeDirection));
    }

    public List<Point> getUnvisitedNeighbouringNodes() {
        return unvisitedNeighbouringNodes;
    }
}
