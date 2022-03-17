package com.example.elblog.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 朱策
 */
@Configuration
public class MqConfig {
    public static final String ES_EXCHANGE = "es_exchagne";
    public static final String ES_SAVE_QUEUE = "es_save_queue";
    public static final String ES_SAVE_ROUTINGKEY = "es_save_routingKey";
    public static final String ES_DELETE_QUEUE = "es_delete_queue";
    public static final String ES_DELETE_ROUTINGKEY = "es_delete_routingKey";
    public static final String ES_DELETEALL_ROUTINKEY = "es_deleteAll_routingKey";

    @Bean
    DirectExchange esExchange() {
        return ExchangeBuilder.directExchange(MqConfig.ES_EXCHANGE).build();
    }

    @Bean
    Queue esSaveQueue() {
        return QueueBuilder.durable(MqConfig.ES_SAVE_QUEUE).build();
    }

    @Bean
    Queue esDeleteQueue() {
        return QueueBuilder.durable(MqConfig.ES_DELETE_QUEUE).build();
    }

    @Bean
    Binding esSaveQueueBindEsExchange(@Qualifier("esExchange") Exchange esExchange,
                                      @Qualifier("esSaveQueue") Queue esSaveQueue) {
        return BindingBuilder.bind(esSaveQueue).to(esExchange).with(MqConfig.ES_SAVE_ROUTINGKEY).noargs();
    }

    @Bean
    Binding esDeleteQueueBindEsExchange(@Qualifier("esExchange") Exchange esExchange,
                                        @Qualifier("esDeleteQueue") Queue esDeleteQueue) {
        return BindingBuilder.bind(esDeleteQueue).to(esExchange).with(MqConfig.ES_DELETE_ROUTINGKEY).noargs();
    }

    @Bean
    Binding deleteAll(@Qualifier("esExchange") Exchange esExchange,
                      @Qualifier("esDeleteQueue") Queue esDeleteQueue) {
        return BindingBuilder.bind(esDeleteQueue).to(esExchange).with(MqConfig.ES_DELETEALL_ROUTINKEY).noargs();
    }

}
