package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.items.SubMenuItem;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoForwardBackMenuItem extends SubMenuItem
{
	public GoForwardBackMenuItem(ItemMenu gui, boolean forward)
	{
		super(Main.instance, ChatColor.GRAY + "<- Go " + (forward ? "To " + gui.getName() : "Back" ), new ItemStack(Material.BED), gui, new String[0]);
	}
}
