package pt.uc.dei.kafkaStreams;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class SimpleConsumer {


    public static void getTotalTemperaturesStdWS(KStream<String, String> lines){
        KTable<String, Long> outlines = lines.groupByKey().count();
        
    }


    public static void main(String[] args) throws Exception{
        //Assign topicName to string variable
        String [] topicNames = {"StandardWeather","WeatherAlert"};

        // create instance for properties to access producer configs
        Properties props = new Properties();
        //Assign localhost id
        props.put("bootstrap.servers", "broker1:9092"); //Set acknowledgements for producer requests. props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        for(String topicName : topicNames){
            Consumer<String, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(topicName));


            StreamsBuilder builder = new StreamsBuilder(); 
            KStream<String, String> lines = builder.stream(topicName);

            getTotalTemperaturesStdWS(lines);


            System.out.println("topic:" + topicName);
            try {
                Duration d = Duration.ofSeconds(1000000);
                ConsumerRecords<String, String> records = consumer.poll(d);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.key() + " => " + record.value()); 
                }
            }
            finally {
                consumer.close();
                
            }

        }
    } 
}