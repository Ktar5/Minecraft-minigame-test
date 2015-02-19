package ktar.five.TurfWars.guis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

public class ServerWaitingMenuItem extends MenuItem
{
	private Plugin pl;
	private String serverid;
	
	public ServerWaitingMenuItem(int players, String serverid)
	{
		super(serverid, new ItemStack(Material.EMERALD_BLOCK), new String[] 
				{"",
				 ChatColor.YELLOW + "Players: " + ChatColor.WHITE + players + "/" + (Lobby.players.maxPerTeam * 2),
				 "",
				 ChatColor.UNDERLINE +  "Click to Join"
				 });
		this.serverid = serverid;
		pl = Main.instance;
	}
	
	public void onItemClick(final ItemClickEvent event)
	{
        event.setWillClose(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            public void run() {
                Player p = event.getPlayer();
                if (p != null) {
                	ByteArrayOutputStream b = new ByteArrayOutputStream();
            		DataOutputStream out = new DataOutputStream(b);
            		try
            		{
            			out.writeUTF("Connect");
            			//out.writeUTF(p.getName());
            			out.writeUTF(serverid);
            			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            			DataOutputStream msgout = new DataOutputStream(msgbytes);
            			msgout.writeUTF(Status);

            			out.write(msgbytes.toByteArray());
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
