package com.template.Bot.Commands;

import com.template.Bot.Managers.Command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends SlashCommand {

    public PingCommand() {
        super("ping", "Shows the ping of the bot");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Calculating ping...")
                .queue(response -> response.editOriginal("Ping: " + event.getJDA().getGatewayPing() + "ms").queue());

    }
}
