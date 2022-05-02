package io;

import controller.GameController;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;
import game.GameAction;
import game.GameModel;

/**
 * The graphical user interface for this application
 * @author Jiang Han
 */
public final class GraphicalUI extends JPanel implements ActionListener, KeyListener, MouseListener {

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 600;
    public static final int BOARD_ANCHOR_X = 0;
    public static final int BOARD_ANCHOR_Y = 100;
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;
    private static final int MARGIN = 10;
    private static final int TILE_LEN = (BOARD_WIDTH - 5 * MARGIN) / 4;

    //Stores whether the tile of a particular value has been seen or not
    private final boolean[] hasSeen;
    
    private Timer timer;

    //Info button attributes
    private int infoButtonCenterX;
    private final int infoButtonCenterY = 60, infoButtonRadius = 20;
    Image infoIcon;

    //Queues the list of objects to be drawn in one painting. Can be added or taken out concurrently
    private final ConcurrentLinkedQueue<List<Tile>> tileListQueue = new ConcurrentLinkedQueue<>();

    private GameController controller;
    private int score = 0;
    
    //Score increment attributes
    private int scoreIncrement = 0;
    private double incrementPos = 0;
    private int incrementAlpha = 0;
    private static final Map<Integer, Color> COLOR_LIST = new HashMap<>();

    static {
        COLOR_LIST.put(0, new Color(205, 193, 180));
        COLOR_LIST.put(2, new Color(121, 183, 172)); //kolom 1 
        COLOR_LIST.put(4, new Color(255, 255, 186));
        COLOR_LIST.put(8, new Color(214, 181, 208));
        COLOR_LIST.put(16, new Color(251, 223, 235));
        COLOR_LIST.put(32, new Color(188, 221, 212)); //kolom 2
        COLOR_LIST.put(64, new Color(238, 224, 221));
        COLOR_LIST.put(128, new Color(203, 227, 195));
        COLOR_LIST.put(256, new Color(247, 183, 210));
        COLOR_LIST.put(512, new Color(198, 225, 206)); //kolom 3
        COLOR_LIST.put(1024, new Color(249, 243, 229));
        COLOR_LIST.put(2048, new Color(214, 228, 143));
        COLOR_LIST.put(4096, new Color(162, 218, 219));
        COLOR_LIST.put(8192, new Color(224, 224, 226)); //kolom 4
        COLOR_LIST.put(16384, new Color(235, 148, 157));
        COLOR_LIST.put(32768, new Color(202, 233, 235));
        COLOR_LIST.put(65536, new Color(243, 202, 218));
    }
    
    private boolean isGameOver;

    public GraphicalUI() {
        hasSeen = new boolean[20];
        //Assumes that empty tile, 2, 4, and 8 tile has appeared before
        for (int i = 0; i <= 3; i++) {
            hasSeen[i] = true;
        }

        //Loads the info icon image
        URL infoIconUrl = getClass().getClassLoader().getResource("images/information-icon.png");
        if (infoIconUrl == null) {
            System.out.println(infoIconUrl);
            System.err.println("Couldn't find info icon");
            System.exit(-1);
        } else {
            try {
                infoIcon = ImageIO.read(infoIconUrl);
            } catch (IOException ex) {
                System.err.println("Image icon load error");
            }
        }
    }

    /**
     * Displays a game state with the transitions leading to this state.
     * Queues several list of tiles to be drawn into tileListQueue.
     * @param state The game state
     */
    public void displayBoard(GameModel.GameState state) {
        int newScore = state.getScore();
        if (newScore != score) {
            if (newScore > 0) {
                scoreIncrement = newScore - score;
                incrementAlpha = 255;
                incrementPos = 0;
            }
            this.score = newScore;
        }

        //final board state
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < state.getSize(); i++) {
            for (int j = 0; j < state.getSize(); j++) {
                tiles.add(new Tile(BOARD_ANCHOR_X + MARGIN * (j + 1) + j * TILE_LEN, BOARD_ANCHOR_Y + MARGIN * (i + 1) + i * TILE_LEN, COLOR_LIST.get(state.getCellValue(i, j)), TILE_LEN, state.getCellValue(i, j)));
            }
        }

