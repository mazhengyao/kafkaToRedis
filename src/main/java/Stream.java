import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.internals.InternalTopologyBuilder;

import java.util.Properties;

public class Stream {
    public static void main(String[] args) {
        String input = "my-input-topic";   //���� topic
        String output = "my-output-topic";  //��� topic

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"StreamDemo");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        //ʹ��Serdes�ഴ�����л�/�����л������Serdeʵ�� Serdes��Ϊ���������ṩĬ�ϵ�ʵ�֣�String��Byte array��Long��Integer��Double��
        Serde<String> stringSerde = Serdes.String();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> simpleFirstStream = builder.stream(input, Consumed.with(stringSerde, stringSerde));
        // ʹ��KStream.mapValues �������������� abc: ��ֻ�ȡ�±�Ϊ 1 �ַ���
        KStream<String, String> upperCasedStream = simpleFirstStream.mapValues(line -> line.toUpperCase());
        // ��ת������������һ��topic
        upperCasedStream.to(output, Produced.with(stringSerde, stringSerde));

        //����������KStream
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
        kafkaStreams.start();
    }
}