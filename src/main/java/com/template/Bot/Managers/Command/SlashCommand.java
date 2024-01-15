package com.template.Bot.Managers.Command;

import com.template.Bot.Managers.Cooldown;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SlashCommand class is the base class for all slash commands.
 * It contains all information about the slash command.
 * It also contains the execute method, which is called when the slash command is executed.
 */
public abstract class SlashCommand {

    private Map<DiscordLocale, String> commandNames = new HashMap<>();
    private Map<DiscordLocale, String> descriptions = new HashMap<>();
    private boolean guildOnly = false;
    private boolean homeGuildOnly = false;
    private boolean NSFW = false;
    private DefaultMemberPermissions defaultMemberPermissions;
    private Cooldown cooldown;
    private List<OptionData> options = new ArrayList<>();
    private List<SubcommandData> subcommands = new ArrayList<>();
    private List<SubcommandGroupData> subcommandGroups = new ArrayList<>();
    private String helpMessage;

    /**
     * The execute method is called when the slash command is executed.
     * @param event The SlashCommandInteractionEvent
     */
    public abstract void execute(SlashCommandInteractionEvent event);

    /**
     * The SlashCommand constructor is used to set the default name and description of the slash command.
     * @param defaultName The default name of the slash command
     * @param defaultDescription The default description of the slash command
     */
    public SlashCommand(String defaultName, String defaultDescription) {
        this.commandNames.put(DiscordLocale.ENGLISH_US, defaultName);
        this.descriptions.put(DiscordLocale.ENGLISH_US, defaultDescription);
    }

    /**
     * Returns the default name of the slash command.
     * @return The english name of the slash command as a String
     */
    public String getDefaultCommandName(){
        return commandNames.get(DiscordLocale.ENGLISH_US);
    }

    /**
     * Returns the name of the slash command in the specified locale.
     * @param locale The locale of the name
     * @return The name of the slash command as a String in the specified locale
     */
    public String getCommandName(DiscordLocale locale){
        return commandNames.get(locale);
    }

    /**
     * Returns all names of the slash command.
     * @return A Map containing all names of the slash command
     */
    public Map<DiscordLocale, String> getCommandNames() {
        return commandNames;
    }

    /**
     * Sets the default name of the slash command.
     * @param commandName The default name of the slash command
     */
    public void setDefaultCommandName(String commandName) {
        this.commandNames.put(DiscordLocale.ENGLISH_US, commandName);
    }

    /**
     * Sets the names of the slash command.
     * @param commandName A Map containing all names of the slash command
     */
    public void setCommandName(Map<DiscordLocale, String> commandName) {
        this.commandNames = commandName;
    }

    /**
     * Returns whether the slash command has additional names.
     * @return True if the slash command has additional names, false otherwise
     */
    public boolean hasAdditionalNames() {
        return commandNames.size() > 1;
    }

    /**
     * Returns the default description of the slash command.
     * @return The english description of the slash command as a String
     */
    public String getDefaultDescription(){
        return descriptions.get(DiscordLocale.ENGLISH_US);
    }

    /**
     * Returns the description of the slash command in the specified locale.
     * @param locale The locale of the description
     * @return The description of the slash command as a String in the specified locale
     */
    public String getDescription(DiscordLocale locale){
        return descriptions.get(locale);
    }

    /**
     * Returns all descriptions of the slash command.
     * @return A Map containing all descriptions of the slash command
     */
    public Map<DiscordLocale, String> getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the default description of the slash command.
     * @param description The default description of the slash command
     */
    public void setDefaultDescription(String description) {
        this.descriptions.put(DiscordLocale.ENGLISH_US, description);
    }

    /**
     * Sets the descriptions of the slash command.
     * @param description A Map containing all descriptions of the slash command
     */
    public void setDescription(Map<DiscordLocale, String> description) {
        this.descriptions = description;
    }

    /**
     * Returns whether the slash command has additional descriptions.
     * @return True if the slash command has additional descriptions, false otherwise
     */
    public boolean hasAdditionalDescriptions() {
        return descriptions.size() > 1;
    }


