/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stackroute.messaging.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

import com.stackroute.messaging.model.User;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
/**
 * Listener
 *
 * @author Dinesh Metkari (dineshmetkari@gmail.com)
 */
public class Listener {

	public final CountDownLatch countDownLatch1 = new CountDownLatch(1);

	@KafkaListener(id = "topicid", topics = "topic-ActivityStream", group = "ActivityStream")
	public void listen(ConsumerRecord<?, ?> record) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Message Received from topic:" + record);
		String jsonString = record.toString();
		String searchString ="value =";
		jsonString = jsonString.substring(jsonString.indexOf(searchString)+searchString.length(), jsonString.length()-1);
		System.out.println("Message Received from topic:" + jsonString);
		
		
		User user = null;
		try {
			ObjectMapper mapper = new ObjectMapper();

			//jsonString = record.toString();
			System.out.println("JSON Stringc:" + jsonString);
			// JSON from file to Object
			// User user = mapper.readValue(new File("c:\\user.json"),
			// User.class);

			// JSON from String to Object
			user = mapper.readValue(jsonString, User.class);
		} catch (Exception e) {
			System.out.println("Exception Message to JSON Conversion:" + e.getMessage());
			e.printStackTrace();
		}

		
		System.out.println("JSON Converted Message:" + user.getId() + "|" + user.getName() + "|" + user.getAge() + "|"
				+ user.getSalary());


	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
	    System.out.println("Calling Spring Rest Api with Kafka Message Received: http://localhost:8080/user/");
	    ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8080/user/",
	            user, Void.class);
	    
	    //return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	    
	    System.out.println(response.getHeaders().toString());

	    
		countDownLatch1.countDown();
	}

}
