import java.util.*;
import java.util.stream.Collectors;

public interface AI {
    class Move {
        public int x;
        public int y;
        public Shape shape;
        public double score;    // 分数越低越好
    }

    // 这个人工智能其实是个人工智障，不过用来说明问题已经足够了
    default public Move calculateBestMove(GamingArea gamingArea, Shape shape) {
        // 计算最佳旋转
        if (shape.getHeight() > shape.fastRotation().getHeight()) {
            shape = shape.fastRotation();
        }

        // 计算最佳位置
        int bestCol = 0, bestRow = 0, bestScore = 99999999;
        boolean moveFound = false;
        for (int col = 0; col < gamingArea.getAreaWidth(); col++) {
            int row = gamingArea.getColumnHeight(col);
            int result = gamingArea.place(shape, col, row);
            if (result <= GamingArea.ROW_FULL) {
                int score = row + shape.getHeight();
                if (!moveFound) {
                    bestCol = col;
                    bestRow = row;
                    bestScore = score;
                    moveFound = true;
                } else if (score < bestScore) {
                    bestCol = col;
                    bestRow = row;
                    bestScore = score;
                }
            }
            gamingArea.undo();
        }

        // 返回计算出的bestMove
        if (!moveFound) {
            return null;
        } else {
            Move bestMove = new Move();
            bestMove.shape = shape;
            bestMove.x = bestCol;
            bestMove.y = bestRow;
            bestMove.score = bestScore;
            return bestMove;
        }
    }

    default Move Axis(GamingArea gamingArea, Shape shape) {

        int bestCol = 0, bestRow = 0;
        double bestScore = -999999.9;
        Shape root = new Shape(shape.getPoints());
        Shape best = root;
        int num = 0;
        while (true) {
            for (int col = 0; col < gamingArea.getAreaWidth(); col++) {
                int row = gamingArea.getColumnHeight(col);
                int result = gamingArea.place(shape, col, row);
                if (gamingArea.getMaxHeight() > gamingArea.getAreaHeight() - Tetris.TOP_SPACE) {
                    result = GamingArea.OUT;
                }
                if (result <= GamingArea.ROW_FULL) {

                    gamingArea.undo();
                    if (gamingArea.place(shape, col, row + 1) == GamingArea.COLLIDED) {
                        continue;
                    } else {
                        gamingArea.undo();
                    }

                    if (gamingArea.place(shape, col, row + 2) == GamingArea.COLLIDED) {
                        continue;
                    } else {
                        gamingArea.undo();
                    }
                    gamingArea.place(shape, col, row);

                    double LandingHeight = row + shape.getHeight() / 2.0;

                    int RowsEliminated = gamingArea.clearRows();
                    int RowTransition = RowTransition(gamingArea);
                    int ColumnTransitions = ColumnTransitions(gamingArea);
                    int NumberOfHoles = NumberOfHoles(gamingArea);
                    int WellSums = WellSums(gamingArea);

                    double score = (-4.5 * LandingHeight) +
                            (3.42 * RowsEliminated) +
                            (-3.22 * RowTransition) +
                            (-9.35 * ColumnTransitions) +
                            (-7.9 * NumberOfHoles) +
                            (-3.39 * WellSums);

                    if (score > bestScore) {
                        bestCol = col;
                        bestRow = row;
                        bestScore = score;
                        best = shape;
                    }
                }
                gamingArea.undo();
                num++;
            }
            if (shape.fastRotation().equals(root)) {
                System.out.println(num);
                break;
            } else {
                shape = shape.fastRotation();
            }
        }

        // 返回计算出的bestMove
        Move bestMove = new Move();
        bestMove.shape = best;
        bestMove.x = bestCol;
        bestMove.y = bestRow;
        bestMove.score = bestScore;
        return bestMove;
    }

    default int WellSums(GamingArea gamingArea) {

        List<Integer> wellList = new ArrayList<>();
        for (int x = 0; x < gamingArea.getAreaWidth(); x++) {
            int row = 0;
            int num = 0;
            while (row < gamingArea.getAreaHeight()) {
                if (gamingArea.isFilled(x - 1, row) && gamingArea.isFilled(x + 1, row) && !gamingArea.isFilled(x, row)) {
                    num++;
                } else if (num > 0) {
                    wellList.add(num);
                    num = 0;
                }
                row++;
            }
            wellList.add(num);
        }
        return wellList.stream().map(this::sumX).mapToInt(x -> x).sum();
    }

    default int sumX(int num) {

        int sum = 0;
        while (num > 0) {
            sum += num;
            num--;
        }
        return sum;
    }

    default int NumberOfHoles(GamingArea gamingArea) {

        int numberOfHoles = 0;
        for (int x = 0; x < gamingArea.getAreaWidth(); x++) {
            boolean start = false;
            for (int y = gamingArea.getAreaHeight() - 1; y >= 0; y--) {
                if (gamingArea.isFilled(x, y)) {
                    start = true;
                } else if (!gamingArea.isFilled(x, y) && start) {
                    numberOfHoles++;
                }
            }
        }

        return numberOfHoles;
    }


    default int ColumnTransitions(GamingArea gamingArea) {

        int columnTransitions = 0;
        for (int x = 0; x < gamingArea.getAreaWidth(); x++) {
            for (int y = 0; y <= gamingArea.getAreaHeight(); y++) {
                if (gamingArea.isFilled(x, y - 1) != gamingArea.isFilled(x, y)) {
                    columnTransitions++;
                }

            }
        }
        return columnTransitions;
    }


    default int RowTransition(GamingArea gamingArea) {

        int rowTransition = 0;
        for (int y = 0; y < gamingArea.getAreaHeight(); y++) {
            for (int x = 0; x <= gamingArea.getAreaWidth(); x++) {
                if (gamingArea.isFilled(x - 1, y) != gamingArea.isFilled(x, y)) {
                    rowTransition++;
                }
            }
        }
        return rowTransition;
    }


    default List<Shape> gamingAreaAnalyse(GamingArea gamingArea, Shape[] shapes) {

        Map<Shape, Integer> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                map.put(shapes[i], (gamingArea.getAreaWidth() - 1) * 2 + 1);
            } else {
                map.put(shapes[i], 0);
            }
        }
        for (int col = 0; col < gamingArea.getAreaWidth() - 1; col++) {
            int state = gamingArea.getColumnHeight(col) - gamingArea.getColumnHeight(col + 1);
            switch (state) {

                case 0:
                    map.put(shapes[1], map.get(shapes[1]) + 1);
                    break;
                case -1:
                    map.put(shapes[4], map.get(shapes[4]) + 1);
                    map.put(shapes[6], map.get(shapes[6]) + 1);
                    break;
                case 1:
                    map.put(shapes[4], map.get(shapes[4]) + 1);
                    map.put(shapes[5], map.get(shapes[5]) + 1);
                    break;
                case 2:
                    map.put(shapes[2], map.get(shapes[2]) + 1);
                    break;
                case -2:
                    map.put(shapes[3], map.get(shapes[3]) + 1);
                    break;
            }
        }
        Comparator<Map.Entry<Shape, Integer>> comparator = Map.Entry.comparingByValue();
        List<Shape> shapeList = map.entrySet().stream().sorted(comparator).map(Map.Entry::getKey).collect(Collectors.toList());
        return shapeList;
    }


}