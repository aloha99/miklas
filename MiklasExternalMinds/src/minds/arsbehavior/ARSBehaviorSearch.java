package minds.arsbehavior;

public class ARSBehaviorSearch extends ARSBehavior {

	@Override
	protected void setBehavior() {
		//Cover all vision
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.TURN_RIGHT.toString());
		actionsForStep.add(Action.TURN_RIGHT.toString());
		actionsForStep.add(Action.TURN_RIGHT.toString());
		actionsForStep.add(Action.TURN_RIGHT.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		actionsForStep.add(Action.TURN_LEFT.toString());
		
		actionsForStep.add(Action.MOVE_FORWARD.toString());
		actionsForStep.add(Action.MOVE_FORWARD.toString());
		
		//Here random
		int randomUsageOfTurn = (int) (Math.random()*2)+2;
		String[] actions = {Action.TURN_LEFT.toString(), Action.TURN_RIGHT.toString()};
		String randomTurn = actions[(int) (Math.random()*2)];
		for (int i=0; i<=randomUsageOfTurn; i++) {
			actionsForStep.add(randomTurn);
		}
	}

	@Override
	public String getName() {
		return "SEARCH";
	}
}
