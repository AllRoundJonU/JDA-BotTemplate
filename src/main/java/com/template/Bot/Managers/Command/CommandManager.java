package com.template.Bot.Managers.Command;

import com.template.Bot.Bot;
import com.template.Language.LanguageUtils;
import com.template.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The CommandManager class manages registration and execution of slash commands.
 * It extends ListenerAdapter to handle slash command interactions.
 */
public class CommandManager extends ListenerAdapter {
    // Register a private Logger for the CommandManager class
    private final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    // Register all private lists to store the registered slash commands
    private final List<SlashCommand> commands = new ArrayList<>();
    private final Collection<CommandData> commandData = new ArrayList<>();
    private final Collection<CommandData> homeGuildCommandData = new ArrayList<>();

    /**
     * Constructor for CommandManager.
     * Automatically registers all commands in the package basePackage if they extend the Command class.
     * If a Command is registered, it will be added to the commandData list, which is used to register the commands in the Discord API.
     * If the Command file is not in the package basePackage, it will not be registered automatically.
     */
    public CommandManager() {
        String basePackage = Main.basePackage;

        // Create a new Reflections object to scan for all classes that extend SlashCommand
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(basePackage))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(basePackage))));

        Set<Class<? extends SlashCommand>> commandClasses = reflections.getSubTypesOf(SlashCommand.class);

        for (Class<? extends SlashCommand> commandClass : commandClasses){
            try {
                Constructor<? extends SlashCommand> constructor = commandClass.getDeclaredConstructor();
                SlashCommand command = constructor.newInstance();

                registerCommand(command);

                logger.info("Automatically registered command: " + command.getDefaultCommandName());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                logger.error("Failed to register command from class: " + commandClass.getName(), e);
            }
        }
    }

    /**
     * Registers a new slash command.
     *
     * @param command The slash command to register.
     */
    public void registerCommand(SlashCommand command) {
        commands.add(command);
    }

    /**
     * Retrieves the list of registered slash commands.
     *
     * @return The list of registered slash commands.
     */
    public List<SlashCommand> getCommands() {
        return commands;
    }

    /**
     * Retrieves the collection with all commandData.
     *
     * @return The Collection of CommandData.
     */
    public Collection<CommandData> getCommandData() {
        return commandData;
    }

    /**
     * Retrieves the collection with all homeGuildCommandData.
     *
     * @return The Collection of homeGuildCommandData.
     */
    public Collection<CommandData> getHomeGuildCommandData() {
        return homeGuildCommandData;
    }

    /**
     * Registers slash commands with Discord.
     * This method is called in the Bot class.
     * It iterates through all registered commands and creates CommandData for each command.
     */
    public void registerCommands() {
        // JDA instance for interacting with Discord
        JDA jda = Bot.getJDA();

        // Iterate through registered commands to create CommandData
        for (SlashCommand command : commands) {
            SlashCommandData currentCommandData = Commands.slash(command.getDefaultCommandName(),
                    command.getDefaultDescription());

            // Customize CommandData based on the command's properties
            if (command.hasAdditionalNames()) {
                currentCommandData.setNameLocalizations(command.getCommandNames());
            }

            if (command.hasAdditionalDescriptions()) {
                currentCommandData.setDescriptionLocalizations(command.getDescriptions());
            }

            Optional.ofNullable(command.getDefaultMemberPermissions()).ifPresent(currentCommandData::setDefaultPermissions);

            if (command.hasOptions()) {
                currentCommandData.addOptions(command.getOptions());
            }

            if (command.hasSubcommands()) {
                currentCommandData.addSubcommands(command.getSubcommands());
            }

            if (command.hasSubcommandGroups()) {
                currentCommandData.addSubcommandGroups(command.getSubcommandGroups());
            }

            currentCommandData.setGuildOnly(command.isGuildOnly());
            currentCommandData.setNSFW(command.isNSFW());

            // Categorize based on home guild requirement
            if (command.isHomeGuildOnly()) {
                homeGuildCommandData.add(currentCommandData);
            } else {
                commandData.add(currentCommandData);
            }

            // Log the registration of the command
            logger.info(String.format("Registered command: %s", command.getDefaultCommandName()));
        }
    }

    /**
     * Handles slash command interactions.
     *
     * @param event The SlashCommandInteractionEvent representing the interaction.
     */
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // Find the corresponding command based on the interaction name
        commands.stream()
                .filter(command -> command.getDefaultCommandName().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresentOrElse(command -> {
                    // Check for cooldown and execute the command
                    if (command.hasCooldown() && command.getCooldown().isOnCooldown(event, command)) {
                        Locale userLocal = event.getUserLocale().toLocale();
                        String message = LanguageUtils.getLanguageString("languages.bot.global", "command.cooldown.response", userLocal);

                        String timeRelative = command.getCooldown().getEndTimeRelative(event, command);

                        event.reply(String.format(message, timeRelative)).setEphemeral(true).queue();
                        return;
                    }
                    command.execute(event);
                }, () -> event.reply("This Command is currently not available").setEphemeral(true).queue());
    }
}
