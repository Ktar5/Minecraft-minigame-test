package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import ktar.five.TurfWars.guiapi.menus.items.MenuItem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ServerActiveMenuItem extends MenuItem
{
	private Plugin pl;
	private String serverid;
	
	public ServerActiveMenuItem(int players, String serverid)
	{
		super(serverid, new ItemStack(Material.EMERALD_BLOCK), new String[] 
				{"",
				 ChatColor.YELLOW + "Players: " + ChatColor.WHITE + players + "/" + (Lobby.players.maxPerTeam * 2),
				 "",
				 ChatColor.YELLOW + "In Progress",
				 });
		this.serverid = serverid;
		pl = Main.instance;
	}

	public void onItemClick(ItemClickEvent event)
	{
        event.setWillClose(true);
        final String playerName = event.getPlayer().getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            public void run() {
                Player p = Bukkit.getPlayerExact(playerName);
                if (p != null) {
                	
                }
            }
        }, 3);

	}
}