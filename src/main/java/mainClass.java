import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;

public class mainClass {
    public static void main(String[] args) {
        try {

                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(new telegramBot());




        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
