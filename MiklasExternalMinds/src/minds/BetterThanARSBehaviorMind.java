package minds;

import java.util.ArrayList;
import java.util.Arrays;

import logger.MyLogger;

import org.slf4j.Logger;

import utils.PerceptionUtils;
import entity.mind.ExternalMindBodyInterface;
import entity.mind.ExternalMindControlInterface;
import entity.mind.ExternalPerceptionInterface;

public class BetterThanARSBehaviorMind implements ExternalMindControlInterface {

	private static final Logger log = MyLogger.getLog("Mind"); 
	
	private ExternalMindBodyInterface gameMethods;
	private PerceptionUtils perceptionUtils = new PerceptionUtils();
	
	private int talkforbidden=(int) (Math.random()*20);
	
	public BetterThanARSBehaviorMind(ExternalMindBodyInterface game) {
		gameMethods = game;
		
	}
	
	@Override
	public void startCycle() {
		
		boolean isPrioritized = false;
		//Get perception
		ArrayList<ExternalPerceptionInterface> perception = gameMethods.getExternalPerception();
		log.info("Perception {}", perception);
		
		//Make beliefs, infer beliefs
		ArrayList<String> obstacleBodies = new ArrayList<String>(Arrays.asList("OBSTACLE"));
		ArrayList<ExternalPerceptionInterface> obstacleInThePathList = perceptionUtils.getEntitiesOfPosition(0, 1, obstacleBodies, new ArrayList<String>(), perception);
		
		ArrayList<String> attackableBodies = new ArrayList<String>(Arrays.asList("HUMANPLAYER","RANDOMPLAYER","ACTIONLESSPLAYER","ARSPLAYERTYPE"));
		ArrayList<ExternalPerceptionInterface> playersInThePathList = perceptionUtils.getEntitiesOfPosition(0, 1, attackableBodies, new ArrayList<String>(), perception);
		
		ArrayList<ExternalPerceptionInterface> playersInLeftThePathList = perceptionUtils.getEntitiesOfPosition(1, 0, attackableBodies, new ArrayList<String>(), perception);
		
		ArrayList<ExternalPerceptionInterface> playersInRightThePathList = perceptionUtils.getEntitiesOfPosition(-1, 0, attackableBodies, new ArrayList<String>(), perception);
		
		ArrayList<String> eatableBodies = new ArrayList<String>(Arrays.asList("EATABLEGOOD","EATABLEBAD"));
		ArrayList<ExternalPerceptionInterface> entitiesInField = perceptionUtils.getEntitiesOfPosition(0, 0, eatableBodies, new ArrayList<String>(), perception);
		
		//Decision unit
		String action = "";
		if (playersInLeftThePathList.isEmpty()==false) {
			action = "TURN_LEFT";
		} else if (playersInRightThePathList.isEmpty()==false) {
			action = "TURN_RIGHT";
			//first attack
		} else if (playersInThePathList.isEmpty()==false) {
			//Player in front of me
			double choice = Math.random();
			if (choice<0.5) {
				action = "ATTACK";
				isPrioritized=true;
			} else {
				action = "NONE";
			}		
		} else if (entitiesInField.isEmpty()==false) {
			//cake at my place
			action = "EAT";
		} else if (obstacleInThePathList.isEmpty()==false) {
			//Obstacle in the way
			String[] moveactions = {"TURN_LEFT", "TURN_RIGHT"};
			action = moveactions[(int) (moveactions.length*Math.random())];
		} else {
			//Create random action
			String[] actions = {"MOVE_FORWARD", "TURN_LEFT", "TURN_RIGHT"};
			action = actions[(int) (actions.length*Math.random())];
		}
		
		
		if (isPrioritized==false) {
			//Remove determinism and put random actions in 10% of the cases
			if (Math.random()<0.05) {
				String[] allactions = {"MOVE_FORWARD", "TURN_LEFT", "TURN_RIGHT", "EAT", "ATTACK"};
				action = allactions[(int) (allactions.length*Math.random())];
			}
			
			//Talk sometimes
			if (talkforbidden==0) {
				action = "TALK";
				
				this.talkforbidden=50;
			} else {
				if (Math.random()<0.5) {
					this.talkforbidden--;
				}
			}
		}
		
		try {
			this.gameMethods.setAction(action);
		} catch (Exception e) {
			log.error("Error in updating action {}", action, e);
		}
		
//		synchronized (this) {
//			try {
//				this.wait(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	
	}

	@Override
	public void killMind() {
		// TODO Auto-generated method stub
		
	}

}
