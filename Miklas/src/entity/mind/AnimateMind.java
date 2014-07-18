package entity.mind;

import java.util.ArrayList;



import logger.MyLogger;

import org.slf4j.Logger;

import entity.body.BodyMindInterface;
import evaluator.EvaluatorManagerMindInterface;

public abstract class AnimateMind implements AnimateMindInterface {

	private final BodyMindInterface body;
	private final EvaluatorManagerMindInterface score;
	
	protected static final Logger log = MyLogger.getLog("Mind"); 
	
	public AnimateMind(BodyMindInterface body, EvaluatorManagerMindInterface score) {
		this.body = body;
		this.score = score;
	}
	
	@Override
	public void startCycle() {
		setup();
		this.executeMindCycle();
		endCycle();

	}
	
	private void setup() {

	}
	
	private void endCycle() {
		//Update score and evaluation
		//Get health
		this.score.updateHealth(this.body.getEntityIdentifier(), this.body.getBodyPerception().getCurrentHealth());
		
		//TEST pain
		BodyPerceptionInterface bodyInternal = this.body.getBodyPerception();
		if (bodyInternal.getPainOrPleasure()<0) {
			log.debug("Pain is felt {}", bodyInternal.getPainOrPleasure());
		} else {
			log.debug("Pleasure is felt {}", bodyInternal.getPainOrPleasure());
		}
		
		score.updateEvaluation(this.body.getEntityIdentifier(), this.body.getPerception(), this.body.getBodyPerception(), this.body.getCurrentAction());
	}
	
	protected ArrayList<ExternalPerceptionInterface> getExternalPerception() {
		return this.body.getPerception();
	}
	
	protected BodyPerceptionInterface getBodyPerception() {
		return this.body.getBodyPerception();
	}
	
	protected void setAction(String action) {
		try {
			this.body.executeAction(action);
		} catch (Exception e) {
			log.error("Cannot set action {}. The simulator does not allow it", action);
		}
	}
	
	protected abstract void executeMindCycle();

}
