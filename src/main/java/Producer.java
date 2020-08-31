import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import redis.clients.jedis.Jedis;

// ������
public class Producer {
    //��������
    public static String topic = "topicIdea";
    // 1.kafka����Ǽ�Ⱥ�������ַ�ö��ŷָ�(,)
    // 2.Properties��put��������һ�������������ַ�������:p.put("bootstrap.servers","192.168.23.76:9092")
    // 3.kafkaProducer.send(record)����ͨ�����ص�Future���ж��Ƿ��Ѿ����͵�kafka����ǿ��Ϣ�Ŀɿ��ԡ�ͬʱҲ����ʹ��send�ĵڶ����������ص���ͨ���ص��ж��Ƿ��ͳɹ���
    // 4.p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);�������л��࣬����д���ȫ·��
    public static void main(String[] args) throws InterruptedException {
        Properties p = new Properties();
        //kafka��ַ�������ַ�ö��ŷָ�
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // ���л�����������
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer(p);

        try {
            while (true) {
                String msg = "Hello," + new Random().nextInt(100);
                // ������¼
                ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
                kafkaProducer.send(record);
                System.out.println("��Ϣ���ͳɹ�:" + msg);
                Thread.sleep(500);
            }
        } finally {
            kafkaProducer.close();
        }

    }
}