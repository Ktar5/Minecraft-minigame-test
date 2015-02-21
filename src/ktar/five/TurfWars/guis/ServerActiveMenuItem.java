package ktar.five.TurfWars.guis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Info.GameStatus;
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
				 ChatColor.YELLOW + "Players: " + ChatColor.WHITE + players + "/16",
				 "",
				 ChatColor.YELLOW + "In Progress",
				 });
		this.serverid = serverid;
		pl = Main.instance;
	}

	public void onItemClick(ItemClickEvent event)
	{
		ResultSet set;
		try {
			set = Main.sql.querySQL("SELECT status FROM GameStatus WHERE name='" + serverid+"'");
			while (set.next()){
				if(!GameStatus.getById(set.getInt("status")).equals(GameStatus.IN_PROGRESS)){
					return;
				}
			}
		} catch (SQLException e) {
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		event.setWillClose(true);
		final String playerName = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			public void run() {
				Player p = Bukkit.getPlayerExact(playerName);
				if (p != null) {
					ByteArrayOutputStream b = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(b);
					try
					{
						out.writeUTF("Connect");
						out.writeUTF(serverid);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					p.sendPluginMessage(pl, "BungeeCord", b.toByteArray());
				}
			}
		}, 3);

	}
}