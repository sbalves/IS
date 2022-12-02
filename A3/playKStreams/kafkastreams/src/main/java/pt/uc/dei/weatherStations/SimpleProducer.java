package pt.uc.dei.weatherStations;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.github.javafaker.Faker;
import java.util.Random;

public class SimpleProducer {


    public static String generateStandardWeather(){
        String [] locations = {"150 Carroll Causeway","12782 Kirby Avenue","115 Cole Rue","51930 Lincoln Trail","65807 Wolf Club","623 Kihn Bridge","02854 Joanne Junctions","36962 Carter Loop","83065 Weissnat Freeway","9473 Kuhic Prairie"};        
        Random rand = new Random();
        return locations[rand.nextInt(10)] + "#" +  rand.nextInt(-10,40);
    } 
    
    public static String generateWeatherAlert(){
        String [] type = {"red","green"};
        String [] locations = {"150 Carroll Causeway","12782 Kirby Avenue","115 Cole Rue","51930 Lincoln Trail","65807 Wolf Club","623 Kihn Bridge","02854 Joanne Junctions","36962 Carter Loop","83065 Weissnat Freeway","9473 Kuhic Prairie"};        
        Random rand = new Random();
        return locations[rand.nextInt(10)] + "#" +  type[rand.nextInt(2)];
    } 
    


    public static void main(String[] args) throws Exception{ //Assign topicName to string variable

        String [] topicNames = {"StandardWeather","WeatherAlert"};

        // create instance for properties to access producer configs
        Properties props = new Properties(); //Assign localhost id
        props.put("bootstrap.servers", "broker1:9092");
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
        props.put("value.serializer", "org.apache.kafka.common.serialization.LongSerializer");

        for(String topicName : topicNames){
            Producer<String, Long> producer = new KafkaProducer<>(props);
            for(int i = 0; i < 1000; i++) {
                if(topicName=="StandardWeather")
                    producer.send(new ProducerRecord<String, Long>(topicName, generateStandardWeather(),(long) i));
                else
                    producer.send(new ProducerRecord<String, Long>(topicName, generateWeatherAlert(),(long) i));
                if (i % 100 == 0)
                    System.out.println("Sending message " + (i + 1) + " to topic " + topicName);
            }
            producer.close();

        }
    }
}