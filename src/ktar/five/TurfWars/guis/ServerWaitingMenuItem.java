package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import ktar.five.TurfWars.guiapi.menus.items.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		ResultSet set;
		try {
			set = Main.sql.querySQL("SELECT playercount,status FROM GameStatus WHERE name=" + serverid);
			while (set.next()){
				if(set.getInt("playercount") >= 16){
					return;
				}
				if(!GameStatus.getById(set.getInt("status")).equals(GameStatus.WAITING_FOR_PLAYERS)){
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
