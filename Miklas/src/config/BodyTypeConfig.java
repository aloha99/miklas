package config;

import java.util.ArrayList;

/**
 * 
 * #BodyTypes
	bodytype.0.health=100
	bodytype.0.bodytype=ANIMATE
	bodytype.0.typename=HUMANPLAYER
	bodytype.0.effectonaction=
	bodytype.0.effectonreaction=BUMPOBSTACLE
 * 
 * @author wendt
 *
 */
public class BodyTypeConfig {
	private final String bodyTypeName;
	private final MindConfig mind;
	private final ArrayList<EventConfig> eventOnActionName;
	private final ArrayList<EventConfig> eventOnReactionName;
	private final ArrayList<EventConfig> eventOnOwnActionName;
	private final ArrayList<EventConfig> eventOnBodyInternalsName;
	
	public BodyTypeConfig(String moBodyTypeName, MindConfig poMind, ArrayList<EventConfig> moEventOnActionName, ArrayList<EventConfig> moEventOnReactionName, ArrayList<EventConfig> eventOnOwnActionName, ArrayList<EventConfig> eventOnBodyInternalsName) {
		super();
		
		this.bodyTypeName = moBodyTypeName;
		this.eventOnActionName = moEventOnActionName;
		this.eventOnReactionName = moEventOnReactionName;
		this.eventOnOwnActionName = eventOnOwnActionName;
		this.eventOnBodyInternalsName = eventOnBodyInternalsName;
		this.mind = poMind;
	}

	public String getBodyTypeName() {
		return bodyTypeName;
	}

	public ArrayList<EventConfig> getEventOnActionNames() {
		return eventOnActionName;
	}

	public ArrayList<EventConfig> getEventOnReactionNames() {
		return eventOnReactionName;
	}
	
	public ArrayList<EventConfig> getEventOnOwnActionName() {
		return eventOnOwnActionName;
	}
	
	public ArrayList<EventConfig> getEventOnBodyInternalsName() {
		return eventOnBodyInternalsName;
	}
	
	public ArrayList<EventConfig> getAllEvents() {
		ArrayList<EventConfig> result = new ArrayList<EventConfig>();
		result.addAll(eventOnActionName);
		result.addAll(eventOnReactionName);
		result.addAll(this.eventOnOwnActionName);
		result.addAll(this.eventOnBodyInternalsName);
		return result;
	}
	
	public MindConfig getMind() {
		return mind;
	}

	@Override
	public String toString() {
		return "BodyTypeConfig [moBodyTypeName=" + bodyTypeName
				+ ", eventOnActionName=" + eventOnActionName
				+ ", eventOnOwnActions=" + eventOnOwnActionName
				+ ", eventOnReactionName=" + eventOnReactionName + "]";
	}



}
