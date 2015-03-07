package ktar.five.TurfWars.Game;

import java.lang.reflect.Field;
import java.util.UUID;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.MessageStorage;
import ktar.five.TurfWars.Game.Cooling.TurfEvent;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase.PhaseType;
import ktar.five.TurfWars.Game.Player.Kit;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;

public class GameListeners implements Listener{


	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event) {
		if(Lobby.players.playerInGame(event.getPlayer().getUniqueId()) && Lobby.status == GameStatus.IN_PROGRESS){
			if(Lobby.getGame().phase.getType() == PhaseType.BUILDING){
				if(!Lobby.getGame().worldManager.canBePlaced(event.getBlockPlaced(), Lobby.players.getPlayerTeam(event.getPlayer().getUniqueId()))){
					event.setCancelled(true);
				}else if(event.getBlockAgainst().getType() == Material.STAINED_CLAY
						|| event.getBlockAgainst().getType() == Material.WOOL){
					Lobby.getGame().worldManager.addPlacedBlock(event.getBlockPlaced());
					Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId()).placedBlock();
				}else{
					event.setCancelled(true);
				}
			}else{
				event.setCancelled(true);
			}
		}else{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void playerDestroyBlock(BlockBreakEvent event){
		if(Lobby.players.playerInGame(event.getPlayer().getUniqueId()) && Lobby.status == GameStatus.IN_PROGRESS){
			if(event.getBlock().hasMetadata("floor")){
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void entityHitByOtherEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();

			if(Lobby.players.playerInGame(damaged.getUniqueId()) && Lobby.status == GameStatus.IN_PROGRESS){
				if (event.getCause().equals(DamageCause.ENTITY_ATTACK) && event.getDamager() instanceof Player) {
					Player damager = (Player) event.getDamager();
					if (Lobby.players.areOnSameTeam(damaged.getUniqueId(), damager.getUniqueId())) {
						event.setDamage(0D);
						event.setCancelled(true);
					}else if ((double) ((Damageable) damaged).getHealth() <= 0) {
						TurfPlayer player = Lobby.players.getAll().get(damager.getUniqueId());
						player.addKill();
						damaged.setHealth(20D);
						Lobby.getGame().playerDied(damaged, MessageStorage.get("swordDeath").replaceAll("<player>",
								((Lobby.players.getPlayerTeam(player).equals(Team.RED) ? "&4" : "&b")
										+ player.getPlayer().getName().toUpperCase())));
					}

				}else if (event.getCause().equals(DamageCause.PROJECTILE) && event.getDamager() instanceof Arrow){
					Arrow arrow = (Arrow) event.getDamager();
					Player damager = Bukkit.getPlayer((UUID) arrow.getMetadata("Arrow").get(0).value());
					if(damager == damaged){
						event.setCancelled(true);
					} else if (Lobby.players.areOnSameTeam(damaged.getUniqueId(), damager.getUniqueId())) {
						event.setDamage(0D);
						event.setCancelled(true);
					}else{
						TurfPlayer player = Lobby.players.getAll().get(damager.getUniqueId());
						player.addKill();
						Lobby.getGame().playerDied(damaged, MessageStorage.get("arrowDeath").replaceAll("<player>",
								((Lobby.players.getPlayerTeam(player).equals(Team.RED) ? "&4" : "&b")
										+ player.getPlayer().getName().toUpperCase())));
					}
				}
			}
		}
	}

	@EventHandler
	public void entityDamagedEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			DamageCause cause = event.getCause();
			if(Lobby.players.playerInGame(player.getUniqueId()) && Lobby.status == GameStatus.IN_PROGRESS){

				if (cause.equals(DamageCause.FALL)) {
					event.setDamage(0D);
					event.setCancelled(true);
				} else if (cause.equals(DamageCause.LAVA)) {
					event.setDamage(0D);
				} else if (cause.equals(DamageCause.VOID)) {
					event.setDamage(0D);
					Lobby.getGame().playerDied(player, MessageStorage.get("voidDeath"));
				} else if (cause.equals(DamageCause.FIRE_TICK)){
					event.setDamage(0D);
				}
			}
		}
	}

	@EventHandler
	private void onProjectileHit(final ProjectileHitEvent e) {
		final Player shooter = Bukkit.getPlayer((UUID) e.getEntity().getMetadata("Arrow").get(0).value());
		if(Lobby.players.playerInGame(shooter.getUniqueId()) && Lobby.status == GameStatus.IN_PROGRESS){
			if (e.getEntityType() == EntityType.ARROW) {
				// Must be run in a delayed task otherwise it won't be able to find the block
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
					public void run() {
						try {

							net.minecraft.server.v1_8_R1.EntityArrow entityArrow = ((CraftArrow) e
									.getEntity()).getHandle();

							Field fieldX = net.minecraft.server.v1_8_R1.EntityArrow.class
									.getDeclaredField("d");
							Field fieldY = net.minecraft.server.v1_8_R1.EntityArrow.class
									.getDeclaredField("e");
							Field fieldZ = net.minecraft.server.v1_8_R1.EntityArrow.class
									.getDeclaredField("f");

							fieldX.setAccessible(true);
							fieldY.setAccessible(true);
							fieldZ.setAccessible(true);

							int x = fieldX.getInt(entityArrow);
							int y = fieldY.getInt(entityArrow);
							int z = fieldZ.getInt(entityArrow);

							if (y != -1) {
								Block block = e.getEntity().getWorld().getBlockAt(x, y, z);
								if(block.getType() != Material.AIR){ //Check all non-solid blockid's here.
									return;
								}else if(block.getType() == Material.WOOL){
									Lobby.getGame().worldManager.removeIfIsPlacedBlock(block);
									Lobby.players.getTurfPlayer(shooter.getUniqueId()).brokeBlock();	
								}
							}
						} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(Lobby.players.playerInGame(event.getPlayer().getUniqueId()) && Lobby.status == GameStatus.IN_PROGRESS){
			TurfPlayer p = Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId());

			switch (event.getTo().getBlock().getType()){
			case LAVA:
			case STATIONARY_LAVA:
				Lobby.getGame().playerDied(p.getPlayer(), MessageStorage.get("lavaDeath"));
				break;
			case WATER:
			case STATIONARY_WATER:
				Lobby.getGame().playerDied(p.getPlayer(), MessageStorage.get("waterDeath"));
				break;
			default:
				break;
			}

			p.handleMoving(event.getTo());

			if (!p.canMove && event.getTo().getY() > event.getFrom().getY()) {
				event.setTo(event.getFrom());
			}
		}
	}

	@EventHandler
	public void onArrowGetCooldown(TurfEvent event) {
		if(Lobby.players.playerInGame(event.getUUID()) && Lobby.status == GameStatus.IN_PROGRESS){
			TurfPlayer player = Lobby.players.getTurfPlayer(event.getUUID());
			if(player.arrows < player.kit.maxArrows){
				player.giveArrow();
			}
		}
	}

	@EventHandler
	public void entityShootBow(EntityShootBowEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if(Lobby.players.playerInGame(p.getUniqueId())){
				TurfPlayer player = Lobby.players.getTurfPlayer(p.getUniqueId());
				if(player.kit == Kit.SHREDDER){
					for(int i = player.arrows ; i >= 0 ; i--){
						Projectile proj = p.getWorld().spawnArrow(p.getLocation(),p.getLocation().getDirection(),
								2/*put your arrow speed here*/, 2/*put your spread here*/);
						proj.setMetadata("Arrow", new FixedMetadataValue(Main.instance, p.getUniqueId()));
						player.shotArrow();
					}
				}else{
					event.getProjectile().setMetadata("Arrow", new FixedMetadataValue(Main.instance, p.getUniqueId()));
					player.shotArrow();
				}
			}
		}
	}


}
