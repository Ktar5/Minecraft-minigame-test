package ktar.five.TurfWars.Game.Info;

import java.util.ArrayList;
import java.util.List;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

public class WorldManager {

	public List<Blocks> turfBlocks;
	public List<Location> placedBlocks, fireworks;
	public Location lobbySpawn,redSpawn,blueSpawn,winning,loosing;

	public int red;
	public int blue;

	public WorldManager(Location lobbySpawn, Location redSpawn,
			Location blueSpawn, Location boxCorner1,
			Location boxCorner2, List<Location> fireworks, Location winning, Location loosing) {

		this.lobbySpawn = lobbySpawn;
		this.redSpawn = redSpawn;
		this.blueSpawn = blueSpawn;

		this.fireworks = fireworks;

		this.winning = winning;
		this.loosing = loosing;

		turfBlocks = new ArrayList<>();
		placedBlocks = new ArrayList<>();
		int xmin = Math.min(boxCorner2.getBlockX(), boxCorner1.getBlockX());
		int ymin = Math.min(boxCorner2.getBlockY(), boxCorner1.getBlockY());
		int zmin = Math.min(boxCorner2.getBlockZ(), boxCorner1.getBlockZ());

		int xmax = Math.max(boxCorner2.getBlockX(), boxCorner1.getBlockX());
		int ymax = Math.max(boxCorner2.getBlockY(), boxCorner1.getBlockY());
		int zmax = Math.max(boxCorner2.getBlockZ(), boxCorner1.getBlockZ());

		for(int x = xmin ; x <= xmax ; x++){
			List<Location> blocks = new ArrayList<>();
			for(int y = ymin ; y <= ymax ; y++){
				for(int z = zmin ; z <= zmax ; z++){
					blocks.add(boxCorner2.getWorld().getBlockAt(x, y, z).getLocation());
				}
			}
			turfBlocks.add(new Blocks(Team.SPECTATOR, blocks));
		}
		
		for(int i = 0 ; i < turfBlocks.size() ; i++){
			if(i < turfBlocks.size()/2){
				this.setTeam(Team.BLUE, i);
			}else{
				this.setTeam(Team.RED, i);
			}
		}

		blue = red = turfBlocks.size()/2;
	}

	public boolean removeIfIsPlacedBlock(Block block) {
		if (placedBlocks.contains(block.getLocation())) {
			placedBlocks.remove(block.getLocation());
			block.setType(Material.AIR);
			return true;
		}else{
			return false;
		}
	}

	public boolean canBePlaced(Block block, Team team){
		for(Blocks blocks : turfBlocks){
			if(blocks.team == team){
				if(blocks.blocks.contains(block.getLocation())){
					return true;
				}
			}
		}
		return false;
	}

	public void addPlacedBlock(Block block) {
		placedBlocks.add(block.getLocation());
	}

	public void addClays(Team team, int num) {
		if(team == Team.BLUE){
			for(int i = 1 ; i <= num ; i++ ){
				++blue;
				red--;
				setTeam(team, blue-1);
				if(red == 0){
					Lobby.teamWon(team);
					return;
				}
			}
		}else if(team == Team.RED){
			for(int i = 1 ; i <= num ; i++ ){
				blue--;
				++red;
				setTeam(team, (turfBlocks.size())-red);
				if(blue == 0){
					Lobby.teamWon(team);
					return;
				}
			}
		}
	}

	public Team getWinning(){
		return red >= blue ? Team.RED : Team.BLUE;
	}

	
	public void reset(){
		for(Blocks block : turfBlocks){
			for(Location loc : block.blocks){
				if(loc.getBlock().getType().equals(Material.STAINED_CLAY)){
					loc.getBlock().setType(Material.SPONGE);
				}
			}
		}
	}
	
	public void resetMap(){
		for(Location block : placedBlocks){
			block.getBlock().setType(Material.AIR);
		}
		for(int i = 0 ; i < turfBlocks.size() ; i++){
			if(i < turfBlocks.size()/2){
				this.setTeam(Team.BLUE, i);
			}else{
				this.setTeam(Team.RED, i);
			}
		}
		blue = red = turfBlocks.size()/2;
	}

	public void setTeam(Team team, int index){
		turfBlocks.get(index).team = team;
		for(Location b : turfBlocks.get(index).blocks){
			if(b.getBlock().getType().equals(Material.STAINED_CLAY) && b.getBlock().hasMetadata("floor")){
				b.getBlock().removeMetadata("floor", Main.instance);	
			}else if(b.getBlock().getType().equals(Material.SPONGE)){
				b.getBlock().setType(Material.STAINED_CLAY);
			}else{
				continue;
			}
			b.getBlock().setData(team.color);
			b.getBlock().setMetadata("floor", new FixedMetadataValue(Main.instance, team));
			
			for(Location loc : placedBlocks){
				if(loc.getBlockX() == b.getBlockX()){
					loc.getBlock().setData(team.color);
				}
			}
			
		}
	}

}
