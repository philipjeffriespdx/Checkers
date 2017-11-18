/*
Philip Jeffries
Networking and Security 
Checkers Game
*/
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//There will be a checkers Server and Client
//Server Starts the Game
//SERVER IS THE BLACK PLAYER BECAUSE RED GOES FIRST
//BLACK IS ON THE RIGHT SIDE AND RED IS ON THE LEFT SIDE
class CheckersServer {
   public static int [][] pieces = new int [8][8];
   public static void main(String argv[]) throws Exception {
      //Wait for Checkers Client 
      String clientMove = "", serverMove = "", serverHops = "", clientHops = "";
      char temp;
      int fromCol, fromRow, toCol, toRow, Col, Row, moveLength, numHops, hopedCol, hopedRow, currentPiece;
      //for more than one hop use toRows[] and toCols[]
      boolean valid = false, hop = false, alreadyhoped = false, alreadymoved = false;
        
      //set up socket
      ServerSocket welcomeSocket = new ServerSocket(6789);
   
      //accept traffic from socket
      Socket connectionSocket = welcomeSocket.accept();
      
      //read in from user
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      
      //reading in from client
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      
      //reading out to client
      DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
      
      //AFTER CONNECTION::
      
      //Create Matrix of Peices
      /*
      Empty = 0
      Red Pawn = 1
      Black Pawn = 2
      Red King = 3
      Black King = 4
      */
      // pieces[rows][columns]
      SetBoard();
      PrintBoard();
      
      //continue to receive and send data
      while (true) { 
         //LOOP OF MOVES ::
         //Wait for Client move
         String clientFrom, sNumClientHops, currentClientHop, clientDestination;
         int numClientHops = 0;
         
         System.out.println("Waiting for Client... ");
         //receive original COL " " ROW
         clientFrom = inFromClient.readLine();
         temp = clientFrom.charAt(0);
         fromCol = temp - 48;
         temp = clientFrom.charAt(1);
         fromRow = temp - 48;
         
         outToClient.writeBytes("y" + "\n");
         
         currentPiece = pieces[fromCol][fromRow];
         pieces[fromCol][fromRow] = 0;
         
         //just a number
         sNumClientHops = inFromClient.readLine();
         numClientHops = Integer.valueOf(sNumClientHops);
         
         outToClient.writeBytes("y" + "\n");
         
         //num client hops
         if(numClientHops > 0)
         {
            for(int i = 0; i < numClientHops; i++)
            {
               //receive things that will be hoped COL " " ROW
               clientHops = inFromClient.readLine();
               temp = clientFrom.charAt(0);
               Col = temp - 48;
               temp = clientFrom.charAt(1);
               Row = temp - 48;
               pieces[Col][Row] = 0;
               outToClient.writeBytes("y" + "\n");
            }
         }//end if numClientHops
         
         //pass destination
         clientDestination = inFromClient.readLine();
         temp = clientDestination.charAt(0);
         toCol = temp - 48;
         temp = clientDestination.charAt(1);
         toRow = temp - 48;
         
         outToClient.writeBytes("y" + "\n");
         
         pieces[toCol][toRow] = currentPiece;
         
         
         
         System.out.println("Client Move: " + clientFrom + " to " + clientDestination);
         
         /* this would  be like doing it the same way as imput
         moveLength = clientMove.length();
         moveLength = moveLength - 2;
         numHops = moveLength / 3; 
            
         System.out.println("NumHops is: " + numHops); 
         //get moving piece
         temp = clientMove.charAt(0);
         fromCol = temp - 65;
         temp = clientMove.charAt(1);
         fromRow = temp - 48;
         System.out.println("Server From  : " + fromCol + " " + fromRow);

         currentPiece = pieces[fromCol][fromRow];   
         //create substring
         clientHops = clientMove.substring(2);
               
         //create array of toRow and toCol for number of hops and give them their values
         int [] toRows = new int [numHops+1];
         int [] toCols = new int [numHops+1];
         int [] thingstohopCol = new int [numHops];
         int [] thingstohopRow = new int [numHops];
         toRows[0] = fromRow;
         toCols[0] = fromCol;
            
         int count = 1;
         for(int i = 1; i<numHops+1; i++)
         {
            toCols[i] = clientHops.charAt(count) - 65;
            toRows[i] = clientHops.charAt(count+1) - 48;
            System.out.println("Client to: " + toCols[i] + " " + toRows[i]);
            count = count + 3;
         }
         for(int i = 1; i<numHops+1; i++)
         {
            if(pieces[fromCol][fromRow]==1)
            {
               if(toCols[i]==toCols[i-1]+2 && (toRows[i]==toRows[i-1]-2 || toRows[i]==toRows[i-1]+2) )
               {
                   // col or row of hoped piece: from + (to - from) / 2
                  System.out.println("toCols[i-1] and toCols[i]: " + toCols[i-1] + " " + toCols[i]);
                  System.out.println("toRows[i-1] and toRows[i]: " + toCols[i-1] + " " + toCols[i]);
                  hopedCol = toCols[i-1] + (toCols[i] - toCols[i-1]) / 2;
                  hopedRow = toRows[i-1] + (toRows[i] - toRows[i-1]) / 2;
                  System.out.println("HopedCol and HopedRow: " + hopedCol + " " + hopedRow);
                  //if there is not a piece to hop in the closer square
                  if(pieces[hopedCol][hopedRow]!=1 && pieces[hopedCol][hopedRow]!=3 )
                  {
                     valid = false;
                     System.out.println("Error Pawn: There is no piece in the square you are tyring to hop over");
                     break;
                  }
                  thingstohopCol[i-1] = hopedCol;
                  thingstohopRow[i-1] = hopedRow;
                  alreadyhoped = true;
               } //else if move is (- 1 cols) and (+ or - 1 row)
            }
            else if 
         }
         */
         //Update Matrix
         
         
         
         
         
         
         
         //Repaint
         PrintBoard();
         
         valid = false;
         while(!valid)
         {
            //Ask user for move (Have user type moves Ex: A1 to B2)
            System.out.println("Where do you want to move? From(Col)(Row) To(Col)(Row) To(Col)(Row) ... for additional hops");
            serverMove = inFromUser.readLine();         
            moveLength = serverMove.length();
            moveLength = moveLength - 2;
            numHops = moveLength / 3; 
            
            System.out.println("NumHops is: " + numHops); 
            //get moving piece
            temp = serverMove.charAt(0);
            fromCol = temp - 65;
            temp = serverMove.charAt(1);
            fromRow = temp - 48;
            System.out.println("Server From  : " + fromCol + " " + fromRow);

            currentPiece = pieces[fromCol][fromRow];   
            //create substring
            serverHops = serverMove.substring(2);
               
            //create array of toRow and toCol for number of hops and give them their values
            int [] toRows = new int [numHops+1];
            int [] toCols = new int [numHops+1];
            int [] thingstohopCol = new int [numHops];
            int [] thingstohopRow = new int [numHops];
            toRows[0] = fromRow;
            toCols[0] = fromCol;
            
            int count = 1;
            for(int i = 1; i<numHops+1; i++)
            {
               toCols[i] = serverHops.charAt(count) - 65;
               toRows[i] = serverHops.charAt(count+1) - 48;
               System.out.println("Server to: " + toCols[i] + " " + toRows[i]);
               count = count + 3;
            }
            
            //assume move is valid until otherwise proven
            valid = true; 
            alreadyhoped = false;
            alreadymoved = false; 
          //if not king nor pawn
          if(pieces[fromCol][fromRow]!=2 && pieces[fromCol][fromRow]!=4)
          {
               valid = false;
               System.out.println("Error: There is no piecec at the place you selected");
               continue;
          }
          else if(pieces[fromCol][fromRow]==2)
          {
            //CHECK ALL HOPS: to make sure all are valid before changing matrix values
            for(int i = 1; i < numHops + 1; i++)
            {               
               //make sure next move does not place player out of the board area
               if(toRows[i]<0 || toRows[i]>8 || toCols[i]<0 || toCols[i]>8)
               {
                  valid = false;
                  System.out.println("Error Pawn: that move is out of bounds");
                  break;
               }
             //make sure it is a valid pawn move
               //if move is (- 2 cols) and (+ or - 2 rows)
               if(toCols[i]==toCols[i-1]-2 && (toRows[i]==toRows[i-1]-2 || toRows[i]==toRows[i-1]+2) )
               {
                  if(alreadymoved)
                  {
                     valid = false;
                     System.out.println("Error Pawn: You cannot move then hop in one turn");
                     break;
                  }
                  //if there is a piece but the next spot is blocked
                  if(pieces[toCols[i]][toRows[i]]!=0)
                  {
                     valid = false;
                     System.out.println("Error Pawn: There is a piece in the square you are trying to hop to");
                     break;
                  }
                  //space to hop over
                  // col or row of hoped piece: from + (to - from) / 2
                  System.out.println("toCols[i-1] and toCols[i]: " + toCols[i-1] + " " + toCols[i]);
                  System.out.println("toRows[i-1] and toRows[i]: " + toCols[i-1] + " " + toCols[i]);
                  hopedCol = toCols[i-1] + (toCols[i] - toCols[i-1]) / 2;
                  hopedRow = toRows[i-1] + (toRows[i] - toRows[i-1]) / 2;
                  System.out.println("HopedCol and HopedRow: " + hopedCol + " " + hopedRow);
                  //if there is not a piece to hop in the closer square
                  if(pieces[hopedCol][hopedRow]!=1 && pieces[hopedCol][hopedRow]!=3 )
                  {
                     valid = false;
                     System.out.println("Error Pawn: There is no piece in the square you are tyring to hop over");
                     break;
                  }
                  thingstohopCol[i-1] = hopedCol;
                  thingstohopRow[i-1] = hopedRow;
                  alreadyhoped = true;
               } //else if move is (- 1 cols) and (+ or - 1 row)
               else if(toCols[i]==toCols[i-1]-1 && (toRows[i]==toRows[i-1]-1 || toRows[i]==toRows[i-1]+1) )
               {
                  if(alreadyhoped || alreadymoved)
                  {
                     valid = false;
                     System.out.println("Error Pawn: You cannot move after you have already moved or hoped");
                     break;
                  }
                  //check to see if a piece is already in that location
                  if(pieces[toCols[i]][toRows[i]]!=0)
                  {
                     valid = false;
                     System.out.println("Error Pawn: There is a piece in the square you are trying to move to");
                     break;
                  }
                  alreadymoved = true;   
               } //else
               else
               {
                  System.out.println("Error Pawn: That move is out of the scope of a pawn");
                  valid = false;
                  break;  
               }                     
                 
            }//end for
            if(valid==false)
            {
               continue;
            }
            //MAKE CHANGES:
            //if valid changes
            if(valid==false)
            {
               continue;
            }
            else if (valid==true)
            {
               //first set to zero
               pieces[fromCol][fromRow] = 0;
               //all hoped set to zero
               if(thingstohopCol.length > 0) //if there are are things to hop
               {
                  for(int i = 0; i<thingstohopCol.length; i++)//remove all of them 
                  {
                     System.out.println("We tried to hop: " + thingstohopCol[i] + " " + thingstohopRow[i]);
                     
                     pieces[thingstohopCol[i]][thingstohopRow[i]] = 0;
                  }
               }
               //last set to original
               pieces[toCols[numHops]][toRows[numHops]] = currentPiece;
            }   
          }//end else if pawn
          else //if king
          {
            //CHECK ALL HOPS: to make sure all are valid before changing matrix values
            for(int i = 0; i < numHops; i++)
            {               
               //make sure next move does not place player out of the board area
              
               //make sure it is a valid king move
                  //if move is (+ or - 2 cols) and (+ or - 2 rows)
                     //if there is not a piece to hop in the closer square
               
                     //if there is a piece but the next spot is blocked
                     
                  //if move is (+ or - 1 cols) and (+ or - 1 row)
                     //check to see if a piece is already in that location
                     
                  //else invalid move
            }
            if(valid==false)
            {
               continue;
            }
            //MAKE CHANGES:
            //if valid changes
            //for loop through each hop  
        }//end else if king
         }//end second while   
         
         System.out.println("Server Move  : " + serverMove);
         //Send valid move to other player
         outToClient.writeBytes(serverMove + "\n");
         
         //Repaint
         PrintBoard();

         if(serverMove.equals("q") || clientMove.equals("q"))
            break;
         
      } //end first while  
   }//end method
   
   public static void SetBoard() //initially sets up pieces on gameboard for a new game
   {
      int temp = 0;
      /*
      Empty = 0
      Red Pawn = 1
      Black Pawn = 2
      Red King = 3
      Black King = 4
      */
      // pieces[columns][rows]
      
      //set all zeros
      for(int i = 0; i<8; i++)
      {
         for(int j = 0; j<8; j++)
         {
            pieces[i][j]=0;         
         }
      }
      //red left side of board
      for(int i = 0; i<3; i++) //columns
      {    
         for(int j = i%2; j<8; j+=2) //rows
         {
            pieces[i][j] = 1;
         }
         
      }
      //black right side of board
      for(int i = 5; i<8; i++) //columns
      {
         for(int j = i%2; j<8; j+=2) //rows
         {
            pieces[i][j] = 2;
         }
      }
      
      //Testing
      pieces[4][2] = 1;
      
      
   }
   
   public static void PrintBoard()
   {
      System.out.println();
      System.out.println("    A B C D E F G H");
      for(int i = 0; i<8; i++)
      {
         System.out.print(i + ":  ");
         for(int j = 0; j<8; j++)
         {
           System.out.print(pieces[j][i] + " ");         
         }
         System.out.println();
      }
   }
}
   

