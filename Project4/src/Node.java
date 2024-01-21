import java.util.ArrayList;
import java.util.PriorityQueue;

//import java.util.HashMap;

public class Node {

	int id;
	PriorityQueue<AdjNode> adjacents = new PriorityQueue<>((v1, v2) -> v1.getWeight() - v2.getWeight());
	PriorityQueue<Edge> flagEdges = new PriorityQueue<>((v1, v2) -> v1.getWeight() - v2.getWeight());
	int relativeDistance;
	
	public Node(int id, String[] list) {
		this.id = id;
		for(int i=1;i<list.length;i=i+2) {
			 adjacents.add(new AdjNode(list[i],Integer.valueOf(list[i+1])));
		}
		relativeDistance = Integer.MAX_VALUE;
	}
	
	public Node(Node[] flagNodes) {
		for(int i=0;i<flagNodes.length;i++) {
			if(flagNodes[i].relativeDistance==0) {
				id = flagNodes[i].id;
			}
			else
				flagEdges.add(new Edge(i,flagNodes[i].relativeDistance));
		}
	}

	public void addAdjacent(int id, Integer weight) {
		adjacents.add(new AdjNode(id, weight));
	}
}
