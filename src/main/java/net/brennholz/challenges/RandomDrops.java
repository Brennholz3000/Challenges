package net.brennholz.challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

public class RandomDrops {

	private final ArrayList<Material> BLOCKS;
	private final ArrayList<Material> ITEM_DROPS;
	private HashMap<Material, Material> drops = new HashMap<>();
	private ArrayList<Material> BLOCKED_DROPS = new ArrayList<>();

	FileConfiguration config = Challenges.getplugin().getRndDropsConfig();

	public RandomDrops() {
		getBlocked();
		BLOCKS = (ArrayList<Material>) Arrays.stream(Material.values()).filter(mat -> mat.isBlock())
				.collect(Collectors.toList());
		ITEM_DROPS = (ArrayList<Material>) Arrays.stream(Material.values())
				.filter(mat -> mat.isItem() && !BLOCKED_DROPS.contains(mat) && !(mat == Material.BARRIER))
				.collect(Collectors.toList());
	}

	public void getBlocked() {
		config.getStringList("removed_drops").forEach(str -> {
			BLOCKED_DROPS.add(Material.getMaterial(str));
		});
	}

	public void shuffleItems() {
		Collections.shuffle(ITEM_DROPS);
		ArrayList<Material> itemDropsCopy = new ArrayList<>();
		ITEM_DROPS.forEach(m -> itemDropsCopy.add(m));
		BLOCKS.forEach(mat -> {
			int r = new Random().nextInt(itemDropsCopy.size() - 1);
			drops.put(mat, itemDropsCopy.get(r));
			itemDropsCopy.remove(r);
		});
		saveItems();
	}

	public void saveItems() {
		config.set("drops", drops.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.toList()));
	}

	public void loadItems() {
		drops.clear();
		if (config.isList("drops")) {
			config.getStringList("drops").forEach(str -> {
				String[] materialSplitted = str.split(":");
				drops.put(Material.getMaterial(materialSplitted[0]), Material.getMaterial(materialSplitted[1]));
			});
		} else {
			shuffleItems();
		}
	}

	public Material getMaterialFromBlock(Block block) {
		return drops.get(block.getType());
	}
}