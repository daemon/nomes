package net.rocketeer.nomes.command;

import net.rocketeer.nomes.towny.TownyDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NationSetHomeCommand implements CommandExecutor {
  private final TownyDataManager mManager;

  public NationSetHomeCommand(TownyDataManager manager) {
    mManager = manager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (!(sender instanceof Player))
      return true;
    Player player = (Player) sender;
    if (!mManager.isNationLeader(player)) {
      player.sendMessage(ChatColor.RED + "You must be the leader of the nation to do this!");
      return true;
    }
    mManager.getNation(player).setSpawn(player.getLocation());
    player.sendMessage(ChatColor.GOLD + "Nation spawn set! You and your constituents may now use /nhtp");
    return true;
  }
}
