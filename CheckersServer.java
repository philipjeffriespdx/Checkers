/*
Philip Jeffries
Networking and Security 
Checkers Game
*/

//I just copied my first networking lab to be able to build on top of it:
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
      String clientMove = "", serverMove = "", serverHops = "";
      char temp;
      int fromCol, fromRow, toCol, toRow, moveLength, numHops;
      //for more than one hop use toRows[] and toCols[]
      boolean valid = false, hop = false;
        
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
      
      //Create GUI of board
      
      
      //continue to receive and send data
      while (true) { 
         //LOOP OF MOVES ::
         //Wait for Client move
         System.out.println("Waiting for Client... ");
         clientMove = inFromClient.readLine();
         System.out.println("Client Move: " + clientMove);
         temp = clientMove.charAt(0);
         fromCol = temp - 65;
         temp = clientMove.charAt(1);
         fromRow = temp - 48;
         temp = clientMove.charAt(3);
         toCol = temp - 65;
         temp = clientMove.charAt(4);
         toRow = temp - 48;
         
         //Update Matrix
         System.out.println("Client From  : " + fromCol + " " + fromRow);
         System.out.println("Client To    : " + toCol + " " + toRow);
         
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
            
            //get moving piece
            temp = serverMove.charAt(0);
            fromCol = temp - 65;
            temp = serverMove.charAt(1);
            fromRow = temp - 48;
               
            //create substring
            serverHops = serverMove.substring(2);
               
            //create array of toRow and toCol for number of hops and give them their values
            int [] toRows = new int [numHops+1];
            int [] toCols = new int [numHops+1];
            
            toRows[0] = fromRow;
            toCols[0] = fromCol;
            
            int count = 1;
            for(int i = 1; i<numHops+1; i++)
            {
               toRows[i] = serverHops.charAt(count);
               toCols[i] = serverHops.charAt(count+1);
               count = count + 3;
            }
            
            //assume move is valid until otherwise proven
            valid = true;  
          //if not king nor pawn
          if(pieces[fromCol][fromRow]!=2 && pieces[fromCol][fromRow]!=4)
          {
               valid = false;
               continue;
          }
          else if(pieces[fromCol][fromRow]==2)
          {
            //CHECK ALL HOPS: to make sure all are valid before changing matrix values
            for(int i = 1; i < numHops+1; i++)
            {               
               //make sure next move does not place player out of the board area
               if(toRows[i]<0 || toRows[i]>8 || toCols[i]<0 || toCols[i]>8)
               {
                  valid = false;
                  break;
               }
               //make sure it is a valid pawn move
               
               //if move is (- 2 cols) and (+ or - 2 rows)
                   //if there is not a piece to hop in the closer square
               
                   //if there is a piece but the next spot is blocked
                     
               //if move is (- 1 cols) and (+ or - 1 row)
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
          } 
            
          //else if king
          //{
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
        //}
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
   

