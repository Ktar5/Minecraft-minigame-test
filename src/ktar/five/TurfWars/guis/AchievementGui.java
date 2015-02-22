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
		super("Turf Wars Stats", Size.FIVE_LINE, Main.instance);
		this.player = new TurfPlayer(player.getUniqueId());
		initInventory();
		this.player = null;
	}

	public void initInventory() {
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
			this.setItem(2, new TurfWarsStatsItem("Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You've unlocked this achievement at 1000 kills!"
			}, true));
		} else {
			this.setItem(2, new TurfWarsStatsItem("Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You will unlock this once you reach 1000 kills!"
			}, false));
			master = false;
		}
		if (player.totalKills >= 10000) {
			this.setItem(4, new TurfWarsStatsItem("Serial Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You've unlocked this achievement at 10000 kills!"
			}, true));
		} else {
			this.setItem(4, new TurfWarsStatsItem("Serial Killer", new String[]{
					"You have " + player.totalKills + " kills!",
					"You will unlock this once you reach 10000 kills!"
			}, false));
			master = false;
		}
		if (player.topKillsPerMatch >= 40) {
			this.setItem(6, new TurfWarsStatsItem("Combo", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You've unlocked this achievement at 40 kills!"
			}, true));
		} else {
			this.setItem(6, new TurfWarsStatsItem("Combo", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You will unlock this once you reach 40 kills!"
			}, false));
			master = false;
		}
		if (player.topKillsPerMatch >= 100) {
			this.setItem(8, new TurfWarsStatsItem("God", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You've unlocked this achievement at 100 kills!"
			}, true));
		} else {
			this.setItem(8, new TurfWarsStatsItem("God", new String[]{
					"You have " + player.topKillsPerMatch + " kills!",
					"You will unlock this once you reach 100 kills!"
			}, false));
			master = false;
		}
		if (player.shortestGame <= 180) {
			this.setItem(10, new TurfWarsStatsItem("Domination", new String[]{
					"You're shortest game: " + player.shortestGame + " seconds!",
					"You've unlocked this by winning a game in under 3 minutes!"
			}, true));
		} else {
			this.setItem(10, new TurfWarsStatsItem("Domination", new String[]{
					"You're shortest game: " + player.shortestGame + " seconds!",
					"You will unlock this by winning a game in under 3 minutes!"
			}, false));
			master = false;
		}
		if (player.topKillStreak >= 20) {
			this.setItem(12, new TurfWarsStatsItem("Killstreaks", new String[]{
					"You have killed " + player.topKillStreak + " players without dying!",
					"You've unlocked this achievement by killing 20 players without dying!"
			}, true));
		} else {
			this.setItem(12, new TurfWarsStatsItem("Killstreaks", new String[]{
					"You have killed " + player.topKillStreak + " players without dying!",
					"You will unlock this once you kill 20 players without dying!"
			}, false));
			master = false;
		}
		if (player.blocksDestroyed >= 5000) {
			this.setItem(14, new TurfWarsStatsItem("Griefer", new String[]{
					"You have destroyed " + player.blocksDestroyed + " blocks!",
					"You've unlocked this achievement by destroying 5000 enemy blocks!"
			}, true));
		} else {
			this.setItem(14, new TurfWarsStatsItem("Griefer", new String[]{
					"You have destroyed " + player.blocksDestroyed + " blocks!",
					"You will unlock this once you destroy 5000 enemy blocks!"
			}, false));
			master = false;
		}
		if (player.arrowsShot >= 10000) {
			this.setItem(16, new TurfWarsStatsItem("Robin Hood", new String[]{
					"You have shot " + player.arrowsShot + " arrows!",
					"You've unlocked this achievement by shooting 1000 arrows!"
			}, true));
		} else {
			this.setItem(16, new TurfWarsStatsItem("Robin Hood", new String[]{
					"You have shot " + player.arrowsShot + " arrows!",
					"You will unlock this once you shoot 1000 arrows!"
			}, false));
			master = false;
		}
		if (master) {
			this.setItem(22, new TurfWarsStatsItem("Turf Master", new String[]{
					"You... win."
			}, true));
		} else {
			this.setItem(22, new TurfWarsStatsItem("Turf Master", new String[]{
					"You will unlock this once you unlock all other achievements!"
			}, false));
			master = false;
		}

		this.setItem(28, new TurfWarsStatsItem("Kills", new String[]{
				String.valueOf(player.totalKills)
		}, true));
		this.setItem(29, new TurfWarsStatsItem("Deaths", new String[]{
				String.valueOf(player.totalDeaths)
		}, true));
		this.setItem(30, new TurfWarsStatsItem("KDR", new String[]{
				String.valueOf(player.totalKills/(player.totalDeaths + 1))
		}, true));
		this.setItem(32, new TurfWarsStatsItem("Most Kills In A Game", new String[]{
				String.valueOf(player.topKillsPerMatch)
		}, true));
		this.setItem(33, new TurfWarsStatsItem("Top Kill Streak", new String[]{
				String.valueOf(player.topKillStreak)
		}, true));
		this.setItem(34, new TurfWarsStatsItem("Average Kills Per Game", new String[]{
				String.valueOf(player.totalKills/(player.wins + player.defeats + 1))
		}, true));
		this.setItem(36, new TurfWarsStatsItem("Average Deaths Per Game", new String[]{
				String.valueOf(player.totalDeaths/(player.wins + player.defeats + 1))
		}, true));
		this.setItem(37, new TurfWarsStatsItem("Arrow Accuracy", new String[]{
				String.valueOf(player.totalKills/(player.arrowsShot + 1))
		}, true));
		this.setItem(38, new TurfWarsStatsItem("Arrows shot", new String[]{
				String.valueOf(player.arrowsShot)
		}, true));
		this.setItem(39, new TurfWarsStatsItem("Blocks Broken", new String[]{
				String.valueOf(player.blocksDestroyed)
		}, true));
		this.setItem(40, new TurfWarsStatsItem("Blocks Placed", new String[]{
				String.valueOf(player.blocksPlaced)
		}, true));
		this.setItem(41, new TurfWarsStatsItem("Game Wins", new String[]{
				String.valueOf(player.wins)
		}, true));
		this.setItem(42, new TurfWarsStatsItem("Game Defeats", new String[]{
				String.valueOf(player.defeats)
		}, true));
		this.setItem(43, new TurfWarsStatsItem("Longest Game", new String[]{
				String.valueOf(player.longestGame)
		}, true));
		this.setItem(44, new TurfWarsStatsItem("Shortest Game", new String[]{
				String.valueOf(player.shortestGame)
		}, true));




	}
}
