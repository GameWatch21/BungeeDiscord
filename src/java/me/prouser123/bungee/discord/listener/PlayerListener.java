package me.prouser123.bungee.discord.listener;

import me.prouser123.bungee.discord.Discord;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.javacord.api.entity.channel.TextChannel;

public class PlayerListener implements Listener {

    public static TextChannel channel = null;

    public PlayerListener(String id) {
        PlayerListener.channel = Discord.api.getTextChannelById(id).get();
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        channel.sendMessage("`" + event.getPlayer().getName() + "` Has Joined the Network");
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        channel.sendMessage("`" + event.getPlayer().getName() + "` Has Left the Network");
    }
}
