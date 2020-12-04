package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DorfSpawn_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			World w = p.getWorld();
			if (p.hasPermission("challenges.dorfspawn")) {
				if (w.getEnvironment().equals(Environment.NORMAL) && !chl.isMLGWorld(p)) {
					Location nearVil = w.locateNearestStructure(p.getLocation(), StructureType.VILLAGE, 1000, false);
					if (nearVil != null) {
						for (int i = w.getSeaLevel(); i < 255; i++) {
							nearVil.setY(i);
							if (nearVil.getBlock().getType() == Material.AIR) {
								if (nearVil.add(0, 1, 0).getBlock().getType() == Material.AIR) {
									w.loadChunk(nearVil.getBlockX(), nearVil.getBlockZ());
									w.setSpawnLocation(nearVil);
									tpVillage(nearVil.subtract(-.5, 1, -.5));
									break;
								}
							}
						}
					} else
						p.sendMessage("§cKonnte kein Dorf in der NÃ¤he finden!");
				} else
					p.sendMessage("§cDu befindest dich nicht in der Oberwelt!");
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}

	public void tpVillage(Location vill) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(vill);
		}
	}
}
