import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class BST<T extends Comparable<T>> {

	// change this
	private Node<T> root;
	public FileWriter f_writer;
	
	public BST(T root) {
		this.root = new Node<T>(root);
	}
	
	public BST(T root, String path) throws IOException {
		this.root = new Node<T>(root);
		this.f_writer = new FileWriter(path);
	}

	private Node<T> getParentOfMin(Node<T> node){
		if(node.leftChild == null) {
			return null;
		}
		if(node.leftChild.leftChild != null)
			return getParentOfMin(node.leftChild);
		else
			return node;
	}
	
	private Node<T> findRecursive(T data, Node<T> node) throws IOException{
		int comparison = data.compareTo(node.data);
		if (comparison>0) {
			if(node.rightChild != null) {
				if(node.rightChild.data.equals(data)) {
					return node;
				}
				else
					return findRecursive(data, node.rightChild);
			}
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
			return null;
		}
		else {
			return null;
		}
	}
	
	private LinkedList<Node<T>> findParents(T data, Node<T> node, LinkedList<Node<T>> list){
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
	
	public void delete(T data) throws IOException {
		Node<T> node = null;
		Node<T> parentNode = null;
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
//		if(node.leftChild!=null)
//			System.out.println(node.leftChild.data + " left child ");
//		if(node.rightChild!=null)
//			System.out.println(node.rightChild.data + " right child");
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
				if(rightToDelete) {
					parentNode.rightChild = node.rightChild;
				}
				else {
					parentNode.leftChild = node.rightChild;
				}
					
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
			if(ifRootDeleted)
				// :(
				root = node.leftChild;
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
//					System.out.println("hikaye");
//					if(node.leftChild!=null)
//						System.out.println(node.leftChild.data + " left child ");
//					if(node.rightChild!=null)
//						System.out.println(node.rightChild.data + " right child");
					Node<T> parentOfMin = getParentOfMin(node.rightChild);
					T minValue = parentOfMin.leftChild.data;
					this.f_writer.write(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue+ "\n");
					//System.out.println(parentNode.getData() + ": Non Leaf Node Deleted; removed: " + node.getData()+ " replaced: " + minValue);
					if(parentOfMin.leftChild.rightChild==null) {
						parentOfMin.leftChild = null;
					}
					else {
						parentOfMin.leftChild = parentOfMin.leftChild.rightChild;
					}	
					node.data = minValue;
				}
			}	
		}
	}
	
	private Node<T> insertRecursive(T data, Node<T> node) throws IOException {
		this.f_writer.write(node.getData() + ": New node being added with IP:" + data +"\n");
		//System.out.println(node.getData() + ": New node being added with IP:" + data);
		int comparison = data.compareTo(node.data);
		if (comparison>0) {
			if(node.rightChild == null) {
				node.setRightChild(new Node<T>(data));
				return node.rightChild;
			}
			else
				return insertRecursive(data, node.rightChild);
		}
		else if(comparison<0) {
			if(node.leftChild == null) {
				node.setLeftChild(new Node<T>(data));
				return node.leftChild;
			}
			else
				return insertRecursive(data, node.leftChild);
		}
		else {
			return null;
		}
	}
	
	public void insert(T data) throws IOException{ 
		insertRecursive(data, root);
	}
	
	public void send(T sender, T receiver) throws IOException {
		LinkedList<Node<T>> parentsOfSender = findParents(sender, root, new LinkedList<Node<T>>());;
		LinkedList<Node<T>> parentsOfReceiver = findParents(receiver, root, new LinkedList<Node<T>>());;
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
	
	// TO DO change this
	private class Node<T extends Comparable<T>>{

		private T data;
		private Node<T> leftChild, rightChild;

		public Node(T data, Node<T> leftChild, Node<T> rightChild) {
			this.data = data;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
		}

		public Node(T data) {
			this.data = data;
			this.leftChild = null;
			this.rightChild = null;
		}

		public T getData() {
			return data;
		}

		public Node<T> getLeftChild() {
			return leftChild;
		}

		public Node<T> getRightChild() {
			return rightChild;
		}

		public void setLeftChild(Node<T> leftChild) {
			this.leftChild = leftChild;
		}

		public void setRightChild(Node<T> rightChild) {
			this.rightChild = rightChild;
		}
		
		
	}

}
