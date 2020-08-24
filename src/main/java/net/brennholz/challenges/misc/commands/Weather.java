package net.brennholz.challenges.misc.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Weather implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			World w = Bukkit.getWorlds().get(0);
			if (p.hasPermission("challenges.weather")) {
				if (lable.equalsIgnoreCase("sun")) {
					w.setStorm(false);
					w.setThundering(false);
					p.sendMessage("§7Das Wetter wurde zu §6Sonnenschein §7geändert");
				}
				if (lable.equalsIgnoreCase("rain")) {
					w.setStorm(true);
					w.setThundering(false);
					p.sendMessage("§7Das Wetter wurde zu §6Regen §7geändert");
				}
				if (lable.equalsIgnoreCase("thunder")) {
					w.setStorm(true);
					w.setThundering(true);
					p.sendMessage("§7Das Wetter wurde zu §6Gewitter §7geändert");
				}
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}
}
