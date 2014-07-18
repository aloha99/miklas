package event;

import java.util.HashMap;
import java.util.Map.Entry;

import logger.MyLogger;

import org.slf4j.Logger;

public class DataStructureContainer {
	private HashMap<String, Datapoint<?>> temporaryDataContainer = new HashMap<String, Datapoint<?>>();
	private HashMap<String, Datapoint<?>> permanentDataContainer = new HashMap<String, Datapoint<?>>();
	
	private static final Logger log = MyLogger.getLog("Event");
	
	public void setPermanentData(Datapoint<?> data) {
		this.permanentDataContainer.put(data.getName(), data);
	}
	
	public void unregisterPermanentData(String id) {
		this.permanentDataContainer.remove(id);
	}
	
	public void setTemporaryData(Datapoint<?> newData) throws Exception {
		this.temporaryDataContainer.put(newData.getName(), newData);
		//log.info("this.temporaryDataContainer: {}", this.temporaryDataContainer);
	}
	
	public <T extends Object> boolean getData(Datapoint<T> value) throws Exception {
		return getData(value, false, null);
	}
	
	/**
	 * Get datapoint
	 * 
	 * @param value
	 * @param removeFromTemp = remove when it has been read
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> boolean getData(Datapoint<T> value, boolean removeFromTemp, T defaultValue) throws Exception {
		//Value found
		boolean result = false;
		
		Class<T> type = (Class<T>) value.getClass();
		
		Datapoint<?> datapoint = temporaryDataContainer.get(value.getName());
		if (datapoint==null) {
			datapoint = this.permanentDataContainer.get(value.getName());
//			if (result==null) {
//				log.warn("No result for id {}");
//			}
		} else {
			//As this is a temporary container, the value is removed after receival
			if (removeFromTemp==true) {
				this.temporaryDataContainer.remove(datapoint.getName());
			}
			
		}
		
		if (datapoint!=null) {
			if (type.isAssignableFrom(datapoint.getClass())==true) {
				value.setValue((T) datapoint.getValue());
			} else {
				throw new Exception("Wrong class type. Datapointclass: " + datapoint.getValue().getClass() + ", demanded class: " + type);
			}
			
			result=true;
		} else {
			value.setValue(defaultValue);
		}
		
		return result;
	}
	
	public void clearTemporaryData() {
		this.temporaryDataContainer.clear();
	}
	
	public String toString() {
		String result = "";
		
		result += "\npermanent structures: ";
		
		for (Entry<String, Datapoint<?>> e : permanentDataContainer.entrySet()) {
			result += e.getKey() + "=" + e.getValue() + "; ";
		}
		
		result += "\ntemporary structures: ";
		
		for (Entry<String, Datapoint<?>> e : temporaryDataContainer.entrySet()) {
			result += e.getKey() + "=" + e.getValue() + "; ";
		}
		
		return result;
		
	}
	
}
