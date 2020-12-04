package net.brennholz.challenges;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Challenges_Command implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender.hasPermission("challenges.challenges")) {
			sender.sendMessage("§eChallenges §6Ver 2.2.2 §eby §cBrennholz3000");
			sender.sendMessage("§6Verfügbare Befehle:");
			sender.sendMessage("§c/HP §4- §6Verwalte die HP einzelner Spieler");
			sender.sendMessage("§c/Coords §4- §6Teile oder speichere Koordinaten");
			sender.sendMessage("§c/Settings §4- §6Verwalte die Einstellungen");
			sender.sendMessage("§c/Timer §4- §6Verwalte den Timer");
			sender.sendMessage("§c/Revive §4- §6Wiederbelebe Spieler");
			sender.sendMessage("§c/Backpack §6| §c/bp §4- §6Ã–ffne das Backpack");
			sender.sendMessage("§c/Dorfspawn §4- §6Teleportiert alle zum nächsten Dorf");
			sender.sendMessage("§c/Reset §4- §6Generiert eine neue Welt, resettet Timer/Coords und Backpack");
		} else
			sender.sendMessage("§cDu hast hierfür keine Berechtigung");
		return true;
	}
}