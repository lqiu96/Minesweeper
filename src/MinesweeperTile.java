import javax.swing.*;
import java.awt.*;

public class MinesweeperTile extends JPanel {
    private boolean bomb, firstButton, beenChecked, rightButton;
    private int numberSurroundingBombs, row, col;
    private JLabel numberBombs;

    /**
     * Initializes the tile for the board
     *
     * @param hasBomb Does a tile have a bomb
     * @param r Which row it is in
     * @param c Which column it is in
     */
    public MinesweeperTile(boolean hasBomb, int r, int c) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        numberBombs = new JLabel("");
        numberBombs.setHorizontalAlignment(SwingConstants.CENTER);
        numberBombs.setVerticalAlignment(SwingConstants.CENTER);
        add(numberBombs, BorderLayout.CENTER);
        setBackground(Color.GRAY);
        bomb = hasBomb;
        rightButton = false;
        firstButton = false;
        beenChecked = false;
        numberSurroundingBombs = 0;
        row = r;
        col = c;
    }

    /**
     * Gives back which row tile is in
     *
     * @return Row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Gives back which column tile is in
     *
     * @return Column number
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets if a tile has been checked or not
     *
     * @param checked Whether a tile has been checked or not
     */
    public void setBeenChecked(boolean checked) {
        beenChecked = checked;
    }

    /**
     * Gives back if tile has been checked
     *
     * @return Whether tile has been checked
     */
    public boolean getBeenChecked() {
        return beenChecked;
    }

    /**
     * How many bombs are next to it
     *
     * @return Number of surrounding bombs
     */
    public int getNumberSurroundingBombs() {
        return numberSurroundingBombs;
    }

    /**
     * If it is the right button
     *
     * @return Whether tile has been flagged or not
     */
    public boolean getRightButton() {
        return rightButton;
    }

    /**
     * Sets the right button to the opposite of the button
     * If already clicked, nothing happens
     *
     * @param button What mode it is in
     */
    public void isFirstButton(boolean button) {
        rightButton = !button;
        if (!firstButton)           //If not already clicked, change to clicked
            firstButton = button;   //If clicked, nothing happens
    }

    /**
     * If the is the first button
     *
     * @return FirstButton
     */
    public boolean getFirstButton() {
        return firstButton;
    }

    /**
     * Sets number of surrounding bombs
     *
     * @param n Number of surrounding bombs
     */
    public void setNumberSurroundingBombs(int n) {
        numberSurroundingBombs = n;
    }

    /**
     * If the tile has the bomb
     *
     * @return Whether the tile has the bomb or not
     */
    public boolean hasBomb() {
        return bomb;
    }

    /**
     * Displays a tile based on the number of bombs there are
     * Green- No bombs near by
     * Red- There is a bomb near by
     * Pink- Bomb
     * Blue- Flagged
     */
    public void draw() {
        if (!firstButton) {
            setBackground(Color.BLUE);
        }
        if (firstButton || beenChecked) {
            if (!bomb) {
                if (numberSurroundingBombs == 0)
                    setBackground(Color.GREEN);
                else
                    setBackground(Color.RED);
                numberBombs.setText((numberSurroundingBombs == 0 ? "" : numberSurroundingBombs + ""));
            } else
                setBackground(Color.PINK);
        }
    }
}