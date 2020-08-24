package net.brennholz.challenges.misc.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("challenges.fly")) {
				if (args.length == 0) {
					if (p.getAllowFlight()) {
						p.setAllowFlight(false);
						p.sendMessage("§7Der Flugmodus wurde §cdeaktiviert§7!");
					} else {
						p.setAllowFlight(true);
						p.sendMessage("§7Der Flugmodus wurde §aaktiviert§7!");
					}
				} else {
					OfflinePlayer offp = Bukkit.getOfflinePlayer(args[0]);
					if (offp.isOnline()) {
						Player onp = (Player) offp;
						if (onp.getAllowFlight()) {
							onp.setAllowFlight(false);
							onp.sendMessage("§7Der Flugmodus wurde §cdeaktiviert§7!");
							p.sendMessage("§7Flugmodus für §6" + onp.getName() + " §cdeaktiviert!");
						} else {
							onp.setAllowFlight(true);
							onp.sendMessage("§7Der Flugmodus wurde §aaktiviert§7!");
							p.sendMessage("§7Flugmodus für §6" + onp.getName() + " §aaktiviert!");
						}
					} else
						p.sendMessage("§6Brenntasa§8.§6net §8» §6" + offp.getName() + " §cist nicht online!");
				}
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}
}
