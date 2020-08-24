package net.brennholz.challenges;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer_Command implements CommandExecutor {

	private Challenges chl = Challenges.getplugin();
	private Timer_Command timer = chl.getTimer();
	private SettingsManager sett = chl.getSM();
	private ConfigManager cfg = chl.getcfg();

	public static boolean active;
	public static boolean reversed;
	public int time;
	public int sek;
	public String ssek;
	public int min;
	public String smin;
	public int hrs;
	public int tsincemlg = 0;
	public int rndmlg;
	public static int mlgCount = 0;
	public static boolean mlg_active = false;
	public Random random = new Random();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender.hasPermission("challenges.timer")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("resume")) {
					chl.checkMLGWorld();
					if (!timer.isActive()) {
						timer.setActive(true);
						Bukkit.broadcastMessage("§aDer Timer wird fortgesetzt!");
					} else
						sender.sendMessage("§cDer Timer läuft bereits!");
				} else if (args[0].equalsIgnoreCase("pause")) {
					if (timer.isActive()) {
						timer.setActive(false);
						Bukkit.broadcastMessage("§6Der Timer wurde angehalten");
					} else
						sender.sendMessage("§cDer Timer ist bereits pausiert!");
				} else if (args[0].equalsIgnoreCase("reset")) {
					timer.setTime(0);
					Bukkit.broadcastMessage("§cDer Timer wurde zurückgesetzt!");
				} else if (args[0].equalsIgnoreCase("reverse")) {
					timer.setReversed(!timer.isReversed());
					if (timer.isReversed()) {
						Bukkit.broadcastMessage("§5Der Timer läuft nun rückwärts!");
					} else
						Bukkit.broadcastMessage("§9Der Timer läuft nun vorwärts!");
				} else if (args[0].equalsIgnoreCase("forcemlg")) {
					chl.forceLoadMLGWorld();
					if (!isMLG()) {
						setMLG(true);
						setCounter(getCounter() + 1);
						Bukkit.broadcastMessage("§bZeit für einen MLG! §6Viel Glück!");
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.getGameMode() == GameMode.SURVIVAL && !p.isDead()) {
								tpMLG(p);
							}
						}
						mlgReset();
					} else
						sender.sendMessage("§cEin MLG findet bereits statt!");
				} else

					sender.sendMessage("§cBenutze: /timer resume/pause/reset/reverse/set [Zeit in Sekunden]");
			} else if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("set")) {
					timer.setTime(Integer.parseInt(args[1]));
				} else
					sender.sendMessage("§cBenutze: /timer resume/pause/reset/reverse/set [Zeit in Sekunden]");
			} else {
				sender.sendMessage("§c~~~~~ §6Timer command §c~~~~~");
				sender.sendMessage("§c/timer resume §4- §6Setze den Timer fort");
				sender.sendMessage("§c/timer pause §4- §6Pausiere den Timer");
				sender.sendMessage("§c/timer reset §4- §6Setze den Timer zurück");
				sender.sendMessage("§c/timer set [Zeit in Sekunden] §4- §6Setze den Timer auf eine bestimmte Zeit");
				sender.sendMessage("§c/timer reverse §4- §6Ändere ob der Timer vor- oder rückwärts läuft");
			}
		} else
			sender.sendMessage("§cDu hast hierfür keine Berechtigung");
		return true;

	}

	public void loadTimer() {
		setActive(false);
		setReversed(cfg.getBool("timer.reversed"));
		time = cfg.getInt("timer.time");
		rndmlg = random.nextInt((sett.mlgTime * 60) + 20);
		updateStrings();
		startTimer();
	}

	public void saveTimer() {
		cfg.saveInt("timer.time", time);
		cfg.saveBool("timer.reversed", reversed);
	}

	public void setTime(int t) {
		setActive(false);
		time = t;
		updateStrings();
		Bukkit.broadcastMessage("§bDer Timer wurde auf §a" + hrs + ":" + smin + ":" + ssek + " §bgesetzt!");
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean newActive) {
		active = newActive;
	}

	public boolean isReversed() {
		return reversed;
	}

	public void setReversed(boolean reverse) {
		reversed = reverse;
	}

	public boolean isMLG() {
		return mlg_active;
	}

	public void setMLG(boolean mlg) {
		mlg_active = mlg;
	}

	public void setCounter(int count) {
		mlgCount = count;
	}

	public int getCounter() {
		return mlgCount;
	}

	public void startTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (isActive()) {
					tsincemlg++;
					if (sett.mlg && !mlg_active && Bukkit.getOnlinePlayers().size() >= 1 && tsincemlg >= rndmlg) {
						Bukkit.broadcastMessage("§bZeit für einen MLG! §6Viel Glück!");
						mlgCount++;
						mlg_active = true;
						tsincemlg = 0;
						rndmlg = random.nextInt((sett.mlgTime * 60) + 20);
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.getGameMode() == GameMode.SURVIVAL & !p.isDead()) {
								tpMLG(p);
							}
						}
						mlgReset();
					}

					if (isReversed()) {
						if (time <= 0) {
							setActive(false);
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.setGameMode(GameMode.SPECTATOR);
								p.sendTitle("§4ZEIT ABGELAUFEN!", "§cChallenge fehgeschlagen", 10, 60, 10);
							}
							Bukkit.broadcastMessage(
									"§4Die Zeit ist abgelaufen und die Challenge somit fehlgeschlagen!");
							Bukkit.broadcastMessage("§aSeed: §b" + Bukkit.getWorlds().get(0).getSeed());
						} else {
							time--;
							updateStrings();
						}
						for (Player p : Bukkit.getOnlinePlayers()) {
							Actionbar.sendActionBarMessage(p,
									"§aVerbleibende Zeit: §b" + hrs + ":" + smin + ":" + ssek);
						}
					} else {
						time++;
						updateStrings();
						for (Player p : Bukkit.getOnlinePlayers()) {
							Actionbar.sendActionBarMessage(p, "§aIn Challenge: §b" + hrs + ":" + smin + ":" + ssek);
						}
					}

				} else
					for (Player p : Bukkit.getOnlinePlayers()) {
						Actionbar.sendActionBarMessage(p, "§6Timer pausiert...");
						if (sett.pauseParticles && p.getGameMode() != GameMode.SPECTATOR) {
							p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1);
						}
					}
			}
		}.runTaskTimer(chl, 0L, 20L);
	}

	public void updateStrings() {
		min = time / 60;
		hrs = min / 60;
		min = min % 60;
		sek = time % 60;
		if (sek >= 10) {
			ssek = Integer.toString(sek);
		} else
			ssek = '0' + Integer.toString(sek);
		if (min >= 10) {
			smin = Integer.toString(min);
		} else
			smin = '0' + Integer.toString(min);
	}

	public void tpMLG(Player p) {
		p.closeInventory();
		int rndY = random.nextInt(20);
		Location loc = p.getLocation();
		Location mlgLoc = p.getLocation();
		mlgLoc.setWorld(Bukkit.getWorld("mlg_challenge"));
		mlgLoc.setY(50 + rndY);
		mlgLoc.getWorld().loadChunk(mlgLoc.getBlockX(), mlgLoc.getBlockZ());
		Inventory inv = Bukkit.createInventory(p, InventoryType.PLAYER, p.getName());
		inv.setContents(p.getInventory().getContents());
		p.getInventory().clear();
		p.getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
		p.teleport(mlgLoc);
		mlgResult(p, loc, inv);
	}

	public void mlgResult(Player p, Location loc, Inventory inv) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(chl, new Runnable() {

			@Override
			public void run() {
				if (!p.isDead() && chl.isMLGWorld(p)) {
					if (isActive()) {
						p.sendMessage("§aDu hast den §bMLG §aerfolgreich absolviert! §6Weitere folgen...");
						p.setInvulnerable(true);
						mlgBack(p, loc, inv);
					} else
						mlgBack(p, loc, inv);
				} else if (sett.keepInv) {
					p.getInventory().setContents(inv.getContents());
				} else
					for (ItemStack i : inv.getContents()) {
						if (i != null) {
							loc.getWorld().dropItem(loc, i);
						}
					}
			}
		}, 120);
	}

	public void mlgBack(Player p, Location loc, Inventory inv) {
		p.teleport(loc);
		p.getInventory().setContents(inv.getContents());
		Bukkit.getScheduler().scheduleSyncDelayedTask(chl, new Runnable() {

			@Override
			public void run() {
				p.setInvulnerable(false);
			}
		}, 60);
	}

	public void mlgReset() {
		new BukkitRunnable() {

			@Override
			public void run() {
				setMLG(false);
				Bukkit.broadcastMessage("§aDas war der §6" + getCounter() + ". §bMLG§a!");
			}
		}.runTaskLater(chl, 180);
	}
}