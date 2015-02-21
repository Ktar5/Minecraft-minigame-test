package ktar.five.TurfWars;

import ktar.five.TurfWars.guis.AchievementGui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("turf")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				if(args[0].equalsIgnoreCase("achievement")){
					AchievementGui gui = new AchievementGui(player);
					gui.open(player);
				}
			}
		}
		return false;
	}

}
