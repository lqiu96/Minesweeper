import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MinesweeperPanel extends JPanel {
    private MinesweeperControlPanel controlPanel;
    private MinesweeperTile[][] tiles;
    private ArrayList<MinesweeperTile> otherTiles;
    private GridLayout grid;
    private boolean running;
    private int minesLeft = 0, r = 0, c = 0;
    private int numRows, numCols;

    /**
     * Initializes the board and populates it with tiles randomly filled with bombs
     *
     * @param controlPanel Control Panel that monitors the game
     */
    public MinesweeperPanel(MinesweeperControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        numRows = 10;
        numCols = 10;
        tiles = new MinesweeperTile[numRows][numCols];
        grid = new GridLayout();
        grid.setRows(numRows);
        grid.setColumns(numCols);
        setLayout(grid);
        running = false;
        otherTiles = new ArrayList<MinesweeperTile>();
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                tiles[r][c] = new MinesweeperTile((Math.random() < 0.2), r, c);
                add(tiles[r][c]);
            }
        }
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                int n = findNumSurroundingBombs(r, c);
                tiles[r][c].setNumberSurroundingBombs(n);
            }
        }
        findTotalMines();
        addMouseListener(new MouseHandler());
    }

    /**
     * Default mode restarts the game
     * Number of rows and columns are both set to 10
     */
    public void setDefaultMode() {
        controlPanel.stopTimer();
        numRows = 10;
        numCols = 10;
        restart();
    }

    /**
     * Default mode restarts the game
     * Number of rows and columns depends on user input
     * @param r Number of rows
     * @param c Number of columns
     */
    public void setCustomMode(int r, int c) {
        controlPanel.stopTimer();
        numRows = r;
        numCols = c;
        restart();
    }

    /**
     * Beginner mode restarts the game
     * Number of rows and columns are both set to 8
     */
    public void setBeginnerMode() {
        controlPanel.stopTimer();
        numRows = 8;
        numCols = 8;
        restart();
    }

    /**
     * Intermediate Mode restarts the game
     * Number of Rows and Columns set to 16
     */
    public void setIntermediateMode() {
        controlPanel.stopTimer();
        numRows = 16;
        numCols = 16;
        restart();
    }

    /**
     * Expert Mode restarts the game
     * Number of Rows is 16
     * Number of Columns is 31
     */
    public void setExpertMode() {
        controlPanel.stopTimer();
        numRows = 16;
        numCols = 31;
        restart();
    }

    /**
     * Clears the game and removes the board
     * Board is then repopulated with tiles randomly filled with bombs and added into the board
     */
    public void restart() {
        otherTiles.clear();
        this.removeAll();
        grid.setRows(numRows);
        grid.setColumns(numCols);
        tiles = new MinesweeperTile[numRows][numCols];
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                tiles[r][c] = new MinesweeperTile((Math.random() < 0.2), r, c);
                add(tiles[r][c]);
            }
        }
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                int n = findNumSurroundingBombs(r, c);
                tiles[r][c].setNumberSurroundingBombs(n);
            }
        }
        running = false;
        controlPanel.resetTimer();
        this.updateUI();
        findTotalMines();
        controlPanel.setMinesLeft(minesLeft);
    }

    /**
     * Displays the board if it is not already seen by the user
     * Game is not running if board is revealed
     */
    public void revealBoard() {
        for (MinesweeperTile[] tile : tiles) {
            for (MinesweeperTile aTile : tile) {
                aTile.isFirstButton(true);
                aTile.draw();
            }
        }
        running = false;
    }

    /**
     * For each row and column, it calculates the number of surrounding areas and counts the number
     * of bombs each of them have
     * @param r Row to look at
     * @param c Column to look at
     * @return Number of bombs surrounding rows have
     */
    private int findNumSurroundingBombs(int r, int c) {
        int count = 0;
        for (int x = ((r == 0) ? 0 : r - 1); x < ((r == tiles.length - 1) ? tiles.length : r + 2); x++) {
            for (int y = ((c == 0) ? 0 : c - 1); y < ((c == tiles[r].length - 1) ? tiles[r].length : c + 2); y++) {
                if (tiles[x][y].hasBomb())
                    count++;
            }
        }
        return count;
    }

    /**
     * Searches through the entire board anc oounts the number of mines left
     */
    private void findTotalMines() {
        minesLeft = 0;
        for (MinesweeperTile[] msTile : tiles) {
            for (MinesweeperTile aMsTile : msTile) {
                if (aMsTile.hasBomb())
                    minesLeft++;
            }
        }
    }

    /**
     * Recursively searches through the board to find spots where there are no mines and
     * reveals them. It allows the the board to largely expand when there are no nearby bombs
     * @param r Row to look at
     * @param c Column to look at
     */
    private void checkOtherTiles(int r, int c) {
        if (tiles[r][c].hasBomb())
            return;
        else if (tiles[r][c].getNumberSurroundingBombs() != 0) {
            if (tiles[r][c].getRightButton() && !tiles[r][c].getBeenChecked())
                minesLeft++;
            tiles[r][c].setBeenChecked(true);
            otherTiles.add(tiles[r][c]);
            return;
        } else if (tiles[r][c].getBeenChecked())
            return;
        for (MinesweeperTile msTile : otherTiles) {
            if (msTile.getRow() == r && msTile.getCol() == c)
                return;
        }
        if (tiles[r][c].getRightButton() && !tiles[r][c].getBeenChecked())
            minesLeft++;
        tiles[r][c].setBeenChecked(true);
        otherTiles.add(tiles[r][c]);
        if (r >= 1)
            checkOtherTiles(r - 1, c);
        if (c <= tiles[r].length - 2)
            checkOtherTiles(r, c + 1);
        if (c >= 1)
            checkOtherTiles(r, c - 1);
        if (r <= tiles.length - 2)
            checkOtherTiles(r + 1, c);
        if (r >= 1 && c >= 1)
            checkOtherTiles(r - 1, c - 1);
        if (r >= 1 && c <= tiles[r].length - 2)
            checkOtherTiles(r - 1, c + 1);
        if (r <= tiles.length - 2 && c >= 1)
            checkOtherTiles(r + 1, c - 1);
        if (r <= tiles.length - 2 && c <= tiles[r].length - 2)
            checkOtherTiles(r + 1, c + 1);
    }

    /**
     * Checks to see if board has been cleared
     * Looks to see if any tile still has the bomb or hasn't been flagged
     */
    private void checkForVictory() {
        for (MinesweeperTile[] tile : tiles) {
            for (MinesweeperTile aTile : tile) {
                if (aTile.hasBomb() && !aTile.getRightButton())
                    return;
            }
        }
        int n = JOptionPane.showConfirmDialog(null, "You have won minesweeper! Do you want to restart?", "Victory!", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION)
            controlPanel.gameOver();
        else
            System.exit(0);
    }

    /**
     * Draws each of the tiles
     * @param g Graphics component
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            tiles[r][c].draw();
            for (MinesweeperTile msTiles : otherTiles) {
                msTiles.setBeenChecked(true);
                msTiles.draw();
            }
            controlPanel.setMinesLeft(minesLeft);
        }
        otherTiles.clear();
    }

    /**
     * Finds the location where each mouse click was pressed down at
     * Each x and y location has a spot on the board and action is the accordingly
     * done to the tile
     */
    private class MouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (!running) {
                controlPanel.start();
                running = true;
            }
            int x = e.getX();
            int y = e.getY();
            r = (int) (y / ((getSize().height * 1.0) / tiles.length));
            c = (int) (x / ((getSize().width * 1.0) / tiles[0].length));
            if (e.getButton() == MouseEvent.BUTTON1) {
                checkOtherTiles(r, c);
                tiles[r][c].isFirstButton(true);
                if (tiles[r][c].hasBomb())
                    controlPanel.gameOver();
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (!tiles[r][c].getRightButton())
                    controlPanel.setMinesLeft(--minesLeft);
                tiles[r][c].isFirstButton(false);
            }
            if (minesLeft == 0)
                checkForVictory();
            repaint();
        }
    }
}