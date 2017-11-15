/*
Philip Jeffries
Networking and Security 
Checkers Game
*/
import java.io.*;
import java.net.*;

//There will be a checkers Server and Client
//Server Starts the Game and Client connects
//SERVER IS RED PLAYER BECAUSE THEY GO FIRST
class CheckersClient {
   public static int [][] pieces = new int [8][8];
   public static void main(String argv[]) throws Exception {
   //Connect to Checkers Server 
      String clientMove = "", serverMove = "";
      char temp;
      int fromCol, fromRow, toCol, toRow, numHops;
      boolean valid = true;
      
      //read user input
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      
      //setup socket
      //CHANGE LOCALHOST TO THE IP OF THE SERVER
      //USE TWO VIRUS LAB COMPUTERS
      Socket clientSocket = new Socket("localhost", 6789);
      
       //setup data output stream
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      
      //setup server reader
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      
      //Create Matrix of Pieces
      SetBoard();
      PrintBoard();

      //Create GUI of board
      
      while (true) {
         //LOOP OF MOVES ::
         //Wait for Server move
         //Update Matrix
         //Repaint
         //Ask user for move (Have user type moves Ex: A1 to B2
         //Tell user if move is valid
         //Change Matrix value
         //Send valid move to other player
         //Repaint

         //send move
         System.out.println("Where do you want to move? From(Col)(Row) To(Col)(Row)");
         clientMove = inFromUser.readLine();
         outToServer.writeBytes(clientMove + '\n');
         
         //recieve move
         System.out.println("Waiting for Client... ");
         serverMove = inFromServer.readLine();
         System.out.println("Server Move: " + serverMove + "");
         
         if(serverMove.equals("q") || clientMove.equals("q"))
            break;

      }
      
      //close socket
      clientSocket.close();
   }
   
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