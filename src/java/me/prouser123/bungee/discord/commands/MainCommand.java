package me.prouser123.bungee.discord.commands;

import me.prouser123.bungee.discord.Main;
import me.prouser123.bungee.discord.base.BaseCommand;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.ArrayList;

public class MainCommand implements MessageCreateListener, BaseCommand
{
	public static ArrayList<String> array;
	public static ArrayList<String> subArray;

	public MainCommand() {
		MainCommand.array = new ArrayList<String>();
		MainCommand.subArray = new ArrayList<String>();
		Main.inst().getDebugLogger().info("[MainCommand@Init] Loaded MainCommand and Array");
		Main.inst().getDebugLogger().info("[MainCommand@Init] Arr: " + MainCommand.array);
	}

	@Override
	public void onMessageCreate(final MessageCreateEvent event) {
		if (event.getMessage().getContent().equalsIgnoreCase("!b")) {
			event.getChannel().sendMessage(this.createMainCommandEmbed());
		}
	}

	private EmbedBuilder createMainCommandEmbed() {
		final EmbedBuilder embed = new EmbedBuilder().setTitle("Commands");
		for (final String command : MainCommand.array) {
			final String[] split = command.split(":");
			Main.inst().getDebugLogger().info("[MainCommand@OnMessage] Command: " + split[0] + ", HelpText: " + split[1]);
			embed.addField(split[0], split[1]);
		}
		return embed;
	}
}