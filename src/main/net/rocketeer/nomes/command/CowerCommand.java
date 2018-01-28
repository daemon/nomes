package net.rocketeer.nomes.command;

import net.rocketeer.nomes.database.town.NomesTown;
import net.rocketeer.nomes.towny.TownyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CowerCommand implements CommandExecutor {
  private final TownyDataManager mManager;
  private final Map<UUID, Long> mLastUsed;
  private final static long DELAY_MS = 10 * 60000;

  public CowerCommand(TownyDataManager manager) {
    mManager = manager;
    mLastUsed = new HashMap<>();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (!(sender instanceof Player))
      return true;
    Player player = (Player) sender;
    NomesTown town = mManager.getTown(player);
    if (town == null) {
      player.sendMessage(ChatColor.RED + "You're not part of a town!");
      return true;
    }

    if (System.currentTimeMillis() - mLastUsed.getOrDefault(town.uuid(), 0L) < DELAY_MS) {
      long waitSecs = (DELAY_MS - (System.currentTimeMillis() - mLastUsed.getOrDefault(town.uuid(), 0L))) / 1000;
      long seconds = waitSecs % 60;
      long minutes = waitSecs / 60;
      StringBuilder builder = new StringBuilder();
      if (minutes > 0) builder.append(minutes).append(" minutes, ");
      builder.append(seconds).append(" seconds.");
      player.sendMessage(ChatColor.RED + "Someone in your town has cowered too recently! Please wait " +
          builder.toString());
      return true;
    }
    int points = (int) (Math.random() * 10 + 1);
    Bukkit.getServer().broadcastMessage(makeMessage(player, points));
    town.incrementFlauntCount(-points);
    mLastUsed.put(town.uuid(), System.currentTimeMillis());
    return true;
  }

  private String makeMessage(Player player, int points) {
    StringBuilder builder = new StringBuilder();
    builder.append(ChatColor.AQUA).append(player.getName()).append(ChatColor.YELLOW);
    builder.append(" fearfully cowers like a chicken and loses their town ").append(ChatColor.GOLD);
    builder.append(points).append(ChatColor.YELLOW).append(" respect points!");
    return builder.toString();
  }
}
