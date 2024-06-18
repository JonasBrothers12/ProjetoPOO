package bot.game;

import java.io.IOException;
import java.util.Scanner;

import bot.game.Creature.Adult;
import net.dv8tion.jda.api.entities.User;

public class GameActions {
	public Creature create_character(User user){
		FileHandler file = new FileHandler(); 
		if (file.verify_user(user.getId())){
			return null;
		}else{
			Creature character = new Creature();
			String name = user.getName();
			String id = user.getId();
			character.initial_creature(id,name);
			file.creating_user(character);
			return character;
		}
	}
	public Creature load_character(User user) throws IOException{
		FileHandler file = new FileHandler();
		if (file.verify_user(user.getId())){	
			Creature character = new Creature();
			try {
				character = file.reading_save(user.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return character;
		}else{
			System.out.println("Não existe essa criatura!");
			return null;
		}
	}
	public Creature Verify(long time0,Creature character){
		long time,oficial_time;
		time = System.currentTimeMillis();
		oficial_time = ((time - time0)/120000);
		System.out.println(oficial_time);
		int oficial_time_int = (int)oficial_time;
		for(int a=0;a < oficial_time_int;a++) {
			character.status.hunger -= 2;
			character.status.sleepiness += 2;
			character.status.age++;
			character.status.weight--;
		}
		
		if ((character.status.hunger <= 0)||(character.status.sleepiness >= 100)||(character.status.weight <= 10)){
			character.status.alive = false;
		}else {
			if ((character.status.inteligency > 99) && (character.status.faith > 99) && (character.status.happiness <= 99)) {
			character = character.evolute_creature(character);
			}
		}
		return character;
	}
}
