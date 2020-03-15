package uk.robevans;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.Point;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    private static final int MAZE_WIDTH = 10;
    private static final int MAZE_HEIGHT = 15;
    private Game testObject;

    @Mock
    private RandomPointGenerator pointGeneratorMock;
    @Mock
    private SpanningTree spanningTreeMock;

    @Before
    public void setUp() {
        when(pointGeneratorMock.getPlayerStartLocation(anyInt(), anyInt())).thenReturn(new Point(2, 3));
        testObject = new Game(MAZE_WIDTH, MAZE_HEIGHT, pointGeneratorMock);
    }

    @Test
    public void thereShouldBeAMazeWithWidthAndHeightSpecified() {
        String[][] result = testObject.getGameGrid();

        assertEquals(MAZE_HEIGHT, result.length);
        assertEquals(MAZE_WIDTH, result[0].length);
    }


    @Test
    public void invalidMazeSizeShouldThrowAnException() {
        try {
            new Game(1,1, pointGeneratorMock);
        } catch (RuntimeException ex) {
            assertEquals("The game grid must be at least 2 rows by 2 columns", ex.getMessage());
        }

        try {
            new Game(0,1, pointGeneratorMock);
        } catch (RuntimeException ex) {
            assertEquals("The game grid must be at least 2 rows by 2 columns", ex.getMessage());
        }

        try {
            new Game(1,0, pointGeneratorMock);
        } catch (RuntimeException ex) {
            assertEquals("The game grid must be at least 2 rows by 2 columns", ex.getMessage());
        }
    }

    @Test
    public void theMinimumMazeSizeShouldBe2By2() {
        testObject = new Game(2, 4, pointGeneratorMock);

        assertEquals(2, testObject.getGameWidth());
        assertEquals(4, testObject.getGameHeight());
    }

    @Test
    public void theGameGridShouldStartTheMousePlayerAtALocationInTheMaze() {
        assertEquals(new Point(2, 3), testObject.getMouseLocation());
    }

}