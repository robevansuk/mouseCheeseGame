package uk.robevans;

import java.awt.Point;
import java.util.Random;

public class RandomPointGenerator {

    public Point getPlayerStartLocation(int width, int height) {
        int start_x = new Random().nextInt(width);
        int start_y = new Random().nextInt(height);
        return new Point(start_x, start_y);
    }
}
