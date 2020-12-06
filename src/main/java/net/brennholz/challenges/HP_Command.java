package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HP_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();
	private SettingsManager sett = chl.getSM();
	private GUIManager gui = chl.getGUIM();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender.hasPermission("challenges.hp.modify")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("healall")) {
					for (Player pp : Bukkit.getOnlinePlayers()) {
						pp.setHealth(pp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
						pp.setFoodLevel(20);
					}
					Bukkit.broadcastMessage("§b" + sender.getName() + " §ahat alle geheilt!");
				} else if (args[0].equalsIgnoreCase("sync")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						Double hp = p.getHealth();
						for (Player pp : Bukkit.getOnlinePlayers()) {
							pp.setHealth(hp);
						}
						Bukkit.broadcastMessage("§aLeben wurden synchronisiert!");
					} else
						sender.sendMessage("§cKein Konsolenbefehl!");
				} else
					sendHelp(sender);
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("max")) {
					Double newHP = Double.parseDouble(args[1]);
					if (newHP >= 1 && newHP <= 2048) {
						sett.maxHP = newHP;
						gui.setLifeGUI(14, Material.STONE_BUTTON, "§6Maximale Leben = §c" + sett.maxHP + "§6HP",
								"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10",
								"§7 Shift-Rechtsklick: -10");
						for (Player pp : Bukkit.getOnlinePlayers()) {
							pp.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHP);
							pp.setHealth(newHP);
						}
						settingsChangeString("Maximale HP", newHP.toString());
					} else
						sender.sendMessage("§cUngültiger Wert (1-2048)");
				} else if (args[0].equalsIgnoreCase("get")) {
					OfflinePlayer other = (Bukkit.getOfflinePlayer(args[1]));
					if (other.isOnline()) {
						Player p = (Player) other;
						sender.sendMessage("§6" + p.getName() + " §ebesitzt derzeit §6" + p.getHealth() + "HP§e!");
					} else
						sender.sendMessage("§cDieser Spieler ist nicht online!");
				} else
					sendHelp(sender);
			} else if (args.length >= 3) {
				OfflinePlayer other = (Bukkit.getOfflinePlayer(args[1]));
				if (other.isOnline()) {
					Player p = (Player) other;
					double finaladdhp = p.getHealth() + Double.parseDouble(args[2]);
					double finalremhp = p.getHealth() - Double.parseDouble(args[2]);
					if (args[0].equalsIgnoreCase("add")) {
						if (!(finaladdhp > p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
								&& !(finaladdhp < 0)) {
							p.setHealth(finaladdhp);
							p.sendMessage(
									"§aDu wurdest von §b" + sender.getName() + " §aum §c" + args[2] + "HP §ageheilt!");
							sender.sendMessage("§aDu hast §b" + p.getName() + " §aum §c" + args[2] + "HP §ageheilt!");
						} else
							sender.sendMessage("§cUnmögliche Operation");
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (!(finalremhp > p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
								&& !(finalremhp < 0)) {
							p.setHealth(finalremhp);
							p.sendMessage("§4" + sender.getName() + " §chat dir §4" + args[2] + "HP §centfernt!");
							sender.sendMessage("§cDu hast §4" + p.getName() + " " + args[2] + "HP §centfernt!");
						} else
							sender.sendMessage("§cUnmögliche Operation!");
					} else if (args[0].equalsIgnoreCase("set")) {
						if (!(Double.parseDouble(args[2]) > p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
								&& !(Double.parseDouble(args[2]) < 0)) {
							p.setHealth(Double.parseDouble(args[2]));
							p.sendMessage("§6" + sender.getName() + " §ehat deine HP auf §6" + args[2] + " §egesetzt!");
							sender.sendMessage(
									"§eDu hast die HP von §6" + p.getName() + " §eauf §6" + args[2] + " §egesetzt!");
						} else
							sender.sendMessage("§cUnmögliche Operation");
					} else
						sendHelp(sender);
				} else
					sender.sendMessage("§cDieser Spieler ist nicht online!");
			} else
				sendHelp(sender);
		} else
			sender.sendMessage("§cDu hast hierfür keine Berechtigung");
		return true;
	}

	private void sendHelp(CommandSender s) {
		s.sendMessage("§c~~~~~ §6HP Command §c~~~~~");
		s.sendMessage("§c/HP Get <Spieler> §4- §6Erhalte die aktuelen HP eines Spielers");
		s.sendMessage("§c/HP HealAll §4- §6Heile alle Spieler");
		s.sendMessage("§c/HP Sync §4- §6Die Herzen alle Spieler werden mit deinen Synchronisiert");
		s.sendMessage("§c/HP Add <Spieler> <Wert> §4- §6Füge einem Spieler die angegebene Zahl an Herzen hinzu");
		s.sendMessage("§c/HP Remove <Spieler> <Wert> §4- §6Entferne einem Spieler die angegebene Zahl an Herzen");
		s.sendMessage("§c/HP Set <Spieler> <Wert> §4- §6Setze die Herzen eines Spieler auf die angegebene Zahl");
		s.sendMessage("§c/HP Max <Wert> §4- §6Setze die maximalen HP aller Spieler");
	}

	public void settingsChangeString(String Setting, String value) {
		if (sett.settingsTitle) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle("§c" + Setting, " §bist nun: " + value, 10, 40, 10);
			}
		}
	}
}
