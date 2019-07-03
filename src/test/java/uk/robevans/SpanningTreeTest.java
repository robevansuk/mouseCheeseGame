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
        Point topLeftCornerPoint = new Point(0,0);

        testObject.addTopLeftCornerEdgesToEdgeMap(topLeftCornerPoint);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(1, testObject.getBidirectionalEdgeMap().size());
        assertEquals(2, testObject.getBidirectionalEdgeMap().get(topLeftCornerPoint).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(topLeftCornerPoint).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topLeftCornerPoint).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd3EdgesForEachTopEdgeThatIsNotACorner() {
        Point topLeftCornerPoint = new Point(0,0);
        testObject.addTopLeftCornerEdgesToEdgeMap(topLeftCornerPoint);
        Point topEdge1 = new Point(1, 0);
        Point topEdge2 = new Point(2, 0);
        Point topEdge3 = new Point(3, 0);

        testObject.addTopEdges(topEdge1);
        testObject.addTopEdges(topEdge2);
        testObject.addTopEdges(topEdge3);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(4, testObject.getBidirectionalEdgeMap().size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(topEdge1).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(topEdge2).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(topEdge3).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge1).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge1).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge1).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge2).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge2).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge2).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge3).containsKey(Direction.LEFT));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge3).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topEdge3).containsKey(Direction.RIGHT));
    }

    @Test
    public void shouldAdd2EdgesForTopRightCorner() {
        Point topLeftCornerPoint = new Point(0, 0);
        Point topRightCornerPoint = new Point(0,4);
        testObject.addTopLeftCornerEdgesToEdgeMap(topLeftCornerPoint);
        testObject.addTopEdges(new Point(1, 0));
        testObject.addTopEdges(new Point(2, 0));
        testObject.addTopEdges(new Point(3, 0));

        testObject.addTopRightCornerEdgesToEdgeMap(topRightCornerPoint);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(1, testObject.getBidirectionalEdgeMap().size());
        assertEquals(2, testObject.getBidirectionalEdgeMap().get(topRightCornerPoint).size());
        assertTrue(testObject.getBidirectionalEdgeMap().get(topRightCornerPoint).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(topRightCornerPoint).containsKey(Direction.LEFT));
    }

    @Test
    public void shouldAdd3EdgesForEachLeftEdgeThatIsNotACorner() {
        Point topLeftCornerPoint = new Point(0,0);
        testObject.addTopLeftCornerEdgesToEdgeMap(topLeftCornerPoint);
        Point leftEdge1 = new Point(0, 1);
        Point leftEdge2 = new Point(0, 2);
        Point leftEdge3 = new Point(0, 3);

        testObject.addLeftEdges(leftEdge1);
        testObject.addLeftEdges(leftEdge2);
        testObject.addLeftEdges(leftEdge3);

        // 1 entry for the point with 2 directions to navigate to
        assertEquals(4, testObject.getBidirectionalEdgeMap().size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(leftEdge1).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(leftEdge2).size());
        assertEquals(3, testObject.getBidirectionalEdgeMap().get(leftEdge3).size());

        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge1).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge1).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge1).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge2).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge2).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge2).containsKey(Direction.RIGHT));

        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge3).containsKey(Direction.UP));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge3).containsKey(Direction.DOWN));
        assertTrue(testObject.getBidirectionalEdgeMap().get(leftEdge3).containsKey(Direction.RIGHT));
    }
}