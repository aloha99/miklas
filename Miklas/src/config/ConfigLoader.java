package config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import logger.MyLogger;

import org.slf4j.Logger;

import propertyhandler.PropertyException;
import propertyhandler.PropertyHandler;

public class ConfigLoader {
	private static final String GAMEINIPATH = "conf/game.ini";
	private static final String MODINIPATHIDENTIFIER = "modfilepath";
	
	private static final String EVENTPREFIX = "event";
	private static final String BODYTYPEPREFIX = "bodytype";
	private static final String ACTORPREFIX = "actor";
	private static final String WORLDPREFIX = "world";
	private static final String MINDPREFIX = "mind";
	private static final String CONDITIONPREFIX = "condition";
	
	private static final String BODYTYPETYPENAMESUFFIX = "typename";
	private static final String BODYTYPEEFFECTONACTIONSUFFIX = "effectonaction";
	private static final String BODYTYPEEFFECTONREACTIONSUFFIX = "effectonreaction";
	private static final String BODYTYPEEFFECTOFOWNACTIONSUFFIX = "possibleaction";
	private static final String BODYTYPEEFFECTOFBODYINTERNALEVENTSSUFFIX = "bodyinternalevents";
	private static final String BODYTYPEMINDSUFFIX = "mind";
	
	private static final String CONDITIONNAMESUFFIX = "name";
	private static final String CONDITIONCLASSNAMESUFFIX = "classname";
	private static final String CONDITIONCUSTOMSUFFIX = "custom";
	
	private static final String EVENTNAMESUFFIX = "eventname";
	private static final String EVENTCLASSNAMESUFFIX = "classname";
	private static final String EVENTCONDITIONSUFFIX = "condition";
	private static final String EVENTCUSTOMSUFFIX = "custom";
	private static final String EVENTSCORECHANGESUFFIX = "scorechange";
	private static final String EVENTPERMANENTGRAPHICCHANGESUFFIX = "permanentgraphic";
	
	private static final String ACTORACTORNAMESUFFIX = "actorname";
	private static final String ACTORBODYTYPENAMESUFFIX = "bodytypename";
	private static final String ACTORICONGRAPHICPATHSUFFIX  = "icongraphicpath";
	private static final String ACTORROTATEGRAPHICWITHDIRECTIONSUFFIX = "rotategraphicwithdirection";
	private static final String ACTORINITROTATIONSUFFIX = "initrotation";
	private static final String ACTORNUMBEROFICONSFORINTERVALSUFFIX = "numberoficonsforinterval";
	private static final String ACTORTOTALNUMBEROFICONSSUFFIX = "totalnumberoficons";
	private static final String ACTORINTERVALFORGRAPHICCHANGE = "intervalforgraphicchange";
	private static final String ACTORWORLDMAPCHARSUFFIX = "worldmapchar";
	private static final String ACTOREVALUATEACTORSUFFIX = "evaluateactor";
	
	private static final String ACTOREVENTASSIGNMENT = "event";
	private static final String ACTOREVENTASSIGNMENTSOUNDSUFFIX = "sound";
	private static final String ACTOREVENTASSIGNMENTGRAPHICSUFFIX = "graphic";
	
	private static final String WORLDHORIZONTALCELLSSUFFIX = "horizontalcells";
	private static final String WORLDVERTICALCELLSSUFFIX = "verticalcells";
	private static final String WORLDLAYERCOUNTSUFFIX = "layercount";
	private static final String WORLDLAYERSUFFIX = "layer";
	
	private static final String MINDNAMESUFFIX = "mindname";
	private static final String MINDCLASSSUFFIX = "mindclass";
	private static final String MINDTYPESUFFIX = "mindtype";
	
	private static final String VISUALIZATIONPREFIX = "visualization";
	private static final String VISUALIZATIONCELLSIZESUFFIX = "cellsize";
	private static final String VISUALIZATIONSIMPERIODSUFFIX = "simulationperiod";
	private static final String VISUALIZATIONBGIMAGEPATHSUFFIX = "bgimagepath";
	private static final String VISUALIZATIONSHOWGRIDSUFFIX = "showgrid";
	
	private static final String MUSICPREFIX = "music";
	private static final String MUSICPATHSUFFIX = "backgroundmusic";
	
	protected static final Logger log = MyLogger.getLog("Config"); 
	
