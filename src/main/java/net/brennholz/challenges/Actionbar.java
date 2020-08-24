package net.brennholz.challenges;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Actionbar {

	public static void sendActionBarMessage(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
}