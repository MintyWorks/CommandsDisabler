package net.mintyyworks.commandsdisabler;

import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

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

                String withoutSlash = message.startsWith("/") ? message.substring(1) : message;
                Component errorLine = Component.text(withoutSlash + "<--[HERE]")
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.suggestCommand(message));

                player.sendMessage(errorLine);
            } else {
                player.sendMessage(CommandsDisabler.color(vanilla_simple_mesg));
            }
        }
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("commandsdisabler.bypass")) {
            return;
        }

        event.getCommands().removeIf(command -> {
            int namespaceIndex = command.indexOf(':');
            String baseCommand = namespaceIndex >= 0 ? command.substring(namespaceIndex + 1) : command;
            return plugin.isDisabled(baseCommand);
        });
    }
}
