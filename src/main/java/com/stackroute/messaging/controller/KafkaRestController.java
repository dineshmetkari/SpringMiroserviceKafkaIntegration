/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stackroute.messaging.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.stackroute.messaging.model.Message;
import com.stackroute.messaging.model.User;
import com.stackroute.messaging.util.Listener;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
/**
 * KafkaRestController
 *
 * @author Dinesh Metkari (dineshmetkari@gmail.com)
 */
@RestController
public class KafkaRestController {
    
    @Value("${topic}")
    private String topic;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private Listener listener;

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    public void hello() throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, "Hello world");
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failed");
            }
        });
    }
    
    
    @RequestMapping(value = "/sendmessage/", method = RequestMethod.POST)
	public ResponseEntity<Void> sendMessage(@RequestBody Message message, 	UriComponentsBuilder ucBuilder) throws ExecutionException, InterruptedException {
		System.out.println("Sending Message " + message.getMessage());
		
		 ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message.getTopic(), message.getUsername() +"|"+message.getCircle()+"|"+message.getMessage());
	        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
	            @Override
	            public void onSuccess(SendResult<String, String> result) {
	                System.out.println("success");
	            }

	            @Override
	            public void onFailure(Throwable ex) {
	                System.out.println("failed");
	            }
	        });
	       


		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/message/{id}").buildAndExpand("1234").toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

    
    @RequestMapping(value = "/usermessage/", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody User user, 	UriComponentsBuilder ucBuilder) throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("Creating usermessage " + user.getName());
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("'id':");
		sb.append(user.getId());
		sb.append(", 'name':'");
		sb.append(user.getName());
		sb.append("', 'age':");
		sb.append(user.getAge());
		sb.append(", 'salary':");
		sb.append(user.getSalary());
		sb.append("}");
		System.out.println("JSON Message1:"+sb.toString());
		
		//Convert object to JSON string
		ObjectMapper mapper = new ObjectMapper();
		String userJSon = mapper.writeValueAsString(user);
		System.out.println("JSON Message2:"+userJSon);
		
		
		//ObjectMapper mapper = new ObjectMapper();
		//String jsonInString = "{'name' : 'mkyong'}";

		//JSON from file to Object
		//User user = mapper.readValue(new File("c:\\user.json"), User.class);

		//JSON from String to Object
		//User user = mapper.readValue(jsonInString, User.class);
		
		
		  ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, userJSon);
	        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
	            @Override
	            public void onSuccess(SendResult<String, String> result) {
	                System.out.println("success");
	            }

	            @Override
	            public void onFailure(Throwable ex) {
	                System.out.println("failed");
	            }
	        });
	        
		

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}
