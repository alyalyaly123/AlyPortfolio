import java.util.*;

public class Tree<Terminal> {
    private Node<Terminal> root;
    

    public Tree(Terminal rootData) {
        root = new Node<Terminal>();
        ArrayList<Terminal>temp= new ArrayList<Terminal>();
        root.data = rootData;
        root.children = new ArrayList<Node<Terminal>>();
    }
    
    public void addChildren(int current, ArrayList<Terminal> children, int nextID) {
    	root.addChildren(current, children, nextID);
    	
    }
 
    public int treeSize() {
    	return root.size();
    }
    public Terminal find(int number) {
    	return root.find(number);
    }
   
    public ArrayList<Terminal> answer() {
    	 ArrayList<Terminal> theTerminals = new ArrayList<Terminal>();
    	return root.answer(theTerminals);
    	
    }
    
    public int getLevel(int id) {
    	return root.getLevel(id, 0);
    }
    
    public int getNoChildren(int id) {
    	return root.getNoChildren(id);
    }
    
    public int[] getChildrenID(int number) {
    	return root.getChildrenID(number);
    }
    public static class Node<Terminal> {
    	private int ID ;
        private Terminal data;
        private Node<Terminal> parent;
        private ArrayList<Node<Terminal>> children = new ArrayList<Node<Terminal>>();
        
        public Node<Terminal> getByID(int ID) {
        	
        	return this;
   
        }
        public int size() {
        	int noChildren = children.size();
        	int theSize = 1;
        	for(int i =0;i<noChildren;i++) {
        		theSize += children.get(i).size();
        	}
        	return theSize; 
        }
        
        public Terminal find(int number) {
//there is an error with this casting a char to a terminal, not sure where
        	if (ID == number) {
        		return this.data;
        	} else {
        		int noChildren = children.size();
            	for(int i =0;i<noChildren;i++) {
            		Terminal current = children.get(i).find(number);
            		if (current != null) {
            			return current;
            		}
            	}
            	return null; 
        	}
        }
        
        public ArrayList<Terminal> answer(ArrayList<Terminal> theTerminals) {
    
       	if (children.size() > 0) {
       		for (int i = 0;i<children.size();i++) {
       			children.get(i).answer(theTerminals);
       		}
       	} else {
       		theTerminals.add(data);
       		
       	}
       	return theTerminals;        
       }
        
        public int getLevel(int id, int level) {
        	if (id == ID) {
        		return level;
        	} else {
        		int noChildren = children.size();
            	for(int i =0;i<noChildren;i++) {
            		int theReturn = children.get(i).getLevel(id, level+1);
            		if (theReturn != -1) {
            			return theReturn;
            		}
            	}
            	return -1; 
        	}
        }
        public int getNoChildren(int id) {
        	if (ID == id) {
        		return this.children.size();
        	} else {
        		int noChildren = children.size();
            	for(int i =0;i<noChildren;i++) {
            		if (children.get(i).find(id) != null) {
            			return children.get(i).children.size();
            		}
            	}
            	return 0; 
        	}
        }
        
        public int[] getChildrenID(int number) {
        		if (children.size() > 0) {
        	        	if (ID == number) {
        	        		
        	        		int[] childrenID = new int[children.size()]; 
        	        		for (int i =0;i<children.size();i++) {
        	        			childrenID[i] = children.get(i).ID;
        	        		}
        	        		return childrenID;
        	        	} else {
        	        		int noChildren = children.size();
        	        	
        	            	for(int i =0;i<noChildren;i++) {
        	            	int [] current = children.get(i).getChildrenID(number);
        	            		if (current.length != 0) {
        	            			 return current;
        	            		}
        	            	}
        	            	
        	        	}
        		}
        		int[] fakeArray = new int[0];
        	            	return fakeArray; 
        	}
        
        public void addChildren(int current, ArrayList<Terminal> newChildren, int nextID) {
        	if (ID == current) {
        		ArrayList<Node<Terminal>> tempChildren = new ArrayList<Node<Terminal>>();
        		for (int i = 0;i < newChildren.size();i++) {
        		Node newNode = new Node<Terminal>();
        		newNode.data = newChildren.get(i);
        		newNode.ID = nextID + i;
            	
            	tempChildren.add(newNode);
        		}
        		children = tempChildren;
        	} else {
        		int noChildren = children.size();
            	for(int i =0;i<noChildren;i++) {
            		children.get(i).addChildren(current, newChildren, nextID);           			
            		}
            	}
        	}
        	
        	
        	
        	
        
    }
}