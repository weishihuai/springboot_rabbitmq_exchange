package com.wsh.exchange.springboot_rabbitmq_exchange.topicexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsh.exchange.springboot_rabbitmq_exchange.utils.MQConnecitonUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: Topic Exchange通配符交换机
 * @Author: weixiaohuai
 * @Date: 2019/6/25
 * @Time: 21:22
 * <p>
 * 说明：
 * 1. 任何发送到Topic Exchange的消息都会被转发到所有满足Route Key与Binding Key模糊匹配的队列Queue上；
 * 2. 生产者发送消息的时候需要指定Route Key，同时绑定Exchange与Queue的时候也需要指定Binding Key；
 * 3. #” 表示0个或多个关键字，“*”表示匹配一个关键字；
 * 4. 如果Exchange没有发现能够与RouteKey模糊匹配的队列Queue，则会抛弃此消息；
 */
public class CustomProducer {
    private static final String EXCHANGE_NAME = "topic_exchange";
    //交换机类型：direct
    private static final String EXCHANGE_TYPE = "topic";
    //路由键
    private static final String EXCHANGE_ROUTE_KEY = "user.add.submit";

    public static void main(String[] args) {
        //获取MQ连接
        Connection connection = MQConnecitonUtils.getConnection();
        //从连接中获取Channel通道对象
        Channel channel = null;
        try {
            channel = connection.createChannel();
            //创建交换机对象
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            //发送消息到交换机exchange上
            String msg = "hello topic exchange!!!";
            //指定routing key为info
            channel.basicPublish(EXCHANGE_NAME, EXCHANGE_ROUTE_KEY, null, msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != channel) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
