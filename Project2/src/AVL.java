import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;


public class AVL<T extends Comparable<T>> {

	private AvlNode<T> root;
	public FileWriter f_writer;
	
	public AVL(T root) {
		this.root = new AvlNode<T>(root);
	}

	public AVL(T root, String path) throws IOException {
		this.root = new AvlNode<T>(root);
		this.f_writer = new FileWriter(path);
	}

	public void insert(T data) throws IOException{ 
		root = insertRecursive(data, root);
	}

	private AvlNode<T> insertRecursive( T data, AvlNode<T> node ) throws IOException{
		f_writer.write(node.getData() + ": New node being added with IP:" + data + "\n");
		//System.out.println(node.getData() + ": New node being added with IP:" + data);
		int comparison = data.compareTo(node.data);
		if( comparison < 0 ){
			if(node.leftChild!=null) {
				node.leftChild = insertRecursive(data, node.leftChild);
			}
			else {
				node.leftChild = new AvlNode<T>(data);
			}
		}
		else if( comparison > 0 ) {
			if(node.rightChild!=null) {
				node.rightChild = insertRecursive(data, node.rightChild );
			}
			else {
				node.rightChild = new AvlNode<T>(data);
			}
		}
		return balance(node);
	}

	
	public void delete(T data) throws IOException {
		AvlNode<T> node = null;
		AvlNode<T> parentNode = null;
		boolean ifRootDeleted, rightToDelete = false;
		if(data.compareTo(root.data)==0) {
			node = root;
			ifRootDeleted = true;
		}
		else {
			ifRootDeleted = false;
			parentNode = findRecursive(data, root);
			int comparison = data.compareTo(parentNode.data);
			if (comparison>0) {
				rightToDelete = true;
				node = parentNode.rightChild;
			}
			else if(comparison<0) {
				rightToDelete = false;
				node = parentNode.leftChild;
			}
		}
		
		if((node.leftChild == null)&&(node.rightChild == null)){
			this.f_writer.write(parentNode.getData() + ": Leaf Node Deleted: " + node.getData()+ "\n");
			//System.out.println(parentNode.getData() + ": Leaf Node Deleted: " + node.getData());
			if(ifRootDeleted)
				root = null;
			else
				if(rightToDelete)
					parentNode.rightChild = null;
				else
					parentNode.leftChild = null;
		}
		else if((node.leftChild == null)&&(node.rightChild != null)) {
			this.f_writer.write(parentNode.getData() + ": Node with single child Deleted: " + node.getData()+ "\n");
			//System.out.println(parentNode.getData() + ": Node with single child Deleted: " + node.getData());
			if(ifRootDeleted) 
				root = node.rightChild;
			else
				if(rightToDelete)
					parentNode.rightChild = node.rightChild;
				else
					parentNode.leftChild = node.rightChild;
		}
		else if((node.leftChild != null)&&(node.rightChild == null)) {
			this.f_writer.write(parentNode.getData() + ": Node with single child Deleted: " + node.getData()+ "\n");
			//System.out.println(parentNode.getData() + ": Node with single child Deleted: " + node.getData());
			if(ifRootDeleted) 
				root = node.leftChild;
			else
				if(rightToDelete)
					parentNode.rightChild = node.leftChild;
				else
					parentNode.leftChild = node.leftChild;
		}
		else if((node.leftChild != null)&&(node.rightChild != null)) {
			if(ifRootDeleted) {
				// :(
//				T minValue = findMin(root.rightChild).data;
//				if(root.leftChild.rightChild==null) {
//					root.leftChild = null;
//				}
//				else {
//					root.leftChild = root.leftChild.rightChild;
//				}
//				root = balanceAfterOperation(root.data, root);
//				this.f_writer.write(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue+ "\n");
//				System.out.println(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue);
//				node.data = minValue;
//				root = node.leftChild;
			}
			else {
				if(getParentOfMin(node.rightChild)==null) {
					T minValue = node.rightChild.data;
					this.f_writer.write(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue+ "\n");
					//System.out.println(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue);
					node.rightChild.leftChild = node.leftChild;
					if(rightToDelete)
						parentNode.rightChild = node.rightChild;
					else
						parentNode.leftChild = node.rightChild;
				}
				else {
					AvlNode<T> parentOfMin = getParentOfMin(node.rightChild);
					T minValue = parentOfMin.leftChild.data;
					//System.out.println("min value "+ minValue + " parentofmin " + parentOfMin.data);
					this.f_writer.write(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue+ "\n");
					//System.out.println(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue);
					if(parentOfMin.leftChild.rightChild==null) {
						parentOfMin.leftChild = null;
					}
					else {
						parentOfMin.leftChild = parentOfMin.leftChild.rightChild;
					}	
					node.data = minValue;
					//System.out.println("min value "+ minValue + " parentofmin " + parentOfMin.data);
					root = balanceAfterOperation(parentOfMin.data, root);
				}
			}	
		}
		root = balanceAfterOperation(data, root);
	}
	
