/**
 * Author: _NewAge
 * https://github.com/NewAgeCZ/CommandHook/blob/master/modules/mapping/paper/mojang-mapped/src/main/java/org/bitbucket/_newage/commandhook/mapping/MojangMapping.java
 */
package com.yanisbft.commandhookpaper.mapping;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MojangMapping implements Mapping {

  @Override
  public List<Entity> getEntitiesFromSelector(String selector, Block commandBlock) {
    List<Entity> entities = Collections.emptyList();

    CommandSourceStack wrapper = getCommandListenerWrapper(commandBlock);
    EntitySelectorParser parser = getArgumentParser(selector);

    try {
      if (wrapper != null) {
        List<? extends net.minecraft.world.entity.Entity> nmsEntities = getNmsEntities(parser, wrapper);
        entities = convertToBukkitEntity(nmsEntities);
      }
    } catch (CommandSyntaxException ignored) {
    }

    return entities;
  }

  @Override
  public CommandSourceStack getCommandListenerWrapper(Block block) {
    Level world = ((CraftWorld) block.getWorld()).getHandle();
    BlockPos blockPosition = new BlockPos(block.getX(), block.getY(), block.getZ());

    Optional<CommandBlockEntity> optionalBlockEntity = world.getBlockEntity(blockPosition, BlockEntityType.COMMAND_BLOCK);
    if (optionalBlockEntity.isPresent()) {
      BaseCommandBlock commandBlockListenerAbstract = optionalBlockEntity.get().getCommandBlock();
      return commandBlockListenerAbstract.createCommandSourceStack();
    }

    return null;
  }

  @Override
  public EntitySelectorParser getArgumentParser(String selector) {
    StringReader stringReader = new StringReader(selector);
    return new EntitySelectorParser(stringReader, true);
  }

  private List<? extends net.minecraft.world.entity.Entity> getNmsEntities(EntitySelectorParser argumentParser, CommandSourceStack wrapper) throws CommandSyntaxException {
    EntitySelector selector = argumentParser.parse(false);
    return selector.findEntities(wrapper);
  }

  private List<Entity> convertToBukkitEntity(List<? extends net.minecraft.world.entity.Entity> entities) {
    return entities.stream()
      .map(net.minecraft.world.entity.Entity::getBukkitEntity)
      .collect(Collectors.toList());
  }
}
