package com.bharath.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hm.eligibilitycheck.listeners.EligibilityCheckListener;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, InterruptedException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext("eligibilityuser", "eligibilitypass")) {
			JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
			JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);
			//consumer.setMessageListener(new EligibilityCheckListener());
			
			for(int i=1;i<=10;i+=2) {
				System.out.println("Consumer1: "+consumer1.receive());
				System.out.println("Consumer2: "+consumer2.receive());
			}
			
			//Thread.sleep(10000);

		};

	}

}
