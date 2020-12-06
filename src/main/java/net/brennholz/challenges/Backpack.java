package net.brennholz.challenges;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Backpack implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();

	public static Inventory Backpack = Bukkit.createInventory(null, 27, "§6Backpack");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("challenges.backpack")) {
				p.openInventory(Backpack);
			} else
				p.sendMessage("§cDu hast hierfür keine Berechtigung");
		} else
			sender.sendMessage("§cKein Konsolenbefehl!");
		return true;
	}

	public void clearConfig() {
		Iterator<?> var2 = chl.getBackpackConfig().getKeys(false).iterator();
		while (var2.hasNext()) {
			String main_path = (String) var2.next();
			chl.getBackpackConfig().set(main_path, null);
		}
	}

	public void saveBackpack() {
		clearConfig();
		for (int slot = 0; slot < Backpack.getSize(); slot++) {
			ItemStack stack = Backpack.getItem(slot);
			if ((stack != null) && (stack.getType() != Material.AIR)) {
				chl.getBackpackConfig().set(String.valueOf(slot), stack.serialize());
			}
		}
	}

	public void loadBackpack() {
		Iterator<?> var2 = chl.getBackpackConfig().getValues(false).entrySet().iterator();
		int slot;
		while (var2.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) var2.next();
			try {
				slot = Integer.parseInt((String) entry.getKey());
			} catch (NumberFormatException var6) {
				chl.getServer().broadcastMessage("Fehler! Slot: " + (String) entry.getKey());
				continue;
			}
			if (slot >= Backpack.getSize()) {
				chl.getServer().broadcastMessage("§cSlot " + slot + " befindet sich auÃŸerhalb des Inventars!");
			} else {
				MemorySection memorySection = (MemorySection) entry.getValue();
				ItemStack deserialized = ItemStack.deserialize(memorySection.getValues(false));
				Backpack.setItem(slot, deserialized);
			}
		}
	}
}