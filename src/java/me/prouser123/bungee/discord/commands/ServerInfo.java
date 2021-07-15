package me.prouser123.bungee.discord.commands;

import me.prouser123.bungee.discord.Main;
import me.prouser123.bungee.discord.base.BaseCommand;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class ServerInfo implements MessageCreateListener, BaseCommand
{
	private base base;

	public ServerInfo(final int piority, final String command, final String helpText) {
		this.base = this.easyBaseSetup(piority, command, helpText);
	}

	@Override
	public void onMessageCreate(final MessageCreateEvent event) {
		if (event.getMessage().getContent().equalsIgnoreCase(this.base.command)) {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd:HH:mm:ss.SSS");
			final String uptime = formatter.format(ManagementFactory.getRuntimeMXBean().getUptime());
			final String[] uptime_split = uptime.split(":");
			final String uptime_days = Integer.toString(Integer.parseInt(uptime_split[0]) - 1);
			final String uptime_hours = Integer.toString(Integer.parseInt(uptime_split[1]) - 1);
			final String uptime_minutes = uptime_split[2];
			final String uptime_seconds_2 = uptime_split[3];
			final String uptime_seconds = uptime_seconds_2.substring(0, uptime_seconds_2.indexOf("."));
			String uptime_output = "";
			if (Integer.parseInt(uptime_days) != 0) {
				uptime_output = uptime_output + uptime_days + "d ";
			}
			if (Integer.parseInt(uptime_hours) + Integer.parseInt(uptime_days) != 0) {
				uptime_output = uptime_output + uptime_hours + "h ";
			}
			if (Integer.parseInt(uptime_minutes) != 0) {
				uptime_output = uptime_output + uptime_minutes + "m ";
			}
			uptime_output = uptime_output + uptime_seconds + "s";
			String bot_owner = "<@";
			try {
				bot_owner += Long.toString(event.getApi().getApplicationInfo().get().getOwnerId());
				bot_owner += ">";
			}
			catch (InterruptedException | ExecutionException ex2) {
				final Exception e = null;
				assert false;
				e.printStackTrace();
			}
			final EmbedBuilder embed = new EmbedBuilder().setAuthor(Main.getConfig().getString("server-name") + " Server Information")
					.addInlineField("Players", Main.inst().getProxy().getPlayers().size() + "/" + "50")
                                        .addInlineField("Server Owner", <@525984207567061007>)
                                        .addInlineField("Server Co-Owner", bot_owner)
					.addInlineField("Uptime", uptime_output)
					.addInlineField("Memory", Runtime.getRuntime().freeMemory() / 1024L / 1024L + "/" + Runtime.getRuntime().totalMemory() / 1024L / 1024L + " MB free")
					.addInlineField("Servers", Integer.toString(Main.inst().getProxy().getServers().size()))
					.addInlineField("Server Versions", Main.inst().getProxy().getGameVersion())
					.addInlineField("Bot Owner", bot_owner)
					.addInlineField("Server Version", System.getProperty("os.name") + ", " + Main.inst().getProxy().getVersion());
			event.getChannel().sendMessage(embed);
		}
	}
}
