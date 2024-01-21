import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class project4 {

	static Node[] graph;
	static HashMap<String,Node> graphOld;
	static HashMap<String,Integer> map;
	static Node[] flagGraph;
	static int V;
	static int M;
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.nanoTime();
		
		FileWriter f_writer = new FileWriter(args[1]);
		BufferedReader in = new BufferedReader(new FileReader(args[0]));
		String list[];

		V = Integer.valueOf(in.readLine());
		M = Integer.valueOf(in.readLine());
		map = new HashMap<String,Integer>(V);
		graph = new Node[V];
		list = in.readLine().strip().split(" ");
		String startOld = list[0];
		String finishOld = list[1];
		String[] flagsOld = in.readLine().strip().split(" ");
		for(int i = 0;i<V;i++) {
			list = in.readLine().strip().split(" ");
			graph[i] = new Node(i, list);
			map.put(list[0], i);
			//graphOld.put(list[0], new Node(list));
		}
		in.close();
		
		int start = map.get(startOld);
		int finish = map.get(finishOld);
		int[] flags = new int[M];
		for(int i =0;i<M;i++) {
			flags[i] = map.get(flagsOld[i]);
		}
		
		for(Node node : graph) {
			for(AdjNode adj : node.adjacents) {
				if(adj.getId()!=null) {
					adj.reelID = map.get(adj.getId());
					if(adj.reelID!=node.id) graph[adj.reelID].addAdjacent(node.id, adj.getWeight());
				}
			}
		}
		
		int firstLine = minDistance(graph[start], graph[finish]);
		if(firstLine == Integer.MAX_VALUE)
			firstLine = -1;
		System.out.println("minDistance: " + firstLine);
		f_writer.write(firstLine + "\n");
		
		int secondLine = flags(flags);
		if(secondLine == Integer.MAX_VALUE)
			secondLine = -1;
		System.out.println("flags: " + secondLine);
		f_writer.write(secondLine + "\n");
		
		
		System.out.println(Double.valueOf(System.nanoTime() - startTime) /1000000000);
		f_writer.close();
	}

	private static int minDistance(Node source,Node finish) {
		djikstra(source);
		return finish.relativeDistance;
	}
	
	private static void refreshRelativeDistances() {
		for(Node node : graph) {
			node.relativeDistance = Integer.MAX_VALUE;
		}
	}
	
	private static void djikstra(Node source) {
		source.relativeDistance = 0;

		PriorityQueue<AdjNode> pq = new PriorityQueue<>((v1, v2) -> v1.getWeight() - v2.getWeight());
		pq.add(new AdjNode(source.id, 0));

		Node current;
		Node adj;
		while (pq.size() > 0) {
			current = graph[pq.poll().reelID];
 
			for (AdjNode n : current.adjacents) {
				adj = graph[n.reelID];
				if (current.relativeDistance + n.getWeight() < adj.relativeDistance) {
					adj.relativeDistance	= current.relativeDistance + n.getWeight();
					pq.add(new AdjNode(	n.reelID,adj.relativeDistance));
				}
			}
		}
	}
	
	private static int flags(int[] flags) {
		Node[] flagNodes = new Node[flags.length];
		flagGraph = new Node[flags.length];
		for(int i = 0;i<flags.length;i++) {
			flagNodes[i] = graph[flags[i]];
		}
		for(int i = 0;i<flags.length;i++) {
			refreshRelativeDistances();
			djikstra(flagNodes[i]);
			flagGraph[i] = new Node(flagNodes);
		}
		return prim();
	}
	
	private static int prim() {
		ArrayList<Edge> usedEdges = new ArrayList<Edge>();
		int[] counts = new int[M];
		while(true) {
			int minimumWeight = Integer.MAX_VALUE;
			int currentMinimumID = 0; // ????????
			for(int i =0;i<counts.length;i++) {
				if(counts[i]!=0) {
					while(!flagGraph[i].flagEdges.isEmpty()) {
						if(counts[flagGraph[i].flagEdges.peek().getId()]==0) {
							if(flagGraph[i].flagEdges.peek().getWeight()<minimumWeight) {
								minimumWeight = flagGraph[i].flagEdges.peek().getWeight();
								currentMinimumID = i;
							}
							break;
						}
						else {
							flagGraph[i].flagEdges.poll();
						}
					}
				}
			}
			if(flagGraph[currentMinimumID].flagEdges.isEmpty()) {
				break;
			}
			int nodeToAdd = flagGraph[currentMinimumID].flagEdges.peek().getId();
			if((nodeToAdd==0)&&(counts[0]==1)) {
				break;
			}
			else {
				counts[currentMinimumID] = 1;
				counts[flagGraph[currentMinimumID].flagEdges.peek().getId()] = 1;
				usedEdges.add(flagGraph[currentMinimumID].flagEdges.poll());
			}
		}
		int sum = 0;
		for(Edge edge:usedEdges ) {
			sum = sum + edge.getWeight();
		}
		return sum;
	}

}