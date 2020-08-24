package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Settings_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();
	private SettingsManager sett = chl.getSM();
	private GUIManager gui = chl.getGUIM();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				if (p.hasPermission("challenges.settings.view")) {
					p.openInventory(gui.getMainGUI());
				} else
					p.sendMessage("§cDu hast hierfür keine Berechtigung");
			} else if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("get")) {
					if (p.hasPermission("challenges.settings.get")) {
						p.sendMessage("§c~~~~~§6Aktuelle Einstellungen§c~~~~~");
						p.sendMessage("§6Erlaube Crafting =§c " + sett.craftingTable);
						p.sendMessage("§6Erlaube Handeln =§c " + sett.trading);
						p.sendMessage("§6Schleichschaden =§c " + sett.sneakDMG + "HP");
						p.sendMessage("§6Tod bei Fallschaden =§c " + sett.noFall);
						p.sendMessage("§6Alle sterben bei Fallschaden =§c " + sett.noFallKillAll);
						p.sendMessage("§6Tod bei BlockBreak =§c " + sett.noBreak);
						p.sendMessage("§6BlockBreak tötet alle =§c " + sett.noBreakKillAll);
						p.sendMessage("§6Tod bei BlockPlace =§c " + sett.noPlace);
						p.sendMessage("§6BlockPlace tötet alle =§c " + sett.noPlaceKillAll);
						p.sendMessage("§6Random Drops =§c " + sett.rndDrops);
						p.sendMessage("§6CutClean Feuerstein =§c " + sett.cutGravel);
						p.sendMessage("§6CutClean Eisen =§c " + sett.cutIron);
						p.sendMessage("§6CutClean Gold =§c " + sett.cutGold);
						p.sendMessage("§6CutClean Fleisch =§c " + sett.cutFood);
						p.sendMessage("§6MLG Challenge =§c " + sett.mlg);
						p.sendMessage("§6MLG-Fail tötet alle Spieler =§c " + sett.mlgKillAll);
						p.sendMessage("§6Maximale Zeit bis MLG =§c " + sett.mlgTime);
						p.sendMessage("§6Boden ist Lava =§c " + sett.lavaFloor);
						p.sendMessage("§6Zahl nutzbarer Slots =§c " + sett.invSlots);
						p.sendMessage("§6BlockOffHand =§c " + sett.blockOH);
						p.sendMessage("§6Maximale HP =§c " + sett.maxHP);
						p.sendMessage("§6Geteilte Herzen =§c " + sett.shareHP);
						p.sendMessage("§6Respawn =§c " + sett.respawn);
						p.sendMessage("§6Ein Leben für alle =§c " + sett.oneLife);
						p.sendMessage("§6Schaden in Chat =§c " + sett.chatDMG);
						p.sendMessage("§6KeepInventory = §c " + sett.keepInv);
						p.sendMessage("§6Natürliche Regeneration = §c " + sett.natRegen);
						p.sendMessage("§6Jegliche Regeneration = §c " + sett.regen);
						p.sendMessage("§6HP in TAB-Liste =§c " + sett.tabHP);
						p.sendMessage("§6Partikel bei Pause =§c " + sett.pauseParticles);
						p.sendMessage("§6Einstellungstitel =§c " + sett.settingsTitle);
						p.sendMessage("§6PvP =§c " + sett.pvp);
						p.sendMessage("§6Position in Todesnachricht =§c " + sett.deathPos);
					} else
						p.sendMessage("§cDu hast hierfür keine Berechtigung");
				} else if (args[0].equalsIgnoreCase("shuffledrops")) {
					if (p.hasPermission("challenges.settings.modify")) {
						chl.getRandomDropsManager().shuffleItems();
						Bukkit.broadcastMessage("§aRandom Drops wurden neu gemischt!");
					} else
						p.sendMessage("§cDu hast hierfür keine Berechtigung");
				} else
					sendHelp(p);
			} else
				sendHelp(p);
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}

	private void sendHelp(Player p) {
		p.sendMessage("§c~~~~~ §6Settings Command §c~~~~~");
		p.sendMessage("§c/Settings §4- §6Öffne die GUI");
		p.sendMessage("§c/Settings Get §4- §6Liste alle Einstellungen auf");
		p.sendMessage("§c/Settings ShuffleDrops §4- §6Mische die RandomDrops neu");
	}

}
