package userinterface;

public interface SoundManagerInterface {
	public void playSound(String poEntityName, String poEventName);
	public void registerEntity(String poEntityIdentifier) throws Exception;
	public void registerSound(String poEntityIdentifier, String poEventName, String poSoundAddress);
}
