package net.rocketeer.nomes.command;

import net.rocketeer.nomes.database.town.NomesTown;
import net.rocketeer.nomes.towny.TownyDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RespectTopCommand implements CommandExecutor {
  private final TownyDataManager mManager;

  public RespectTopCommand(TownyDataManager manager) {
    mManager = manager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    mManager.fetchTopFlauntTowns(towns -> {
      if (towns.size() == 0) {
        sender.sendMessage(ChatColor.YELLOW + "No towns");
        return;
      }
      int i = 1;
      for (NomesTown town : towns) {
        sender.sendMessage(makeMessage(i, town.townName(), town.cachedData().flauntCount()));
        ++i;
      }
    });
    return true;
  }

  private String makeMessage(int i, String townName, int respect) {
    return String.valueOf(i) + ". " + ChatColor.AQUA + townName + " " + ChatColor.GOLD + String.valueOf(respect);
  }
}
