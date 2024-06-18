package bot.commands;

import java.io.IOException;
import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;

import bot.game.Creature;
import bot.game.FileHandler;
import bot.game.GameActions;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

public class Game extends ListenerAdapter {
	public FileHandler file = new FileHandler();
	public Creature creature = new Creature();
    private String waitingForResponseFromUser = null;
    private long time = 0;
    private String messageId,messageId_show;
    private User interactingUser;
    private GameActions game = new GameActions();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
    	String content = event.getMessage().getContentRaw();
        if (content.equalsIgnoreCase("!play")) {
            this.interactingUser = event.getAuthor();
            sendInitialMessage(event);
        }
    }
    private void sendGameMessage(MessageReactionAddEvent event) {
        event.getChannel().sendMessage("☸︎ O que deseja fazer? ☸︎\n"+
        		"🍔 Comer\n" +        		
        	    "😴 Dormir\n"+
        	    "📖 Estudar\n"+
        	    "💪 Treinar\n"+
        		"🛑 Sair")
            .queue(message -> {
                messageId = message.getId();
                message.addReaction(Emoji.fromUnicode("U+1F354")).queue();  
                message.addReaction(Emoji.fromUnicode("U+1F634")).queue();  
                message.addReaction(Emoji.fromUnicode("U+1F4D6")).queue();  
                message.addReaction(Emoji.fromUnicode("U+1F4AA")).queue();  
                message.addReaction(Emoji.fromUnicode("U+1F6D1")).queue();                 
            });
    }
    private void sendInitialMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("🛐🕉️🛕🙏ૐBem vindo ao TamaIg0ytchi 🧑‍🦲🛐🕉️🛕🙏ૐ\nSelecione uma opção:\n(a) Carregar um jogo\n(b) Novo jogo\n(c) Deletar jogo \n(d) Sair")
            .queue(message -> {
                messageId = message.getId();
                message.addReaction(Emoji.fromUnicode("U+1F1E6")).queue();  
                message.addReaction(Emoji.fromUnicode("U+1F1E7")).queue();
                message.addReaction(Emoji.fromUnicode("U+1F1E8")).queue();  
                message.addReaction(Emoji.fromUnicode("U+1F1E9")).queue();              
            });
    }
    public void gameInitialMessage(MessageReactionAddEvent event){
    	this.creature = game.Verify(this.time, this.creature);
    	this.time = System.currentTimeMillis();
		if (this.creature.status.alive){
			if (event.getUser().isBot() || !event.getUser().equals(interactingUser)) return;
	        if (!event.getMessageId().equals(messageId)) return;
			show_status(event);
			sendGameMessage(event);			
		}else {
		 try {
			file.delete_save(event.getUser().getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		 event.getChannel().sendMessage("Este Ig0y não está mais entre nós!😢🛐🕉️🛕🙏ૐ\nDigite \"!play\"").queue(message -> 
         message.delete().queueAfter(20, TimeUnit.SECONDS));
		 MessageReceivedEvent fakeEvent = new MessageReceivedEvent(
	                event.getJDA(), event.getResponseNumber(), event.getChannel().retrieveMessageById("!play").complete()
	            );
	     sendInitialMessage(fakeEvent);
   		 return;
		}
    }
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().isBot() || !event.getUser().equals(interactingUser)) return;
        if (!event.getMessageId().equals(messageId)) return;
        GameActions game = new GameActions();
        MessageReaction emoji = event.getReaction();
        String reaction = emoji.getEmoji().getAsReactionCode();
        if (reaction.equals("🇦")) {
        	try {
				this.creature = game.load_character(event.getUser());
			} catch (IOException e) {
				e.printStackTrace();
			}
        	if (this.creature == null) {
        		 event.getChannel().sendMessage("⚛Este Ig0y não existe, Galado!⚛\"!play\"").queue(message -> 
                 message.delete().queueAfter(5, TimeUnit.SECONDS));
        		 restartInteraction(event);
        		 return;
        	}
        	event.getChannel().deleteMessageById(messageId).queue();
        	this.time = System.currentTimeMillis();
        	gameInitialMessage(event);        	
        }else if (reaction.equals("🇧")) {
        	this.creature = game.create_character(event.getUser());
        	if (this.creature == null) {
       		 event.getChannel().sendMessage("⚛Este Ig0y já existe, Galado!⚛\"!play\"").queue(message -> 
             message.delete().queueAfter(5, TimeUnit.SECONDS));
       		 restartInteraction(event);
       		 return;
        	}
        	event.getChannel().deleteMessageById(messageId).queue();
        	this.time = System.currentTimeMillis();
        	gameInitialMessage(event);
        }else if (reaction.equals("🇨")) {
        	User user = event.getUser();
        	String id = user.getId();
        	try {
				file.delete_save(id);
			} catch (IOException e) {
				e.printStackTrace();
			}
        	restartInteraction(event);
        	return;
        }else if (reaction.equals("🇩")) {
        	event.getChannel().deleteMessageById(messageId).queue();
        	return;
        }else if (reaction.equals("🍔")) {
        	event.getChannel().deleteMessageById(messageId).queue();
        	event.getChannel().sendMessage("Comendo Maniçoba hum..hum...🪔🪔🪔").queue(message -> 
            message.delete().queueAfter(5, TimeUnit.SECONDS));       
        	event.getChannel().deleteMessageById(messageId_show).queue();
			this.creature.status.hunger += 15;	
			this.creature.status.weight += 2;
			gameInitialMessage(event);	
        	return;
        }else if (reaction.equals("😴")) {
        	event.getChannel().deleteMessageById(messageId).queue();
        	event.getChannel().sendMessage("Dormindo esfoliado...☯️☯️☯️").queue(message -> 
            message.delete().queueAfter(5, TimeUnit.SECONDS));       
        	event.getChannel().deleteMessageById(messageId_show).queue();
			this.creature.status.sleepiness -= 5;
			gameInitialMessage(event);	
        	return;
        }else if (reaction.equals("📖")) {
        	event.getChannel().deleteMessageById(messageId).queue();
        	event.getChannel().sendMessage("🤓🤓🤓Lendo o que apanha no centeio...🙇🏻🙇🏻🙇🏻").queue(message -> 
            message.delete().queueAfter(5, TimeUnit.SECONDS));       
        	event.getChannel().deleteMessageById(messageId_show).queue();
			this.creature.status.inteligency += 5;
			this.creature.status.faith += 5;
			gameInitialMessage(event);	
        	return;
        }else if (reaction.equals("💪")) {
        	event.getChannel().deleteMessageById(messageId).queue();
        	event.getChannel().sendMessage("Jogando Tênis...🎾🎾🎾").queue(message -> 
            message.delete().queueAfter(5, TimeUnit.SECONDS));       
        	event.getChannel().deleteMessageById(messageId_show).queue();
			this.creature.status.weight -= 1;
			this.creature.status.happiness += 5;
			gameInitialMessage(event);		
        	return;
        }else if (reaction.equals("🛑")) {
        	event.getChannel().sendMessage("Saindo do RN...🦋🦋🦋....Indo para end🟣🟣🟣🟣🟣🟣🟣🟣🟣").queue(message -> 
            message.delete().queueAfter(10, TimeUnit.SECONDS));
        	event.getChannel().sendMessage("https://www.twitch.tv/endpdc").queue();
        	event.getChannel().deleteMessageById(messageId).queue();  
        	event.getChannel().deleteMessageById(messageId_show).queue();
        	restartInteraction(event);
        	try {
				file.update_save(this.creature);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	return;
        }else {
        	event.getChannel().sendMessage("Comando Inválido Boy Galado🥛🥛🥛🥛").queue(message -> 
            message.delete().queueAfter(10, TimeUnit.SECONDS));
        	restartInteraction(event);
      		return;
        }

    }
    public void restartInteraction(MessageReactionAddEvent event) {
        event.getChannel().deleteMessageById(messageId).queue();
        MessageReceivedEvent fakeEvent = new MessageReceivedEvent(
                event.getJDA(), event.getResponseNumber(), event.getChannel().retrieveMessageById("!play").complete()
            );
        sendInitialMessage(fakeEvent);
    }
    public void show_status(MessageReactionAddEvent event){
		event.getChannel().sendMessage(
				"📒 Status:\n"+
				"🧑‍🦲 Dono: " + this.creature.name+
				"\n🎂 Nascimento: " + this.creature.birth+
				"\n🤤 Fome: " + this.creature.status.hunger+
				"\n🛌 Sonolência: " + this.creature.status.sleepiness+
				"\n📚 Inteligência: " + this.creature.status.inteligency+
				"\n🙏 Fé: " + this.creature.status.faith+
				"\n🍗 Peso: " + this.creature.status.weight+
				"\n👦 Idade: " + this.creature.status.age+
				"\n👁️ Forma: " + this.creature.stage.name
				).queue(message->{
					messageId_show = message.getId();
				});
	}
}


 