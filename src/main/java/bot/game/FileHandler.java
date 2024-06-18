package bot.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler{
	public void delete_save(String name) throws IOException{
		String save = "saves/" + name + ".txt";
		File file = new File(save);
		file.delete();
	}
	public void update_save(Creature character) throws IOException{
		FileHandler new_save = new FileHandler();
		new_save.delete_save(character.id);
		new_save.creating_user(character);
	}
	
	public void creating_user(Creature character){
		String save = "saves/" + character.id + ".txt";
		File file = new File(save);
	    try (FileWriter fw = new FileWriter(file)) {
	        fw.write(character.name + "\n");
	        fw.write(character.birth.toString() + "\n");
	        fw.write(String.valueOf(character.status.age) + "\n");
	        fw.write(String.valueOf(character.status.weight) + "\n");
	        fw.write(String.valueOf(character.status.faith) + "\n");
	        fw.write(String.valueOf(character.status.happiness) + "\n");
	        fw.write(String.valueOf(character.status.hunger) + "\n");
	        fw.write(String.valueOf(character.status.inteligency) + "\n");
	        fw.write(String.valueOf(character.status.sleepiness) + "\n");
	        fw.write(character.status.alive.toString() + "\n");
	        fw.write(character.stage.name + "\n");
	        fw.close();
	    } catch (IOException e) {
	        System.out.println("Error creating file: " + e.getMessage());
	    }
	}
	public Creature reading_save(String id) throws IOException{
		String save = "saves/" + id + ".txt";
		Creature character = new Creature();
		try (BufferedReader fr = new BufferedReader(new FileReader(save))) {
	        character.name = fr.readLine();
	        character.birth = fr.readLine();
	        character.status.age = Integer.valueOf(fr.readLine());
	        character.status.weight = Integer.valueOf(fr.readLine());
	        character.status.faith = Integer.valueOf(fr.readLine());
	        character.status.happiness = Integer.valueOf(fr.readLine());
	        character.status.hunger = Integer.valueOf(fr.readLine());
	        character.status.inteligency = Integer.valueOf(fr.readLine());
	        character.status.sleepiness = Integer.valueOf(fr.readLine());
	        character.status.alive = Boolean.valueOf(fr.readLine());
			System.out.println(character.status.alive);
	        character.stage.name = fr.readLine();
	    } catch (FileNotFoundException e) {
	        System.err.println("Error: Save file not found: " + save);
	    }
		return character;
	}
	public Boolean verify_user(String name){
		String file = "saves/" + name + ".txt";
		Path path = Paths.get(file);
		if (Files.exists(path)) {
			return true;
		}else {
			return false;
		}
	}
}