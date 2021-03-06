package com.example.SpringBootActiveMQ.firstTopic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.support.JmsUtils;

import javax.jms.*;

/**
 *
 * 本类演示在fourthDeviceId客户端上的sixthConsumerId和sevenConsumerId用户订阅非持久消息
 *
 */
public class fourthClientSubscriber {
    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
            "admin", "tcp://10.242.62.239:61616");
    public static void main(String[] args) {

        // Connection ：JMS 客户端到JMS Provider 的连接
        try {
            final Connection connection = connectionFactory.createConnection();
            connection.setClientID("fourthDeviceId"); //持久订阅需要设置这个。
            connection.start();
            int INDIVIDUAL_ACK_TYPE = 4;
            // Session： 一个发送或接收消息的线程
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // Destination ：消息的目的地;消息送谁那获取.
            Topic topic = session.createTopic("first_topic");// 创建topic
//            Destination topic2 = session.createTopic("first_topic");// 创建topic

            // 第6个消息者，收到非持久化消息
            MessageConsumer sixthConsumer = session.createConsumer(topic, null, true); // 普通订阅
            sixthConsumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message msg) {

                        try {

                            TextMessage message = (TextMessage) msg;
                            System.out.println("第四个客户端上的非持久订阅，sixthConsumerId 收到消息： " + message.getText());
                            JmsUtils.commitIfNecessary(session);
                          //  session.commit();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }

                    }
                });

            // 第7个消息者，非持久订阅
            MessageConsumer sevenConsumer = session.createConsumer(topic, null, true); // 普通订阅
            sevenConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("第四个客户端上的非持久订阅，sevenConsumerId 收到消息： " + message.getText());
                        JmsUtils.commitIfNecessary(session);
                        //  session.commit();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}