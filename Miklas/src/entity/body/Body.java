package entity.body;

import java.util.ArrayList;




import logger.MyLogger;

import org.apache.log4j.MDC;
import org.slf4j.Logger;

import ch.aplu.jgamegrid.Location;
import utils.LocationUtilities;
import entity.Entity;
import entity.EntityInterface;
import entity.mind.AnimateMindInterface;
import entity.mind.BodyPerception;
import entity.mind.BodyPerceptionInterface;
import entity.mind.ExternalPerceptionInterface;
import event.DataStructureContainer;
import event.Datapoint;
import event.EventHandlerInterface;
import event.EventVariables;

public class Body implements BodyInterface, BodyMindInterface {
	protected static final Logger log = MyLogger.getLog("Body");
	
	private final String moBodyTypeName;
	
	private final EventHandlerInterface eventHandlerForActions;
	private final EventHandlerInterface eventHandlerForReactions;
	private final EventHandlerInterface eventHandlerForMyActionExecutions;
	private final EventHandlerInterface eventHandlerForBodyInternals;
	
	private final DataStructureContainer sharedDataStructures = new DataStructureContainer();

	private final Entity moOwnerEntity;
	protected final BodyInteractionEngineInterface interactionEngine;
	protected final LocationUtilities locationUtils;
	
	//private boolean mbActionExecutable = true;
	
	private final String HEALTHNAME = "HEALTH";
	private final String MAXHEALTHNAME = "MAXHEALTH";
	private final String ISACTIONEXECUTABLE = "ISACTIONEXECUTABLE";
	
	private AnimateMindInterface generalMind=null;
	
	private int previousHealth;
	private String currentAction = "NONE";
	
	public Body(String poBodyType, Entity poOwnerEntity, EventHandlerInterface poEventHandlerForActions, EventHandlerInterface poEventHandlerForReactions, EventHandlerInterface eventHandlerForMyActionExecutions, EventHandlerInterface eventHandlerForBodyInternals) throws Exception {
		moBodyTypeName = poBodyType;
		moOwnerEntity = poOwnerEntity;
		//maxHealth = mnMaxHealth;
		//setHealth(maxHealth);
		
		//Create a new interaction
		this.interactionEngine = new BodyInteractionEngine();
		this.interactionEngine.init(this);
		
		//Set dependencies to location utils
		this.locationUtils = new LocationUtilities(this.interactionEngine);
		
		//Eventhandlers
		this.eventHandlerForActions = poEventHandlerForActions;
		this.eventHandlerForReactions = poEventHandlerForReactions;
		this.eventHandlerForMyActionExecutions = eventHandlerForMyActionExecutions;
		this.eventHandlerForBodyInternals = eventHandlerForBodyInternals;
	}
	
	@Override
	public void init() throws Exception {
		//Set operation modes
		this.eventHandlerForActions.init(false, true);	//Execute eventhandler only once
		this.eventHandlerForBodyInternals.init(true, true); //Execute eventhandler as long as events can be executed but exclude already executed events
		this.eventHandlerForMyActionExecutions.init(true, true); //Execute eventhandler as long as events can be executed but exclude already executed events
		this.eventHandlerForReactions.init(true, true); //Execute eventhandler as long as events can be executed but exclude already executed events
		
		//Set this body as permanent data structure to both event handlers
		this.eventHandlerForActions.setLocalPermanentDataStructure(new Datapoint<BodyInterface>(EventVariables.MYBODY.toString(), this));
		this.eventHandlerForReactions.setLocalPermanentDataStructure(new Datapoint<BodyInterface>(EventVariables.MYBODY.toString(), this));
		this.eventHandlerForReactions.setLocalPermanentDataStructure(new Datapoint<BodyInteractionEngineInterface>(EventVariables.INTERACTIONENGINE.toString(), this.interactionEngine));
		this.eventHandlerForMyActionExecutions.setLocalPermanentDataStructure(new Datapoint<BodyInterface>(EventVariables.MYBODY.toString(), this));
		this.eventHandlerForMyActionExecutions.setLocalPermanentDataStructure(new Datapoint<BodyInteractionEngineInterface>(EventVariables.INTERACTIONENGINE.toString(), this.interactionEngine));
		this.eventHandlerForBodyInternals.setLocalPermanentDataStructure(new Datapoint<BodyInterface>(EventVariables.MYBODY.toString(), this));
		
		//Init all permanent data structures
		this.eventHandlerForActions.initLocalPermanentDataStructuresInEvents();
		this.eventHandlerForMyActionExecutions.initLocalPermanentDataStructuresInEvents();
		this.eventHandlerForReactions.initLocalPermanentDataStructuresInEvents();
		this.eventHandlerForBodyInternals.initLocalPermanentDataStructuresInEvents();
		
		//Execute init events for the body - used to e.g. reduce init health of the agent. The agent executes the action init on itself
		try {
			eventHandlerForActions.setLocalTemporaryDataStructure(new Datapoint<String>(EventVariables.ACTIONOFCALLER.toString(), PredefinedBodyAction.INIT.toString()));
			eventHandlerForActions.setLocalTemporaryDataStructure(new Datapoint<EntityInterface>(EventVariables.ENTITYOFCALLER.toString(), this.moOwnerEntity));
			this.eventHandlerForActions.executeMatchingEvents();
		} catch (Exception e) {
			log.error("Cannot execute the internal body action INIT on the body at initilalization",e );
			throw e;
		}
		
		//Run eventinit

	}
	
