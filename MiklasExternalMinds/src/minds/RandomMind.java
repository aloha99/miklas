package minds;

import java.util.ArrayList;

import logger.MyLogger;

import org.slf4j.Logger;

import entity.mind.ExternalMindBodyInterface;
import entity.mind.ExternalMindControlInterface;
import entity.mind.ExternalPerceptionInterface;

public class RandomMind implements ExternalMindControlInterface {

	private static final Logger log = MyLogger.getLog("Mind"); 
	
	private ExternalMindBodyInterface gameMethods;
	
	public RandomMind(ExternalMindBodyInterface game) {
		gameMethods = game;
	}
	
	@Override
	public void startCycle() {
		ArrayList<ExternalPerceptionInterface> perception = gameMethods.getExternalPerception();
		log.info("Perception {}", perception);
		gameMethods.setAction("TURN_LEFT");
		
	}

	@Override
	public void killMind() {
		// TODO Auto-generated method stub
		
	}

}
