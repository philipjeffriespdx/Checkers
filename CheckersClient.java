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
      String clientMove = "", serverMove = "", serverHops = "", trash = "";
      char temp;
      int fromCol, fromRow, toCol, toRow, moveLength, numHops, hopedCol, hopedRow, currentPiece;
      //for more than one hop use toRows[] and toCols[]
      boolean valid = false, hop = false, alreadyhoped = false, alreadymoved = false;

      
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
         valid = false;
         while(!valid)
         {
            //send move
            System.out.println("Where do you want to move? From(Col)(Row) To(Col)(Row) To(Col)(Row) ... for additional hops");
            clientMove = inFromUser.readLine();
            moveLength = clientMove.length();
            moveLength = moveLength - 2;
            numHops = moveLength / 3; 
            
            
            //get moving piece
            temp = clientMove.charAt(0);
            fromCol = temp - 65;
            temp = clientMove.charAt(1);
            fromRow = temp - 48;
            System.out.println("Client From: " + fromCol + " " + fromRow);

            currentPiece = pieces[fromCol][fromRow];   
            //create substring
            serverHops = clientMove.substring(2);
               
            //create array of toRow and toCol for number of hops and give them their values
            int [] toRows = new int [numHops+1];
            int [] toCols = new int [numHops+1];
            int [] thingstohopCol = new int [numHops-1];
            int [] thingstohopRow = new int [numHops-1];
            toRows[0] = fromRow;
            toCols[0] = fromCol;
            
            int count = 1;
            for(int i = 1; i<numHops+1; i++)
            {
               toCols[i] = serverHops.charAt(count) - 65;
               toRows[i] = serverHops.charAt(count+1) - 48;
               System.out.println("Client to: " + toCols[i] + " " + toRows[i]);
               count = count + 3;
            }
            
            //assume move is valid until otherwise proven
            valid = true; 
            alreadyhoped = false;
            alreadymoved = false; 
          //if not king nor pawn
          if(pieces[fromCol][fromRow]!=1 && pieces[fromCol][fromRow]!=3)
          {
               valid = false;
               System.out.println("Error: There is no piecec at the place you selected, or it is not your piece");
               continue;
          }
          else if(pieces[fromCol][fromRow]==1)
          {
//FROM HERE ON MAKE CHANGES TO  EVERTHING TO MAKE SURE IT IS FOR CLIENT AND NOT SERVER NUMBERS
            //CHECK ALL HOPS: to make sure all are valid before changing matrix values
            for(int i = 1; i < numHops+1; i++)
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
               if(toCols[i]==toCols[i-1]+2 && (toRows[i]==toRows[i-1]-2 || toRows[i]==toRows[i-1]+2) )
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
                  hopedCol = toCols[i-1] + (toCols[i] - toCols[i-1]) / 2;
                  hopedRow = toRows[i-1] + (toRows[i] - toRows[i-1]) / 2;
                  //if there is not a piece to hop in the closer square
                  if(pieces[hopedCol][hopedRow]!=2 && pieces[hopedCol][hopedRow]!=4 )
                  {
                     valid = false;
                     System.out.println("Error Pawn: There is no piece in the square you are tyring to hop over");
                     break;
                  }
                  thingstohopCol[i-1] = hopedCol;
                  thingstohopRow[i-1] = hopedRow;
                  alreadyhoped = true;
               } //else if move is (- 1 cols) and (+ or - 1 row)
               else if(toCols[i]==toCols[i-1]+1 && (toRows[i]==toRows[i-1]-1 || toRows[i]==toRows[i-1]+1) )
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
               outToServer.writeBytes(fromCol + "" + fromRow + '\n');
               
               trash = inFromServer.readLine();
               //all hoped set to zero
               outToServer.writeBytes(thingstohopCol.length + "" + '\n');
               
               trash = inFromServer.readLine();
               if(thingstohopCol.length > 0) //if there are are things to hop
               {
                  for(int i = 0; i<thingstohopCol.length; i++)//remove all of them 
                  {
                     pieces[thingstohopCol[i]][thingstohopRow[i]] = 0;
                     outToServer.writeBytes(thingstohopCol[i] + "" + thingstohopRow[i] + '\n');
                     trash = inFromServer.readLine();
                  }
               }
               //last set to original
               pieces[toCols[numHops]][toRows[numHops]] = currentPiece;
               outToServer.writeBytes(toCols[numHops] + "" + toRows[numHops] + '\n');
               trash = inFromServer.readLine();
            }   
          }//end else if pawn
            else //if king 3
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
         
         //outToServer.writeBytes(clientMove + '\n');
         
         PrintBoard();
         
         //recieve move
         System.out.println("Waiting for Server... ");
         serverMove = inFromServer.readLine();
         System.out.println("Server Move: " + serverMove + "");
         
         if(serverMove.equals("q") || clientMove.equals("q"))
            break;

      }
      //
      
//turns pawns to kings
//game over when no other pieces left
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