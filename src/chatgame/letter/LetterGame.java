package chatgame.letter;

import java.util.Random;

import chatgame.ChatGame;
import chatgame.event.*;
import org.bukkit.entity.Player;

import chatgame.TurnBasedChatGame;
import exceptions.EmptyQueueException;
import utilities.ChatHelper;

public class LetterGame extends TurnBasedChatGame implements ChatGameListener {

	private char currentLetter;
	private int guesses;
	
	public LetterGame() {
		super("GuessTheLetter", 1, 2, 30);
	}

	@Override
	public void start() {
		super.start();

		final String alphabet =
				"abcdefghijklmnopqrstuvwxyz";

		int index = new Random()
		.nextInt(alphabet.length());
		
		currentLetter = alphabet.charAt(index);
		
		System.out.println("The letter is " + currentLetter);
		
		ChatHelper.broadcastYellowMessage(getName() + " has started.");
		ChatHelper.broadcastYellowMessage("Whoever guesses the letter first wins!");
	}

	public int getGuesses () {
		return guesses;
	}

	@Override
	public void stop() {
		super.stop();
		
		guesses = 0;
		
	}

	@Override
	public void onEventFire(ChatGameEvent event) {

		ChatGame chatGame = event.getChatGame();
		if (!(chatGame instanceof LetterGame)) return;

		Player player = null;
		try {
			player = getNextPlayer();
		} catch (EmptyQueueException e) {
			ChatGameHandler.fireEvent(new ChatGameEndEvent(this));
			System.out.println("Unexpected end of " + getName() + ".");
		}

		System.out.println("The next player is " + player.getName() + ".");

		char guessedChar = message.charAt(0);

		guesses++;

		if (guessedChar != currentLetter) {

			ChatHelper.sendRedMessage(player, "Nope!");
			System.out.println("Looks like he guessed wrong.");
		}

		else ChatGameHandler.fireEvent(new ChatGameWinEvent(this, player));
	}
}
