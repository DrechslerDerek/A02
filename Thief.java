import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;

public class Thief {

	MaxPQ<Treasure> weightPQ;
	MaxPQ<Treasure> valuePQ;
	MaxPQ<Treasure> valuePerLb;
	double backpack;
	Stack<String> stackWeights = new Stack<String>();
	Stack<String> stackValues = new Stack<String>();
	Stack<String> stackRatios = new Stack<String>();
	double weightTotal;
	double weightTotalval;
	double weightRatio;

	private class Treasure implements Comparable<Treasure>{
		String item;
		double weight;
		double value;
		final Comparator<Treasure> BY_VALUE = new ValueComp();
		final Comparator<Treasure> BY_RATIO = new RatioComp();
		public Treasure()
		{
			
		}
		public Treasure(String item, double weight, double value)
		{
			this.item = item;
			this.weight = weight;
			this.value = value;
		}
		@Override
		public int compareTo(Treasure t) {
			if(this.weight>t.weight) return 1;
			if(this.weight<t.weight) return -1;
			else return 0;
		}
		@Override
		public String toString()
		{
			return this.item;
		}
		public class ValueComp implements Comparator<Treasure>
		{
			@Override
			public int compare(Treasure t1, Treasure t2) {
				Double value1 = t1.value;
				Double value2 = t2.value;
				return value1.compareTo(value2);
			}
		}
		public class RatioComp implements Comparator<Treasure>
		{
			@Override
			public int compare(Treasure t1, Treasure t2) {
				Double value1 = t1.value/t1.weight;
				Double value2 = t2.value/t2.weight;
				return value1.compareTo(value2);
			}
		}
	}
	
	public Thief(String filename, double backpack)
	{
		weightTotal = 0;
		weightTotalval = 0;
		int index = 0;
		In in = new In(filename);
		String[] size = in.readAllLines();
		in = new In(filename);
		Treasure item = new Treasure();
		weightPQ = new MaxPQ<Treasure>(size.length);
		valuePQ = new MaxPQ<Treasure>(size.length,item.BY_VALUE);
		valuePerLb = new MaxPQ<Treasure>(size.length,item.BY_RATIO);
		while(in.hasNextLine())
		{
			String[] line = in.readLine().split(",");
			item = new Treasure(line[0],Double.parseDouble(line[1]),Double.parseDouble(line[2]));
			weightPQ.insert(item);
			valuePQ.insert(item);
			valuePerLb.insert(item);
			index++;
		}
		this.backpack = backpack;
	}
	
	public void optimize()
	{
		String queue = "Items to take: ";
		double valofWeights = valueOfWeights();
		double highVals = highestValues();
		double ratios = topRatios();
		if(valofWeights>highVals&&valofWeights>ratios)
		{
			for(String s:stackWeights)
				queue += s+" ";
			System.out.println(queue+"\ngiving a total value of "+valofWeights+" \nwith a total weight of "+weightTotal);
		}
		if(highVals>=valofWeights&&highVals>=ratios)
		{
			for(String s:stackValues)
				queue += s+" ";
			System.out.println(queue+"\ngiving a total value of "+highVals+" \nwith a total weight of "+weightTotalval);
		}
		if(ratios>valofWeights&&ratios>highVals)
		{
			for(String s:stackRatios)
				queue += s+" ";
			System.out.println(queue+"\ngiving a total value of "+ratios+" \nwith a total weight of "+weightRatio);
		}
	}
	
	private double valueOfWeights()
	{
		double w = 0;
		double val = 0;
		while(w<=this.backpack)
		{
			if(!weightPQ.isEmpty())
			{
				Treasure x = weightPQ.delMax();
				if(x.weight+w>this.backpack)
				{
					double space = this.backpack-w;
					for(Treasure y:weightPQ)
					{
						if(y.weight<=space)
						{
							stackWeights.push(y.item);
							val += y.value;
							w += y.weight;
							break;
						}
					}
				}else {
					stackWeights.push(x.item);
					val += x.value;
					w += x.weight;
				}
			}else {
				break;
			}
		}
		weightTotal = w;
		return val;
	}
	 
	private double highestValues()
	{
		double w = 0;
		double val = 0;
		while(w<=this.backpack)
		{
			if(!valuePQ.isEmpty())
			{
			Treasure x = valuePQ.delMax();
				if(w+x.weight>backpack)
				{
					for(Treasure y:valuePQ)
					{
						if(y.weight<=(this.backpack-w))
						{
							stackValues.push(y.item);
							val += y.value;
							w += y.weight;
							break;
						}
					}
				}else {
					stackValues.push(x.item);
					val += x.value;
					w += x.weight;
				}
			}else {
				break;
			}
		}
		weightTotalval = w;
		return val;
	}
	
	private double topRatios()
	{
		double w = 0;
		double val = 0;
		while(w<=this.backpack)
		{
			if(!valuePerLb.isEmpty())
			{
			Treasure x = valuePerLb.delMax();
				if(w+x.weight>backpack)
				{
					for(Treasure y:valuePerLb)
					{
						if(y.weight<=(this.backpack-w))
						{
							stackRatios.push(y.item);
							val += y.value;
							w += y.weight;
							break;
						}
					}
				}else {
					stackRatios.push(x.item);
					val += x.value;
					w += x.weight;
				}
			}else {
				break;
			}
		}
		weightRatio = w;
		return val;
	}

	public static void main(String[]args)
	{
		System.out.println("What is the capacity of your backpack? ");
		double knapsack = StdIn.readDouble();
		Thief thief = new Thief("src/art.txt",knapsack);
		thief.optimize();
	}
}
