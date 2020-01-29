package me.prouser123.bungee.discord.base;

import me.prouser123.bungee.discord.Main;
import me.prouser123.bungee.discord.commands.MainCommand;

public interface BaseCommand
{
	public static final String arraySeperator = ":";

	default base createBase() {
		return new base();
	}

	default base easyBaseSetup(final int piority, final String command, final String helpText) {
		final base b = this.createBase();
		b.add(piority, command, helpText);
		this.debugInit(piority, command, helpText, b);
		this.addCommandToHelp(b);
		return b;
	}

	default void addCommandToHelp(final base b) {
		Main.inst().getDebugLogger().info("[BaseCommand@Add2Help] Adding " + b.command);
		MainCommand.array.add(b.helpPriority, b.command + ":" + b.helpText);
		Main.inst().getDebugLogger().info("[BaseCommand@Add2Help] " + MainCommand.array);
	}

	default void debugInit(final int piority, final String command, final String helpText, final base base) {
		Main.inst().getDebugLogger().info("[BaseCommand@debugInit] Init info: " + piority + " | " + command + " | " + helpText);
		Main.inst().getDebugLogger().info("[BaseCommand@debugInit] BASE() info: | " + base.helpPriority + " | " + base.command + " | " + base.helpText);
	}

	public static class base
	{
		public String command;
		public String helpText;
		public int helpPriority;

		public base() {
			this.command = "";
			this.helpText = "";
			this.helpPriority = 0;
		}

		public void add(final int piority, final String command, final String helpText) {
			this.helpPriority = piority;
			this.command = command;
			this.helpText = helpText;
		}
	}
}