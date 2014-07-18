package minds.arsbehavior;

import java.util.ArrayList;

public abstract class ARSBehavior implements ARSBehaviorInterface {
	protected ArrayList<String> actionsForStep = new ArrayList<String>();
	private int currentStep=0;

	@Override
	public String getNextAction() {
		String action = "NONE";
		if (this.isFinished()==false) {
			action = this.actionsForStep.get(currentStep);
			this.incrementBehavior();
		} else {
			init();
		}
		
		return action;
	}

	private void incrementBehavior() {
		this.currentStep++;
	}

	@Override
	public void init() {
		this.currentStep=0;
		actionsForStep.clear();
		this.setBehavior();
	}
	
	private boolean isFinished() {
		boolean result = true;
		if (this.currentStep<this.actionsForStep.size()) {
			result = false;
		}
		
		return result;
	}
	
	public abstract String getName();
	
	protected abstract void setBehavior();
}
