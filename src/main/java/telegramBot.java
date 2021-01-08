import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.jms.*;
import java.io.Serializable;

public class telegramBot extends TelegramLongPollingBot {

    ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + "127.0.0.1" + ":" + "61616", "artemis", "simetraehcapa");
    String keyword;
    Boolean twitter=false;
    //Boolean reddit=false;
    Boolean general=false;
    Boolean reddit_user=false;
    Boolean reddit_sub=false;
    Boolean general_reddit_user=false;
    Boolean general_reddit_sub=false;
    String reddit_username;
    String reddit_subreddit;
    public String getBotUsername() {
        return "Mitra_eap_bot";
    }

    public String getBotToken() {
        return "1412784469:AAHEgOPKFWzdN0g7K2lx3hpl9xYLKtvDFzc";
    }

    public void onUpdateReceived(Update update) {
        String input = update.getMessage().getText();
        if(input.equals("hello")) {
            SendMessage choiceofPlatform = new SendMessage();
            choiceofPlatform.setText("Choose the platform to extract data from :\n 1. Twitter \n 2. Reddit \n 3. General");
            choiceofPlatform.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(choiceofPlatform);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if(reddit_user){

                reddit_username = input;
                reddit_user=false;
            if(!general) {

                try (Connection connection = factory.createConnection()) {
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Destination destination = session.createQueue("eap.input");
                    MessageProducer producer = session.createProducer(destination);
                    MapMessage message = session.createMapMessage();
                    message.setString("latest_posts_user", reddit_username);
                    message.setStringProperty("type","reddit");
                    producer.send(message);
                    System.out.println("reached");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{

                general_reddit_user=true;
                twitter=true;
                SendMessage generalgetTwitterdata = new SendMessage();
                generalgetTwitterdata.setText("Enter the twitter handle:");
                generalgetTwitterdata.setChatId(String.valueOf(update.getMessage().getChatId()));
                try {
                    execute(generalgetTwitterdata);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        else if(reddit_sub){

                reddit_subreddit = input;
            reddit_sub=false;
            if(!general) {
                try (Connection connection = factory.createConnection()) {
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Destination destination = session.createQueue("eap.input");
                    MessageProducer producer = session.createProducer(destination);
                    MapMessage message = session.createMapMessage();
                    message.setString("latest_posts_subreddit", reddit_subreddit);
                    message.setStringProperty("type","reddit");
                    producer.send(message);
                    System.out.println("reached");
                    reddit_sub = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                reddit_subreddit = input;
                general_reddit_sub=true;
                twitter=true;
                SendMessage generalgetTwitterdata = new SendMessage();
                generalgetTwitterdata.setText("Enter the twitter handle:");
                generalgetTwitterdata.setChatId(String.valueOf(update.getMessage().getChatId()));
                try {
                    execute(generalgetTwitterdata);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
        }
        else if(twitter){
            if(!general) {
                try (Connection connection = factory.createConnection()) {
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Destination destination = session.createQueue("eap.input");
                    MessageProducer producer = session.createProducer(destination);
                    MapMessage message = session.createMapMessage();
                    message.setString("latest_posts_user", input);
                   message.setStringProperty("type","twitter");
                    producer.send(message);
                    System.out.println("reached");
                    twitter = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Sent normal twitter");
            }
            else{
                    try (Connection connection = factory.createConnection()) {
                            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                            Destination destination = session.createQueue("eap.input");
                            MessageProducer producer = session.createProducer(destination);
                            MapMessage general_message = session.createMapMessage();
                            general_message.setString("latest_posts_user", input);
                            general_message.setStringProperty("type","generic");
                            twitter = false;
                            if(general_reddit_sub){
                                general_reddit_sub=false;
                                    general_message.setString("latest_posts_subreddit", reddit_subreddit);

                            }
                            if(general_reddit_user){
                                general_reddit_user=false;
                                general_message.setString("latest_posts_user", reddit_username);
                                }
                            general=false;
                            producer.send(general_message);
                            System.out.println("sent general message");

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        //User has selected twitter as desired platform
        else if(input.equals("1")) {
            twitter=true;
            SendMessage twitterMessage = new SendMessage();
            twitterMessage.setText("Enter the twitter handle:");
            twitterMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(twitterMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }
        else if(input.equals("2")) {

            SendMessage redditMessage = new SendMessage();
            redditMessage.setText("Select desired option:\n a. Username  b. Subreddit?");
            redditMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(redditMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
        else if(input.equals("3")) {
            general=true;
            SendMessage generalMessage = new SendMessage();
            generalMessage.setText("Select desired option for Reddit:\n a. Username  b. Subreddit?");
            generalMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(generalMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
        else if(input.equals("a"))
        {

                reddit_user = true;
                SendMessage get_username = new SendMessage();
                get_username.setText("Please enter the reddit username");
                get_username.setChatId(String.valueOf(update.getMessage().getChatId()));
                try {
                    execute(get_username);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }



            }


        else if(input.equals("b"))
        {
            reddit_sub = true;
            SendMessage get_subreddit = new SendMessage();
            get_subreddit.setText("Please enter the reddit username");
            get_subreddit.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(get_subreddit);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }
        else{
//
    }

}
}
