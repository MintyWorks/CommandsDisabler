package net.mintyyworks.commandsdisabler;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandsDisabler extends JavaPlugin {

    private final Set<String> disabledCommands = new HashSet<>();
    private String denyMessage = "";
    private String vanillaStyle = "simple";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();

        getServer().getPluginManager().registerEvents(new CommandBlockListener(this), this);

        if (getCommand("commandsdisabler") != null) {
            getCommand("commandsdisabler").setExecutor(this);
        }
    }

    /**
     * (Re)loads disabled-commands and deny-message from config.yml.
     * Safe to call at any time, e.g. from the reload sub-command. xd
     */
    public void loadSettings() {
        reloadConfig();
        FileConfiguration config = getConfig();

        disabledCommands.clear();
        List<String> configured = config.getStringList("disabled-commands");
        for (String entry : configured) {
            if (entry == null || entry.isEmpty()) {
                continue;
            }
            disabledCommands.add(entry.toLowerCase(Locale.ROOT));
        }

        denyMessage = config.getString("deny-message", "");
        vanillaStyle = config.getString("vanilla-style", "simple").toLowerCase(Locale.ROOT);
    }

    public boolean isDisabled(String baseCommand) {
        return disabledCommands.contains(baseCommand.toLowerCase(Locale.ROOT));
    }

    public boolean isDisabled(String fullLabel, String baseCommand) {
        return disabledCommands.contains(fullLabel.toLowerCase(Locale.ROOT))
                || disabledCommands.contains(baseCommand.toLowerCase(Locale.ROOT));
    }

    public String getDenyMessage() {
        return denyMessage;
    }

    public String getVanillaStyle() {
        return vanillaStyle;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("commandsdisabler")) {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("commandsdisabler.admin")) {
                sender.sendMessage(color("&cYou do not have permission to do that."));
                return true;
            }
            loadSettings();
            sender.sendMessage(color("&aCommandsDisabler configuration reloaded."));
            return true;
        }

        sender.sendMessage(color("&eUsage: /" + label + " reload"));
        return true;
    }

    public static String color(String input) {
        return input == null ? "" : ChatColor.translateAlternateColorCodes('&', input);
    }
}
