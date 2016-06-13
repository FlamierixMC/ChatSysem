package event;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import chatgame.ChatGame;
import event.listeners.ChatGameWinListener;
import logan.reward_api.main.Reward;
import logan.reward_api.main.RewardStore;
import logan.reward_api.main.RewardType;
import utilities.ChatHelper;

public class ChatGameWinEventHandler implements ChatGameWinListener {

	@Override
	public void onChatGameWin(ChatGameWinEvent event) {

		Player player = event.getPlayer();
		ChatGame chatGame = event.getChatGame();
		
		ChatHelper.sendGreenMessage(player, "You won the chat game " + chatGame.getName() + "!");
		
		new Reward(player)
		.setMaterial(Material.COAL)
		.setAmount(8)
		.setDisplayName("Chat game reward")
		.setLore("Coal...")
		.give(player);

		chatGame.stop();
		
	}

}