package ktar.five.TurfWars.Game;

import ktar.five.TurfWars.MessageStorage;
import ktar.five.TurfWars.Game.Cooling.Cooldown;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase;
import ktar.five.TurfWars.Game.Info.Phase.PhaseType;
import ktar.five.TurfWars.Game.Info.WorldManager;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

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
		for (TurfPlayer p : Lobby.players.redTeam.values()){
			p.getPlayer().teleport(worldManager.redSpawn);
			p.resetInventory();
		}
		for (TurfPlayer p : Lobby.players.blueTeam.values()){
			p.getPlayer().teleport(worldManager.blueSpawn);
			p.resetInventory();
		}
		phase = Phase.startCount;
	}

	public void perSecond() {
		totalTime++;
		if (Lobby.status == GameStatus.STARTING) {
			if (Lobby.seconds == 0) {
				displayStartGametitlecountdown();
			} else if (Lobby.seconds != Phase.startCount.getSeconds()) {
				displayStartGametitlecountdown();
			} else if(Lobby.seconds == Phase.startCount.getSeconds()) {
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
		int n = (Phase.startCount.getSeconds() - Lobby.seconds);
		for(TurfPlayer player : Lobby.players.getAll().values()){
			TitleAPI.sendTitle(player.getPlayer(), 0, 19, 0,
					MessageStorage.get("countdown").replaceAll("<seconds>", String.valueOf(n)), 
					MessageStorage.get("countdown").replaceAll("<seconds>", String.valueOf(n)));
		player.canMove = false;
		}
	}

	private void displayStartGametitle() {
		for(TurfPlayer player : Lobby.players.getAll().values()){
			TitleAPI.sendTitle(player.getPlayer(), 0, 19, 0, MessageStorage.get("startGame"), MessageStorage.get("startGameSub"));
		player.canMove = true;
		}
	}

	private void handlePhases() {
		if (phase.getType() == Phase.PhaseType.BUILDING && Lobby.seconds == 0) {//if it is a build phase starting.
			for (TurfPlayer player : Lobby.players.getAll().values())
				player.canVenture = false;
			for (TurfPlayer player : Lobby.players.blueTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.WOOL, phase.getAmount(), Team.BLUE.color));
			for (TurfPlayer player : Lobby.players.redTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.WOOL, phase.getAmount(), Team.RED.color));
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
		if(Cooldown.isCooling(p.getUniqueId(), "death")){
			return;
		}else{
			Cooldown.add(p.getUniqueId(), "death", 5);
		}
		p.setHealth(20D);
		p.setFireTicks(0);
		TurfPlayer player = Lobby.players.getAll().get(p.getUniqueId());
		Team team = Lobby.players.getPlayerTeam(player);
		
		int n = phase.getType() == PhaseType.BUILDING ? 1 : phase.getAmount();
		if (team == Team.BLUE) {
			p.teleport(worldManager.blueSpawn);
			worldManager.addClays(Team.RED, n);
		} else if (team == Team.RED) {
			p.teleport(worldManager.redSpawn);
			worldManager.addClays(Team.BLUE, n);
		}
		p.sendMessage(MessageStorage.get("deathFormat").replaceAll("<type>", message).replaceAll("<player>",
						team.equals(Team.RED) ? "&4" : "&b") + p.getName().toUpperCase());
		player.addDeath();
		player.resetInventory();
		p.removePotionEffect(PotionEffectType.SLOW);
	}
	
}
