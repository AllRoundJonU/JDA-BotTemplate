package com.template.Bot.Interactions;

import com.template.Bot.Managers.Interaction.ContextInteraction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

public class MentionUserInteraction extends ContextInteraction {

    public MentionUserInteraction() {
        super(Command.Type.USER, "mention User");
    }

    @Override
    public void execute(UserContextInteractionEvent event) {

        User user = event.getTarget();
        event.reply("Mentioned user: " + user.getAsMention()).queue();
    }
}
