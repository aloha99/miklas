package event.body;

import java.util.ArrayList;
import java.util.HashMap;

import event.BodyEffectEvent;
import event.Datapoint;

public class EventChangeHealth extends BodyEffectEvent {
	
	private final String HEALTHCHANGEVARIABLENAME = "healthchangename";
	private final String HEALTHCHANGENAME = "healthchange";
	private String healthChangeVariable;
	
	private int healthChange;

	public EventChangeHealth(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected void runEffectOfEvent() {
		//int currentHealth = (int)this.myBody.getSharedVariables().getData("HEALTH");
		
		//Set new health
		try {
			//Get first healthchange
			Datapoint<Integer> dp0 = new Datapoint<Integer>(healthChangeVariable);
			this.myBody.getSharedVariables().getData(dp0, false, 0);
			int originalHealthchange = dp0.getValue();
			
			this.myBody.getSharedVariables().setTemporaryData(new Datapoint<Integer>(healthChangeVariable, this.healthChange + originalHealthchange));
		} catch (Exception e) {
			log.error("Cannot set temporary datapoint", e);
		}
		
		//int nCurrentHealth = this.myBody.getHealth();
		//int nNewHealth = nCurrentHealth + this.healthChange;
		//this.myBody.setHealth(nNewHealth);
		
		log.debug("Player {} health has been changed with {}", this.myBody.getOwnerEntity().getName(), this.healthChange);
	}

	@Override
	protected void setCustomProperties() {
		healthChange = Integer.valueOf(this.customParameter.getSingleParameter(HEALTHCHANGENAME));
		healthChangeVariable = this.customParameter.getSingleParameter(HEALTHCHANGEVARIABLENAME);
		
	}

}
