package event;

import java.util.ArrayList;
import java.util.HashMap;

import entity.body.BodyInteractionEngineInterface;
import entity.body.BodyInterface;

public abstract class EventAction extends Event {
	
	protected BodyInterface myBody;
	private BodyInteractionEngineInterface actionExecutor;
	
	protected String triggerActionName;

	public EventAction(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
	}

	@Override
	protected String setTargetIdentifier() {
		return myBody.getOwnerEntity().entitiyIdentifier;
	}

	@Override
	protected void initEventWithPermanentStructures() {
		try {
			//myBody = (BodyInterface)eventHandler.getLocalDataStructureFromContainer(EventVariables.MYBODY.toString(), BodyInterface.class);
			//actionExecutor = (BodyInteractionEngineInterface)eventHandler.getLocalDataStructureFromContainer(EventVariables.INTERACTIONENGINE.toString(), BodyInteractionEngineInterface.class);
			Datapoint<BodyInterface> dp = new Datapoint(EventVariables.MYBODY.toString());
			Datapoint<BodyInteractionEngineInterface> dp2 = new Datapoint(EventVariables.INTERACTIONENGINE.toString());
			boolean res1 = eventHandler.getLocalDataStructureFromContainer(dp);
			boolean res2 = eventHandler.getLocalDataStructureFromContainer(dp2);
			
			myBody = dp.getValue();
			actionExecutor = dp2.getValue();
			
		} catch (Exception e) {
			log.error("", e);
		}
		
	}
	
	@Override
	protected void setCustomProperties() {
		triggerActionName = this.customParameter.getSingleParameter("triggeractionname");
		
		//Add individual custom proerties
		this.setIndividualCustomProperties();
		
	}
	
	protected abstract void setIndividualCustomProperties();

	public BodyInteractionEngineInterface getActionExecutor() {
		return actionExecutor;
	}



}
