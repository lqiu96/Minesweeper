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

    public void setDefaultMode() {
        controlPanel.stopTimer();
        numRows = 10;
        numCols = 10;
        restart();
    }

    public void setCustomMode(int r, int c) {
        controlPanel.stopTimer();
        numRows = r;
        numCols = c;
        restart();
    }

    public void setBeginnerMode() {
        controlPanel.stopTimer();
        numRows = 8;
        numCols = 8;
        restart();
    }

    public void setIntermediateMode() {
        controlPanel.stopTimer();
        numRows = 16;
        numCols = 16;
        restart();
    }

    public void setExpertMode() {
        controlPanel.stopTimer();
        numRows = 16;
        numCols = 31;
        restart();
    }

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
//
//  public void restart(){
//    otherTiles.clear();
//    this.removeAll();
//    tiles = new MinesweeperTile[10][10];
//    for(int r = 0; r < tiles.length; r++){
//      for(int c = 0; c < tiles[r].length; c++){
//        tiles[r][c] = new MinesweeperTile((Math.random() < 0.2) ? true : false, r, c);
//        add(tiles[r][c]);
//      }
//    }
//    for(int r = 0; r < tiles.length; r++){
//      for(int c = 0; c < tiles[r].length; c++){
//        int n = findNumSurroundingBombs(r,c);
//        tiles[r][c].setNumberSurroundingBombs(n);
//      }
//    }
//    running = false;
//    this.updateUI();
//	findTotalMines();
//    controlPanel.setMinesLeft(minesLeft);
//  }

    public void revealBoard() {
        for (MinesweeperTile[] tile : tiles) {
            for (int c = 0; c < tile.length; c++) {
                tile[c].isFirstButton(true);
                tile[c].draw();
            }
        }
        running = false;
    }

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

    private void findTotalMines() {
        minesLeft = 0;
        for (MinesweeperTile[] msTile : tiles) {
            for (MinesweeperTile aMsTile : msTile) {
                if (aMsTile.hasBomb())
                    minesLeft++;
            }
        }
    }

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
