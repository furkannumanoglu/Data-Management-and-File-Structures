import java.util.ArrayList;
import java.util.Stack;

public class PlaylistTree {
	
	public PlaylistNode primaryRoot;		//root of the primary B+ tree
	public PlaylistNode secondaryRoot;	//root of the secondary B+ tree
	public PlaylistTree(Integer order) {
		PlaylistNode.order = order;
		primaryRoot = new PlaylistNodePrimaryLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new PlaylistNodeSecondaryLeaf(null);
		secondaryRoot.level = 0;
	}
	
	public void addSong(CengSong song) {
		// TODO: Implement this method
		int size, flag = 0;
		PlaylistNodePrimaryLeaf targetLeaf_p = ((PlaylistNodePrimaryLeaf) findLeaf_p(song));
		PlaylistNodePrimaryIndex parent_node_p;

		//FOR PRIMARY TREE
		if(targetLeaf_p.songCount()==0){
			targetLeaf_p.addSong(0,song);
		}

		else if(targetLeaf_p.songCount() >= 2*PlaylistNode.order){

			parent_node_p = up1(targetLeaf_p);
			while(parent_node_p.audioIdCount() > 2*PlaylistNode.order) {
				parent_node_p = up2(parent_node_p);
			}
			if(parent_node_p.getParent() == null){
				primaryRoot = parent_node_p;}
			flag = 1;
			this.addSong(song);
		}

		else{
			for(int i = 0; i<targetLeaf_p.songCount();i++){
				size = targetLeaf_p.songCount();

				if(i == size-1){//tek eleman varsa ve o eleman song.audioId den büyükse başına koysun
					if(targetLeaf_p.audioIdAtIndex(i)>song.audioId()){
						targetLeaf_p.addSong(i,song);
						break;
					}
					targetLeaf_p.addSong(i+1,song);
					break;
				}
				else if(song.audioId() < targetLeaf_p.audioIdAtIndex(i)){
					targetLeaf_p.addSong(i,song);
					break;
				}
			}
		}

		if(flag==0){
			this.addSong_s(song);
		}
	}
	
