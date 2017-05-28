//Andrew Tek
//CS 380-P6
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TicTacToeClient {
	
	public static void main (String args []) throws UnknownHostException, IOException {

		try (Socket socket = new Socket("codebank.xyz", 38006)) {
			System.out.println("Connected to server...");
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			//This thread checks for messages and decodes/outputs them
			Thread getMessage = new Thread() {
				public void run() {
					Object message ;
					while (true) {
						try {
							message = inStream.readObject();
							if (message.getClass().equals(BoardMessage.class)) {
								BoardMessage bmess = (BoardMessage)message;
								printBoard(bmess.getBoard());
								int win = checkWin(bmess.getBoard());
								switch (win) {
									case 1: System.out.println("You Win!");
										outStream.writeObject(new CommandMessage(CommandMessage.Command.EXIT));
										System.exit(0);
										break;
									case 2: System.out.println("Computer Wins!");
										outStream.writeObject(new CommandMessage(CommandMessage.Command.EXIT));
										System.exit(0);
										break;
									case 3: System.out.println("Tie Game.");
										outStream.writeObject(new CommandMessage(CommandMessage.Command.EXIT));
										System.exit(0);
										break;
									default: 
										break;
								}			
							}
							else if (message.getClass().equals(ErrorMessage.class)) {
								ErrorMessage err = (ErrorMessage) message;
								System.out.println(err.getError());
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						} 
					}
				}
			};
            Scanner sc = new Scanner (System.in);
            System.out.print("Enter Username: ");
           String username = sc.next();
            outStream.writeObject(new ConnectMessage(username));
            outStream.writeObject(new CommandMessage(CommandMessage.Command.NEW_GAME));
            getMessage.start();
            while (true) {
            	System.out.println("Enter the number for where you want to move (-1 if you want to surrender): ");
            	int move = sc.nextInt();
            	switch (move) {
            	case 1: 
            		outStream.writeObject(new MoveMessage((byte) 0, (byte) (0)));
            		break;
            	case 2: 
            		outStream.writeObject(new MoveMessage((byte) 0, (byte) (1)));
            		break;
            	case 3: 
            		outStream.writeObject(new MoveMessage((byte) 0, (byte) (2)));
            		break;
            	case 4: 
            		outStream.writeObject(new MoveMessage((byte) 1, (byte) (0)));
            		break;
            	case 5: 
            		outStream.writeObject(new MoveMessage((byte) 1, (byte) (1)));
            		break;
            	case 6: 
            		outStream.writeObject(new MoveMessage((byte) 1, (byte) (2)));
            		break;
            	case 7: 
            		outStream.writeObject(new MoveMessage((byte) 2, (byte) (0)));
            		break;
            	case 8: 
            		outStream.writeObject(new MoveMessage((byte) 2, (byte) (1)));
            		break;
            	case 9: 
            		outStream.writeObject(new MoveMessage((byte) 2, (byte) (2)));
            		break;
            	case -1:
            		outStream.writeObject(new CommandMessage(CommandMessage.Command.SURRENDER));
            		break;
            	case 0:
            		outStream.writeObject(new CommandMessage(CommandMessage.Command.EXIT));
            		System.exit(0);
            		break;
            		
            		
            	}
            }
            
		}
	}
	public static void printBoard(byte[][] board) {
		int counter = 1;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (board[i][j] == (byte)0) {
					System.out.print(" " + counter + " |");
					counter++;
				}
				else if (board[i][j] == (byte) 1) {
					System.out.print(" X |");
					counter++;
				}
				else  {
					System.out.print(" O |");
					counter++;
				}
				
			}
			
			if (board[i][2] == (byte)0) {
				System.out.print(" " + counter + " \n");
				counter++;
			}
			else if (board[i][2] == (byte)1) {
				System.out.print(" X \n");
				counter++;
			}
			else  {
				System.out.print(" O \n");	
				counter++;
			}
			System.out.println("---|---|---");
		}
		for (int i = 0; i < 2; i++) {
			if (board[2][i] == (byte)0)  {
				System.out.print(" " + counter + " |");
				counter++; 
			}
			else if (board[2][i] == (byte)1) {
				System.out.print(" X |");
			}
			else {
				System.out.print(" O |");
			}
		}
		if (board[2][2] == (byte)0) {
			System.out.print(" " + counter + " \n");
			counter++;
		}
		else if (board[2][2] == (byte)1) {
			System.out.print(" X \n");
			counter++;
		}
		else  {
			System.out.print(" O \n");	
			counter++;
		}
		System.out.println();
	}
	public static int checkWin (byte [][] board) {
		for (int i = 0; i < 3; i++) {
			int player = board [i][0];
			boolean win = true;
			for (int j = 1; j < 3; j++) {
				if (board[i][j] != player) {
					win = false;
					break;
				}
			}
			if (win == true)
				return player;
		}
		for (int i = 0; i < 3; i++) {
			int player = board [0][i];
			boolean win = true;
			for (int j = 1; j < 3; j++) {
				if (board[j][i] != player) {
					win = false;
					break;
				}
			}
			if (win == true)
				return player;
		}
		for (int i = 0; i < 3; i++) {
			int player = board [0][0];
			boolean win = true;
			for (int j = 1; j < 3; j++) {
				if (board[i][i] != board [j][j]) {
					win = false;
					break;
				}
			}
			if (win == true)
				return player;
		}
		for (int i = 0; i < 3; i++) {
			int player = board [0][0];
			boolean win = true;
			for (int j = 1; j < 3; j++) {
				if (board[i][i] != board [j][j]) {
					win = false;
					break;
				}
			}
			if (win == true)
				return player;
		}
		if (board[0][2] == board [1][1]  && board[0][2] == board[2][0])
			return board[0][2];
		int counter = 0;
		for (int i = 0; i < 3; i++) 
			for (int j = 0; j < 3;j++) {
			if (board[i][j] == 1 || board[i][j] == 2)
				counter++;
			}
		if (counter == 9)
			return 3;
		return -1;
	}

}