	private AvlNode<T> balanceAfterOperation( T data, AvlNode<T> node ) throws IOException
	{
		if(node==null) {
			return null;
		}
		int comparison = data.compareTo(node.data);
		if( comparison < 0 )
			node.leftChild = balanceAfterOperation( data, node.leftChild );
		else if( comparison > 0 )
			node.rightChild = balanceAfterOperation( data, node.rightChild );
//		else if( node.leftChild != null && node.rightChild != null ) // Two children
//		{
//			node.data = findMin( node.rightChild ).data;
//			node.rightChild = balanceAfterOperation( node.data, node.rightChild );
//		}
		return balance( node );
	}
	
	private AvlNode<T> findMin(AvlNode<T> node){
		if(node == null) {
			return null;
		}
		if(node.leftChild != null)
			return findMin(node.leftChild);
		else
			return node;
	}
	
	private AvlNode<T> getParentOfMin(AvlNode<T> node){
		if(node.leftChild == null) {
			return null;
		}
		if(node.leftChild.leftChild != null)
			return getParentOfMin(node.leftChild);
		else
			return node;
	}
	
	private AvlNode<T> findRecursive(T data, AvlNode<T> node) throws IOException{
		//System.out.println("eeee "+data+ "eeeeee " + node.data);
		//f_writer.write("eeee"+data+ "eeeeee" + node.data + "\n");
		int comparison = data.compareTo(node.data);
		if (comparison>0) {
			if(node.rightChild != null) {
				if(node.rightChild.data.equals(data)) {
					return node;
				}
				else
					return findRecursive(data, node.rightChild);
			}
			System.out.println(data +" not found");
			return null;
		}
		else if(comparison<0) {
			if(node.leftChild != null) {
				if(node.leftChild.data.equals(data)) {
					return node;
				}
				else
					return findRecursive(data, node.leftChild);
			}
			System.out.println(data +" not found");
			return null;
		}
		else {
			return null;
		}
	}

	private int height( AvlNode<T> node ){
		if(node==null)
			return -1;
		else
			return node.height;
	}

	private AvlNode<T> balance(AvlNode<T> node ) throws IOException{
		if(height(node.leftChild) - height(node.rightChild)>1) {
			if(height(node.leftChild.leftChild) >= height(node.leftChild.rightChild)) {
				node = rightRotation(node);
				f_writer.write("Rebalancing: right rotation\n");
				//System.out.println("Rebalancing: right rotation");
			}
			else {
				node = leftRightRotation(node);
				f_writer.write("Rebalancing: left-right rotation\n");
				//System.out.println("Rebalancing: left-right rotation");
			}
		}
		else if(height(node.rightChild) - height(node.leftChild)>1) {
			if( height(node.rightChild.rightChild) >= height(node.rightChild.leftChild)) {
				node = leftRotation(node);
				f_writer.write("Rebalancing: left rotation\n");
				//System.out.println("Rebalancing: left rotation");
			}				
			else {
				node = rightLeftRotation(node);
				f_writer.write("Rebalancing: right-left rotation\n");
				//System.out.println("Rebalancing: right-left rotation");
			}
		}
		updateHeight(node);
//		System.out.println("balanced new root "+ node.data);
//		if(node.leftChild!=null)
//			System.out.println(node.leftChild.data + " left child ");
//		if(node.rightChild!=null)
//			System.out.println(node.rightChild.data + " right child");
		return node;
	}

	private AvlNode<T> rightRotation( AvlNode<T> parent ){
		AvlNode<T> left = parent.leftChild;
		parent.leftChild = left.rightChild;
		left.rightChild = parent;
		updateHeight(parent);
		updateHeight(left);
		return left;
	}

	private AvlNode<T> leftRotation( AvlNode<T> parent ){
		AvlNode<T> right = parent.rightChild;
		parent.rightChild = right.leftChild;
		right.leftChild = parent;
		updateHeight(parent);
		updateHeight(right);
		return right;
	}

	private AvlNode<T> leftRightRotation( AvlNode<T> k3 ){
		k3.leftChild = leftRotation( k3.leftChild );
		return rightRotation( k3 );
	}

	private AvlNode<T> rightLeftRotation( AvlNode<T> k1 ){
		k1.rightChild = rightRotation( k1.rightChild );
		return leftRotation( k1 );
	}

	private void updateHeight(AvlNode<T> node){
		node.setHeight(Math.max( height(node.leftChild), height(node.rightChild)) + 1);
	}

