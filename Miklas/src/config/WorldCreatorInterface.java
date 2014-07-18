package config;

import ch.aplu.jgamegrid.Location;

public interface WorldCreatorInterface {
	public char getCellValue(Location location, int pnLayer) throws Exception;
	public int getLayerCount();
	public int getXDimension();
	public int getYDimension();
}
