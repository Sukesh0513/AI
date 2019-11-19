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
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        System.out.println("Position of the player before moving ("+cX+","+ cY+")");
        
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
        
        findMove(cX, cY);
    }    
    
    /**
     * methods findMove to follow the percepts
     * @param cX for horizontal position
     * @param cY for vertical position
     */
    public void findMove(int cX, int cY)
    {
        if (w.hasStench(cX, cY))
        {
            moveStench(cX, cY);//it is used if the player is in stench
        }
        else if(w.getDirection() == World.DIR_UP)
        {
            moveUp(cX, cY);//follow this method if the player is facing up
        }
        else if(w.getDirection() == World.DIR_RIGHT)
        {
            moveRight(cX, cY);//method to follow if the player is facing right
        }
        else if(w.getDirection() == World.DIR_DOWN)
        {
            moveDown(cX, cY);//this method is used  if the player is facing down
        }
        else if(w.getDirection() == World.DIR_LEFT)
        {
            moveLeft(cX, cY);//this method if the player is facing left
        }
    }
    
    /**
     * the method specifies rules for stench
     * every possible positions of stench and instructions according to that
     * @param cX for horizontal position
     * @param cY for vertical position
     */
    public void moveStench(int cX, int cY)//move if the player is in stench
    { 
          //if the world has stench in the positions(cX-1,cY-1) and it has no arrow
          
         if(w.hasStench(cX-1, cY-1) && !w.hasArrow()) //the position has stench but no arrow
        {
            if(w.getDirection() == World.DIR_UP)//facing up
                w.doAction(World.A_MOVE);//move
            else if(w.getDirection() == World.DIR_LEFT)//facing left
                w.doAction(World.A_TURN_RIGHT);//turn right
            else if(w.getDirection() == World.DIR_DOWN)//facing down
                w.doAction(World.A_TURN_RIGHT);//turn right
            else
                w.doAction(World.A_TURN_LEFT);//turn left
            return;
        }
        //if the stench is in (cX, cY+2) 
        
        if (w.hasStench(cX, cY+2))  
        {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())//facing up with arrow
                w.doAction(World.A_SHOOT);//shoot if wumpus found
            else if (w.getDirection() == World.DIR_RIGHT && w.hasArrow())//facing right with arrow
                w.doAction(World.A_TURN_LEFT);//tuen left
            else if (w.getDirection() == World.DIR_DOWN && w.hasArrow())//facing down with arrow
                w.doAction(World.A_TURN_LEFT);//turn left
            else if (w.getDirection() == World.DIR_UP && !w.hasArrow())//facing up with no arrow
                w.doAction(World.A_TURN_LEFT);//turn left
            else if (!w.hasArrow() && w.isValidPosition(cX-1, cY))//no arrow with valid position
                w.doAction(World.A_MOVE);//move
            else if (!w.hasArrow())//the player has no arrow so try to escape
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);//move
            }
            else
                w.doAction(World.A_TURN_RIGHT);//turn right
        }
        
