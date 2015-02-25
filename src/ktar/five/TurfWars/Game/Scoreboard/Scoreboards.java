package ktar.five.TurfWars.Game.Scoreboard;

import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Scoreboards {

    public static void getLobbyScoreboard(TurfPlayer p){
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective ob = sb.registerNewObjective("Turf", "dummy");
        ob.setDisplayName(Lobby.status == GameStatus.WAITING_FOR_PLAYERS
                ? ChatColor.YELLOW + "" + ChatColor.BOLD + "Wait For More" : ChatColor.GREEN + "" + ChatColor.BOLD + "Start In " + (Lobby.lobbyCountdown - Lobby.seconds) + " sec");
        ob.setDisplaySlot(DisplaySlot.SIDEBAR);
        ob.getScore(" ").setScore(15);
        ob.getScore(ChatColor.BOLD + "" + ChatColor.GOLD + "Players").setScore(14);
        ob.getScore(Lobby.players.getAll().size() + "/16").setScore(13);
        ob.getScore("  ").setScore(12);
        ob.getScore(ChatColor.BOLD + "" + ChatColor.RED + "Kit").setScore(11);
        ob.getScore(p.kit.name()).setScore(10);
        ob.getScore("   ").setScore(9);
        ob.getScore(ChatColor.BOLD + "" + ChatColor.GREEN + "Gems").setScore(8);
        ob.getScore(String.valueOf(p.getBalance())).setScore(7);
        p.getPlayer().setScoreboard(sb);
    }

    public static void getGameScoreboard(TurfPlayer p){
    	Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
    	p.getPlayer().setScoreboard(sb);
        Objective ob = sb.registerNewObjective("Turf", "dummy");
        ob.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + "SKYCRAFT");
        ob.setDisplaySlot(DisplaySlot.SIDEBAR);
        ob.getScore(" ").setScore(15);
        ob.getScore(ChatColor.BOLD + "" + ChatColor.WHITE + Lobby.info.blue + ChatColor.AQUA +" Blue").setScore(14);
        ob.getScore("  ").setScore(13);
        ob.getScore(ChatColor.BOLD + "" + ChatColor.WHITE + Lobby.info.red + ChatColor.RED + " Red").setScore(12);
        ob.getScore("   ").setScore(11);
        ob.getScore(Lobby.getGame().phase.getType() == Phase.PhaseType.BUILDING ?
        		ChatColor.BOLD + "" + ChatColor.YELLOW +  "Build Time" 
        		:
        		Lobby.getGame().phase.getType() == Phase.PhaseType.KILLING ?
        				ChatColor.BOLD + "" + ChatColor.DARK_RED + "Combat Time" 
        				:
        				ChatColor.BOLD + "" + ChatColor.GOLD + "Starting Count").setScore(10);
        ob.getScore(String.valueOf(Lobby.getGame().phase.getSeconds() - Lobby.seconds)).setScore(9);
        p.getPlayer().setScoreboard(sb);
    }


}
