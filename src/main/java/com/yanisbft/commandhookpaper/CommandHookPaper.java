package com.yanisbft.commandhookpaper;

import com.yanisbft.commandhookpaper.mapping.MojangMapping;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHookPaper extends JavaPlugin {

  @Override
  public void onEnable() {
    CommandBlockListener listener = new CommandBlockListener(new MojangMapping());
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
