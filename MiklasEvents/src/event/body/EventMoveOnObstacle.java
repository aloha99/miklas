package event.body;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;

import event.BodyEffectEvent;
import event.Datapoint;

public class EventMoveOnObstacle extends BodyEffectEvent {
	
	private final String HEALTHCHANGEVARIABLENAME = "healthchangename";
	private final String HEALTHCHANGENAME = "healthchange";
	private String healthChangeVariable;
	
	private int healthChange;
	
	//private static final Logger log = myLogger.getLog("Event");
	
	public EventMoveOnObstacle(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void runEffectOfEvent() {
		try {
			this.myBody.getSharedVariables().setTemporaryData(new Datapoint<Integer>(healthChangeVariable, this.healthChange));
		} catch (Exception e) {
			log.error("Cannot set temporary datapoint", e);
		}
		
		this.myBody.setActionExecutable(false);
		
		log.debug("{} ran into an obstacle and lost {} Health. Action executable: {}.", this.myBody.getOwnerEntity().getName(), this.healthChange, false);
		
	}

	@Override
	protected void setCustomProperties() {
		healthChange = Integer.valueOf(this.customParameter.getSingleParameter(HEALTHCHANGENAME));
		healthChangeVariable = this.customParameter.getSingleParameter(HEALTHCHANGEVARIABLENAME);
		
	}

}
