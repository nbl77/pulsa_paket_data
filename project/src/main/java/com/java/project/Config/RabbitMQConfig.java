package com.java.project.Config;

import com.java.project.BackEnd.Controller.ReceiverController;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
@Service
public class RabbitMQConfig {
    final private String exchange_name = "project_exchange";
    final private String queue_name = "project";
    private Connection connection;
    private Channel channel;
    private ReceiverController receiverController = null;

    public RabbitMQConfig() {
        ConnectMQ();
    }

    private void ConnectMQ() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            if (channel.isOpen()) {
                channel.basicQos(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instance() {
        if (!connection.isOpen() && !channel.isOpen()) {
            ConnectMQ();
        }
    }

    public void exchangeDec(String exchange) throws IOException {
        channel.exchangeDeclare(exchange, "fanout", true);
    }

    public void queueDec(String queue) throws IOException {
        channel.queueDeclare(queue, true, false, false, null);
    }

    public void bind(String queue, String exchange, String rout_key) throws IOException {
        channel.queueBind(queue, exchange, rout_key);
    }
    public String[] publish(String message, String routingKey) {
        try {
            instance();
            exchangeDec(exchange_name);
            queueDec(queue_name);
            bind(queue_name, exchange_name, routingKey);
            System.out.println("[=]Send data to server.");
            channel.basicPublish(exchange_name, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            channel.close();
            connection.close();
            System.out.println("[=]Successfully sent data");
            System.out.println("[=]Waiting for feedback from the server.");
            String teks = getFeedback(routingKey);
            System.out.println("[=]Receive feedback");
            String[] str = teks.split(" ,");
            System.out.println("[=]Feedback from server : " + str[1]);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{"400","Something Error!"};
    }
    public void consume() throws IOException {
        instance();
        exchangeDec(exchange_name);
        queueDec(queue_name);
        DeliverCallback callback = (consumeTag, delivery)->{
            String message = new String(delivery.getBody(), "UTF-8");
            String RoutingKey = delivery.getEnvelope().getRoutingKey();
            if (receiverController == null) {
                receiverController = new ReceiverController(message, RoutingKey);
            }
            receiverController.startReceiver(message, RoutingKey);
        };
        channel.basicConsume(queue_name,true,callback,cancelBack->{});
    }

    public void publishFeedback(String message, String routingKey) {
        try {
            instance();
            queueDec(routingKey);
            channel.basicPublish("", routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFeedback(String queuName) {
        try {
            instance();
            queueDec(queuName);
            GetResponse response = channel.basicGet(queuName, false);
            while (response == null) {
                response = channel.basicGet(queuName, false);
            }
            channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
            String message = new String(response.getBody(), "UTF-8");
            channel.queueDelete(queuName);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Something wrong";
    }
}
