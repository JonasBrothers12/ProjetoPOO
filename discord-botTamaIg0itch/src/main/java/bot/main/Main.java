package bot.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import bot.commands.Game;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	
	  public static void main(String[] args) throws LoginException, IOException {
		    String path = null;
	        try (BufferedReader fr = new BufferedReader(new FileReader("token.txt"))) {
	        	 path = fr.readLine();
			} catch (FileNotFoundException e) {
				 e.printStackTrace();
			} 
	        String token = path;
	        JDABuilder builder = JDABuilder.createDefault(token);
	        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MESSAGES);
	        builder.addEventListeners(new Game());
	        builder.build();
	    }
}




