package gameengine;

import userinterface.SoundManagerInterface;
import userinterface.VisualizationEvaluationInterface;
import ch.aplu.jgamegrid.GameGrid;
import evaluator.EvaluatorManagerMindInterface;

public interface GameEngineInterface {
	public GameGrid getGameGrid();
	public void addActorsToWorld() throws Exception;
	public void setVisualization(VisualizationEvaluationInterface poVis);
	public void setSoundManager(SoundManagerInterface sound);
	public void setScoreManager(EvaluatorManagerMindInterface score);
	public void initializeEntityFactory();
	public void init();
}
