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
			this.setItem(0, new TurfWarsStatsItem("Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You've unlocked this achievement at 1000 kills!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You will unlock this once you reach 1000 kills!"
			}, false));
			master = false;
		}
		if (player.totalKills >= 10000) {
			this.setItem(0, new TurfWarsStatsItem("Serial Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You've unlocked this achievement at 10000 kills!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Serial Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You will unlock this once you reach 10000 kills!"
			}, false));
			master = false;
		}
		if (player.topKillsPerMatch >= 40) {
			this.setItem(0, new TurfWarsStatsItem("Combo", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You've unlocked this achievement at 40 kills!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Combo", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You will unlock this once you reach 40 kills!"
			}, false));
			master = false;
		}
		if (player.topKillsPerMatch >= 100) {
			this.setItem(0, new TurfWarsStatsItem("God", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You've unlocked this achievement at 100 kills!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("God", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You will unlock this once you reach 100 kills!"
			}, false));
			master = false;
		}
		if (player.shortestGame <= 180) {
			this.setItem(0, new TurfWarsStatsItem("Domination", new String[]{
					"You're shortest game: " + player.shortestGame + " seconds!",
					"You've unlocked this by winning a game in under 3 minutes!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Domination", new String[]{
					"You're shortest game: " + player.shortestGame + " seconds!",
					"You will unlock this by winning a game in under 3 minutes!"
			}, false));
			master = false;
		}
		if (player.topKillStreak >= 20) {
			this.setItem(0, new TurfWarsStatsItem("Killstreaks", new String[]{
					"You have killed " + player.topKillStreak + " players without dying!",
					"You've unlocked this achievement by killing 20 players without dying!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Killstreaks", new String[]{
					"You have killed " + player.topKillStreak + " players without dying!",
					"You will unlock this once you kill 20 players without dying!"
			}, false));
			master = false;
		}
		if (player.blocksDestroyed >= 5000) {
			this.setItem(0, new TurfWarsStatsItem("Griefer", new String[]{
					"You have destroyed " + player.blocksDestroyed + " blocks!",
					"You've unlocked this achievement by destroying 5000 enemy blocks!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Griefer", new String[]{
					"You have destroyed " + player.blocksDestroyed + " blocks!",
					"You will unlock this once you destroy 5000 enemy blocks!"
			}, false));
			master = false;
		}
		if (player.arrowsShot >= 10000) {
			this.setItem(0, new TurfWarsStatsItem("Robin Hood", new String[]{
					"You have shot " + player.arrowsShot + " arrows!",
					"You've unlocked this achievement by shooting 1000 arrows!"
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Robin Hood", new String[]{
					"You have shot " + player.arrowsShot + " arrows!",
					"You will unlock this once you shoot 1000 arrows!"
			}, false));
			master = false;
		}
		if (master) {
			this.setItem(0, new TurfWarsStatsItem("Turf Master", new String[]{
					"You... win."
			}, true));
		} else {
			this.setItem(0, new TurfWarsStatsItem("Turf Master", new String[]{
					"You will unlock this once you unlock all other achievements!"
			}, false));
			master = false;
		}
	}
}
