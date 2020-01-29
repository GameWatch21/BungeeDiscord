package me.prouser123.bungee.discord;

import java.util.logging.Level;

public class DebugLogger
{
	private boolean debugEnabled;

	public void info(final String message) {
		if (this.debugEnabled) {
			Main.inst().getProxy().getLogger().log(Level.INFO, "[" + Main.inst().getDescription().getName() + ".DEBUG] " + message);
		}
	}

	public DebugLogger() {
		this.debugEnabled = false;
		try {
			Main.inst();
			if (Main.getConfig().getBoolean("debug-enabled")) {
				this.debugEnabled = true;
				Main.inst().getLogger().info("Enabled debug logging.");
			}
		}
		catch (NullPointerException npex) {}
	}
}