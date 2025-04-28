package com.bharath.jms.guaranteedmessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageProducer {

    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()) {
            JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED);
            JMSProducer producer = jmsContext.createProducer();
            producer.send(requestQueue, "Message 1");
            producer.send(requestQueue, "Message 2");
            jmsContext.commit();
            //jmsContext.rollback();

        }

    }

}
