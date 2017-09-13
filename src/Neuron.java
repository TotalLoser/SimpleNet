import java.io.*;
import java.util.ArrayList;
import java.util.Random;
public class Neuron
{
	PrintWriter pw;
	
	private double[] weights;
	Random rand = new Random();
	private int value;
	private int numOutputs;
	private double state;
	//private double bias;
	public Neuron(int numOutputs)
	{
		this(numOutputs, 0);
	}
	public Neuron(int tnumOutputs, int val)
	{
		this(tnumOutputs, val, null);
//		try
//		{
//			pw = new PrintWriter("Output.txt");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		//bias = tbias;
		weights = new double[numOutputs];
		for(int i = 0; i < weights.length; i++)
		{
			weights[i] = (rand.nextInt(20001) - 10000)/ 10000.0;
			//System.out.println(weights[i]);
		}
	}
	public Neuron(int tnumOutputs, int val, double[] tweights)
	{
		numOutputs = tnumOutputs;
		value = val;
		weights = tweights;
	}
//	public double fire(double[] inputs, int index)
//	{
//		try
//		{
//			
//			pw.println("----" + index + "----");
//			double sum = 0;
//			for(int i = 0; i < inputs.length; i++)
//			{
//				sum += inputs[i];
//				//pw.println(index + " : " + 10 / (1 + Math.pow(Math.E, (sum * -1 * weights[index]))));
//			}
//			pw.println(10 / (1 + Math.pow(Math.E, ))));
//			return 10 / (1 + Math.pow(Math.E, (sum * -1 * weights[index])));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return 0.0;
//	}
	public void setState(double tstate)
	{
		double[] state = {tstate};
		this.setState(state);
	}
	public void setState(double[] inputs)
	{
		double sum = 0;
		for(double d : inputs)
			sum += d;
		sum /= inputs.length;
		state = sum;
	}
	public double[] getWeightedStates()
	{
		double[] outputs = new double[weights.length];
		for(int i = 0; i < weights.length; i++)
			outputs[i] = 10 / (1 + Math.pow(Math.E, state * weights[i]));
		return outputs;
	}
	public double getState()
	{
		return state;
	}
	public int getVal()
	{
		return value;
	}
	public double[] getWeights()
	{
		return weights;
	}
	public int getNumOutputs()
	{
		return numOutputs;
	}
}
