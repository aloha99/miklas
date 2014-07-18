package minds.arsbehavior;

public class ARSBehaviorPanic extends ARSBehavior {

	@Override
	protected void setBehavior() {
		actionsForStep.add(Action.MOVE_BACKWARD.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.MOVE_FORWARD.toString());
		actionsForStep.add(Action.MOVE_FORWARD.toString());
	}

	@Override
	public String getName() {
		return "PANIC";
	}
}
