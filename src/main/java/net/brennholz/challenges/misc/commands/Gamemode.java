package net.brennholz.challenges.misc.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

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
					if (args[1].equalsIgnoreCase("@a")) {
						for (Player player : Bukkit.getServer().getOnlinePlayers()) {
							setPlayersGamemode(player, args[0]);
						}
					} else if (args[1].equalsIgnoreCase("@s")) {
						setPlayersGamemode(p, args[0]);
					} else {
						OfflinePlayer offp = Bukkit.getPlayer(args[1]);
						if (offp.isOnline()) {
							Player onp = (Player) offp;
							setPlayersGamemode(onp, args[0]);
						} else
							p.sendMessage("§cDieser Spieler ist nicht online!");
					}
				} else
					p.sendMessage("§c/gm <0|1|2|3> [Spieler]");
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}
	
	private void setPlayersGamemode(Player p, String arg) {
		if (arg.equalsIgnoreCase("survival") || arg.equals("0")) {
			p.setGameMode(GameMode.SURVIVAL);
			p.sendMessage("§7Dein Spielmodus wurde zu §6Überleben §7geändert");
		} else if (arg.equalsIgnoreCase("creative") || arg.equals("1")
				|| arg.equalsIgnoreCase("c")) {
			p.setGameMode(GameMode.CREATIVE);
			p.sendMessage("§7Dein Spielmodus wurde zu §6Kreativ §7geändert");
		} else if (arg.equalsIgnoreCase("adventure") || arg.equals("2")
				|| arg.equalsIgnoreCase("a")) {
			p.setGameMode(GameMode.ADVENTURE);
			p.sendMessage("§7Dein Spielmodus wurde zu §6Abenteuer §7geändert");
		} else if (arg.equalsIgnoreCase("spectator") || arg.equals("3")) {
			p.setGameMode(GameMode.SPECTATOR);
			p.sendMessage("§7Dein Spielmodus wurde zu §6Zuschauer §7geändert");
		} else
			p.sendMessage("§c/gm <0|1|2|3> [Spieler]");
	}
}
