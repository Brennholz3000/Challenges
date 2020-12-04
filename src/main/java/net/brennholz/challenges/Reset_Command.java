package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Reset_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();
	private ConfigManager cfg = chl.getcfg();
	Backpack backpack;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("challenges.reset")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle("§cWelt wird zurückgesetzt!", "§6Du wirst nun gekickt!", 10, 60, 10);
			}
			cfg.saveStr("world_name", Bukkit.getWorlds().get(0).getName());
			cfg.saveBool("world_reset", true);
			for (String key : chl.getConfig().getConfigurationSection("locations").getKeys(false)) {
				cfg.saveStr("locations." + key, null);
			}

			Bukkit.getScheduler().scheduleSyncDelayedTask(chl, new Runnable() {
				@Override
				public void run() {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.kickPlayer("§cWelt wird zurückgesetzt! \n Bitte warte einen Moment...");
					}
					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
					Bukkit.dispatchCommand(console, "restart");
				}
			}, 60);
		} else
			sender.sendMessage("§cDu hast hierfür keine Berechtigung");
		return true;
	}
}
