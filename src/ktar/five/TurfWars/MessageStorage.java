package ktar.five.TurfWars;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class MessageStorage {

	public static Map<String, String> messages;
	
	public MessageStorage(ConfigurationSection config){
		messages = new HashMap<String, String>();
		for(String key : config.getKeys(false)){
			messages.put(key, ChatColor.translateAlternateColorCodes('&', config.getString(key)));
		}	
	}
	
	public static String get(String key){
		return messages.get(key);
	}
	
}
