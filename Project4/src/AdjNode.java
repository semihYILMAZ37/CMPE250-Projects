
public class AdjNode {

	public int reelID;
	private String id;
	private int weight;
	public AdjNode(String id, int weight) {
		this.id = id;
		this.weight = weight;
	}
	
	public AdjNode(int id, int weight) {
		reelID = id;
		this.weight = weight;
	}
	
	public String getId() {
		return id;
	}
	public int getWeight() {
		return weight;
	}
}
