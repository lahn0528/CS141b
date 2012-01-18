import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;


public class Server implements Runnable {
	private final ArrayList<BlockingQueue> inputQueues;
	private final BlockingQueue requestQueue;
	private final BlockingQueue tokenQueue;
	
	public Server(ArrayList<BlockingQueue> inputQueues,
			BlockingQueue requestQueue, BlockingQueue tokenQueue) {
		this.inputQueues = inputQueues;
		this.requestQueue = requestQueue;
		this.tokenQueue = tokenQueue;
	}
	
	public void run() {
		int numOfActiveClients = inputQueues.size();
		while (numOfActiveClients > 0) {
			try {
				Message token = (Message)tokenQueue.take();
				Message requestMesg = (Message)requestQueue.take();
				if (requestMesg.getMesgType() == "request") {
					inputQueues.get(requestMesg.getRequestorID()).put(token);
				}
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
