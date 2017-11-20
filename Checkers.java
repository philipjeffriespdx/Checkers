 import javax.swing.*;
import java.awt.*;

public class Checkers extends JFrame {
	public static final int rows = 8, columns = 8;
	// private JPanel master = new JPanel(new BorderLayout());
	private JPanel board;
	// private Container contents;
	private JButton reset = new JButton("Reset");
	private JPanel buttonPanel;
	private JButton button;
	private JLabel timer = new JLabel("Timer");
	private JPanel[][] squares;
	public JToolBar toolbar;

	public Checkers() {
		buttonPanel = new JPanel();
		buttonPanel.setSize(getWidth(), 800);

		JToolBar tools = new JToolBar();
		tools.add(reset);
		tools.addSeparator();
		tools.add(timer);
		tools.setFloatable(false);

		button = new JButton("Button 2 (CENTER)");
		squares = new JPanel[rows][columns];
		board = new JPanel();
		board.setLayout(new GridLayout(rows, columns));

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				squares[i][j] = new JPanel();
				squares[i][j].setBackground(Color.DARK_GRAY);
				if ((i + j) % 2 == 0) {
					squares[i][j].setBackground(Color.LIGHT_GRAY);
				}
				JButton b = new JButton();
				squares[i][j].add(b, BorderLayout.CENTER);
				board.add(squares[i][j]);
			}
		}
		add(board);
		buttonPanel.add(button);
		add(buttonPanel, BorderLayout.SOUTH);
		button.setSize(100, 100);
		setSize(800, 800);
		setVisible(true);
	}

	public static void main(String[] args) {
		Checkers game = new Checkers();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}