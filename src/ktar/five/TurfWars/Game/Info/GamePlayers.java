package ktar.five.TurfWars.Game.Info;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GamePlayers {

    public Map<UUID, TurfPlayer> spectators, redTeam, blueTeam;
    public final int maxPerTeam = 8, minPerTeam=4;

    public GamePlayers() {
        this.redTeam = new HashMap<>();
        this.blueTeam = new HashMap<>();
        this.spectators = new HashMap<>();
    }

    public boolean isFull(Team team) {
        if (team == Team.BLUE) {
            return blueTeam.size() == maxPerTeam;
        } else {
            return redTeam.size() == maxPerTeam;
        }
    }
    
    public boolean hasEnoughPlayers(){
    	return this.redTeam.size() >= minPerTeam && this.blueTeam.size() >= minPerTeam;
    }
    
    public TurfPlayer getTurfPlayer(UUID uu){
    	return getAll().get(uu) != null ? getAll().get(uu) : new TurfPlayer(uu);
    }
    
    public Team getPlayerTeam(UUID uu){
    	return getPlayerTeam(getTurfPlayer(uu));
    }
    
    public boolean areOnSameTeam(UUID uu1, UUID uu2){
    	return areOnSameTeam(getTurfPlayer(uu1), getTurfPlayer(uu2));
    }
    
    public boolean areOnSameTeam(TurfPlayer one, TurfPlayer two){
    	return (getPlayerTeam(one).equals(getPlayerTeam(two)));
    }
    
    public Map<UUID, TurfPlayer> getTurfPlayers(Team team, boolean opposite){
    	if(team == Team.BLUE && !opposite){
    		return blueTeam;
    	}else if(team == Team.BLUE){
    		return redTeam;
    	}else if(team == Team.RED && opposite){
    		return blueTeam;
    	}else if(team == Team.RED){
    		return redTeam;
    	}
		return null;
    }
    
    public Map<UUID, TurfPlayer> getAll() {
        Map<UUID, TurfPlayer> players = new HashMap<>();
        players.putAll(blueTeam);
        players.putAll(redTeam);
        players.putAll(spectators);
        return players;
    }

    public boolean gameFull() {
        return (isFull(Team.BLUE) && isFull(Team.RED));
    }

    public Team getTeamWithLess() {
        if (redTeam.size() < blueTeam.size()) {
            return Team.RED;
        } else if (redTeam.size() > blueTeam.size()) {
            return Team.BLUE;
        } else {
            return Team.BLUE;
        }
    }

    public boolean playerInGame(UUID uu) {
        return this.getAll().containsKey(uu);
    }

    public Team getPlayerTeam(TurfPlayer player) {
        if (spectators.containsValue(player)) {
            return Team.SPECTATOR;
        } else if (redTeam.containsValue(player)) {
            return Team.RED;
        } else if (blueTeam.containsValue(player)) {
            return Team.BLUE;
        } else {
            return null;
        }
    }

    public void putInLowerTeam(TurfPlayer player) {
            putInTeam(getTeamWithLess(), player);
    }

    public byte getTeamByte(TurfPlayer player) {
        return getPlayerTeam(player).color;
    }
    
    public byte getOtherTeamByte(TurfPlayer player){
    	return getPlayerTeam(player).getOppositeTeam().color;
    }

    public void putInTeam(Team team, TurfPlayer player) {
        if (team == Team.BLUE) {
            blueTeam.put(player.playerUUID, player);
            player.getPlayer().getInventory().setHelmet(new ItemStack(Material.LAPIS_BLOCK));
        } else if (team == Team.RED) {
            redTeam.put(player.playerUUID, player);
            player.getPlayer().getInventory().setHelmet(new ItemStack(Material.REDSTONE_BLOCK));
        }else if(team == Team.SPECTATOR){
        	spectators.put(player.playerUUID, player);
            player.getPlayer().getInventory().setHelmet(new ItemStack(Material.GLASS));
        }
    }
    
    public boolean switchTeam(TurfPlayer player){
    	Team team = getPlayerTeam(player);
    	if(!isFull(team.getOppositeTeam())){
        	remove(player);
        	putInTeam(team.getOppositeTeam(), player);
        	return true;
    	}
    	return false;
    }

    public void removeFromTeam(TurfPlayer player, Team team){
    	if(team == Team.BLUE){
    		this.blueTeam.remove(player.playerUUID);
    	}else if(team == Team.RED){
    		this.redTeam.remove(player.playerUUID);
    	}else if(team == Team.SPECTATOR){
    		this.spectators.remove(player.playerUUID);
    	}
    }

    public void remove(TurfPlayer player){
    	this.removeFromTeam(player, getPlayerTeam(player));
    }
    
    public void updateDatabase(){
    	List<String> stmts = new ArrayList<>();
    	for(TurfPlayer p : this.getAll().values()){
    		stmts.add(p.getQuery());
    	}
    	
    	try {
			Main.sql.sendBatchStatement(stmts.toArray(new String[stmts.size()]));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }

    public void clear(){
        this.blueTeam.clear();
        this.redTeam.clear();
        this.spectators.clear();
    }

}
