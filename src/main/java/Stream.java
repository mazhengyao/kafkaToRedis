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
        String input = "my-input-topic";   //输入 topic
        String output = "my-output-topic";  //输出 topic

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"StreamDemo");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        // SerDes表示串行器和解串器。每个Kafka流都必须为记录的数据类型提供SerDes，并在必要时记录值以实现数据。
        // 使用Serdes类创建序列化/反序列化所需的Serde实例 Serdes类为以下类型提供默认的实现：String、Byte array、Long、Integer和Double。
        Serde<String> stringSerde = Serdes.String();

        StreamsBuilder builder = new StreamsBuilder();
        // stream内置consumed
        KStream<String, String> simpleFirstStream = builder.stream(input, Consumed.with(stringSerde, stringSerde));
        // 使用KStream.mapValues 大写
        KStream<String, String> upperCasedStream = simpleFirstStream.mapValues(line -> line.toUpperCase());
        // 把转换结果输出到另一个topic
        // stream内置produced
        upperCasedStream.to(output, Produced.with(stringSerde, stringSerde));

        // 创建和启动KStream
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
        kafkaStreams.start();
    }
}