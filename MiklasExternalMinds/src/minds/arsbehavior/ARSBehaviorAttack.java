package minds.arsbehavior;

public class ARSBehaviorAttack extends ARSBehavior {

	@Override
	protected void setBehavior() {
		this.actionsForStep.add("ATTACK");
		
	}

	@Override
	public String getName() {
		return "ATTACK";
	}

}