//if the world has stench in (cX+2, cY)and kill wumpus if found
        
        else if (w.hasStench(cX+2, cY))  
        {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())//facing up with arrow
                w.doAction(World.A_TURN_RIGHT);//turn right
            else if (w.getDirection() == World.DIR_RIGHT && w.hasArrow())//facing right with arrow
                w.doAction(World.A_SHOOT);//shoot if wumpus found
            else if (w.getDirection() == World.DIR_LEFT && w.hasArrow())//facing left with arrow
                w.doAction(World.A_TURN_LEFT);//turn left
            else if (w.getDirection() == World.DIR_RIGHT && !w.hasArrow())//facing right with no arrow
                w.doAction(World.A_TURN_LEFT);//turn left
            else if (!w.hasArrow() && w.isValidPosition(cX, cY+1))//no arrow with valid positon
                w.doAction(World.A_MOVE);//move
            else if (!w.hasArrow())//no arrow try to escape
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);//move
            }
            else
                w.doAction(World.A_TURN_LEFT);//turn left
        }
        
        /**
         *  if Wumpus found at (cX-2, cY) kill it 
         * if the player has no arrow then try to escape
         */
        
        else if (w.hasStench(cX-2, cY))  
        {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())
                w.doAction(World.A_TURN_LEFT);//left
            else if (w.getDirection() == World.DIR_RIGHT && w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_DOWN && w.hasArrow())
                w.doAction(World.A_TURN_RIGHT);//turn right
            else if (w.getDirection() == World.DIR_LEFT && !w.hasArrow())
                w.doAction(World.A_TURN_RIGHT);//turn right
            else if (!w.hasArrow() && w.isValidPosition(cX, cY+1))
                w.doAction(World.A_MOVE);
            else if (!w.hasArrow())
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);//move
            }
            else
                w.doAction(World.A_SHOOT);//shoot
        }
        
        /**
         * if the world has stench(cX, cY-2) and if wumpus found kill it 
         *if the  player has no arrow,try to escape
         */
        
        else if (w.hasStench(cX, cY-2))  
        {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_RIGHT && w.hasArrow())
                w.doAction(World.A_TURN_RIGHT);//turn right
            else if (w.getDirection() == World.DIR_DOWN && w.hasArrow())
                w.doAction(World.A_SHOOT);//shoot
            else if (w.getDirection() == World.DIR_DOWN && !w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (!w.hasArrow() && w.isValidPosition(cX, cY+1))
                w.doAction(World.A_MOVE);
            else if (!w.hasArrow())
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);
            }
            else
                w.doAction(World.A_TURN_LEFT);
        }
        
          //if the world has stench(cX-1, cY+1) 
        else if (w.hasStench(cX-1, cY+1))
        {
        if (w.isVisited(cX-1, cY)) //rules if the position is visited  
            {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())
                w.doAction(World.A_SHOOT);
            else if (w.getDirection() == World.DIR_RIGHT)
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_DOWN)
                w.doAction(World.A_TURN_LEFT);
            else
                w.doAction(World.A_TURN_RIGHT);
            }
            else if(w.isVisited(cX, cY+1)) //rules to follow if the positon is visited
            {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_RIGHT && w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_DOWN && w.hasArrow())
                w.doAction(World.A_TURN_RIGHT);
            else if (w.getDirection() == World.DIR_LEFT && !w.hasArrow())
                w.doAction(World.A_TURN_RIGHT);
            else if (!w.hasArrow() && w.isValidPosition(cX, cY+1))
                w.doAction(World.A_MOVE);
            else if (!w.hasArrow())
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
            else
                w.doAction(World.A_SHOOT);
            }
        }
        else if (w.hasStench(cX+1, cY+1))
        {
            if (w.isVisited(cX, cY+1))//the postion has stench and visited  
            {
            if (w.getDirection() == World.DIR_UP)
                w.doAction(World.A_TURN_RIGHT);
            else if (w.getDirection() == World.DIR_RIGHT)
               w.doAction(World.A_SHOOT);
            else if (w.getDirection() == World.DIR_DOWN)
                w.doAction(World.A_TURN_LEFT);
            else
                w.doAction(World.A_TURN_LEFT);
            }
            else if (w.isVisited(cX+1, cY)) //the position has stench and visited 
            {
            if (w.getDirection() == World.DIR_UP)
                w.doAction(World.A_SHOOT);
            else if (w.getDirection() == World.DIR_RIGHT)
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_DOWN)
                w.doAction(World.A_TURN_LEFT);
            else
                w.doAction(World.A_TURN_RIGHT);
            }
        }
        else if (w.hasStench(cX+1, cY-1))
        {
            if (w.isVisited(cX+1, cY)) //visited with stench available in the position
            {
            if (w.getDirection() == World.DIR_UP)
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_RIGHT)
                w.doAction(World.A_TURN_RIGHT);
            else if (w.getDirection() == World.DIR_DOWN)
               w.doAction(World.A_SHOOT);
            else
                w.doAction(World.A_TURN_LEFT);
            }
     
            else if (w.isVisited(cX, cY-1))  
            {
            if (w.getDirection() == World.DIR_UP)
                w.doAction(World.A_TURN_RIGHT);
            else if (w.getDirection() == World.DIR_RIGHT)
                w.doAction(World.A_SHOOT);
            else if (w.getDirection() == World.DIR_DOWN)
                w.doAction(World.A_TURN_LEFT);
            else
                w.doAction(World.A_TURN_LEFT);
            }
        }
        else if (w.hasStench(cX-1, cY-1))
        {
            if (w.isVisited(cX, cY-1)) 
            {
            if (w.getDirection() == World.DIR_UP)
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_RIGHT)
               w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_DOWN)
                w.doAction(World.A_TURN_RIGHT);
            else
               w.doAction(World.A_SHOOT);
            }
            else if (w.isVisited(cX-1, cY))  
            {
            if (w.getDirection() == World.DIR_UP && w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (w.getDirection() == World.DIR_RIGHT && w.hasArrow())
                w.doAction(World.A_TURN_RIGHT);
            else if (w.getDirection() == World.DIR_DOWN && w.hasArrow())
                w.doAction(World.A_SHOOT);
            else if (w.getDirection() == World.DIR_DOWN && !w.hasArrow())
                w.doAction(World.A_TURN_LEFT);
            else if (!w.hasArrow() && w.isValidPosition(cX, cY+1))
                w.doAction(World.A_MOVE);
            else if (!w.hasArrow())
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
            else
                w.doAction(World.A_TURN_LEFT);
            }
        } 
        //rules to follow if the position is invalid and it has arrow
        else if (!w.isValidPosition(cX-1, cY) && !w.isValidPosition(cX, cY-1) && w.hasArrow())
        {
            if(w.getDirection() == World.DIR_RIGHT)
                w.doAction(World.A_SHOOT);
            else if(w.getDirection() == World.DIR_LEFT)
                w.doAction(World.A_TURN_LEFT);
            else if(w.getDirection() == World.DIR_DOWN)
                w.doAction(World.A_TURN_LEFT);
            else
                w.doAction(World.A_TURN_RIGHT);
        }
        else if (w.isUnknown(cX, cY+1) && w.isUnknown(cX+1, cY) && !w.hasArrow())//the positon is unknown and it has no arrow
        {
            w.doAction(World.A_MOVE);
        }
        else if (w.getDirection() == World.DIR_LEFT)
        {
            if (!w.isValidPosition(cX-1, cY))//if the world finds wall 
            {//case 1:one invalid position,three visited and one unknown/empty position
                if((!w.isValidPosition(cX, cY-1) || w.isVisited(cX, cY-1)) && (w.isVisited(cX+1, cY) || (w.isUnknown(cX+1, cY) && w.isVisited(cX+1, cY-1)) ))
                {
                    w.doAction(World.A_TURN_RIGHT);//turn right
                    w.doAction(World.A_SHOOT);//shoot
                }
                else
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                }
            }
            else if(!w.isValidPosition(cX+1, cY) && w.isVisited(cX-1, cY-1))//one invalid position and one visited 
                w.doAction(World.A_MOVE);//move
            else 
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);
            }
        }
        else if (w.getDirection() == World.DIR_RIGHT)//turn right
        {
            if (!w.isValidPosition(cX+1, cY))//the position is not valid
            {//one invalid visited position and one unknown visited position and one visited
                if((!w.isValidPosition(cX, cY-1) || w.isVisited(cX, cY-1)) && (w.isVisited(cX-1, cY) || (w.isUnknown(cX-1, cY) && w.isVisited(cX-1, cY-1))))
                {
                    w.doAction(World.A_TURN_LEFT);//turn left
                    w.doAction(World.A_SHOOT);//shoot
                }
                else
                {
                    w.doAction(World.A_TURN_RIGHT);//turn right
                    w.doAction(World.A_SHOOT);//shoot
                }
            }
            else if(!w.isValidPosition(cX-1, cY) && w.isVisited(cX+1, cY+1))//position is invalid but visited
                w.doAction(World.A_MOVE);//move
            else 
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_MOVE);//move
            }
        }
        if(w.getDirection() == World.DIR_UP && w.hasPit(cX, cY))//facing up and the position has pit
        {
            w.doAction(World.A_TURN_LEFT);//turn left
            w.doAction(World.A_MOVE);//move
        }
    }
    
    /**
     * methods to follow if there is breeze
     * @param cX for the horizontal position
     * @param cY for the vertical position
     */
    public void moveUp(int cX, int cY)
    {     
        if(w.hasBreeze(cX, cY))
        {
            if(w.hasBreeze(cX, cY-1) && w.isVisited(cX, cY-2))
            {
                if(w.hasBreeze(cX, cY-2))//rules to follow if it has breeze
                {
                    w.doAction(World.A_TURN_RIGHT);//turn right
                    w.doAction(World.A_MOVE);//move
                    return;
                }
                if(!w.hasBreeze(cX, cY-2))//rules to follow if it has no breeze
                {
                    w.doAction(World.A_TURN_LEFT);//turn left
                    w.doAction(World.A_TURN_LEFT);//turn left
                    w.doAction(World.A_MOVE);//move
                    return;
                }
            }
        }
        if(w.hasBreeze(cX, cY-1) && !w.isValidPosition(cX, cY-2))//breeze with invalid position
        {
            w.doAction(World.A_MOVE);//move
        }
        if (w.getDirection() == World.DIR_UP)//facing up
        {
            if(!w.isValidPosition(cX-1, cY))//invalid postion
            {
                if(w.isVisited(cX+1, cY))//the tile is visited
                {
                    w.doAction(World.A_MOVE);//move
                    w.doAction(World.A_TURN_RIGHT);//turn right
                }
                else
                {
                    w.doAction(World.A_TURN_RIGHT);//turn right
                    w.doAction(World.A_MOVE);//move
                }
            }
            else if(!w.isValidPosition(cX+1, cY))//the tile is invalid
            {
                if(w.isVisited(cX-1, cY))//the tile is visited
                {
                    if(w.isValidPosition(cX-2, cY) && w.hasBreeze(cX-1, cY))//follow rules if it has breeze and is in valid position
                    {
                        w.doAction(World.A_TURN_RIGHT);//turn right
                        w.doAction(World.A_TURN_RIGHT);//turn right
                        w.doAction(World.A_MOVE);//move
                        w.doAction(World.A_MOVE);//move
                    }
                    else
                    {
                        w.doAction(World.A_MOVE);
                        w.doAction(World.A_TURN_LEFT);
                    }
                }
                else
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
            }
            else if(!w.isValidPosition(cX, cY+1) && !w.isVisited(cX, cY+1))//invalid and not visited positions
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);
            }
            else
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
            }  
        }
    }
    
    /**
     * methods to follow if the breeze is heading down
     * @param cX for the horizontal position
     * @param cY for the vertical position
     */
    public void moveDown(int cX, int cY)
    { 
        if(w.hasBreeze(cX, cY)){
            if(w.hasBreeze(cX, cY+1) && w.isVisited(cX, cY-1))//the postion has breeze and the other position is visited
            {
                w.doAction(World.A_MOVE);//move
                return;
            }
            if(w.hasBreeze(cX-1, cY-1) && w.hasBreeze(cX-1, cY))//both the positons has breeze
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                return;
            }
        }
        if (w.getDirection() == World.DIR_DOWN)//facing down
        {
            if(w.isValidPosition(cX, cY-1) && w.isUnknown(cX, cY-1))//valid and unknwon position
            {
                w.doAction(World.A_MOVE);
                w.doAction(World.A_TURN_LEFT);
            }
            else if(w.isValidPosition(cX+1, cY) && w.isUnknown(cX+1, cY))//valid and unknwon position
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else if(w.isValidPosition(cX-1, cY) && w.isUnknown(cX-1, cY))//valid and unknwon position
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);
            }
            else if(w.isValidPosition(cX+1, cY))//valid position
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_MOVE);
            }
            else if(w.isValidPosition(cX-1, cY))//valid  position
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);
            }
            else
            {
                w.doAction(World.A_TURN_LEFT);
            }
        }
    }
    
    /**
     * the methods for breeze if it is heading right
     * @param cX for the  horizontal position
     * @param cY for the vertical position
     */
    public void moveRight(int cX, int cY)
    {     
        if(w.hasBreeze(cX, cY))
        {
            if(w.hasBreeze(cX-1, cY-1) && !w.isValidPosition(cX-2, cY-1))//if the tile has beeze and invalid 
            {
                w.doAction(World.A_MOVE);//move
            }
            if(!w.isValidPosition(cX+1, cY))//invalid position
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            if(w.hasBreeze(cX, cY-1) && w.hasStench(cX, cY-1) && w.isUnknown(cX-1, cY+1))//rules if it has both stench and breeze
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_MOVE);
                return;
            }
            if(w.hasBreeze(cX, cY-1) && w.hasStench(cX, cY-1) && w.isVisited(cX-1, cY+1))//the position has both stench and breeze and one visited postion
            {
                w.doAction(World.A_MOVE);//move
                return;
            }
            if(w.isValidPosition(cX+1, cY) && w.isUnknown(cX+1, cY) && w.isValidPosition(cX+2, cY) && w.isUnknown(cX+2, cY))//valid positions but unknown
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);//move
            }
            if(w.isValidPosition(cX+1, cY) && w.isUnknown(cX+1, cY) && !w.isValidPosition(cX+2, cY))//if the next position is invalid
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_MOVE);
            }
        }
        if (w.getDirection() == World.DIR_RIGHT)//facing right
        {
            if(w.isValidPosition(cX+1, cY))
            {
                if(w.isUnknown(cX+1, cY))
                    w.doAction(World.A_MOVE);
                else if(w.hasStench(cX-1, cY) || w.hasBreeze(cX-1, cY))//the tile has stench or breeze
                    w.doAction(World.A_MOVE);//move
                else if(w.isVisited(cX+1, cY))
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
            }
            //scenario 1:one invalid,one valid,one visited and one unknown tile
            else if(!(w.isValidPosition(cX+1, cY) && w.isValidPosition(cX, cY+1)) && w.isVisited(cX-1, cY) && w.isUnknown(cX, cY-1))
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);
            }
            //scenario 2:one invalid,one valid,one visited and one unknown tile
            else if(!(w.isValidPosition(cX+1, cY) && w.isValidPosition(cX, cY+1)) && w.isVisited(cX, cY-1) && w.isUnknown(cX-1, cY))
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);//move
            }
            //scenario 3:one invalid,one valid,one visited and one unknown tile
            else if(!w.isValidPosition(cX-1, cY) && w.isVisited(cX+1, cY) && w.isVisited(cX, cY-1) && !w.isValidPosition(cX, cY+1))
                w.doAction(World.A_MOVE);
            else
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_MOVE);//move
                w.doAction(World.A_TURN_LEFT);//turn left
            }
        }
    }
    
    /**
     * method moveLeft specifies rules according to the position
     * @param cX  for the horizontal position
     * @param cY for the vertical position
     */
    public void moveLeft(int cX, int cY)
    { 
        if (w.getDirection() == World.DIR_LEFT)//facing left
        {
            if(w.isValidPosition(cX-1, cY))
            {
                if(w.isUnknown(cX-1, cY))
                    w.doAction(World.A_MOVE);//move
                else if(w.hasStench(cX+1, cY) || w.hasBreeze(cX+1, cY))//the tile has stench or breeze
                    w.doAction(World.A_MOVE);//move
                else if(w.isVisited(cX-1, cY))//the tile is visited
                { 
                    w.doAction(World.A_TURN_RIGHT);//turn right
                    w.doAction(World.A_MOVE);
                }
            }
            //one invalid,one valid,one visited and one unknown positions
            else if(!(w.isValidPosition(cX-1, cY) && w.isValidPosition(cX, cY+1)) && w.isVisited(cX+1, cY) && w.isUnknown(cX, cY-1))
            {
                w.doAction(World.A_TURN_LEFT);//turn left
                w.doAction(World.A_MOVE);//move
            }
            //one invalid,one valid,one visited and one unknown positions
            else if(!(w.isValidPosition(cX-1, cY) && w.isValidPosition(cX, cY+1)) && w.isVisited(cX, cY-1) && w.isUnknown(cX+1, cY))
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);//move
            }
            //two visited and two invalid position
            else if(w.isVisited(cX-1, cY) && !w.isValidPosition(cX+1, cY) && w.isVisited(cX, cY-1) && !w.isValidPosition(cX, cY+1))
                w.doAction(World.A_MOVE);
            else
            {
                w.doAction(World.A_TURN_RIGHT);//turn right
                w.doAction(World.A_MOVE);//move
                w.doAction(World.A_TURN_RIGHT);//turn right
            }
        }
    }
}

