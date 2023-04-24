import java.util.ArrayList;

public class PlaylistNodeSecondaryIndex extends PlaylistNode {
	private ArrayList<String> genres;
	private ArrayList<PlaylistNode> children;

	public PlaylistNodeSecondaryIndex(PlaylistNode parent) {
		super(parent);
		genres = new ArrayList<String>();
		children = new ArrayList<PlaylistNode>();
		this.type = PlaylistNodeType.Internal;
	}

	public PlaylistNodeSecondaryIndex(PlaylistNode parent, ArrayList<String> genres, ArrayList<PlaylistNode> children) {
		super(parent);
		this.genres = genres;
		this.children = children;
		this.type = PlaylistNodeType.Internal;
	}

	// GUI Methods - Do not modify
	public ArrayList<PlaylistNode> getAllChildren() {
		return this.children;
	}

	public PlaylistNode getChildrenAt(Integer index) {

		return this.children.get(index);
	}
	public Integer genreCount() {
		return this.genres.size();
	}

	public String genreAtIndex(Integer index) {
		if (index >= this.genreCount() || index < 0) {
			return "Not Valid Index!!!";
		} else {
			return this.genres.get(index);
		}
	}

	// Extra functions if needed
	public Integer findPosition_s(String genre){
		Integer position = 0;
		while(position < this.genres.size() && genre.compareTo(this.genres.get(position)) >=0)
			position += 1;

		return position;
	}

	public PlaylistNode findWay_s(String genre){
		return this.children.get((findPosition_s(genre)));
	}

	public void addGenre(Integer position, String genre){
		this.genres.add(position, genre);
	}

	public void addChild(Integer position, PlaylistNode child){
		this.children.add(position, child);
	}

	public void updateChild(Integer position, PlaylistNode child){
		this.children.set(position, child);
	}


	public Integer findIndexOfChild(PlaylistNode src){
		return this.children.indexOf(src);
	}

	public PlaylistNode getChild(Integer position){
		return this.children.get(position);
	}






}