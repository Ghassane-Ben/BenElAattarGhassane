package US_Airports_Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import US_Airports_DB.DAO;

public class Model {
	
	private List<Flight> flight_passengers=new ArrayList<>();
	private DAO dao;
	private int [] Visited;
	private Graph graph;
	

	public Model() {
		dao=new DAO();		
	    
        graph=new Graph();  
	}
	
	public void loadFlights(int year) {
		
		   flight_passengers=dao.getFlights_Passengers(year);
		   
		   Visited=new int[flight_passengers.size()];
		   
		 
	}
	
	
   int search(int distance, List<Flight> input, int n,int visited[]) {
    
     if (n == 0 || distance == 0)
       return 0;
     
     
     if (input.get(n-1).getDistance() >distance) {
    	 
      return search(distance, input, n-1,visited);
      
     }
   
     else {

       int v1[]=new int[visited.length];
       
       System.arraycopy(visited, 0, v1, 0, v1.length);
       
       int v2[]=new int[visited.length];
       
       System.arraycopy(visited, 0, v2, 0, v2.length);
       
       v1[n-1]=1;

       int ans1 = input.get(n-1).getAvg_passengers() + search(distance-input.get(n-1).getDistance(), input,n-1,v1);
       
       int ans2 = search(distance,input, n-1,v2);
           
       if(ans1>ans2){
    	   
           System.arraycopy(v1, 0, visited, 0, v1.length);
           
            return ans1;
       }
       else{
    	   
           System.arraycopy(v2, 0, visited, 0, v2.length);
           
           return ans2;
       }
       
    }
     
  }
	
	public void printBest(int distance) {
		
		
		
		int n=flight_passengers.size();
		
		int val=search(distance,flight_passengers,n,Visited);
		
		for(int i=0;i<n;i++)
			
	        if(Visited[i]==1) {
	        	
	         if(flight_passengers.get(i).getAvg_passengers()>0 && flight_passengers.get(i).getDistance()>0)
	        	System.out.println(flight_passengers.get(i));
	         
	              }	
	 } 
	
	 	
	public void createGraph() {
			
		for(Flight f:flight_passengers) {
			
			if(!this.graph.containsEdge(f.getOrigin(), f.getDestination())) {
				
				this.graph.addEdge(f.getOrigin(),f.getDestination(),f.getDistance());
				
			}
			
		}
	}
	
	
	private void depthFirstSearch(String vertex,Map<String,Boolean>vis) {
		
		
		vis.put(vertex, true);
			
		
		for(Edge e:this.graph.getNeighbours(vertex)) {
			
						
			     if(!vis.containsKey(e.getDestination())) {
			    	 
			    	 depthFirstSearch(e.getDestination(),vis);
			    	 
			     }	
		}
			
	}
	
	
	public boolean isConnected(String origin,String destination) {
		
         Map<String,Boolean>Vis=new HashMap<String,Boolean>();
         
         if(!this.graph.containsVertex(origin) || !this.graph.containsVertex(destination)) {
        	 
        	 System.out.println("L'aeroporto di origine o destinazione indicato non è presente nel grafo nell'anno indicato");
        	 
        	 return false;
         }
         
         List<Edge> g=this.graph.getNeighbours("Hilo, HI");
         
         
		
		depthFirstSearch(origin,Vis);
		
		if(Vis.containsKey(destination)) {
			
			return true;
		}
		
		
		
		return false;
	}
	
	public Map<Long,List<String>> dijkstraShortestPath(String origin,String destination){
		
		
		
		if(!isConnected(origin,destination)) {
			
			System.out.println("I due aeroporti non sono connessi nell'anno selezionato"); 
			return null;
			
		}
		
		
		PriorityQueue<Edge>pq=new PriorityQueue<Edge>();
		
		long  INF=1000000000;
		
        Map<String,Long>distanza=new HashMap<String,Long>();
        
        Map<String,String>precedente=new HashMap<String,String>();
        
        for(Flight f:this.flight_passengers) {
        	
        	distanza.put(f.getOrigin(),INF);
        	
        	distanza.put(f.getDestination(),INF);
        	
        	
        	
        }
        
              
		Edge inizio=new Edge(origin,0);
		
		pq.add(inizio);
		
		distanza.put(origin,(long) 0);
		
		
		
		while(!pq.isEmpty()) {
			
			Edge curr=pq.peek();
			
			
			String nodo=pq.peek().getDestination();
			
			long peso=pq.peek().getWeight();
			
			pq.poll();
			
			
			if(peso!=distanza.get(nodo)) {
				continue;
			}
			
			for(Edge e:this.graph.getNeighbours(nodo)) {
				
				if(distanza.get(e.getDestination())>peso+e.getWeight()) {
					
					 distanza.put(e.getDestination(),peso+e.getWeight());
					 
					 pq.add(new Edge(e.getDestination(),(int)peso+e.getWeight()));
					 
					 precedente.put(e.getDestination(),nodo);
					
				}
				
				
			}
			
			
			
		}
		
		String corrente=destination;
		
		List<String>percorso=new ArrayList<String>();
		
	    percorso.add(corrente);
		
	    
	    
	    
		while(corrente!=origin) {
			
			corrente=precedente.get(corrente);
			
			percorso.add(corrente);
			
		}
		
         Collections.reverse(percorso);
         
         
         Map<Long,List<String>>risultato=new HashMap<Long,List<String>>();
         
         risultato.put(distanza.get(destination),percorso);
         
         
         
         
         return risultato;
         
         	
	}
	
	
	public void printAll() {
		
		
		for(Flight f:this.flight_passengers) {
			System.out.println(f);
		}
		
		
	}
	

}