	private LinkedList<AvlNode<T>> findParents(T data, AvlNode<T> node, LinkedList<AvlNode<T>> list){
		int comparison = data.compareTo(node.data);
		if (comparison>0) {
			if(node.rightChild != null) {
				list.add(node);
				if(node.rightChild.data.equals(data)) {
					return list;
				}
				else
					return findParents(data, node.rightChild, list);
			}
			return list;
		}
		else if(comparison<0) {
			if(node.leftChild != null) {
				list.add(node);
				if(node.leftChild.data.equals(data)) {
					return list;
				}
				else
					return findParents(data, node.leftChild,list);
			}
			return list;
		}
		else {
			return list;
		}
	}

	public void send(T sender, T receiver) throws IOException {
		LinkedList<AvlNode<T>> parentsOfSender = findParents(sender, root, new LinkedList<AvlNode<T>>());;
		LinkedList<AvlNode<T>> parentsOfReceiver = findParents(receiver, root, new LinkedList<AvlNode<T>>());;
		if((parentsOfSender.size()>1)&&(parentsOfReceiver.size()>1)) {
			while(parentsOfSender.get(1)==parentsOfReceiver.get(1)) {
				parentsOfSender.remove(0);
				parentsOfReceiver.remove(0);
				if((parentsOfSender.size()>1)&&(parentsOfReceiver.size()>1)) {
					
				}
				else
					break;
			}
		}
		if(parentsOfSender.size()>1) {
			if(parentsOfSender.get(1).data.equals(receiver)) {
				parentsOfSender.remove(0);
				parentsOfReceiver.remove(0);
			}
		}
		if(parentsOfReceiver.size()>1) {
			if(parentsOfReceiver.get(1).data.equals(sender)) {
				parentsOfSender.remove(0);
				parentsOfReceiver.remove(0);
			}
		}
		T from = sender;
		
		if(parentsOfReceiver.isEmpty()) {
			this.f_writer.write(sender + ": Sending message to: " + receiver +"\n");
			//System.out.println(sender + ": Sending message to: " + receiver);
			parentsOfSender.removeFirst();
			while(!parentsOfSender.isEmpty()) {
				this.f_writer.write(parentsOfSender.getLast().data+ ": Transmission from: "+from + " receiver: "+ receiver +" sender:"+ sender +"\n");
				//System.out.println(parentsOfSender.getLast().data+ ": Transmission from: "+from + " receiver: "+ receiver +" sender:"+ sender);
				from = parentsOfSender.removeLast().data;
			}
			this.f_writer.write(receiver + ": Received message from: " + sender+"\n");
			//System.out.println(receiver + ": Received message from: " + sender);
		}
		else {
			this.f_writer.write(sender + ": Sending message to: " + receiver +"\n");
			//System.out.println(sender + ": Sending message to: " + receiver);
			while(!parentsOfSender.isEmpty()) {
				this.f_writer.write(parentsOfSender.getLast().data+ ": Transmission from: "+from + " receiver: "+ receiver +" sender:"+ sender +"\n");
				//System.out.println(parentsOfSender.getLast().data+ ": Transmission from: "+from + " receiver: "+ receiver +" sender:"+ sender);
				from = parentsOfSender.removeLast().data;
			}
			parentsOfReceiver.removeFirst();
			while(!parentsOfReceiver.isEmpty()) {
				this.f_writer.write(parentsOfReceiver.getFirst().data+ ": Transmission from: "+from + " receiver: "+ receiver +" sender:"+ sender +"\n");
				//System.out.println(parentsOfReceiver.getFirst().data+ ": Transmission from: "+from + " receiver: "+ receiver +" sender:"+ sender);
				from = parentsOfReceiver.removeFirst().data;
			}
			this.f_writer.write(receiver + ": Received message from: " + sender+"\n");
			//System.out.println(receiver + ": Received message from: " + sender);
		}
	}
		

	private class AvlNode<T extends Comparable<T>>{

		private T data;
		private AvlNode<T> leftChild, rightChild;
		private int height;

		//		public AvlNode(T data, AvlNode<T> leftChild, AvlNode<T> rightChild) {
		//			this.data = data;
		//			this.leftChild = leftChild;
		//			this.rightChild = rightChild;
		//		}

		public AvlNode(T data) {
			this.data = data;
			this.leftChild = null;
			this.rightChild = null;
			this.setHeight(0);
		}

		public T getData() {
			return data;
		}

		public AvlNode<T> getLeftChild() {
			return leftChild;
		}

		public AvlNode<T> getRightChild() {
			return rightChild;
		}

		public void setLeftChild(AvlNode<T> leftChild) {
			this.leftChild = leftChild;
		}

		public void setRightChild(AvlNode<T> rightChild) {
			this.rightChild = rightChild;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}		
	}
}
