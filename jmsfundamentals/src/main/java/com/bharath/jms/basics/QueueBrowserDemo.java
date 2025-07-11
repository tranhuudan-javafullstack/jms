package com.bharath.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {

    public static void main(String[] args) {
        InitialContext initialContext = null;
        Connection connection = null;
        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");
            MessageProducer producer = session.createProducer(queue);
            TextMessage message1 = session.createTextMessage("Message 1");
            TextMessage message2 = session.createTextMessage("Message 2");
            producer.send(message1);
            producer.send(message2);
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration messagesEnum = browser.getEnumeration();
            while (messagesEnum.hasMoreElements()) {
                TextMessage eachMessage = (TextMessage) messagesEnum.nextElement();
                System.out.println("Browsing:" + eachMessage.getText());
            }
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: " + messageReceived.getText());
            messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: " + messageReceived.getText());

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