	private final String modIniPath;
	
	private PropertyHandler phGameIni = new PropertyHandler();
	private PropertyHandler phModIni = new PropertyHandler();
	
	private HashMap<String, EmptyConditionConfig> availableConditions = new HashMap<String, EmptyConditionConfig>();
	private HashMap<String, EventConfig> availableEvents = new HashMap<String, EventConfig>();
	private HashMap<String, BodyTypeConfig> availableBodyTypes = new HashMap<String, BodyTypeConfig>();
	private HashMap<Character, ActorConfig> availableActors = new HashMap<Character, ActorConfig>();
	private WorldMapConfig worldMapConfig = null;
	private VisualizationConfig visualizationConfig = null;
	private HashMap<String, MindConfig> availableMinds = new HashMap<String, MindConfig>();
	private MusicConfig musicConfig = null;
	
	public ConfigLoader() throws IOException, PropertyException {
		modIniPath = loadGameConfig();
		log.info("Config file {} will be used", modIniPath);
		phModIni.load(modIniPath);
		if (phModIni.getSize()==0) {
			try {
				throw new Exception("Nothing has been loaded for path " + modIniPath);
			} catch (Exception e) {
				log.error("", e);
				System.exit(-1);
			}
		}
		log.debug("Config file {} successfully loaded", modIniPath);
	}
	
	private String loadGameConfig() throws IOException, PropertyException {
		phGameIni.load(GAMEINIPATH);
		String modIniPath = phGameIni.getString(MODINIPATHIDENTIFIER);
		return modIniPath; 
	}
	
