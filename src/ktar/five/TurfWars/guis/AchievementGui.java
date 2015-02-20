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
		super("Turf Wars Stats", Size.THREE_LINE, Main.instance);
		this.player = new TurfPlayer(player.getUniqueId());
		initInventory();
		this.player = null;
	}

	public void initInventory() {
		/*
		Winner: Win 1000 matches - Reward: 10,000 gems (money vault)
Killer: Kill 1000 player - Reward: 1000 gems (money vault)
Serial Killer: Kill 10000 player - Reward: 10,000 gems (money vault)
Combo: In one match kill 40 players - Reward: 500 gems (money vault)
God: In one match kill 100 players - Reward: 1250 gems (money vault)
Domination: Win a game in less than 3 minutes - Reward: 300 gems (money vault)
Killstreaks: Kill 20 People in a row without die - Reward: 100 gems (money vault)
Griefer: Destroy 5000 enemies blocks - Reward: 500 gems (money vault)
Robin Hood: Shoot 10000 arrows - Reward: 1000 gems (money vault)
Turf Master: Complete all objectives - Reward: 10,000 gems (money vault)

		 */
		boolean master = true;
		if (player.wins >= 1000) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.wins + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.wins + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.totalKills >= 1000) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.totalKills + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.totalKills + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.totalKills >= 10000) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.totalKills + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.totalKills + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.topKillsPerMatch >= 40) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.topKillsPerMatch + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.topKillsPerMatch + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.topKillsPerMatch >= 100) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.topKillsPerMatch + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.topKillsPerMatch + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.shortestGame <= 180) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.shortestGame + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.shortestGame + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.topKillStreak >= 20) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.topKillStreak + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.topKillStreak + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.blocksDestroyed >= 5000) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.blocksDestroyed + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.blocksDestroyed + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (player.arrowsShot >= 10000) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.arrowsShot + " wins!",
					"You've unlocked this achievement at 1000 wins!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You have " + player.arrowsShot + " wins!",
					"You will unlock this once you reach 1000 wins!"
			}, false));
			master = false;
		}
		if (master) {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You... win."
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Winner", new String[]{
					"You will unlock this once you unlock all other achievements!"
			}, false));
			master = false;
		}
	}
}
