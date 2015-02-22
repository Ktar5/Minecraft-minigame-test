package ktar.five.TurfWars.Game;

import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase;
import ktar.five.TurfWars.Game.Info.WorldManager;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.titleapi.TitleAPI;

public class Game {

	public final String serverID;
	public int totalTime, blockgettercounter;
	public Phase phase;
	public WorldManager worldManager;

	public Game(String serverID) {
		this.serverID = serverID;
	}

	public void start(WorldManager manager) {
		this.totalTime = 0;
		this.blockgettercounter = 0;
		this.worldManager = manager;
		for (TurfPlayer p : Lobby.players.redTeam.values())
			p.getPlayer().teleport(worldManager.redSpawn);
		for (TurfPlayer p : Lobby.players.blueTeam.values())
			p.getPlayer().teleport(worldManager.blueSpawn);
		phase = Phase.startCount;
	}

	public void perSecond() {
		totalTime++;
		if (Lobby.status == GameStatus.STARTING) {
			if (Lobby.seconds == 1) {
				Bukkit.getServer().broadcastMessage("0");
				for (TurfPlayer player : Lobby.players.getAll().values()) {
					player.canMove = false;
				}
			} else if (Lobby.seconds != Phase.startCount.getSeconds()) {
				displayStartGametitlecountdown();
				Bukkit.getServer().broadcastMessage("1");
			} else if(Lobby.seconds == Phase.startCount.getSeconds()) {
				for (TurfPlayer player : Lobby.players.getAll().values()) {
					player.canMove = true;
				}
				Bukkit.getServer().broadcastMessage("2");
				Lobby.seconds = 0;
				displayStartGametitle();
				Lobby.updateStatus(GameStatus.IN_PROGRESS);
				phase = Phase.n1;
				handlePhases();
			}
		} else if (Lobby.status == GameStatus.IN_PROGRESS) {
			if (Lobby.seconds == phase.getSeconds()) {
				phase = Phase.valueOf("n" + (phase.getPhaseNumber() + 1));
				Lobby.seconds = 0;
				handlePhases();//give the items n such
			} else {
				handlePhases();//items n such
			}
		}
	}

	private void displayStartGametitlecountdown() {
		for(TurfPlayer player : Lobby.players.getAll().values())
			TitleAPI.sendTitle(player.getPlayer(), 0, 20, 0, "GAME STARTS IN", (Phase.startCount.getSeconds() - Lobby.seconds) + " SECONDS");
	}

	private void displayStartGametitle() {
		for(TurfPlayer player : Lobby.players.getAll().values())
			TitleAPI.sendTitle(player.getPlayer(), 0, 20, 0, "GAME IS STARTING", "GOOD LUCK");
	}

	private void handlePhases() {
		if (phase.getType() == Phase.PhaseType.BUILDING && Lobby.seconds == 0) {//if it is a build phase starting.
			for (TurfPlayer player : Lobby.players.getAll().values())
				player.canVenture = false;
			for (TurfPlayer player : Lobby.players.blueTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, phase.getAmount(), Team.BLUE.color));
			for (TurfPlayer player : Lobby.players.redTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, phase.getAmount(), Team.RED.color));
		} else if (phase.getType() == Phase.PhaseType.KILLING && Lobby.seconds == 0) {//else if is killing phase starting
			for (TurfPlayer player : Lobby.players.getAll().values()) {
				player.setKitVenturing();
				player.resetInventory();
			}
		} else if (phase.getType() == Phase.PhaseType.KILLING) {
			blockgettercounter++;
			if (blockgettercounter == 6) {
				blockgettercounter = 0;
				for (TurfPlayer player : Lobby.players.blueTeam.values())
					player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 1, Team.BLUE.color));
				for (TurfPlayer player : Lobby.players.redTeam.values())
					player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 1, Team.RED.color));
			}
		}
	}

	public void endGame(Team winning) {
		Lobby.updateStatus(GameStatus.ENDING);
		for (TurfPlayer player : Lobby.players.getTurfPlayers(winning, false).values()) {
			player.addWin(this.totalTime);
			player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
		for (TurfPlayer player : Lobby.players.getTurfPlayers(winning, true).values()) {
			player.addDefeat(this.totalTime);
			player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
		this.worldManager.resetMap();
		Lobby.players.updateDatabase();
	}

	public void playerDied(Player p, String message) {
		p.setHealth(20D);
		p.setFireTicks(0);
		TurfPlayer player = Lobby.players.getAll().get(p.getUniqueId());
		Team team = Lobby.players.getPlayerTeam(player);
		if (team == Team.BLUE) {
			p.teleport(worldManager.blueSpawn);
			worldManager.addClays(Team.RED, phase.getAmount());
		} else if (team == Team.RED) {
			p.teleport(worldManager.redSpawn);
			worldManager.addClays(Team.BLUE, phase.getAmount());
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lDeath>&r &c" + p.getName().toUpperCase() + "&7 was killed by " + message));
		player.addDeath();
		player.resetInventory();
	}
	
}