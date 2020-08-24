package net.brennholz.challenges;

public class ConfigManager {

	private Challenges main = Challenges.getplugin();

	public Integer getInt(String path) {
		return main.getConfig().getInt(path);
	}

	public String getStr(String path) {
		return main.getConfig().getString(path);
	}

	public Double getDbl(String path) {
		return main.getConfig().getDouble(path);
	}

	public Boolean getBool(String path) {
		return main.getConfig().getBoolean(path);
	}

	public void saveInt(String path, Integer value) {
		main.getConfig().set(path, value);
		main.saveConfig();
	}

	public void saveStr(String path, String value) {
		main.getConfig().set(path, value);
		main.saveConfig();
	}

	public void saveDbl(String path, Double value) {
		main.getConfig().set(path, value);
		main.saveConfig();
	}

	public void saveBool(String path, Boolean value) {
		main.getConfig().set(path, value);
		main.saveConfig();
	}

}
