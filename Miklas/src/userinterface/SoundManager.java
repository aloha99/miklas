package userinterface;

import java.util.HashMap;

import logger.MyLogger;

import org.slf4j.Logger;

import ch.aplu.jgamegrid.GameGrid;

/**
 * For every player and Event, there is a sound. For each executed event, the correct sound is searched and played
 * 
 * @author wendt
 *
 */
public class SoundManager implements SoundManagerInterface {
	private static final Logger	log	= MyLogger.getLog("Sound");
	private final GameGrid moGameEngine;
	private HashMap<String, HashMap<String, String>> moEventToSoundList = new HashMap<String, HashMap<String, String>>();
	
	public SoundManager(GameGrid moGameEngine) {
		this.moGameEngine = moGameEngine;
	}
	
	@Override
	public void playSound(String poEntityIdentifier, String poEventName) {
		String oSoundAddress = moEventToSoundList.get(poEntityIdentifier).get(poEventName);
		if (oSoundAddress != null) {
			moGameEngine.playSound(oSoundAddress);
			log.debug("Play sound for entity {}, for event {} with address {}", poEntityIdentifier, poEventName, oSoundAddress);
		} else {
			log.debug("No sound for event {} for player {}", poEventName, poEntityIdentifier);
		}
		
	}
	
	public void registerSound(String poEntityIdentifier, String poEventName, String poSoundAddress) {
		
		
		HashMap<String, String> oEventList = moEventToSoundList.get(poEntityIdentifier);
		if (oEventList==null) {
			oEventList = new HashMap<String, String>();
		}
		
		oEventList.put(poEventName, poSoundAddress);
		
		this.moEventToSoundList.put(poEntityIdentifier, oEventList);
		log.debug("Sound registered: {}, {}, {}", poEntityIdentifier, poEventName, poSoundAddress);
		
	}

	@Override
	public void registerEntity(String poEntityIdentifier) throws Exception {
		if (this.moEventToSoundList.get(poEntityIdentifier)==null) {
			this.moEventToSoundList.put(poEntityIdentifier, new HashMap<String, String>());
		} 
	}

	@Override
	public String toString() {
		return "SoundManager [moGameEngine=" + moGameEngine
				+ ", moEventToSoundList=" + moEventToSoundList + "]";
	}
	
	
}
