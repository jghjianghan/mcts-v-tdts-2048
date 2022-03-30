
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import model.GameModel;
import model.GameAction;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Jiang Han
 */
public class BoardUtilTest {

    @Test
    public void testEncodeDecode() {
        int board[][] = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        int boards[][][] = new int[5][][];
        boards[0] = board;
        board = new int[][]{
            {1, 5, 14, 13},
            {7, 10, 8, 12},
            {0, 6, 2, 3},
            {15, 9, 11, 4},};
        boards[1] = board;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 2; i < 5; i++) {
            boards[i] = new int[4][4];
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    boards[i][j][k] = random.nextInt(16);
                }
            }
        }

        for (int i = 0; i < boards.length; i++) {
            int newBoard[][] = GameModel.decode(GameModel.encode(boards[i]));
            Assert.assertArrayEquals(boards[i], newBoard);
        }
    }

    @Test
    public void testGetValueAt() {
        int board[][] = new int[4][4];
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = random.nextInt(16);
            }
        }

        long code = GameModel.encode(board);
        for (int i = 0; i < 16; i++) {
            Assert.assertEquals(GameModel.getValueAt(code, i), board[i / 4][i % 4]);
        }
    }

    @Test
    public void testSetValueAt() {
        int board[][] = new int[4][4];
        int board2[][] = new int[4][4];
        int resultBoard[][];

        int values[] = {2, 0, 1, 9, 12, 15};

        for (int k = 0; k < values.length; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    long code = GameModel.encode(board);
                    long resultCode = GameModel.setValueAt(code, i * 4 + j, values[k]);
                    board2[i][j] = values[k];
                    resultBoard = GameModel.decode(resultCode);
                    Assert.assertArrayEquals(board2, resultBoard);
                    board2[i][j] = 0;
                }
            }
        }

        board = new int[][]{
            {1, 0, 0, 1},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {10, 10, 6, 0},};
        int[][] expectedBoard = new int[][]{
            {1, 0, 0, 1},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {10, 0, 6, 0},};
        resultBoard = GameModel.decode(GameModel.setValueAt(GameModel.encode(board), 13, 0));
        Assert.assertArrayEquals(expectedBoard, resultBoard);
    }

    @Test
    public void testAvailableMoves1() {
        int board[][];
        GameAction expectedResult[];
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), GameAction.values());

        board = new int[][]{
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), GameAction.values());

        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 2, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[0]);
    }

    @Test
    public void testAvailableMoves2() {
        int board[][];
        GameAction expectedResult[];
        board = new int[][]{
            {1, 2, 1, 2},
            {2, 3, 3, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT});

        board = new int[][]{
            {1, 2, 1, 2},
            {3, 3, 2, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT});

        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 3, 3},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT});

        board = new int[][]{
            {1, 2, 2, 2},
            {2, 1, 2, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), GameAction.values());

        board = new int[][]{
            {1, 2, 3, 2},
            {2, 1, 3, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.UP, GameAction.DOWN});

        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 3, 1},
            {1, 2, 3, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.UP, GameAction.DOWN});

        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 2, 1},
            {1, 2, 1, 3},
            {2, 1, 2, 3},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.UP, GameAction.DOWN});
    }

    @Test
    public void testAvailableMoves3() {
        int board[][];
        GameAction expectedResult[];
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 1, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.UP});

        board = new int[][]{
            {1, 1, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 1, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.UP});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 0, 0, 0},
            {1, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT, GameAction.UP, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.UP, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 0, 1},
            {0, 0, 0, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.UP, GameAction.DOWN});

        board = new int[][]{
            {1, 0, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT, GameAction.UP, GameAction.DOWN});
    }

    @Test
    public void testAvailableMoves4() {
        int board[][];
        GameAction expectedResult[];
        board = new int[][]{
            {1, 0, 0, 0},
            {2, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT, GameAction.DOWN});

        board = new int[][]{
            {1, 2, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 1, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 0, 2},
            {0, 0, 0, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.DOWN});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 2},
            {0, 0, 0, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.UP});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 2, 1},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.UP});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 1, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT, GameAction.UP});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 0, 0, 0},
            {2, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT, GameAction.UP});
    }

    @Test
    public void testAvailableMoves5() {
        int board[][];
        GameAction expectedResult[];

        board = new int[][]{
            {1, 0, 0, 0},
            {2, 0, 0, 0},
            {1, 0, 0, 0},
            {2, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.RIGHT});

        board = new int[][]{
            {0, 1, 0, 0},
            {0, 2, 0, 0},
            {0, 1, 0, 0},
            {0, 2, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT, GameAction.RIGHT});

        board = new int[][]{
            {0, 0, 0, 1},
            {0, 0, 0, 2},
            {0, 0, 0, 1},
            {0, 0, 0, 2},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.LEFT});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.UP});

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.UP, GameAction.DOWN});

        board = new int[][]{
            {1, 2, 1, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(GameModel.getAvailableMoves(GameModel.encode(board)), new GameAction[]{GameAction.DOWN});
    }

    @Test
    public void testMoveLeft() {
        int board[][], expectedBoard[][];
        board = new int[][]{
            {1, 0, 0, 1},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {10, 10, 6, 0},};

        expectedBoard = new int[][]{
            {2, 0, 0, 0},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {11, 6, 0, 0},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.LEFT).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.LEFT).getKey(), (Integer) (4 + 2048));

        board = new int[][]{
            {1, 1, 2, 3},
            {2, 0, 2, 2},
            {4, 4, 4, 4},
            {5, 4, 4, 5},};
        expectedBoard = new int[][]{
            {2, 2, 3, 0},
            {3, 2, 0, 0},
            {5, 5, 0, 0},
            {5, 5, 5, 0},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.LEFT).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.LEFT).getKey(), (Integer) (4 + 8 + 64 + 32));
    }

    @Test
    public void testMoveRight() {
        int board[][], expectedBoard[][];
        board = new int[][]{
            {1, 0, 0, 1},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {10, 10, 6, 0},};

        expectedBoard = new int[][]{
            {0, 0, 0, 2},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {0, 0, 11, 6},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.RIGHT).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.RIGHT).getKey(), (Integer) (4 + 2048));

        board = new int[][]{
            {1, 1, 2, 3},
            {2, 0, 2, 2},
            {4, 4, 4, 4},
            {5, 4, 4, 5},};
        expectedBoard = new int[][]{
            {0, 2, 2, 3},
            {0, 0, 2, 3},
            {0, 0, 5, 5},
            {0, 5, 5, 5},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.RIGHT).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.RIGHT).getKey(), (Integer) (4 + 8 + 64 + 32));
    }

    @Test
    public void testMoveUp() {
        int board[][], expectedBoard[][];
        board = new int[][]{
            {1, 0, 0, 1},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {10, 10, 6, 0},};

        expectedBoard = new int[][]{
            {1, 4, 3, 2},
            {2, 6, 5, 2},
            {1, 10, 6, 0},
            {10, 0, 0, 0},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.UP).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.UP).getKey(), (Integer) (4));

        board = new int[][]{
            {1, 2, 4, 5},
            {1, 0, 4, 4},
            {2, 2, 4, 4},
            {3, 2, 4, 5},};

        expectedBoard = new int[][]{
            {2, 3, 5, 5},
            {2, 2, 5, 5},
            {3, 0, 0, 5},
            {0, 0, 0, 0},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.UP).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.UP).getKey(), (Integer) (4 + 8 + 64 + 32));
    }

    @Test
    public void testMoveDown() {
        int board[][], expectedBoard[][];
        board = new int[][]{
            {1, 0, 0, 1},
            {2, 4, 3, 1},
            {1, 6, 5, 2},
            {10, 10, 6, 0},};

        expectedBoard = new int[][]{
            {1, 0, 0, 0},
            {2, 4, 3, 0},
            {1, 6, 5, 2},
            {10, 10, 6, 2},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.DOWN).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.DOWN).getKey(), (Integer) (4));

        board = new int[][]{
            {1, 2, 4, 5},
            {1, 0, 4, 4},
            {2, 2, 4, 4},
            {3, 2, 4, 5},};

        expectedBoard = new int[][]{
            {0, 0, 0, 0},
            {2, 0, 0, 5},
            {2, 2, 5, 5},
            {3, 3, 5, 5},};

        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.DOWN).getValue(), (Long) GameModel.encode(expectedBoard));
        Assert.assertEquals(GameModel.slideTiles(GameModel.encode(board), GameAction.DOWN).getKey(), (Integer) (4 + 8 + 64 + 32));
    }

    @Test
    public void testPrint() {
        int board[][] = new int[][]{
            {1, 5, 14, 13},
            {7, 10, 8, 12},
            {0, 6, 2, 3},
            {15, 9, 11, 4}
        };
        String expectedResult = "1\t5\t14\t13\n7\t10\t8\t12\n0\t6\t2\t3\n15\t9\t11\t4\n";
        Assert.assertEquals(expectedResult, GameModel.print(GameModel.encode(board)));
    }
}
