package net.brennholz.challenges.misc.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("challenges.gamemode")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage("§7Dein Spielmodus wurde zu §6Überleben §7geändert");
					} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")
							|| args[0].equalsIgnoreCase("c")) {
						p.setGameMode(GameMode.CREATIVE);
						p.sendMessage("§7Dein Spielmodus wurde zu §6Kreativ §7geändert");
					} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")
							|| args[0].equalsIgnoreCase("a")) {
						p.setGameMode(GameMode.ADVENTURE);
						p.sendMessage("§7Dein Spielmodus wurde zu §6Abenteuer §7geändert");
					} else if (args[0].equalsIgnoreCase("spectator") || args[0].equals("3")) {
						p.setGameMode(GameMode.SPECTATOR);
						p.sendMessage("§7Dein Spielmodus wurde zu §6Zuschauer §7geändert");
					} else
						p.sendMessage("§c/gm <0|1|2|3> [Spieler]");
				} else if (args.length >= 2) {
					OfflinePlayer offp = Bukkit.getOfflinePlayer(args[1]);
					if (offp.isOnline()) {
						Player onp = (Player) offp;
						if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
							onp.setGameMode(GameMode.SURVIVAL);
							onp.sendMessage("§7Dein Spielmodus wurde zu §6Überleben §7geändert");
						} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")
								|| args[0].equalsIgnoreCase("c")) {
							onp.setGameMode(GameMode.CREATIVE);
							onp.sendMessage("§7Dein Spielmodus wurde zu §6Kreativ §7geändert");
						} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")
								|| args[0].equalsIgnoreCase("a")) {
							onp.setGameMode(GameMode.ADVENTURE);
							onp.sendMessage("§7Dein Spielmodus wurde zu §6Abenteuer §7geändert");
						} else if (args[0].equalsIgnoreCase("spectator") || args[0].equals("3")) {
							onp.setGameMode(GameMode.SPECTATOR);
							onp.sendMessage("§7Dein Spielmodus wurde zu §6Zuschauer §7geändert");
						} else
							p.sendMessage("§c/gm <0|1|2|3> [Spieler]");
					} else
						p.sendMessage("§cDieser Spieler ist nicht online!");
				} else
					p.sendMessage("§c/gm <0|1|2|3> [Spieler]");
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}
}
