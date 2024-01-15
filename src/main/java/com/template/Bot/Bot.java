package com.template.Bot;

import com.template.Bot.Managers.Command.CommandManager;
import com.template.Bot.Managers.Interaction.InteractionManager;
import com.template.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Properties;

public class Bot {

    private static final Properties discord = Main.discord;
    static final Logger logger = LoggerFactory.getLogger(Bot.class);
    public static DefaultShardManagerBuilder builder;
    public static ShardManager shardManager;
    private static CommandManager commandManager;
    private static InteractionManager interactionManager;

    public static void stopDiscordBot() {
        shardManager.shutdown();
        logger.info("Bot stopped");
    }

    public static void startDiscordBot() throws InterruptedException {
        builder = DefaultShardManagerBuilder.create(discord.getProperty("discord.bot.token"),
                // This is a privileged gateway intent that is used to update user information and join/leaves (including kicks). This is required to cache all members of a guild (including chunking)
                GatewayIntent.GUILD_MEMBERS,
                // This will only track guild webhook create/update/delete.
                GatewayIntent.GUILD_WEBHOOKS,
                // This is used to receive incoming messages in guilds (servers), most bots will need this for commands.
                GatewayIntent.GUILD_MESSAGES,
                // This is used to track reactions on messages in guilds (servers).
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                // This is used to receive incoming messages in private channels (DMs).
                GatewayIntent.DIRECT_MESSAGES,
                // This is used to track reactions on messages in private channels (DMs).
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.MESSAGE_CONTENT);
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.FORUM_TAGS, CacheFlag.ONLINE_STATUS, CacheFlag.SCHEDULED_EVENTS, CacheFlag.STICKER, CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.ONLINE);
        ChunkingFilter chunkingFilter = ChunkingFilter.include(Long.parseLong(discord.getProperty("discord.server.id")));
        builder.setChunkingFilter(chunkingFilter);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setLargeThreshold(250);
        builder.setAutoReconnect(true);
        builder.setShardsTotal(-1);

        String activity = discord.getProperty("discord.bot.activityName");
        String activityType = discord.getProperty("discord.bot.activityType");
        switch (activityType) {
            case "WATCHING" -> builder.setActivity(Activity.watching(activity));
            case "LISTENING" -> builder.setActivity(Activity.listening(activity));
            case "STREAMING" -> builder.setActivity(Activity.streaming(activity, "https://www.twitch.tv/evolutionbot"));
            case "COMPETING" -> builder.setActivity(Activity.competing(activity));
            default -> builder.setActivity(Activity.customStatus(activity));
        }

        commandManager = new CommandManager();
        interactionManager = new InteractionManager();
        builder.addEventListeners(
                commandManager,
                interactionManager
        );

        builder.addEventListeners(new ListenerAdapter() {
            @Override
            public void onReady(@NotNull ReadyEvent event) {
                super.onReady(event);
                logger.info("Bot is ready");
                registerEverything();
            }
        });

        shardManager = builder.build();

    }

    public static JDA getJDA(int shardId) {
        return shardManager.getShardById(shardId);
    }

    public static JDA getJDA() {
        return shardManager.getShardById(0);
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }

    public static DefaultShardManagerBuilder getBuilder() {
        return builder;
    }

    public boolean isRunning() {
        return shardManager != null;
    }

    public static void restartDiscordBot() throws InterruptedException {
        logger.info("Restarting bot");
        stopDiscordBot();
        logger.info("Bot stopped");
        startDiscordBot();
        logger.info("Bot started");
    }

    private static void registerEverything() {

        commandManager.registerCommands();
        interactionManager.registerInteractions();

        Collection<CommandData> commands = commandManager.getCommandData();
        Collection<CommandData> homeGuildCommands = commandManager.getHomeGuildCommandData();
        Collection<CommandData> interactions = interactionManager.getInteractionData();
        Collection<CommandData> homeGuildInteractions = interactionManager.getHomeGuildInteractionData();

        commands.addAll(interactions);
        homeGuildCommands.addAll(homeGuildInteractions);

        getHomeGuild().updateCommands().addCommands(homeGuildCommands).complete();
        getJDA().updateCommands().addCommands(commands).complete();

        logger.info("Global Commands:");
        getJDA().retrieveCommands().queue(jdaCommands -> {
            for (Command jdaCommand : jdaCommands) {
                logger.info("Command Name: {}, Description: {}, Options: {}",
                        jdaCommand.getName(),
                        jdaCommand.getDescription(),
                        jdaCommand.getOptions());
            }
        });

        logger.info("Home Guild Commands:");
        getHomeGuild().retrieveCommands().queue(guildCommands -> {
            for (Command guildCommand : guildCommands) {
                logger.info("Command Name: {}, Description: {}, Options: {}",
                        guildCommand.getName(),
                        guildCommand.getDescription(),
                        guildCommand.getOptions());
            }
        });

    }
    public static Guild getHomeGuild() {
        return getJDA().getGuildById(Main.discord.get("discord.server.id").toString());
    }
}
