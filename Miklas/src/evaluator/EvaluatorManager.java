package evaluator;

import java.util.ArrayList;
import java.util.HashMap;

import logger.MyLogger;

import org.slf4j.Logger;

import entity.mind.BodyPerceptionInterface;
import entity.mind.ExternalPerceptionInterface;
import userinterface.VisualizationEvaluationInterface;

public class EvaluatorManager implements EvaluatorManagerMindInterface {

	private static final Logger log = MyLogger.getLog("Evaluation");
	
	private HashMap<String, EntityEvaluation> moScoreTable = new HashMap<String, EntityEvaluation>();
	private HashMap<String, Integer> moScoreForEvent = new HashMap<String, Integer>();
	private final VisualizationEvaluationInterface visualization;
	
	public EvaluatorManager(VisualizationEvaluationInterface visualization) {
		this.visualization = visualization;
	}

	@Override
	public void updateScoreFromEvent(String entityIdentifier, String poEventName) {
		try {
			updateScore(entityIdentifier, this.moScoreForEvent.get(poEventName));
		} catch (NullPointerException e) {
			log.error("Event {} does not exist in the score table and has not been registered for player {}.", poEventName, entityIdentifier);
		}
	}
	
	private void updateScore(String entityIdentifier, int pnAdditionalScore) {
		//Get evaluator
		EntityEvaluation eval = this.moScoreTable.get(entityIdentifier);
		if (eval!=null) {
			int oldScore = eval.getScore();
			eval.updateScore(pnAdditionalScore);
//			int nScore = eval.getScore();
//			int nNewScore = nScore + pnAdditionalScore;
//			eval.setScore(nNewScore);

			//Update visualization
			this.visualization.updateStats(entityIdentifier, this.moScoreTable.get(entityIdentifier));
			log.debug("Score was updated for entity {} from {} to {}.", entityIdentifier, oldScore, eval.getScore());
		} else {
			log.debug("Entity {} has not been registered in the score table", entityIdentifier);
		}		
	}
	
	public void updateHealth(String entityIdentifier, int currentHealth) {
		this.moScoreTable.get(entityIdentifier).setHealth(currentHealth);
		this.visualization.updateStats(entityIdentifier, this.moScoreTable.get(entityIdentifier));
	}
	
	@Override
	public void updateEvaluation(String entityIdentifier, ArrayList<ExternalPerceptionInterface> perceptionValues, BodyPerceptionInterface bodyValues, String currentAction) {
		//Get evaluator
		EntityEvaluation eval = this.moScoreTable.get(entityIdentifier);
		if (eval!=null && currentAction.equals("NONE")==false) {
			
			//If no other action was set, then update neutral actions
			eval.updateNeutralActions();
		}
		
	}
	


	@Override
	public void registerEntity(String entityIdentifier, int health) throws Exception {
		if (this.moScoreTable.get(entityIdentifier)==null) {
			//moScoreTable.put(poEntityName, 0);
			this.moScoreTable.put(entityIdentifier, new EntityEvaluation(entityIdentifier, health));
			log.debug("Entity {} was registered in the score list", entityIdentifier);
		}
	}

	@Override
	public void registerEvent(String poEventName, int pnScoreChange) throws Exception {
		if (this.moScoreForEvent.get(poEventName)==null) {
			moScoreForEvent.put(poEventName, pnScoreChange);
			log.debug("Event {} was registered in the score list", poEventName);
		} else {
			throw new Exception("Agent already registered");
		}
		
	}

	@Override
	public String toString() {
		return "ScoreManager [moScoreTable=" + moScoreTable
				+ ", moScoreForEvent=" + moScoreForEvent + "]";
	}

}
