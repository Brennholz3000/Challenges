package net.brennholz.challenges;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsManager {

	private Challenges chl = Challenges.getplugin();
	private ConfigManager cfg = chl.getcfg();

	public Boolean craftingTable;
	public Boolean trading;
	public Boolean noPlace;
	public Boolean noPlaceKillAll;
	public Boolean noBreak;
	public Boolean noBreakKillAll;
	public Boolean rndDrops;
	public Boolean cutIron;
	public Boolean cutGold;
	public Boolean cutGravel;
	public Boolean cutFood;
	public Boolean noFall;
	public Boolean noFallKillAll;
	public Double sneakDMG;
	public Boolean lavaFloor;
	public Integer invSlots;
	public Boolean blockOH;
	public Boolean shareHP;
	public Boolean respawn;
	public Boolean oneLife;
	public Boolean chatDMG;
	public Boolean mlg;
	public Boolean mlgKillAll;
	public Integer mlgTime;
	public Boolean natRegen;
	public Boolean regen;
	public Boolean keepInv;
	public Boolean deathPos;
	public Boolean tabHP;
	public Boolean pauseParticles;
	public Boolean settingsTitle;
	public Boolean pvp;
	public Double maxHP;

	public void loadSettings() {
		craftingTable = cfg.getBool("craftingtable");
		trading = cfg.getBool("trading");
		noPlace = cfg.getBool("noplace.enabled");
		noPlaceKillAll = cfg.getBool("noplace.killall");
		noBreak = cfg.getBool("nobreak.enabled");
		noBreakKillAll = cfg.getBool("nobreak.killall");
		rndDrops = cfg.getBool("rnddrops");
		cutIron = cfg.getBool("cutclean.iron");
		cutGold = cfg.getBool("cutclean.gold");
		cutGravel = cfg.getBool("cutclean.gravel");
		cutFood = cfg.getBool("cutclean.food");
		noFall = cfg.getBool("deathonfall.enabled");
		noFallKillAll = cfg.getBool("deathonfall.killall");
		sneakDMG = cfg.getDbl("sneakdmg");
		lavaFloor = cfg.getBool("lavafloor");
		invSlots = cfg.getInt("invslots");
		blockOH = cfg.getBool("blockoffhand");
		shareHP = cfg.getBool("sharehp");
		respawn = cfg.getBool("respawn");
		oneLife = cfg.getBool("onelife");
		chatDMG = cfg.getBool("dmginchat");
		mlg = cfg.getBool("mlg.enabled");
		mlgKillAll = cfg.getBool("mlg.killall");
		mlgTime = cfg.getInt("mlg.maxtime");
		natRegen = cfg.getBool("regen.nat");
		regen = cfg.getBool("regen.all");
		keepInv = cfg.getBool("keepinventory");
		deathPos = cfg.getBool("showdeathpos");
		tabHP = cfg.getBool("tabhp");
		pauseParticles = cfg.getBool("pauseparticles");
		settingsTitle = cfg.getBool("showsettingstitle");
		pvp = cfg.getBool("pvp");
		maxHP = cfg.getDbl("maxhp");
	}

	public void saveSettings() {
		cfg.saveBool("craftingtable", craftingTable);
		cfg.saveBool("trading", trading);
		cfg.saveBool("noplace.enabled", noPlace);
		cfg.saveBool("noplace.killall", noPlaceKillAll);
		cfg.saveBool("nobreak.enabled", noBreak);
		cfg.saveBool("nobreak.killall", noBreakKillAll);
		cfg.saveBool("rnddrops", rndDrops);
		cfg.saveBool("cutclean.iron", cutIron);
		cfg.saveBool("cutclean.gold", cutGold);
		cfg.saveBool("cutclean.gravel", cutGravel);
		cfg.saveBool("cutclean.food", cutFood);
		cfg.saveBool("deathonfall.enabled", noFall);
		cfg.saveBool("deathonfall.killall", noFallKillAll);
		cfg.saveDbl("sneakdmg", sneakDMG);
		cfg.saveBool("lavafloor", lavaFloor);
		cfg.saveInt("invslots", invSlots);
		cfg.saveBool("blockoffhand", blockOH);
		cfg.saveBool("sharehp", shareHP);
		cfg.saveBool("respawn", respawn);
		cfg.saveBool("onelife", oneLife);
		cfg.saveBool("dmginchat", chatDMG);
		cfg.saveBool("mlg.enabled", mlg);
		cfg.saveBool("mlg.killall", mlgKillAll);
		cfg.saveInt("mlg.maxtime", mlgTime);
		cfg.saveBool("regen.nat", natRegen);
		cfg.saveBool("regen.all", regen);
		cfg.saveBool("keepinventory", keepInv);
		cfg.saveBool("showdeathpos", deathPos);
		cfg.saveBool("tabhp", tabHP);
		cfg.saveBool("pauseparticles", pauseParticles);
		cfg.saveBool("showsettingstitle", settingsTitle);
		cfg.saveBool("pvp", pvp);
		cfg.saveDbl("maxhp", maxHP);
	}

	public void updateInvSlots(Player p) {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4GESPERRT");
		item.setItemMeta(meta);
		for (int i = 0; i <= 44; i++) {
			if (p.getInventory().getItem(i) != null && p.getInventory().getItem(i).getType() == Material.BARRIER) {
				p.getInventory().clear(i);
			}
		}
		if (p.getGameMode() == GameMode.SURVIVAL) {
			for (int i = 9; i <= 35 && i <= (44 - invSlots); i++) {
				p.getInventory().setItem(i, item);
			}
			if (invSlots < 9) {
				for (int i = 0 + invSlots; i <= 9; i++) {
					p.getInventory().setItem(i, item);
				}
			}
			if (blockOH) {
				p.getInventory().setItemInOffHand(item);
			} else if (p.getInventory().getItemInOffHand().getType() == Material.BARRIER) {
				p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
		}
	}

}
