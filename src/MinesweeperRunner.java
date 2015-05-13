import javax.swing.*;
import java.awt.*;

public class MinesweeperRunner {
    /**
     * @author Lawrence Qiu
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Minesweeper");
        Toolkit tk = f.getToolkit();
        Dimension d = tk.getScreenSize();
        f.setBounds(100, 100, d.width - 200, d.height - 200);   //Create Frame that is almost Fullscreen
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = f.getContentPane();
        f.setLayout(new BorderLayout());

        MinesweeperControlPanel controlPanel = new MinesweeperControlPanel();
        pane.add(controlPanel);
        f.setVisible(true);
    }
}