package com.template.Language;

import net.dv8tion.jda.api.interactions.DiscordLocale;

import java.util.*;

public class LanguageUtils {

    private static final Map<String, ResourceBundle> bundleCache = new HashMap<>();

    public static String getLanguageString(String bundleName, String key, Locale language) {
        ResourceBundle localBundle = getCachedBundle(bundleName, language);

        try {
            if (localBundle.containsKey(key) && !localBundle.getString(key).isBlank()) return localBundle.getString(key);
            else return key;
        } catch (Exception e) {
            // Schlüssel nicht gefunden, hier können Sie einen Standardwert oder den Schlüssel selbst zurückgeben
            return key;
        }
    }

    public static HashMap<DiscordLocale, String> generateCommandNameMap(String commandDefaultName) {
        HashMap<DiscordLocale, String> commandNameMap = new HashMap<>();
        List<Locale> availableLocales = getAvailableLocales("languages.commands" + "." + commandDefaultName);
        for (Locale locale : availableLocales) {
            commandNameMap.put(LocalToDiscordLocal(locale), getLanguageString("languages.commands" + "." + commandDefaultName, "command.name", locale));
        }
        for (DiscordLocale discordLocale : DiscordLocale.values()) {
            if (discordLocale.equals(DiscordLocale.UNKNOWN)) continue;
            if (!commandNameMap.containsKey(discordLocale)) {
                commandNameMap.put(discordLocale, getLanguageString("languages.commands" + "." + commandDefaultName, "command.name", Locale.ENGLISH));
            }
        }
        return commandNameMap;
    }

    public static HashMap<DiscordLocale, String> generateCommandDescriptionMap(String commandDefaultName){
        HashMap<DiscordLocale, String> commandDescriptionMap = new HashMap<>();
        List<Locale> availableLocales = getAvailableLocales("languages.commands" + "." + commandDefaultName);
        for (Locale locale : availableLocales) {
            commandDescriptionMap.put(LocalToDiscordLocal(locale), getLanguageString("languages.commands" + "." + commandDefaultName, "command.description", locale));
        }
        for (DiscordLocale discordLocale : DiscordLocale.values()) {
            if (discordLocale.equals(DiscordLocale.UNKNOWN)) continue;
            if (!commandDescriptionMap.containsKey(discordLocale)) {
                commandDescriptionMap.put(discordLocale, getLanguageString("languages.commands" + "." + commandDefaultName, "command.description", Locale.ENGLISH));
            }
        }
        return commandDescriptionMap;
    }

    public static HashMap<DiscordLocale, String> generateLanguageMap(String bundleName, String key){
        HashMap<DiscordLocale, String> languageMap = new HashMap<>();
        List<Locale> availableLocales = getAvailableLocales(bundleName);
        for (Locale locale : availableLocales) {
            languageMap.put(LocalToDiscordLocal(locale), getLanguageString(bundleName, key, locale));
        }
        for (DiscordLocale discordLocale : DiscordLocale.values()) {
            if (discordLocale.equals(DiscordLocale.UNKNOWN)) continue;
            if (!languageMap.containsKey(discordLocale)) {
                languageMap.put(discordLocale, getLanguageString(bundleName, key, Locale.ENGLISH));
            }
        }
        return languageMap;
    }

    private static ResourceBundle getCachedBundle(String bundleName, Locale language) {
        String cacheKey = bundleName + "_" + language.toString();

        if (bundleCache.containsKey(cacheKey)) {
            return bundleCache.get(cacheKey);
        } else {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, language, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT));
            bundleCache.put(cacheKey, bundle);
            return bundle;
        }
    }

    private static List<Locale> getAvailableLocales(String bundleName) {
        List<Locale> availableLocales = new ArrayList<>();

        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                ResourceBundle.getBundle(bundleName, locale, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT));
                availableLocales.add(locale);
            } catch (MissingResourceException e) {
                // ignore
            }
        }
        return availableLocales;
    }

    private static DiscordLocale LocalToDiscordLocal(Locale locale) {
        for (DiscordLocale discordLocale : DiscordLocale.values()) {
            if (discordLocale.toLocale().equals(locale)) {
                return discordLocale;
            }
        }
        return DiscordLocale.ENGLISH_US;
    }


}
