package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import ktar.five.TurfWars.guiapi.menus.items.MenuItem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GoForwardBackMenuItem extends MenuItem
{
	private boolean forward;
	
	public GoForwardBackMenuItem(boolean forward)
	{
		super(ChatColor.GRAY + "<- Go " + (forward ? "To Server Select" : "Back" ), new ItemStack(Material.BED), new String[0]);
		this.forward = forward;
	}
	
	public void onItemClick(ItemClickEvent event)
	{
		 event.setWillClose(true);
	        final String playerName = event.getPlayer().getName();
	        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
	            public void run() {
	                Player p = Bukkit.getPlayerExact(playerName);
	                if (p != null) {
	                	new ServerSelectGui(forward).open(p);
	                }
	            }
	        }, 3);
	}
}
