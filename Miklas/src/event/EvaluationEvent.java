package event;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EvaluationEvent extends Event {

	public EvaluationEvent(String poEventName, HashMap<String, ArrayList<String>> parameters) {
		super(poEventName, parameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String setTargetIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initEventWithPermanentStructures() {
		// TODO Auto-generated method stub
		
	}


}

