import java.util.Objects;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

class Main {
    public static void main(String[] args) {
        Robot bot = new Robot(10000, 10000, Direction.DOWN);
        Move move = new Move();
        move.moveRobot(bot, 0, 0);
    }
}

class Move {
    // no redundant turns, sequence swapped XY->YX if needed
    // relies on ENUM ordinal so ENUM listing should stay as UP-DOWN-LEFT-RIGHT
    static final int SAME = 0;
    static final int OPPOSITE = 1;
    static final int CLOCKWISE = 2;
    static final int DOWN_LEFT = 3;
    
    public static void moveRobot(Robot robot, int toX, int toY) {
        
        ArrayList<Character> axes = new ArrayList<>(List.of('X', 'Y', 'X'));

        ArrayList<Direction> turns = new ArrayList<>(Arrays.asList(
                robot.getDirection(),
                toX - robot.getX() > 0 ? Direction.RIGHT : Direction.LEFT,
                toY - robot.getY() > 0 ? Direction.UP : Direction.DOWN));

        if (Math.abs(turns.get(0).ordinal() - turns.get(1).ordinal()) == OPPOSITE && 
                      turns.get(0).ordinal() + turns.get(1).ordinal() != DOWN_LEFT) {
            Collections.swap(turns, 1, 2);
            axes.remove(0);
        }

        int size = turns.size() - 1;
        for (int i = 0; i < size; i++) {
            rotateToDestination(robot, turns.get(i), turns.get(i + 1));
            if (axes.get(i) == 'X') {
                moveAxisWise(robot, Math.abs(toX - robot.getX()));
            } else {
                moveAxisWise(robot, Math.abs(toY - robot.getY()));
            }
        }
    }

    public static void rotateToDestination(Robot robot, Direction a, Direction b) {
        
        if (a.ordinal() - b.ordinal() == SAME) {
        } else if (a.ordinal() - b.ordinal() == CLOCKWISE || (a.ordinal() - b.ordinal()) % 2 == -1) {
            robot.turnRight();
        } else {
            robot.turnLeft();
        }
    }

    public static void moveAxisWise(Robot robot, int steps) {
    
        for (int i = 0; i < steps; i++) {
            robot.stepForward();
        }
    }
}

enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction turnLeft() {
        switch (this) {
            case UP:
                return LEFT;
            case DOWN:
                return RIGHT;
            case LEFT:
                return DOWN;
            case RIGHT:
                return UP;
            default:
                throw new IllegalStateException();
        }
    }

    public Direction turnRight() {
        switch (this) {
            case UP:
                return RIGHT;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            case RIGHT:
                return DOWN;
            default:
                throw new IllegalStateException();
        }
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }
}

class Robot {
    private int x;
    private int y;
    private Direction direction;

    public Robot(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public void turnRight() {
        direction = direction.turnRight();
    }

    public void stepForward() {
        x += direction.dx();
        y += direction.dy();
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
