package me.prouser123.bungee.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class Discord
{
	public static String token;
	public static DiscordApi api;

	public Discord(final String token) {
		Discord.token = token;
		try {
			Discord.api = new DiscordApiBuilder().setToken(token).login().join();
		}
		catch (CompletionException IllegalStateException) {
			Main.inst().getLogger().info("Connection Error. Did you put a valid token in the config?");
		}
	}

	public static String getBotOwner(final MessageCreateEvent event) {
		String bot_owner = "<@";
		try {
			bot_owner += Long.toString(event.getApi().getApplicationInfo().get().getOwnerId());
			bot_owner += ">";
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (ExecutionException e2) {
			e2.printStackTrace();
		}
		return bot_owner;
	}

	static {
		Discord.token = null;
		Discord.api = null;
	}
}