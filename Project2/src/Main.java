import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException {
				
		Scanner line;
		File input = new File(args[0]);
		Scanner reader = new Scanner(input);
		String type;
		String ip, sender, receiver;
		
		String root = reader.next();
		BST<String> bstTree = new BST<String>(root,args[1] + "_bst.txt");
		AVL<String> avlTree = new AVL<String>(root,args[1] + "_avl.txt");
		while(reader.hasNextLine()) {
			line = new Scanner(reader.nextLine());
			if(line.hasNext())
				type = line.next();
			//if line is empty then it skips
			else
				continue;
			if(type.equals("ADDNODE")) {
				ip = line.next();
				bstTree.insert(ip);
				avlTree.insert(ip);
			}
			else if(type.equals("DELETE")) {
				ip = line.next();
				bstTree.delete(ip);
				avlTree.delete(ip);
			}
			else if(type.equals("SEND")) {
				sender = line.next();
				receiver = line.next();
				bstTree.send(sender, receiver);
				avlTree.send(sender, receiver);
			}
			line.close();
		}
		reader.close();
		bstTree.f_writer.close();
		avlTree.f_writer.close();
	}
}