	public CengSong searchSong(Integer audioId) {
		// TODO: Implement this method
		// find the song with the searched audioId in primary B+ tree
		// return value will not be tested, just print according to the specifications
		CengSong song = null;
		int flag = 0;
		ArrayList<PlaylistNode> result = new ArrayList<>(); //result_1, result_2 ?
		ArrayList<CengSong> cengsongs_p = new ArrayList<>();
		PlaylistNode root_p = this.primaryRoot;

		while(root_p != null) {
			result.add(root_p);

			if(root_p.type == PlaylistNodeType.Internal) {
				PlaylistNodePrimaryIndex nodeIndex = (PlaylistNodePrimaryIndex) root_p;
				root_p = nodeIndex.search(audioId);
			}
			else if (root_p.type == PlaylistNodeType.Leaf){
				PlaylistNodePrimaryLeaf nodePrimaryLeaf = (PlaylistNodePrimaryLeaf) root_p;
				cengsongs_p = nodePrimaryLeaf.getSongs();
				for(int i = 0; i < cengsongs_p.size(); i++){
					if(cengsongs_p.get(i).audioId().equals(audioId)){
						song = cengsongs_p.get(i);
						flag = 1;
						break;
					}
				}
				if(flag == 1){break;}
			}
		}

		if(song == null){
			System.out.println("Could not find "+audioId+".");
			return null;
		}
		for(int i = 0; i<result.size(); i++){
			if(result.get(i).type == PlaylistNodeType.Internal){
				PlaylistNodePrimaryIndex nodePrimaryIndex = (PlaylistNodePrimaryIndex) result.get(i);
				nodePrimaryIndex.printUp(i);
			}
			else {
				for(int j = 0; j<i;j++){
					System.out.print("\t");
				}
				System.out.println("<data>");

				for(int j = 0; j<i;j++){

					System.out.print("\t");
				}
				System.out.println("<record>" + song.fullName() + "</record>");

				for(int j = 0; j<i;j++){
					System.out.print("\t");
				}
				System.out.println("</data>");
			}
		}
		return song;
	}
	public void printPrimaryPlaylist() {
		// TODO: Implement this method
		// print the primary B+ tree in Depth-first order

		PlaylistNodePrimaryIndex tempinternal;
		PlaylistNode pRoot = primaryRoot;

		if(pRoot.type == PlaylistNodeType.Internal){
			tempinternal = (PlaylistNodePrimaryIndex) pRoot;
			ArrayList<PlaylistNode> children = tempinternal.getAllChildren();

			System.out.println("<index>");
			for(int i=0; i<tempinternal.audioIdCount();i++){
				System.out.println(tempinternal.audioIdAtIndex(i));
			}
			System.out.println("</index>");

			if(children.get(0).type == PlaylistNodeType.Internal){
				for (int i = 0; i <= children.size() - 1 ; i++)
					printprimindex((PlaylistNodePrimaryIndex) children.get(i), 1);
			}
			else if(children.get(0).type == PlaylistNodeType.Leaf){
				for (int i = 0; i <= children.size() - 1 ; i++)
					printprimleaf((PlaylistNodePrimaryLeaf) children.get(i), 1);
			}
		}
		else if(pRoot.type == PlaylistNodeType.Leaf){
			printprimleaf((PlaylistNodePrimaryLeaf) pRoot, 0);
		}
	}
	public void printSecondaryPlaylist() {
		// TODO: Implement this method
		// print the secondary B+ tree in Depth-first order

		PlaylistNodeSecondaryIndex tempinternal;
		PlaylistNode pRoot = secondaryRoot;

		if(pRoot.type == PlaylistNodeType.Internal){
			tempinternal = (PlaylistNodeSecondaryIndex) pRoot;
			ArrayList<PlaylistNode> children = tempinternal.getAllChildren();

			System.out.println("<index>");
			for(int i=0; i<tempinternal.genreCount();i++){
				System.out.println(tempinternal.genreAtIndex(i));
			}
			System.out.println("</index>");

			if(children.get(0).type == PlaylistNodeType.Internal){
				for (int i = 0; i <= children.size() - 1 ; i++)
					printprimindex_s((PlaylistNodeSecondaryIndex) children.get(i), 1);
			}
			else if(children.get(0).type == PlaylistNodeType.Leaf){
				for (int i = 0; i <= children.size() - 1 ; i++)
					printprimleaf_s((PlaylistNodeSecondaryLeaf) children.get(i), 1);
			}
		}
		else if(pRoot.type == PlaylistNodeType.Leaf){
			printprimleaf_s((PlaylistNodeSecondaryLeaf) pRoot, 0);
		}
	}
	
