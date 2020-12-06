package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Coords_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();
	private ConfigManager cfg = chl.getcfg();
	private GUIManager gui = chl.getGUIM();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			String pName = p.getName();
			World w;
			String wName;
			Environment wEnv;
			String envStr;
			String posName;
			int x;
			int y;
			int z;
			if (!chl.isMLGWorld(p)) {
				if (args.length == 0) {
					if (p.hasPermission("challenges.coords.view")) {
						p.openInventory(gui.getCoordsGUI());
					} else
						p.sendMessage("§cDu hast hierfür keine Berechtigung");
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("share")) {
						if (p.hasPermission("challenges.coords.share")) {
							w = p.getWorld();
							x = p.getLocation().getBlockX();
							y = p.getLocation().getBlockY();
							z = p.getLocation().getBlockZ();
							wEnv = w.getEnvironment();
							envStr = getEnv(wEnv);
							Bukkit.broadcastMessage("§9Position von §a" + pName);
							Bukkit.broadcastMessage("§7¤·" + " " + envStr + "§7, §c" + x + "§7, §c" + y + "§7, §c" + z);
						} else
							p.sendMessage("§cDu hast hierfür keine Berechtigung");
					} else if (args[0].equalsIgnoreCase("get")) {
						if (p.hasPermission("challenges.coords.get")) {
							p.sendMessage("§6Gespeicherte Koordinaten:");
							for (String pos : chl.getConfig().getConfigurationSection("locations").getKeys(false)) {
								p.sendMessage("§7- §e" + cfg.getStr("locations." + pos + ".name"));
							}
							p.sendMessage("§6Für bestimmte Koordinate benutze §e/coords get <Name>");
						} else
							p.sendMessage("§cDu hast hierfür keine Berechtigung");
					} else
						sendHelp(p);
				} else if (args.length >= 2) {
					String pos = args[1].toLowerCase();
					if (args[0].equalsIgnoreCase("save")) {
						if (p.hasPermission("challenges.coords.save")) {
							if (!chl.getConfig().contains("locations." + pos)) {
								posName = args[1];
								w = p.getWorld();
								x = p.getLocation().getBlockX();
								y = p.getLocation().getBlockY();
								z = p.getLocation().getBlockZ();
								wName = w.getName();
								wEnv = w.getEnvironment();
								envStr = getEnv(wEnv);

								cfg.saveStr("locations." + pos + ".name", posName);
								cfg.saveStr("locations." + pos + ".world", wName);
								cfg.saveInt("locations." + pos + ".x", x);
								cfg.saveInt("locations." + pos + ".y", y);
								cfg.saveInt("locations." + pos + ".z", z);

								gui.createCoordsGUI();

								Bukkit.broadcastMessage("§a" + pName + " §7» §e" + posName + " §6[" + envStr + "§7, §c"
										+ x + "§7, §c" + y + "§7, §c" + z + "§6]");
							} else
								p.sendMessage("§cDiese Position existiert bereits!");
						} else
							p.sendMessage("§cDu hast hierfür keine Berechtigung");
					} else if (args[0].equalsIgnoreCase("get")) {
						if (p.hasPermission("challenges.coords.get")) {
							if (chl.getConfig().contains("locations." + pos)) {
								posName = cfg.getStr("locations." + pos + ".name");
								wName = cfg.getStr("locations." + pos + ".world");
								x = cfg.getInt("locations." + pos + ".x");
								y = cfg.getInt("locations." + pos + ".y");
								z = cfg.getInt("locations." + pos + ".z");
								envStr = getEnv(Bukkit.getWorld(wName).getEnvironment());

								p.sendMessage(
										"§e" + posName + " §7» " + envStr + "§7, §c" + x + "§7, §c" + y + "§7, §c" + z);
							} else
								p.sendMessage("§cDiese Position existiert nicht!");
						} else
							p.sendMessage("§cDu hast hierfür keine Berechtigung");
					} else if (args[0].equalsIgnoreCase("delete")) {
						if (p.hasPermission("challenges.coords.delete")) {
							if (chl.getConfig().contains("locations." + pos)) {
								posName = cfg.getStr("locations." + pos + ".name");
								gui.newCoDelGUI(p, posName);
							} else
								p.sendMessage("§cDiese Position existiert nicht!");
						} else
							p.sendMessage("§cDu hast hierfür keine Berechtigung");
					} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
						if (p.hasPermission("challenges.coords.teleport")) {
							if (chl.getConfig().contains("locations." + pos)) {
								posName = cfg.getStr("locations." + pos + ".name");
								wName = cfg.getStr("locations." + pos + ".world");
								x = cfg.getInt("locations." + pos + ".x");
								y = cfg.getInt("locations." + pos + ".y");
								z = cfg.getInt("locations." + pos + ".z");
								Location loc = new Location(Bukkit.getWorld(wName), x, y, z);
								p.teleport(loc);
								p.sendMessage("§aDu wurdest zur Koordinate §6" + posName + " §ateleportiert!");
							} else
								p.sendMessage("§cDiese Position existiert nicht!");
						} else
							p.sendMessage("§cDu hast hierfür keine Berechtigung");
					} else
						sendHelp(p);
				} else
					sendHelp(p);
			} else
				p.sendMessage("§cDieser Befehl ist in dieser Welt nicht verfügbar!");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}

	private void sendHelp(Player p) {
		p.sendMessage("§c~~~~~ §6Coords Command §c~~~~~");
		p.sendMessage("§c/Coords §4- §6Ã–ffne die GUI");
		p.sendMessage("§c/Coords Share §4- §6Teile deine Position");
		p.sendMessage("§c/Coords Get §4- §6Listet alle Koordinaten im Chat auf");
		p.sendMessage("§c/Coords Get <Name> §4- §6Zeigt die Koordinate im Chat");
		p.sendMessage("§c/Coords Save <Name> §4- §6Speichert deine Koordinate");
		p.sendMessage("§c/Coords Delete §4- §6Lösche die Koordinate");
		p.sendMessage("§c/Coords TP §6| §cTeleport §4- §6Teleportiert dich zur Koordinate");
	}

	public String getEnv(Environment env) {
		String wEnv;
		if (env == Environment.NORMAL) {
			wEnv = "§3Overworld";
		} else if (env == Environment.NETHER) {
			wEnv = "§4Nether";
		} else if (env == Environment.THE_END) {
			wEnv = "§5End";
		} else
			wEnv = "§7Unbekannt";
		return wEnv;
	}
}