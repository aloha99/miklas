package evaluator;

import java.util.ArrayList;

import entity.mind.BodyPerception;
import entity.mind.ExternalPerception;

public class EntityEvaluation {
	
	private final String agentIdentifier;
	private int health;
	private int score=0;
	private int positiveActions=0;
	private int negativeActions=0;
	private int neutralActions=0;
	
	private boolean isScoreUpdateSet = false;
	
	private ArrayList<ExternalPerception> previousPerceptionValues = new ArrayList<ExternalPerception>();
	private BodyPerception bodyValues = null;
	
	public EntityEvaluation(String agentIdentifier, int health) {
		this.agentIdentifier = agentIdentifier;
		this.health=health;
	}
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getPositiveActions() {
		return positiveActions;
	}
	public void setPositiveActions(int positiveActions) {
		this.positiveActions = positiveActions;
	}
	public int getNegativeActions() {
		return negativeActions;
	}
	public void setNegativeActions(int negativeActions) {
		this.negativeActions = negativeActions;
	}
	public int getNeutralActions() {
		return neutralActions;
	}
	public void setNeutralActions(int neutralActions) {
		this.neutralActions = neutralActions;
	}

	public String getEntityIdentifier() {
		return agentIdentifier;
	}
	
	public void updateScore(int pnAdditionalScore) {
		int nScore = this.getScore();
		int nNewScore = nScore + pnAdditionalScore;
		this.setScore(nNewScore);
		
		updateActions(pnAdditionalScore);
	}
	
	private void updateActions(int scoreChange) {
		if (scoreChange>0) {
			this.setPositiveActions(this.getPositiveActions() + 1);
			isScoreUpdateSet = true;
		} else if (scoreChange<0) {
			this.setNegativeActions(this.getNegativeActions() + 1);
			isScoreUpdateSet = true;
		}
	}
	
	public void updateActions(ArrayList<ExternalPerception> currentPerceptionValues, BodyPerception bodyValues, String currentAction) {
		//Put an eventhandler here for evaluation
		
		
	}
	
	public void updateNeutralActions() {
		if (isScoreUpdateSet==false) {
			this.setNeutralActions(this.getNeutralActions() + 1);
		}
		
		isScoreUpdateSet=false;
			
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("agentName=").append(agentIdentifier)
				.append(", health=").append(health).append(", score=")
				.append(score).append(", positiveActions=")
				.append(positiveActions).append(", negativeActions=")
				.append(negativeActions).append(", neutralActions=")
				.append(neutralActions);
		return builder.toString();
	}
	
}
