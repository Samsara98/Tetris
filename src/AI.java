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
        if(shape.getHeight() > shape.fastRotation().getHeight()) {
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
        if(!moveFound) {
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
}