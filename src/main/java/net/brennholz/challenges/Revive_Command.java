package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Revive_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender.hasPermission("challenges.revive")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("all")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
						p.setGameMode(GameMode.SURVIVAL);
					}
					chl.getServer().broadcastMessage("§bAlle Spieler wurden wiederbelebt! §aEs kann weiter gehen!");
				} else {
					OfflinePlayer other = (Bukkit.getServer().getOfflinePlayer(args[0]));
					if (other.isOnline()) {
						Player p = (Player) other;
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
						p.setGameMode(GameMode.SURVIVAL);
						chl.getServer().broadcastMessage(
								"§b" + p.getName() + " §awurde von §b" + sender.getName() + " §awiederbelebt!");
					} else
						sender.sendMessage("§cDieser Spieler ist nicht online!");
				}
			} else if (args.length >= 5) {
				World w = Bukkit.getWorld(args[1]);
				double x = Double.parseDouble(args[2]);
				double y = Double.parseDouble(args[3]);
				double z = Double.parseDouble(args[4]);
				Location loc = new Location(w, x, y, z);
				if (args[0].equalsIgnoreCase("all")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.teleport(loc);
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
						p.setGameMode(GameMode.SURVIVAL);
					}
					chl.getServer().broadcastMessage("§bAlle Spieler wurden bei §c" + w.getName() + " " + x + " " + y
							+ " " + z + " §bwiederbelebt! §aEs kann weiter gehen!");
				} else {
					OfflinePlayer other = (Bukkit.getServer().getOfflinePlayer(args[0]));
					if (other.isOnline()) {
						Player p = (Player) other;
						p.teleport(loc);
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
						p.setGameMode(GameMode.SURVIVAL);
						chl.getServer().broadcastMessage("§b" + p.getName() + " §awurde von §b" + sender.getName()
								+ " §abei §c" + w.getName() + " " + x + " " + y + " " + z + " §awiederbelebt!");
					} else
						sender.sendMessage("§cDieser Spieler ist nicht online!");
				}
			} else
				sender.sendMessage("§cBenutze: /revive <Spieler|All> [world] [x] [y] [z]");
		} else
			sender.sendMessage("§cDu hast hierfür keine Berechtigung");
		return true;
	}
}