package minds;

import java.util.ArrayList;

import logger.MyLogger;

import org.slf4j.Logger;

import entity.mind.ExternalMindBodyInterface;
import entity.mind.ExternalMindControlInterface;
import entity.mind.ExternalPerceptionInterface;



public class ActionLessMind implements ExternalMindControlInterface {

	private static final Logger log = MyLogger.getLog("Mind"); 
	
	private ExternalMindBodyInterface gameMethods;
	
	public ActionLessMind(ExternalMindBodyInterface game) {
		gameMethods = game;
	}
	
	@Override
	public void startCycle() {
		ArrayList<ExternalPerceptionInterface> perception = gameMethods.getExternalPerception();
		log.info("Test");
		log.info("Perception {}", perception);
	}

	@Override
	public void killMind() {
		log.info("Killed mind");
		
	}
}
