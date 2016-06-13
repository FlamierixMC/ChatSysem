package chatgame;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

import event.ChatGameWinEvent;
import event.handlers.ChatGameWinHandler;
import utilities.ChatGameHelper;

public abstract class ChatGame implements Listener {

	private String name;
	
	private int minPlayers;
	private int maxTime;
	private boolean running;
	
	private BukkitTask gameTask;
	private Player recentWinner;
	
	public ChatGame (String name, int minPlayers, int maxTime) {
		this.name = name;
		this.minPlayers = minPlayers;
		this.maxTime = maxTime;
	}
	
	public String getName () {
		return name;
	}
	
	public int getMinimumPlayers () {
		return minPlayers;
	}
	
	public int getMaximumTime () {
		return maxTime;
	}
	
	public Player getRecentWinner () {
		return recentWinner;
	}
	
	public void setWinner (Player winner) {
		this.recentWinner = winner;
		ChatGameWinEvent chatGameWinEvent = new ChatGameWinEvent();
		chatGameWinEvent.setPlayer(player);
		chatGameWinEvent.setChatGame(this);
		ChatGameWinHandler.fireEvent(chatGameWinEvent);
	}
	
	public boolean isRunning () {
		return running;
	}
	
	public void start () {
		running = true;
		gameTask = ChatGameHelper.runRepeatingTask(new ChatGameRunnable(this), 20, 20);
	}
	
	public void stop () {
		running = false;
		recentWinner = null;
		gameTask.cancel();
	}
	
	public Player player;
	public String message;
	
	public void onPlayerChat (AsyncPlayerChatEvent event) {
		player = event.getPlayer();
		message = event.getMessage();
	}
	
}
