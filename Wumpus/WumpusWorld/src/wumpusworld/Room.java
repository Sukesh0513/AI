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
	 //will crete the graph containing 16 nodes corresponding to 16 rooms in wumpus world  
	 void create_graph(){	 
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){
					this.N[i][j] = new Node();
				}
			}
			for(int i=1; i<3; i++) {
				for(int j=1; j<3; j++){
					this.N[i][j].c1 = this.N[i][j+1];
					this.N[i][j].c2 = this.N[i+1][j];
					this.N[i][j].c3 = this.N[i][j-1];
					this.N[i][j].c4 = this.N[i-1][j];
				}
			}
			
			for(int i=1, j=0; i<3; i++) {				
					this.N[i][j].c1 = this.N[i][j+1];
					this.N[i][j].c2 = this.N[i+1][j];
					this.N[i][j].c4 = this.N[i-1][j];
			}
			
			for(int i=1, j=3; i<3; i++) {
				
					this.N[i][j].c2 = this.N[i+1][j];
					this.N[i][j].c3 = this.N[i][j-1];
					this.N[i][j].c4 = this.N[i-1][j];
				}
				
			
			
				for(int j=1, i=0; j<3; j++){
					this.N[i][j].c1 = this.N[i][j+1];
					this.N[i][j].c2 = this.N[i+1][j];
					this.N[i][j].c3 = this.N[i][j-1];
				}
			
			
			
				for(int j=1, i=3; j<3; j++){
					this.N[i][j].c1 = this.N[i][j+1];
					this.N[i][j].c3 = this.N[i][j-1];
					this.N[i][j].c4 = this.N[i-1][j];
				}
					
			
			this.N[0][0].c1 = this.N[0][1];
			this.N[0][0].c2 = this.N[1][0];
			
			this.N[3][0].c1 = this.N[3][1];
			this.N[3][0].c4 = this.N[2][0];
			
			this.N[0][3].c2 = this.N[1][3];
			this.N[0][3].c3 = this.N[0][2];
			
			this.N[3][3].c3 = this.N[3][2];
			this.N[3][3].c4 = this.N[2][3];			
		}
	 

		
	 //will assign scores to each room based on the percept
	 void update_graph() {
		 int count = 0;		 
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){	
					int p=i+1, q= j+1;
					if(this.w.hasBreeze(p, q)) {
						if(w.isValidPosition(p, q+1)) {
							N[i][j+1].cost = 50;
							N[i][j+1].pit = true;
						}
						if(w.isValidPosition(p+1, q)) {
							N[i+1][j].cost = 50;
							N[i+1][j].pit = true;
						}
						if(w.isValidPosition(p, q-1)) {
							N[i][j-1].cost = 50;
							N[i][j-1].pit = true;
						}
						if(w.isValidPosition(p-1, q)) {
							N[i-1][j].cost = 50;
							N[i-1][j].pit = true;
						}
						
						if(w.isVisited(p+1, q+1) && !w.hasBreeze(p+1, q+1)){
							System.out.println("check at 1,1 for ["+p+", "+q+"]");
							if(w.isValidPosition(p, q+1)) {
								N[i][j+1].cost = 1;
								N[i][j+1].pit = false;
							}
							if(w.isValidPosition(p+1, q)) {
								N[i+1][j].cost = 1;
								N[i+1][j].pit = false;
							}
						}
					
						if(w.isVisited(p+1, q-1) && !w.hasBreeze(p+1, q-1)){
							System.out.println("check at 1,-1 for ["+p+", "+q+"]");
							if(w.isValidPosition(p, q-1)) {
								N[i][j-1].cost = 1;
								N[i][j-1].pit = false;
							}
							if(w.isValidPosition(p+1, q)) {
								N[i+1][j].cost = 1;
								N[i+1][j].pit = false;
							}
						}
						if(w.isVisited(p-1, q-1) && !w.hasBreeze(p-1, q-1)){
							System.out.println("check at -1,-1 for ["+p+", "+q+"]");
							if(w.isValidPosition(p-1, q)) {
								N[i-1][j].cost = 1;
								N[i-1][j].pit = false;
							}
							if(w.isValidPosition(p, q-1)) {
								N[i][j-1].cost = 1;
								N[i][j-1].pit = false;
							}
						}
						if(w.isVisited(p-1, q+1) && !w.hasBreeze(p-1, q+1)){
							System.out.println("check at -1,1 for ["+p+", "+q+"]");
							if(w.isValidPosition(p-1, q)) {
								N[i-1][j].cost = 1;
								N[i-1][j].pit = false;
							}
							if(w.isValidPosition(p, q+1)) {
								N[i][j+1].cost = 1;
								N[i][j+1].pit = false;
							}
						}
					if(w.isVisited(p, q+1) && !w.hasPit(p, q+1)) {
						N[i][j+1].cost = 1;
						N[i][j+1].pit = false;
					}
					if(w.isVisited(p+1, q) && !w.hasPit(p+1, q)) {
						N[i+1][j].cost = 1;
						N[i+1][j].pit = false;
					}
					if(w.isVisited(p, q-1) && !w.hasPit(p, q-1)) {
						N[i][j-1].cost = 1;
						N[i][j-1].pit = false;
					}
					if(w.isVisited(p-1, q) && !w.hasPit(p-1, q)) {
						N[i-1][j].cost = 1;
						N[i-1][j].pit = false;
					}
					
					if(w.isVisited(p, q+1) && w.hasPit(p, q+1)) {
						N[i][j+1].cost = 1000;
						N[i][j+1].pit = true;
					}					
					if(w.isVisited(p+1, q) && w.hasPit(p+1, q)) {
						N[i+1][j].cost = 1000;
						N[i+1][j].pit = true;
					}
					if(w.isVisited(p, q-1) && w.hasPit(p, q-1)) {
						N[i][j-1].cost = 1000;
						N[i][j-1].pit = true;
					}
					if(w.isVisited(p-1, q) && w.hasPit(p-1, q)) {
						N[i-1][j].cost = 1000;
						N[i-1][j].pit = true;
					}					
				}
				
				if(w.hasPit(p, q))	
				{
					N[i][j].cost=1000;
				}						
					
					if(w.wumpusAlive() && !wumpus_known) {						
						if(w.hasStench(p, q)) {
							if(w.hasStench(p+1, q+1)) {            	
				            	if(!(w.hasStench(p+1, q)) && w.isVisited(p+1, q)) {
				            		N[i][j+1].cost = 5000;
				            		N[i][j+1].wumpus = true;
				            	}
				            	else if(!(w.hasStench(p, q+1)) && w.isVisited(p, q+1)) {
				            		N[i+1][j].cost = 5000;
				            		N[i+1][j].wumpus = true;
				            	}
				            }
				            
							else if(w.hasStench(p+1, q-1)) {
				            	
				            	if(!(w.hasStench(p+1, q)) && w.isVisited(p+1, q)) {
				            		N[i][j-1].cost = 5000;
				            		N[i][j-1].wumpus = true;
				            	}
				            	else if(!(w.hasStench(p, q-1)) && w.isVisited(p, q-1)) {
				            		N[i+1][j].cost = 5000;
				            		N[i+1][j].wumpus = true;
				            	}  	
				            }
				            
							else if(w.hasStench(p-1, q-1)) {
				            	if(!(w.hasStench(p-1, q)) && w.isVisited(p-1, q)) {
				            		N[i][j-1].cost = 5000;
				            		N[i][j-1].wumpus = true;
				            	}
				            	else if(!(w.hasStench(p, q-1)) && w.isVisited(p, q-1)) {
				            		N[i-1][j].cost = 5000; 
				            		N[i-1][j].wumpus = true;
				            	}
				            }
				            
							else if(w.hasStench(p-1, q+1)) {
				            	if(!(w.hasStench(p, q+1)) && w.isVisited(p, q+1)) {
				            		N[i-1][j].cost = 5000;
				            		N[i-1][j].wumpus = true;
				            	}
				            	else if(!(w.hasStench(p-1, q)) && w.isVisited(p, q)) {
				            		N[i][j+1].cost = 5000;
				            		N[i][j+1].wumpus = true;
				            	}
				            }
				            
							else if(w.hasStench(p+2, q) && w.isVisited(p+2, q)) {
				            	N[i+1][j].cost = 5000;
				            	N[i+1][j].wumpus = true;
				            	} 
				            
							else if(w.hasStench(p-2, q) && w.isVisited(p-2, q)) {
				            	N[i-1][j].cost = 5000;
				            	N[i-1][j].wumpus = true;
				            }
				            
				           else if(w.hasStench(p, q+2) && w.isVisited(p, q+2)) {
				            	N[i][j+1].cost = 5000; 
				            	N[i][j+1].wumpus = true;
				            }
				            
				           else if(w.hasStench(p, q-2) && w.isVisited(p, q-2)) {
				            	N[i][j-1].cost = 5000;
				            	N[i][j-1].wumpus = true;
				            }
							
				           //else {
								if(w.isValidPosition(p, q+1) && !w.isVisited(p, q+1) ) {
									System.out.println("dbug1");
									N[i][j+1].cost = 5000;
									N[i][j+1].wumpus = true;
								}
								if(w.isValidPosition(p+1, q) && !w.isVisited(p+1, q)) {
									System.out.println("dbug2");
									N[i+1][j].cost = 5000;
									N[i+1][j].wumpus = true;
								}
								if(w.isValidPosition(p, q-1) && !w.isVisited(p, q-1)) {
									System.out.println("dbug3");
									N[i][j-1].cost = 5000;
									N[i][j-1].wumpus = true;
								}
								if(w.isValidPosition(p-1, q) && !w.isVisited(p-1, q)) {
									System.out.println("dbug4");
									N[i-1][j].cost = 5000;
									N[i-1][j].wumpus = true;
								}
								
								if(w.isVisited(p, q+1) && !w.hasPit(p, q+1)) {
									N[i][j+1].cost = 1;
									N[i][j+1].wumpus = false;
								}
								if(w.isVisited(p+1, q) && !w.hasPit(p+1, q)) {
									N[i+1][j].cost = 1;
									N[i+1][j].wumpus = false;
								}
								if(w.isVisited(p, q-1) && !w.hasPit(p, q-1)) {
									N[i][j-1].cost = 1;
									N[i][j-1].wumpus = false;
								}
								if(w.isVisited(p-1, q) && !w.hasPit(p-1, q)) {
									N[i-1][j].cost = 1;
									N[i-1][j].wumpus = false;
								}
								if(w.isVisited(p, q+1) && w.hasPit(p, q+1)) {
									N[i][j+1].cost = 1000;
									N[i][j+1].wumpus = false;
								}
								if(w.isVisited(p+1, q) && w.hasPit(p+1, q)) {
									N[i+1][j].cost = 1000;
									N[i+1][j].wumpus = false;
								}
								if(w.isVisited(p, q-1) && w.hasPit(p, q-1)) {
									N[i][j-1].cost = 1000;
									N[i][j-1].wumpus = false;
								}
								if(w.isVisited(p-1, q) && w.hasPit(p-1, q)) {
									N[i-1][j].cost = 1000;
									N[i-1][j].wumpus = false;
								}
				            //}
							
				           //if(!w.hasStench(p+1, q+1) && w.isVisited(p+1, q+1) && !w.hasPit(p+1, q+1)) {
							if(!w.hasStench(p+1, q+1) && w.isVisited(p+1, q+1)) {
								System.out.println("check at (1,1)");
			            		if(w.isValidPosition(p, q+1)) {
			            			System.out.println("dbug6");
			            			if(N[i][j+1].pit == false){
					            		N[i][j+1].cost = 1;
					            		N[i][j+1].wumpus = false;
			            			}
			            			else {
			            				N[i][j+1].cost = 50;
			            				N[i][j+1].wumpus = false;
			            			}
			            		}
			            		
			            		if(w.isValidPosition(p+1, q)) {
				            		if(N[i+1][j].pit == false) {
				            			System.out.println("dbug7");
				            			N[i+1][j].cost = 1;
					            		N[i+1][j].wumpus = false;
				            		}
				            		else {
			            				N[i+1][j].cost = 50;
			            				N[i+1][j].wumpus = false;
			            			}
			            		}
				            	
				            }
				            
				            if(!w.hasStench(p+1, q-1) && w.isVisited(p+1, q-1)) { 
				            	System.out.println("check at (1,-1)");
				            	if(w.isValidPosition(p, q-1)) {
				            		if(N[i][j-1].pit == false) {
				            			System.out.println("dbug8");
					            		N[i][j-1].cost = 1;
					            		N[i][j-1].wumpus = false;
				            		}
				            		else {
			            				N[i][j-1].cost = 50;
			            				N[i][j-1].wumpus = false;
			            			}
				            	}
				            	if(w.isValidPosition(p+1, q)) {
				            		if(N[i+1][j].pit == false) {
				            			System.out.println("dbug9");
					            		N[i+1][j].cost = 1;
					            		N[i+1][j].wumpus = false;
				            		}
				            		else {
			            				N[i+1][j].cost = 50;
			            				N[i+1][j].wumpus = false;
			            			}
				            	}
				            	 	
				            }
				            
				            if(!w.hasStench(p-1, q-1) && w.isVisited(p-1, q-1)) {
				            	System.out.println("check at (-1,-1)");
				            	if(w.isValidPosition(p, q-1)) {
				            		if(N[i][j-1].pit == false) {
				            			System.out.println("dbug10");
					            		N[i][j-1].cost = 1;
					            		N[i][j-1].wumpus = false;
				            		}
				            		else {
			            				N[i][j-1].cost = 50;
			            				N[i][j-1].wumpus = false;
			            			}
				            	}
				            	if(w.isValidPosition(p-1, q)) {
				            		if(N[i-1][j].pit == false) {
				            			System.out.println("dbug11");
					            		N[i-1][j].cost = 1; 
					            		N[i-1][j].wumpus = false;
				            		}
				            		else {
			            				N[i-1][j].cost = 50;
			            				N[i-1][j].wumpus = false;
			            			}
				            	}
				            }
				            
				            if(!w.hasStench(p-1, q+1) && w.isVisited(p-1, q+1)) {
				            	System.out.println("check at (-1,1)");
				            	if(w.isValidPosition(p-1, q)) {
				            		if(N[i-1][j].pit == false) {
				            			System.out.println("dbug12");
					            		N[i-1][j].cost = 1;
					            		N[i-1][j].wumpus = false;
				            		}
				            		else {
			            				N[i-1][j].cost = 50;
			            				N[i-1][j].wumpus = false;
			            			}
				            	}
				            	if(w.isValidPosition(p, q+1)) {
				            		if(N[i][j+1].pit == false) {
				            			System.out.println("dbug13");
					            		N[i][j+1].cost = 1;
					            		N[i][j+1].wumpus = false;
				            		}
				            		else {
			            				N[i][j+1].cost = 50;
			            				N[i][j+1].wumpus = false;
			            			}
				            	}
				            }
				            
				            if(!w.hasStench(p+2, q) && w.isVisited(p+2, q)) {
				            	if(N[i+1][j].pit == false) {
					            	N[i+1][j].cost = 1;
					            	N[i+1][j].wumpus = false;
				            	}
				            	else {
				            		N[i+1][j].cost = 50;
					            	N[i+1][j].wumpus = false;
				            	} 
				            } 
				            
				            if(!w.hasStench(p-2, q) && w.isVisited(p-2, q)) {
				            	if(N[i-1][j].pit == false) {
				            		N[i-1][j].cost = 1;				            	
				            		N[i-1][j].wumpus = false;
				            	}
				            	else {
				            		N[i-1][j].cost = 50;				            	
				            		N[i-1][j].wumpus = false;
				            	}
				            }
				            
				            if(!w.hasStench(p, q+2) && w.isVisited(p, q+2)) {
				            	if(N[i][j+1].pit == false) {
					            	N[i][j+1].cost = 1; 
					            	N[i][j+1].wumpus = false;
				            	}
				            	else {
				            		N[i][j+1].cost = 50; 
					            	N[i][j+1].wumpus = false;
				            	}
				            }
				            
				            if(!w.hasStench(p, q-2) && w.isVisited(p, q-2)) {
				            	if(N[i][j-1].pit == false) {
					            	N[i][j-1].cost = 1;
					            	N[i][j-1].wumpus = false;
				            	}
				            	else {
				            		N[i][j-1].cost = 50;
					            	N[i][j-1].wumpus = false;
				            	}
				            }
				            

						}								
					}
					
					if(!w.wumpusAlive()) {
							N[i][j].wumpus = false;
							N[i][j].steanch = false;						 
					}
					
					if(w.hasPit(p, q)) {
						count++;
						N[i][j].fix_pit = true;
						if(count == 3) {
							for(int u=0; u<4; u++) {
						 		for(int v=0; v<4; v++){
						 			if(N[u][v].fix_pit == false && N[u][v].wumpus == false) N[u][v].cost = 1;						 			
						 		}
						 	}
						}
					
					}
				}
		 }		 
		  for(int i=0; i<4; i++) {
		 		for(int j=0; j<4; j++){	
		 			System.out.println("the cost at node ["+(i+1)+", "+(j+1)+"] is "+N[i][j].cost);
		 		}
		 	}	 		 
	 }
	 
	 //calculating the shortest path using A* algirithm and doing the best move
	 void shortest_path(){
		 
		 Node current_node = N[cX-1][cY-1];
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){	
					this.nodes_left.put(N[i][j], 30000); // assigning infinity to all nodes
				}
			}		 
		 nodes_left.put(current_node, 0); // distance from start to start is 0
		 while(this.nodes_left.size() != 0) {
			 Node min = this.extract_min(); // storing the min from nodes_left
			 this.min_distance.put(min, this.nodes_left.get(min));
			 this.nodes_left.remove(min);
			 this.update_nodes_left(min);							 			 			 
		 }
		
		 this.min_distance.remove(current_node);
		 Node dest = this.extract_goal_node();
		 Node final_dest = new Node();
		 while(dest != current_node) {
			 final_dest = dest; 
			 dest = rout.get(dest);
		 }	
		 
		if(current_node.c1 != null && final_dest == current_node.c1) {
			System.out.println("up");
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
			 System.out.println("right");
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
			 System.out.println("down");
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
			 System.out.println("left");
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
	 
	 
	 Node extract_min(){
		 int t = Integer.MAX_VALUE;
		 Node min_node = new Node();
		 for (HashMap.Entry<Node, Integer> pair: this.nodes_left.entrySet()) {
			 
			    if(pair.getValue()<t) {
	            	t = pair.getValue();
	            	min_node = pair.getKey();
	            }
		}
		//System.out.println("cast at min node is "+this.nodes_left.get(min_node)); 
		return min_node;
	 }
	 
	 
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



// breeze at corners only 2 possibilities
// forth block daggara breese and steaqn ch fails avutunai
// have to check wumpus shoot conditions