    /**
     * Sets whether the slash command is guild only. By default, the slash command is not guild only.
     * @param guildOnly True if the slash command is guild only, false otherwise
     */
    public void setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
    }

    /**
     * Returns whether the slash command is guild only.
     * @return True if the slash command is guild only, false otherwise
     */
    public boolean isGuildOnly() {
        return guildOnly;
    }

    /**
     * Sets whether the slash command is home guild only.
     * By default, the slash command is not home guild only.
     * If the slash command is home guild only, it can only be used in the home guild defined in the discord.properties.
     * @param homeGuildOnly True if the slash command is home guild only, false otherwise
     */
    public void setHomeGuildOnly(boolean homeGuildOnly) {
        this.homeGuildOnly = homeGuildOnly;
    }

    /**
     * Returns whether the slash command is home guild only.
     * @return True if the slash command is home guild only, false otherwise
     */
    public boolean isHomeGuildOnly() {
        return homeGuildOnly;
    }

    /**
     * Sets whether the slash command is NSFW. By default, the slash command is not NSFW.
     * If the slash command is NSFW, it can only be used in NSFW channels.
     * @param NSFW True if the slash command is NSFW, false otherwise
     */
    public void setNSFW(boolean NSFW) {
        this.NSFW = NSFW;
    }

    /**
     * Returns whether the slash command is NSFW.
     * @return True if the slash command is NSFW, false otherwise
     */
    public boolean isNSFW() {
        return NSFW;
    }

    /**
     * Sets the default member permissions of the slash command.
     * @param defaultMemberPermissions The default member permissions of the slash command
     */
    public void setDefaultMemberPermissions(DefaultMemberPermissions defaultMemberPermissions) {
        this.defaultMemberPermissions = defaultMemberPermissions;
    }

    /**
     * Returns the default member permissions of the slash command.
     * @return The default member permissions of the slash command
     */
    public DefaultMemberPermissions getDefaultMemberPermissions() {
        return defaultMemberPermissions;
    }

    /**
     * Sets the cooldown of the slash command.
     * @param cooldown The {@link Cooldown} of the slash command
     */
    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Returns the cooldown of the slash command.
     * @return The {@link Cooldown} of the slash command
     */
    public Cooldown getCooldown() {
        return cooldown;
    }

    /**
     * Returns whether the slash command has a cooldown.
     * @return True if the slash command has a cooldown, false otherwise
     */
    public boolean hasCooldown() {
        return cooldown != null;
    }

    /**
     * Sets the options of the slash command.
     * @param options A List containing all options of the slash command
     */
    public void setOptions(List<OptionData> options) {
        this.options = options;
    }

    /**
     * Returns all options of the slash command.
     * @return A List containing all options of the slash command
     */
    public List<OptionData> getOptions() {
        return options;
    }

    /**
     * Returns whether the slash command has options.
     * @return True if the slash command has options, false otherwise
     */
    public boolean hasOptions() {
        return !options.isEmpty();
    }

    /**
     * Adds an option to the slash command.
     * @param option The option to add
     */
    public void addOption(OptionData option) {
        options.add(option);
    }

    /**
     * Adds multiple options to the slash command.
     * @param options A List containing all options to add
     */
    public void addOptions(List<OptionData> options) {
        this.options.addAll(options);
    }

    /**
     * Sets the subcommands of the slash command.
     * @param subcommands A List containing all subcommands of the slash command
     */
    public void setSubcommands(List<SubcommandData> subcommands) {
        this.subcommands = subcommands;
    }

    /**
     * Returns all subcommands of the slash command.
     * @return A List containing all subcommands of the slash command
     */
    public List<SubcommandData> getSubcommands() {
        return subcommands;
    }

    /**
     * Returns whether the slash command has subcommands.
     * @return True if the slash command has subcommands, false otherwise
     */
    public boolean hasSubcommands() {
        return !subcommands.isEmpty();
    }

    /**
     * Sets the subcommand groups of the slash command.
     * @param subcommandGroups A List containing all subcommand groups of the slash command
     */
    public void setSubcommandGroups(List<SubcommandGroupData> subcommandGroups) {
        this.subcommandGroups = subcommandGroups;
    }

    /**
     * Returns all subcommand groups of the slash command.
     * @return A List containing all subcommand groups of the slash command
     */
    public List<SubcommandGroupData> getSubcommandGroups() {
        return subcommandGroups;
    }

    /**
     * Returns whether the slash command has subcommand groups.
     * @return True if the slash command has subcommand groups, false otherwise
     */
    public boolean hasSubcommandGroups() {
        return !subcommandGroups.isEmpty();
    }

    /**
     * Sets the help message of the slash command.
     * The help message is displayed when using the in built help command.
     * @param helpMessage The help message of the slash command
     */
    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    /**
     * Returns the help message of the slash command.
     * @return The help message of the slash command
     */
    public String getHelpMessage() {
        return helpMessage;
    }

    /**
     * Returns whether the slash command has a help message.
     * @return True if the slash command has a help message, false otherwise
     */
    public boolean hasHelpMessage() {
        return helpMessage != null;
    }
}
