package com.template.Bot.Interactions;

import com.template.Bot.Managers.Interaction.ContextInteraction;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.concurrent.TimeUnit;

public class DeleteMessageInteraction extends ContextInteraction {

    public DeleteMessageInteraction() {
        super(Command.Type.MESSAGE, "delete Message");
    }

    @Override
    public void execute(MessageContextInteractionEvent event) {
        Message targetMessage =  event.getTarget();
        String targetMessageId = targetMessage.getId();
        targetMessage.delete().queue();

        // Send a response to the user that the message was deleted and delete the response after 5 seconds
        event.reply("Deleted message with id " + targetMessageId).queue(response -> response.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
    }

}
