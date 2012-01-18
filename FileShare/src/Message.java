
public class Message {
    
    // three message types used in this program: "request", "terminate", "token"
    private String mesgType;
    // Each threads has it's own id.
    private int requestorID;
    
    public Message(String mesgType, int requestorID) {
        this.mesgType = mesgType;
        this.requestorID = requestorID;
    }
    
    // Getter and Setter methods
    public String getMesgType() {
        return mesgType;
    }

    public void setMesgType(String mesgType) {
        this.mesgType = mesgType;
    }

    public int getRequestorID() {
        return requestorID;
    }

    public void setRequestorID(int requestorID) {
        this.requestorID = requestorID;
    }

    @Override
    public String toString() {
        return "Message [mesgType=" + mesgType + ", requestorID=" + requestorID
                + "]";
    }

}
