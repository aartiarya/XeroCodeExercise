package com.aartitest.selenium.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;

/**
 * @author Aarti Arya
 *
 */

public class EnvironmentProperties extends Properties {

	/*Default Serial Version UID is added to suppress the warning.
	This value is currently not being used anywhere*/
	private static final long serialVersionUID = 1L;
	private Logger logger = new GenericTest().logger;	
	
	public EnvironmentProperties() {
			
		}
	
	public String getCurrentEnvironmentName() {
		Properties propMainEnvFile = new Properties();
		InputStream inputStreamMain = Thread.currentThread().getContextClassLoader().getResourceAsStream("env.properties");
		try {
			propMainEnvFile.load(inputStreamMain);
		} catch(FileNotFoundException e) {
			logger.error("Environment Properties File - env.properties Not Found");
		} catch(IOException e) {
			logger.error("Error while Opening env.properties file");
		}
		
		String currentEnvironment = propMainEnvFile.getProperty("environment.to.be.used");
        //String sysenv = System.getenv("properties.test-env");
        propMainEnvFile.setProperty("environment.to.be.used", currentEnvironment);
		/*
		 * if (!(currentEnvironment.equals(sysenv))){ currentEnvironment = sysenv;
		 * propMainEnvFile.setProperty("environment.to.be.used", currentEnvironment); }
		 */
		return currentEnvironment;
	}


	public String getProperty(String key) {
		String currentEnvironment = getCurrentEnvironmentName();
		//currentEnvironment = currentEnvironment.toLowerCase() + ".properties";
				
		//Use the current environment property file to retrieve environment specific data
		Properties properties = new Properties();
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(currentEnvironment);
		
		try {
			properties.load(inputStream);
			inputStream.close();
		} catch (FileNotFoundException e) {
			logger.error("Environment Properties File - " + currentEnvironment + " Not Found");
		} catch (IOException e) {
			logger.error("Error while Opening " + currentEnvironment + " file");
		}
		
		String value = properties.getProperty(key);
		return value;
	}
}
