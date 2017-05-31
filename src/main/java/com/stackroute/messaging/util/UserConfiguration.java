package com.stackroute.messaging.util;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
/**
 * The UserConfiguration.
 *
 * @author Dinesh Metkari (dineshmetkari@gmail.com)
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.stackroute.messaging")
public class UserConfiguration {
	

}