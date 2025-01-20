package com.yanisbft.commandhookpaper.mapping;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.List;

public interface Mapping {

  /**
   * Bukkit entities matching selector for further processing.
   *
   * @param selector selector in command block, e.g. @e[distance=..3,type=player]
   * @param commandBlock block in world that is certainly Command Block
   * @return list of entities matching given selector
   */
  List<Entity> getEntitiesFromSelector(String selector, Block commandBlock);

  CommandSourceStack getCommandListenerWrapper(Block block);

  EntitySelectorParser getArgumentParser(String selector);
}
