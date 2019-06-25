package com.wsh.exchange.springboot_rabbitmq_exchange.directexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsh.exchange.springboot_rabbitmq_exchange.utils.MQConnecitonUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: Direct Exchange直连交换机
 * @Author: weixiaohuai
 * @Date: 2019/6/25
 * @Time: 21:22
 * <p>
 * 说明：
 * 1. 任何发送到Direct Exchange的消息都会被转发到指定RouteKey中指定的队列Queue；
 * 2. 生产者生产消息的时候需要执行Routing Key路由键；
 * 3. 队列绑定交换机的时候需要指定Binding Key,只有路由键与绑定键相同的话，才能将消息发送到绑定这个队列的消费者；
 * 4. 如果vhost中不存在RouteKey中指定的队列名，则该消息会被丢弃；
 */
public class CustomProducer {
    private static final String EXCHANGE_NAME = "direct_exchange";
    //交换机类型：direct
    private static final String EXCHANGE_TYPE = "direct";
    //路由键
    private static final String EXCHANGE_ROUTE_KEY = "user.add";

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
            String msg = "hello direct exchange!!!";
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
