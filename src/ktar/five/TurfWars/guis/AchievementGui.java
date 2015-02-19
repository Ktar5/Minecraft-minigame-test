package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;

import org.bukkit.entity.Player;

public class AchievementGui extends ItemMenu
{
	private TurfPlayer player;
	
	public AchievementGui(Player player)
	{
		super("Turf Wars", Size.SIX_LINE, Main.instance);
		this.player = new TurfPlayer(player.getUniqueId());
		initInventory();
	}

	public void initInventory()	
	{
		this.setItem(4, new GoBackMenuItem(this));
		this.setItem(22, new TurfWarsStatsItem(player));
	}
}
