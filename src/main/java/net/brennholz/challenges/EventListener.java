package net.brennholz.challenges;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EventListener implements Listener {

	private Challenges chl = Challenges.getplugin();
	private Timer_Command timer = chl.getTimer();
	private SettingsManager sett = chl.getSM();
	private SBManager sb = chl.getSBManager();
	private ConfigManager cfg = chl.getcfg();
	private GUIManager gui = chl.getGUIM();

	private ArrayList<Block> LAVA_BLOCKS = new ArrayList<>();
	private static ArrayList<Player> food = new ArrayList<>();

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage("§6" + p.getName() + " §chat den Server betreten!");
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(sett.maxHP);
		sett.updateInvSlots(p);
		if (sett.tabHP) {
			sb.createScoreboard(p);
		}
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage("§6" + p.getName() + " §chat den Server verlassen!");
		if (Bukkit.getOnlinePlayers().size() <= 1 && timer.isActive()) {
			timer.setActive(false);
			Bukkit.broadcastMessage("§cDer Timer wurde pausert, da sich niemand mehr auf dem Server befindet!");
		}
	}

	@EventHandler
	public void gameModeChange(PlayerGameModeChangeEvent e) {
		if (e.getNewGameMode() == GameMode.SURVIVAL) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(chl, new Runnable() {

				@Override
				public void run() {
					sett.updateInvSlots(e.getPlayer());
				}
			}, 1);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block block = e.getBlock();
		Material blockmat = block.getType();
		World world = block.getWorld();
		Location loc = block.getLocation();
		if (chl.isMLGWorld(p)) {
			e.setCancelled(true);
		} else {
			if (timer.isActive() && sett.noBreak && p.getGameMode() == GameMode.SURVIVAL) {
				Bukkit.broadcastMessage("§c" + p.getName() + " §6hat einen Block abgebaut!");
				if (sett.noBreakKillAll) {
					for (Player pp : Bukkit.getOnlinePlayers()) {
						pp.setHealth(0);
						pp.playSound(pp.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
					}
				} else {
					p.setHealth(0);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
				}
			}
			if (sett.rndDrops && p.getGameMode() == GameMode.SURVIVAL) {
				Material mat = chl.getRandomDropsManager().getMaterialFromBlock(block);
				e.setDropItems(false);
				if (mat != Material.AIR) {
					world.dropItem(block.getLocation().add(.5, .5, .5), new ItemStack(mat));
				} else
					chl.getLogger().info("Block " + blockmat + " droppt " + mat);
			} else if (p.getGameMode() == GameMode.SURVIVAL) {
				List<ItemStack> drops = (List<ItemStack>) block.getDrops(p.getInventory().getItemInMainHand());

				for (ItemStack i : drops) {
					if (blockmat == Material.GRAVEL && i.getType() == Material.GRAVEL && sett.cutGravel) {
						e.setDropItems(false);
						world.dropItem(loc.add(.5, .5, .5), new ItemStack(Material.FLINT));
					}
					if (blockmat == Material.IRON_ORE && i.getType() == Material.IRON_ORE && sett.cutIron) {
						e.setDropItems(false);
						e.setExpToDrop(1);
						world.dropItem(loc.add(.5, .5, .5), new ItemStack(Material.IRON_INGOT));
					}
					if (blockmat == Material.GOLD_ORE && i.getType() == Material.GOLD_ORE && sett.cutGold) {
						e.setDropItems(false);
						e.setExpToDrop(1);
						world.dropItem(loc.add(.5, .5, .5), new ItemStack(Material.GOLD_INGOT));
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Material blockType = e.getBlockAgainst().getType();
		if (chl.isMLGWorld(p)) {
			e.setCancelled(true);
		} else {
			if (e.getBlockPlaced().getType() == Material.BARRIER && p.getGameMode() == GameMode.SURVIVAL) {
				e.setCancelled(true);
			}
			if (timer.isActive() && sett.noPlace && blockType != Material.BARRIER
					&& p.getGameMode() == GameMode.SURVIVAL && blockType != Material.END_PORTAL_FRAME
					&& blockType != Material.OBSIDIAN) {
				Bukkit.broadcastMessage("§c" + p.getName() + " §6hat einen Block platziert!");
				if (sett.noPlaceKillAll) {
					for (Player pp : Bukkit.getOnlinePlayers()) {
						pp.setHealth(0);
						pp.playSound(pp.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
					}
				} else {
					p.setHealth(0);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
				}
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		if (timer.isActive()) {
			if (sett.rndDrops) {
				e.setYield(0);
				List<Block> blocks = e.blockList();
				Iterator<Block> i = blocks.iterator();
				while (i.hasNext()) {
					Block block = i.next();
					Material mat = chl.getRandomDropsManager().getMaterialFromBlock(block);
					if (mat != Material.AIR) {
						block.getWorld().dropItem(block.getLocation().add(.5, .5, .5), new ItemStack(mat));
					} else
						chl.getLogger().info("Block " + block.getType() + " droppt " + mat);
				}
			}
		} else
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		if (timer.isActive()) {
			if (sett.rndDrops) {
				e.setYield(0);
				List<Block> blocks = e.blockList();
				Iterator<Block> i = blocks.iterator();
				while (i.hasNext()) {
					Block block = i.next();
					Material mat = chl.getRandomDropsManager().getMaterialFromBlock(block);
					if (mat != Material.AIR) {
						block.getWorld().dropItem(block.getLocation().add(.5, .5, .5), new ItemStack(mat));
					} else
						chl.getLogger().info("Block " + block.getType() + " droppt " + mat);
				}
			}
		} else
			e.setCancelled(true);
	}

	@EventHandler
	public void bucketPlace(PlayerBucketEmptyEvent e) {
		Player p = e.getPlayer();
		if (chl.isMLGWorld(p)) {
			removeBlock(e.getBlockClicked());
		}
	}

	public void removeBlock(Block b) {
		World w = b.getLocation().getWorld();
		int x = b.getLocation().getBlockX();
		int y = b.getLocation().getBlockY();
		int z = b.getLocation().getBlockZ();
		Location loc = new Location(w, x, y + 1, z);
		b.getLocation().setY(b.getLocation().getY() + 1);

		Bukkit.getScheduler().scheduleSyncDelayedTask(chl, new Runnable() {

			@Override
			public void run() {
				loc.getBlock().setType(Material.AIR);
			}
		}, 30);
	}

	@EventHandler
	public void playerItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (e.getItemDrop().getItemStack().getType() == Material.BARRIER && p.getGameMode() == GameMode.SURVIVAL) {
			e.setCancelled(true);
		}
		if (chl.isMLGWorld(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.CRAFTING_TABLE) {
				if (!sett.craftingTable) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent e) {
		if (e.getRightClicked().getType() == EntityType.VILLAGER) {
			if (!sett.trading) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if (timer.isActive() && sett.lavaFloor) {
			Player p = e.getPlayer();
			Location oldLoc = e.getFrom();
			Location newLoc = e.getTo();
			int oldx = oldLoc.getBlockX();
			int oldy = oldLoc.getBlockY();
			int oldz = oldLoc.getBlockZ();
			int newx = newLoc.getBlockX();
			int newy = newLoc.getBlockY();
			int newz = newLoc.getBlockZ();

			if (newx != oldx || newy != oldy || newz != oldz) {
				new BukkitRunnable() {

					@Override
					public void run() {
						Block b = p.getLocation().subtract(0, 1, 0).getBlock();
						if (!chl.isMLGWorld(p) && p.getGameMode() == GameMode.SURVIVAL && !b.isPassable()
								&& !LAVA_BLOCKS.contains(b)) {
							LAVA_BLOCKS.add(b);
							setMagma(b, b.getType());
						}
					}
				}.runTaskLater(chl, 1L);
			}
		}
	}

	public void setMagma(Block b, Material old) {
		new BukkitRunnable() {

			@Override
			public void run() {
				b.setType(Material.MAGMA_BLOCK);
				setLava(b, old);
			}
		}.runTaskLater(chl, 20L);
	}

	public void setLava(Block b, Material old) {
		new BukkitRunnable() {

			@Override
			public void run() {
				b.setType(Material.LAVA);
				setOld(b, old);
			}
		}.runTaskLater(chl, 60L);
	}

	public void setOld(Block b, Material old) {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (b.getType() == Material.LAVA) {
					b.setType(old);
				}
				LAVA_BLOCKS.remove(b);
			}
		}.runTaskLater(chl, 200L);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		sett.updateInvSlots(p);
		if (!sett.respawn && p.getHealth() <= 0) {
			p.sendTitle("§cDu bist gestorben!", "§4Du bist nun Zuschauer", 10, 80, 10);
			Bukkit.getScheduler().scheduleSyncDelayedTask(chl, new Runnable() {
				@Override
				public void run() {
					p.setGameMode(GameMode.SPECTATOR);
				}
			}, 1);
		}
	}

	@EventHandler
	public void playerSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Double dmg = sett.sneakDMG;
		if (timer.isActive() && dmg > 0 && p.getGameMode() == GameMode.SURVIVAL && e.isSneaking()) {
			Double newHP = p.getHealth() - dmg;
			if (newHP < 0) {
				p.damage(p.getHealth());
			} else {
				p.damage(dmg);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (timer.isActive()) {
				Player p = (Player) e.getEntity();
				Double dmg = e.getFinalDamage();
				Double hp = p.getHealth() - dmg;
				DamageCause cause = e.getCause();
				if (dmg > 0) {
					if (cause == DamageCause.FALL) {
						if (chl.isMLGWorld(p)) {
							e.setCancelled(true);
							mlgDeath(p);
						} else if (!fallDeath(p, dmg)) {
							chatDamage(p, dmg, cause.toString());
							syncHP(p, hp);
						} else
							e.setCancelled(true);
					} else if (cause == DamageCause.CUSTOM) {
						chatDamage(p, dmg, "SNEAKING");
						syncHP(p, hp);
					} else if (cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.ENTITY_EXPLOSION
							|| cause == DamageCause.PROJECTILE) {

					} else {
						chatDamage(p, dmg, cause.toString());
						syncHP(p, hp);
					}
				}
			} else
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Player) && (e.getFinalDamage() > 0)) {
			if (timer.isActive()) {
				Player p = (Player) e.getEntity();
				Double dmg = e.getFinalDamage();
				Double hp = p.getHealth() - dmg;
				String damager = e.getDamager().getName();
				if (e.getDamager() instanceof Player && !sett.pvp) {
					e.setCancelled(true);
				} else if (e.getDamager() instanceof Projectile) {
					Projectile proj = (Projectile) e.getDamager();
					if (proj.getShooter() instanceof Player && !sett.pvp) {
						e.setCancelled(true);
					} else {
						chatDamage(p, dmg, damager);
						syncHP(p, hp);
					}
				} else {
					syncHP(p, hp);
					chatDamage(p, dmg, damager);
				}
			} else
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		List<ItemStack> drops = e.getDrops();
		Iterator<ItemStack> i = drops.iterator();
		if (sett.keepInv) {
			e.setDroppedExp(0);
			e.getDrops().clear();
			e.getDroppedExp();
			e.setKeepInventory(true);
			e.setKeepLevel(true);
		}
		while (i.hasNext()) {
			ItemStack item = i.next();
			if (item.getType() == Material.BARRIER) {
				i.remove();
			}
		}
		e.setDeathMessage(null);
		if (chl.isMLGWorld(p)) {
			p.getInventory().clear();
			e.setDroppedExp(0);
			e.getDrops().clear();
		} else
			sendDeathMessage(p);
		cancelChallenge();
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		List<ItemStack> drops = e.getDrops();
		if (sett.cutFood) {
			for (ItemStack i : drops) {
				if (i.getType() == Material.BEEF) {
					drops.add(new ItemStack(Material.COOKED_BEEF, i.getAmount()));
					drops.remove(i);
				}
				if (i.getType() == Material.PORKCHOP) {
					drops.add(new ItemStack(Material.COOKED_PORKCHOP, i.getAmount()));
					drops.remove(i);
				}
				if (i.getType() == Material.MUTTON) {
					drops.add(new ItemStack(Material.COOKED_MUTTON, i.getAmount()));
					drops.remove(i);
				}
				if (i.getType() == Material.CHICKEN) {
					drops.add(new ItemStack(Material.COOKED_CHICKEN, i.getAmount()));
					drops.remove(i);
				}
				if (i.getType() == Material.RABBIT) {
					drops.add(new ItemStack(Material.COOKED_RABBIT, i.getAmount()));
					drops.remove(i);
				}
				if (i.getType() == Material.COD) {
					drops.add(new ItemStack(Material.COOKED_COD, i.getAmount()));
					drops.remove(i);
				}
				if (i.getType() == Material.SALMON) {
					drops.add(new ItemStack(Material.COOKED_SALMON, i.getAmount()));
					drops.remove(i);
				}
			}
		}
		if (e.getEntity() instanceof EnderDragon) {
			timer.setActive(false);
			String sek = timer.ssek;
			String min = timer.smin;
			int hrs = timer.hrs;
			Bukkit.broadcastMessage("§6§kAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§dGRATULATION! IHR HABT DIE CHALLENGE ERFOLGREICH ABGESCHLOSSEN!");
			if (timer.isReversed()) {
				Bukkit.broadcastMessage("§aVerbleibende Zeit: §b" + hrs + ":" + min + ":" + sek);
			} else
				Bukkit.broadcastMessage("§aBenötigte Zeit: §b" + hrs + ":" + min + ":" + sek);
			Bukkit.broadcastMessage("§aSeed: §b" + Bukkit.getWorlds().get(0).getSeed());
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§6§kAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		}
	}

	public boolean fallDeath(Player p, Double dmg) {
		if (sett.noFall) {
			Bukkit.broadcastMessage("§6" + p.getName() + " §chat Fallschaden bekommen!");
			if (sett.noFallKillAll) {
				for (Player pp : Bukkit.getOnlinePlayers()) {
					pp.setHealth(0);
					pp.playSound(pp.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
				}
			} else {
				p.setHealth(0);
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
			}
			return true;
		} else
			return false;
	}

	public void mlgDeath(Player p) {
		Bukkit.broadcastMessage("§6" + p.getName() + " §chat die §bMLG-Challenge §cnicht geschafft!");
		if (sett.mlgKillAll) {
			for (Player pp : Bukkit.getOnlinePlayers()) {
				pp.setHealth(0);
				pp.playSound(pp.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
			}
		} else {
			p.setHealth(0);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
		}
	}

	public void sendDeathMessage(Player p) {
		if (timer.isActive()) {
			String w = p.getWorld().getName();
			int x = p.getLocation().getBlockX();
			int y = p.getLocation().getBlockY();
			int z = p.getLocation().getBlockZ();
			if (sett.deathPos) {
				Bukkit.broadcastMessage(
						"§6" + p.getName() + " §cist gestorben! (§6" + w + " " + x + " " + y + " " + z + "§c)");
			} else
				Bukkit.broadcastMessage("§6" + p.getName() + " §cist gestorben!");
			if (!sett.respawn) {
				Bukkit.getServer().broadcastMessage(
						"§6Benutze §c/revive " + p.getName() + " [world] [x] [y] [z] §6zum wiederbeleben!");
			}
		}
	}

	public void cancelChallenge() {
		if (sett.oneLife) {
			timer.setActive(false);
			String sek = timer.ssek;
			String min = timer.smin;
			int hrs = timer.hrs;
			for (Player pp : Bukkit.getOnlinePlayers()) {
				pp.setGameMode(GameMode.SPECTATOR);
			}
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("");
			if (timer.isReversed()) {
				Bukkit.broadcastMessage("§cDie Challenge wurde mit einer Restzeit von §6" + hrs + ":" + min + ":" + sek
						+ " §cabgebrochen!");
			} else
				Bukkit.broadcastMessage(
						"§cDie Challenge wurde nach §6" + hrs + ":" + min + ":" + sek + " §cabgebrochen!");
			Bukkit.broadcastMessage("§aSeed: §b" + Bukkit.getWorlds().get(0).getSeed());
			Bukkit.broadcastMessage("§6Um alle wiederzubeleben benutze §c/revive ALL");
		}
	}

	public void chatDamage(Player p, double dmg, String cause) {
		String sdmg = new DecimalFormat("#.##").format(dmg);
		if (sett.chatDMG) {
			Bukkit.broadcastMessage("§6" + p.getName() + " §chat §4" + sdmg + "HP §cSchaden genommen durch §4" + cause);
		}
	}

	public void syncHP(Player p, double health) {
		if (sett.shareHP) {
			for (Player pp : Bukkit.getOnlinePlayers()) {
				if (pp != p) {
					if (health > pp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
						pp.setHealth(pp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					} else if (health < 0) {
						pp.setHealth(0);
					} else
						pp.setHealth(health);
				}
			}
		}
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		if (!timer.isActive()) {
			if (e.getFoodLevel() < p.getFoodLevel()) {
				e.setCancelled(true);
			}
		} else {
			if (food.contains(p) && e.getFoodLevel() < p.getFoodLevel()) {
				e.setCancelled(true);
				food.remove(p);
			}
		}
	}

	@EventHandler
	public void regainHealth(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (!sett.regen || chl.isMLGWorld(p)) {
				food.add(p);
				e.setCancelled(true);
			} else if (!timer.isActive() || !sett.natRegen) {
				if (e.getRegainReason() == RegainReason.EATING || e.getRegainReason() == RegainReason.SATIATED) {
					if (!food.contains(p)) {
						food.add(p);
					}
					e.setCancelled(true);
				} else
					syncHP(p, p.getHealth() + e.getAmount());
			} else
				syncHP(p, p.getHealth() + e.getAmount());
		}
	}

	@EventHandler
	public void swapHands(PlayerSwapHandItemsEvent e) {
		if (e.getMainHandItem().getType() == Material.BARRIER || e.getOffHandItem().getType() == Material.BARRIER) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void invClick(InventoryClickEvent e) {
		String invname = e.getView().getTitle();
		ClickType clickType = e.getClick();
		Player p = (Player) e.getWhoClicked();
		int slot = e.getRawSlot();

		if (e.getCurrentItem() != null && p.getGameMode() == GameMode.SURVIVAL) {
			if (e.getCurrentItem().getType() == Material.BARRIER) {
				e.setCancelled(true);
			}
		}

		if (clickType == ClickType.NUMBER_KEY && p.getGameMode() == GameMode.SURVIVAL
				&& p.getInventory().getItem(e.getHotbarButton()) != null) {
			if (p.getInventory().getItem(e.getHotbarButton()).getType() == Material.BARRIER) {
				e.setCancelled(true);
			}
		}

		if (invname.equals("§6Hauptmenü")) {
			e.setCancelled(true);
			switch (slot) {
			case 10:
				p.closeInventory();
				p.openInventory(gui.getChallGUI());
				break;
			case 12:
				p.closeInventory();
				p.openInventory(gui.getLifeGUI());
				break;
			case 14:
				p.closeInventory();
				p.openInventory(gui.getKillGUI());
				break;
			case 16:
				p.closeInventory();
				p.openInventory(gui.getMiscGUI());
				break;
			default:
				break;
			}
		}

		if (invname.equals("§bChallenges")) {
			e.setCancelled(true);
			if (slot == 49) {
				p.closeInventory();
				p.openInventory(gui.getMainGUI());
			} else if (slot == 31) {
				p.closeInventory();
				p.openInventory(gui.getCutGUI());
			} else if (p.hasPermission("challenges.settings.modify")) {
				switch (slot) {
				case 1:
					sett.craftingTable = !sett.craftingTable;
					gui.setChallGUI(1, gui.getDye(sett.craftingTable), gui.getBool(sett.craftingTable));
					settingsChange("Erlaube Werkbänke", sett.craftingTable);
					break;
				case 7:
					sett.noBreak = !sett.noBreak;
					gui.setChallGUI(7, gui.getDye(sett.noBreak), gui.getBool(sett.noBreak));
					settingsChange("NoBlockBreak", sett.noBreak);
					break;
				case 10:
					sett.trading = !sett.trading;
					gui.setChallGUI(10, gui.getDye(sett.trading), gui.getBool(sett.trading));
					settingsChange("Erlaube Handeln", sett.trading);
					break;
				case 12:
					Double newDMG = sett.sneakDMG;
					if (clickType == ClickType.LEFT) {
						newDMG = newDMG + 1;
					}
					if (clickType == ClickType.RIGHT) {
						newDMG = newDMG - 1;
					}
					if (clickType == ClickType.SHIFT_LEFT) {
						newDMG = newDMG + 10;
					}
					if (clickType == ClickType.SHIFT_RIGHT) {
						newDMG = newDMG - 10;
					}
					if (newDMG >= 0 && newDMG <= 2048) {
						sett.sneakDMG = newDMG;
						gui.setChallGUI(12, Material.STONE_BUTTON, "§6Schleichschaden = §c" + sett.sneakDMG + "§6HP",
								"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10",
								"§7 Shift-Rechtsklick: -10");
						gui.setChallGUI(3, Material.MAGMA_BLOCK, "§6Schaden beim Schleichen",
								"§cSpieler erhalten §6" + sett.sneakDMG + "HP §cSchaden",
								"§cwenn sie beginnen zu schleichen");
						settingsChangeString("Schleichschaden", newDMG.toString());
					}
					break;
				case 14:
					sett.noFall = !sett.noFall;
					gui.setChallGUI(14, gui.getDye(sett.noFall), gui.getBool(sett.noFall));
					settingsChange("Tod bei Fallschaden", sett.noFall);
					break;
				case 16:
					sett.noPlace = !sett.noPlace;
					gui.setChallGUI(16, gui.getDye(sett.noPlace), gui.getBool(sett.noPlace));
					settingsChange("NoBlockPlace", sett.noPlace);
					break;
				case 28:
					sett.blockOH = !sett.blockOH;
					gui.setChallGUI(28, gui.getDye(sett.blockOH), gui.getBool(sett.blockOH));
					for (Player pp : Bukkit.getOnlinePlayers()) {
						sett.updateInvSlots(pp);
					}
					settingsChange("Sperre Offhand", sett.blockOH);
					break;
				case 30:
					sett.lavaFloor = !sett.lavaFloor;
					gui.setChallGUI(30, gui.getLavaBucket(), "§6Boden ist Lava " + gui.getBool(sett.lavaFloor),
							"§cHinter Spielern", "§cerscheint Lava");
					settingsChange("Boden ist Lava", sett.lavaFloor);
					break;
				case 32:
					chl.getRandomDropsManager().shuffleItems();
					Bukkit.broadcastMessage("§aRandom Drops wurden neu gemischt!");
					break;
				case 34:
					sett.rndDrops = !sett.rndDrops;
					gui.setChallGUI(34, gui.getDye(sett.rndDrops), gui.getBool(sett.rndDrops));
					settingsChange("Random Drops", sett.rndDrops);
					break;
				case 37:
					int newSlots = sett.invSlots;
					if (clickType == ClickType.LEFT) {
						newSlots = newSlots + 1;
					}
					if (clickType == ClickType.RIGHT) {
						newSlots = newSlots - 1;
					}
					if (clickType == ClickType.SHIFT_LEFT) {
						newSlots = newSlots + 10;
					}
					if (clickType == ClickType.SHIFT_RIGHT) {
						newSlots = newSlots - 10;
					}
					if (newSlots >= 0 && newSlots <= 36) {
						sett.invSlots = newSlots;
						gui.setChallGUI(37, Material.STONE_BUTTON, "§6Nutzbare Slots = §c" + sett.invSlots,
								"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10",
								"§7 Shift-Rechtsklick: -10");
						gui.setChallGUI(36, Material.BARRIER, "§6Nutzbare Inventar Slots",
								"§cSpieler können §6" + sett.invSlots + " §cSlots verwenden");
						for (Player pp : Bukkit.getOnlinePlayers()) {
							sett.updateInvSlots(pp);
						}
						settingsChangeString("Nutzbare Slots", "" + newSlots);
					}
					break;
				case 43:
					int newMLGTime = sett.mlgTime;
					if (clickType == ClickType.LEFT) {
						newMLGTime = newMLGTime + 1;
					}
					if (clickType == ClickType.RIGHT) {
						newMLGTime = newMLGTime - 1;
					}
					if (clickType == ClickType.SHIFT_LEFT) {
						newMLGTime = newMLGTime + 10;
					}
					if (clickType == ClickType.SHIFT_RIGHT) {
						newMLGTime = newMLGTime - 10;
					}
					if (newMLGTime >= 0 && newMLGTime <= 36) {
						sett.mlgTime = newMLGTime;
						gui.setChallGUI(43, Material.STONE_BUTTON, "§6Max Minuten bis MLG = §c" + sett.mlgTime,
								"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10",
								"§7 Shift-Rechtsklick: -10");
						settingsChangeString("Max Minuten bis MLG", "" + newMLGTime);
					}
					break;
				case 44:
					sett.mlg = !sett.mlg;
					gui.setChallGUI(44, gui.getMLGBucket(), "§bMLG-Challenge " + gui.getBool(sett.mlg),
							"§cSpieler müssen zufällig", "§ceinen MLG machen");
					chl.checkMLGWorld();
					settingsChange("MLG-Challenge", sett.mlg);
					break;
				default:
					break;
				}
			}
		}

		if (invname.equals("§eCutClean Einstellungen")) {
			e.setCancelled(true);
			if (slot == 31) {
				p.closeInventory();
				p.openInventory(gui.getChallGUI());
			} else if (p.hasPermission("challenges.settings.modify")) {
				switch (slot) {
				case 19:
					sett.cutGravel = !sett.cutGravel;
					gui.setCutGUI(19, gui.getDye(sett.cutGravel), gui.getBool(sett.cutGravel));
					settingsChange("CutClean Feuerstein", sett.cutGravel);
					break;
				case 21:
					sett.cutIron = !sett.cutIron;
					gui.setCutGUI(21, gui.getDye(sett.cutIron), gui.getBool(sett.cutIron));
					settingsChange("CutClean Eisen", sett.cutIron);
					break;
				case 23:
					sett.cutGold = !sett.cutGold;
					gui.setCutGUI(23, gui.getDye(sett.cutGold), gui.getBool(sett.cutGold));
					settingsChange("Cutclean Gold", sett.cutGold);
					break;
				case 25:
					sett.cutFood = !sett.cutFood;
					gui.setCutGUI(25, gui.getDye(sett.cutFood), gui.getBool(sett.cutFood));
					settingsChange("Cutclean Fleisch", sett.cutFood);
					break;
				default:
					break;
				}
			}
		}

		if (invname.equals("§7KillAll Einstellungen")) {
			e.setCancelled(true);
			if (slot == 31) {
				p.closeInventory();
				p.openInventory(gui.getMainGUI());
			} else if (p.hasPermission("challenges.settings.modify")) {
				switch (slot) {
				case 19:
					sett.noFallKillAll = !sett.noFallKillAll;
					gui.setKillGUI(19, gui.getDye(sett.noFallKillAll), gui.getBool(sett.noFallKillAll));
					settingsChange("KillAllonFall", sett.noFallKillAll);
					break;
				case 21:
					sett.mlgKillAll = !sett.mlgKillAll;
					gui.setKillGUI(21, gui.getDye(sett.mlgKillAll), gui.getBool(sett.mlgKillAll));
					settingsChange("KillAllonMLG", sett.mlgKillAll);
					break;
				case 23:
					sett.noBreakKillAll = !sett.noBreakKillAll;
					gui.setKillGUI(23, gui.getDye(sett.noBreakKillAll), gui.getBool(sett.noBreakKillAll));
					settingsChange("KillAllonBreak", sett.noBreakKillAll);
					break;
				case 25:
					sett.noPlaceKillAll = !sett.noPlaceKillAll;
					gui.setKillGUI(25, gui.getDye(sett.noPlaceKillAll), gui.getBool(sett.noPlaceKillAll));
					settingsChange("KillAllonPlace", sett.noPlaceKillAll);
					break;
				default:
					break;
				}
			}
		}

		if (invname.equals("§dLebens Einstellungen")) {
			e.setCancelled(true);
			if (slot == 21) {
				p.closeInventory();
				p.openInventory(gui.getMainGUI());
			} else if (slot == 23) {
				p.performCommand("hp healall");
			} else if (p.hasPermission("challenges.settings.modify")) {
				switch (slot) {
				case 10:
					sett.shareHP = !sett.shareHP;
					gui.setLifeGUI(10, gui.getDye(sett.shareHP), gui.getBool(sett.shareHP));
					settingsChange("Geteilte Herzen", sett.shareHP);
					break;
				case 11:
					sett.respawn = !sett.respawn;
					gui.setLifeGUI(11, gui.getDye(sett.respawn), gui.getBool(sett.respawn));
					settingsChange("Respawn", sett.respawn);
					break;
				case 12:
					sett.oneLife = !sett.oneLife;
					gui.setLifeGUI(12, gui.getDye(sett.oneLife), gui.getBool(sett.oneLife));
					settingsChange("Ein Leben für alle", sett.oneLife);
					break;
				case 14:
					Double newHP = sett.maxHP;
					if (clickType == ClickType.LEFT) {
						newHP = newHP + 1;
					}
					if (clickType == ClickType.RIGHT) {
						newHP = newHP - 1;
					}
					if (clickType == ClickType.SHIFT_LEFT) {
						newHP = newHP + 10;
					}
					if (clickType == ClickType.SHIFT_RIGHT) {
						newHP = newHP - 10;
					}
					if (newHP >= 1 && newHP <= 2048) {
						sett.maxHP = newHP;
						gui.setLifeGUI(14, Material.STONE_BUTTON, "§6Maximale Leben = §c" + sett.maxHP + "§6HP",
								"§7Linksklick: +1", "§7Rechtsklick: -1", "§7 Shift-Linksklick: +10",
								"§7 Shift-Rechtsklick: -10");
						for (Player pp : Bukkit.getOnlinePlayers()) {
							pp.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHP);
							pp.setHealth(newHP);
						}
						settingsChangeString("Maximale HP", newHP.toString());
					}
					break;
				case 15:
					sett.natRegen = !sett.natRegen;
					gui.setLifeGUI(15, gui.getDye(sett.natRegen), gui.getBool(sett.natRegen));
					settingsChange("Natürliche Regeneration", sett.natRegen);
					break;
				case 16:
					sett.regen = !sett.regen;
					gui.setLifeGUI(16, gui.getDye(sett.regen), gui.getBool(sett.regen));
					settingsChange("Jegliche Regeneration", sett.regen);
					break;
				default:
					break;
				}
			}
		}

		if (invname.equals("§aSonstige Einstellungen")) {
			e.setCancelled(true);
			if (slot == 22) {
				p.closeInventory();
				p.openInventory(gui.getMainGUI());
			} else if (p.hasPermission("challenges.settings.modify")) {
				switch (slot) {
				case 10:
					sett.pvp = !sett.pvp;
					gui.setMiscGUI(10, gui.getDye(sett.pvp), gui.getBool(sett.pvp));
					settingsChange("PVP", sett.pvp);
					break;
				case 11:
					sett.tabHP = !sett.tabHP;
					gui.setMiscGUI(11, gui.getDye(sett.tabHP), gui.getBool(sett.tabHP));
					for (Player pp : Bukkit.getOnlinePlayers()) {
						if (sett.tabHP) {
							sb.createScoreboard(pp);
						} else
							sb.removeScoreboard(pp);
					}
					settingsChange("TABHP", sett.tabHP);
					break;
				case 12:
					sett.chatDMG = !sett.chatDMG;
					gui.setMiscGUI(12, gui.getDye(sett.chatDMG), gui.getBool(sett.chatDMG));
					settingsChange("Schaden im Chat", sett.chatDMG);
					break;
				case 13:
					sett.deathPos = !sett.deathPos;
					gui.setMiscGUI(13, gui.getDye(sett.deathPos), gui.getBool(sett.deathPos));
					settingsChange("Zeige Todesposition", sett.deathPos);
					break;
				case 14:
					sett.pauseParticles = !sett.pauseParticles;
					gui.setMiscGUI(14, gui.getDye(sett.pauseParticles), gui.getBool(sett.pauseParticles));
					settingsChange("Enderpartikel", sett.pauseParticles);
					break;
				case 15:
					sett.settingsTitle = !sett.settingsTitle;
					gui.setMiscGUI(15, gui.getDye(sett.settingsTitle), gui.getBool(sett.settingsTitle));
					settingsChange("Einstellungstitel", sett.settingsTitle);
					break;
				case 16:
					sett.keepInv = !sett.keepInv;
					gui.setMiscGUI(16, gui.getDye(sett.keepInv), gui.getBool(sett.keepInv));
					settingsChange("KeepInventory", sett.keepInv);
					break;
				default:
					break;
				}
			}
		}

		if (invname.equals("§6Koordinaten")) {
			e.setCancelled(true);
			if (slot >= 0 && slot <= 44) {
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR
						&& e.getCurrentItem().getType() != Material.BARRIER) {
					gui.newCoInfGUI(p, e.getCurrentItem().getItemMeta().getDisplayName());
				}
			}
		}

		if (invname.equals("§6Koordinaten Info")) {
			e.setCancelled(true);
			if (slot == 8) {
				p.closeInventory();
				p.openInventory(gui.getCoordsGUI());
			} else
				switch (slot) {
				case 0:
					p.closeInventory();
					p.performCommand("coords delete " + e.getInventory().getItem(4).getItemMeta().getDisplayName());
					break;
				case 2:
					p.closeInventory();
					p.performCommand("coords get " + e.getInventory().getItem(4).getItemMeta().getDisplayName());
					break;
				case 6:
					p.closeInventory();
					p.performCommand("coords teleport " + e.getInventory().getItem(4).getItemMeta().getDisplayName());
					break;
				default:
					break;
				}
		}

		if (invname.equals("§6Koordinate löschen")) {
			e.setCancelled(true);
			String posName = e.getInventory().getItem(4).getItemMeta().getDisplayName();
			if (slot == 0) {
				cfg.saveStr("locations." + posName.toLowerCase(), null);
				gui.createCoordsGUI();
				p.closeInventory();
				p.sendMessage("§aKoordinate §6" + posName + " §awurde gelöscht!");
			} else if (slot == 8) {
				p.closeInventory();
				gui.newCoInfGUI(p, posName);
			}
		}
	}

	public void settingsChange(String Setting, Boolean bool) {
		String Wert;
		if (bool) {
			Wert = "§aAktiviert";
		} else
			Wert = "§4Deaktiviert";
		if (sett.settingsTitle) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle("§c" + Setting, " §bist nun: " + Wert, 10, 40, 10);
			}
		}
	}

	public void settingsChangeString(String Setting, String value) {
		if (sett.settingsTitle) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle("§c" + Setting, " §bist nun: " + value, 10, 40, 10);
			}
		}
	}
}