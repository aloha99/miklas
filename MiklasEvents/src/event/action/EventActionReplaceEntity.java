package event.action;

import java.util.ArrayList;
import java.util.HashMap;

import ch.aplu.jgamegrid.Location;
import event.Datapoint;
import event.EventAction;

public class EventActionReplaceEntity extends EventAction {

	private final String TARGETLOCATION = "targetlocation";
	private String targetLocationVariableName;
	
	public EventActionReplaceEntity(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void runEffectOfEvent() {
		Location targetLocation = null;
		try {
			Datapoint<Location> dp0 = new Datapoint<Location>(targetLocationVariableName);
			this.myBody.getSharedVariables().getData(dp0);
			targetLocation = dp0.getValue();
		} catch (Exception e) {
			log.error("Cannot receive data for {}", targetLocation);
		}
		
		//Run effect on the target objects of the action taken
		this.myBody.executeActionOnEntityOnField(targetLocation, this.triggerActionName, true);	//true because execute only on top entity
		
		//Perform change of state/action on the agent itself
		this.getActionExecutor().setMyLocation(targetLocation);
		
	}

	@Override
	protected void setIndividualCustomProperties() {
		targetLocationVariableName = this.customParameter.getSingleParameter(TARGETLOCATION);
		
	}

}
