package event.action;

import java.util.ArrayList;
import java.util.HashMap;

import ch.aplu.jgamegrid.Location;
import event.EventAction;

public class EventActionOnForwardLocation extends EventAction {

	public EventActionOnForwardLocation(String poEventName,	HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void setIndividualCustomProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void runEffectOfEvent() {
		//Get action inputs
		Location myNextLocation = this.getActionExecutor().getNeighborLocationOfMyDirection();
		
		
		//Run effect on the target objects of the action taken
		this.myBody.executeActionOnEntityOnField(myNextLocation, this.triggerActionName, true);	//true because execute only on top entity
				
				
		
	}

}
