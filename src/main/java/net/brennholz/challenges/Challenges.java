package net.brennholz.challenges;

import java.io.File;
import java.io.IOException;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;

import net.brennholz.challenges.misc.commands.Daytime;
import net.brennholz.challenges.misc.commands.Fly;
import net.brennholz.challenges.misc.commands.Gamemode;
import net.brennholz.challenges.misc.commands.Weather;

public class Challenges extends JavaPlugin {

	private static Challenges plugin;

	private File BackpackConfigFile;
	private FileConfiguration BackpackConfig;

	private File RndDropsConfigFile;
	private FileConfiguration RndDropsConfig;

	private Timer_Command timer;
	private Backpack backpack;
	private SBManager sbManager;
	private RandomDrops rndDrops;
	private ConfigManager cfgMngr;
	private SettingsManager settingsMngr;
	private GUIManager guiMngr;

	public static Challenges getplugin() {
		return plugin;
	}

	@Override
	public void onEnable() {
		Challenges.plugin = this;
		backpack = new Backpack();
		cfgMngr = new ConfigManager();
		settingsMngr = new SettingsManager();
		guiMngr = new GUIManager();
		timer = new Timer_Command();
		sbManager = new SBManager();
		getCommand("settings").setExecutor(new Settings_Command());
		getCommand("hp").setExecutor(new HP_Command());
		getCommand("coords").setExecutor(new Coords_Command());
		getCommand("revive").setExecutor(new Revive_Command());
		getCommand("timer").setExecutor(new Timer_Command());
		getCommand("challenges").setExecutor(new Challenges_Command());
		getCommand("backpack").setExecutor(new Backpack());
		getCommand("reset").setExecutor(new Reset_Command());
		getCommand("dorfspawn").setExecutor(new DorfSpawn_Command());
		getCommand("gamemode").setExecutor(new Gamemode());
		getCommand("fly").setExecutor(new Fly());
		getCommand("day").setExecutor(new Daytime());
		getCommand("sun").setExecutor(new Weather());
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		saveDefaultConfig();
		reloadConfig();
		createBackpackConfig();
		createRndDropsConfig();
		rndDrops = new RandomDrops();
		if (getcfg().getBool("world_reset")) {
			this.getLogger().info("/Reset command was executed!");
			this.getLogger().info("Preparing world reset!");
			String worldname = getcfg().getStr("world_name");
			String nethername = worldname + "_nether";
			String endname = worldname + "_the_end";
			File worldfolder = new File(Bukkit.getWorldContainer().getPath() + "/" + worldname);
			File netherfolder = new File(Bukkit.getWorldContainer().getPath() + "/" + nethername);
			File endfolder = new File(Bukkit.getWorldContainer().getPath() + "/" + endname);
			File mlgfolder = new File(Bukkit.getWorldContainer().getPath() + "/mlg_challenge");

			try {
				if (worldfolder.exists()) {
					MoreFiles.deleteRecursively(worldfolder.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
				} else
					getLogger().info("Worldfolder does not exist, can't delete!");
				if (netherfolder.exists()) {
					MoreFiles.deleteRecursively(netherfolder.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
				} else
					getLogger().info("Netherfolder does not exist, can't delete!");
				if (endfolder.exists()) {
					MoreFiles.deleteRecursively(endfolder.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
				} else
					getLogger().info("Endfolder does not exist, can't delete!");
				if (mlgfolder.exists()) {
					MoreFiles.deleteRecursively(mlgfolder.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
				} else
					getLogger().info("MLGfolder does not exist, can't delete!");
				this.getLogger().info("World reset successful!");
			} catch (IOException e) {
				this.getLogger().info("World reset failed!");
				e.printStackTrace();
			}
			getcfg().saveBool("world_reset", false);
			getcfg().saveInt("timer.time", 0);
			backpack.clearConfig();
			rndDrops.shuffleItems();
		} else {
			rndDrops.loadItems();
		}

		getSM().loadSettings();

		getGUIM().createChallGUI();
		getGUIM().createCutGUI();
		getGUIM().createKillGUI();
		getGUIM().createLifeGUI();
		getGUIM().createMiscGUI();
		getGUIM().createMainGUI();

		runTilWorldLoaded();

		backpack.loadBackpack();

		getTimer().loadTimer();

		int pluginID = 8053;
		Metrics metrics = new Metrics(this, pluginID);
		this.getLogger().info("Plugin loaded!");
	}

	@Override
	public void onDisable() {
		getTimer().setActive(false);
		getTimer().saveTimer();
		getSM().saveSettings();
		backpack.saveBackpack();
		;
		try {
			BackpackConfig.save(BackpackConfigFile);
			RndDropsConfig.save(RndDropsConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getLogger().info("Plugin unloaded");
	}

	public RandomDrops getRandomDropsManager() {
		return rndDrops;
	}

	public FileConfiguration getBackpackConfig() {
		return this.BackpackConfig;
	}

	private void createBackpackConfig() {
		BackpackConfigFile = new File(getDataFolder(), "backpack.yml");
		if (!BackpackConfigFile.exists()) {
			BackpackConfigFile.getParentFile().mkdirs();
			saveResource("backpack.yml", false);
		}
		BackpackConfig = new YamlConfiguration();
		try {
			BackpackConfig.load(BackpackConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getRndDropsConfig() {
		return this.RndDropsConfig;
	}

	private void createRndDropsConfig() {
		RndDropsConfigFile = new File(getDataFolder(), "rnddrops.yml");
		if (!RndDropsConfigFile.exists()) {
			RndDropsConfigFile.getParentFile().mkdirs();
			saveResource("rnddrops.yml", false);
		}
		RndDropsConfig = new YamlConfiguration();
		try {
			RndDropsConfig.load(RndDropsConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public Timer_Command getTimer() {
		return timer;
	}

	public ConfigManager getcfg() {
		return cfgMngr;
	}

	public SettingsManager getSM() {
		return settingsMngr;
	}

	public GUIManager getGUIM() {
		return guiMngr;
	}

	public SBManager getSBManager() {
		return sbManager;
	}

	public void runTilWorldLoaded() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!closeWorldMissing()) {
					if (Bukkit.getWorlds().get(0) != null && Bukkit.getWorlds().get(1) != null
							&& Bukkit.getWorlds().get(2) != null) {
						getGUIM().createCoordsGUI();
						checkMLGWorld();
						getLogger().info("Main worlds loaded!");
						cancel();
					}
				} else {
					Bukkit.shutdown();
					cancel();
				}
			}
		}.runTaskTimer(this, 20, 20);
	}

	public boolean closeWorldMissing() {
		Boolean allowNether = Bukkit.getAllowNether();
		Boolean allowEnd = Bukkit.getAllowEnd();

		if (allowNether && allowEnd) {
			return false;
		} else {
			if (!allowNether) {
				getLogger().info(ChatColor.DARK_RED + "'allow-nether' ist 'false'");
				getLogger().info(ChatColor.DARK_RED
						+ "Ändere den Wert in der 'server.properties' auf 'true' und starte den Server neu!");
			}
			if (!allowEnd) {
				getLogger().info(ChatColor.DARK_RED + "'allow-end' ist 'false'");
				getLogger().info(ChatColor.DARK_RED
						+ "Ändere den Wert in der 'bukkit.yml' auf 'true' und starte den Server neu!");
			}
			getLogger().info(ChatColor.DARK_RED + "Server fährt herunter...");
			return true;
		}
	}

	@SuppressWarnings("deprecation")
	public void checkMLGWorld() {
		if (getSM().mlg && (Bukkit.getWorld("mlg_challenge") == null)) {
			Bukkit.broadcastMessage("§4LAGGS MÖGLICH: §cMLG-Welt wird generiert...");
			Bukkit.createWorld(new WorldCreator("mlg_challenge").type(WorldType.FLAT).generateStructures(false));
			Bukkit.broadcastMessage("§aMLG-Welt wurde erfolgreich erstellt!");
			World w = Bukkit.getWorld("mlg_challenge");
			w.setAnimalSpawnLimit(0);
			w.setMonsterSpawnLimit(0);
			Bukkit.getWorld("mlg_challenge").setGameRuleValue("doDaylightCycle", "false");
			Bukkit.getWorld("mlg_challenge").setGameRuleValue("doWeatherCycle", "false");
			Bukkit.getWorld("mlg_challenge").setTime(6000);
		}
	}

	@SuppressWarnings("deprecation")
	public void forceLoadMLGWorld() {
		if (Bukkit.getWorld("mlg_challenge") == null) {
			Bukkit.broadcastMessage("§4LAGGS MÖGLICH: §cMLG-Welt wird generiert...");
			Bukkit.createWorld(new WorldCreator("mlg_challenge").type(WorldType.FLAT).generateStructures(false));
			Bukkit.broadcastMessage("§aMLG-Welt wurde erfolgreich erstellt!");
			World w = Bukkit.getWorld("mlg_challenge");
			w.setAnimalSpawnLimit(0);
			w.setMonsterSpawnLimit(0);
			Bukkit.getWorld("mlg_challenge").setGameRuleValue("doDaylightCycle", "false");
			Bukkit.getWorld("mlg_challenge").setGameRuleValue("doWeatherCycle", "false");
			Bukkit.getWorld("mlg_challenge").setTime(6000);
		}
	}

	public boolean isMLGWorld(Player p) {
		if (p.getWorld() == Bukkit.getWorld("mlg_challenge")) {
			return true;
		} else
			return false;
	}
}