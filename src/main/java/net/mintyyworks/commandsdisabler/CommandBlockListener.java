package net.mintyyworks.commandsdisabler;

import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class CommandBlockListener implements Listener {

    private static final String vanilla_simple_mesg =
            "&cUnknown command. Type \"/help\" for help.";

    private static final String vanilla_detailed_mesg = vanilla_simple_mesg;

    private final CommandsDisabler plugin;

    public CommandBlockListener(CommandsDisabler plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("commandsdisabler.bypass")) {
            return;
        }

        String message = event.getMessage();
        if (message.length() < 2) {
            return;
        }

        String label = message.substring(1).split(" ", 2)[0].toLowerCase(Locale.ROOT);

        int namespaceIndex = label.indexOf(':');
        String baseCommand = namespaceIndex >= 0 ? label.substring(namespaceIndex + 1) : label;

        if (baseCommand.isEmpty()) {
            return;
        }

        if (plugin.isDisabled(baseCommand)) {
            event.setCancelled(true);

            String denyMessage = plugin.getDenyMessage();
            if (denyMessage != null && !denyMessage.isEmpty()) {
                player.sendMessage(CommandsDisabler.color(denyMessage));
                return;
            }

            if ("detailed".equals(plugin.getVanillaStyle())) {
                player.sendMessage(CommandsDisabler.color(vanilla_detailed_mesg));
                player.sendMessage(CommandsDisabler.color("&7" + message + "&c<--[HERE]"));
            } else {
                player.sendMessage(CommandsDisabler.color(vanilla_simple_mesg));
            }
        }
    }
}
