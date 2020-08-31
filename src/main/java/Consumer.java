import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import redis.clients.jedis.Jedis;

// ������
public class Consumer {
    public static void main(String[] args) {
        // 1.������Ϣ���Զ��Ķ������
        // 2.ConsumerConfig.GROUP_ID_CONFIG��ʾ�����ߵķ��飬kafka���ݷ��������ж��ǲ���ͬһ�������ߣ�ͬһ��������ȥ����һ����������ݵ�ʱ�����ݽ�����һ��������������ѯ��
        // 3.�����漰�������ĸ��ͬһ�������ߵĸ������ܴ��ڷ���������Ϊ��һ������ֻ�ܱ�ͬһȺ���һ�����������ѡ����ַ���С�������߸�����ʱ�򣬿��Զ�̬���ӷ�����
        // 4.ע��������ߵĶԱȣ�Properties�е�key��value�Ƿ����л����������������л���

        // ����һЩ����
        Properties p = new Properties();
        // ��������������
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // �����л�����������
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "topicIdea");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(p);
        // ������Ϣ
        kafkaConsumer.subscribe(Collections.singletonList(Producer.topic));
        // redis����
        Jedis jedis = new Jedis("localhost");

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                // �洢���ݵ�hash
                jedis.hset("helloHash", String.format("%s%d", record.topic(), record.offset()),record.value());
                System.out.println("��redis��ȡ��:"+jedis.hget("helloHash", String.format("%s%d", record.topic(), record.offset())));
            }
        }
    }
}