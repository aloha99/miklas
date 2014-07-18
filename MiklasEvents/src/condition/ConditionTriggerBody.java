package condition;

import java.util.ArrayList;
import java.util.HashMap;

import entity.EntityInterface;
import event.Datapoint;
import event.EventVariables;

public class ConditionTriggerBody extends Condition {
	
	private ArrayList<String> triggerBodyTypes;

	public ConditionTriggerBody(String name, HashMap<String, ArrayList<String>> parameters) {
		super(name, parameters);
	}

	@Override
	public boolean testCondition() {
		boolean result = false;
		
		EntityInterface entityObject=null;
		try {
			Datapoint<EntityInterface> dp = new Datapoint<EntityInterface>(EventVariables.ENTITYOFCALLER.toString());
			boolean isChanged = this.assignedEvent.getLocalDataStructure(dp);
			entityObject = dp.getValue();
		} catch (Exception e) {
			log.error("Wrong datatype", e);
		}
		EntityInterface entity = null;
		if (entityObject instanceof EntityInterface) {
			entity = ((EntityInterface)entityObject);
		}
		
		if (this.triggerBodyTypes.contains(entity.getBodyType())==true) {
			result = true;
		}
		
		return result;
	}

	@Override
	protected void setCustomProperties() {
		triggerBodyTypes = this.customParameter.getMultipleParameter("triggerbody");
		
	}

	@Override
	public void initConditionWithPermanentDatastructures() {
		// TODO Auto-generated method stub
		
	}

}
