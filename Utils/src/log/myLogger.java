/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import propertyhandler.PropertyHandler;

/**
 *
 * @author gunther
 */
public class myLogger {
	private static boolean ansiSet = false;
    
    private myLogger() {
    	
    }
    
    private static void useAnsiSystemInstall() {
    	PropertyHandler ph = new PropertyHandler();
    	try {
			ph.load("config/logger.conf");
			boolean useSystemInstall = ph.getBoolean("usesysteminstall");
			if (useSystemInstall==true) {
				AnsiConsole.systemInstall();
			}
			
		} catch (IOException e) {
			System.out.println("Could not load logger options");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Cannot read value for the use of ansi systeminstall");
			e.printStackTrace();
		}
    	
    }
    
    private static void setSystemPropertyForANSI() {
        //Set this property, in order to activate JAnsi correctly
        System.setProperty("jansi.passthrough", "true");
    }
    
//    /**
//	 * DOCUMENT (muchitsch) - insert description
//	 *
//	 * @since 03.09.2012 15:05:46
//	 *
//	 */
//	private static String getCurrentUser() {
//		
//		String userName="";
//		
//		try{
//			userName = System.getProperty("user.name");
//		}
//		catch(Exception e){
//			System.out.printf(e.toString());						
//		}
//		
//		return userName;
//		
//
//		
//	}
    
    public static Logger getLog(String loggerName) {
        if (ansiSet==false) {
        	useAnsiSystemInstall();
            setSystemPropertyForANSI();
            PropertyConfigurator.configure("config/log4j.properties");
            ansiSet=true;
        }
        
        return LoggerFactory.getLogger(loggerName);
        
    }
	
}
