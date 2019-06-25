package com.wsh.exchange.springboot_rabbitmq_exchange.topicexchange;

import com.rabbitmq.client.*;
import com.wsh.exchange.springboot_rabbitmq_exchange.utils.MQConnecitonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Consumer01 {
    private static Logger logger = LoggerFactory.getLogger(Consumer01.class);
    private static final String QUEUE_NAME = "direct_exchange_queue01";
    private static final String EXCHANGE_NAME = "topic_exchange";
    //binding key
    private static final String EXCHANGE_ROUTE_KEY = "user.#";

    public static void main(String[] args) {
        //获取MQ连接对象
        Connection connection = MQConnecitonUtils.getConnection();
        try {
            //创建消息通道对象
            final Channel channel = connection.createChannel();
            //创建队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //将队列绑定到交换机上,并且指定routing_key
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, EXCHANGE_ROUTE_KEY);

            channel.basicQos(1);

            //创建消费者对象
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //消息消费者获取消息
                    String message = new String(body, StandardCharsets.UTF_8);
                    logger.info("【Consumer01】receive message: " + message);
                }
            };
            //监听消息队列
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
