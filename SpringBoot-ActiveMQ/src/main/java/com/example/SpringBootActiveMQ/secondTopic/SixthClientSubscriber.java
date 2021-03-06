package com.example.SpringBootActiveMQ.secondTopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 *
 * 本类演示在secondDeviceId客户端上的secondConsumerId用户订阅持久消息功能
 *
 */
public class SixthClientSubscriber {
    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
            "admin", "tcp://10.242.62.239:61616");
    public static void main(String[] args) {

        // Connection ：JMS 客户端到JMS Provider 的连接
        try {
            final Connection connection = connectionFactory.createConnection();
            connection.setClientID("sixthDeviceId"); //持久订阅需要设置这个。
            connection.start();
            // Session： 一个发送或接收消息的线程
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建topic
            Topic topic = session.createTopic("second_topic");

            // 第2个消费者，订阅持久化消息
            MessageConsumer secondConsumer = session.createDurableSubscriber(topic,"secondConsumerId");//持久化订阅
            secondConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("第六个客户端上的 secondConsumer 收到second_topic消息： " + message.getText());
                        session.commit();

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });

            // 第3个消费者，订阅持久消息
            MessageConsumer thirdConsumer = session.createDurableSubscriber(topic,"thirdConsumerId");//持久化订阅
            thirdConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("第六个客户端上的 thirdConsumer 收到second_topic消息： " + message.getText());
                        session.commit();

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });

//            connection.close(); // 如果关闭连接，每次执行一次程序就会停止，且每次执行时，只有secondConsumer消费者消费队列中的消息，thirdConsumer的队列中即使有消息，也不会进行消费
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}