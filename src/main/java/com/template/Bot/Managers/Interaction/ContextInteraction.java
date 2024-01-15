package com.template.Bot.Managers.Interaction;

import com.template.Bot.Managers.Cooldown;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

import java.util.HashMap;
import java.util.Map;

/**
 * The ContextInteraction class is the base class for all ContextInteractions.
 * It contains all the information about the context interaction.
 * It also contains two execute methods, one for each type of interaction.
 */
public abstract class ContextInteraction {

    private final Command.Type type;
    private Map<DiscordLocale, String> interactionNames = new HashMap<>();
    private boolean guildOnly = false;
    private boolean homeGuildOnly = false;
    private boolean NSFW = false;
    private DefaultMemberPermissions defaultMemberPermissions;
    private Cooldown cooldown;

    /**
     * This method is called when the interaction is executed.
     * @param event The MessageContextInteractionEvent
     */
    public void execute(MessageContextInteractionEvent event){
        throw new UnsupportedOperationException("This command is not supported for this type of interaction");
    };

    /**
     * This method is called when the interaction is executed.
     * @param event The UserContextInteractionEvent
     */
    public void execute(UserContextInteractionEvent event){
        throw new UnsupportedOperationException("This command is not supported for this type of interaction");
    };

    /**
     * This constructor is used to create a new ContextInteraction.
     * @param type The type of interaction
     * @param defaultName The default name of the interaction
     */
    public ContextInteraction(Command.Type type, String defaultName) {
        this.type = type;
        this.interactionNames.put(DiscordLocale.ENGLISH_US, defaultName);
    }

    /**
     * Get the type of interaction
     * @return The {@link Command.Type} of the interaction
     */
    public Command.Type getType() {
        return type;
    }

    /**
     * Get the default name of the interaction
     * @return The default name of the interaction
     */
    public String getDefaultInteractionName(){
        return interactionNames.get(DiscordLocale.ENGLISH_US);
    }

    /**
     * Get the name of the interaction in a specific locale
     * @param locale The locale of the name
     * @return The name of the interaction in the specified locale
     */
    public String getInteractionName(DiscordLocale locale){
        return interactionNames.get(locale);
    }

    /**
     * Get the names of the interaction in all locales
     * @return A map of all the names of the interaction in all locales
     */
    public Map<DiscordLocale, String> getInteractionNames() {
        return interactionNames;
    }

    /**
     * Set the names of the interaction in all locales
     * @param interactionNames A map of all the names of the interaction in all locales
     */
    public void setInteractionNames(Map<DiscordLocale, String> interactionNames) {
        this.interactionNames = interactionNames;
    }

    /**
     * Add a name to the interaction in a specific locale
     * @param locale The locale of the name
     * @param name The name of the interaction in the specified locale
     */
    public void addInteractionName(DiscordLocale locale, String name){
        this.interactionNames.put(locale, name);
    }

    /**
     * Check if the interaction has additional names
     * @return True if the interaction has additional names
     */
    public boolean hasAdditionalNames(){
        return interactionNames.size() > 1;
    }

    /**
     * Set if the interaction is guild only
     * @param guildOnly True if the interaction is guild only
     */
    public void setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
    }

    /**
     * Check if the interaction is guild only
     * @return True if the interaction is guild only
     */
    public boolean isGuildOnly() {
        return guildOnly;
    }

    /**
     * Set if the interaction is home guild only
     * @param homeGuildOnly True if the interaction is home guild only
     */
    public void setHomeGuildOnly(boolean homeGuildOnly) {
        this.homeGuildOnly = homeGuildOnly;
    }

    /**
     * Check if the interaction is home guild only
     * @return True if the interaction is home guild only
     */
    public boolean isHomeGuildOnly() {
        return homeGuildOnly;
    }

    /**
     * Set if the interaction is NSFW
     * @param NSFW True if the interaction is NSFW
     */
    public void setNSFW(boolean NSFW) {
        this.NSFW = NSFW;
    }

    /**
     * Check if the interaction is NSFW
     * @return True if the interaction is NSFW
     */
    public boolean isNSFW() {
        return NSFW;
    }

    /**
     * Set the default member permissions
     * @param defaultMemberPermissions The default member permissions
     */
    public void setDefaultMemberPermissions(DefaultMemberPermissions defaultMemberPermissions) {
        this.defaultMemberPermissions = defaultMemberPermissions;
    }

    /**
     * Get the default member permissions
     * @return The default member permissions
     */
    public DefaultMemberPermissions getDefaultMemberPermissions() {
        return defaultMemberPermissions;
    }

    /**
     * Set the cooldown
     * @param cooldown The cooldown
     */
    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Get the cooldown
     * @return The cooldown
     */
    public Cooldown getCooldown() {
        return cooldown;
    }

    /**
     * Check if the interaction has a cooldown
     * @return True if the interaction has a cooldown
     */
    public boolean hasCooldown() {
        return cooldown != null;
    }
}
