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
      
      //server variables
      String serverFrom, sNumServerHops, currentServerHop, serverDestination;
      int numServerHops = 0, Col, Row;

      
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
            //System.out.println("Client From: " + fromCol + " " + fromRow);

            currentPiece = pieces[fromCol][fromRow];   
            //create substring
            serverHops = clientMove.substring(2);
               
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
               //System.out.println("Client to: " + toCols[i] + " " + toRows[i]);
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
          else if(pieces[fromCol][fromRow]==1) //CHECK ALL HOPS: to make sure all are valid before changing matrix values
          {
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
                  //System.out.println("toCols[i-1] and toCols[i]: " + toCols[i-1] + " " + toCols[i]);
                  //System.out.println("toRows[i-1] and toRows[i]: " + toRows[i-1] + " " + toRows[i]);
                  hopedCol = toCols[i-1] + (toCols[i] - toCols[i-1]) / 2;
                  hopedRow = toRows[i-1] + (toRows[i] - toRows[i-1]) / 2;
                  //System.out.println("HopedCol and HopedRow: " + hopedCol + " " + hopedRow);
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
            }//end pawn hop for
            if(valid==false)
            {
               continue;
            }  
          }//end else if pawn
          else //if king 3
          { 
             //CHECK ALL HOPS: to make sure all are valid before changing matrix values
            for(int i = 1; i < numHops + 1; i++)
            {               
               //make sure next move does not place player out of the board area
               if(toRows[i]<0 || toRows[i]>8 || toCols[i]<0 || toCols[i]>8)
               {
                  valid = false;
                  System.out.println("Error King: that move is out of bounds");
                  break;
               }
             //make sure it is a valid pawn move
               //if move is (- 2 cols) and (+ or - 2 rows)
               if((toCols[i]==toCols[i-1]-2 || toCols[i]==toCols[i-1]+2) && (toRows[i]==toRows[i-1]-2 || toRows[i]==toRows[i-1]+2) )
               {
                  if(alreadymoved)
                  {
                     valid = false;
                     System.out.println("Error King: You cannot move then hop in one turn");
                     break;
                  }
                  //if there is a piece but the next spot is blocked
                  if(pieces[toCols[i]][toRows[i]]!=0)
                  {
                     valid = false;
                     System.out.println("Error King: There is a piece in the square you are trying to hop to");
                     break;
                  }
                  //space to hop over
                  // col or row of hoped piece: from + (to - from) / 2
                  //System.out.println("toCols[i-1] and toCols[i]: " + toCols[i-1] + " " + toCols[i]);
                  //System.out.println("toRows[i-1] and toRows[i]: " + toCols[i-1] + " " + toCols[i]);
                  hopedCol = toCols[i-1] + (toCols[i] - toCols[i-1]) / 2;
                  hopedRow = toRows[i-1] + (toRows[i] - toRows[i-1]) / 2;
                  //System.out.println("HopedCol and HopedRow: " + hopedCol + " " + hopedRow);
                  //if there is not a piece to hop in the closer square
                  if(pieces[hopedCol][hopedRow]!=2 && pieces[hopedCol][hopedRow]!=4 )
                  {
                     valid = false;
                     System.out.println("Error King: There is no piece in the square you are tyring to hop over");
                     break;
                  }
                  thingstohopCol[i-1] = hopedCol;
                  thingstohopRow[i-1] = hopedRow;
                  alreadyhoped = true;
               } //else if move is (- 1 cols) and (+ or - 1 row)
               else if((toCols[i]==toCols[i-1]-1 || toCols[i]==toCols[i-1]+1) && (toRows[i]==toRows[i-1]-1 || toRows[i]==toRows[i-1]+1) )
               {
                  if(alreadyhoped || alreadymoved)
                  {
                     valid = false;
                     System.out.println("Error King: You cannot move after you have already moved or hoped");
                     break;
                  }
                  //check to see if a piece is already in that location
                  if(pieces[toCols[i]][toRows[i]]!=0)
                  {
                     valid = false;
                     System.out.println("Error King: There is a piece in the square you are trying to move to");
                     break;
                  }
                  alreadymoved = true;   
               } 
               else
               {
                  System.out.println("Error King: That move is out of the scope of a pawn");
                  valid = false;
                  break;  
               }                       
            }//end for
            if(valid==false)
            {
               continue;
            } 
          }//end else if king

          
          //SEND CHANGES TO SERVER
          if(valid==true && (pieces[fromCol][fromRow]==1 || pieces[fromCol][fromRow]==3))
          {
               //System.out.println("Writing to server 180");
               //first set to zero
               pieces[fromCol][fromRow] = 0;
               //System.out.println("Sending to Server Origin: " + fromCol + "" + fromRow);
               outToServer.writeBytes(fromCol + "" + fromRow + '\n');
               
               trash = inFromServer.readLine();
               //all hoped set to zero
               //System.out.println("Sending to Server NumberOfHops: " + thingstohopCol.length);
               outToServer.writeBytes(thingstohopCol.length + "" + '\n');
               
               trash = inFromServer.readLine();
               if(thingstohopCol.length > 0) //if there are are things to hop
               {
                  for(int i = 0; i<thingstohopCol.length ; i++)//remove all of them 
                  {
                     if(thingstohopCol[i]!=0 && thingstohopRow[i]!=0)
                     {
                        pieces[thingstohopCol[i]][thingstohopRow[i]] = 0;
                     }
                     //System.out.println("Sending to Server HOP: " + thingstohopCol[i] + "" + thingstohopRow[i]);
                     outToServer.writeBytes(thingstohopCol[i] + "" + thingstohopRow[i] + '\n');
                     trash = inFromServer.readLine();
                  }
               }
               //last set to original
               pieces[toCols[numHops]][toRows[numHops]] = currentPiece;
               //System.out.println("Sending to Server Destination: " + toCols[numHops] + "" + toRows[numHops]);
               outToServer.writeBytes(toCols[numHops] + "" + toRows[numHops] + '\n');
               trash = inFromServer.readLine();
          } //end making changes
          
         }//end second while   
     //END CLIENT MOVE
     //********************* START SERVER MOVE ****************************************************************************
         PrintBoard();

         //Get Client Move move
         System.out.println("Waiting for SERVER... ");
         //receive ORIGIN: COL " " ROW
         serverFrom = inFromServer.readLine();
         if(serverFrom.equals("q") || serverFrom.equals("q"))
            break;
         if(serverFrom.length() < 2)
         {
            //System.out.println("ServerFrom is incorrect at 243: " + serverFrom);
            outToServer.writeBytes("y" + "\n");
         }
         temp = serverFrom.charAt(0);
         fromCol = temp - 48;
         temp = serverFrom.charAt(1);
         fromRow = temp - 48;
         
         outToServer.writeBytes("y" + "\n");
         
         currentPiece = pieces[fromCol][fromRow];
         pieces[fromCol][fromRow] = 0;
         
         //receive HOPS
         sNumServerHops = inFromServer.readLine();
         numServerHops = Integer.valueOf(sNumServerHops);
         
         outToServer.writeBytes("y" + "\n");
         
         //loop through num client hops
         if(numServerHops > 0)
         {
            for(int i = 0; i < numServerHops; i++)
            {
               //receive things that will be hoped COL " " ROW
               serverHops = inFromServer.readLine();
               temp = serverHops.charAt(0);
               Col = temp - 48;
               temp = serverHops.charAt(1);
               Row = temp - 48;
               if(Col!=0 && Row!=0)
               {
                  pieces[Col][Row] = 0;
               }
               outToServer.writeBytes("y" + "\n");
            }
         }//end if numClientHops
         
         //receive DESTINATION:
         serverDestination = inFromServer.readLine();
         temp = serverDestination.charAt(0);
         toCol = temp - 48;
         temp = serverDestination.charAt(1);
         toRow = temp - 48;
         
         //outToServer.writeBytes("y" + "\n");
         
         pieces[toCol][toRow] = currentPiece;
         
         System.out.println("Server Move: " + serverFrom + " to " + serverDestination);
         
         //Repaint
         PrintBoard();
         
      }//End first while
