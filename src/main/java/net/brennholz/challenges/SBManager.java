package net.brennholz.challenges;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.ScoreboardManager;

public class SBManager {

	public void createScoreboard(Player p) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sb = manager.getNewScoreboard();
		Objective tabHP = sb.registerNewObjective("Healthpoints", "health", "&cHP");
		tabHP.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		tabHP.setDisplayName("§cHP");
		tabHP.setRenderType(RenderType.HEARTS);
		p.setScoreboard(sb);
	}

	public void removeScoreboard(Player p) {
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
