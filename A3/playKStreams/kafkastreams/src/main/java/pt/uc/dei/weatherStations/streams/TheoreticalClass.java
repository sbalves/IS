package pt.uc.dei.weatherStations.streams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import javax.sound.sampled.SourceDataLine;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.json.JSONException;
import org.json.JSONObject;

public class TheoreticalClass {
    static String fileName =  System.getProperty("user.dir") + "/results.out";

    public static void writeResultsFile(String str){
        byte[] strToBytes = str.getBytes();
        try {
            System.out.println("fileName:" + fileName);
            FileOutputStream outputStream = new FileOutputStream(fileName, true);
            outputStream.write(strToBytes);
            outputStream.close();
            System.out.println("print done <3");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getParameter(String str, String par) throws JSONException{
        JSONObject strJson = new JSONObject(str);
        return strJson.getString(par);
    }

    /*
    {"schema":
        {"type":"struct",
            "fields":[
                    {"type":"double",
                    "optional":false,
                    "field":"revenue"},
                    {"type":"double",
                    "optional":false,
                    "field":"expenses"},
                    {"type":"double",
                    "optional":false,
                    "field":"profit"}],
            "optional":false,
            "name":"total data"},
            "payload":{"revenue":988500.0,
                        "expenses":731430.0,
                        "profit":257070.0
            }
    }
    */

    public static String addToJsonSchema(String key, String value, String template) throws JSONException{

        
        JSONObject templateJson = new JSONObject(template);

        templateJson.getJSONObject("payload").put(key,value);
        
        return templateJson.toString();
    }

    /* 1. Count	temperature	readings	of	standard	weather	events	per	weather	station */
    public static void getTotalTemperaturesStdWS(KStream<String, String> lines, String resultTopic, int topic, String template){
        writeResultsFile("1. Count	temperature	readings	of	standard	weather	events	per	weather	station\n");
        KTable<String, Long> outlines = lines.groupByKey().count();
        outlines.mapValues((k,v) -> {
                                writeResultsFile("Weather Station: " + k + "\t\tNo. temperature readings: " + v + "\n");
                                try {
                                    return (addToJsonSchema(k,"" + v, template));
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                return null;
                                })
                .toStream()
                .peek((key, value) -> {
                    if(topic == 1)
                        System.out.println("Weather Station: " + key + " No. temperature readings: " + value);
                    else
                        System.out.println("Weather Station: " + key + " No. alerts: " + value);})
                .to(resultTopic + "-1", Produced.with(Serdes.String(), Serdes.String()));
    }

    /* 2. Count	temperature	readings	of	standard	weather	events	per	location */
    public static void getTotalTemperaturesStdLocation(KStream<String, String> lines, String resultTopic){
        writeResultsFile("2. Count	temperature	readings	of	standard	weather	events	per	weather	location\n");

                KTable outlines = lines.map((k,v) -> {
                                                try {
                                                    return new KeyValue<>(getParameter(v, "location"), v);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                return null;
                                           })
                                        .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))    
                                        .count();


                outlines.mapValues((k,v) -> {
                        writeResultsFile("Location: " + k + "\t\tNo. temperature readings: " + v + "\n");
                        return ("" + v);})
                        .toStream()
                        .peek((key, value) -> System.out.println("Location: " + key + " No. temperature readings: " + value))
                        .to(resultTopic + "-2", Produced.with(Serdes.String(), Serdes.String()));
    }

    public static int getMinTemperature(int v1, int v2){
        if(v1 < v2)
            return v1;
        return v2;
    }

    public static int getMaxTemperature(int v1, int v2){
        if(v1 > v2)
            return v1;
        return v2;
    }

    /* 3. Get	minimum	and	maximum	temperature	per	weather	station. */
    public static void getMinMaxPerWS(KStream<String, String> lines, String resultTopic){
        writeResultsFile("3. Get	minimum	and	maximum	temperature	per	weather	station.\n");
        lines.map((k,v) -> {
            try {
                return new KeyValue<>(k, Integer.valueOf(getParameter(v, "temperature")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))  
            .reduce((v1, v2) ->  getMinTemperature(v1,v2))
            .toStream()
            .peek((k,v) -> System.out.println("Weather station: " + k + "\t\t\tMin temperature: " + v))
            .to(resultTopic + "-3", Produced.with(Serdes.String(), Serdes.Integer()));

        lines.map((k,v) -> {
                try {
                    return new KeyValue<>(k, Integer.valueOf(getParameter(v, "temperature")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            })
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))  
                .reduce((v1, v2) -> {
                    v2 = getMaxTemperature(v1,v2);
                    return v2;})
                .toStream()
                .peek((k,v) -> System.out.println("Weather station: " + k + "\t\t\tMax temperature: " + v))
                .to(resultTopic + "-3", Produced.with(Serdes.String(), Serdes.Integer()));

                /* */
    }

    
    /* 4. Get	minimum	and	maximum	temperature	per	weather	location.  */
    public static void getMinMaxPerLocation(KStream<String, String> lines, String resultTopic){
        writeResultsFile("4. Get	minimum	and	maximum	temperature	per	location.\n");

        lines
            .map((k,v) -> {
                        try {
                            return new KeyValue<>(getParameter(v, "location"), Integer.valueOf(getParameter(v, "temperature")));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
            .reduce((v1, v2) -> getMinTemperature(v1,v2))
            .toStream()
            .peek((k,v) -> System.out.println("Location: " + k + "\t\t\tMin temperature: " + v))
            .to(resultTopic + "-4", Produced.with(Serdes.String(), Serdes.Integer()));

            lines
            .map((k,v) -> {
                        try {
                            return new KeyValue<>(getParameter(v, "location"), Integer.valueOf(getParameter(v, "temperature")));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
            .reduce((v1, v2) -> getMaxTemperature(v1,v2))
            .toStream()
            .peek((k,v) -> System.out.println("Location: " + k + "\t\t\tMax temperature: " + v))
            .to(resultTopic + "-4", Produced.with(Serdes.String(), Serdes.Integer()));
            
    }

    /* 6. Count	the	total	alerts	per	type */
    public static void getTotalAlertsType(KStream<String, String> lines, String resultTopic){
        writeResultsFile("6. Count	the	total	alerts	per	type \n");

        lines.map((k,v) -> {
                            try {
                                System.out.println(v);
                                return new KeyValue<>(getParameter(v, "type"), v);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return null;
                        })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))    
            .count()
            .toStream()
            .peek((key, value) -> System.out.println("Type: " + key + " No. alerts: " + value))
            .to(resultTopic + "-6");
    }



    /* 7. Get	minimum	temperature of	weather	stations	with	red	alert	events  */
    public static void getMinTempRedAlert(KStream<String, String> linesAlerts, KStream<String, String> linesStd, String resultTopic){
        writeResultsFile("7. Get	minimum	temperature of	weather	stations	with	red	alert	events \n");
        
        KStream<String, String> redAlerts  = linesAlerts /*  => left  {"ws1":{"type":"red"}}; {"ws2":{"type":"red"}};
                                                                      {"ws1":{"type":"red"}}; {"ws3":{"type":"red"}}; */
        .filter((k,v) -> {
            try {
                //System.out.println("value: " + getParameter(v,"type"));
                return "red".equals(getParameter(v,"type"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        });
        

            KStream<String, String> temperatures = linesStd  /* => right  {"ws1":{"temperature":"23"}}; {"ws2":{"temperature":"0"}};
                                                                           {"ws1":{"temperature":"1"}}; {"ws3":{"temperature":"37"}}; */
                .map((k,v) -> {
                try {
                    System.out.println("ValueTemp: " + v);
                    return new KeyValue<>(k, getParameter(v, "temperature"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
                });

        /* {"ws1":{"type":"red","temperature":"23"}}; {"ws2":{"type":"red","temperature":"0"}};
           {"ws1":{"type":"red","temperature":"1"}}; {"ws3":{"type":"red","temperature":"37"}};   
        */


        ValueJoiner<String,String,String> valueJoiner = (leftValue, rightValue) -> {
                                                                                    JSONObject joinedValue;
                                                                                    try {
                                                                                        joinedValue = new JSONObject(leftValue);
                                                                                        joinedValue.put("temperature", Integer.valueOf(rightValue));
                                                                                        return joinedValue.toString();
                                                                                    } catch (JSONException e) {
                                                                                        // TODO Auto-generated catch block
                                                                                        System.out.println("Deu erro :(");
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    return null;
                                                                                    };

        redAlerts.join(temperatures, valueJoiner, JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(1)))
                    .map((k,v) -> {
                        try {
                            System.out.println("K: " + k + " v: " + v);
                            return new KeyValue<>("1", Integer.valueOf(getParameter(v, "temperature")));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })      
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))                                                               
                    .reduce((v1, v2) -> getMinTemperature(v1,v2))
                    .toStream()
                    .peek((k,v) -> System.out.println("K: " + k + " Min temp: " + v))
                    .to(resultTopic + "-7", Produced.with(Serdes.String(), Serdes.Integer()));
    }
    
    /* 8. Get maximum temperature of each location of alert events for the last hour
    (students are allowed to define a different value for the time window)  */
    public static void getMaxTempAlertHour(KStream<String, String> linesAlerts, KStream<String, String> linesStd, String resultTopic){
        writeResultsFile("8. Get maximum temperature of each location of alert events for the last hour \n");
        
        KStream<String, String> AlertLocations  = linesAlerts /*  => left  {"ws1":{"type":"red"}}; {"ws2":{"type":"red"}};
                                                                      {"ws1":{"type":"red"}}; {"ws3":{"type":"red"}}; */
        .map((k,v) -> {
                try {
                    System.out.println("ValueTemp: " + v);
                    return new KeyValue<>(getParameter(v, "location"),v);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
                });

        KStream<String, String> StdLocations  = linesStd /*  => left  {"ws1":{"type":"red"}}; {"ws2":{"type":"red"}};
                                                                      {"ws1":{"type":"red"}}; {"ws3":{"type":"red"}}; */
        .map((k,v) -> {
                try {
                    System.out.println("ValueTemp: " + v);
                    return new KeyValue<>(getParameter(v, "location"), getParameter(v, "temperature"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
                });
        
        ValueJoiner<String,String,String> valueJoiner = (leftValue, rightValue) -> {
                                                                                    JSONObject joinedValue;
                                                                                    try {
                                                                                        joinedValue = new JSONObject(leftValue);
                                                                                        joinedValue.put("temperature", rightValue);
                                                                                        return joinedValue.toString();
                                                                                    } catch (JSONException e) {
                                                                                        // TODO Auto-generated catch block
                                                                                        System.out.println("Deu erro :(");
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    return null;
                                                                                    };


        AlertLocations.join(StdLocations, valueJoiner, JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(60)))
                    .map((k,v) -> {
                        try {
                            System.out.println("K: " + k + " v: " + v);
                            return new KeyValue<>(getParameter(v, "location"), Integer.valueOf(getParameter(v, "temperature")));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })      
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))                                                               
                    .reduce((v1, v2) -> getMaxTemperature(v1,v2))
                    .toStream()
                    .peek((k,v) -> System.out.println("K: " + k + " Max temp: " + v))
                    .to(resultTopic + "-8", Produced.with(Serdes.String(), Serdes.Integer()));
        

    }
    
    /* 9. Get minimum temperature per weather station in red alert zones */
    public static void getMinTempRedZones(KStream<String, String> linesAlerts, KStream<String, String> linesStd, String resultTopic){
         writeResultsFile("9. Get minimum temperature per weather station in red alert zones \n");
        
        KStream<String, String> RedAlert  = linesAlerts 
         .filter((k,v) -> {
            try {
                //System.out.println("value: " + getParameter(v,"type"));
                return "red".equals(getParameter(v,"type"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        });
        
        KStream<String, String> StdTemp = linesStd  
            .map((k,v) -> {
            try {
                System.out.println("ValueTemp: " + v);
                return new KeyValue<>(k, getParameter(v, "temperature"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            });
        
        
        ValueJoiner<String,String,String> valueJoiner = (leftValue, rightValue) -> {
                                                                                    JSONObject joinedValue;
                                                                                    try {
                                                                                        joinedValue = new JSONObject(leftValue);
                                                                                        joinedValue.put("temperature", rightValue);
                                                                                        return joinedValue.toString();
                                                                                    } catch (JSONException e) {
                                                                                        // TODO Auto-generated catch block
                                                                                        System.out.println("Deu erro :(");
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    return null;
                                                                                    };

        RedAlert.join(StdTemp, valueJoiner, JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(1)))
                    .map((k,v) -> {
                        try {
                            System.out.println("K: " + k + " v: " + v);
                            return new KeyValue<>(k, Integer.valueOf(getParameter(v, "temperature")));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })      
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))                                                               
                    .reduce((v1, v2) -> getMinTemperature(v1,v2))
                    .toStream()
                    .peek((k,v) -> System.out.println("K: " + k + " Min temp: " + v));
        

    }
    
    /* 10. Get the average temperature per weather station */
    public static void getAvgTempPerWS( KStream<String, String> lines, String resultTopic){

        lines.map((k,v) -> {
            try {
                System.out.println(v);
                return new KeyValue<>(k, Integer.valueOf(getParameter(v, "temperature")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
            .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {
                aggValue[0] += 1;
                aggValue[1] += newValue;

                return aggValue;
            }, Materialized.with(Serdes.String(), new IntArraySerde()))
            .mapValues(v -> v[0] != 0 ? "" + (1.0 * v[1]) / v[0] : "div by 0")
            .toStream()
            .peek((k, v) -> System.out.println("Weather Station: " + k + " Average temperature: " + v))
            .to(resultTopic + "-10", Produced.with(Serdes.String(), Serdes.String()));

    }
    
    /* 11. Get the average temperature of weather stations with red alert events for the last hour
    (students are allowed to define a different value for the time window)  */
    public static void getAvgTempRedZonesLastHour(KStream<String, String> linesAlerts, KStream<String, String> linesStd, String resultTopic){
        writeResultsFile("11. Get	minimum	temperature of	weather	stations	with	red	alert	events \n");
        
        KStream<String, String> redAlerts  = linesAlerts /*  => left  {"ws1":{"type":"red"}}; {"ws2":{"type":"red"}};
                                                                      {"ws1":{"type":"red"}}; {"ws3":{"type":"red"}}; */
        .filter((k,v) -> {
            try {
                //System.out.println("value: " + getParameter(v,"type"));
                return "red".equals(getParameter(v,"type"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        });
        

        KStream<String, String> temperatures = linesStd  /* => right  {"ws1":{"temperature":"23"}}; {"ws2":{"temperature":"0"}};
                                                                        {"ws1":{"temperature":"1"}}; {"ws3":{"temperature":"37"}}; */
            .map((k,v) -> {
            try {
                System.out.println("ValueTemp: " + v);
                return new KeyValue<>(k, getParameter(v, "temperature"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            });

        /* {"ws1":{"type":"red","temperature":"23"}}; {"ws2":{"type":"red","temperature":"0"}};
           {"ws1":{"type":"red","temperature":"1"}}; {"ws3":{"type":"red","temperature":"37"}};   
        */


        ValueJoiner<String,String,String> valueJoiner = (leftValue, rightValue) -> {
                                                                                    JSONObject joinedValue;
                                                                                    try {
                                                                                        joinedValue = new JSONObject(leftValue);
                                                                                        joinedValue.put("temperature", Integer.valueOf(rightValue));
                                                                                        return joinedValue.toString();
                                                                                    } catch (JSONException e) {
                                                                                        // TODO Auto-generated catch block
                                                                                        System.out.println("Deu erro :(");
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    return null;
                                                                                    };

        redAlerts.join(temperatures, valueJoiner, JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(60)))
        .map((k,v) -> {
            try {
                System.out.println(v);
                return new KeyValue<>("1", Integer.valueOf(getParameter(v, "temperature")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
            .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {
                aggValue[0] += 1;
                aggValue[1] += newValue;

                return aggValue;
            }, Materialized.with(Serdes.String(), new IntArraySerde()))
            .mapValues(v -> v[0] != 0 ? "" + (1.0 * v[1]) / v[0] : "div by 0")
            .toStream()
            .peek((k, v) -> System.out.println("Weather Station: " + k + " Average temperature: " + v))
            .to(resultTopic + "-11", Produced.with(Serdes.String(), Serdes.String()));                                                               
    }


    public static void main(String[] args) throws InterruptedException, IOException, JSONException {         

        String topicStandard = "StandardWeather1";
        String topicAlert = "WeatherAlert1";
        String outtopicname = "results";

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "theoretical-class13");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, String> linesStd = builder.stream(topicStandard);
        KStream<String, String> linesAlert = builder.stream(topicAlert);




    
    String template1 = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"varchar\",\"optional\":false,\"field\":\"expenses\"},{\"type\":\"varchar\",\"optional\":false,\"field\":\"profit\"}],\"optional\":false,\"name\":\"total data\"},\"payload\":{}}";

    
    System.out.println(addToJsonSchema("WS1","temp1", template1));

       // lines.peek((k,v)-> System.out.println(v));
       getTotalTemperaturesStdWS(linesStd, outtopicname, 1, template1);
       //getTotalTemperaturesStdLocation(lines, outtopicname);
       //getMinMaxPerWS(linesStd, outtopicname);
       //getMinMaxPerLocation(linesStd, outtopicname);
       //getTotalTemperaturesStdWS(linesAlert, outtopicname,2);
       //getTotalAlertsType(linesAlert, outtopicname);
       //getMinTempRedAlert(linesAlert, linesStd, outtopicname);
       //getMaxTempAlertHour(linesAlert, linesStd, outtopicname);
       //getMinTempRedZones(linesAlert, linesStd, outtopicname);
       //getAvgTempRedZonesLastHour(linesAlert, linesStd, outtopicname);
       //getAvgTempPerWS(linesStd, outtopicname);

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