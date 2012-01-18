import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Setup {

	void main() {
		int numOfClient = 2;
		int numOfIter = 10;
		
		// Instantiate the Queues
		ArrayList<Client> clients = new ArrayList<Client>();
		ArrayList<BlockingQueue> inputQueues = new ArrayList<BlockingQueue>();
		for (int i = 0; i < numOfClient; i ++ ){
			inputQueues.add(new ArrayBlockingQueue<Message>(1));
		}
		BlockingQueue requestQueue = new ArrayBlockingQueue<Message>(numOfClient);
		BlockingQueue tokenQueue = new ArrayBlockingQueue<Message>(1);
		tokenQueue.add(new Message("token", -1));
		
		// Instantiate the server and client objects
		Server s = new Server(inputQueues, requestQueue, tokenQueue);
		for (int i = 0; i < numOfClient; i ++) {
			clients.add(new Client(requestQueue, tokenQueue, inputQueues.get(i), i, numOfIter));
		}
        
		// Starting server and clients thread.
		new Thread(s).start();
		for (int i = 0; i < numOfClient; i ++) {
			new Thread(clients.get(i)).start();
		}
		
	}
}
