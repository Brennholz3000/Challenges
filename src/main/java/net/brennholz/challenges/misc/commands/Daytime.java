package net.brennholz.challenges.misc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Daytime implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("challenges.daytime")) {
				if (lable.equalsIgnoreCase("day")) {
					Bukkit.getWorlds().get(0).setTime(1000);
					p.sendMessage("§7Zeit wurde auf §6Tag §7gesetzt");
				}
				if (lable.equalsIgnoreCase("night")) {
					Bukkit.getWorlds().get(0).setTime(13000);
					p.sendMessage("§7Zeit wurde auf §6Nacht §7gesetzt");
				}
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}
}