	// Extra functions if needed
	private PlaylistNode findLeaf_p(CengSong song){
		PlaylistNode tmp = primaryRoot;
		while(tmp.getType() != PlaylistNodeType.Leaf){
			tmp = ((PlaylistNodePrimaryIndex) tmp).findWay(song.audioId());
		}
		return tmp;
	}
	private PlaylistNode findLeaf_s(CengSong song){
		PlaylistNode temp = secondaryRoot;
		while(temp.getType() != PlaylistNodeType.Leaf){
			temp = ((PlaylistNodeSecondaryIndex) temp).findWay_s(song.genre());
		}
		return temp;
	}
	private PlaylistNodePrimaryIndex up1(PlaylistNodePrimaryLeaf target){
		Integer midPos =  target.songCount()/2;
		Integer midId = target.audioIdAtIndex(midPos);
		PlaylistNodePrimaryIndex parent_node = (PlaylistNodePrimaryIndex) target.getParent();
		PlaylistNodePrimaryLeaf left_node = new PlaylistNodePrimaryLeaf(parent_node);

		for(int i = 0; i < midPos; i++) {
			left_node.addSong(i, target.getSongs().get(i));
		}

		PlaylistNodePrimaryLeaf right_node = new PlaylistNodePrimaryLeaf(parent_node);

		for(int i = midPos; i < target.songCount(); i++) {
			right_node.addSong(i - midPos, target.getSongs().get(i));
		}

		if(parent_node == null){
			parent_node = new PlaylistNodePrimaryIndex(null);
			left_node.setParent(parent_node);
			right_node.setParent(parent_node);

			parent_node.addId(0, midId);
			parent_node.addChild(0, left_node);
			parent_node.addChild(1, right_node);
		}else {
			Integer idPos = parent_node.findChildIndex(target);
			parent_node.addId(idPos, midId);
			parent_node.updateChild(idPos, left_node);
			parent_node.addChild(idPos + 1, right_node);
		}
		return parent_node;
	}
	private PlaylistNodeSecondaryIndex up1_s(PlaylistNodeSecondaryLeaf target, int z){
		Integer midPos =  target.genreCount()/2 + z;
		String midGenre = target.genreAtIndex(midPos);
		PlaylistNodeSecondaryIndex parent_node = (PlaylistNodeSecondaryIndex) target.getParent();

		PlaylistNodeSecondaryLeaf left_node = new PlaylistNodeSecondaryLeaf(parent_node);

		for(int i = 0; i < midPos; i++) {
			left_node.addSong(i, target.getSongBucket().get(i).get(0));
		}

		PlaylistNodeSecondaryLeaf right_node = new PlaylistNodeSecondaryLeaf(parent_node);

		for(int i = midPos; i < target.genreCount(); i++) {
			right_node.addSong(i - midPos, target.getSongBucket().get(i).get(0));
		}

		if(parent_node == null){
			parent_node = new PlaylistNodeSecondaryIndex(null);
			left_node.setParent(parent_node);
			right_node.setParent(parent_node);

			parent_node.addGenre(0, midGenre);
			parent_node.addChild(0, left_node);
			parent_node.addChild(1, right_node);
		}else {
			Integer tPos = parent_node.findIndexOfChild(target);
			parent_node.addGenre(tPos, midGenre);
			parent_node.updateChild(tPos, left_node);
			parent_node.addChild(tPos + 1, right_node);
		}
		return parent_node;
	}
	private PlaylistNodePrimaryIndex up2(PlaylistNodePrimaryIndex target){
		Integer mid_position =  target.audioIdCount()/2;
		Integer mid_key = target.audioIdAtIndex(mid_position);
		PlaylistNodePrimaryIndex parent_node = (PlaylistNodePrimaryIndex) target.getParent();
		PlaylistNode tmp;
		Integer tmp_id;

		PlaylistNodePrimaryIndex left_node = new PlaylistNodePrimaryIndex(parent_node);

		for(int i = 0; i < mid_position; i++) {
			tmp = target.getChild(i);
			tmp.setParent(left_node);
			left_node.addChild(i, tmp);

			tmp_id = target.audioIdAtIndex(i);
			left_node.addId(i, tmp_id);
		}

		PlaylistNodePrimaryIndex right_node = new PlaylistNodePrimaryIndex(parent_node);

		for(int i = mid_position + 1; i < target.audioIdCount(); i++) {
			tmp = target.getChild(i);
			tmp.setParent(right_node);
			right_node.addChild(i - mid_position - 1, tmp);

			tmp_id = target.audioIdAtIndex(i);
			right_node.addId(i - mid_position - 1, tmp_id);
		}

		tmp = target.getChild(mid_position);
		tmp.setParent(left_node);
		left_node.addChild(mid_position, tmp);

		tmp = target.getChild(target.audioIdCount());
		tmp.setParent(right_node);
		right_node.addChild(target.audioIdCount() - mid_position -1, tmp);

		if(parent_node == null){
			parent_node = new PlaylistNodePrimaryIndex(null);
			left_node.setParent(parent_node);
			right_node.setParent(parent_node);

			parent_node.addId(0, mid_key);
			parent_node.addChild(0, left_node);
			parent_node.addChild(1, right_node);
		}else {
			Integer key_position = parent_node.findChildIndex(target);
			parent_node.addId(key_position, mid_key);
			parent_node.updateChild(key_position, left_node);
			parent_node.addChild(key_position + 1, right_node);
		}
		return parent_node;
	}
	private PlaylistNodeSecondaryIndex up2_s(PlaylistNodeSecondaryIndex target){
		Integer mid_position =  target.genreCount()/2;
		String mid_genre = target.genreAtIndex(mid_position);
		PlaylistNodeSecondaryIndex parent_node = (PlaylistNodeSecondaryIndex) target.getParent();

		PlaylistNode tmp;
		String tmp_genre;

		// create left node
		PlaylistNodeSecondaryIndex left_node = new PlaylistNodeSecondaryIndex(parent_node);

		for(int i = 0; i < mid_position; i++) {
			tmp = target.getChild(i);
			tmp.setParent(left_node);
			left_node.addChild(i, tmp);

			tmp_genre = target.genreAtIndex(i);
			left_node.addGenre(i, tmp_genre);
		}

		// create right node
		PlaylistNodeSecondaryIndex right_node = new PlaylistNodeSecondaryIndex(parent_node);

		for(int i = mid_position + 1; i < target.genreCount(); i++) {
			tmp = target.getChild(i);
			tmp.setParent(right_node);
			right_node.addChild(i - mid_position - 1, tmp);

			tmp_genre = target.genreAtIndex(i);
			right_node.addGenre(i - mid_position - 1, tmp_genre);
		}

		// Special cases
		tmp = target.getChild(mid_position);
		tmp.setParent(left_node);
		left_node.addChild(mid_position, tmp);

		tmp = target.getChild(target.genreCount());
		tmp.setParent(right_node);
		right_node.addChild(target.genreCount() - mid_position -1, tmp);

		// If Internal node is root
		if(parent_node == null){
			parent_node = new PlaylistNodeSecondaryIndex(null);
			left_node.setParent(parent_node);
			right_node.setParent(parent_node);

			parent_node.addGenre(0, mid_genre);
			parent_node.addChild(0, left_node);
			parent_node.addChild(1, right_node);
		}else {
			Integer key_position = parent_node.findIndexOfChild(target);
			parent_node.addGenre(key_position, mid_genre);
			parent_node.updateChild(key_position, left_node);
			parent_node.addChild(key_position + 1, right_node);
		}

		return parent_node;
	}
	public void addSong_s(CengSong song) {
		int leaf_size, is=0;
		PlaylistNodeSecondaryLeaf targetLeaf_s = ((PlaylistNodeSecondaryLeaf) findLeaf_s(song));
		PlaylistNodeSecondaryIndex parent_node_s;

		leaf_size = targetLeaf_s.genreCount();
		for(int i = 0; i < leaf_size ;i++){
			if(targetLeaf_s.genreAtIndex(i).equals(song.genre())){
				is = 1;
				break;
			}
		}
		if(leaf_size==0){
			targetLeaf_s.addSong(0,song);
		}
		else if(leaf_size >= 2*PlaylistNode.order && is==0){
			Integer mid_position =  targetLeaf_s.genreCount()/2;
			int z = 0;
			if(song.genre().compareTo(targetLeaf_s.genreAtIndex(mid_position)) > 0){
				z = 1;
			}
			parent_node_s = up1_s(targetLeaf_s,z);
			while(parent_node_s.genreCount() > 2*PlaylistNode.order) {
				parent_node_s = up2_s(parent_node_s);
			}
			if(parent_node_s.getParent() == null){
				secondaryRoot = parent_node_s;}
			if(is==0){
				this.addSong_s(song);
			}
		}
		else{
			for(int i = 0; i<targetLeaf_s.genreCount();i++){
				leaf_size = targetLeaf_s.genreCount();
				if(i == leaf_size-1){
					if(targetLeaf_s.genreAtIndex(i).compareTo(song.genre()) >= 0){
						targetLeaf_s.addSong(i,song);
						break;
					}
					targetLeaf_s.addSong(i+1,song);
					break;
				}
				else if(song.genre().compareTo(targetLeaf_s.genreAtIndex(i)) <= 0){
					targetLeaf_s.addSong(i,song);
					break;
				}
			}
		}
	}
	public void printprimleaf(PlaylistNodePrimaryLeaf temp, int indent){
		if(temp.getType() == PlaylistNodeType.Leaf){
			for(int j = 0; j<indent;j++){
				System.out.print("\t");
			}
			System.out.println("<data>");
			for (int i = 0; i < ((PlaylistNodePrimaryLeaf) temp).songCount() ; i++)
			{
				for(int j = 0; j<indent;j++){
					System.out.print("\t");
				}
				System.out.print("<record>");
				System.out.print(temp.songAtIndex(i).fullName());
				System.out.println("</record>");
			}
			for(int j = 0; j<indent;j++){
				System.out.print("\t");
			}
			System.out.println("</data>");
		}
	}
	public void printprimindex(PlaylistNodePrimaryIndex temp, int indent){
		PlaylistNodePrimaryIndex tempinternal;
		PlaylistNodePrimaryLeaf templeaf;
		ArrayList<PlaylistNode> children = temp.getAllChildren();
		Stack<PlaylistNode> nodes = new Stack<PlaylistNode>();
		for (int i = children.size() - 1; i >= 0 ; i--)
			nodes.add(children.get(i));

		for(int j = 0; j<indent;j++){
			System.out.print("\t");
		}
		System.out.println("<index>");
		for (int i = 0; i < temp.audioIdCount() ; i++){
			for(int j = 0; j<indent;j++){
				System.out.print("\t");
			}
			System.out.println(temp.audioIdAtIndex(i));
		}
		for(int j = 0; j<indent;j++){
			System.out.print("\t");
		}
		System.out.println("</index>");
		while(!nodes.isEmpty()){
			if(nodes.get(0).type == PlaylistNodeType.Internal){
				tempinternal = (PlaylistNodePrimaryIndex) nodes.pop();
				printprimindex(tempinternal, indent+1);
			}
			else if(nodes.get(0).type == PlaylistNodeType.Leaf){
				templeaf = (PlaylistNodePrimaryLeaf) nodes.pop();
				printprimleaf(templeaf, indent+1);
			}
		}
	}
	public void printprimleaf_s(PlaylistNodeSecondaryLeaf temp, int indent){
		if(temp.getType() == PlaylistNodeType.Leaf){
			for(int j = 0; j<indent;j++){
				System.out.print("\t");
			}
			System.out.println("<data>");
			for (int i = 0; i < ((PlaylistNodeSecondaryLeaf) temp).genreCount() ; i++)
			{
				for(int j = 0; j<indent;j++){
					System.out.print("\t");
				}
				System.out.println(temp.genreAtIndex(i));
				indent+=1;

				int size = temp.songsAtIndex(i).size();
				for(int k = 0; k< size; k++){
					for(int j = 0; j<indent;j++){
						System.out.print("\t");
					}
					System.out.print("<record>");
					System.out.print(temp.songsAtIndex(i).get(k).fullName());
					System.out.println("</record>");
				}
				indent--;
			}
			for(int j = 0; j<indent;j++){
				System.out.print("\t");
			}
			System.out.println("</data>");
		}
	}
	public void printprimindex_s(PlaylistNodeSecondaryIndex temp, int indent){
		PlaylistNodeSecondaryIndex tempinternal;
		PlaylistNodeSecondaryLeaf templeaf;
		ArrayList<PlaylistNode> children = temp.getAllChildren();
		Stack<PlaylistNode> nodes = new Stack<PlaylistNode>();
		for (int i = children.size() - 1; i >= 0 ; i--){
			nodes.add(children.get(i));
		}
		for(int j = 0; j<indent;j++){
			System.out.print("\t");
		}
		System.out.println("<index>");

		for (int i = 0; i < temp.genreCount() ; i++){
			for(int j = 0; j<indent;j++){
				System.out.print("\t");
			}
			System.out.println(temp.genreAtIndex(i));
		}
		for(int j = 0; j<indent;j++){
			System.out.print("\t");
		}
		System.out.println("</index>");
		while(!nodes.isEmpty()){
			if(nodes.get(0).type == PlaylistNodeType.Internal){
				tempinternal = (PlaylistNodeSecondaryIndex) nodes.pop();
				printprimindex_s(tempinternal, indent+1);
			}
			else if(nodes.get(0).type == PlaylistNodeType.Leaf){
				templeaf = (PlaylistNodeSecondaryLeaf) nodes.pop();
				printprimleaf_s(templeaf, indent+1);
			}
		}
	}
}

