package com.bharath.jms.hm.eligibilitycheck.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hm.model.Patient;

public class EligibilityCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			InitialContext initialContext = new InitialContext();
			Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
			MapMessage replyMessage = jmsContext.createMapMessage();
			Patient patient = (Patient) objectMessage.getObject();
			
			String insuranceProvider = patient.getInsuranceProvider();
			System.out.println("Insurance Provider: "+insuranceProvider);
			
			if(insuranceProvider.equals("Blue Cross Blue Shield") ||insuranceProvider.equals("United Health")) {
				System.out.println("Patients Copay is:"+patient.getCopay());
				System.out.println("Amount to be paid:"+patient.getAmountToBePayed());
				if(patient.getCopay()<40 && patient.getAmountToBePayed()<1000) {
					replyMessage.setBoolean("eligible", true);
				}
			}else {
				replyMessage.setBoolean("eligible", false);
			}
			
			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, replyMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
