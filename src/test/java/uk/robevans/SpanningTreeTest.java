package uk.robevans;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpanningTreeTest {

    private SpanningTree testObject;
    private static final String[][] TEST_MATRIX = new String[5][5];

    @Before
    public void setUp() {
        testObject = new SpanningTree();
        testObject.build(TEST_MATRIX);
    }

    @Test
    public void shouldBe_25_NodesInAllTreeNodesForA_5x5_Game() {
        List<Point> result = testObject.getAllTreeNodes();

        assertEquals(25, result.size());
    }

    @Test
    public void onInitSpanningTreeShouldCreateAnEmptySetForProcessedNodes() {
        List<Point> processedPoints = testObject.getProcessedPoints();

        assertEquals(0, processedPoints.size());
    }

    @Test
    public void onInitThereShouldBeWidthMultipliedByHeightCellsToProcess() {
        List<Point> pointsToProcess = testObject.getPointsToProcess();

        assertEquals(25, pointsToProcess.size());
    }


    @Test
    public void thereShouldBeTheSameNumberOfEdgeEntriesInTheEdgeMapAsNodesInTheSpanningTree() {
        createFullyConnectedGraph();

        Map<Point, Map<Direction, Integer>> edgeEntries = testObject.getBidirectionalEdgeMap();

        assertEquals(25, edgeEntries.size());
    }

    @Test
    public void isLastColShouldReturnTrueForFinalColumnCells() {
        assertTrue(testObject.isLastCol(new Point(4, 0), TEST_MATRIX));
        assertTrue(testObject.isLastCol(new Point(4, 1), TEST_MATRIX));
        assertTrue(testObject.isLastCol(new Point(4, 2), TEST_MATRIX));
        assertTrue(testObject.isLastCol(new Point(4, 3), TEST_MATRIX));
        assertTrue(testObject.isLastCol(new Point(4, 4), TEST_MATRIX));

        assertFalse(testObject.isLastCol(new Point(3, 4), TEST_MATRIX));
        assertFalse(testObject.isLastCol(new Point(2, 4), TEST_MATRIX));
        assertFalse(testObject.isLastCol(new Point(1, 4), TEST_MATRIX));
        assertFalse(testObject.isLastCol(new Point(0, 4), TEST_MATRIX));
    }

    @Test
    public void resettingStateShouldClearAllArrays() {
        testObject.resetState();

        assertEquals(0, testObject.getAllTreeNodes().size());
        assertEquals(0, testObject.getBidirectionalEdgeMap().size());
        assertEquals(0, testObject.getProcessedPoints().size());
    }

    @Test
    public void shouldBeAbleToDetermineTopLeftCorner() {
        assertTrue(testObject.isTopLeftCorner(new Point(0, 0)));
        assertFalse(testObject.isTopLeftCorner(new Point(0, 1)));
        assertFalse(testObject.isTopLeftCorner(new Point(1, 0)));
    }

    @Test
    public void shouldBeAbleToDetermineTopRightCorner() {
        assertTrue(testObject.isTopRightCorner(new Point(4, 0), TEST_MATRIX));
        assertFalse(testObject.isTopRightCorner(new Point(0, 3), TEST_MATRIX));
        assertFalse(testObject.isTopRightCorner(new Point(0, 2), TEST_MATRIX));
        assertFalse(testObject.isTopRightCorner(new Point(0, 1), TEST_MATRIX));
        assertFalse(testObject.isTopRightCorner(new Point(1, 4), TEST_MATRIX));
    }

    @Test
    public void isLeftEdgeShouldReturnTrueForLeftEdgesOnly() {
        assertTrue(testObject.isLeftEdgeOnly(new Point(0, 1), TEST_MATRIX));
        assertTrue(testObject.isLeftEdgeOnly(new Point(0, 2), TEST_MATRIX));
        assertTrue(testObject.isLeftEdgeOnly(new Point(0, 3), TEST_MATRIX));

        assertFalse(testObject.isLeftEdgeOnly(new Point(0, 0), TEST_MATRIX));
        assertFalse(testObject.isLeftEdgeOnly(new Point(0, 4), TEST_MATRIX));
        assertFalse(testObject.isLeftEdgeOnly(new Point(1, 0), TEST_MATRIX));
        assertFalse(testObject.isLeftEdgeOnly(new Point(2, 0), TEST_MATRIX));
        assertFalse(testObject.isLeftEdgeOnly(new Point(3, 0), TEST_MATRIX));
        assertFalse(testObject.isLeftEdgeOnly(new Point(4, 0), TEST_MATRIX));
    }

    @Test
    public void isRightEdgeShouldReturnTrueForRightEdgesOnly() {
        assertTrue(testObject.isRightEdgeOnly(new Point(4, 1), TEST_MATRIX));
        assertTrue(testObject.isRightEdgeOnly(new Point(4, 2), TEST_MATRIX));
        assertTrue(testObject.isRightEdgeOnly(new Point(4, 3), TEST_MATRIX));

        assertFalse(testObject.isRightEdgeOnly(new Point(4, 0), TEST_MATRIX));
        assertFalse(testObject.isRightEdgeOnly(new Point(4, 4), TEST_MATRIX));
        assertFalse(testObject.isRightEdgeOnly(new Point(1, 0), TEST_MATRIX));
        assertFalse(testObject.isRightEdgeOnly(new Point(2, 0), TEST_MATRIX));
        assertFalse(testObject.isRightEdgeOnly(new Point(3, 0), TEST_MATRIX));
        assertFalse(testObject.isRightEdgeOnly(new Point(4, 0), TEST_MATRIX));
    }

    @Test
    public void isBottomLeftCornerShouldReturnTrueForBottomLeftCornerOnly() {
        assertTrue(testObject.isBottomLeftCorner(new Point(0, 4), TEST_MATRIX));
        assertFalse(testObject.isBottomLeftCorner(new Point(0, 3), TEST_MATRIX));
        assertFalse(testObject.isBottomLeftCorner(new Point(1, 4), TEST_MATRIX));
    }

    @Test
    public void isBottomEdgeShouldReturnTrueForBottomEdgesOnly() {
        assertTrue(testObject.isBottomEdgeOnly(new Point(1, 4), TEST_MATRIX));
        assertTrue(testObject.isBottomEdgeOnly(new Point(2, 4), TEST_MATRIX));
        assertTrue(testObject.isBottomEdgeOnly(new Point(3, 4), TEST_MATRIX));

        assertFalse(testObject.isBottomEdgeOnly(new Point(0, 4), TEST_MATRIX));
        assertFalse(testObject.isBottomEdgeOnly(new Point(4, 4), TEST_MATRIX));
    }

    @Test
    public void isBottomRightCornerShouldReturnTrueForBottomRightCornerOnly() {
        assertTrue(testObject.isBottomRightCorner(new Point(4, 4), TEST_MATRIX));
        assertFalse(testObject.isBottomRightCorner(new Point(4, 3), TEST_MATRIX));
        assertFalse(testObject.isBottomRightCorner(new Point(3, 4), TEST_MATRIX));
    }

    @Test
    public void shouldOffSetNodeUpForDirectionUp() {
        Point startingPoint = new Point(3, 4);

        Point result = testObject.offsetPoint(startingPoint, Direction.UP);

        assertEquals(3, result.x);
        assertEquals(3, result.y);
    }

    @Test
    public void shouldOffSetNodeDownForDirectionDown() {
        Point startingPoint = new Point(2, 0);

        Point result = testObject.offsetPoint(startingPoint, Direction.DOWN);

        assertEquals(2, result.x);
        assertEquals(1, result.y);
    }

    @Test
    public void shouldOffSetNodeLeftForDirectionLeft() {
        Point startingPoint = new Point(2, 0);

        Point result = testObject.offsetPoint(startingPoint, Direction.LEFT);

        assertEquals(1, result.x);
        assertEquals(0, result.y);
    }

    @Test
    public void shouldOffSetNodeRightForDirectionRight() {
        Point startingPoint = new Point(2, 0);

        Point result = testObject.offsetPoint(startingPoint, Direction.RIGHT);

        assertEquals(3, result.x);
        assertEquals(0, result.y);
    }

    @Test
    public void shouldAdd2EdgesForTopLeftCorner() {
        Point topLeftCornerPoint = new Point(0, 0);

        testObject.addTopLeftCornerEdges(topLeftCornerPoint);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(1, testObject.getBidirectionalEdgeMap().size());
        assertEquals(2, testObject.getBidirectionalEdgeMap().get(topLeftCornerPoint).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(topLeftCornerPoint).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topLeftCornerPoint).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd3EdgesForEachTopEdgeThatIsNotACorner() {
        Point topLeftCornerPoint = new Point(0, 0);
        testObject.addTopLeftCornerEdges(topLeftCornerPoint);
        Point topPoint1 = new Point(1, 0);
        Point topPoint2 = new Point(2, 0);
        Point topPoint3 = new Point(3, 0);

        testObject.addTopEdges(topPoint1);
        testObject.addTopEdges(topPoint2);
        testObject.addTopEdges(topPoint3);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(4, testObject.getBidirectionalEdgeMap().size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(topPoint1).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(topPoint2).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(topPoint3).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint1).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint1).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint1).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint2).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint2).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint2).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint3).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint3).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topPoint3).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd2EdgesForTopRightCorner() {
        Point topLeftCornerPoint = new Point(0, 0);
        Point topRightCornerPoint = new Point(4, 0);
        testObject.addTopLeftCornerEdges(topLeftCornerPoint);
        testObject.addTopEdges(new Point(1, 0));
        testObject.addTopEdges(new Point(2, 0));
        testObject.addTopEdges(new Point(3, 0));

        testObject.addTopRightCornerEdges(topRightCornerPoint);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(5, testObject.getBidirectionalEdgeMap().size());
        assertEquals(2, testObject.getBidirectionalEdgeMap().get(topRightCornerPoint).size());
        assertTrue(testObject.getBidirectionalEdgeMap().get(topRightCornerPoint).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topRightCornerPoint).containsKey(Direction.LEFT));
    }

    @Test
    public void shouldAdd3EdgesForEachLeftEdgeThatIsNotACorner() {
        Point topLeftCornerPoint = new Point(0, 0);
        testObject.addTopLeftCornerEdges(topLeftCornerPoint);
        Point leftPoint1 = new Point(0, 1);
        Point leftPoint2 = new Point(0, 2);
        Point leftPoint3 = new Point(0, 3);

        testObject.addLeftEdges(leftPoint1);
        testObject.addLeftEdges(leftPoint2);
        testObject.addLeftEdges(leftPoint3);

        assertEquals(4, testObject.getBidirectionalEdgeMap().size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(leftPoint1).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(leftPoint2).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(leftPoint3).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint1).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint1).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint1).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint2).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint2).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint2).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint3).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint3).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftPoint3).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd4EdgesForACentreCellWithEntriesForAll4Directions() {
        Point topLeftCornerPoint = new Point(0, 0);
        testObject.addTopLeftCornerEdges(topLeftCornerPoint);
        Point topPoint1 = new Point(1, 0);
        testObject.addTopEdges(topPoint1);
        Point leftPoint1 = new Point(0, 1);
        Point leftPoint2 = new Point(0, 2);
        Point leftPoint3 = new Point(0, 3);
        testObject.addLeftEdges(leftPoint1);
        testObject.addLeftEdges(leftPoint2);
        testObject.addLeftEdges(leftPoint3);

        Point centralPoint = new Point(1, 1);
        testObject.addCentrePointEdges(centralPoint);

        assertEquals(6, testObject.getBidirectionalEdgeMap().size());
        assertEquals(4, testObject.getBidirectionalEdgeMap().get(centralPoint).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(centralPoint).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(centralPoint).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(centralPoint).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(centralPoint).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd3EdgesForARightHandSideCellWithEntriesForUpDownAndLeft() {
        Point topLeftCornerPoint = new Point(0, 0);
        testObject.addTopLeftCornerEdges(topLeftCornerPoint);
        Point topPoint1 = new Point(1, 0);
        Point topPoint2 = new Point(2, 0);
        Point topPoint3 = new Point(3, 0);
        testObject.addTopEdges(topPoint1);
        testObject.addTopEdges(topPoint2);
        testObject.addTopEdges(topPoint3);
        Point topRightCorner = new Point(4, 0);
        testObject.addTopRightCornerEdges(topRightCorner);
        Point leftPoint1 = new Point(0, 1);
        Point leftPoint2 = new Point(0, 2);
        Point leftPoint3 = new Point(0, 3);
        testObject.addLeftEdges(leftPoint1);
        testObject.addLeftEdges(leftPoint2);
        testObject.addLeftEdges(leftPoint3);
        Point centralPoint1 = new Point(1, 1);
        Point centralPoint2 = new Point(2, 1);
        Point centralPoint3 = new Point(3, 1);
        testObject.addCentrePointEdges(centralPoint1);
        testObject.addCentrePointEdges(centralPoint2);
        testObject.addCentrePointEdges(centralPoint3);

        Point rightEdge = new Point(4, 1);
        testObject.addRightEdges(rightEdge);

        assertEquals(12, testObject.getBidirectionalEdgeMap().size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(rightEdge).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(rightEdge).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(rightEdge).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(rightEdge).containsKey(Direction.LEFT));
    }

    @Test
    public void shouldAdd2EdgesForABottomLeftCorner() {
        fillWeightsUpToBottomRow();

        Point bottomLeftCornerPoint = new Point(0, 4);
        testObject.addBottomLeftCornerEdges(bottomLeftCornerPoint);

        assertEquals(21, testObject.getBidirectionalEdgeMap().size());
        assertEquals(2, testObject.getBidirectionalEdgeMap().get(bottomLeftCornerPoint).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomLeftCornerPoint).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomLeftCornerPoint).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd3EdgesForABottomEdgePoint() {
        fillWeightsUpToBottomRow();
        testObject.addBottomLeftCornerEdges(new Point(0, 4));
        Point bottomPoint1 = new Point(1, 4);
        Point bottomPoint2 = new Point(2, 4);
        Point bottomPoint3 = new Point(3, 4);
        testObject.addBottomEdges(bottomPoint1);
        testObject.addBottomEdges(bottomPoint2);
        testObject.addBottomEdges(bottomPoint3);

        assertEquals(24, testObject.getBidirectionalEdgeMap().size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(bottomPoint1).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(bottomPoint2).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(bottomPoint3).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint1).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint1).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint1).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint2).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint2).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint2).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint3).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint3).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomPoint3).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd2EdgesForABottomRightCornerPoint() {
        fillWeightsUpToBottomRow();
        testObject.addBottomLeftCornerEdges(new Point(0, 4));
        Point bottomPoint1 = new Point(1, 4);
        Point bottomPoint2 = new Point(2, 4);
        Point bottomPoint3 = new Point(3, 4);
        testObject.addBottomEdges(bottomPoint1);
        testObject.addBottomEdges(bottomPoint2);
        testObject.addBottomEdges(bottomPoint3);

        Point bottomRightCornerPoint = new Point(4, 4);
        testObject.addBottomRightCornerEdges(bottomRightCornerPoint);

        assertEquals(25, testObject.getBidirectionalEdgeMap().size());
        assertEquals(2, testObject.getBidirectionalEdgeMap().get(bottomRightCornerPoint).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomRightCornerPoint).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(bottomRightCornerPoint).containsKey(Direction.UP));
    }

    private void fillWeightsUpToBottomRow() {
        testObject.addTopLeftCornerEdges(new Point(0, 0));

        for (int i = 1; i < TEST_MATRIX[0].length - 1; i++) {
            testObject.addTopEdges(new Point(i, 0));
        }

        testObject.addTopRightCornerEdges(new Point(4, 0));

        for (int i = 1; i < TEST_MATRIX.length - 1; i++) {
            testObject.addLeftEdges(new Point(0, i));
        }

        for (int i = 1; i < TEST_MATRIX[0].length - 1; i++) {
            testObject.addCentrePointEdges(new Point(i, 1));
            testObject.addCentrePointEdges(new Point(i, 2));
            testObject.addCentrePointEdges(new Point(i, 3));
        }

        for (int i = 1; i < TEST_MATRIX.length - 1; i++) {
            testObject.addRightEdges(new Point(4, i));
        }
    }

    @Test
    public void shouldAddFirstNodeToVisitedNodeWhenVisitIsCalled() {
        testObject.visit(new Point(0, 0));

        assertEquals(1, testObject.getVisitedNodes().size());
    }

    @Test
    public void shouldRemoveFirstNodeFromNodesLeftToVisitWhenVisitNodeIsCalled() {
        testObject.visit(new Point(0, 0));
        assertEquals(24, testObject.getPointsToProcess().size());
    }

    @Test
    public void addFirstEdgeAndConnectedNodeToMSTAndVisitedNodesByPickingTheOneWithTheShortestDistanceToTheNodeToProcess() {
        // Could mock generating the min distance for one of the edges to clean up this test but this more lengthy version also works
        createFullyConnectedGraph();
        Point topLeftCorner = new Point(0, 0);
        Map<Direction, Integer> directionalDistancesMap = testObject.getBidirectionalEdgeMap().get(topLeftCorner);
        Direction closestDirection = Direction.RIGHT;
        if (directionalDistancesMap.get(Direction.DOWN) <= directionalDistancesMap.get(Direction.RIGHT)) {
            closestDirection = Direction.DOWN;
        }
        Point nearestPoint = testObject.offsetPoint(topLeftCorner, closestDirection);

        testObject.visit(topLeftCorner);

        List<Point> pointsToProcess = testObject.getPointsToProcess();
        Point lastPointToProcess = pointsToProcess.get(pointsToProcess.size() - 1);
        assertEquals(nearestPoint, lastPointToProcess);
    }

    private void createFullyConnectedGraph() {
        testObject.addTopLeftCornerEdges(new Point(0, 0));
        for (int i = 1; i < TEST_MATRIX[0].length - 1; i++) {
            testObject.addTopEdges(new Point(i, 0));
        }
        testObject.addTopRightCornerEdges(new Point(4, 0));

        for (int i = 1; i < TEST_MATRIX.length - 1; i++) {
            testObject.addLeftEdges(new Point(0, i));
        }
        for (int i = 1; i < TEST_MATRIX[0].length - 1; i++) {
            testObject.addCentrePointEdges(new Point(i, 1));
            testObject.addCentrePointEdges(new Point(i, 2));
            testObject.addCentrePointEdges(new Point(i, 3));
        }
        for (int i = 1; i < TEST_MATRIX.length - 1; i++) {
            testObject.addRightEdges(new Point(4, i));
        }

        testObject.addBottomLeftCornerEdges(new Point(0, 4));
        for(int i=1; i<TEST_MATRIX[0].length - 1; i++) {
            testObject.addBottomEdges(new Point(i, 4));
        }
        testObject.addBottomRightCornerEdges(new Point(4, 4));
    }
}