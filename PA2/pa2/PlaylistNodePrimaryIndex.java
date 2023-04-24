import java.util.ArrayList;

public class PlaylistNodePrimaryIndex extends PlaylistNode {
	private ArrayList<Integer> audioIds;
	private ArrayList<PlaylistNode> children;
	
	public PlaylistNodePrimaryIndex(PlaylistNode parent) {
		super(parent);
		audioIds = new ArrayList<Integer>();
		children = new ArrayList<PlaylistNode>();
		this.type = PlaylistNodeType.Internal;
	}
	
	public PlaylistNodePrimaryIndex(PlaylistNode parent, ArrayList<Integer> audioIds, ArrayList<PlaylistNode> children) {
		super(parent);
		this.audioIds = audioIds;
		this.children = children;
		this.type = PlaylistNodeType.Internal;
	}
	
	// GUI Methods - Do not modify
	public ArrayList<PlaylistNode> getAllChildren()
	{
		return this.children;
	}
	
	public PlaylistNode getChildrenAt(Integer index) {return this.children.get(index); }
	
	public Integer audioIdCount()
	{
		return this.audioIds.size();
	}
	public Integer audioIdAtIndex(Integer index) {
		if(index >= this.audioIdCount() || index < 0) {
			return -1;
		}
		else {
			return this.audioIds.get(index);
		}
	}
	
	// Extra functions if needed

public PlaylistNode search(Integer audioID){
		for(int i = 0; i<this.audioIds.size(); i++){
			if (this.audioIds.get(i) > audioID) {
				return this.children.get(i);
			}
		}
		return this.children.get(this.children.size() -1 );
}

	public void printUp(int indent) {

		for(int i = 0; i<indent;i++){
			System.out.print("\t");
		}

		System.out.println("<index>");

		for (int i = 0; i < this.audioIds.size(); i++) {
			for(int j = 0; j<indent;j++){

				System.out.print("\t");
			}

			System.out.println(this.audioIds.get(i));
		}
		for(int i = 0; i<indent;i++){
			System.out.print("\t");
		}
		System.out.println("</index>");
	}


	public Integer findPosition(Integer key){
		Integer position = 0;
		while(position < this.audioIds.size() && key >= this.audioIds.get(position))
			position += 1;

		return position;
	}


	public Integer findChildIndex(PlaylistNode src){
		return this.children.indexOf(src);
	}

	public PlaylistNode findWay(Integer key){
		return this.children.get((findPosition(key)));
	}

	public PlaylistNode getChild(Integer position){
		return this.children.get(position);
	}

	public void addId(Integer position, Integer key){
		this.audioIds.add(position, key);
	}

	public void addChild(Integer position, PlaylistNode child){
		this.children.add(position, child);
	}

	public void updateChild(Integer position, PlaylistNode child){
		this.children.set(position, child);
	}




}
