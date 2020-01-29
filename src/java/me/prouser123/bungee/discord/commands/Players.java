package me.prouser123.bungee.discord.commands;

import me.prouser123.bungee.discord.Main;
import me.prouser123.bungee.discord.base.BaseCommand;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Players implements MessageCreateListener, BaseCommand
{
    private base base;

    public Players(final int piority, final String command, final String helpText) {
        this.base = this.easyBaseSetup(piority, command, helpText);
    }

    @Override
    public void onMessageCreate(final MessageCreateEvent event) {
        if (event.getMessage().getContent().equalsIgnoreCase(this.base.command)) {
            final EmbedBuilder embed2 = new EmbedBuilder().setTitle("Online Players").setDescription("All players currently online (in-game)");
            if (Main.inst().getProxy().getPlayers().size() == 0) {
                embed2.setDescription("Uh oh, nobody's online");
                event.getChannel().sendMessage(embed2);
                return;
            }
            final Map<String, List<String>> map = new HashMap<String, List<String>>();
            for (final ServerInfo server : Main.inst().getProxy().getServers().values()) {
                map.put(server.getName(), new ArrayList<String>());
            }
            for (final ProxiedPlayer player : Main.inst().getProxy().getPlayers()) {
                map.get(player.getServer().getInfo().getName()).add(player.getDisplayName());
            }
            for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
                String players = entry.getValue().toString().replace("[", "").replace("]", "");
                if(!players.isEmpty()) {
                    embed2.addField(entry.getKey(), players);
                }
                else {
                    embed2.addField(entry.getKey(), "n/");
                }
            }
            event.getChannel().sendMessage(embed2);
        }
    }
}