package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageTypesDemo {

    public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            JMSProducer producer = jmsContext.createProducer();
            TextMessage textMessage = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
            BytesMessage bytesMessage = jmsContext.createBytesMessage();
            bytesMessage.writeUTF("John");
            bytesMessage.writeLong(123l);

            StreamMessage streamMessage = jmsContext.createStreamMessage();
            streamMessage.writeBoolean(true);
            streamMessage.writeFloat(2.5f);

            MapMessage mapMessage = jmsContext.createMapMessage();
            mapMessage.setBoolean("isCreditAvailable", true);

            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            Patient patient = new Patient();
            patient.setId(123);
            patient.setName("John");
            objectMessage.setObject(patient);

            producer.send(queue, patient);

            //	BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive(5000);
            //System.out.println(messageReceived.readUTF());
            //System.out.println(messageReceived.readLong());


            //StreamMessage messageReceived = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
            //System.out.println(messageReceived.readBoolean());
            //System.out.println(messageReceived.readFloat());

            //MapMessage messageReceived = (MapMessage) jmsContext.createConsumer(queue).receive(5000);
            //System.out.println(messageReceived.getBoolean("isCreditAvailable"));

            Patient patientReceived = jmsContext.createConsumer(queue).receiveBody(Patient.class);
            //Patient object = (Patient) messageReceived.getObject();
            System.out.println(patientReceived.getId());
            System.out.println(patientReceived.getName());
        }

    }

}
