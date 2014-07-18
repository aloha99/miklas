package event.body;

import java.util.ArrayList;
import java.util.HashMap;

import ch.aplu.jgamegrid.Location;
import event.BodyEffectEvent;
import event.Datapoint;
import event.EventVariables;

public class EventIsPulled extends BodyEffectEvent {
	
	private final String TARGETLOCATION = "targetlocation";
	private final String ACTIONNAME = "executeaction";
	private String targetLocationVariableName;
	private String action;

	public EventIsPulled(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);

	}

	@Override
	protected void runEffectOfEvent() {
		//Get entity
		Location entityLocation = null;
		Datapoint<Location> dp = new Datapoint<Location>(EventVariables.LOCATIONOFCALLER.toString());
		try {
			this.getLocalDataStructure(dp);
			entityLocation = dp.getValue();
		} catch (Exception e) {
			log.error("{}> Cannot load {} in event", this.getEventName(), dp, e);
		}
				
		//Move object to the entity of the caller
		Location newLocation = entityLocation;
		
		Location newAbsoluteLocation = this.myBody.getLocationUtils().getAbsoluteLocationForRelativeLocation(this.myBody.getOwnerEntity().getLocation(), this.myBody.getOwnerEntity().getDirection(), newLocation);//new Location(newLocation.x + this.myBody.getOwnerEntity().getX(), newLocation.y + this.myBody.getOwnerEntity().getY());
		
		//set new location of pushed entity
		try {
			this.myBody.getSharedVariables().setTemporaryData(new Datapoint<Location>(targetLocationVariableName, newAbsoluteLocation));
		} catch (Exception e) {
			log.error("Cannot set temporary datapoint", e);
		}
				
		//Execute action for displacement
		try {
			this.myBody.executeAction(action);
		} catch (Exception e) {
			log.error("Cannot execute action {}", action);
		}
		
	}

	@Override
	protected void setCustomProperties() {
		targetLocationVariableName = this.customParameter.getSingleParameter(TARGETLOCATION);
		action = this.customParameter.getSingleParameter(ACTIONNAME);
		
	}

}
