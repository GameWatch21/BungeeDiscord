package me.prouser123.bungee.discord;

import com.google.common.io.ByteStreams;
import me.prouser123.bungee.discord.commands.MainCommand;
import me.prouser123.bungee.discord.commands.Players;
import me.prouser123.bungee.discord.commands.ServerInfo;
import me.prouser123.bungee.discord.listener.PlayerListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.javacord.api.entity.activity.ActivityType;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Plugin
{
	private static Main instance;
	private static Configuration configuration;
	private static Configuration botCommandConfiguration;
	private static DebugLogger debugLogger;
	Timer timer;

	public Main() {
		this.timer = new Timer();
	}

	public static Main inst() {
		return Main.instance;
	}

	public static Configuration getConfig() {
		return Main.configuration;
	}

	public static Configuration getConfigBotCommand() {
		return Main.botCommandConfiguration;
	}

	public DebugLogger getDebugLogger() {
		return Main.debugLogger;
	}

	public void onEnable() {
		loadResource(Main.instance = this, "config.yml");
		try {
			Main.configuration = ConfigurationProvider.getProvider((Class)YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
		}
		catch (IOException e) {
			this.getLogger().severe("Error loading config.yml");
		}
		loadResource(this, "bot-command-options.yml");
		try {
			Main.botCommandConfiguration = ConfigurationProvider.getProvider((Class)YamlConfiguration.class).load(new File(this.getDataFolder(), "bot-command-options.yml"));
		}
		catch (IOException e) {
			this.getLogger().severe("Error loading bot-command-options.yml");
		}
		Main.debugLogger = new DebugLogger();
		new Discord(getConfig().getString("token"));
		Discord.api.setMessageCacheSize(10, 3600);
		registerListeners.botCommands();
		// Register Bungee Player Join/Leave Listeners
		Main.registerListeners.playerJoinLeave();

		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (Main.inst().getProxy().getPlayers().size() == 1) {
					Discord.api.updateActivity(ActivityType.PLAYING, "with " + Main.inst().getProxy().getPlayers().size() + " player");
				}
				else {
					Discord.api.updateActivity(ActivityType.PLAYING, "with " + Main.inst().getProxy().getPlayers().size() + " players");
				}
			}
		}, 0L, 120000L);
	}

	public static void loadResource(final Plugin plugin, final String resource) {
		final File folder = plugin.getDataFolder();
		if (!folder.exists()) {
			folder.mkdir();
		}
		final File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (final InputStream in = plugin.getResourceAsStream(resource);
					 final OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDisable() {
		this.timer.cancel();
		if (Discord.api != null) {
			Discord.api.disconnect();
		}
	}

	private static class registerListeners
	{
		private static void playerJoinLeave() {
			// Register Bungee Player Join/Leave Listeners
			String jlcID = getConfig().getString("join-leave-chat-id");

			try {
				Main.inst().getProxy().getPluginManager().registerListener(Main.inst(), new PlayerListener(jlcID));
				Main.inst().getLogger().info("Join Leave Chat enabled for channel: #" + Discord.api.getChannelById(jlcID).toString().replaceAll(".*\\[|\\].*", "") + " (id: " + jlcID + ")");
			} catch (NoSuchElementException e) {

				Main.inst().getLogger().info("Join Leave Chat disabled. Did you put a valid channel ID in the config?");
			}
		}

		private static void botCommands() {
			Main.inst().getLogger().info("Registering commands...");
			Discord.api.addMessageCreateListener(new MainCommand());
			if (Main.getConfigBotCommand().contains("server-info")) {
				Discord.api.addMessageCreateListener(new ServerInfo(0, Main.getConfigBotCommand().getString("server-info.command"), Main.getConfigBotCommand().getString("server-info.description")));
			}
			else {
				Main.inst().getLogger().warning("[Bot Command Options] Missing the server-info path. You will not be able to customize the !serverinfo command.");
				Discord.api.addMessageCreateListener(new ServerInfo(0, "!serverinfo", "OldCraft server information"));
			}
			if (Main.getConfigBotCommand().contains("players")) {
				Discord.api.addMessageCreateListener(new Players(1, Main.getConfigBotCommand().getString("players.command"), Main.getConfigBotCommand().getString("players.description")));
			}
			else {
				Main.inst().getLogger().warning("[Bot Command Options] Missing the players path. You will not be able to customize the !players command.");
				Discord.api.addMessageCreateListener(new Players(2, "!players", "Show veryone currently online in-game"));
			}
		}
	}
}