	@Override
	public void executeEffectOfExternActionOnMyBody(Entity caller, String poAction) {
		//Get interaction area, get relative direction of caller entity
		Location callerLocation = this.locationUtils.getRelativeLocation(this.moOwnerEntity, caller);
		
		try {
			//Set all variables first, then execute matching events
			eventHandlerForActions.setLocalTemporaryDataStructure(new Datapoint<String>(EventVariables.ACTIONOFCALLER.toString(), poAction));
			eventHandlerForActions.setLocalTemporaryDataStructure(new Datapoint<EntityInterface>(EventVariables.ENTITYOFCALLER.toString(), caller));
			eventHandlerForActions.setLocalTemporaryDataStructure(new Datapoint<Location>(EventVariables.LOCATIONOFCALLER.toString(), callerLocation));
			
			//Execute matching events
			log.debug("{}> execute external action on this body", this.getOwnerEntity().getEntityIdentifier());
			eventHandlerForActions.executeMatchingEvents();
		} catch (Exception e) {
			log.error("Could not execute event", e);
		}
	}

	@Override
	public void executeEffectOfExternReactionMyOnBody(Entity reCaller, String poAction) {
		//Get interaction area, get relative direction of caller entity
		Location reCallerLocation = this.locationUtils.getRelativeLocation(this.moOwnerEntity, reCaller);
		
		try {
			//Set all variables first, then execute matching events
			eventHandlerForReactions.setLocalTemporaryDataStructure(new Datapoint<String>(EventVariables.ACTIONOFCALLER.toString(), poAction));
			eventHandlerForReactions.setLocalTemporaryDataStructure(new Datapoint<EntityInterface>(EventVariables.ENTITYOFCALLER.toString(), reCaller));
			eventHandlerForReactions.setLocalTemporaryDataStructure(new Datapoint<Location>(EventVariables.LOCATIONOFCALLER.toString(), reCallerLocation));
			
			//Execute matching events
			log.debug("{}>, execute reaction on this body", this.getOwnerEntity().getEntityIdentifier());
			eventHandlerForReactions.executeMatchingEvents();
		} catch (Exception e) {
			log.error("Cannot execute event", e);
		}
	}
	
	/**
	 * Create the effect on each entity in the field
	 * 
	 * @param poTargetLocation
	 * @param poAction
	 */
	public boolean executeActionOnEntityOnField(Location poTargetLocation, String poAction, boolean pbExecuteOnlyOnTopEntity) {
		ArrayList<EntityInterface> oEntityList = this.getBodyInteractionEngine().getEntityAtLocation(poTargetLocation);
		
		if (pbExecuteOnlyOnTopEntity == true) {
			EntityInterface topEntity = this.getBodyInteractionEngine().getTopEntity(oEntityList);
			if (topEntity!=null) {
				topEntity.actionOnBody(this.getOwnerEntity(), poAction);
			} else {
				log.debug("No entity on which the action in a top entity can be executed");
			}
			
		} else {
			for (EntityInterface oEntity : oEntityList) {
				oEntity.actionOnBody(this.getOwnerEntity(), poAction);
			}
		}
		
		log.info("Executed action={}", poAction);
		
		return false;
	}
	
	@Override
	public void executeAction(String action) throws Exception {
		try {
			//Set input action
			this.eventHandlerForMyActionExecutions.setLocalTemporaryDataStructure(new Datapoint<String>(EventVariables.MYACTION.toString(), action));
			
			//Start eventhandler for actions
			log.debug("{}> execute Action events", this.getOwnerEntity().getEntityIdentifier());
			this.eventHandlerForMyActionExecutions.executeMatchingEvents();
			
			//this.executeAction(poAction);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("Could not execute action " + action);
		}
	}

