import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.PrimMST;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;

public class jewelThief {
	
	//default constructor
	EdgeWeightedGraph ewg;
	ST<String,Double> byVal;
	ST<String,Integer> byWeight;
	ST<String,Integer> vertex;
	ST<Integer,String> reverse;
	double mostEfficient;

	int vCounter;
	public jewelThief(int V)
	{
		ewg = new EdgeWeightedGraph(V);
		vCounter = 0;
	}
	public jewelThief(String filename)
	{
		vCounter=0;
		In in = new In(filename);
		String[] v = in.readAllLines();
		ewg = new EdgeWeightedGraph(v.length+2);
		in = new In(filename);
		byVal = new ST<String,Double>();
		byWeight = new ST<String,Integer>();
		vertex = new ST<String,Integer>();
		reverse = new ST<Integer,String>();
		while(in.hasNextLine())
		{
			vCounter++;
			String line = in.readLine();
			String[] ln = line.split(",");
			byVal.put(ln[0], Double.parseDouble(ln[2]));
			byWeight.put(ln[0],Integer.parseInt(ln[1]));
			vertex.put(ln[0], vCounter);
			reverse.put(vCounter, ln[0]);
		}
		mostEfficient = byVal.get(reverse.get(vCounter))/byWeight.get(reverse.get(vCounter));
		int haveTohaveIt = 1;
		for(int i = 1;i<=vertex.size();i++)
		{
			double challenge = byVal.get(reverse.get(i))/byWeight.get(reverse.get(i));
			if(challenge<mostEfficient)
			{
				mostEfficient = challenge;
				haveTohaveIt = i;
			}
			double key = byWeight.get(reverse.get(i));
			Edge e;
			for(int j = i;j>0;j--)
			{
				e = new Edge(j-1,i,key);
				ewg.addEdge(e);
			}
			e = new Edge(0,i,key);
			ewg.addEdge(e);
		}
		Edge e = new Edge(haveTohaveIt,ewg.V()-1,0);
		ewg.addEdge(e);
	}
	public void addTreasure(String item, int weight, double value)
	{
		vCounter++;
		if(vCounter == 1)
			mostEfficient = byVal.get(reverse.get(vCounter))/byWeight.get(reverse.get(vCounter));
		byVal.put(item, value);
		byWeight.put(item, weight);
		vertex.put(item, vCounter);
		int haveTohaveIt=1;
		for(int i = vCounter;i<=vertex.size();i++)
		{
			double challenge = byVal.get(reverse.get(i))/byWeight.get(reverse.get(i));
			if(challenge<mostEfficient)
			{
				mostEfficient = challenge;
				haveTohaveIt = i;
			}
			double key = byWeight.get(reverse.get(i));
			Edge e;
			for(int j = i;j>0;j--)
			{
				e = new Edge(j-1,i,key);
				ewg.addEdge(e);
			}
			e = new Edge(0,i,key);
			ewg.addEdge(e);
		}
		Edge e = new Edge(haveTohaveIt,ewg.V()-1,0);
		ewg.addEdge(e);
	}
	
	public void optimize(double w)
	{
		KruskalMST search = new KruskalMST(ewg);
		for(Edge e:search.edges())
		{
			String item = reverse.get(e.other(e.either()));
		}
	}
	public static void main(String[]args)
	{
		jewelThief theif = new jewelThief("src/art.txt");
		double knapsack = 20;
		theif.optimize(knapsack);
//		System.out.println("How many items do you have? :");
//		int V = Integer.parseInt(StdIn.readLine())+2;
//		jewelThief manual = new jewelThief(V);
//		for(int i = 0; i<V-2;i++)
//		{
//			System.out.println("Enter items name: ");
//			String name = StdIn.readLine();
//			System.out.println("Enter items weight: ");
//			int weight = Integer.parseInt(StdIn.readLine());
//			System.out.println("Enter items value: ");
//			double val = Double.parseDouble(StdIn.readLine());
//			manual.addTreasure(name, weight, val);
//		}
		
		
	}
}
