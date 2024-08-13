import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFourGUI extends JFrame {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY_SLOT = '-';
    private static final char PLAYER_ONE = 'X';
    private static final char PLAYER_TWO = 'O';

    private char[][] board;
    private boolean isPlayerOneTurn;
    private JButton[] buttons;
    private JLabel[][] slots;
    
    public ConnectFourGUI() {
        board = new char[ROWS][COLUMNS];
        initializeBoard();
        isPlayerOneTurn = true;
        
        setTitle("Connect Four");
        setSize(2000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, COLUMNS));
        buttons = new JButton[COLUMNS];
        for (int i = 0; i < COLUMNS; i++) {
            buttons[i] = new JButton("Drop");
            final int column = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleMove(column);
                }
            });
            buttonPanel.add(buttons[i]);
        }
        add(buttonPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(ROWS, COLUMNS));
        slots = new JLabel[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                slots[i][j] = new JLabel(String.valueOf(EMPTY_SLOT), SwingConstants.CENTER);
                slots[i][j].setFont(new Font("Arial", Font.PLAIN, 24));
                slots[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(slots[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY_SLOT;
            }
        }
    }

    private void handleMove(int column) {
        if (placeMove(column)) {
            updateBoard();
            if (checkWin()) {
                JOptionPane.showMessageDialog(this, "Player " + (isPlayerOneTurn ? "One" : "Two") + " wins!");
                resetGame();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "It's a draw!");
                resetGame();
            } else {
                isPlayerOneTurn = !isPlayerOneTurn;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Column is full! Choose another column.");
        }
    }

    private boolean placeMove(int column) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == EMPTY_SLOT) {
                board[i][column] = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
                return true;
            }
        }
        return false;
    }

    private void updateBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                slots[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                char player = board[i][j];
                if (player != EMPTY_SLOT) {
                    if (checkDirection(i, j, 1, 0, player) ||  // Horizontal
                        checkDirection(i, j, 0, 1, player) ||  // Vertical
                        checkDirection(i, j, 1, 1, player) ||  // Diagonal /
                        checkDirection(i, j, 1, -1, player)) { // Diagonal \
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && board[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }

    private boolean isBoardFull() {
        for (int j = 0; j < COLUMNS; j++) {
            if (board[0][j] == EMPTY_SLOT) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        initializeBoard();
        updateBoard();
        isPlayerOneTurn = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConnectFourGUI game = new ConnectFourGUI();
                game.setVisible(true);
            }
        });
    }
}
