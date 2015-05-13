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

        initializeMenu();

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

    /**
     * Create the Menu Bar that gives the users more options for Minesweeper
     * Two menu options- "Game" and "Options" to choose from
     * Game allows the user to choose the size of the Minesweeper field from 5 options:
     * Beginner through Expert- Increasing in difficulty
     * Default which is what the user first seas
     * Custom- User chooses what option they want
     * Options simply displays the Instructions
     */
    private void initializeMenu() {
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
    }

    /**
     * Stops the timer from continuing
     */
    public void stopTimer() {
        timer.stop();
    }

    /**
     * Displays the hidden tiles and stops the timer
     */
    public void gameOver() {
        minesweeperPanel.revealBoard();
        timer.stop();
    }

    /**
     * Displays how many mines are left that user has to find
     * @param n Number of mines left
     */
    public void setMinesLeft(int n) {
        if (n >= 0)
            mines = n;
        minesLeft.setText("Mines Left: " + mines);
    }

    /**
     * Simples starts the timer. Essentially starts the round
     */
    public void start() {
        timer.start();
    }

    /**
     * Restarts the timer. Such as when a round has finished and a new one begins
     */
    public void resetTimer() {
        seconds = 0;
    }

    /**
     * Sets the board to match the mode that the user selects
     * Based on from Beginner to Expert, Default and Custom
     * Maximum Row and Columns user can choose in Custom Mode is 25
     */
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

    /**
     * Displays the instructions for the User
     */
    private class InstructionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String message = "It's just like normal Minesweeper\n" +
                    "Right-Click to mark bombs\n" +
                    "Choose level or difficulty or choose your own!";
            JOptionPane.showMessageDialog(null, message, "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Starts the the round by intializing the timer
     */
    private class HandleStart implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            minesweeperPanel.restart();
            resetTimer();
            timer.start();
        }
    }

    /**
     * Displays the time
     */
    private class TimerHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            seconds++;
            time.setText("Time: " + seconds);
        }
    }
}
