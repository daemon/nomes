package net.rocketeer.nomes.command;

import net.rocketeer.nomes.database.nation.NomesNation;
import net.rocketeer.nomes.towny.TownyDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NationTeleportCommand implements CommandExecutor {
  private final TownyDataManager mManager;

  public NationTeleportCommand(TownyDataManager manager) {
    mManager = manager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (!(sender instanceof Player))
      return true;
    Player player = (Player) sender;
    NomesNation nation = mManager.getNation(player);
    if (nation == null) {
      player.sendMessage(ChatColor.RED + "You're not part of a nation!");
      return true;
    }
    nation.fetchSpawn(player::teleport);
    return true;
  }
}
