import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.jms.*;

public class telegramBot extends TelegramLongPollingBot {

    ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + "127.0.0.1" + ":" + "61616", "artemis", "simetraehcapa");
    String keyword;
    public String getBotUsername() {
        return "Mitra_eap_bot";
    }

    public String getBotToken() {
        return "1412784469:AAHEgOPKFWzdN0g7K2lx3hpl9xYLKtvDFzc";
    }

    public void onUpdateReceived(Update update) {
       // System.out.println(update.getMessage().getText());
        String input = update.getMessage().getText();
        // System.out.println(update.getMessage().getFrom().getFirstName());
        if(input.equals("hello")) {
            SendMessage startMessage = new SendMessage();
            startMessage.setText("Hi!\nEnter the word on which you would like to perform sentiment analysis on");
            startMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(startMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
        }
        }
        else if(input.equals("1")){
            try (Connection connection = factory.createConnection()) {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Queue twitterqueue = context.createQueue("eap.twitter_input.1");
                JMSProducer producer = context.createProducer();
               // MessageProducer producer = context.createProducer(twitterqueue);
                //ObjectMessage message = session.createObjectMessage();
                String textMessage = keyword;
                TextMessage message = context.createTextMessage(textMessage);
                producer.send(twitterqueue,message);
                System.out.println("reached");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(input.equals("2")){
            try (Connection connection = factory.createConnection())) {
                Queue redditqueue = context.createQueue("eap.reddit_input");
                JMSProducer producer = context.createProducer();
                // MessageProducer producer = context.createProducer(redditqueue);
                //ObjectMessage message = session.createObjectMessage();
                String textMessage = keyword;
                TextMessage message = context.createTextMessage(textMessage);
                producer.send(redditqueue,message);
                System.out.println("reached");

            } catch (JMSRuntimeException | JMSException | Exception e) {
                e.printStackTrace();
            }
        }
        else{
            keyword = input;
            SendMessage choiceofPlatform = new SendMessage();

            choiceofPlatform.setText("Choose the platform to extract data from :\n 1. Twitter \n 2. Reddit \n 3. General");
            choiceofPlatform.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(choiceofPlatform);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
        // message.setChatId(String.valueOf(update.getMessage().getChatId()));
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }
}
