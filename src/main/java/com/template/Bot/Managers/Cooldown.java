package com.template.Bot.Managers;

import com.template.Bot.Managers.Command.SlashCommand;
import com.template.Bot.Managers.Interaction.ContextInteraction;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Cooldown class
 *
 * @param time     The length of the cooldown
 * @param timeType The time type of the cooldown (seconds, minutes, hours, days)
 * @param scope    The scope of the cooldown (user, channel, guild)
 */
public record Cooldown(int time, Cooldown.Time timeType, Cooldown.Scope scope) {

    /**
     * @return The length of the cooldown in milliseconds
     */
    public long getTimeInMS() {
        return time * Time.getTimeInMS(timeType);
    }

    /**
     * @return The {@link Time} of the Cooldown as a string
     */
    public String getTimeString() {
        return time + " " + timeType.toString().toLowerCase();
    }

    /**
     * @return The {@link Scope} of the Cooldown as a string
     */
    public String getScopeString() {
        return scope.toString().toLowerCase();
    }

    /**
     * Checks if a specified target is currently on cooldown for a given {@link SlashCommand}.
     * @param event The {@link SlashCommandInteractionEvent} for which the cooldown status is checked.
     * @param command The {@link SlashCommand} for which the cooldown status is checked.
     * @return True if the target is on cooldown; false otherwise.
     */
    public boolean isOnCooldown(SlashCommandInteractionEvent event, SlashCommand command) {
        Scope scope = command.getCooldown().scope();
        switch (scope) {
            case USER -> {
                return CooldownManager.isOnCooldown(event.getUser().getId(), command);
            }
            case CHANNEL -> {
                return CooldownManager.isOnCooldown(event.getChannel().getId(), command);
            }
            case GUILD -> {
                return CooldownManager.isOnCooldown(event.getGuild().getId(), command);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Checks if a specified target is currently on cooldown for a given {@link ContextInteraction}.
     * @param event The {@link MessageContextInteractionEvent} for which the cooldown status is checked.
     * @param interaction The {@link ContextInteraction} for which the cooldown status is checked.
     * @return True if the target is on cooldown; false otherwise.
     */
    public boolean isOnCooldown(MessageContextInteractionEvent event, ContextInteraction interaction) {
        Scope scope = interaction.getCooldown().scope();
        switch (scope) {
            case USER -> {
                return CooldownManager.isOnCooldown(event.getUser().getId(), interaction);
            }
            case CHANNEL -> {
                return CooldownManager.isOnCooldown(event.getChannel().getId(), interaction);
            }
            case GUILD -> {
                return CooldownManager.isOnCooldown(event.getGuild().getId(), interaction);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Checks if a specified target is currently on cooldown for a given {@link ContextInteraction}.
     * @param event The {@link UserContextInteractionEvent} for which the cooldown status is checked.
     * @param interaction The {@link ContextInteraction} for which the cooldown status is checked.
     * @return True if the target is on cooldown; false otherwise.
     */
    public boolean isOnCooldown(UserContextInteractionEvent event, ContextInteraction interaction) {
        Scope scope = interaction.getCooldown().scope();
        switch (scope) {
            case USER -> {
                return CooldownManager.isOnCooldown(event.getUser().getId(), interaction);
            }
            case CHANNEL -> {
                return CooldownManager.isOnCooldown(event.getChannel().getId(), interaction);
            }
            case GUILD -> {
                return CooldownManager.isOnCooldown(event.getGuild().getId(), interaction);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Retrieves the end time of a cooldown for a specified target, {@link SlashCommand}.
     * @param event The {@link SlashCommandInteractionEvent} for which the cooldown is being checked.
     * @param command The {@link SlashCommand} for which the cooldown is being checked.
     * @return The end time of the cooldown for the specified target and command, as a relative {@link TimeFormat}.
     */
    public String getEndTimeRelative(SlashCommandInteractionEvent event, SlashCommand command) {
        Scope scope = command.getCooldown().scope();
        switch (scope) {
            case USER -> {
                long endTime = CooldownManager.getEndTime(event.getUser().getId(), command.getDefaultCommandName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            case CHANNEL -> {
                long endTime = CooldownManager.getEndTime(event.getChannel().getId(), command.getDefaultCommandName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            case GUILD -> {
                long endTime = CooldownManager.getEndTime(event.getGuild().getId(), command.getDefaultCommandName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Retrieves the end time of a cooldown for a specified target, {@link ContextInteraction}.
     * @param event The {@link MessageContextInteractionEvent} for which the cooldown is being checked.
     * @param interaction The {@link ContextInteraction} for which the cooldown is being checked.
     * @return The end time of the cooldown for the specified target and command, as a relative {@link TimeFormat}.
     */

    public String getEndTimeRelative(MessageContextInteractionEvent event, ContextInteraction interaction) {
        Scope scope = interaction.getCooldown().scope();
        switch (scope) {
            case USER -> {
                long endTime = CooldownManager.getEndTime(event.getUser().getId(), interaction.getDefaultInteractionName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            case CHANNEL -> {
                long endTime = CooldownManager.getEndTime(event.getChannel().getId(), interaction.getDefaultInteractionName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            case GUILD -> {
                long endTime = CooldownManager.getEndTime(event.getGuild().getId(), interaction.getDefaultInteractionName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Retrieves the end time of a cooldown for a specified target, {@link ContextInteraction}.
     * @param event The {@link UserContextInteractionEvent} for which the cooldown is being checked.
     * @param interaction The {@link ContextInteraction} for which the cooldown is being checked.
     * @return The end time of the cooldown for the specified target and command, as a relative {@link TimeFormat}.
     */

    public String getEndTimeRelative(UserContextInteractionEvent event, ContextInteraction interaction) {
        Scope scope = interaction.getCooldown().scope();
        switch (scope) {
            case USER -> {
                long endTime = CooldownManager.getEndTime(event.getUser().getId(), interaction.getDefaultInteractionName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            case CHANNEL -> {
                long endTime = CooldownManager.getEndTime(event.getChannel().getId(), interaction.getDefaultInteractionName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            case GUILD -> {
                long endTime = CooldownManager.getEndTime(event.getGuild().getId(), interaction.getDefaultInteractionName(), scope);
                return TimeFormat.RELATIVE.format(endTime);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Cooldown Scope
     * <p>
     *     USER: The cooldown is only for the user
     *     CHANNEL: The cooldown is only for the channel
     *     GUILD: The cooldown is only for the guild
     *     UNKNOWN: Unknown scope
     *    </p>
     */
    public enum Scope {

        USER,
        CHANNEL,
        GUILD,
        UNKNOWN;

        public static Scope getScope(String scope) {
            return switch (scope.toLowerCase()) {
                case "user" -> USER;
                case "guild" -> GUILD;
                case "channel" -> CHANNEL;
                default -> UNKNOWN;
            };
        }
    }

    /**
     * Cooldown Time
     * <p>
     *     SECONDS: The cooldown is in seconds
     *     MINUTES: The cooldown is in minutes
     *     HOURS: The cooldown is in hours
     *     DAYS: The cooldown is in days
     *     UNKNOWN: Unknown time
     *    </p>
     */
    public enum Time {

        SECONDS,
        MINUTES,
        HOURS,
        DAYS,
        UNKNOWN;

        public static Time getTime(String time) {
            return switch (time.toLowerCase()) {
                case "seconds" -> SECONDS;
                case "minutes" -> MINUTES;
                case "hours" -> HOURS;
                case "days" -> DAYS;
                default -> UNKNOWN;
            };
        }

        public static long getTimeInMS(Time time) {
            return switch (time) {
                case SECONDS -> 1000;
                case MINUTES -> 60000;
                case HOURS -> 3600000;
                case DAYS -> 86400000;
                default -> 0;
            };
        }
    }

    /**
     * Cooldown Manager
     * <p>
     *     This class manages the cooldowns
     *   </p>
     */
    private static class CooldownManager {

        private static final Map<String, Map<String, Long>> globalUserCooldowns = new HashMap<>();
        private static final Map<String, Map<String, Long>> globalChannelCooldowns = new HashMap<>();
        private static final Map<String, Map<String, Long>> globalServerCooldowns = new HashMap<>();

        /**
         * Checks if a specified target is currently on cooldown for a given {@link SlashCommand}.
         *
         * @param targetId The unique identifier of the target (e.g., user ID, channel ID, guild ID).
         * @param command  The {@link SlashCommand} for which the cooldown status is checked.
         * @return True if the target is on cooldown; false otherwise.
         *
         * <p>This method determines whether a specified target is currently on cooldown for the given
         * SlashCommand. If the SlashCommand has no cooldown set (null), the target is considered not
         * on cooldown. Otherwise, the method checks the cooldown scope, retrieves the corresponding
         * cooldown map (targetMap), and determines if the target is on cooldown based on the cooldown
         * end time.</p>
         *
         * <p>If the target is not on cooldown, a new cooldown is set with the specified duration.</p>
         *
         * <p>Usage example:</p>
         * <pre>
         * {@code
         * boolean onCooldown = isOnCooldown("123456", mySlashCommand);
         * if (onCooldown) {
         *     System.out.println("The user with ID 123456 is on cooldown for the command: " + mySlashCommand.getDefaultCommandName());
         * } else {
         *     System.out.println("The user with ID 123456 is not on cooldown for the command: " + mySlashCommand.getDefaultCommandName());
         * }
         * }
         * </pre>
         */

        public static boolean isOnCooldown(String targetId, SlashCommand command) {

            if (command.getCooldown() == null) {
                return false;
            }

            Scope scope = command.getCooldown().scope();
            Map<String, Map<String, Long>> targetMap;
            switch (scope) {
                case USER -> targetMap = globalUserCooldowns;
                case CHANNEL -> targetMap = globalChannelCooldowns;
                case GUILD -> targetMap = globalServerCooldowns;
                default -> {
                    return false;
                }
            }

            long currentTime = new Date().getTime();
            long endTime = currentTime + command.getCooldown().getTimeInMS();

            if (!targetMap.containsKey(targetId)) {
                targetMap.put(targetId, new HashMap<>());
                setCooldown(targetId, command.getDefaultCommandName(), endTime, scope);
                return false;
            }

            long oldEndTime = getEndTime(targetId, command.getDefaultCommandName(), scope);

            if (oldEndTime < currentTime) {
                setCooldown(targetId, command.getDefaultCommandName(), endTime, scope);
                return false;
            }
            return true;
        }

        /**
         * Checks if a specified target is currently on cooldown for a given {@link ContextInteraction}.
         *
         * @param targetId    The unique identifier of the target (e.g., user ID, channel ID, guild ID).
         * @param interaction The {@link ContextInteraction} for which the cooldown status is checked.
         * @return True if the target is on cooldown; false otherwise.
         *
         * <p>This method determines whether a specified target is currently on cooldown for the given
         * SlashCommand. If the SlashCommand has no cooldown set (null), the target is considered not
         * on cooldown. Otherwise, the method checks the cooldown scope, retrieves the corresponding
         * cooldown map (targetMap), and determines if the target is on cooldown based on the cooldown
         * end time.</p>
         *
         * <p>If the target is not on cooldown, a new cooldown is set with the specified duration.</p>
         *
         * <p>Usage example:</p>
         * <pre>
         * {@code
         * boolean onCooldown = isOnCooldown("123456", myContextInteraction);
         * if (onCooldown) {
         *     System.out.println("The user with ID 123456 is on cooldown for the ContextInteraction: " + myContextInteraction.getDefaultCommandName());
         * } else {
         *     System.out.println("The user with ID 123456 is not on cooldown for the ContextInteraction: " + myContextInteraction.getDefaultCommandName());
         * }
         * }
         * </pre>
         */
        public static boolean isOnCooldown(String targetId, ContextInteraction interaction) {

            if (interaction.getCooldown() == null) {
                return false;
            }

            Scope scope = interaction.getCooldown().scope();
            Map<String, Map<String, Long>> targetMap;
            switch (scope) {
                case USER -> targetMap = globalUserCooldowns;
                case CHANNEL -> targetMap = globalChannelCooldowns;
                case GUILD -> targetMap = globalServerCooldowns;
                default -> {
                    return false;
                }
            }

            long currentTime = new Date().getTime();
            long endTime = currentTime + interaction.getCooldown().getTimeInMS();

            if (!targetMap.containsKey(targetId)) {
                targetMap.put(targetId, new HashMap<>());
                setCooldown(targetId, interaction.getDefaultInteractionName(), endTime, scope);
                return false;
            }
            return true;
        }

        /**
         * Retrieves the end time of a cooldown for a specified target, {@link SlashCommand} or {@link ContextInteraction}, and {@link Scope}.
         *
         * @param targetId      The unique identifier of the target (e.g., user ID, channel ID, guild ID).
         * @param operationName The name of the {@link SlashCommand} or {@link ContextInteraction} for which the cooldown is being checked.
         * @param scope         The {@link Scope} of the cooldown (USER, CHANNEL, GUILD).
         * @return The end time of the cooldown for the specified target and command, or 0 if no cooldown is found.
         *
         *
         * <p>This method looks up the cooldown information for a specific target, command, and scope.
         * It determines the scope and retrieves the corresponding cooldown map (targetMap).
         * If the target does not exist in the map, or if the command is not present for the target,
         * the method returns 0, indicating that there is no cooldown set.
         * If a cooldown is found, the method returns the end time of the cooldown.</p>
         *
         * <p>Usage Example:</p>
         * <pre>
         * {@code
         * long endTime = CooldownManager.getEndTime(event.getUser().getId(), command.getDefaultCommandName(), scope);
         * }
         * </pre>
         */
        private static long getEndTime(String targetId, String operationName, Cooldown.Scope scope) {
            Map<String, Map<String, Long>> targetMap;
            switch (scope) {
                case USER -> targetMap = globalUserCooldowns;
                case CHANNEL -> targetMap = globalChannelCooldowns;
                case GUILD -> targetMap = globalServerCooldowns;
                default -> {
                    return 0;
                }
            }
            if (!targetMap.containsKey(targetId)) {
                return 0;
            }
            if (!targetMap.get(targetId).containsKey(operationName)) {
                return 0;
            } else {
                return targetMap.get(targetId).get(operationName);
            }
        }

        /**
         * Sets a cooldown for a specific target, {@link SlashCommand} or {@link ContextInteraction}, and scope.
         *
         * @param targetId      The unique identifier of the target (e.g., user ID, channel ID, guild ID).
         * @param operationName The name representing the {@link SlashCommand} or {@link ContextInteraction} for which the cooldown is being set.
         * @param cooldownTime  The duration of the cooldown in milliseconds.
         * @param scope         The {@link Scope} of the cooldown (USER, CHANNEL, GUILD).
         *
         *                      <p>This method sets a cooldown for a particular target, operation, and scope. The cooldown
         *                      duration is specified in milliseconds. The method first determines the scope and retrieves
         *                      the corresponding cooldown map (targetMap). If the target does not exist in the map, a new
         *                      entry is created. Subsequently, the method associates the specified operation with the
         *                      provided cooldown time.</p>
         *
         *                      <p>Usage example:</p>
         *                      <pre>
         *                                           {@code
         *                                           setCooldown("123456", "exampleOperation", 5000, Cooldown.Scope.USER);
         *                                           System.out.println("Cooldown set for the user with ID 123456 for the operation 'exampleOperation'.");
         *                                           }
         *                                           </pre>
         * @see Cooldown.Scope
         */
        private static void setCooldown(String targetId, String operationName, long cooldownTime, Cooldown.Scope scope) {
            Map<String, Map<String, Long>> targetMap;
            switch (scope) {
                case USER -> targetMap = globalUserCooldowns;
                case CHANNEL -> targetMap = globalChannelCooldowns;
                case GUILD -> targetMap = globalServerCooldowns;
                default -> {
                    return;
                }
            }
            if (!targetMap.containsKey(targetId)) {
                targetMap.put(targetId, new HashMap<>());
            }
            targetMap.get(targetId).put(operationName, cooldownTime);
        }
    }
}
