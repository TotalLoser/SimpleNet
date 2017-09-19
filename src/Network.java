import java.io.*;
import java.util.Scanner;

/**
 * Class to represent a network of <b> Neuron </b> objects which, together,
 * learn to solve a given problem
 * test
 * @author SelloutMillionare
 * @see Neuron
 */
public class Network
{
	/**
	 * number of neurons in a layer
	 */
	private int neurons;
	/**
	 * number of hidden layers
	 */
	private int iterations;
	/**
	 * the number of inputs this network was designed to take
	 */
	private int numInputs;
	/**
	 * the number of possible answers to the given problem
	 */
	private int numPossibles;
	/**
	 * output neurons
	 */
	private Neuron[] outputs;
	/**
	 * two dimensional array of neurons to represent the network
	 */
	private Neuron[][] neuronArray;
	/**
	 * File for testing various output
	 */
	File f = new File("Output.txt");
	/**
	 * printwriter for testing various output
	 */
	PrintWriter pw;

	/**
	 * Alternate constructor for a network loading data from save files
	 * 
	 * @param networkfilename
	 *            the name of the save file for network
	 * @param neuronfilename
	 *            the name of the save file for neurons
	 */
	public Network(String networkfilename, String neuronfilename)
	{
		System.out.println();
		try
		{
			/**
			 * network save file
			 */
			File netf = new File(networkfilename);
			/**
			 * neuron save file
			 */
			File neurf = new File(neuronfilename);
			/**
			 * Scanner for reading the network save file
			 */
			Scanner netloader = new Scanner(netf);
			/**
			 * Scanner for reading the neuron save file
			 */
			Scanner neurloader = new Scanner(neurf);
			String[] netStats = netloader.nextLine().split(";");
			neurons = Integer.parseInt(netStats[0].trim());
			iterations = Integer.parseInt(netStats[1].trim());
			numInputs = Integer.parseInt(netStats[2].trim());
			numPossibles = Integer.parseInt(netStats[3].trim());
			neuronArray = new Neuron[iterations][neurons];
			for (int i = 0; i < iterations; i++)
			{
				String[] neurStats = neurloader.nextLine().split(":");
				for (int j = 0; j < neurons; j++)
				{
					int numOutputs = Integer.parseInt(neurStats[j].split(";")[0].trim());

					String[] neurWeights = neurStats[j].split(";")[1].split(",");
					double[] weights = new double[neurWeights.length];
					for (int k = 0; k < neurWeights.length; k++)
					{
						weights[k] = Double.parseDouble(neurStats[j].split(";")[1].split(",")[k].trim());
					}
					neuronArray[i][j] = new Neuron(numOutputs, 0, weights);
				}
			}

		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * constructor for a network of neurons that can be taught
	 * 
	 * @param tneurons
	 *            number of neurons in a layer
	 * @param tnumPossibles
	 *            number of possible answers
	 * @param titerations
	 *            number of hidden layers
	 * @param tnumInputs
	 *            number of inputs
	 */
	public Network(int tneurons, int titerations, int tnumInputs, int tnumPossibles)
	{
		try
		{
			pw = new PrintWriter(f);
			pw.println("test");
		}
		catch (FileNotFoundException e)
		{// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// declare class variables
		neurons = tneurons;
		iterations = titerations;
		numInputs = tnumInputs;
		numPossibles = tnumPossibles;
		neuronArray = new Neuron[iterations][neurons];
		// populate the array with neurons with random wieght (load function
		// coming soon)
		for (int i = 0; i < neuronArray.length; i++)
		{
			for (int j = 0; j < neuronArray[i].length; j++)
			{
				if(i == neuronArray.length - 1)
					neuronArray[i][j] = new Neuron(numPossibles);
				else
					neuronArray[i][j] = new Neuron(neurons);
			}

		}
		// populate the array with output neurons with random weight
		outputs = new Neuron[numPossibles];
		for (int i = 0; i < outputs.length; i++)
			outputs[i] = new Neuron(neurons);
	}

	/**
	 * Network attempts to solve a problem and corrects nessecary wieghts if it
	 * fails
	 * 
	 * @param expression
	 *            the expression intended to be solved
	 * @return true if the answer the network was at least 95% accurate to the
	 *         actual answer
	 * @throws IllegalArgumentException
	 */
	public boolean learn(String expression) throws IllegalArgumentException
	{
		/**
		 * the components of the expression seprerated into different elements
		 */
		String[] components = expression.split(" ");
		/**
		 * array location for the components once they are converted to doubles
		 */
		double[] parsedComponents = new double[components.length];
		// parse all the elements from components and put them into
		// parsedComponents
		for (int i = 0; i < components.length; i++)
			parsedComponents[i] = dubFrom(components[i]);
		/**
		 * the correct answer to the problem
		 */
		int answer;
		// if the expression asking to be learned is outside the scope of this
		// neural net throw exception
		if(components.length != numInputs)
			throw new IllegalArgumentException(
					"experession had " + components.length + " components, expected " + numInputs);
		// evaluate the answer based on the operator
		if(components[1].equals("+"))
			answer = (int) (parsedComponents[0] + parsedComponents[2]);
		else if(components[1].equals("-"))
			answer = (int) (parsedComponents[0] - parsedComponents[2]);
		else
			throw new IllegalArgumentException(components[1]);

		fireAll(parsedComponents);
		pw.println("---Network Representation---");
		for (Neuron[] na : neuronArray)
		{
			for (Neuron n : na)
			{
				pw.print((int) n.getState() + "   ");
			}
			pw.println();
			pw.println();
			for (int i = 0; i < na.length; i++)
				pw.print("v   ");
			pw.println();
			pw.println();
		}
		pw.println("END OF PROCESS");
		pw.close();

		for (int i = 0; i < this.outputs.length; i++)
		{
			double[] tempIn = new double[neuronArray[neuronArray.length - 1].length];
			for (int j = 0; j < neuronArray[neuronArray.length - 1].length; j++)
			{
				tempIn[j] = neuronArray[neuronArray.length - 1][j].getWeightedStates()[i];
			}
			this.outputs[i].setState(tempIn);
		}
		for (int i = 0; i < outputs.length; i++)
			System.out.print((i + 1) + " : " + outputs[i].getState() + "   ");
		for (Neuron ne : this.outputs)
		{
			double testing = ne.getState();
			/*
			 * if the neuron with the answer doesn't fire close to full or the
			 * non-answer neurons dont fire close to empty return false
			 */
			if(ne.getVal() != answer && (testing < 0 || testing > (0.05 * numPossibles)))
				return false;
			else if(ne.getVal() == answer && (testing < 10 - (0.05 * numPossibles) || testing > 10))
				return false;
		}
		return true;
	}

	/**
	 * recursivley fire all neurons in network with each output feeding into the
	 * input of others
	 * 
	 * @param inputs
	 *            the inputs that casue the first neurons to fire
	 */
	public void fireAll(double[] inputs)
	{
		fireAll(inputs, iterations - 1);
	}

	/**
	 * helper methond for <b> fireAll(double[] inputs) </b>
	 * 
	 * @param inputs
	 *            the inputs that cause the first neurons to fire
	 * @param index
	 *            the iteration currently being fired
	 */
	public void fireAll(double[] inputs, int index)
	{

		double[] tempIn;
		if(index < 0)
			return;
		for (int i = 0; i < neurons; i++)
		{
			if(neuronArray[index][i].getState() == 0)
			{
				if(index == 0)
				{
					neuronArray[index][i].setState(inputs);
					// System.out.println("set neuron " + i + " to " +
					// neuronArray[index][i].getState());
					// pw.println("set neuron " + i + " to " +
					// neuronArray[index][i].getState());
				}
				else
				{
					for (Neuron n : neuronArray[index - 1])
					{
						if(n.getState() == 0)
							;
						fireAll(inputs, index - 1);
					}
					tempIn = new double[neuronArray[index - 1].length];
					for (int k = 0; k < neuronArray[index - 1].length; k++)
						tempIn[k] = neuronArray[index - 1][k].getWeightedStates()[i];
					neuronArray[index][i].setState(tempIn);
					pw.println("set neuron " + index + ", " + i + " to " + neuronArray[index][i].getState());
					// for(double d : tempIn)
					// pw.print((int)d + ", ");
					// pw.print(" from ");
					// for(Neuron n : neuronArray[index - 1])
					// pw.print(n.getState() + ", ");
					// pw.println();
				}
			}
		}
		pw.println("-----------------");
	}

	// public double[] fireAll(double[] inputs, int index)
	// {
	// //System.out.println("ran");
	// double[] output = new double[neurons];;
	// if(index == 0)
	// {
	// for(int i = 0; i < output.length ; i++)
	// {
	// output[i] = neuronArray[index][i].fire(inputs, i);
	// }
	// return output;
	// }
	// if(index < neuronArray.length)
	// {
	// for(int i = 0; i < output.length ; i++)
	// {
	// output[i] = neuronArray[index][i].fire(fireAll(inputs, index - 1), i);
	// }
	// //System.
	// return output;
	// }
	// return output;
	// }
	/**
	 * special double converter for this class
	 * 
	 * @param s
	 *            String that is to be converted
	 * @return 1 for "+", -1 for "-", and try and parse a double otherwise
	 * @throws IllegalArgumentException
	 */
	public double dubFrom(String s) throws IllegalArgumentException
	{
		if(s.equals("+"))
			return 1;
		else if(s.equals("-"))
			return -1;
		else
		{
			// try
			// {
			return Double.parseDouble(s);
			// }
			// catch(Exception e)
			// {
			// throw new IllegalArgumentException();
			// }
		}
	}

	/**
	 * A way to save the neural networks progress (not fully implemented)
	 * 
	 * @param netfilename
	 *            name of file to store network data
	 * @param neurfilename
	 *            name of file to store neuron data
	 * @throws FileNotFoundException
	 */
	public void save(String netfilename, String neurfilename) throws FileNotFoundException
	{
		PrintWriter netWriter = new PrintWriter(netfilename);
		netWriter.println(neurons + " ; " + iterations + " ; " + numInputs + " ; " + numPossibles);
		PrintWriter neurWriter = new PrintWriter(neurfilename);
		for (Neuron[] nar : neuronArray)
		{
			for (int i = 0; i < nar.length; i++)
			{
				// System.out.println(i);
				// System.out.println(nar.length);
				neurWriter.print(nar[i].getNumOutputs() + " ; ");
				for (int j = 0; j < nar[i].getWeights().length; j++)
				{
					neurWriter.print(nar[i].getWeights()[j]);
					if(j < nar[i].getWeights().length - 1)
						neurWriter.print(" , ");
				}
				if(i < nar.length - 1)
					neurWriter.print(" : ");
			}
			neurWriter.println();
		}
		neurWriter.println("outputs");
		for (int j = 0; j < outputs.length; j++)
		{
			Neuron n = outputs[j];
			neurWriter.print(n.getVal() + " ; ");
			for (int i = 0; i < n.getWeights().length; i++)
			{
				neurWriter.print(n.getWeights()[i]);
				if(i < n.getWeights().length - 1)
					neurWriter.print(" , ");
			}
			if(j < outputs.length - 1)
				neurWriter.print(" : ");
		}
		netWriter.close();
		neurWriter.close();
	}

	public double getWeightAtPosition(int x, int y, int z)
	{
		return neuronArray[x][y].getWeights()[z];
	}
}
