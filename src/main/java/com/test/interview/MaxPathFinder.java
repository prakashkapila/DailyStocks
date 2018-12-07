package com.test.interview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MaxPathFinder {

	TreeMap<Long,String> map = new TreeMap<Long,String>();
	NavigableMap map1;
	private void printAll() {
		for(Map.Entry<Long,String> highest :map.entrySet()) {
		System.out.print("The Entry Value is"+highest.getKey());
 		System.out.println("\tThe Corresponding Path is "+highest.getValue());
		}
	}
	public void findMaxPath() {
		// First build the Graph
		GraphBuilder builder = new GraphBuilder();
		builder.buildGraph();
		Node graph = builder.graph;
	// Aggregate the values of each graph root node through leaf node in left first.	
		doPreOrder(graph);
	// Collect the values from map	
		Map.Entry<Long,String> highest = map.lastEntry();
		System.out.println("The Highest Value is"+highest.getKey());
 		System.out.println("The Corresponding Path is "+highest.getValue());
 		// do a test print for all the values to authenticate the result.
 		printAll();
	}
	private void doPreOrder(Node graph) {
		 traverse(graph,"",0);
	}
	
	private void traverse(Node graph,String path,long sum) {
		 if (graph == null) {
			 map.put(sum, path);
			 return;
		 }
	        /* first print data of node */
	        path += graph.data + " ";
	        sum += graph.weight;
	        /* then recur on left sutree */
	        traverse(graph.leftNode,path,sum);
	 
	        /* now recur on right subtree */
	        traverse(graph.rightNode,path,sum);
	}
	public static void main(String arg[])
	{
		MaxPathFinder path = new MaxPathFinder();
		path.findMaxPath();
		
	}
}
/**The generic representation of Binary tree object with  node and left sub tree and right subtree**/
class Node{
	String data;
	int weight;
	int level;
 	Node leftNode;
	Node rightNode;
	
	// Get the parents for the level for level 1 its the root itself.
	private Node[] getParents(List<Node> parents,int level,Node curentNode)
	{
		if( level < 1)
			throw new RuntimeException(" this method can only be called for getting parents, Root node will not have parent");
		if(level == 1)
			return new Node[] {curentNode};
		
 		List<Node> parent = new ArrayList<Node>();
 		if(parents==null)
	 	{
	 		parent.add(curentNode.leftNode);
	 		parent.add(curentNode.rightNode);
	 	}
 		else {
 		for(Node node:parents) {
			if(level -1  > node.level)
			{
				parent.add(node.leftNode);
				parent.add(node.rightNode);
			}
 		}
 		}
	 	if(level-1 == curentNode.level)
		{
			return parents.toArray(new Node[parent.size()]);
		}
	 	
	 	return getParents(parent,level,parent.get(0));
	}
	
	// fill in the details of the node like its weight and level.
	private void addNode(Node node,String data,int weight,int level)
	{
		 	node.data = data;
			node.weight=Integer.parseInt(data.trim());
			node.level = level;
	}

	/**This method will add the node that will add Node at the appropiate level travesring to the parent for that level.
	 * Note that a child is referenced as left child and right for different parents.And parents may appear in duplicates.This method takes into consideration that issue
	 * and avoids duplicate processing of the parent.**/
	public void addNode(String data,int wieght,int level)
	{
		if(level ==0)
			addNode(this,data,wieght,level);
		else
		{
		 	String[] nodevals = data.split(" ");
		 	Node[] parents = getParents(null,level,this); 
		 	int toggle=0;// incremented only in case of child being added to avoid duplicate processing and Array out of bounds.
		 	Node rchild=null;
		 	Node lchild = null;
		 	for(int i=0;i<parents.length;i++)
		 	{
 	 			Node node = parents[i];
 	 	  		if(node.leftNode == null) { // avoid duplicate processing of the parent
		  			lchild = lchild==null? new Node():lchild;
		 			node.leftNode=lchild;
		 			addNode(lchild,nodevals[toggle++],wieght,level);
		 		}
		 		if(node.rightNode == null) {// avoid duplicate processing of the parent
		 			rchild = new Node();
			 		addNode(rchild,nodevals[toggle],wieght,level);
		 			node.rightNode=rchild;
		 		}
		 		lchild = rchild; // right child becomes the left child of another parent.
		 	}
		}
	}
}

class GraphBuilder{
	Node graph = new Node();
	String file = "c:/test/interview/triangle_test_100rows.txt";
	List<String> lines = new ArrayList<String>();
	public void readFile(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
 			String line = "";//c:\test => c:\\test => "c:/test"
			while(reader.ready()) {
 				line = reader.readLine();
 				if(line.equals(""))
 					continue;
 				line = line.replaceAll("\n", "");
				lines.add(line);
			}
			reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	public void buildGraph() {
		readFile();
		int size =5;// lines.size();//=4; // use this number to test. for full blown run the memory is n0t enough use  30g, with 3g I am able to process till 29 levels.
		for(int i=0;i<size;i++)
		{
			System.out.println("building level"+i);
			graph.addNode(lines.get(i), i, i);
		}
	}
}