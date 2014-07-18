package minds.arsbehavior;

public class ARSBehaviorEat extends ARSBehavior {

	@Override
	protected void setBehavior() {
		this.actionsForStep.add("EAT");
		
	}

	@Override
	public String getName() {
		return "EAT";
	}

}
