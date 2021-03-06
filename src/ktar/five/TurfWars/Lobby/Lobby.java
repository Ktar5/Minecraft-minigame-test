package ktar.five.TurfWars.Lobby;

import java.util.ArrayList;
import java.util.List;

import ktar.five.TurfWars.GenericUtils;
import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.MessageStorage;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Cooling.Cooldown;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.WorldManager;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Game.Scoreboard.Scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.connorlinfoot.titleapi.TitleAPI;

public class Lobby implements Listener{

	private static Game game;	
	public static WorldManager info;
	public static GameStatus status;
	public static int seconds;
	public static int lobbyCountdown = 50;
	public static GamePlayers players;
	public static String serverid;

	public Lobby(FileConfiguration config){
		players = new GamePlayers();
		seconds = 0;
		serverid = config.getString("serverid");
		World world = Bukkit.getWorld(config.getString("lobbyOptions.world"));
		ConfigurationSection locations = config.getConfigurationSection("lobbyOptions.locations");
		LobbyUtils.spawnMobs(GenericUtils.configToLocation(locations.getConfigurationSection("blueSheep"), world),
				GenericUtils.configToLocation(locations.getConfigurationSection("redSheep"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("marksman"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("infiltrator"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("shredder"), world));
		List<Location> fireworks = new ArrayList<>();
		for(String section : locations.getConfigurationSection("fireworks").getKeys(false)){
			fireworks.add(GenericUtils.configToLocation(locations.getConfigurationSection("fireworks." + section), world));
		}
		info = new WorldManager(GenericUtils.configToLocation(locations.getConfigurationSection("lobbySpawn"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("redSpawn"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("blueSpawn"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("boundaryOne"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("boundaryTwo"), world),
				fireworks,
				GenericUtils.configToLocation(locations.getConfigurationSection("winning"), world),
				GenericUtils.configToLocation(locations.getConfigurationSection("loosing"), world));
		status = GameStatus.WAITING_FOR_PLAYERS;
		game = new Game(serverid);
		Main.updateGameStatus();
		this.createTimer();
	}
	
	private void createTimer() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
			@Override
			public void run() {
				perSecond();
				Cooldown.handleCooldowns();
			}
		}, 0L, 20L);
	}
    
    public void perSecond() {
        seconds++;
		if(status == GameStatus.LOBBY_COUNTDOWN || status == GameStatus.WAITING_FOR_PLAYERS){
			for(TurfPlayer p : players.getAll().values()){
				Scoreboards.getLobbyScoreboard(p);
			}
		}else if(status == GameStatus.IN_PROGRESS || status == GameStatus.STARTING){
			for(TurfPlayer p : players.getAll().values()){
				Scoreboards.getGameScoreboard(p);
			}
		}
        if (status == GameStatus.WAITING_FOR_PLAYERS && players.hasEnoughPlayers()) {
            seconds = 0;
            updateStatus(GameStatus.LOBBY_COUNTDOWN);
        } else if (status == GameStatus.LOBBY_COUNTDOWN) {
            if (!players.hasEnoughPlayers()) {
                seconds = 0;
                updateStatus(GameStatus.WAITING_FOR_PLAYERS);
            } else if (players.hasEnoughPlayers() && seconds == lobbyCountdown) {
                this.startGame();
                seconds = 0;
                updateStatus(GameStatus.STARTING);
                game.perSecond();
            }
        } else if (status == GameStatus.STARTING || status == GameStatus.IN_PROGRESS){
        	game.perSecond();
        } else if (status == GameStatus.ENDING) {
			if (seconds == 30) {
				updateStatus(GameStatus.RESTARTING);
				for (TurfPlayer player : players.getAll().values()) {
					player.returnToHub();
				}
				endGame(info.getWinning());
				players.clear();
			}else{
				LobbyUtils.playFireworks();
			}

		}
    }

	private void startGame(){
    	game.start(info);
    }
    
    private static void endGame(Team team){
    	game.endGame(team);
		game = new Game(serverid);
		status = GameStatus.WAITING_FOR_PLAYERS;
		seconds = 0;
    }

	public static void teamWon(Team team){
		updateStatus(GameStatus.ENDING);
		for(TurfPlayer player : players.getTurfPlayers(team, false).values()){
			Player p = player.getPlayer();
			p.getInventory().clear();
			p.setHealth(20d);

			player.canMove = false;
			player.canVenture = true;
			displayWinTitle(p,team);
			p.teleport(info.winning);
		}
		for(TurfPlayer player : players.getTurfPlayers(team, true).values()){
			Player p = player.getPlayer();
			p.getInventory().clear();
			p.setHealth(20d);

			player.canMove = false;
			player.canVenture = true;
			displayLooseTitle(p,team);
			p.teleport(info.loosing);
		}
		seconds = 0;
	}

	public static void displayWinTitle(Player p, Team win){
		TitleAPI.sendTitle(p, 20, 100, 10,
				convertToEndGameTitle(MessageStorage.get("winTitle"), win),
				convertToEndGameTitle(MessageStorage.get("winTitleSub"), win));
	}
	
	public static void displayLooseTitle(Player p, Team win){
		TitleAPI.sendTitle(p, 20, 100, 10,
				convertToEndGameTitle(MessageStorage.get("looseTitle"), win),
				convertToEndGameTitle(MessageStorage.get("looseTitleSub"), win));
	}
	
	public static String convertToEndGameTitle(String string, Team win){
		return string.replaceAll("<winteam>", (win == Team.BLUE ?
				ChatColor.AQUA : ChatColor.RED)
				+ win.toString())
		.replaceAll("<looseteam>", (win.getOppositeTeam() == Team.BLUE ?
				ChatColor.AQUA : ChatColor.RED)
				+ win.getOppositeTeam().toString())
		.replaceAll("<time>", String.valueOf(game.totalTime));
	}
	
	public static void updateStatus(GameStatus newstatus) {
		status = newstatus;
		Main.updateGameStatus();
	}
	
	public static Game getGame(){
		return game;
	}

	public String getName() {
		return getGame().serverID;
	}
	
}
