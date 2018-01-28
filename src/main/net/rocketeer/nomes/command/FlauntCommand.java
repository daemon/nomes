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

public class FlauntCommand implements CommandExecutor {
  private final TownyDataManager mManager;
  private final Map<UUID, Long> mLastUsed;

  public FlauntCommand(TownyDataManager manager) {
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

    if (System.currentTimeMillis() - mLastUsed.getOrDefault(town.uuid(), 0L) < 60000) {
      long waitMs = System.currentTimeMillis() - mLastUsed.getOrDefault(town.uuid(), 0L);
      String lengthStr = String.valueOf(60 - (int) Math.ceil(waitMs / 1000.0)) + " seconds.";
      player.sendMessage(ChatColor.RED + "Someone in your town has flaunted too recently! Please wait " + lengthStr);
      return true;
    }
    int points = (int) (Math.random() * 10 + 1);
    Bukkit.getServer().broadcastMessage(makeMessage(player, town, points));
    town.incrementFlauntCount(points);
    mLastUsed.put(town.uuid(), System.currentTimeMillis());
    return true;
  }

  private String makeMessage(Player player, NomesTown town, int points) {
    StringBuilder builder = new StringBuilder();
    builder.append(ChatColor.AQUA).append(player.getName()).append(ChatColor.YELLOW);
    builder.append(" openly flaunts their town, ").append(ChatColor.AQUA).append(town.townName());
    builder.append(ChatColor.YELLOW).append(", and earns the town ").append(ChatColor.GOLD);
    builder.append(points).append(ChatColor.YELLOW).append(" respect points!");
    return builder.toString();
  }
}
