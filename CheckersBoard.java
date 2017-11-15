/*So I was thinking we can create a base JFrame that has no buttons and it is just squares that alternate Red and Black.
   Then we can set buttons ontop of this GUI that are color-less and has those act as the pieces. ie, click A1 then click on B2 to move a piece.*/
   public class CheckersBoard extends JFrame
   {
      private Container contents;
      private JButton [][] squares;
      int sides = 8;
      
      public void PaintBoard()
      {
         //super( "CHECKERS!" );
         contents = getContentPane( );

         // set layout to an 8-by-8 Grid
         contents.setLayout( new GridLayout( sides, sides ) );

         squares = new JButton[sides][sides];

         ButtonHandler bh = new ButtonHandler( );
         setSize( 800, 800 );
         setVisible( true );
      }
      
      private class ButtonHandler implements ActionListener
      {
         public void actionPerformed( ActionEvent ae )
         {
            for ( int i = 0; i < sides; i++ )
             {
               for ( int j = 0; j < sides; j++ )
               {
                 if (sides==8)//place holder
                 {
                   
                   return;
                 }
               }
             }
         }
      }
   }
   
