package userinterface;

import entity.Entity;
import evaluator.EntityEvaluation;
import gameengine.GameEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import logger.MyLogger;

import org.slf4j.Logger;

import config.VisualizationConfig;

public class Visualization extends JFrame implements VisualizationEvaluationInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameEngine gameEngine;
	private final VisualizationConfig visConfig;
	private HashMap<String, GraphicSetting> eventToGraphicList = new HashMap<String, GraphicSetting>();
	
	private ArrayList<JTextField> moTextFields = new ArrayList<JTextField>();
	
	private static final Logger	log	= MyLogger.getLog("Visualization");
	
	public Visualization(VisualizationConfig visConfig) {
		this.visConfig = visConfig;
	}
	
	public void setGameEngine(GameEngine poGameEngine) {
		gameEngine = poGameEngine;
	}
	
	public void init() {
		//Set cell size
		gameEngine.getGameGrid().setCellSize(this.visConfig.getCellSize());
		//Set grid color
		gameEngine.getGameGrid().setSimulationPeriod(this.visConfig.getSimulationPeriod());
		
		//Set BG Imagepath
		if (visConfig.getBgImagePath().equals("")==false) {
			gameEngine.getGameGrid().setBgImagePath(visConfig.getBgImagePath());
		}
		
		//Set if a grid shall be shown
		if (visConfig.isShowGrid()==true) {
			gameEngine.getGameGrid().setGridColor(Color.red);
		}
	    
	    //Set terminat condition
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setResizable(false);
	    
	    Container oGraphicsContainer = getContentPane();
	    oGraphicsContainer.setLayout(new BorderLayout());
	    
	    oGraphicsContainer.add(gameEngine.getGameGrid(), BorderLayout.NORTH);
	    //oGraphicsContainer.add(test, BorderLayout.NORTH);
	    
	    
	    moTextFields.add(new JTextField("Punkte für Mia: "));
	    //JTextField f = new JTextField("Punkte für Mia: ");
	    oGraphicsContainer.add(moTextFields.get(0), BorderLayout.CENTER);
	    
	    
	    moTextFields.add(new JTextField("Punkte für Niklas: "));
	    //JTextField f2 = new JTextField("Punkte für Niklas: ");
	    oGraphicsContainer.add(moTextFields.get(1), BorderLayout.SOUTH);
	    
	    pack();  // Must be called before actors are added!
	    
	    //Set visualization
	    gameEngine.setVisualization(this);
	    
	    //Init entity factory
	    gameEngine.initializeEntityFactory();
	    
	    //Add actors
	    try {
			gameEngine.addActorsToWorld();
		} catch (Exception e) {
			log.error("Cannot add actors to world", e);
			System.exit(-1);
		}
	    
	    gameEngine.getGameGrid().doRun();
	    
	    this.setVisible(true);
	}


	@Override
	public void updateStats(final String poEntityName, final EntityEvaluation evaluation) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int choice = 0;
				if (poEntityName.equals("Mia")) {
					choice=0;
				} else if (poEntityName.equals("Niklas")) {
					choice=1;
				}
				
				log.debug(evaluation.toString());
				
				moTextFields.get(choice).setText("Name: " + poEntityName
						+ "; Gesundheit: " + evaluation.getHealth() 
						+ "; Punkte: " + evaluation.getScore()
						+ "; Belohnungen: " + evaluation.getPositiveActions()
						+ "; Bestrafungen: " + evaluation.getNegativeActions()
						+ "; Neutral: " + evaluation.getNeutralActions());
			}
		});
		
	}

	@Override
	public synchronized void registerEntity(Entity entity, int numberOfIconsContinousLoop, int iconChangeInterval, int totalNumberOfIcons) {
		eventToGraphicList.put(entity.getEntityIdentifier(), new GraphicSetting(entity, numberOfIconsContinousLoop, iconChangeInterval, totalNumberOfIcons));
		
	}

	@Override
	public synchronized void updateEntityIcon(String entityIdentifier) {
		GraphicSetting graphic = this.eventToGraphicList.get(entityIdentifier);
		if (graphic!=null) {
			graphic.updateRotatingGraphic();
		}
		
	}

	@Override
	public synchronized void updateEntityIcon(String entityIdentifier, String eventName) {
		GraphicSetting graphic = this.eventToGraphicList.get(entityIdentifier);
		if (graphic!=null) {
			try {
				graphic.activateEventGraphic(eventName);
			} catch (Exception e) {
				log.error("Could not activate graphic for {}, entity {}", eventName, entityIdentifier);
			}
		} else {
			log.error("Could not find entity {} in the list for visualization", entityIdentifier);
			throw new NullPointerException("Could not find entity" + entityIdentifier);
		}
		
	}

	@Override
	public void registerEventGraphic(String entityIdentifier, String poEventName, int graphicNumber, boolean isPermanentGraphicChange) throws Exception {
		GraphicSetting graphic = this.eventToGraphicList.get(entityIdentifier);
		if (graphic!=null) {
			try {
				graphic.registerEventGraphic(poEventName, graphicNumber, isPermanentGraphicChange);
			} catch (Exception e) {
				log.error("Could not register event graphic for {} of entity {}", poEventName, entityIdentifier, e);
				throw new Exception(e.getMessage());
			}
		} else {
			log.error("Could not find entity {} in the list for visualization", entityIdentifier);
			throw new NullPointerException("Could not find entity" + entityIdentifier);
		}
		
	}

	

}
