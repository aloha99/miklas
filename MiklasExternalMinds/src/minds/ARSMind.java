package minds;

import java.util.ArrayList;

import logger.MyLogger;

import org.slf4j.Logger;

import entity.mind.BodyPerceptionInterface;
import entity.mind.ExternalMindBodyInterface;
import entity.mind.ExternalMindControlInterface;
import entity.mind.ExternalPerceptionInterface;

public class ARSMind implements ExternalMindControlInterface {

	private static final Logger log = MyLogger.getLog("Mind"); 
	
	private ExternalMindBodyInterface gameMethods;
	
	public ARSMind(ExternalMindBodyInterface game) {
		gameMethods = game;
	}
	
	@Override
	public void startCycle() {
		//Inputs
		ArrayList<ExternalPerceptionInterface> perception = gameMethods.getExternalPerception();
		log.info("Perception {}", perception);
		BodyPerceptionInterface body = gameMethods.getBodyPerception();
		log.info("Body {}", body);
		
		//Action
		gameMethods.setAction("MOVE_FORWARD");
		log.info("Action MOVE_FORWARD");
		
	}

	@Override
	public void killMind() {
		// TODO Auto-generated method stub
		
	}

}
