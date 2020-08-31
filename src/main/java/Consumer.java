import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import redis.clients.jedis.Jedis;

// 消费者
public class Consumer {
    public static void main(String[] args) {
        // 1.订阅消息可以订阅多个主题
        // 2.ConsumerConfig.GROUP_ID_CONFIG表示消费者的分组，kafka根据分组名称判断是不是同一组消费者，同一组消费者去消费一个主题的数据的时候，数据将在这一组消费者上面轮询。
        // 3.主题涉及到分区的概念，同一组消费者的个数不能大于分区数。因为：一个分区只能被同一群组的一个消费者消费。出现分区小于消费者个数的时候，可以动态增加分区。
        // 4.注意和生产者的对比，Properties中的key和value是反序列化，而生产者是序列化。

        // 定义一些属性
        Properties p = new Properties();
        // 引导服务器配置
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 反序列化程序类配置
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "topicIdea");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(p);
        // 订阅消息
        kafkaConsumer.subscribe(Collections.singletonList(Producer.topic));
        // redis连接
        Jedis jedis = new Jedis("localhost");

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                // 存储数据到hash
                jedis.hset("helloHash", String.format("%s%d", record.topic(), record.offset()),record.value());
                System.out.println("从redis中取出:"+jedis.hget("helloHash", String.format("%s%d", record.topic(), record.offset())));
            }
        }
    }
}