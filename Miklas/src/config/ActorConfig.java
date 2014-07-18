package config;

import java.util.HashMap;

/**
 * Add all actors here
 * 
 * @author wendt
 *
 */
public class ActorConfig {

	private final String moActorName;
	private final String moIconGraphicAddress;
	private final boolean mbRotateGraphicWithDirection;
	private final double initRotation;
	private final boolean mbUseMultipleGraphicIcons;
	private final int numberOfGraphicIconsUsedForInterval;
	private final int totalNumberOfIcons;
	private final int mnGraphicStep;
	private final char worldMapChar;
	private final boolean evaluateActor;
	
	private final BodyTypeConfig moBodyTypeConfig;
	
	private HashMap<String, String> moEventSound = new HashMap<String, String>();
	private HashMap<String, Integer> moEventGraphic = new HashMap<String, Integer>();
	
	
	public ActorConfig(String moActorName, BodyTypeConfig moBodyTypeConfig, String moIconGraphicAddress, boolean mbRotateGraphicWithDirection, double initRotation, int numberOfGraphicIconsUsedForInterval, int totalNumberOfIcons, int mnGraphicStep, char worldMapChar, boolean evaluateActor) {
		super();
		this.moActorName = moActorName;
		this.moBodyTypeConfig= moBodyTypeConfig;
		this.moIconGraphicAddress = moIconGraphicAddress;
		this.mbRotateGraphicWithDirection = mbRotateGraphicWithDirection;
		this.initRotation = initRotation;
		this.totalNumberOfIcons = totalNumberOfIcons;
		if (numberOfGraphicIconsUsedForInterval>1) {
			this.mbUseMultipleGraphicIcons = true;
		} else {
			this.mbUseMultipleGraphicIcons = true;
		}
		
		this.numberOfGraphicIconsUsedForInterval = numberOfGraphicIconsUsedForInterval;
		this.mnGraphicStep = mnGraphicStep;
		this.worldMapChar = worldMapChar;
		this.evaluateActor = evaluateActor;
	}

	public String getActorName() {
		return moActorName;
	}

	public BodyTypeConfig getBodyTypeConfig() {
		return moBodyTypeConfig;
	}

	public String getIconGraphicAddress() {
		return moIconGraphicAddress;
	}

	public boolean isRotateGraphicWithDirection() {
		return mbRotateGraphicWithDirection;
	}
	
	public double getInitRotation() {
		return initRotation;
	}

	public boolean isUseMultipleGraphicIcons() {
		return mbUseMultipleGraphicIcons;
	}

	public int getNumberOfGraphicIconsUsedForInterval() {
		return numberOfGraphicIconsUsedForInterval;
	}

	public int getTotalNumberOfIcons() {
		return totalNumberOfIcons;
	}

	public int getMnGraphicStep() {
		return mnGraphicStep;
	}
	
	public void addEventSound(String poEventName, String poEventSoundPath) {
		this.moEventSound.put(poEventName, poEventSoundPath);
	}
	
	public String getEventSound(String poEventName) {
		return this.moEventSound.get(poEventName);
	}
	
	public void addAllEventSounds(HashMap<String, String> eventSounds) {
		this.moEventSound.putAll(eventSounds);
	}
	
	public void addEventGraphic(String poEventName, int graphicNumber) {
		this.moEventGraphic.put(poEventName, graphicNumber);
	}
	
	public int getEventGraphic(String poEventName) {
		Object graphicnumber = this.moEventGraphic.get(poEventName);
		
		int result=0;
		
		if (graphicnumber!=null) {
			result=(int)graphicnumber;
		}
		
		return result;
	}
	
	public void addAllEventGraphics(HashMap<String, Integer> eventGraphics) {
		this.moEventGraphic.putAll(eventGraphics);
	}

	public char getWorldMapChar() {
		return worldMapChar;
	}

	@Override
	public String toString() {
		return "ActorConfig [moActorName=" + moActorName
				+ ", moIconGraphicAddress=" + moIconGraphicAddress
				+ ", mbRotateGraphicWithDirection="
				+ mbRotateGraphicWithDirection + ", mbUseMultipleGraphicIcons="
				+ mbUseMultipleGraphicIcons + ", mnNumberOfGraphicIconsUsed="
				+ numberOfGraphicIconsUsedForInterval + ", mnGraphicStep="
				+ mnGraphicStep + ", worldMapChar=" + worldMapChar
				+ ", moBodyTypeConfig=" + moBodyTypeConfig + ", moEventSound="
				+ moEventSound + "]";
	}

	public boolean isEvaluateActor() {
		return evaluateActor;
	}

}
