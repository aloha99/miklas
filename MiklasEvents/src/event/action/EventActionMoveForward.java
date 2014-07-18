package event.action;

import java.util.ArrayList;
import java.util.HashMap;

import ch.aplu.jgamegrid.Location;
import event.EventAction;

public class EventActionMoveForward extends EventAction {

	public EventActionMoveForward(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void runEffectOfEvent() {
		
		//Get action inputs
		Location nextLocation = this.getActionExecutor().getNeighborLocationOfMyDirection();
		
		//Run effect on the target objects of the action taken
		this.myBody.executeActionOnEntityOnField(nextLocation, this.triggerActionName, false);
		
		//Perform change of state/action on the agent itself
		this.getActionExecutor().setMyLocation(nextLocation);
		
	}

	@Override
	protected void setIndividualCustomProperties() {
		//Do Nothing
		
	}

}
