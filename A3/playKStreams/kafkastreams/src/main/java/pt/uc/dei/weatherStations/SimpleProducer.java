package pt.uc.dei.weatherStations;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import java.util.Random;

public class SimpleProducer {
    static String [] locations = {"Celas","Solum","S.Jose","Se Velha","Se Nova","Portela","S.Martinho","Baixa"}; 
    static String [] type = {"red","green"};
    static Random rand = new Random();

    
    public static ProducerRecord<String, String> generateWeatherEvent(String topicName, String station) {
        ObjectNode transaction = JsonNodeFactory.instance.objectNode();
        transaction.put("location", locations[rand.nextInt(8)]);
        if(topicName == "StandardWeather")
            transaction.put("temperature",  rand.nextInt(-10,40));
        else
            transaction.put("type", type[rand.nextInt(2)]);
        return new ProducerRecord<>(topicName, station, transaction.toString());
    }

   

    public static void main(String[] args) throws Exception{ //Assign topicName to string variable

        String [] topicNames = {"StandardWeather","WeatherAlert"};

        // create instance for properties to access producer configs
        Properties props = new Properties(); //Assign localhost id
        props.put("bootstrap.servers", "broker1:9092"); // adicionar mais um broker
        //Set acknowledgements for producer requests. props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        for(String topicName : topicNames){
            Producer<String, String> producer = new KafkaProducer<>(props);
            for(int i = 0; i < 30; i++) {
                producer.send(generateWeatherEvent(topicName, "WS1"));
                producer.send(generateWeatherEvent(topicName, "WS2"));
                producer.send(generateWeatherEvent(topicName, "WS3"));
                if (i % 10 == 0)
                    System.out.println("Sending message " + (i + 1) + " to topic " + topicName);
            }
            producer.close();

        }
    }
}