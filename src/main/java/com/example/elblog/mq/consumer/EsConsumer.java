package com.example.elblog.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.example.elblog.elasticsearch.repository.BlogRepository;
import com.example.elblog.entity.Blog;
import com.example.elblog.mq.config.MqConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 朱策
 */
@Slf4j
@Component
public class EsConsumer {
    @Autowired
    private BlogRepository blogRepository;

    @RabbitListener(queues = MqConfig.ES_SAVE_QUEUE)
    public void receiveSave(String message, Channel channel) {
        Blog blog = JSON.parseObject(message, Blog.class);
        log.info("接到消息:{}", blog);
        blogRepository.save(blog);
    }

    @RabbitListener(queues = MqConfig.ES_DELETE_QUEUE)
    public void receiveDelete(Integer id) {
        log.info("接收到被删除的id:{}", id);
        blogRepository.deleteById(id);
    }

    @RabbitListener(queues = MqConfig.ES_DELETE_QUEUE)
    public void deleteAll(Integer[] ids) {
        log.info("接收到被删除的id数组:{}", (Object) ids);
        blogRepository.deleteAllById(Arrays.asList(ids));
    }
}
