package event;

import java.util.ArrayList;
import logger.MyLogger;

import org.slf4j.Logger;

import userinterface.SoundManagerInterface;
import userinterface.VisualizationEvaluationInterface;
import evaluator.EvaluatorManagerMindInterface;

public class EventHandler implements EventHandlerInterface {
	
	private final SoundManagerInterface moSoundManager;
	private final VisualizationEvaluationInterface visualization;
	private final EvaluatorManagerMindInterface moScoreManager;
	
	private DataStructureContainer dataStructureContainer = new DataStructureContainer();
	
	private final ArrayList<EventInterface> moEventList;
	
	private boolean isContinousExecution = true;
	private boolean excludeAlreadyFinishedEvents = true;
	
	protected static final Logger log = MyLogger.getLog("Event");

	public EventHandler(SoundManagerInterface poSoundManager, EvaluatorManagerMindInterface poScoreManager, VisualizationEvaluationInterface visualization) {
		moSoundManager = poSoundManager;
		this.visualization = visualization;
		moScoreManager = poScoreManager;
		moEventList = new ArrayList<EventInterface>();
	}
	
	@Override
	public void init(boolean setContinousOperation, boolean setExcludeAlreadyExecutedEvents) {
		this.isContinousExecution = setContinousOperation;
		this.excludeAlreadyFinishedEvents = setExcludeAlreadyExecutedEvents;
		
	}
	
	@Override
	public void executeMatchingEvents() throws Exception {
		//All variables shall already have been assigned

		ArrayList<String> executedEvents = new ArrayList<String>();
		//Execute all events at least once and if at least one event was executed, then execute once again until no events are executed more
		boolean eventExecuted = false;
		do {
			//Check if at least one event has been executed
			eventExecuted = false;
			//Execute matching events
			for (EventInterface oE : moEventList) {
				try {
					if (excludeAlreadyFinishedEvents==false || (excludeAlreadyFinishedEvents==true && executedEvents.contains(oE.getEventName())==false)) {
						boolean isExecuted = oE.runEvent();
						if (isExecuted==true) {
							//log.debug("Test events for input {}", this.dataStructureContainer);
							eventExecuted = true;
							executedEvents.add(oE.getEventName());
							log.debug("Event completed {}", oE.getEventName());
						}
					}
				} catch (Exception e) {
					log.error("Event could not be executed {}", oE, e);
				}
			}
			
			//Check if only one execution shall be done. If yes, then break
			if (isContinousExecution==false) {
				break;
			}
		} while (eventExecuted==true);
		
		
		//Clear all temporary data in order not to use it again
		this.dataStructureContainer.clearTemporaryData();
	}
	
	@Override
	public void registerEvent(EventInterface poEvent) {
		poEvent.setEventHandler(this);
		moEventList.add(poEvent);
	}
	
	@Override
	public void playSound(String entityIdentifier, String poEventName) {
		this.moSoundManager.playSound(entityIdentifier, poEventName);
		
	}
	
	@Override
	public void setGraphic(String entityIdentifier, String poEventName) {
		this.visualization.updateEntityIcon(entityIdentifier, poEventName);
	}

	@Override
	public void updateScore(String entityIdentifier, String poEventName) {
		this.moScoreManager.updateScoreFromEvent(entityIdentifier, poEventName);
		
	}

	@Override
	public String toString() {
		return "Events: " + moEventList;
	}

	@Override
	public void setLocalPermanentDataStructure(Datapoint<?> dataStructure) {
		this.dataStructureContainer.setPermanentData(dataStructure);
		
	}

	@Override
	public <T extends Object> boolean getLocalDataStructureFromContainer(Datapoint<T> value) throws Exception {
		boolean result = this.dataStructureContainer.getData(value);
		return result;
	}

	@Override
	public void setLocalTemporaryDataStructure(Datapoint<?> dataStructure) throws Exception {
		try {
			this.dataStructureContainer.setTemporaryData(dataStructure);
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Override
	public void initLocalPermanentDataStructuresInEvents() {
		for (EventInterface event : this.moEventList) {
			event.initPermanentDataStructures();
		}
		
	}




	
}
