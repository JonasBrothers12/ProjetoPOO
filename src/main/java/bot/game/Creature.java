package bot.game;

import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;



public class Creature{
	public String name;
	public String id;
	public String birth;
	public Creature_status status = new Creature_status();
	public Creature_form stage = new Creature_form();
	public Creature initial_creature(String id,String name) {
		Creature creature = new Creature();
		creature.name = name;
		creature.id = id;
		creature.birth = LocalDate.now().toString();
		creature.status.age = 0;
		creature.status.hunger = 50;
		creature.status.happiness = 50;
		creature.status.faith = 0;
		creature.status.weight = 30;
		creature.status.alive = true;
		creature.status.inteligency = 10;
		creature.status.sleepiness = 10;
		creature.stage = new Normal();
		this.name = creature.name;
		this.id = creature.id;
		this.birth = creature.birth;
		this.status = creature.status;
		this.stage = creature.stage;
		return creature;
	}
	public Creature evolute_creature(Creature creature) {
		creature.stage = new Adult(); 
		return creature;
	}
	public class Creature_status{
		public int sleepiness;
		public int hunger;
		public int happiness;
		public int inteligency;
		public int faith;
		public int weight;
		public int age;
		public Boolean alive;
	}
	public class Creature_form{
		public String name;
	}
	public class Normal extends Creature_form{
		public Normal(){
			name = "😐";
		}
	}

	public class Adult extends Creature_form{
		public Adult(){
			name = "🧘‍♂️";
		}
	}
}




