package ai;

import ai.Global;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import kalaha.*;

/**
 * This is the main class for your Kalaha AI bot. Currently
 * it only makes a random, valid move each turn.
 * 
 * @author Johan Hagelbäck
 */
public class AIClient implements Runnable
{
    private int player;
    private JTextArea text;
    
    private PrintWriter out;
    private BufferedReader in;
    private Thread thr;
    private Socket socket;
    private boolean running;
    private boolean connected;
    	
    /**
     * Creates a new client.
     */
    public AIClient()
    {
	player = -1;
        connected = false;
        
        //This is some necessary client stuff. You don't need
        //to change anything here.
        initGUI();
	
        try
        {
            addText("Connecting to localhost:" + KalahaMain.port);
            socket = new Socket("localhost", KalahaMain.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            addText("Done");
            connected = true;
        }
        catch (Exception ex)
        {
            addText("Unable to connect to server");
            return;
        }
    }
    
    /**
     * Starts the client thread.
     */
    public void start()
    {
        //Don't change this
        if (connected)
        {
            thr = new Thread(this);
            thr.start();
        }
    }
    
    /**
     * Creates the GUI.
     */
    private void initGUI()
    {
        //Client GUI stuff. You don't need to change this.
        JFrame frame = new JFrame("My AI Client");
        frame.setLocation(Global.getClientXpos(), 445);
        frame.setSize(new Dimension(420,250));
        frame.getContentPane().setLayout(new FlowLayout());
        
        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setPreferredSize(new Dimension(400, 210));
        
        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
    }
    
    /**
     * Adds a text string to the GUI textarea.
     * 
     * @param txt The text to add
     */
    public void addText(String txt)
    {
        //Don't change this
        text.append(txt + "\n");
        text.setCaretPosition(text.getDocument().getLength());
    }
    
    /**
     * Thread for server communication. Checks when it is this
     * client's turn to make a move.
     */
    public void run()
    {
        String reply;
        running = true;
        
        try
        {
            while (running)
            {
                //Checks which player you are. No need to change this.
                if (player == -1)
                {
                    out.println(Commands.HELLO);
                    reply = in.readLine();

                    String tokens[] = reply.split(" ");
                    player = Integer.parseInt(tokens[1]);
                    
                    addText("I am player " + player);
                }
                
                //Check if game has ended. No need to change this.
                out.println(Commands.WINNER);
                reply = in.readLine();
                if(reply.equals("1") || reply.equals("2") )
                {
                    int w = Integer.parseInt(reply);
                    if (w == player)
                    {
                        addText("I won!");
                    }
                    else
                    {
                        addText("I lost...");
                    }
                    running = false;
                }
                if(reply.equals("0"))
                {
                    addText("Even game!");
                    running = false;
                }

                //Check if it is my turn. If so, do a move
                out.println(Commands.NEXT_PLAYER);
                reply = in.readLine();
                if (!reply.equals(Errors.GAME_NOT_FULL) && running)
                {
                    int nextPlayer = Integer.parseInt(reply);

                    if(nextPlayer == player)
                    {
                        out.println(Commands.BOARD);
                        String currentBoardStr = in.readLine();
                        boolean validMove = false;
                        while (!validMove)
                        {
                            long startT = System.currentTimeMillis();
                            //This is the call to the function for making a move.
                            //You only need to change the contents in the getMove()
                            //function.
                            GameState currentBoard = new GameState(currentBoardStr);
                            int cMove = getMove(currentBoard);
                            
                            //Timer stuff
                            long tot = System.currentTimeMillis() - startT;
                            double e = (double)tot / (double)1000;
                            
                            out.println(Commands.MOVE + " " + cMove + " " + player);
                            reply = in.readLine();
                            if (!reply.startsWith("ERROR"))
                            {
                                validMove = true;
                                addText("Made move " + cMove + " in " + e + " secs");
                            }
                        }
                    }
                }
                
                //Wait
                Thread.sleep(100);
            }
	}
        catch (Exception ex)
        {
            running = false;
        }
        
        try
        {
            socket.close();
            addText("Disconnected from server");
        }
        catch (Exception ex)
        {
            addText("Error closing connection: " + ex.getMessage());
        }
    }
    
    /**
     * This is the method that makes a move each time it is your turn.
     * Here you need to change the call to the random method to your
     * Minimax search.
     * @param currentBoard The current board state
     * @return Move to make (1-6)
     */
    
    public int maximumPlayer = 1;
	public int minimumPlayer = 2;
	public int eval;
	int alpha = -34000;
	int beta = 34000;
	
	/**
     * will return the best move
     * @param currentBoard Board representation
     * 
     */    
    public int getMove(GameState currentBoard) {
    	
		int move = 1;
		int staticEval = 0;
		int evScore= -32000;
		
		if(player == minimumPlayer) {
			maximumPlayer=2;
			minimumPlayer=1;
		}

		for(int i=1;i<7;i++)
		{
			//calling the minimax for the child nodes at depth 1
			if(currentBoard.moveIsPossible(i))
			{					
				GameState gss = currentBoard.clone();
				gss.makeMove(i);
				
				//getting the static scores for every move at depth 1 
				staticEval = miniMax(gss, 10, gss.getNextPlayer(), alpha, beta);
				
				//compares the static scores of all moves at depth 1 and returns the one with highest score
				if(evScore < staticEval)
				{
					evScore = staticEval;
					move=i;
				}
			}
		
		}
		return move;
	}
	
	/*this function will return the static score
	    **@param GameState, the board state
	    **@param depth the, depth of the minimax tree  
	    **@param currentPlayer, the player to make the move 
	    **@return The static score
	*/    
	public int miniMax(GameState game, int depth, int currentPlayer, int alpha, int beta)
	{
		//if player wins then return the highest static score else return smallest static scores
		if(game.gameEnded())
		{			
			if(game.getWinner() == player) return 1000;
			else return -1000;			
		}
		
		//returning static score at root node
		if(depth==0)
		{
			return game.getScore(maximumPlayer)-game.getScore(minimumPlayer);
		}
		
		
		
		//calling the miniMax recursively
		
		if(currentPlayer == maximumPlayer)
		{
            int max=-34000;           			
			
			for(int i=1;i<=6;i++)
			{				
				if(game.moveIsPossible(i))
				{					
					GameState g = game.clone();
					g.makeMove(i);
					eval = miniMax(g , depth-1, g.getNextPlayer(), alpha, beta);
					max = Math.max(eval, max);	
					alpha = Math.max(alpha, max);
					if(alpha >= beta) break;
				}
			}			
			return max;
		}
		
		else
		{
			int min = 34000;
				
			for(int i=1;i<=6;i++)
			{
				if(game.moveIsPossible(i))
				{
					GameState g = game.clone();
					g.makeMove(i);
					eval = miniMax(g , depth-1, g.getNextPlayer(), alpha, beta);	
					min = Math.min(eval, min);
					beta = Math.min(beta, min);
					if(alpha >= beta) break;
				}
			}
			return min;	
		}		
	} 
}
