package chatgame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chatgame.ReloadableChatGame;
import chatgame.event.ChatGameEvent;
import chatgame.event.ChatGameHandler;
import chatgame.event.ChatGameListener;
import configuration.HangmanGameConfiguration;
import org.bukkit.ChatColor;

import chatgame.TurnBasedChatGame;
import chatgame.event.ChatGameWinEvent;
import chatgame.event.TurnBasedChatEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import utilities.ChatHelper;
import utilities.ListHelper;

public class HangmanGame extends TurnBasedChatGame implements ChatGameListener,
		ReloadableChatGame {

	private List<String> phrases;

	private char[] phraseChars;
	private char[] correctChars;
	private List<Character> guessedChars;
	
	public HangmanGame() {
		super("Hangman", 5, 2, 120);

		phrases = new ArrayList<>
		((List<String>) HangmanGameConfiguration.PHRASES.getValue());
	}

	@Override
	public void reload () {
		phrases = (List<String>) HangmanGameConfiguration.PHRASES.getValue();
	}

	@Override
	public void start () {
		super.start();
		
		String currentPhrase = ListHelper.selectRandom(phrases);

		int length = currentPhrase.length();
		
		phraseChars = new char[length];
		correctChars = new char[length];
		guessedChars = new ArrayList<>();
		currentPhrase.getChars(0, length, phraseChars, 0);
		
		System.out.println("The current phrase is " + currentPhrase);
		
		ChatHelper.broadcastPinkMessage(getName() + " has started.");
	}
	
	private char[] getCharBoard () {
		for (int i = 0; i < phraseChars.length; i++)
			if (guessedChars.contains(phraseChars[i]))
				correctChars[i] = phraseChars[i];
			else if (phraseChars[i] == ' ')
				correctChars[i] = ' ';
			else if (correctChars[i] == 0)
				correctChars[i] = '_';
		return correctChars;
	}
	
	private String getStringBoard () {
		return String.valueOf(getCharBoard());
	}

	@Override
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		super.onPlayerChat(event);
		ChatHelper.sendYellowMessage(player, getStringBoard());
	}

	@Override
	public void onEventFire(ChatGameEvent event) {

		if (!(event instanceof TurnBasedChatEvent)) return;

		char[] messageChars = new char[message.length()];
		message.getChars(0, messageChars.length, messageChars, 0);

		if (messageChars.length == 1)
			guessedChars.add(messageChars[0]);

		if (Arrays.equals(getCharBoard(), phraseChars)) {
			sendAll (ChatColor.GREEN, player.getName() + " guessed the final letter.");

			ChatGameHandler.fireEvent(new ChatGameWinEvent(this, player));
		}

		if (Arrays.equals(messageChars, phraseChars))
			ChatGameHandler.fireEvent(new ChatGameWinEvent(this, player));

		sendAll (ChatColor.YELLOW, getStringBoard());
	}

}