	public void loadGameParameter() throws Exception {
		try {
			//Load map data
			worldMapConfig = loadWorldMapConfig();
			
			//Load visualization data
			visualizationConfig = loadVisualizationConfig();
			
			//Load music config parameters
			this.musicConfig = loadMusicConfig();
			
			//Load available conditions for use in events
			availableConditions = loadConditionConfigs();
			
			//Load all possible events
			availableEvents = this.loadEventConfigs();
			
			//Load all minds
			availableMinds = loadMindConfig();
			
			//Load all bodytypes
			availableBodyTypes = this.loadBodyTypeConfigs(availableEvents);
			
			//Load all actors
			availableActors = this.loadActorConfigs(availableBodyTypes);
			
		} catch (Exception e) {
			log.error("Could not load all parameters.",e);
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * load eventconfigs
	 * 
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, EmptyConditionConfig> loadConditionConfigs() throws Exception {
		HashMap<String, EmptyConditionConfig> res = new HashMap<String, EmptyConditionConfig>();
		
		ArrayList<String> conditionList = phModIni.getUnknownAddressPart(CONDITIONPREFIX, ".");
		for (String condition : conditionList) {
			EmptyConditionConfig conditionConfig = loadConditionConfig(condition);
			log.debug("Load options for condition {}", conditionConfig);
			res.put(conditionConfig.getName(), conditionConfig);
		}
		
		return res;
	}
	
	/**
	 * load a single condition config
	 * 
	 * @param eventNumber
	 * @return
	 * @throws Exception
	 */
	private EmptyConditionConfig loadConditionConfig(String conditionID) throws Exception {
		String conditionName = phModIni.getString(CONDITIONPREFIX + "." + conditionID + "." + CONDITIONNAMESUFFIX);
		log.trace("Load condition: {}", conditionName);
		String className = phModIni.getString(CONDITIONPREFIX + "." + conditionID + "." + CONDITIONCLASSNAMESUFFIX);
		log.trace("Classname: {}", className);
		HashMap<String, ArrayList<String>> customParameter = loadCustomParameter(CONDITIONPREFIX, conditionID, CONDITIONCUSTOMSUFFIX);
		log.trace("customParameter: {}", customParameter);
		
		EmptyConditionConfig conditionConfig = new EmptyConditionConfig(conditionName, className, customParameter);
		
		return conditionConfig;
	}
	
	/**
	 * load a single eventconfig
	 * 
	 * @param eventNumber
	 * @return
	 * @throws Exception
	 */
	private EventConfig loadEventConfig(String eventNumber, HashMap<String, EmptyConditionConfig> availableConditions) throws Exception {
		
		String eventName = phModIni.getString(EVENTPREFIX + "." + eventNumber + "." + EVENTNAMESUFFIX);
		log.trace("Load event: {}", eventName);
		String className = phModIni.getString(EVENTPREFIX + "." + eventNumber + "." + EVENTCLASSNAMESUFFIX);
		log.trace("Classname: {}", className);
		HashMap<String, HashMap<String, ArrayList<String>>> triggerConditionProperties = this.loadEventConditions(eventNumber, this.availableConditions);
		log.trace("triggerConditionProperties: {}", triggerConditionProperties);
		int scoreChange = phModIni.getInt(EVENTPREFIX + "." + eventNumber + "." + EVENTSCORECHANGESUFFIX);
		
		boolean permanentGraphicChange = phModIni.getBoolean(EVENTPREFIX + "." + eventNumber + "." + EVENTPERMANENTGRAPHICCHANGESUFFIX);
		
		HashMap<String, ArrayList<String>> customEventParameter = loadCustomParameter(EVENTPREFIX, eventNumber, EVENTCUSTOMSUFFIX);
		
		EventConfig eventConfig = new EventConfig(eventName, className, triggerConditionProperties, availableConditions, scoreChange, permanentGraphicChange, customEventParameter);
		
		return eventConfig;
	}
	
	private HashMap<String, HashMap<String, ArrayList<String>>> loadEventConditions(String eventNumber, HashMap<String, EmptyConditionConfig> availableConditions) throws PropertyException {
		HashMap<String, HashMap<String, ArrayList<String>>> result = new HashMap<String, HashMap<String, ArrayList<String>>>();
		
		String eventPath = EVENTPREFIX + "." +  eventNumber + "." + EVENTCONDITIONSUFFIX;
		
		//Get available events
		ArrayList<String> conditionsInEvent = phModIni.getUnknownAddressPart(eventPath, ".");
		
		for (String availableCondition : conditionsInEvent) {
			result.put(availableCondition, new HashMap<String, ArrayList<String>>());
			
			//Create prefix for condition
			String conditionPrefix = eventPath + "." + availableCondition;
			
			//Get all properties for the event
			ArrayList<String> conditionProperties = phModIni.getUnknownAddressPart(conditionPrefix, ".");
			
			for (String conditionProperty : conditionProperties) {
				String conditionPropertyPath = conditionPrefix + "." + conditionProperty;
				try {
					String[] value = phModIni.getString(conditionPropertyPath).split(",");
					result.get(availableCondition).put(conditionProperty, new ArrayList<String>(Arrays.asList(value)));
				} catch (PropertyException e) {
					log.error("Cannot read condition property of the event {} for condition {}, propertypath {}", eventNumber, availableCondition, conditionPropertyPath);
					throw e;
				}
				
			}
		}
		
		return result;
	}
	
	private HashMap<String, ArrayList<String>> loadCustomParameter(String prefix, String eventName, String suffix) throws PropertyException {
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		
		//String eventCustomPath = EVENTPREFIX + "." + eventName + "." + EVENTCUSTOMSUFFIX;
		String eventCustomPath = prefix + "." + eventName + "." + suffix;
		
		//Get all custom properties for the event
		ArrayList<String> eventCustomProperties = phModIni.getUnknownAddressPart(eventCustomPath, ".");
		for (String customProperty : eventCustomProperties) {
			String customPropertyPath = eventCustomPath + "." + customProperty;
			try {
				String[] value = phModIni.getString(customPropertyPath).split(",");
				result.put(customProperty, new ArrayList<String>(Arrays.asList(value)));
			} catch (PropertyException e) {
				log.error("Cannot read custom property of the event {} for custom property {}, propertypath {}", eventName, customProperty, customPropertyPath);
				throw e;
			}
			
		}
		
		
		return result;
	}
	
	/**
	 * load eventconfigs
	 * 
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, EventConfig> loadEventConfigs() throws Exception {
		HashMap<String, EventConfig> res = new HashMap<String, EventConfig>();
		
		ArrayList<String> eventList = phModIni.getUnknownAddressPart(EVENTPREFIX, ".");
		
		for (String eventID : eventList) {
			EventConfig eventConfig = loadEventConfig(eventID, this.availableConditions);
			log.debug("Load options for event {}", eventConfig);
			res.put(eventConfig.getEventName(), eventConfig);
		}
		
		return res;
	}
	
	private HashMap<String, BodyTypeConfig> loadBodyTypeConfigs(HashMap<String, EventConfig> availableEvents) throws Exception {
		HashMap<String, BodyTypeConfig> res = new HashMap<String, BodyTypeConfig>();
		
		ArrayList<String> bodyTypeIds = phModIni.getUnknownAddressPart(BODYTYPEPREFIX, ".");
		
		for (String id : bodyTypeIds) {
			BodyTypeConfig bodyTypeConfig = loadBodyTypeConfig(id, availableEvents);
			res.put(bodyTypeConfig.getBodyTypeName(), bodyTypeConfig);
			log.debug("Load options for bodytype {}.{}.{}", BODYTYPEPREFIX, id, BODYTYPETYPENAMESUFFIX);
		}
		
//		int i=0;
//		
//		while(phModIni.propertyExists(BODYTYPEPREFIX + "." + i + "." + BODYTYPETYPENAMESUFFIX)==true) {
//			log.debug("Load options for event {}.{}.{}", BODYTYPEPREFIX, i, BODYTYPETYPENAMESUFFIX);
//			BodyTypeConfig bodyTypeConfig = loadBodyTypeConfig(i, availableEvents);
//			res.put(bodyTypeConfig.getBodyTypeName(), bodyTypeConfig);
//			i++;
//		}
		
		return res;
	}
	
	private BodyTypeConfig loadBodyTypeConfig(String number, HashMap<String, EventConfig> availableEvents) throws Exception {
		String typeName = phModIni.getString(BODYTYPEPREFIX + "." + number + "." + BODYTYPETYPENAMESUFFIX);
		log.trace("Load body type config name: {}", typeName);
		String mindName = phModIni.getString(BODYTYPEPREFIX + "." + number + "." + BODYTYPEMINDSUFFIX);
		log.trace("Mind name: {}", mindName);
		
		//Get events of action on the body
		ArrayList<String> effectOnAction = phModIni.getStringFromList(BODYTYPEPREFIX + "." + number + "." + BODYTYPEEFFECTONACTIONSUFFIX, ",");
		log.trace("Events on actions: {}", effectOnAction);
		ArrayList<EventConfig> effectOnActionEventConfigList = getEventConfigsFromList(effectOnAction, availableEvents);
		
		//Get events on reaction
		ArrayList<String> effectOnReaction = phModIni.getStringFromList(BODYTYPEPREFIX + "." + number + "." + BODYTYPEEFFECTONREACTIONSUFFIX, ",");
		log.trace("Events on reactions: {}", effectOnReaction);
		ArrayList<EventConfig> effectOnReactionEventConfigList = getEventConfigsFromList(effectOnReaction, availableEvents);
		
		//Get events on own actions
		ArrayList<String> effectOnOwnAction = phModIni.getStringFromList(BODYTYPEPREFIX + "." + number + "." + BODYTYPEEFFECTOFOWNACTIONSUFFIX, ",");
		log.trace("Events on own actions: {}", effectOnOwnAction);
		ArrayList<EventConfig> effectOnOwnActionEventConfigList = getEventConfigsFromList(effectOnOwnAction, availableEvents);
		
		
		//Get events for body internal events
		ArrayList<String> effectBodyInternalEvents = phModIni.getStringFromList(BODYTYPEPREFIX + "." + number + "." + BODYTYPEEFFECTOFBODYINTERNALEVENTSSUFFIX, ",");
		log.trace("Events on body internal events: {}", effectBodyInternalEvents);
		ArrayList<EventConfig> effectOnBodyInternalsEventConfigList = getEventConfigsFromList(effectBodyInternalEvents, availableEvents);
		
		//Get mind
		MindConfig mindConfig = this.availableMinds.get(mindName);
		if (mindName.length()>0 && mindConfig==null) {
			log.error("No mind with the name {} exists.", mindName);
			throw new NullPointerException("No such mind exists");
		}
		
		BodyTypeConfig bodyConfig = new BodyTypeConfig(typeName, mindConfig, effectOnActionEventConfigList, effectOnReactionEventConfigList, effectOnOwnActionEventConfigList, effectOnBodyInternalsEventConfigList);
		
		return bodyConfig;
	}
	
	private ArrayList<EventConfig> getEventConfigsFromList(ArrayList<String> nameList, HashMap<String, EventConfig> availableEvents) {
		ArrayList<EventConfig> res = new ArrayList<EventConfig>();
		
		for (String oName : nameList) {
			EventConfig eventConfig = availableEvents.get(oName);
			if (eventConfig==null) {
				throw new NullPointerException("EventConfig for name " + oName + " not found");
			} else {
				res.add(eventConfig);
			}
		}
		
		return res;
	}
	
	private HashMap<Character, ActorConfig> loadActorConfigs(HashMap<String, BodyTypeConfig> availableBodyTypes) throws Exception {
		HashMap<Character, ActorConfig> res = new HashMap<Character, ActorConfig>();
		
		ArrayList<String> actorIds = phModIni.getUnknownAddressPart(ACTORPREFIX, ".");
		
		for (String id : actorIds) {
			ActorConfig actorConfig = loadActorConfig(id, availableBodyTypes);
			res.put(actorConfig.getWorldMapChar(),actorConfig);
			log.debug("Load options for event {}.{}.{}", ACTORPREFIX, id, ACTORACTORNAMESUFFIX);
		}
		
//		int i=0;
//		
//		while(phModIni.propertyExists(ACTORPREFIX + "." + i + "." + ACTORACTORNAMESUFFIX)==true) {
//			log.debug("Load options for event {}.{}.{}", ACTORPREFIX, i, ACTORACTORNAMESUFFIX);
//			ActorConfig actorConfig = loadActorConfig(i, availableBodyTypes);
//			res.put(actorConfig.getWorldMapChar(),actorConfig);
//			i++;
//		}
		
		return res;
	}
	
	private ActorConfig loadActorConfig(String i, HashMap<String, BodyTypeConfig> availableBodyTypes) throws Exception {
		String actorName = phModIni.getString(ACTORPREFIX + "." + i + "." + ACTORACTORNAMESUFFIX);
		log.trace("Load actor name: {}", actorName);
		String bodyTypeName = phModIni.getString(ACTORPREFIX + "." + i + "." + ACTORBODYTYPENAMESUFFIX);
		log.trace("bodytype: {}", bodyTypeName);
		String iconGraphicPath = phModIni.getString(ACTORPREFIX + "." + i + "." + ACTORICONGRAPHICPATHSUFFIX);
		log.trace("Icon path: {}", iconGraphicPath);
		boolean rotateGraphicDirection = phModIni.getBoolean(ACTORPREFIX + "." + i + "." + ACTORROTATEGRAPHICWITHDIRECTIONSUFFIX);
		log.trace("rotateGraphicDirection: {}", rotateGraphicDirection);
		double initRotation = phModIni.getDouble(ACTORPREFIX + "." + i + "." + ACTORINITROTATIONSUFFIX);
		log.trace("initrotation: {}", initRotation);
		int numberoficonsforInterval =  phModIni.getInt(ACTORPREFIX + "." + i + "." + ACTORNUMBEROFICONSFORINTERVALSUFFIX);
		log.trace("number of icons for interval: {}", numberoficonsforInterval);
		int totalnumberoficons =  phModIni.getInt(ACTORPREFIX + "." + i + "." + ACTORTOTALNUMBEROFICONSSUFFIX);
		log.trace("total number of icons: {}", totalnumberoficons);
		int intervalforgraphicchange = phModIni.getInt(ACTORPREFIX + "." + i + "." + ACTORINTERVALFORGRAPHICCHANGE);
		log.trace("intervalforgraphicchange: {}", intervalforgraphicchange);
		char worldMapChar = phModIni.getString(ACTORPREFIX + "." + i + "." + ACTORWORLDMAPCHARSUFFIX).charAt(0);
		log.trace("worldMapChar: {}", worldMapChar);
		boolean evaluateActor = this.phModIni.getBoolean(ACTORPREFIX + "." + i + "." + ACTOREVALUATEACTORSUFFIX);
		log.trace("evaluateActor: {}", evaluateActor);
				
		HashMap<String, String> eventSounds = getEventSounds(i, availableEvents);
		log.trace("eventSounds: {}", eventSounds);
		
		BodyTypeConfig bodyConfig = getBodyTypeConfig(bodyTypeName, availableBodyTypes);
		
		ActorConfig actorConfig = new ActorConfig(actorName, bodyConfig, iconGraphicPath, rotateGraphicDirection, initRotation, numberoficonsforInterval, totalnumberoficons, intervalforgraphicchange, worldMapChar, evaluateActor);
		
		actorConfig.addAllEventSounds(eventSounds);
		
		HashMap<String, Integer> eventGraphics = getEventGraphics(i, availableEvents);
		log.trace("eventGraphics: {}", eventGraphics);
		
		actorConfig.addAllEventGraphics(eventGraphics);
		
		return actorConfig;
	}
	
	private BodyTypeConfig getBodyTypeConfig(String bodyTypeName, HashMap<String, BodyTypeConfig> availableBodyTypes) {
		return availableBodyTypes.get(bodyTypeName);
	}
	
	private HashMap<String, Integer> getEventGraphics(String i, HashMap<String, EventConfig> availableEvents) throws PropertyException {
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		
		//Test all events
		for (Entry<String, EventConfig> oE : availableEvents.entrySet()) {
			String propertyString = ACTORPREFIX + "." + i + "." + ACTOREVENTASSIGNMENT + "." + oE.getKey() + "." + ACTOREVENTASSIGNMENTGRAPHICSUFFIX;
			boolean propertyExists = phModIni.propertyExists(propertyString);
			if (propertyExists==true) {
				try {
					res.put(oE.getKey(), phModIni.getInt(propertyString));
				} catch (Exception e) {
					throw new PropertyException("Error on receiving proerty");
				}
			}
		}
		return res;
	}
	
	private HashMap<String, String> getEventSounds(String i, HashMap<String, EventConfig> availableEvents) throws PropertyException {
		HashMap<String, String> res = new HashMap<String, String>();
		
		//Test all events
		for (Entry<String, EventConfig> oE : availableEvents.entrySet()) {
			String propertyString = ACTORPREFIX + "." + i + "." + ACTOREVENTASSIGNMENT + "." + oE.getKey() + "." + ACTOREVENTASSIGNMENTSOUNDSUFFIX;
			boolean propertyExists = phModIni.propertyExists(propertyString);
			if (propertyExists==true) {
				try {
					res.put(oE.getKey(), phModIni.getString(propertyString));
				} catch (PropertyException e) {
					throw new PropertyException("Error on receiving proerty");
				}
			}
		}
		return res;
	}
	
	private VisualizationConfig loadVisualizationConfig() throws Exception {
		log.trace("Load visualization config");
		int cellSize =  phModIni.getInt(VISUALIZATIONPREFIX + "." + VISUALIZATIONCELLSIZESUFFIX);
		log.trace("cell size: {}", cellSize);
		int simulationPeriod = phModIni.getInt(VISUALIZATIONPREFIX + "." + VISUALIZATIONSIMPERIODSUFFIX);
		log.trace("simulation period: {}", simulationPeriod);
		String bgImagePath =  phModIni.getString(VISUALIZATIONPREFIX + "." + VISUALIZATIONBGIMAGEPATHSUFFIX);
		log.trace("Background image path: {}", bgImagePath);
		boolean showGrid =  phModIni.getBoolean(VISUALIZATIONPREFIX + "." + VISUALIZATIONSHOWGRIDSUFFIX);
		log.trace("Shall grid be shown: {}", showGrid);
		
		
		VisualizationConfig result = new VisualizationConfig(cellSize, simulationPeriod, bgImagePath, showGrid);
		
		return result;
	}
	
	private  MusicConfig loadMusicConfig() throws PropertyException {
		log.trace("Load music config");
		String musicRelativePath = this.phModIni.getString(MUSICPREFIX + "." + MUSICPATHSUFFIX);
		
		MusicConfig result = new MusicConfig(musicRelativePath);
		
		return result;
	}
	
	private WorldMapConfig loadWorldMapConfig() throws Exception {
		log.trace("Load Worldconfig");
		int horizontalcells =  phModIni.getInt(WORLDPREFIX + "." + WORLDHORIZONTALCELLSSUFFIX);
		log.trace("horizontal cells: {}", horizontalcells);
		int verticalcells =  phModIni.getInt(WORLDPREFIX + "." + WORLDVERTICALCELLSSUFFIX);
		log.trace("vertical cells: {}", verticalcells);
		int layercount = phModIni.getInt(WORLDPREFIX + "." + WORLDLAYERCOUNTSUFFIX);
		log.trace("number of layers: {}", layercount);
		
		String[] layers = getLayers(layercount, horizontalcells, verticalcells);
		
		WorldMapConfig res = new WorldMapConfig(horizontalcells, verticalcells, layers);
		
		return res;
		
	}
	
	private String[] getLayers(int layercount, int horizontalCellsCount, int verticalCellsCount) throws Exception {
		String[] res = new String[layercount];
		
		for (int i=0; i<layercount; i++) {
			res[i] = getLayer(i, horizontalCellsCount, verticalCellsCount);
		}
		
		return res;
	}
	
	private String getLayer(int layer, int horizontalCellsCount, int verticalCellsCount) throws Exception {
		String res ="";
		
		for (int i=0;i<verticalCellsCount;i++) {
			if (phModIni.propertyExists(WORLDPREFIX + "." + WORLDLAYERSUFFIX + "." + layer + "." + i)==true) {	//Check the first entry exists
				String mapPart = phModIni.getString(WORLDPREFIX + "." + WORLDLAYERSUFFIX + "." + layer + "." + i);
				log.trace("layers {}: {}",i,  mapPart);
				if (mapPart.length()!=horizontalCellsCount) {
					throw new Exception("The number of horizontal cells does not match the read values for " + WORLDPREFIX + "." + WORLDLAYERSUFFIX + "." + layer + "." + i + " mappart " + mapPart);
				}
				
				res += mapPart;
			} else {
				throw new Exception("The entry " + WORLDPREFIX + "." + WORLDLAYERSUFFIX + "." + layer + "." + i + " does not exist");
			}
		}
		
		return res;
		
	}
	
	/**
	 * Load all minds
	 * 
	 * @return
	 * @throws Exception 
	 */
	private HashMap<String, MindConfig> loadMindConfig() throws Exception {
		HashMap<String, MindConfig> res = new HashMap<String, MindConfig>();
		
		ArrayList<String> mindIds = phModIni.getUnknownAddressPart(MINDPREFIX, ".");
		
		for (String id : mindIds) {
			log.debug("Load options for mind {}.{}.{}", MINDPREFIX, id, MINDNAMESUFFIX);
			MindConfig mindConfig = loadMindConfig(id);
			res.put(mindConfig.getMindName(), mindConfig);
		}
		
//		int i=0;
//		
//		while(phModIni.propertyExists(MINDPREFIX + "." + i + "." + MINDNAMESUFFIX)==true) {
//			log.debug("Load options for mind {}.{}.{}", MINDPREFIX, i, MINDNAMESUFFIX);
//			MindConfig mindConfig = loadMindConfig(i);
//			res.put(mindConfig.getMindName(), mindConfig);
//			i++;
//		}
		
		return res;
	}
	
	/**
	 * load one mind config
	 * 
	 * @param mindNumber
	 * @return
	 * @throws Exception
	 */
	private MindConfig loadMindConfig(String mindNumber) throws Exception {
		String mindName = phModIni.getString(MINDPREFIX + "." + mindNumber + "." + MINDNAMESUFFIX);
		String mindType = phModIni.getString(MINDPREFIX + "." + mindNumber + "." + MINDTYPESUFFIX);
		String mindClass = phModIni.getString(MINDPREFIX + "." + mindNumber + "." + MINDCLASSSUFFIX);
		
		MindConfig mindConfig = new MindConfig(mindName, mindType, mindClass);
		
		return mindConfig;
	}
	

	public HashMap<String, EventConfig> getAvailableEvents() {
		return availableEvents;
	}

	public HashMap<String, BodyTypeConfig> getAvailableBodyTypes() {
		return availableBodyTypes;
	}

	public HashMap<Character, ActorConfig> getAvailableActors() {
		return availableActors;
	}

	public WorldMapConfig getWorldMapConfig() {
		return worldMapConfig;
	}

	public HashMap<String, MindConfig> getAvailableMinds() {
		return availableMinds;
	}

	public void setAvailableMinds(HashMap<String, MindConfig> availableMinds) {
		this.availableMinds = availableMinds;
	}

	public VisualizationConfig getVisualizationConfig() {
		return visualizationConfig;
	}

	public MusicConfig getMusicConfig() {
		return musicConfig;
	}

	public void setMusicConfig(MusicConfig musicConfig) {
		this.musicConfig = musicConfig;
	}

	
	
}