        tileListQueue.add(tiles);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(187, 173, 160));
        Graphics2D g2d = (Graphics2D) g;
        Map<?, ?> desktopHints = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        if (desktopHints != null) {
            g2d.setRenderingHints(desktopHints);
        }

        //Title
        Font font = new Font("SansSerif", Font.BOLD, 48);
        g2d.setFont(font);
        FontMetrics fontMetric = g2d.getFontMetrics();
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("2048", MARGIN, (int) Math.round(50 + fontMetric.getAscent() / 2.0));

        //Info button
        infoButtonCenterX = 2 * MARGIN + fontMetric.stringWidth("2048") + infoButtonRadius;
        g2d.drawImage(infoIcon, infoButtonCenterX - infoButtonRadius, infoButtonCenterY - infoButtonRadius, 2 * infoButtonRadius, 2 * infoButtonRadius, null);

        //Score Label
        String scoreLabel = "Score: ";
        font = new Font("SansSerif", Font.PLAIN, 24);
        g2d.setFont(font);
        fontMetric = g2d.getFontMetrics();
        int labelLen = fontMetric.stringWidth(scoreLabel);
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreLabel, 250, (int) Math.round(50 + fontMetric.getAscent() / 2.0));

        //Score
        font = new Font("SansSerif", Font.BOLD, 36);
        g2d.setFont(font);
        fontMetric = g2d.getFontMetrics();
        String scoreValue = Integer.toString(score);
        g2d.drawString(scoreValue, 250 + labelLen, (int) Math.round(50 + fontMetric.getAscent() / 2.0));
        int labelScoreLen = fontMetric.stringWidth(scoreValue);

        //Score Increment
        if (incrementAlpha > 0) {
            font = new Font("SansSerif", Font.BOLD, 24);
            g2d.setFont(font);
            fontMetric = g2d.getFontMetrics();
            String incrementLabel = "+" + scoreIncrement;

            g2d.setColor(new Color(255, 223, 61, incrementAlpha)); //orange emas
            g2d.drawString(
                    incrementLabel,
                    250 + labelLen + labelScoreLen,
                    (int) Math.round(50 + fontMetric.getAscent() / 2.0 + incrementPos));

            incrementAlpha -= 6;
            incrementPos -= 0.8;
        }

        if (!tileListQueue.isEmpty()) {
            List<Tile> tiles = tileListQueue.peek();
            for (Tile tile : tiles) {
                tile.draw(g2d);
            }

            if (tileListQueue.size() > 1) {
                tileListQueue.poll();
            }
        }

        if (isGameOver) {
            font = new Font("SansSerif", Font.BOLD, 28);
            g2d.setFont(font);
            fontMetric = g2d.getFontMetrics();
            String gameoverLabel = "Game Over";
            g2d.setColor(Color.RED);
            g2d.drawString(
                    gameoverLabel,
                    (int) Math.round(SCREEN_WIDTH / 2.0 - fontMetric.stringWidth(gameoverLabel) / 2.0),
                    (int) Math.round(95));
        }
    }
    
    public void showGameOver() {
        isGameOver = true;
    }

    //Starts the game
    public void start(GameModel.GameState state, GameController controller) {
        addKeyListener(this);
        addMouseListener(this);
        this.controller = controller;

        displayBoard(state);
        timer = new Timer(10, this);
        timer.start();
    }

    //Restarts the game
    public void restart(GameModel.GameState initialState) {
        isGameOver = false;
        displayBoard(initialState);
    }

    //Displays the Help dialog box
    private void showHelp() {
        JOptionPane.showMessageDialog(null,
                "Gunakan panah di keyboard untuk menggerakkan tile.\n"
                + "Tekan tombol R untuk me-restart game.\n"
                + "Tekan icon i atau tombol H untuk membuka Help.",
                "2048 - Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        while (tileListQueue.size() > 1) {
            tileListQueue.poll();
        }
        switch (e.getKeyCode()) {
            case 38: //up
                controller.moveBoard(GameAction.UP);
                break;
            case 40: //down
                controller.moveBoard(GameAction.DOWN);
                break;
            case 37: //left
                controller.moveBoard(GameAction.LEFT);
                break;
            case 39: //right
                controller.moveBoard(GameAction.RIGHT);
                break;
            case 72: //H (Help)
                showHelp();
                break;
            case 82: //R (restart)
                controller.restartGame();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int dist = (int) Math.round(Math.sqrt(Math.pow(infoButtonCenterX - x, 2) + Math.pow(infoButtonCenterY - y, 2)));
        if (dist <= infoButtonRadius) {
            showHelp();
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }
}
