package event.body;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;

import event.BodyEffectEvent;

public class EventAttackPlayer extends BodyEffectEvent {
	
	//private static final Logger log = myLogger.getLog("Event");

	public EventAttackPlayer(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void runEffectOfEvent() {
		//Do nothing, as this is the attacker
		
		log.debug("Attack!!!");
		
	}

	@Override
	protected void setCustomProperties() {
		//None
		
	}

}
