package gameengine;

import java.awt.Color;

import logger.MyLogger;

import org.slf4j.Logger;

import userinterface.SoundManagerInterface;
import userinterface.VisualizationEvaluationInterface;
import config.ActorConfig;
import config.ConfigLoader;
import config.EventConfig;
import config.WorldCreatorInterface;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import entity.Entity;
import entity.EntityFactory;
import evaluator.EvaluatorManagerMindInterface;


public class GameEngine implements GameEngineInterface {
	private final WorldCreatorInterface worldConfig;
	private final ConfigLoader conf;
	private final GameGrid gameGrid;
	private VisualizationEvaluationInterface moVis;
	private SoundManagerInterface soundManager;
	private EvaluatorManagerMindInterface scoreManager;
	
	private EntityFactory entityFactory;
	
	private static int entityCount = 0;
	
	
	
	private static final Logger	log	= MyLogger.getLog("Miklas");
	
	//GGBackground moBackGround;
	
	public GameEngine(WorldCreatorInterface poWorldConfig, ConfigLoader conf) {
		gameGrid = new GameGrid();
		worldConfig = poWorldConfig;
		this.conf= conf;
		
//		try {
//			init();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public GameGrid getGameGrid() {
		// TODO Auto-generated method stub
		return gameGrid;
	}
	
	public void init() {
		//Create world
		try {
			drawGrid();
		} catch (Exception e) {
			log.error("Cannot draw grid");
			System.exit(-1);
		}
		
		//Register all non actor specific content in the score manager
		registerEventsScore();
	}

	private void drawGrid() throws Exception  {
		gameGrid.setNbVertCells(worldConfig.getYDimension());
		gameGrid.setNbHorzCells(worldConfig.getXDimension());
	}
	
	private void registerEventsScore() {
		//Register event in scorehandler
		for (EventConfig eventConfig : this.conf.getAvailableEvents().values()) {
			try {
				this.scoreManager.registerEvent(eventConfig.getEventName(), eventConfig.getScoreChange());
			} catch (Exception e) {
				log.error("Cannot register event {}", e);
			}

		}
		
	}
	
	public void addActorsToWorld() throws Exception {
		int numberOfLayers = this.worldConfig.getLayerCount();
		for (int l=0; l < numberOfLayers; l++) {
			for (int y = 0; y < gameGrid.nbVertCells; y++) {
				for (int x = 0; x < gameGrid.nbHorzCells; x++) {
					Location location = new Location(x, y);
		    		  
		    		  char mapValue = worldConfig.getCellValue(location, l);
		    		  
		    		  ActorConfig actorConfig = conf.getAvailableActors().get(mapValue);
		    		  if (actorConfig==null) {
		    			  if (mapValue!='_') {
		    				  log.warn("There is no actor for position ({}, {}, {}) with map value {}", x, y, l, mapValue);
		    			  }
		    		  } else {
		    			  Entity oNewEntity = this.entityFactory.newEntity(actorConfig, l, GameEngine.getEntityCount());
		    			  GameEngine.incrementEntityCount();
		    	    	  
			    		  gameGrid.addActor(oNewEntity, location);
			    		  oNewEntity.setOnTop();
			    		  oNewEntity.setDirection(actorConfig.getInitRotation());
			    		  gameGrid.refresh();
				 	      log.debug("Add actor {} from {}", oNewEntity, mapValue);  
		    		  }
				}
			}
	    }
	}

	@Override
	public void setVisualization(VisualizationEvaluationInterface poVis) {
		moVis = poVis;
		
	}

	@Override
	public void setSoundManager(SoundManagerInterface sound) {
		this.soundManager = sound;
		
	}

	@Override
	public void initializeEntityFactory() {
		entityFactory = new EntityFactory(this.getGameGrid(), this.scoreManager, this.soundManager, this.moVis);
		
	}

	@Override
	public void setScoreManager(EvaluatorManagerMindInterface score) {
		this.scoreManager = score;
	}

	private static int getEntityCount() {
		return entityCount;
	}

	private static void incrementEntityCount() {
		GameEngine.entityCount++;
	}

}