	@Override
	public void callBodyToAct() {
		MDC.put("id", this.getOwnerEntity().entitiyIdentifier);
		callSpecifiedBodyToAct();
		
		try {
			this.eventHandlerForBodyInternals.executeMatchingEvents();
		} catch (Exception e) {
			log.error("Cannot execute bodyinternal events", e);
		}
		
		//End cycle, clear temporary data
		//this.getSharedVariables().clearTemporaryData();
	}
	
	@Override
	public BodyInteractionEngineInterface getBodyInteractionEngine() {
		return this.interactionEngine;
	}

	public LocationUtilities getLocationUtils() {
		return locationUtils;
	}
	
	public void callSpecifiedBodyToAct() {
		setup();
		
		//Start mind cycle
		if (this.generalMind!=null) {
			this.generalMind.startCycle();
		}
		
		endCycle();
	}
	
	private void setup() {

	}
	
	private void endCycle() {
		previousHealth = this.getHealth();
		
		//Set current action as none as the agent has finished its action 
		this.currentAction = "NONE";
	}
	
	public void killActor() {
		if (this.generalMind!=null) {
			this.generalMind.killMind();
		}
		
		//Remove the actor
		this.getOwnerEntity().removeSelf();
	}
	
	public void setMind(AnimateMindInterface mind) {
		//Use the mind factory to create this mind
		generalMind = mind;
	}
	
	private int getHealth() {
		int health = 0;
		
		Datapoint<Integer> healthdp = new Datapoint<Integer>(HEALTHNAME);
		try {
			this.getSharedVariables().getData(healthdp);
			health=healthdp.getValue();
		} catch (Exception e) {
			log.error("Cannot get health for variable {}", HEALTHNAME);
		}
		
		return health;
	}
	
	private int getMaxHealth() {
		int health = 0;
		
		Datapoint<Integer> healthdp = new Datapoint<Integer>(MAXHEALTHNAME);
		try {
			this.getSharedVariables().getData(healthdp);
			health=healthdp.getValue();
		} catch (Exception e) {
			log.error("Cannot get health for variable {}", MAXHEALTHNAME);
		}
		
		return health;
	}
	
	public BodyPerceptionInterface getBodyPerception() {
		BodyPerception result = new BodyPerception(this.getMaxHealth(), this.getHealth(), this.getHealth()-this.previousHealth);
		
		return result;
	}
	
	public ArrayList<ExternalPerceptionInterface> getPerception() {
		//Get all entities and their positions with relative positions
		ArrayList<ExternalPerceptionInterface> result = locationUtils.getEntitiesForExternalPerception(this);
		
		return result;
		
	}
	
	/**
	 * Returns the value if an action can be executed or not. This value is consumed, i.e. if the value is called and deleteAfterValue is true. In that way, the value, which is set is
	 * only valid for one action execution
	 * 
	 * @param deleteAfterCheck
	 * @return
	 */
	protected boolean isActionExecutable(boolean deleteAfterCheck) {
		boolean isExecutable = true;
		
		Datapoint<Boolean> dp0 = new Datapoint<Boolean>(this.ISACTIONEXECUTABLE, isExecutable);
		try {
			//On the receival, delete the temporary datapoint as it is consumed
			this.getSharedVariables().getData(dp0, deleteAfterCheck, isExecutable);
			isExecutable = dp0.getValue();
		} catch (Exception e) {
			log.error("Cannot receive data for {} for datapoint {}", isExecutable, dp0, e);
		}
		
		return isExecutable;
	}

	public void setActionExecutable(boolean mbActionExecutable) {
		try {
			this.getSharedVariables().setTemporaryData(new Datapoint<Boolean>(ISACTIONEXECUTABLE, mbActionExecutable));
		} catch (Exception e) {
			log.error("Cannot set temporary datapoint for executable action with value {}", mbActionExecutable, e);
		}
		
		//this.mbActionExecutable = mbActionExecutable;
	}

	public Entity getOwnerEntity() {
		return moOwnerEntity;
	}
	
	public String getCurrentAction() {
		return this.currentAction;
	}

	public String getEntityIdentifier() {
		return this.getOwnerEntity().getEntityIdentifier();
	}
	
	public String getBodyType() {
		return moBodyTypeName;
	}
	
	@Override
	public DataStructureContainer getSharedVariables() {
		return sharedDataStructures;
	}

	@Override
	public String toString() {
		return "Body " + moBodyTypeName + ", Variables: " + this.getSharedVariables();
	}
	
}
