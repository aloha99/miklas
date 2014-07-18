package miklas;

import java.io.IOException;

import javax.swing.SwingUtilities;

import logger.MyLogger;

import org.slf4j.Logger;

import propertyhandler.PropertyException;
import userinterface.MusicManager;
import userinterface.MusicManagerInterface;
import userinterface.SoundManagerInterface;
import userinterface.SoundManager;
import userinterface.Visualization;
import config.ConfigLoader;
import config.WorldCreator;
import evaluator.EvaluatorManager;
import evaluator.EvaluatorManagerMindInterface;
import gameengine.GameEngine;

public class Launcher {
	
	private static final Logger	log	= MyLogger.getLog("Miklas");
	
	
	public static void main(String[] args) {
		log.info("Start Miklas 1.0");
				
		//Load parameters from config file
		ConfigLoader conf = null;
		try {
			conf = new ConfigLoader();
		} catch (IOException | PropertyException e) {
			log.error("Could not load config. Exit program", e);
			System.exit(-1);
		}
		try {
			conf.loadGameParameter();
		} catch (Exception e) {
			log.error("Could not load config correctly", e.getMessage());
			System.exit(-1);
		}
		
		//Load config
		WorldCreator world = new WorldCreator(conf);	
		
		//Init visualization
		final Visualization oVis = new Visualization(conf.getVisualizationConfig());
		
	    final GameEngine gameEngine = new GameEngine(world, conf);
	    
		//Load score manager
		EvaluatorManagerMindInterface scoreManager = new EvaluatorManager(oVis);
	    
		//Load sound manager
		SoundManagerInterface soundManager = new SoundManager(gameEngine.getGameGrid());
		
		
		//Load music manager and play music
		MusicManagerInterface musicManager = new MusicManager();
		String relativeMusicPath = conf.getMusicConfig().getRelativMusicPath();
		if (relativeMusicPath.equals("")==false) {
			musicManager.playMusic(relativeMusicPath);
		}
		
		//Set sound manager in the gameengine
		gameEngine.setSoundManager(soundManager);
		gameEngine.setScoreManager(scoreManager);
		
		//As sson as everything is assigned, init the game engine
		gameEngine.init();
	    
	    //Start Visualization
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		//Visualization oVis = new Visualization();
	    		oVis.setGameEngine(gameEngine);
	    		oVis.init();
	    		oVis.setVisible(true);
	    	}
	    });
	}
}
