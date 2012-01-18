import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;

/* Client objects that share a file.*/
public class Client implements Runnable {
    
    // Server shares its requestQueue and tokenQueue with all clients
    private final BlockingQueue<Message> requestQueue;
    private final BlockingQueue<Message> tokenQueue;
    
    // Client's input queue: either empty or has a token
    private final BlockingQueue<Message> inputQueue;
    
    private int id, maxIter;
    // number of file requests a client would make
    private int numOfIter; 
    
    // Measurement variables
    private long startThinking, startHungry, startEating, totalThinking = 0, totalHungry = 0, totalEating = 0;
    private ArrayList<Long> thinking = new ArrayList<Long>();
    private ArrayList<Long> hungry = new ArrayList<Long>();
    private ArrayList<Long> eating = new ArrayList<Long>();
    
    public Client(BlockingQueue<Message> r, BlockingQueue<Message> t, BlockingQueue<Message> i, int idNum, int n) {
        requestQueue = r;
        tokenQueue = t;
        inputQueue = i;
        id = idNum;
        maxIter = n;
        numOfIter = n;
    }
    
    // Use measurements to calculate average time in thinking, hungry, and eating phases
    private void calculateData() {
    	for (int i = 0; i < maxIter; i ++) {
    		totalThinking += thinking.get(i);
    		totalHungry += hungry.get(i);
    		totalEating += eating.get(i);
    	}
    	System.out.println("Average time in thinking phase for client " + id + ": " + totalThinking / maxIter);
    	System.out.println("Average time in hungry phase for client " + id + ": " + totalHungry / maxIter);
    	System.out.println("Average time in eating phase for client " + id + ": " + totalEating / maxIter);
    }
    
    public void run() {   
        while (numOfIter > 0) {
            try {
                // Wait for a random time
            	startThinking = System.currentTimeMillis();
                Thread.sleep(195 + (int)(Math.random() * 11));
                System.out.println("Client " + id + " wants the token");
                
                // Make request to server and wait for token
                startHungry = System.currentTimeMillis();
                requestQueue.put(new Message("request", id));
                Message token = (Message)inputQueue.take();
                System.out.println("Client " + id + " now has the token");
                
                // Eat for a random time
                startEating = System.currentTimeMillis();
                Thread.sleep(15 + (int)(Math.random() * 11));
                System.out.println("Client " + id + " is done with the token");
                
                // Return the token back to server
                tokenQueue.put(token);
                
                // Update number of requests left
                numOfIter--;
                
                // Make measurements
                thinking.add(startHungry - startThinking);
                hungry.add(startEating - startHungry);
                eating.add(System.currentTimeMillis() - startEating);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Once finished all requests/operations, send terminate message to server
        try {
            System.out.println("Client " + id + " is now terminated");
            requestQueue.put(new Message("terminate", id));
            
            // Make Calculations
            calculateData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
