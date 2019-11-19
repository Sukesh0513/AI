package wumpusworld;
/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */


public class MyAgent implements Agent
{
    private World w;
    int rnd;
    boolean wumpus_known = false;
    
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;
        
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
    	
    	Room r = new Room(w, w.getPlayerX(), w.getPlayerY(), this.wumpus_known);  
        r.create_graph();
        r.update_graph();
    	
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");            	
            
        }
        if (w.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }
        if (w.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
            
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        
               
        //r.shortest_path();
        
        if(w.hasStench(1, 1)) {
        	if(w.hasArrow()) {
	        	if(w.getDirection() == World.DIR_UP) w.doAction(World.A_SHOOT);
		 		else if(w.getDirection() == World.DIR_LEFT) {
		 			w.doAction(World.A_TURN_RIGHT);
		 			w.doAction(World.A_SHOOT);
		 			}
		 		else if(w.getDirection() == World.DIR_DOWN) {
		 			w.doAction(World.A_TURN_RIGHT);
		 			w.doAction(World.A_TURN_RIGHT);
		 			w.doAction(World.A_SHOOT);
		 		}
		 		else {
		 			w.doAction(World.A_TURN_LEFT);
		 			w.doAction(World.A_SHOOT);
		 		}
        	}
        	if(w.wumpusAlive()) {
        		this.wumpus_known = true;
        		Room r2 = new Room(w, w.getPlayerX(), w.getPlayerY(), this.wumpus_known);  
                r2.create_graph();
                r2.update_graph();
        		r2.N[1][0].cost = 5000;
        		r2.shortest_path();
        	}
        	else r.shortest_path();
        }
        
        else if (w.hasStench(cX, cY))
	        {  
	        	if(w.hasArrow()) {
		            if(w.hasStench(cX+1, cY+1)) {            	
		            	if(w.isVisited(cX+1, cY)) {
		            		if(w.getDirection() == World.DIR_UP) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);
		            	}
		            	else if(w.isVisited(cX, cY+1)) {
		            		if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_UP) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);            		            		
		            	}
		            }
		            
		            else if(w.hasStench(cX+1, cY-1)) {
		            	if(w.isVisited(cX+1, cY)) {
		            		if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_UP) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);
		            	}
		            	else if(w.isVisited(cX, cY-1)) {
		            		if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_UP) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);            		            		
		            	}  	
		            }
		            
		            else if(w.hasStench(cX-1, cY-1)) {
		            	if(w.isVisited(cX-1, cY)) {
		            		if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_UP) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);
		            	}
		            	else if(w.isVisited(cX, cY-1)) {
		            		if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);            		            		
		            	}
		            }
		            
		            else if(w.hasStench(cX-1, cY+1)) {
		            	if(w.isVisited(cX, cY+1)) {
		            		if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_SHOOT);
		            		else if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_TURN_RIGHT);
		            		else if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_TURN_RIGHT);
		            		else w.doAction(World.A_TURN_LEFT);
		            	}
		            	else if(w.isVisited(cX-1, cY)) {
		            		if(w.getDirection() == World.DIR_UP) w.doAction(World.A_SHOOT);
		                	else if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_TURN_RIGHT);
		                	else if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_TURN_RIGHT);
		                	else w.doAction(World.A_TURN_LEFT);            		            		
		            	}
		            }
		            
		            else if(w.hasStench(cX+2, cY) && w.isVisited(cX+2, cY)) {
		            	if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_SHOOT);
		            	else if(w.getDirection() == World.DIR_UP) w.doAction(World.A_TURN_RIGHT);
		            	else if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_TURN_RIGHT);
		            	else w.doAction(World.A_TURN_LEFT);            	
		            } 
		            
		            else if(w.hasStench(cX-2, cY) && w.isVisited(cX-2, cY)) {
		            	if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_SHOOT);
		            	else if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_TURN_RIGHT);
		        		else if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_TURN_RIGHT);
		        		else w.doAction(World.A_TURN_LEFT);             	
		            }
		            
		            else if(w.hasStench(cX, cY+2) && w.isVisited(cX, cY+2)) {
		            	if(w.getDirection() == World.DIR_UP) w.doAction(World.A_SHOOT);
		            	else if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_TURN_RIGHT);
		            	else if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_TURN_RIGHT);
		            	else w.doAction(World.A_TURN_LEFT);            	             	
		            }
		            
		            else if(w.hasStench(cX, cY-2) && w.isVisited(cX, cY-2)) {
		            	if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_SHOOT);
		        		else if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_TURN_RIGHT);
		        		else if(w.getDirection() == World.DIR_UP) w.doAction(World.A_TURN_RIGHT);
		        		else w.doAction(World.A_TURN_LEFT);            	             	
		            }
		            else {
		            	r.shortest_path();
		            }
	        	}     	
	        }        
        else r.shortest_path();      
    }   
}







  

