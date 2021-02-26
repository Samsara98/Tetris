import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    default List<Shape> gamingAreaAnalyse(GamingArea gamingArea,Shape[] shapes) {

        Map<Shape, Integer> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                map.put(shapes[i], (gamingArea.getAreaWidth()-1)*2+1);
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