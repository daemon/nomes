package net.rocketeer.nomes;

import net.rocketeer.nomes.command.*;
import net.rocketeer.nomes.database.DatabaseManager;
import net.rocketeer.nomes.database.NomesDatabase;
import net.rocketeer.nomes.towny.TownyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.beans.PropertyVetoException;

public class NomesPlugin extends JavaPlugin {
  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    FileConfiguration config = this.getConfig();
    ConfigurationSection mysqlCfg = config.getConfigurationSection("mysql");
    String username = mysqlCfg.getString("username");
    String password = mysqlCfg.getString("password");
    String hostname = mysqlCfg.getString("hostname");
    String database = mysqlCfg.getString("database");
    int port = mysqlCfg.getInt("port");
    String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true";

    DatabaseManager manager = null;
    try {
      manager = new DatabaseManager(url, username, password);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
      return;
    }

    manager.initDatabase();
    NomesDatabase nomesDb = new NomesDatabase(this, manager);
    TownyDataManager townMan = new TownyDataManager(nomesDb);
    Bukkit.getPluginManager().registerEvents(new TownyPreprocessHook(this, townMan), this);
  }
}
