package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.guiapi.menus.items.MenuItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TurfWarsStatsItem extends MenuItem
{
	public TurfWarsStatsItem(String stat, String[] lore)
	{
		super(ChatColor.BOLD + stat, new ItemStack(Material.BOOK), lore);	
	}
}
