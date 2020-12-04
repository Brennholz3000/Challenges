package net.brennholz.challenges;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GUIManager {

	private Challenges chl = Challenges.getplugin();
	private SettingsManager sett = chl.getSM();
	private ConfigManager cfg = chl.getcfg();

	public static Inventory challengesGUI = Bukkit.createInventory(null, 54, "§bChallenges");
	public static Inventory cutCleanGUI = Bukkit.createInventory(null, 36, "§eCutClean Einstellungen");
	public static Inventory killallGUI = Bukkit.createInventory(null, 36, "§7KillAll Einstellungen");
	public static Inventory lifeGUI = Bukkit.createInventory(null, 27, "§dLebens Einstellungen");
	public static Inventory miscGUI = Bukkit.createInventory(null, 27, "§aSonstige Einstellungen");
	public static Inventory mainGUI = Bukkit.createInventory(null, 27, "§6Hauptmenü");

	public static Inventory CoordsGUI = Bukkit.createInventory(null, 45, "§6Koordinaten");

	public void createChallGUI() {
		challengesGUI.setItem(0,
				addGUIItem(Material.CRAFTING_TABLE, "§6Erlaube Werkbänke", "§cSpieler können Werkbänke benutzen"));
		challengesGUI.setItem(3, addGUIItem(Material.MAGMA_BLOCK, "§6Schaden beim Schleichen",
				"§cSpieler erhalten §6" + sett.sneakDMG + "HP §cSchaden", "§cwenn sie beginnen zu schleichen"));
		challengesGUI.setItem(5, addGUIItem(Material.BONE, "§6Tod bei Fallschaden", "§cDer Spieler stirbt wenn",
				"§cer Fallschaden erhält"));
		challengesGUI.setItem(8, addGUIItem(Material.WOODEN_PICKAXE, "§6NoBlockBreak", "§cDer Spieler stirbt wenn",
				"§cer einen Block abbaut"));
		challengesGUI.setItem(9,
				addGUIItem(Material.EMERALD, "§6Erlaube Handeln", "§cSpieler können mit", "§cDorfbewohnern handeln"));
		challengesGUI.setItem(17, addGUIItem(Material.STONE, "§6NoBlockPlace", "§cDer Spieler stirbt wenn",
				"§cer einen Block platziert"));
		challengesGUI.setItem(27,
				addGUIItem(Material.BARRIER, "§6Sperre Offhand", "§cSperre die zweite", "§cHand der Spieler"));
		challengesGUI.setItem(35, addGUIItem(Material.DIAMOND_PICKAXE, "§6Random Drops", "§cBlöcke droppen zufällig",
				"§cfestgelegte Items", "§4Ãœberschreibt CutClean"));
		challengesGUI.setItem(36, addGUIItem(Material.BARRIER, "§6Nutzbare Inventar Slots",
				"§cSpieler können §6" + sett.invSlots + " §cSlots verwenden"));
		challengesGUI.setItem(49, addGUIItem(Material.ARROW, "§6Zurück zum Hauptmenü"));
		challengesGUI.setItem(31, addGUIItem(Material.IRON_INGOT, "§6CutClean", "§cÃ–ffne das Menü"));
		challengesGUI.setItem(32,
				addGUIItem(Material.DIAMOND_PICKAXE, "§6Shuffle Drops", "§cMische die Randomdrops neu"));

		challengesGUI.setItem(1, addGUIItem(getDye(sett.craftingTable), getBool(sett.craftingTable)));
		challengesGUI.setItem(7, addGUIItem(getDye(sett.noBreak), getBool(sett.noBreak)));
		challengesGUI.setItem(10, addGUIItem(getDye(sett.trading), getBool(sett.trading)));
		challengesGUI.setItem(12, addGUIItem(Material.STONE_BUTTON, "§6Schleichschaden = §c" + sett.sneakDMG + "§6HP",
				"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10", "§7 Shift-Rechtsklick: -10"));
		challengesGUI.setItem(14, addGUIItem(getDye(sett.noFall), getBool(sett.noFall)));
		challengesGUI.setItem(16, addGUIItem(getDye(sett.noPlace), getBool(sett.noPlace)));
		challengesGUI.setItem(28, addGUIItem(getDye(sett.blockOH), getBool(sett.blockOH)));
		challengesGUI.setItem(30, addGUIItem(getLavaBucket(), "§6Boden ist Lava " + getBool(sett.lavaFloor),
				"§cHinter Spielern", "§cerscheint Lava"));
		challengesGUI.setItem(34, addGUIItem(getDye(sett.rndDrops), getBool(sett.rndDrops)));
		challengesGUI.setItem(37, addGUIItem(Material.STONE_BUTTON, "§6Nutzbare Slots = §c" + sett.invSlots,
				"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10", "§7 Shift-Rechtsklick: -10"));
		challengesGUI.setItem(43, addGUIItem(Material.STONE_BUTTON, "§6Max Minuten bis MLG = §c" + sett.mlgTime,
				"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10", "§7 Shift-Rechtsklick: -10"));
		challengesGUI.setItem(44, addGUIItem(getMLGBucket(), "§bMLG-Challenge " + getBool(sett.mlg),
				"§cSpieler müssen zufällig", "§ceinen MLG machen"));
		for (int i = 0; i < 54; i++) {
			if (challengesGUI.getItem(i) == null) {
				challengesGUI.setItem(i, addGUIItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ""));
			}
		}
	}

	public void setChallGUI(int slot, Material mat, String name, String... lore) {
		challengesGUI.setItem(slot, addGUIItem(mat, name, lore));
	}

	public void createCutGUI() {
		cutCleanGUI.setItem(10, addGUIItem(Material.FLINT, "§8Kies §7†’ §8Feuerstein"));
		cutCleanGUI.setItem(12, addGUIItem(Material.IRON_INGOT, "§7Eisenerz §8†’ §7Eisenbarren"));
		cutCleanGUI.setItem(14, addGUIItem(Material.GOLD_INGOT, "§eGolderz §6†’ §eGoldbarren"));
		cutCleanGUI.setItem(16,
				addGUIItem(Material.COOKED_BEEF, "§6Gebratenes Essen", "§cTiere droppen", "§cGebratenes Fleisch"));
		cutCleanGUI.setItem(19, addGUIItem(getDye(sett.cutGravel), getBool(sett.cutGravel)));
		cutCleanGUI.setItem(21, addGUIItem(getDye(sett.cutIron), getBool(sett.cutIron)));
		cutCleanGUI.setItem(23, addGUIItem(getDye(sett.cutGold), getBool(sett.cutGold)));
		cutCleanGUI.setItem(25, addGUIItem(getDye(sett.cutFood), getBool(sett.cutFood)));
		cutCleanGUI.setItem(31, addGUIItem(Material.ARROW, "§6Zurück zu den Challenges"));
		for (int i = 0; i < 36; i++) {
			if (cutCleanGUI.getItem(i) == null) {
				cutCleanGUI.setItem(i, addGUIItem(Material.YELLOW_STAINED_GLASS_PANE, ""));
			}
		}
	}

	public void setCutGUI(int slot, Material mat, String name, String... lore) {
		cutCleanGUI.setItem(slot, addGUIItem(mat, name, lore));
	}

	public void createKillGUI() {
		killallGUI.setItem(10, addGUIItem(Material.FEATHER, "§6KillAllonFall", "§cBekommt jemand Fallschaden",
				"§cwerden alle getötet"));
		killallGUI.setItem(12, addGUIItem(Material.WATER_BUCKET, "§6KillAllonMLG", "§cVersagt einer den MLG",
				"§cwerden alle getötet"));
		killallGUI.setItem(14, addGUIItem(Material.WOODEN_PICKAXE, "§6KillAllonBreak", "§cWird ein Block abgebaut",
				"§cwerden alle getötet"));
		killallGUI.setItem(16,
				addGUIItem(Material.STONE, "§6KillAllonPlace", "§cWird ein Block platziert", "§cwerden alle getötet"));
		killallGUI.setItem(19, addGUIItem(getDye(sett.noFallKillAll), getBool(sett.noFallKillAll)));
		killallGUI.setItem(21, addGUIItem(getDye(sett.mlgKillAll), getBool(sett.mlgKillAll)));
		killallGUI.setItem(23, addGUIItem(getDye(sett.noBreakKillAll), getBool(sett.noBreakKillAll)));
		killallGUI.setItem(25, addGUIItem(getDye(sett.noPlaceKillAll), getBool(sett.noPlaceKillAll)));
		killallGUI.setItem(31, addGUIItem(Material.ARROW, "§6Zurück zum Hauptmenü"));
		for (int i = 0; i < 36; i++) {
			if (killallGUI.getItem(i) == null) {
				killallGUI.setItem(i, addGUIItem(Material.GRAY_STAINED_GLASS_PANE, ""));
			}
		}
	}

	public void setKillGUI(int slot, Material mat, String name, String... lore) {
		killallGUI.setItem(slot, addGUIItem(mat, name, lore));
	}

	public void createLifeGUI() {
		lifeGUI.setItem(1, addGUIItem(Material.GLISTERING_MELON_SLICE, "§6Geteilte Herzen", "§cAlle Spieler teilen",
				"§csich die Herzen"));
		lifeGUI.setItem(2, addGUIItem(Material.TOTEM_OF_UNDYING, "§6Respawn", "§cSpieler können respawnen"));
		lifeGUI.setItem(3, addGUIItem(Material.POPPY, "§6Ein Leben für alle", "§cStirbt ein Spieler, ist",
				"§cdie Challenge vorbei", "§4Ãœberschreibt Respawn"));
		lifeGUI.setItem(5,
				addGUIItem(Material.RED_DYE, "§6Maximale Leben", "§cLege die maximalen Leben", "§cder Spieler fest"));
		lifeGUI.setItem(6, addGUIItem(Material.GOLDEN_APPLE, "§6Natürliche Regeneration", "§cSpieler regenerieren",
				"§cwenn sie Essen"));
		lifeGUI.setItem(7, addGUIItem(Material.ENCHANTED_GOLDEN_APPLE, "§6Jegliche Regeneration",
				"§cSpieler können regenerieren", "§4Ãœberschreibt Natürliche Regeneration"));
		lifeGUI.setItem(21, addGUIItem(Material.ARROW, "§6Zurück zum Hauptmenü"));
		lifeGUI.setItem(23, addGUIPot(PotionEffectType.HEAL, "§6Alle Spieler heilen"));

		lifeGUI.setItem(10, addGUIItem(getDye(sett.shareHP), getBool(sett.shareHP)));
		lifeGUI.setItem(11, addGUIItem(getDye(sett.respawn), getBool(sett.respawn)));
		lifeGUI.setItem(12, addGUIItem(getDye(sett.oneLife), getBool(sett.oneLife)));
		lifeGUI.setItem(14, addGUIItem(Material.STONE_BUTTON, "§6Maximale Leben = §c" + sett.maxHP + "§6HP",
				"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10", "§7 Shift-Rechtsklick: -10"));
		lifeGUI.setItem(15, addGUIItem(getDye(sett.natRegen), getBool(sett.natRegen)));
		lifeGUI.setItem(16, addGUIItem(getDye(sett.regen), getBool(sett.regen)));
		for (int i = 0; i < 27; i++) {
			if (lifeGUI.getItem(i) == null) {
				lifeGUI.setItem(i, addGUIItem(Material.MAGENTA_STAINED_GLASS_PANE, ""));
			}
		}
	}

	public void setLifeGUI(int slot, Material mat, String name, String... lore) {
		lifeGUI.setItem(slot, addGUIItem(mat, name, lore));
	}

	public void createMiscGUI() {
		miscGUI.setItem(1, addGUIItem(Material.IRON_SWORD, "§6PVP", "§cSpieler können sich", "§cSchaden zufügen"));
		miscGUI.setItem(2, addGUIItem(Material.POPPY, "§6TABHP", "§cSpielerleben werden", "§cbei TAB angezeigt"));
		miscGUI.setItem(3,
				addGUIItem(Material.BOOK, "§6Schaden im Chat", "§cErhaltener Schaden wird", "§cim Chat angezeigt"));
		miscGUI.setItem(4, addGUIItem(Material.BEACON, "§6Zeige Todesposition", "§cStirbt ein Spieler stehen",
				"§cdie Koordinaten im Chat"));
		miscGUI.setItem(5, addGUIItem(Material.ENDER_EYE, "§6Enderpartikel", "§cEnderpartikel erscheinen",
				"§cist der Timer pausiert"));
		miscGUI.setItem(6, addGUIItem(Material.NAME_TAG, "§6Einstellungstitel", "§cEin Titel wird angezeigt",
				"§cändern sich Einstellungen"));
		miscGUI.setItem(7,
				addGUIItem(Material.CHEST, "§6KeepInventory", "§cBehalte dein Inventar", "§cwenn du stirbst"));
		miscGUI.setItem(22, addGUIItem(Material.ARROW, "§6Zurück zum Hauptmenü"));

		miscGUI.setItem(10, addGUIItem(getDye(sett.pvp), getBool(sett.pvp)));
		miscGUI.setItem(11, addGUIItem(getDye(sett.tabHP), getBool(sett.tabHP)));
		miscGUI.setItem(12, addGUIItem(getDye(sett.chatDMG), getBool(sett.chatDMG)));
		miscGUI.setItem(13, addGUIItem(getDye(sett.deathPos), getBool(sett.deathPos)));
		miscGUI.setItem(14, addGUIItem(getDye(sett.pauseParticles), getBool(sett.pauseParticles)));
		miscGUI.setItem(15, addGUIItem(getDye(sett.settingsTitle), getBool(sett.settingsTitle)));
		miscGUI.setItem(16, addGUIItem(getDye(sett.keepInv), getBool(sett.keepInv)));
		for (int i = 0; i < 27; i++) {
			if (miscGUI.getItem(i) == null) {
				miscGUI.setItem(i, addGUIItem(Material.LIME_STAINED_GLASS_PANE, ""));
			}
		}
	}

	public void setMiscGUI(int slot, Material mat, String name, String... lore) {
		miscGUI.setItem(slot, addGUIItem(mat, name, lore));
	}

	public void createMainGUI() {
		mainGUI.setItem(10, addGUIItem(Material.CRAFTING_TABLE, "§bChallenges", "§7- Kein Craften", "§7- Kein Traden",
				"§7- MLG-Challenge", "§7- Random Drops", "§7und mehr..."));
		mainGUI.setItem(12,
				addGUIItem(Material.TOTEM_OF_UNDYING, "§dLebens Einstellungen", "§7- Regeneration",
						"§7- Geteilte Herzen", "§7- Respawn", "§7- Ein Leben für alle", "§7- Maximale Leben",
						"§7- Alle Heilen"));
		mainGUI.setItem(14, addGUIItem(Material.WOODEN_SWORD, "§7KillAll Einstellungen", "§7- KillAllonFall",
				"§7- KillAllonMLG", "§7- KillAllonBreak", "§7- KillAllonPlace"));
		mainGUI.setItem(16,
				addGUIItem(Material.STRING, "§aSonstige Einstellungen", "§7- PVP", "§7- TABHP", "§7- Schaden im Chat",
						"§7- Zeige Todesposition", "§7- Enderpartikel", "§7- Einstellungstitel", "§7- KeepInventory"));
		for (int i = 0; i < 27; i++) {
			if (mainGUI.getItem(i) == null) {
				mainGUI.setItem(i, addGUIItem(Material.ORANGE_STAINED_GLASS_PANE, ""));
			}
		}
	}

	public void createCoordsGUI() {
		int i = 0;
		String wName;
		int x;
		int y;
		int z;
		String posName;
		Environment wEnv;
		String envStr;
		CoordsGUI.clear();
		for (String pos : chl.getConfig().getConfigurationSection("locations").getKeys(false)) {
			if (i >= 45) {
				break;
			}
			posName = cfg.getStr("locations." + pos + ".name");
			wName = cfg.getStr("locations." + pos + ".world");
			x = cfg.getInt("locations." + pos + ".x");
			y = cfg.getInt("locations." + pos + ".y");
			z = cfg.getInt("locations." + pos + ".z");
			wEnv = Bukkit.getWorld(wName).getEnvironment();
			envStr = getEnv(wEnv);

			CoordsGUI.setItem(i, addGUIItem(getBlock(wEnv), posName, envStr, "§cX: " + x, "§cY: " + y, "§cZ: " + z));
			i++;
		}
		if (i == 0) {
			CoordsGUI.setItem(22, addGUIItem(Material.BARRIER, "§c§oKeine Koordinaten gespeichert"));
		}
	}

	public void newCoInfGUI(Player p, String name) {
		String envStr = getEnv(
				Bukkit.getWorld(cfg.getStr("locations." + name.toLowerCase() + ".world")).getEnvironment());
		Inventory infoGUI = Bukkit.createInventory(null, 9, "§6Koordinaten Info");
		infoGUI.setItem(0, addGUIItem(Material.STRUCTURE_VOID, "§cKoordinate löschen"));
		infoGUI.setItem(2, addGUIItem(Material.BOOK, "§aKoordinate in Chat"));
		infoGUI.setItem(4,
				addGUIItem(Material.ORANGE_BANNER, name, envStr,
						"§cX: " + cfg.getInt("locations." + name.toLowerCase() + ".x"),
						"§cY: " + cfg.getInt("locations." + name.toLowerCase() + ".y"),
						"§cZ: " + cfg.getInt("locations." + name.toLowerCase() + ".z")));
		infoGUI.setItem(6, addGUIItem(Material.ENDER_PEARL, "§5Zu " + name + " teleportieren"));
		infoGUI.setItem(8, addGUIItem(Material.ARROW, "§cZurück zur Ãœbersicht"));
		for (int i = 1; i < 8; i++) {
			if (infoGUI.getItem(i) == null) {
				infoGUI.setItem(i, addGUIItem(Material.ORANGE_STAINED_GLASS_PANE, ""));
			}
		}
		p.openInventory(infoGUI);
	}

	public void newCoDelGUI(Player p, String name) {
		Inventory delGUI = Bukkit.createInventory(null, 9, "§6Koordinate löschen");
		delGUI.setItem(0, addGUIItem(Material.LIME_TERRACOTTA, "§aBestätigen", "§6" + name + " wird gelöscht"));
		delGUI.setItem(4, addGUIItem(Material.ORANGE_BANNER, name));
		delGUI.setItem(8, addGUIItem(Material.RED_TERRACOTTA, "§cAbbrechen", "§6Zurück zur Info"));
		for (int i = 1; i < 8; i++) {
			if (delGUI.getItem(i) == null) {
				delGUI.setItem(i, addGUIItem(Material.ORANGE_STAINED_GLASS_PANE, ""));
			}
		}
		p.openInventory(delGUI);
	}

	public Inventory getChallGUI() {
		return challengesGUI;
	}

	public Inventory getCutGUI() {
		return cutCleanGUI;
	}

	public Inventory getKillGUI() {
		return killallGUI;
	}

	public Inventory getLifeGUI() {
		return lifeGUI;
	}

	public Inventory getMiscGUI() {
		return miscGUI;
	}

	public Inventory getMainGUI() {
		return mainGUI;
	}

	public Inventory getCoordsGUI() {
		return CoordsGUI;
	}

	public ItemStack addGUIItem(Material mat, String name, String... lore) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> metalore = new ArrayList<String>();
		if (lore != null) {
			for (String lorecomments : lore) {
				metalore.add(lorecomments);
			}
		}
		meta.setLore(metalore);
		item.setItemMeta(meta);

		return item;
	}

	public ItemStack addGUIPot(PotionEffectType effectType, String name, String... lore) {
		ItemStack potion = new ItemStack(Material.POTION, 1);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> metalore = new ArrayList<String>();
		if (lore != null) {
			for (String lorecomments : lore) {
				metalore.add(lorecomments);
			}
		}
		meta.setLore(metalore);
		meta.addCustomEffect(new PotionEffect(effectType, 300, 2), true);
		potion.setItemMeta(meta);

		return potion;
	}

	public Material getDye(Boolean bool) {
		ItemStack mat = new ItemStack(Material.GRAY_DYE);
		if (bool) {
			mat.setType(Material.LIME_DYE);
		} else
			mat.setType(Material.RED_DYE);
		return mat.getType();
	}

	public String getBool(Boolean bool) {
		if (bool) {
			return "§aAktiviert";
		} else
			return "§cDeaktiviert";
	}

	public Material getMLGBucket() {
		ItemStack mat = new ItemStack(Material.BUCKET);
		if (sett.mlg) {
			mat.setType(Material.WATER_BUCKET);
		} else
			mat.setType(Material.BUCKET);
		return mat.getType();
	}

	public Material getLavaBucket() {
		ItemStack mat = new ItemStack(Material.BUCKET);
		if (sett.lavaFloor) {
			mat.setType(Material.LAVA_BUCKET);
		} else
			mat.setType(Material.BUCKET);
		return mat.getType();
	}

	public String getEnv(Environment env) {
		String wEnv;
		if (env == Environment.NORMAL) {
			wEnv = "§3Overworld";
		} else if (env == Environment.NETHER) {
			wEnv = "§4Nether";
		} else if (env == Environment.THE_END) {
			wEnv = "§5End";
		} else
			wEnv = "§7Unbekannt";
		return wEnv;
	}

	public Material getBlock(Environment env) {
		Material mat = Material.BARRIER;
		if (env == Environment.NORMAL) {
			mat = Material.GRASS_BLOCK;
		} else if (env == Environment.NETHER) {
			mat = Material.NETHERRACK;
		} else if (env == Environment.THE_END) {
			mat = Material.END_STONE;
		}
		return mat;
	}
}
