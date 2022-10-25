package client;

import dto.CreateStudentWebRequest;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class ReactiveClientApplication {

/*
    void listNamesBirthdates(ResponseEntity response){

    }
*/


    public static void main(String[] args) {

        WebClient client = WebClient.create("http://localhost:8080");
        Flux<CreateStudentWebRequest> studentFlux = client.get()
                .uri("/student")
                .retrieve()
                .bodyToFlux(CreateStudentWebRequest.class);
        studentFlux.subscribe(System.out::println);
    }
}
