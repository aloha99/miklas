package minds.arsbehavior;

public class ARSDynamicBehaviorGoto {

	public String getAction(int targetLocationX, int targetLocationY) {
		String action = "NONE";
		
		if (Math.abs(targetLocationY)>Math.abs(targetLocationX) || targetLocationX==0) {
			if (targetLocationY>1) {
				action = Action.MOVE_FORWARD.toString();
			}
		} else if (targetLocationX<0) {
			action = Action.TURN_RIGHT.toString();
		} else if (targetLocationX>0) {
			action = Action.TURN_LEFT.toString();
		}
		
		return action;
	}
	
	public String getActionToExecuteOnEntity(int targetLocationX, int targetLocationY, Action actionToExecute) {
		String action = "NONE";
		
		if (targetLocationX==0 && targetLocationY==1) {
			action = actionToExecute.toString();
		} else {
			action = getAction(targetLocationX, targetLocationY);
		}
		
		return action;
	}
}
