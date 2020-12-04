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
					p.sendMessage("ß7Das Wetter wurde zu ß6Sonnenschein ß7ge√§ndert");
				}
				if (lable.equalsIgnoreCase("rain")) {
					w.setStorm(true);
					w.setThundering(false);
					p.sendMessage("ß7Das Wetter wurde zu ß6Regen ß7ge√§ndert");
				}
				if (lable.equalsIgnoreCase("thunder")) {
					w.setStorm(true);
					w.setThundering(true);
					p.sendMessage("ß7Das Wetter wurde zu ß6Gewitter ß7ge√§ndert");
				}
			} else
				p.sendMessage("ßcDu hast hierf¸r keine Berechtigung");
		} else
			sender.sendMessage("ßcKein Konsolenbefehl!");
		return true;
	}
}
