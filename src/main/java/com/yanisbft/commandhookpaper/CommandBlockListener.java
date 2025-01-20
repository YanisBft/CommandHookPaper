/**
 * Author: _NewAge
 * https://github.com/NewAgeCZ/CommandHook/blob/master/modules/plugin/src/main/java/org/bitbucket/_newage/commandhook/CommandBlockListener.java
 */
package com.yanisbft.commandhookpaper;

import com.yanisbft.commandhookpaper.mapping.Mapping;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.List;
import java.util.regex.Pattern;

public class CommandBlockListener implements Listener {
  private static final Pattern SELECTOR_PATTERN = Pattern.compile("@[aenprs]([^a-zA-Z0-9]|$)");
  private final Mapping mapping;

  public CommandBlockListener(Mapping mapping) {
    this.mapping = mapping;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onCommandBlockDispatch(ServerCommandEvent e) {
    if (e.getSender() instanceof BlockCommandSender) {
      String cmd = e.getCommand();
      if (cmd.startsWith("/")) {
        cmd = cmd.replaceFirst("/", "");
      }

      if (cmd.startsWith("minecraft:")) {
        return;
      }

      if (SELECTOR_PATTERN.matcher(cmd).find()) {
        String selector = getSelectorWithArguments(cmd);
        List<Entity> entities = mapping.getEntitiesFromSelector(selector, ((BlockCommandSender) e.getSender()).getBlock());

        for (Entity entity : entities) {
          if (entity == null) continue;
          Bukkit.dispatchCommand(e.getSender(), cmd.replace(selector, entity.getName()));
        }

        e.setCancelled(true);
      }
    }
  }

  /**
   * Parse command string
   * We need to look for selector and enclosed arguments
   * It may be nested in multiple levels of [] and also spaces can be used, so we cannot split by them.
   * An example of such command is @p[nbt={SelectedItem:{id:"minecraft:stone_sword",tag:{display:{Name:'[{"text":"Blade of the Outsider","italic":false,"color":"dark_gray","bold":true}]'}}}}]
   *
   * @param cmd command written in Command Block
   * @return Selector with arguments
   */
  public static String getSelectorWithArguments(String cmd) {
    String selector = cmd.substring(cmd.indexOf('@'));

    if (selector.length() > 3 && selector.charAt(2) == '[') {
      int startBrackets = 1;
      int endBrackets = 0;
      for (int i = 3; i < selector.length(); i++) {
        if (selector.charAt(i) == '[') {
          startBrackets++;
        } else if (selector.charAt(i) == ']') {
          endBrackets++;
        }

        if (startBrackets == endBrackets) {
          selector = selector.substring(0, i+1);
          break;
        }
      }
    } else {
      selector = selector.substring(0, 2);
    }

    return selector;
  }
}
