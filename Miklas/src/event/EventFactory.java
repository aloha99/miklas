package event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import logger.MyLogger;

import org.slf4j.Logger;

import condition.ConditionFactory;
import condition.ConditionInterface;
import config.EventConfig;

public class EventFactory {
	
	protected static final Logger log = MyLogger.getLog("Event");
	
	/**
	 * Create an event + conditions
	 * 
	 * @param poEventConfig
	 * @return
	 * @throws Exception
	 */
	public Event createEvent(EventConfig poEventConfig) throws Exception {
		//Get matching conditions from event, uninitialized
		ConditionFactory cf = new ConditionFactory();
		
		//Create event
		ArrayList<ConditionInterface> conditionInterfaceList = cf.createConditionsForEvents(poEventConfig);
		
		Event result = null;
		try {
			result = createEventFromConfig(poEventConfig, conditionInterfaceList);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			log.error("Cannot create event {}", poEventConfig);
			throw e;
		}
		
		//Connect event to its conditions
		for (ConditionInterface condition : conditionInterfaceList) {
			result.registerCondition(condition);
		}
		
		log.debug("Event {} created", result);
		return result;
		
	}
	
	/**
	 * Create an event based on reflections
	 * 
	 * @param poEventConfig
	 * @param conditions
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException 
	 */
	private Event createEventFromConfig(EventConfig poEventConfig, ArrayList<ConditionInterface> conditions) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Event result=null;
		String className = poEventConfig.getClassName();
		HashMap<String,ArrayList<String>> parameters = poEventConfig.getParameterMap();
		String eventName = poEventConfig.getEventName();
		try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(String.class, HashMap.class);
			Object obj = constructor.newInstance(eventName, parameters);
			if (obj instanceof EventInterface) {
				result = (Event) obj;
			}
		} catch (ClassNotFoundException e) {
			log.error("Cannot find a class with the name {}", className, e);
			throw new ClassNotFoundException(e.getMessage());
		} catch (InstantiationException e) {
			log.error("Cannot instantiate class with name {}", className, e);
			throw new InstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error("Cannot access class {}", className, e);
			throw new IllegalAccessException(e.getMessage());
		} catch (NoSuchMethodException e) {
			log.error("Cannot initialize constructor for class {}", className, e);
			throw new NoSuchMethodException(e.getMessage());
		} catch (SecurityException e) {
			log.error("Security exception for constructor for class {}", className, e);
			throw new SecurityException(e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("Wrong arguments in constrctor of class {}", className, e);
			throw new IllegalArgumentException(e.getMessage());
		} catch (InvocationTargetException e) {
			log.error("Cannot invoke constructor of class {}", className, e);
			throw new InvocationTargetException(e, e.getMessage());
		}
		
		return result;
	}
}
