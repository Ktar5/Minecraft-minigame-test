package ktar.five.TurfWars;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ktar.five.TurfWars.Game.GameListeners;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.Lobby.LobbyListeners;
import ktar.five.TurfWars.SQL.MySQL;
import ktar.five.TurfWars.hub.Hub;
import ktar.five.TurfWars.hub.HubListeners;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static MySQL sql = null;
	Connection c = null;
	public static Economy economy = null;

	public static List<LivingEntity> entities;

	public static Main instance = null;

	@Override
	public void onLoad() {
		instance = this;
	}

	@Override
	public void onEnable() {
		entities = new ArrayList<LivingEntity>();
		this.saveDefaultConfig();
		FileConfiguration configuration = this.getConfig();
		try {
			sql = new MySQL(instance, configuration.getString("host"),
					configuration.getString("port"),
					configuration.getString("database"),
					configuration.getString("username"),
					configuration.getString("password"));
			c = sql.openConnection();
			sql.updateSQL("CREATE TABLE IF NOT EXISTS UserStats (id int NOT NULL AUTO_INCREMENT, uuid char(36) NOT NULL UNIQUE, "
					+ "wins int NOT NULL, defeats int NOT NULL, totalKills int NOT NULL, totalDeaths int NOT NULL, topKillsPerMatch int NOT NULL, "
					+ "shortestGame int NOT NULL, longestGame int NOT NULL, topKillStreak int NOT NULL, arrowsShot int NOT NULL, "
					+ "blocksDestroyed int NOT NULL, blocksPlaced int NOT NULL, kitsUnlocked int NOT NULL, money int NOT NULL, PRIMARY KEY (id))");
			sql.updateSQL("CREATE TABLE IF NOT EXISTS GameStatus (id int NOT NULL AUTO_INCREMENT, status int NOT NULL,"
					+ " playercount int NOT NULL, name varchar(255) NOT NULL, PRIMARY KEY (id))");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		if (this.getConfig().getBoolean("isHub")) {
			new Hub(this.getConfig());
			Bukkit.getServer().getPluginManager().registerEvents(new EntityListener(), this);
			Bukkit.getServer().getPluginManager().registerEvents(new HubListeners(), this);
			this.setupEconomy();
		} else {
			try {
				sql.updateSQL("INSERT INTO GameStatus (status, playercount, name) VALUES ("
						+ GameStatus.RESTARTING.id
						+ ", 0, '"
						+ configuration.getString("serverid") + "')");
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			new Lobby(configuration);
			Bukkit.getServer().getPluginManager().registerEvents(new LobbyListeners(), this);
			Bukkit.getServer().getPluginManager().registerEvents(new GameListeners(), this);
			Bukkit.getServer().getPluginManager().registerEvents(new EntityListener(), this);
		}
		this.getCommand("turf").setExecutor(new Commands());

	}

	@Override
	public void onDisable() {
		instance = null;
		try {
			sql.updateSQL("DELETE FROM GameStatus WHERE name= '" + Lobby.serverid+"'");
			sql.closeConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = entities.size() - 1; i >= 0; i--) {
			entities.get(i).setHealth(0d);
			entities.remove(i);
		}
		if (!this.getConfig().getBoolean("isHub")) {
			Lobby.getGame().worldManager.resetMap();
		}
	}

	public static void updateGameStatus() {
		try {
			sql.updateSQL("UPDATE GameStatus SET status=" + Lobby.status.id
					+ ", playercount=" + Bukkit.getOnlinePlayers().length
					+ ", name='" + Lobby.serverid + "' WHERE name= '" + Lobby.serverid+"'");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void getPlayerBalance(){
		//TODO
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

}