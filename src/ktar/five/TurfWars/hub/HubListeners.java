package ktar.five.TurfWars.hub;

import ktar.five.TurfWars.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HubListeners implements Listener{
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event){
		if(Main.economy.hasAccount(event.getPlayer().getPlayer()) == false){
			Main.economy.createPlayerAccount(event.getPlayer().getPlayer());
		}
		Main.economy.depositPlayer(event.getPlayer().getPlayer(), 
				Main.getPlayerBalance(event.getPlayer().getUniqueId()) 
				- Main.economy.getBalance(event.getPlayer().getPlayer()));
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		if(Main.economy.hasAccount(event.getPlayer().getPlayer()) == false){
			Main.economy.createPlayerAccount(event.getPlayer().getPlayer());
		}
		int n = (int) (Main.getPlayerBalance(event.getPlayer().getUniqueId()) - Main.economy.getBalance(event.getPlayer().getPlayer()));
		if(n > 0){
			Main.economy.depositPlayer(event.getPlayer().getPlayer(), 
					Main.getPlayerBalance(event.getPlayer().getUniqueId()) 
					- Main.economy.getBalance(event.getPlayer().getPlayer()));
		}else if( n < 0){
			Main.economy.withdrawPlayer(event.getPlayer().getPlayer(), 
					Main.getPlayerBalance(event.getPlayer().getUniqueId()) 
					- Main.economy.getBalance(event.getPlayer().getPlayer()));
		}
	}
	
	
}
