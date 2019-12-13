/*used to create graph using the nodes from the Nodes class
 * contains methods to crate and update the graph based on the precepts from the wumpus world environment
 * contains the methods to find shortest path to all the nodes from the current node and make move 
 * towards the nearest unvisited node
 * */
package wumpusworld;
import java.util.HashMap;
class Room{
	World w;
	int cX, cY;
	boolean wumpus_known;
	Node N[][] = new Node[4][4];
	HashMap<Node , Integer> nodes_left = new HashMap<Node , Integer>();
	HashMap<Node , Integer> min_distance = new HashMap<Node , Integer>();
	HashMap<Node , Node> rout = new HashMap<Node , Node>();
	
Room(World world, int x, int y,boolean wumpus_known) {
		this.w = world;
		this.cX = x;
		this.cY = y;
		this.wumpus_known = wumpus_known;
	}
	 /*will create the graph containing 16 nodes corresponding to 16 rooms in wumpus world  
	  */
	 void create_graph(){	 
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){
					this.N[i][j] = new Node();					
				}
			}
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){
					int p = i + 1, q = j + 1;
					if(w.isValidPosition(p, q+1)) this.N[i][j].c1 = this.N[i][j+1];
					if(w.isValidPosition(p+1, q)) this.N[i][j].c2 = this.N[i+1][j];
					if(w.isValidPosition(p, q-1)) this.N[i][j].c3 = this.N[i][j-1];
					if(w.isValidPosition(p-1, q)) this.N[i][j].c4 = this.N[i-1][j];			
					}
				}
	 }
	 

		
	 /*assign's scores to each room based on the precept
	  *act's as a heuristic function and assign's scores to non visited rooms based on the precepts and 
	  * also acts as cost-function with assigns actual costs to the nodes visited
	  */
	 void update_graph() {
		int count = 0;
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				int p = i+1, q = j+1;
				if(w.hasPit(p, q)) {
					System.out.println("I'm in pit");
					count = count +1;
					N[i][j].cost = 1000;
					N[i][j].fix_pit = true;					
				}
			}				
		}
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				int p = i+1, q = j+1;
				if(w.hasBreeze(p, q) && count!=3) {
					System.out.println("I'm in breeze");
					N[i][j].cost = 1;
					if(w.isValidPosition(p, q+1) && !w.isVisited(p, q+1)) this.N[i][j].c1.cost = 50;
					if(w.isValidPosition(p+1, q) && !w.isVisited(p+1, q)) this.N[i][j].c2.cost = 50;
					if(w.isValidPosition(p, q-1) && !w.isVisited(p, q-1)) this.N[i][j].c3.cost = 50;
					if(w.isValidPosition(p-1, q) && !w.isVisited(p-1, q)) this.N[i][j].c4.cost = 50;				
				}
			}				
		}	
		
		
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				int p = i+1, q = j+1;
				if(w.hasStench(p, q) && !this.wumpus_known) {
					System.out.println("I'm in Stench");
					N[i][j].cost = 1;
					if(w.isValidPosition(p, q+1) && !w.isVisited(p, q+1)) this.N[i][j].c1.cost = 5000;
					if(w.isValidPosition(p+1, q) && !w.isVisited(p+1, q)) this.N[i][j].c2.cost = 5000;
					if(w.isValidPosition(p, q-1) && !w.isVisited(p, q-1)) this.N[i][j].c3.cost = 5000;
					if(w.isValidPosition(p-1, q) && !w.isVisited(p-1, q)) this.N[i][j].c4.cost = 5000;					
				}
			}				
		}
		
		
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				int p = i+1, q = j+1;
				if(!w.hasStench(p, q) && !w.hasBreeze(p, q) && w.isVisited(p, q)) {
					System.out.println("I'm in empty");
					if(w.isValidPosition(p, q+1) && !w.isVisited(p, q+1)) this.N[i][j].c1.cost = 1;
					if(w.isValidPosition(p+1, q) && !w.isVisited(p+1, q)) this.N[i][j].c2.cost = 1;
					if(w.isValidPosition(p, q-1) && !w.isVisited(p, q-1)) this.N[i][j].c3.cost = 1;
					if(w.isValidPosition(p-1, q) && !w.isVisited(p-1, q)) this.N[i][j].c4.cost = 1;					
				}
			}				
		}
		
		
		
		
		
		
		for(int i=0; i<4; i++) {
	 		for(int j=0; j<4; j++){	
	 			System.out.println("the cost at node ["+(i+1)+", "+(j+1)+"] is "+N[i][j].cost);
	 		}
	 	}	 		 
	 }
	 
	 /*calculating the shortest path's to the all the unvisited node's and 
	  *Picks the unvisited node which is nearest to the current node and 
	  *makes a move to wards that node
	  */
	 void shortest_path(){
		 
		 Node current_node = N[cX-1][cY-1];
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){	
					// assigning infinity to all nodes
					this.nodes_left.put(N[i][j], 30000);	
				}
			}
		 // distance from start node to start node is 0
		 nodes_left.put(current_node, 0);	
		 
		 // iterates until all the nodes_left hash map gets empty
		 while(this.nodes_left.size() != 0) {
			 // extracting node with minimum key value from the nodes_left
			 Node min = this.extract_min();
			 
			 // placing the node extracted from the nodes_left in the min_distance
			 this.min_distance.put(min, this.nodes_left.get(min));	
			 
			 // removing the the node from the nodes_left
			 this.nodes_left.remove(min);
			 
			 // updating the distances of the neighbors of min 
			 this.update_nodes_left(min);							 			 			 
		 }
		
		 this.min_distance.remove(current_node);
		 
		 // extracting the node corresponding to the minimum key value in min_distance
		 Node dest = this.extract_goal_node();
		 Node final_dest = new Node();
		 while(dest != current_node) {
			 final_dest = dest; 
			 dest = rout.get(dest);
		 }
		 
		/* 
		 * make a move towards the destination node
		 */
		if(current_node.c1 != null && final_dest == current_node.c1) {
			if(w.getDirection() == World.DIR_UP) w.doAction(World.A_MOVE);
	 		else if(w.getDirection() == World.DIR_LEFT) {
	 			w.doAction(World.A_TURN_RIGHT);
	 			w.doAction(World.A_MOVE);
	 			}
	 		else if(w.getDirection() == World.DIR_DOWN) {
	 			w.doAction(World.A_TURN_RIGHT);
	 			w.doAction(World.A_TURN_RIGHT);
	 			w.doAction(World.A_MOVE);
	 		}
	 		else {
	 			w.doAction(World.A_TURN_LEFT);
	 			w.doAction(World.A_MOVE);
	 		}
		 }
		 else if(current_node.c2 != null && final_dest == current_node.c2) {
			 if(w.getDirection() == World.DIR_RIGHT) w.doAction(World.A_MOVE);
     		else if(w.getDirection() == World.DIR_UP) {
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_MOVE);
     		}
     		else if(w.getDirection() == World.DIR_LEFT) {
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_MOVE);
     		}
     		else {
     			w.doAction(World.A_TURN_LEFT);
     			w.doAction(World.A_MOVE);
     		}
		 }
		 else if(current_node.c3 != null && final_dest == current_node.c3) {
			 if(w.getDirection() == World.DIR_DOWN) w.doAction(World.A_MOVE);
     		else if(w.getDirection() == World.DIR_RIGHT) {
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_MOVE);
     		}
     		else if(w.getDirection() == World.DIR_UP) {
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_MOVE);
     		}
     		else {
     			w.doAction(World.A_TURN_LEFT);
     			w.doAction(World.A_MOVE);
     		}
		 }
		 else if((current_node.c4 != null && final_dest == current_node.c4)){
			 if(w.getDirection() == World.DIR_LEFT) w.doAction(World.A_MOVE);
     		else if(w.getDirection() == World.DIR_DOWN) {
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_MOVE);
     		}
     		else if(w.getDirection() == World.DIR_RIGHT) {
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_TURN_RIGHT);
     			w.doAction(World.A_MOVE);
     		}
     		else {
     			w.doAction(World.A_TURN_LEFT);
     			w.doAction(World.A_MOVE);
     		}
		 }
	 }
	 
	 /*
	  updates the key values of the nodes that are attached to the current_node in the nedes_left hash map
	  */	 
	 void update_nodes_left(Node current_node){
		 
		 if(current_node.c1 != null && this.nodes_left.containsKey(current_node.c1)) { 
			 if( (this.min_distance.get(current_node) + current_node.c1.cost) < this.nodes_left.get(current_node.c1)){
				 //System.out.println("connection 1" );
				 this.nodes_left.replace(current_node.c1 , (current_node.c1.cost + this.min_distance.get(current_node)));
				 this.rout.put( current_node.c1, current_node);
			 }
		 }
		 if(current_node.c2 != null && this.nodes_left.containsKey(current_node.c2)) { 			 
			 if( (this.min_distance.get(current_node) + current_node.c2.cost) < this.nodes_left.get(current_node.c2)){	
				 //System.out.println("connection 2" );
				 this.nodes_left.replace(current_node.c2 , (current_node.c2.cost + this.min_distance.get(current_node)));
				 this.rout.put( current_node.c2, current_node);
			 }
		 }
		 if(current_node.c3 != null && this.nodes_left.containsKey(current_node.c3)) { 
			 if( (this.min_distance.get(current_node) + current_node.c3.cost) < this.nodes_left.get(current_node.c3)) {
				 //System.out.println("connection 3" );
				 this.nodes_left.replace(current_node.c3 , (current_node.c3.cost + this.min_distance.get(current_node)));
				 this.rout.put( current_node.c3, current_node);
			 }
		 }
		 if(current_node.c4 != null && this.nodes_left.containsKey(current_node.c4)) { 
			 if( (this.min_distance.get(current_node) + current_node.c4.cost) < this.nodes_left.get(current_node.c4)){
				 //System.out.println("connection 4" );
				 this.nodes_left.replace(current_node.c4 , (current_node.c4.cost + this.min_distance.get(current_node)));
				 this.rout.put( current_node.c4, current_node);
			 }
		 }
		 
	 }
	 
	 /*
	  *extracts node corresponding to minimum key value from the nodes_left hash map 
	  */
	 Node extract_min(){
		 int t = Integer.MAX_VALUE;
		 Node min_node = new Node();
		 for (HashMap.Entry<Node, Integer> pair: this.nodes_left.entrySet()) {
			 
			    if(pair.getValue()<t) {
	            	t = pair.getValue();
	            	min_node = pair.getKey();
	            }
		} 
		return min_node;
	 }
	 
	 /*
	  *extracts the unvisited node corresponding to  minimum key value from the min_distance hash map
	  */
	 Node extract_goal_node(){
		 int t = Integer.MAX_VALUE;
		 Node min_node = new Node();
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){	
					if(w.isVisited(i+1, j+1)) this.min_distance.remove(N[i][j]);
				}
			}
		 
		 
		 for (HashMap.Entry<Node, Integer> pair: this.min_distance.entrySet()) {
			 
			    if(pair.getValue()<t) {
	            	t = pair.getValue();
	            	min_node = pair.getKey();
	            }
		}
		return min_node;
		
	 }
}






