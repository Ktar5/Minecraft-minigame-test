package ktar.five.TurfWars.Game.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Cooling.Cooldown;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TurfPlayer {

	public UUID playerUUID;
	public int id;
	public Game game;
	public Kit kit;//Defines the kit that they respawn with
	public int wins, defeats, totalKills, totalDeaths,
	topKillsPerMatch, currentKillsThisMatch, /*kit kills*/
	shortestGame, longestGame,
	blocksDestroyed, blocksPlaced, arrowsShot,
	topKillStreak, currentKillStreak, kitsUnlocked, money;
	public int arrows;
	public double multiplier, moneyGotThisRound;
	public boolean canVenture, canMove, isSuperSlowed;
	public boolean isMaster;


	public TurfPlayer(UUID uu) {
		try {
			this.playerUUID = uu;
			ResultSet rs = Main.sql.querySQL("SELECT * FROM UserStats WHERE uuid='" + uu.toString() + "'");
			while(rs.next()) {
				this.id = rs.getInt("id");
				this.wins = rs.getInt("wins");
				this.defeats = rs.getInt("defeats");
				this.totalKills = rs.getInt("totalKills");
				this.totalDeaths = rs.getInt("totalDeaths");
				this.topKillsPerMatch = rs.getInt("topKillsPerMatch");
				this.currentKillsThisMatch = 0;
				this.shortestGame = rs.getInt("shortestGame");
				this.longestGame = rs.getInt("longestGame");
				this.currentKillStreak = 0;
				this.topKillStreak = rs.getInt("topKillStreak");
				this.arrowsShot = rs.getInt("arrowsShot");
				this.blocksDestroyed = rs.getInt("blocksDestroyed");
				this.blocksPlaced = rs.getInt("blocksPlaced");
				this.kitsUnlocked = rs.getInt("kitsUnlocked");
				this.money = rs.getInt("money");
				this.kit = Kit.MARKSMAN;
				rs.close();
				return;
			}
			wins = defeats = totalKills = totalDeaths = topKillsPerMatch = currentKillsThisMatch = /*kit kills*/
					shortestGame = longestGame = blocksDestroyed = blocksPlaced = arrowsShot =
							topKillStreak = currentKillStreak = kitsUnlocked = money = 0;
			this.kit = Kit.MARKSMAN;
			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		this.isMaster = isMaster();
		canVenture = true;
		canMove= true;
		isSuperSlowed = false;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public int getUnlockedKits(){
		return this.kitsUnlocked;
	}

	public void addMoney(double amnt){
		this.moneyGotThisRound += amnt;
	}

	public boolean hasKitUnlocked(Kit kit){
		if(kitsUnlocked == 3){
			return true;
		}else if (kit == Kit.SHREDDER) {
			return kitsUnlocked == 2;
		} else
			return kit != Kit.INFILTRATOR || kitsUnlocked == 1;
	}

	public double getBalance(){
		return this.money;
	}

	public void removeMoney(double amount){
		this.money -= amount;
	}

	public boolean canBuy(double amount){
		return getBalance()>=amount;
	}

	public boolean isOnOthersTurf() {
		Location loc = Bukkit.getPlayer(playerUUID).getLocation();
		loc = loc.subtract(0, 1, 0);
		if (loc.getBlock().getData() == Lobby.players.getOtherTeamByte(this)) {
			return true;
		}
		return false;
	}

	public void setKitVenturing() {
		canVenture = kit.canVenture;
	}

	public void unlockKit(Kit kit){
		if(this.hasKitUnlocked(Kit.INFILTRATOR) && kit.equals(Kit.SHREDDER)){
			kitsUnlocked = 3;
		}else if (this.hasKitUnlocked(Kit.SHREDDER) && kit.equals(Kit.INFILTRATOR)){
			kitsUnlocked = 3;
		}else if (!this.hasKitUnlocked(Kit.INFILTRATOR) && kit.equals(Kit.SHREDDER)){
			kitsUnlocked = 2;
		}else if (kit.equals(Kit.INFILTRATOR)){
			kitsUnlocked = 1;
		}
	}

	public void handleMoving(Location to) {
		if (!isSuperSlowed && !canMove) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999, 256));
			isSuperSlowed = true;
		} else if (isSuperSlowed && canMove) {
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			isSuperSlowed = false;
		}

		if (isOnOthersTurf() && !canVenture) {
			getPlayer().setVelocity(to.getDirection().multiply(-2.0D));
		}
	}

	public void addWin(int gameTime) {
		wins++;
		addMoney(10);
		if (currentKillsThisMatch > topKillsPerMatch)
			topKillsPerMatch = currentKillsThisMatch;
		if (gameTime < shortestGame)
			shortestGame = gameTime;
		if (gameTime > longestGame)
			longestGame = gameTime;

		if(wins == 1000)
			this.moneyGotThisRound += 10000;
		if(topKillsPerMatch < 40 && currentKillsThisMatch >= 40)
			this.moneyGotThisRound += 500;
		else if(topKillsPerMatch < 100 && currentKillsThisMatch >= 100)
			this.moneyGotThisRound += 1250;
		if(shortestGame > 180 && gameTime <= 180)
			this.moneyGotThisRound += 300;
		if(isMaster == false && isMaster() == true)
			this.moneyGotThisRound += 10000;

		this.money+= moneyGotThisRound*multiplier;
	}

	public void addDefeat(int gameTime) {
		defeats++;
		if (currentKillsThisMatch > topKillsPerMatch)
			topKillsPerMatch = currentKillsThisMatch;
		if (gameTime < shortestGame)
			shortestGame = gameTime;
		if (gameTime > longestGame)
			longestGame = gameTime;

		if(topKillsPerMatch < 40 && currentKillsThisMatch >= 40)
			this.moneyGotThisRound += 500;
		else if(topKillsPerMatch < 100 && currentKillsThisMatch >= 100)
			this.moneyGotThisRound += 1250;
		if(shortestGame > 180 && gameTime <= 180)
			this.moneyGotThisRound += 300;
		if(isMaster == false && isMaster() == true)
			this.moneyGotThisRound += 10000;

		this.money+= moneyGotThisRound*multiplier;
	}

	public void addDeath() {
		totalDeaths++;
		if (currentKillStreak > topKillStreak) {
			topKillStreak = currentKillStreak;
		}
		currentKillStreak = 0;
	}

	public void addKill() {
		currentKillStreak++;
		if(currentKillStreak >= 20 && topKillStreak < 20){
			addMoney(100);
		}
		totalKills++;
		if(totalKills == 1000){
			addMoney(1000);
		}else if(totalKills == 10000){
			addMoney(10000);
		}
		currentKillsThisMatch++;
		addMoney(1);
	}

	public void brokeBlock() {
		this.blocksDestroyed++;
		if(this.blocksDestroyed == 5000){
			addMoney(500);
		}
	}

	public void placedBlock() {
		this.blocksPlaced++;
	}

	public void shotArrow() {
		this.arrowsShot++;
		if(arrowsShot == 10000){
			addMoney(1000);
		}
		this.arrows--;
		if (!Cooldown.isCooling(this.playerUUID, "arrow")) {
			this.addArrowCooldown();
		}
	}

	public void addArrowCooldown() {
		Cooldown.add(this.playerUUID, "arrow", this.kit.oneArrowPerX);
	}

	public String getQuery(){
		return "INSERT INTO UserStats (uuid, wins, defeats, totalKills, totalDeaths, topKillsPerMatch, shortestGame, longestGame, " +
				"topKillStreak, arrowsShot, blocksDestroyed, blocksPlaced, kitsUnlocked, money) " +
				"VALUES ('" + this.playerUUID.toString() + "', " + this.wins  + ", " + this.defeats  + ", " + this.totalKills  + ", " + this.totalDeaths  + ", " +
				this.topKillsPerMatch  + ", " + this.shortestGame  + ", " + this.longestGame  + ", " + this.topKillStreak  + ", " + this.arrowsShot
				+ ", " + this.blocksDestroyed  + ", " + this.blocksPlaced  + ", " + this.kitsUnlocked + ", " + this.money + ") " +
				"ON DUPLICATE KEY UPDATE wins="+ this.wins  + ", defeats=" + this.defeats  + ", totalKills=" + this.totalKills  + ", totalDeaths=" + this.totalDeaths  + ", topKillsPerMatch=" +
		this.topKillsPerMatch  + ", shortestGame=" + this.shortestGame  + ", longestGame=" + this.longestGame  + ", topKillStreak=" + this.topKillStreak  + ", arrowsShot=" + this.arrowsShot
				+ ", blocksDestroyed=" + this.blocksDestroyed  + ", blocksPlaced=" + this.blocksPlaced  + ", kitsUnlocked=" + this.kitsUnlocked + ", money=" + this.money ;
	}

	public void resetInventory() {
		Player p = this.getPlayer();
		Cooldown.removeCooldown(this.playerUUID, "arrow");
		this.arrows = 0;
		this.addArrowCooldown();
		p.getInventory().clear();
		p.getInventory().setContents(kit.getItems().toArray(new ItemStack[kit.getItems().size()]));
		p.updateInventory();
		p.removePotionEffect(PotionEffectType.SLOW);
	}

	public void returnToHub(){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try
		{
			out.writeUTF("Connect");
			out.writeUTF("hub");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		getPlayer().removePotionEffect(PotionEffectType.SLOW);
		getPlayer().sendPluginMessage(Main.instance, "BungeeCord", b.toByteArray());
	}

	public void giveArrow() {
		this.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 1));
		this.arrows++;
		if(this.arrows < this.kit.maxArrows){
			Cooldown.removeCooldown(this.playerUUID, "arrow");
			this.addArrowCooldown();
		}
	}

	public boolean isMaster() {
		if (wins >= 1000 &&totalKills >= 1000 && totalKills >= 10000 && topKillsPerMatch >= 40
				&& topKillsPerMatch >= 100 && shortestGame <= 180 &&
				topKillStreak >= 20 && blocksDestroyed >= 5000 && arrowsShot >= 10000) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object comp){
		if(comp instanceof TurfPlayer){
			return ((TurfPlayer)comp).playerUUID == this.playerUUID;
		}
		return false;
	}

}



