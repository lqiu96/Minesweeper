import javax.swing.*;
import java.awt.*;

public class MinesweeperTile extends JPanel {
    private boolean bomb, firstButton, beenChecked, rightButton;
    private int numberSurroundingBombs, row, col;
    private JLabel numberBombs;

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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setBeenChecked(boolean checked) {
        beenChecked = checked;
    }

    public boolean getBeenChecked() {
        return beenChecked;
    }

    public int getNumberSurroundingBombs() {
        return numberSurroundingBombs;
    }

    public boolean getRightButton() {
        return rightButton;
    }

    public void isFirstButton(boolean button) {
        rightButton = !button;
        if (!firstButton)           //If not already clicked, change to clicked
            firstButton = button;   //If clicked, nothing happens
    }

    public boolean getFirstButton() {
        return firstButton;
    }

    public void setNumberSurroundingBombs(int n) {
        numberSurroundingBombs = n;
    }

    public boolean hasBomb() {
        return bomb;
    }

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