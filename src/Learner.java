/**
 * Right now this whle class is just for testing <b> Network.class </b> but it will eventually run
 * a loop to train it.
 * @author SelloutMillionare
 * @see Network
 * @see Neuron
 */
public class Learner 
{
	public static void main(String[] args) 
	{
		
//		Neuron test = new Neuron(6);
//		double[] wow = {4.0, 1.0, 4.0};
//		test.setState(wow);
//		System.out.println(test.getState());
//		System.out.println(test.getWeightedStates()[1]);
//		System.out.println(test.getWeightedStates()[0]);
//		System.out.println(test.getWeightedStates()[1]);
		Network simple = new Network(10, 10, 3, 10);
		System.out.println(simple.learn("5 + 5"));
		try
		{
			simple.save(args[0], args[1]);
			Network simpleCopy = new Network(args[0], args[1]);
			System.out.println(simple.getWeightAtPosition(4, 3, 3));
			System.out.println(simpleCopy.getWeightAtPosition(4, 3, 3));
			System.out.println(simple.getWeightAtPosition(4, 4, 3));
			System.out.println(simpleCopy.getWeightAtPosition(4, 4, 3));
			System.out.println(simple.getWeightAtPosition(4, 3, 5));
			System.out.println(simpleCopy.getWeightAtPosition(4, 3, 5));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
