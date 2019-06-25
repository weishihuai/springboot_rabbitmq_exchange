package com.wsh.exchange.springboot_rabbitmq_exchange.fanoutexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsh.exchange.springboot_rabbitmq_exchange.utils.MQConnecitonUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 广播式交换机生产者
 * @Author: weixiaohuai
 * @Date: 2019/6/25
 * @Time: 20:43
 * <p>
 * 说明：
 * 1. 广播式交换机发送的消息的时候不需要指定routing key路由键;
 * 2. 所有发送到广播式交换机上面的消息都会被发送到与之绑定的所有队列上;
 * 3.
 */
public class Producer {
    private static final String EXCHANGE_NAME = "fanout_exchange";
    //广播式交换机
    private static final String EXCHANGE_TYPE = "fanout";

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
            String msg = "hello fanout exchange!!";
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
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
