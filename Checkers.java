import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.awt.Color;
import java.awt.Graphics;

public class Checkers extends JFrame 
{
	public static final int rows = 8, columns = 8;
	private JPanel board;
	private JButton reset = new JButton("Reset");
	private JPanel buttonPanel;
   private JTextField entry;
	private JLabel timer;
	private JPanel[][] squares;
   private Color lightBrown, darkBrown;
   private JButton [][] playerOnePieces = new JButton[3][4];
   private JButton [][] playerTwoPieces = new JButton[3][4];
   
	public JToolBar toolbar;

	public Checkers() 
   {
		buttonPanel = new JPanel();
		buttonPanel.setSize(getWidth(), 800);
      
      Color lightBrown = new Color(250, 229, 182);
      Color darkBrown = new Color(120, 84, 7);
      
      TextFieldListener tfl = new TextFieldListener();
      timer = new JLabel("Timer");
      
		JToolBar tools = new JToolBar();
		tools.add(reset);
		tools.addSeparator();
		tools.add(timer);
		tools.setFloatable(true);
      
      entry = new JTextField(10);
      entry.addActionListener(tfl);

		squares = new JPanel[rows][columns];
		board = new JPanel();
		board.setLayout(new GridLayout(rows, columns));

		for (int i = 0; i < rows; i++) 
      {
			for (int j = 0; j < columns; j++) 
         {
				squares[i][j] = new JPanel();
				squares[i][j].setBackground(lightBrown);
   				if ((i + j) % 2 == 0) 
               {
   					squares[i][j].setBackground(darkBrown);
   				}
				JButton piece = new JButton();
            piece.setPreferredSize(new Dimension(35,35));
				squares[i][j].add(piece, BorderLayout.CENTER);
            
				board.add(squares[i][j]);
			}
		}
		add(board);
		buttonPanel.add(tools);
      
      buttonPanel.add(entry);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setSize(800, 800);
		setVisible(true);
	}
  

   private class TextFieldListener implements ActionListener
   {  
      public void actionPerformed(ActionEvent a)
      {
         String move = entry.getText();
         char dash = '-';
         int countOfDash=0;
         
         for (int i = 0; i < move.length();i++)
         {
            //Find out how many dashes are there to determine length of run time
            if (move.charAt(i) == dash)
            {
               countOfDash++;
            } 
         }
         //For every 1 dash there are two source/destination. So E5-D5, one dash, two things
         //For every 2 dashes there are 3 sources. E5-D6-G7
         System.out.println("I recognized "+countOfDash+" hops.");
         String sourceMove="", destinationMove="", hop1="", hop2="", hop3="";
         switch (countOfDash)
         {
            case 1:  sourceMove = move.substring(0,move.indexOf(dash));
                     destinationMove = move.substring(move.length()-2,move.length());
               break;
            case 2:  sourceMove = move.substring(0,move.indexOf(dash));
                     hop1 = move.substring(move.indexOf(dash)+1,move.length()-3);
                     destinationMove = move.substring(move.length()-2,move.length());
               break;
            case 3: sourceMove = move.substring(0,move.indexOf(dash));
                    hop1 = move.substring(move.indexOf(dash),move.length()-2);
               break;
            case 4: System.out.println("Four dash");
               break;
            case 5: System.out.println("Five dash");
               break;
         }
                     
         
         
         
         System.out.println("Original Input: "+move);
         //System.out.println("Starting position: "+ sourceMove);
         //System.out.println("Destination position: "+ destinationMove);
         
      }
   }



	public static void main(String[] args) 
   {
		Checkers game = new Checkers();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}