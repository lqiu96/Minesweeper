import javax.swing.*;
import java.awt.*;

public class MinesweeperRunner {
    public static void main(String[] args) {
        JFrame f = new JFrame("Minesweeper");
        Toolkit tk = f.getToolkit();
        Dimension d = tk.getScreenSize();
        f.setBounds(100, 100, d.width - 200, d.height - 200);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = f.getContentPane();
        f.setLayout(new BorderLayout());

        MinesweeperControlPanel mscp = new MinesweeperControlPanel();
        pane.add(mscp);
        f.setVisible(true);
    }
}