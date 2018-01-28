package net.rocketeer.nomes.command;

import net.rocketeer.nomes.towny.TownyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class TownyPreprocessHook implements Listener {
  private final TownyDataManager mManager;
  private final HashMap<String, CommandExecutor> mTownyCmdMap;
  private final HashMap<String, CommandExecutor> mNationCmdMap;
  private final JavaPlugin mPlugin;

  public TownyPreprocessHook(JavaPlugin plugin, TownyDataManager manager) {
    mManager = manager;
    mTownyCmdMap = new HashMap<>();
    mTownyCmdMap.put("cower", new CowerCommand(manager));
    mTownyCmdMap.put("flaunt", new FlauntCommand(manager));
    mTownyCmdMap.put("respecttop", new RespectTopCommand(manager));

    mNationCmdMap = new HashMap<>();
    mNationCmdMap.put("spawn", new NationTeleportCommand(manager));
    mNationCmdMap.put("setspawn", new NationSetHomeCommand(manager));
    mPlugin = plugin;
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void onCommand(PlayerCommandPreprocessEvent event) {
    String lowerCmd = event.getMessage().substring(1).toLowerCase();
    String[] parts = lowerCmd.split(" ");
    if (parts.length < 2)
      return;
    String cmd = parts[0];
    Map<String, CommandExecutor> map = null;
    if (cmd.equals("n") || cmd.equals("nation")) {
      map = mNationCmdMap;
    } else if (cmd.equals("t") || cmd.equals("town")) {
      map = mTownyCmdMap;
    } else return;

    String subCmd = parts[1];
    Player player = event.getPlayer();
    String newName = parts.length > 2 ? parts[2] : "";
    if (subCmd.equals("new") && map.containsKey(newName)) {
      player.sendMessage(ChatColor.GOLD + "[Towny] " + ChatColor.RED + newName + " is an invalid name.");
      event.setCancelled(true);
      return;
    }

    if (subCmd.equals("?") && map == mTownyCmdMap) {
      Bukkit.getScheduler().runTaskLater(mPlugin, () -> {
        player.sendMessage("  " + ChatColor.RED + "Respect: " + ChatColor.DARK_AQUA + "/town " + ChatColor.AQUA + "flaunt");
        player.sendMessage("  " + ChatColor.RED + "Respect: " + ChatColor.DARK_AQUA + "/town " + ChatColor.AQUA + "cower");
        player.sendMessage("  " + ChatColor.RED + "Respect: " + ChatColor.DARK_AQUA + "/town " + ChatColor.AQUA + "respecttop");
      }, 1);
      return;
    } else if (subCmd.equals("?") && map == mNationCmdMap) {
      Bukkit.getScheduler().runTaskLater(mPlugin, () -> {
        player.sendMessage("  " + ChatColor.RED + "Spawn: " + ChatColor.DARK_AQUA + "/nation " + ChatColor.AQUA + "setspawn");
        player.sendMessage("  " + ChatColor.RED + "Spawn: " + ChatColor.DARK_AQUA + "/nation " + ChatColor.AQUA + "spawn");
      }, 1);
      return;
    }

    CommandExecutor executor = map.get(subCmd);
    if (executor != null) {
      executor.onCommand(event.getPlayer(), null, null, null);
      event.setCancelled(true);
    }
  }
}
