package blockchain;

public class Client {

   private String sendMessage;

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Client() {
    }

    public Client(String sendMessage) {
        this.sendMessage = sendMessage;
    }
}