//turns pawns to kings
//game over when no other pieces left
      //close socket
      clientSocket.close();
   }
   
   public static void SetBoard() //initially sets up pieces on gameboard for a new game
   {
      int temp = 0;
      /* Empty = 0
      Red Pawn = 1
      Black Pawn = 2
      Red King = 3
      Black King = 4   
      pieces[columns][rows]
      //set all zeros */
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
      //pieces[1][1] = 2;
      //pieces[0][2] = 0;
   }
   
   public static void PrintBoard()
   {
      boolean KeepPlaying = true, ServerAlive = false, ClientAlive = false;
      for(int i = 0; i<8; i++)
      {
         if(pieces[0][i]==2)
         {
            pieces[0][i]=4;
         }
         if(pieces[7][i]==1)
         {
            pieces[7][i]=3;
         }

         for(int j = 0; j<8; j++)
         {
            if(pieces[j][i] == 2 || pieces[j][i] == 4)
            {
               ServerAlive = true;
            }
            if(pieces[j][i] == 1 || pieces[j][i] == 3)
            {
               ClientAlive = true;
            }
         }
      }
      
      if(!ServerAlive)
      {
         KeepPlaying = false;
         System.out.println("Player 2 Wins!");
         try{
            Thread.sleep(10);
         }
         catch(InterruptedException e){
            System.out.println("");
         }
         System.exit(0);
      }
      else if(!ClientAlive)
      {
         KeepPlaying = false;
         System.out.println("Player 1 Wins!");
         try{
            Thread.sleep(10);
         }
         catch(InterruptedException e){
            System.out.println("");
         }
         System.exit(0);
      }
      
      if(KeepPlaying)
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
}