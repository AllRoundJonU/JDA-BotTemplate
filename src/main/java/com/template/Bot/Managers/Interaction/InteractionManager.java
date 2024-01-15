package com.template.Bot.Managers.Interaction;


import com.template.Bot.Bot;
import com.template.Language.LanguageUtils;
import com.template.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class InteractionManager extends ListenerAdapter {

    final Logger logger = LoggerFactory.getLogger(InteractionManager.class);

    private final ArrayList<ContextInteraction> interactions = new ArrayList<>();
    private final Collection<CommandData> interactionData = new ArrayList<>();
    private final Collection<CommandData> homeGuildInteractionData = new ArrayList<>();


    public InteractionManager() {
        String basePackage = Main.basePackage;

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(basePackage))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(basePackage))));

        Set<Class<? extends ContextInteraction>> interactionClasses = reflections.getSubTypesOf(ContextInteraction.class);

        for (Class<? extends ContextInteraction> interactionClass : interactionClasses){
            try {
                Constructor<? extends ContextInteraction> constructor = interactionClass.getDeclaredConstructor();
                ContextInteraction interaction = constructor.newInstance();

                registerInteraction(interaction);

                logger.info("Automatically registered interaction: " + interaction.getDefaultInteractionName());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                logger.error("Failed to register command from class: " + interactionClass.getName(), e);
            }
        }
    }

    public void registerInteraction(ContextInteraction interaction){
        interactions.add(interaction);
    }

    public ArrayList<ContextInteraction> getInteractions() {
        return interactions;
    }

    public Collection<CommandData> getInteractionData() {
        return interactionData;
    }

    public Collection<CommandData> getHomeGuildInteractionData() {
        return homeGuildInteractionData;
    }
    public void registerInteractions(){
        JDA jda = Bot.getJDA();

        for (ContextInteraction interaction : interactions) {

            CommandData currentInteractionData = Commands.context(interaction.getType(),
                    interaction.getDefaultInteractionName());

            if (interaction.hasAdditionalNames()){
                currentInteractionData.setNameLocalizations(interaction.getInteractionNames());
            }

            if (interaction.getDefaultMemberPermissions() != null){
                currentInteractionData.setDefaultPermissions(interaction.getDefaultMemberPermissions());
            }

            currentInteractionData.setGuildOnly(interaction.isGuildOnly());
            currentInteractionData.setNSFW(interaction.isNSFW());

            if (interaction.isHomeGuildOnly()) {
                homeGuildInteractionData.add(currentInteractionData);
            }else{
                interactionData.add(currentInteractionData);
            }

            logger.info(String.format("Registered interaction: %s", interaction.getDefaultInteractionName()));
        }
    }

    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {
        interactions.stream()
                .filter(interaction -> interaction.getDefaultInteractionName().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresentOrElse(interaction -> {

                    if (interaction.hasCooldown() && interaction.getCooldown().isOnCooldown(event, interaction)) {
                        Locale userLocale = event.getUserLocale().toLocale();
                        String message = LanguageUtils.getLanguageString("languages.bot.global", "interaction.cooldown.response", userLocale);

                        String timeRelative = interaction.getCooldown().getEndTimeRelative(event, interaction);

                        event.reply(String.format(message, timeRelative)).setEphemeral(true).queue();
                        return;
                    }
                    interaction.execute(event);
                }, () -> event.reply("This Command is currently not available").setEphemeral(true).queue());
    }

    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event){
        interactions.stream()
                .filter(interaction -> interaction.getDefaultInteractionName().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresentOrElse(interaction -> {

                    if (interaction.hasCooldown() && interaction.getCooldown().isOnCooldown(event, interaction)) {
                        Locale userLocale = event.getUserLocale().toLocale();
                        String message = LanguageUtils.getLanguageString("languages.bot.global", "interaction.cooldown.response", userLocale);

                        String timeRelative = interaction.getCooldown().getEndTimeRelative(event, interaction);

                        event.reply(String.format(message, timeRelative)).setEphemeral(true).queue();
                        return;
                    }
                    interaction.execute(event);
                }, () -> event.reply("This Command is currently not available").setEphemeral(true).queue());
    }
}
