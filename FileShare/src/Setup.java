import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Setup {

	public static void main(String arg[]) {
		// Constants
		int numOfClient = 8;
		int numOfIter = 50;
		
		/* Instantiate the Queues
		 * clients = list of BlockingQueues where each queue represents a client's input queue
		 * requestQueue = BlockingQueue for requests and terminated messages
		 * tokenQueue = BlockingQueue for token (will be size 1)
		 */
		ArrayList<Client> clients = new ArrayList<Client>();
		ArrayList<BlockingQueue<Message>> inputQueues = new ArrayList<BlockingQueue<Message>>();
		for (int i = 0; i < numOfClient; i ++ ){
			inputQueues.add(new ArrayBlockingQueue<Message>(1));
		}
		BlockingQueue<Message> requestQueue = new ArrayBlockingQueue<Message>(numOfClient);
		BlockingQueue<Message> tokenQueue = new ArrayBlockingQueue<Message>(1);
		
		// Create token
		tokenQueue.add(new Message("token", -1));
		
		// Instantiate the server and client objects
		// Server and clients will share queues
		Server s = new Server(inputQueues, requestQueue, tokenQueue);
		for (int i = 0; i < numOfClient; i ++) {
			clients.add(new Client(requestQueue, tokenQueue, inputQueues.get(i), i, numOfIter));
		}

		// Start server and clients thread.
		new Thread(s).start();
		for (int i = 0; i < numOfClient; i ++) {
			System.out.println("Client " + i + " is starting");
			new Thread(clients.get(i)).start();
		}		
	}
}
