package pt.uc.dei.weatherStations.streams;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class TheoreticalClass {
    public static void main(String[] args) throws InterruptedException, IOException {         

        String topicStandard = "StandardWeather1";
        String topicAlert = "WeatherAlert1";
        String outtopicname = "results";

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "theoretical-class2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, String> lines = builder.stream(topicStandard);

        /* count() */
        KTable<String, Long> outlines = lines.groupByKey().count();
        outlines.mapValues(v -> "" + v)
                .toStream()
                .peek((key, value) -> System.out.println("-1- Outgoind record - key " + key + " value " + value))
                .to(outtopicname, Produced.with(Serdes.String(), Serdes.String()));


        /* reduce() 
        lines
            .groupByKey()
            .reduce((a, b) -> a + b)
            .mapValues(v -> "" + v)
            .toStream()
            .to(outtopicname + "-2", Produced.with(Serdes.String(), Serdes.String()));

        /* aggregate()
        lines
            .groupByKey()
            .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {
                aggValue[0] += 1;
                aggValue[1] += newValue;

                return aggValue;
            }, Materialized.with(Serdes.String(), new IntArraySerde()))
            .mapValues(v -> v[0] != 0 ? "" + (1.0 * v[1]) / v[0] : "div by 0")
            .toStream()
            .to(outtopicname + "-3", Produced.with(Serdes.String(), Serdes.String()));

        /* groupBy() 
        lines
            .groupBy((k, v) -> "1")
            .count()
            .mapValues(v -> "" + v)
            .toStream()
            .to(outtopicname + "-4", Produced.with(Serdes.String(), Serdes.String()));

        /* swap keys and values
        lines
            .map((k, v) -> new KeyValue<>(v, k))
            .groupByKey(Grouped.with(Serdes.Long(), Serdes.String()))
            .count()
            .mapValues(v -> "" + v)
            .toStream()
            .to(outtopicname + "-5", Produced.with(Serdes.Long(), Serdes.String()));

        /* hopping window 
        Duration windowSize = Duration.ofMinutes(5);
        Duration advanceSize = Duration.ofMinutes(1);
        TimeWindows hoppingWindow = TimeWindows.ofSizeWithNoGrace(windowSize).advanceBy(advanceSize);
        lines
            .groupByKey()
            .windowedBy(hoppingWindow)
            .count()
            .toStream((wk, v) -> wk.key())
            .mapValues((k, v) -> k + " -> " + v)
            .to(outtopicname + "-6", Produced.with(Serdes.String(), Serdes.String()));
        */

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
         streams.start();
    } 
}