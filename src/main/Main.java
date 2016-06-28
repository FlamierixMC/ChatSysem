package main;

import chatgame.ChatGameQueue;
import chatgame.ChatGameRegistry;
import chatgame.event.ChatGameHandler;
import chatgame.game.HangmanGame;
import chatgame.game.ReactionGame;
import chatgame.letter.LetterGame;
import command.*;
import configuration.ConfigurationRegistry;
import configuration.HangmanGameConfiguration;
import configuration.ReactionGameConfiguration;
import event.ChatEvent;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import utilities.ChatGameHelper;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

	private static String name;
	private static String version;
	private static Server server;
	private static PluginManager manager;
	
	private Logger logger = getLogger();

	@Override
	public void onEnable () {		
		name = getName();
		version = getDescription().getVersion();
		server = getServer();
		manager = server.getPluginManager();
		
		new Hooker(server);
		new ChatGameHelper(this);

		new ConfigurationRegistry();
		
		new ChatGameRegistry(this);
		ChatGameRegistry.registerGame(new ReactionGame(), new ReactionGameConfiguration());
		ChatGameRegistry.registerGame(new HangmanGame(), new HangmanGameConfiguration());
		ChatGameRegistry.registerGame(new LetterGame());

		ChatGameRegistry.fillQueue();
		ChatGameQueue.startQueue();

		ChatGameEvents events = new ChatGameEvents();
		ChatGameHandler.addListener(events);
		
		manager.registerEvents(new ChatEvent(), this);

		ChatSystemCommand[] commands = {
			new InfoCommand(this),
			new ForceCommand(),
			new RewardCommand(),
			new SkipCommand(),
			new ReloadCommand(),
			new EnableCommand(),
			new DisableCommand()
		};

		new CommandRegistry(this);

		for (ChatSystemCommand command : commands)
			CommandRegistry.registerCommand(command);
		
		logger.info(name + " " + version + " enabled.");
	}
	
	@Override
	public void onDisable () {
		logger.info(name + " " + version + " disabled.");
	}
	
}
