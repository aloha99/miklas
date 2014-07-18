package event.body;

import java.util.ArrayList;
import java.util.HashMap;

import event.BodyEffectEvent;
import event.Datapoint;

public class EventDeath extends BodyEffectEvent {
	
	private int minHealth;

	public EventDeath(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void runEffectOfEvent() {
		//Set minhealth
		//this.myBody.getSharedVariables().setPermanentData(new Datapoint<Integer>(this.minhealthVariableName, this.minHealth));
				
		//Set current health
		//this.myBody.getSharedVariables().setPermanentData(new Datapoint<Integer>(healthVariableName, initHealth));
		
		this.myBody.killActor();
	}

	@Override
	protected void setCustomProperties() {
		// none
		
	}

}
