package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.guiapi.menus.items.MenuItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TurfWarsStatsItem extends MenuItem
{
	public TurfWarsStatsItem(String stat, String[] lore, boolean unlocked)
	{
		super(ChatColor.BOLD + stat, unlocked ? new ItemStack(Material.BOOK) : new ItemStack(Material.BEDROCK), lore);
	}
}
