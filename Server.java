import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;


public class Server implements Runnable {
	private final ArrayList<BlockingQueue<Message>> inputQueues;
	private final BlockingQueue<Message> requestQueue;
	private final BlockingQueue<Message> tokenQueue;
	
	// Constructor that takes in the shared queues
	public Server(ArrayList<BlockingQueue<Message>> inputQueues,
			BlockingQueue<Message> requestQueue, BlockingQueue<Message> tokenQueue) {
		this.inputQueues = inputQueues;
		this.requestQueue = requestQueue;
		this.tokenQueue = tokenQueue;
	}
	
	public void run() {
		int numOfActiveClients = inputQueues.size();
		// Keeps running until all clients are terminated
		while (numOfActiveClients > 0) {
			try {
				// Waits for message in token queue
				Message token = (Message)tokenQueue.take();
				
				// Transitions to state "has token" and waits for message in request queue
				Message requestMesg = (Message)requestQueue.take();
				
				// If message is request, send token to requester and go to state
				// "doesn't have token"
				if (requestMesg.getMesgType() == "request") {
					inputQueues.get(requestMesg.getRequestorID()).put(token);
				}
				// If message is terminated, decrement numOfActiveClients by one
				// and go to state "has token"
				else if (requestMesg.getMesgType() == "terminate") {
					numOfActiveClients--;
					tokenQueue.put(token);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
