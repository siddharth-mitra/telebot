import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MapMessage;

public class SentimentMessageListener implements MessageListener {
    private String consumerName;
    private MapMessage receivedMessage;

    public SentimentMessageListener(String consumerName) {
        this.consumerName = consumerName;
    }

    public MapMessage GetMessage() {
        return this.receivedMessage;
    }

    public void onMessage(Message message) {
        MapMessage result = (MapMessage)message;
        try {
            System.out.println(consumerName + " received message in listener class:" + result.getString("sentiment"));
            this.receivedMessage = result;
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
