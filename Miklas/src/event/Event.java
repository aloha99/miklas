package event;

import java.util.ArrayList;
import java.util.HashMap;

import logger.MyLogger;

import org.slf4j.Logger;

import condition.ConditionInterface;
import condition.CustomParameter;

public abstract class Event implements EventInterface, EventConditionInterface {

	protected static final Logger log = MyLogger.getLog("Event");
	private final ArrayList<ConditionInterface> conditions = new ArrayList<ConditionInterface>();
	protected CustomParameter customParameter;
	private final String eventName;
	protected EventHandlerInterface eventHandler;
	private String entityIdentifier; 
	
	public Event(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		this.eventName = poEventName;
		
		this.customParameter = new CustomParameter(parameters);
		
		//Assign the properties to the variables
		try {
			this.setCustomProperties();
		} catch (NullPointerException e) {
			log.error("Cannot find property for event {}", this.eventName, e);
			throw new NullPointerException ("Cannot set properties because a required value is missing for event " + this.eventName);
		}
		
		
		log.debug("Init Event {}, parameters={}", this.eventName, this.customParameter);
	}
	
	public void registerCondition(ConditionInterface condition) {
		condition.init(this);
		this.conditions.add(condition);
	}
	
	public <T> boolean getLocalDataStructure(Datapoint<T> value) throws Exception {
		boolean result = this.eventHandler.getLocalDataStructureFromContainer(value);
		
		return result;
		
	}
	
	@Override
	public boolean runEvent() {
		
		//Check Conditions
		boolean bConditionsMet = checkConditions();
		//Run action
		if (bConditionsMet == true) {
			log.debug("{}> Event {}> Event will be executed. Conditions: {}", entityIdentifier, this.eventName, this.conditions);
			
			//Run effect
			runEffectOfEvent();
			log.debug("{}> Event {}> Event was executed.", entityIdentifier, this.eventName);
			
			//Play sound if any is set
			this.getEventHandler().playSound(entityIdentifier, this.getEventName());
	
			//Set graphic if any is set
			this.getEventHandler().setGraphic(entityIdentifier, this.getEventName());
			
			//Update score
			updateScore(entityIdentifier, this.eventName);
		}
		
		return bConditionsMet;
	}
	
	private void updateScore(String entitiyIdentifier, String poEventName) {
		try {
			this.eventHandler.updateScore(entitiyIdentifier, this.eventName);
		} catch (Exception e) {
			log.error("Could not update score for {}, event ", entitiyIdentifier, this.eventName);
		}
		
	}
	
	protected abstract String setTargetIdentifier();
	
	/**
	 * The children shall initiate the parameters they need, i.e. a body event needs the body from the permananent data structures
	 */
	protected abstract void initEventWithPermanentStructures();
	
	/**
	 * run the actual event if conditions are OK
	 */
	protected abstract void runEffectOfEvent();
	
	protected abstract void setCustomProperties();
	
	public void runEventExit() {
		
	}
	
	
	/**
	 * Check if all conditions match
	 * 
	 * @param poIncomingAction
	 * @param poIncomingBodyType
	 * @return
	 */
	private boolean checkConditions() {
		boolean result = true;
		
		for (ConditionInterface condition : this.conditions) {
			if (condition.testCondition()==false) {
				result=false;
				break;
			}
		}
		
		return result;
	}
	
	public void setEventHandler(EventHandlerInterface poEventHandler) {
		eventHandler = poEventHandler;
	}
	
	public void initPermanentDataStructures() {
		//From the permananent datatypes, init the structures, which shall be used in the event
		initEventWithPermanentStructures();
		
		//This function shall be defined by the children. Set the name of the target on which this identifier is used
		this.entityIdentifier = setTargetIdentifier();	
		
		//Init permanent data structures in the conditons
		for (ConditionInterface c :this.conditions) {
			try {
				c.initConditionWithPermanentDatastructures();
			} catch (Exception e) {
				log.error("Cannot init condition {}", c, e);
			}
		}
	}
	
	@Override
	public String getEventName() {
		return this.eventName;
	}

	public EventHandlerInterface getEventHandler() {
		return eventHandler;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("eventname=");
		builder.append(eventName);
		builder.append(", conditions=");
		builder.append(conditions);
		return builder.toString();
	}



}
