import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MinesweeperControlPanel extends JPanel {
    private MinesweeperPanel minesweeperPanel;
    private JLabel time, minesLeft;
    private javax.swing.Timer timer;
    private int seconds, mines;

    public MinesweeperControlPanel() {
        setLayout(new BorderLayout());
        minesweeperPanel = new MinesweeperPanel(this);

        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);
        JMenu game = new JMenu("Game");
        game.setMnemonic(KeyEvent.VK_G);
        JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_O);

        JMenuItem standard = game.add("Default");
        standard.addActionListener(new ModeHandler());
        standard.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));

        JMenuItem custom = game.add("Custom");
        custom.addActionListener(new ModeHandler());
        custom.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));

        game.addSeparator();
        JMenuItem beginner = game.add("Beginner");
        beginner.addActionListener(new ModeHandler());
        beginner.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));

        JMenuItem intermediate = game.add("Intermediate");
        intermediate.addActionListener(new ModeHandler());
        intermediate.setAccelerator(KeyStroke.getKeyStroke('I', InputEvent.CTRL_DOWN_MASK));

        JMenuItem expert = game.add("Expert");
        expert.addActionListener(new ModeHandler());
        expert.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));
        game.addSeparator();

        JMenuItem instructions = options.add("Instructions");
        instructions.addActionListener(new InstructionHandler());
        instructions.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));

        menuBar.add(game);
        menuBar.add(options);


        seconds = 0;
        time = new JLabel("Time: " + seconds);
        time.setPreferredSize(new Dimension(200, 30));
        minesLeft = new JLabel("Mines Left: " + mines);
        minesLeft.setPreferredSize(new Dimension(200, 30));
        JButton restart = new JButton("Restart");
        restart.setMnemonic(KeyEvent.VK_R);
        restart.addActionListener(new HandleStart());
        timer = new javax.swing.Timer(1000, new TimerHandler());
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(1, 3));
        jp.setBackground(Color.GREEN);
        jp.setPreferredSize(new Dimension(700, 30));
        jp.add(minesLeft);
        jp.add(restart);
        jp.add(time);
        add(jp, BorderLayout.SOUTH);
        add(minesweeperPanel, BorderLayout.CENTER);
    }

    public void stopTimer() {
        timer.stop();
    }

    public void gameOver() {
        minesweeperPanel.revealBoard();
        timer.stop();
    }

    public void setMinesLeft(int n) {
        if (n >= 0)
            mines = n;
        minesLeft.setText("Mines Left: " + mines);
    }

    public void start() {
        timer.start();
    }

    public void resetTimer() {
        seconds = 0;
    }

    private class ModeHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JMenuItem menu = (JMenuItem) e.getSource();
            if (menu.getText().equals("Default"))
                minesweeperPanel.setDefaultMode();
            else if (menu.getText().equals("Beginner"))
                minesweeperPanel.setBeginnerMode();
            else if (menu.getText().equals("Intermediate"))
                minesweeperPanel.setIntermediateMode();
            else if (menu.getText().equals("Expert"))
                minesweeperPanel.setExpertMode();
            else {
                JTextField rows = new JTextField(3);
                JTextField cols = new JTextField(3);

                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Rows: "));
                myPanel.add(rows);
                myPanel.add(Box.createHorizontalStrut(10));
                myPanel.add(new JLabel("Cols: "));
                myPanel.add(cols);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Enter rows and columns (Max: 25)", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    minesweeperPanel.setCustomMode((Integer.parseInt(rows.getText()) > 25 ? 25 : Integer.parseInt(rows.getText())),
                            (Integer.parseInt(cols.getText()) > 25 ? 25 : Integer.parseInt(cols.getText())));
                }
            }
        }
    }

    private class InstructionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String message = "It's just like normal Minesweeper\nRight-Click to mark bombs\nChoose level or difficulty or choose your own!";
            JOptionPane.showMessageDialog(null, message, "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class HandleStart implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            minesweeperPanel.restart();
            resetTimer();
            timer.start();
        }
    }

    private class TimerHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            seconds++;
            time.setText("Time: " + seconds);
        }
    }
}
