import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Client implements Runnable {
	private final BlockingQueue requestQueue;
	private final BlockingQueue tokenQueue;
	private final BlockingQueue inputQueue;
	private int id;
	private int numOfIter;
	
	public Client(BlockingQueue r, BlockingQueue t, BlockingQueue i, int id, int n) {
		requestQueue = r;
		tokenQueue = t;
		inputQueue = i;
		id = id;
		numOfIter = n;
	}
	
	public void run() {
		
		while (numOfIter > 0) {
			try {
				Thread.sleep((int)Math.random() * 1000);
				requestQueue.put(new Message("request", id));
				Message token = (Message)inputQueue.take();
				Thread.sleep((int)Math.random() * 1000);
				tokenQueue.put(token);
				numOfIter--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			requestQueue.put(new Message("terminate", id));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
