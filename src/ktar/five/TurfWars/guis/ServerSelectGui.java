package ktar.five.TurfWars.guis;

import java.sql.ResultSet;
import java.sql.SQLException;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;

public class ServerSelectGui extends ItemMenu
{
	public ServerSelectGui(boolean isGold)
	{
		super("Turf Wars", Size.SIX_LINE, Main.instance);
		initInventory(isGold);
	}

	public void initInventory(boolean isGold)	
	{
		ResultSet set = null;
		if(isGold){
			try {
				set = Main.sql.querySQL("SELECT playercount,name FROM GameStatus WHERE status=" + GameStatus.WAITING_FOR_PLAYERS.id);
				while(set.next()){
					this.addItem(new ServerWaitingMenuItem(set.getInt("playercount"), set.getString("name")));
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			this.setItem(49,new GoForwardBackMenuItem(true));
		}else{
			try {
				set = Main.sql.querySQL("SELECT playercount,name FROM GameStatus WHERE status=" + GameStatus.IN_PROGRESS.id);
				while(set.next()){
					this.addItem(new ServerActiveMenuItem(set.getInt("playercount"), set.getString("name")));
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			this.setItem(49,new GoForwardBackMenuItem(false));
		}
	}
}
