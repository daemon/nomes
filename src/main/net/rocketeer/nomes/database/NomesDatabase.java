package net.rocketeer.nomes.database;

import net.rocketeer.nomes.database.nation.NomesNationData;
import net.rocketeer.nomes.database.town.NomesTownData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NomesDatabase {
  private final DatabaseManager mManager;
  private final JavaPlugin mPlugin;

  public NomesDatabase(JavaPlugin plugin, DatabaseManager manager) {
    mManager = manager;
    mPlugin = plugin;
  }

  public BukkitTask scheduleAsyncTask(Runnable task) {
    return Bukkit.getScheduler().runTaskAsynchronously(mPlugin, task);
  }

  public BukkitTask scheduleSyncTask(Runnable task) {
    return Bukkit.getScheduler().runTask(mPlugin, task);
  }

  public NomesNationData fetchNomesNation(UUID nationUuid) {
    try (Connection connection = mManager.getConnection()) {
      return fetchNomesNationWithConnection(nationUuid, connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private NomesNationData fetchNomesNationWithConnection(UUID nationUuid, Connection connection) {
    try (PreparedStatement selStmt = connection.prepareStatement("SELECT * FROM nomes_nations WHERE uuid=?")) {
      selStmt.setString(1, nationUuid.toString());
      ResultSet rs = selStmt.executeQuery();
      if (rs.next())
        return new NomesNationData(nationUuid, rs.getString("world_uuid"), rs.getDouble("x"), rs.getDouble("y"),
            rs.getDouble("z"), rs.getDouble("yaw"), rs.getDouble("pitch"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void checkRows(int rows, String message) throws SQLException {
    if (rows == 0) throw new SQLException(message);
  }

  public void updateOrCreateNomesNation(NomesNationData data) {
    try (Connection connection = mManager.getConnection();
         TransactionGuard<Boolean> guard = new TransactionGuard<>(connection, () -> {
           try (PreparedStatement updateStmt = connection.prepareStatement("UPDATE nomes_nations SET x=?, y=?, z=?, " +
               "world_uuid=?, yaw=?, pitch=? WHERE uuid=?");
                PreparedStatement insStmt = connection.prepareStatement("INSERT INTO nomes_nations (uuid, x, y, z, world_uuid," +
                    "yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
             NomesNationData nData = fetchNomesNationWithConnection(data.uuid(), connection);
             if (nData == null) {
               insStmt.setString(1, data.uuid().toString());
               insStmt.setDouble(2, data.x());
               insStmt.setDouble(3, data.y());
               insStmt.setDouble(4, data.z());
               insStmt.setString(5, data.worldUuid());
               insStmt.setDouble(6, data.yaw());
               insStmt.setDouble(7, data.pitch());
               checkRows(insStmt.executeUpdate(), "Failed to insert nation!");
             } else {
               updateStmt.setDouble(1, data.x());
               updateStmt.setDouble(2, data.y());
               updateStmt.setDouble(3, data.z());
               updateStmt.setString(4, data.worldUuid());
               updateStmt.setDouble(5, data.yaw());
               updateStmt.setDouble(6, data.pitch());
               updateStmt.setString(7, data.uuid().toString());
               checkRows(updateStmt.executeUpdate(), "Failed to update nation!");
             }
           }
           return true;
         })) {
      guard.run();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception ignored) {}
  }

  public NomesTownData fetchOrCreateNomesTown(UUID townUuid, String name) {
    try (Connection connection = mManager.getConnection();
         PreparedStatement selStmt = connection.prepareStatement("SELECT * FROM nomes_towns WHERE uuid=?");
         PreparedStatement insStmt = connection.prepareStatement("INSERT INTO nomes_towns (uuid, name) VALUES (?, ?)")) {
      selStmt.setString(1, townUuid.toString());
      ResultSet rs = selStmt.executeQuery();
      if (rs.next())
        return new NomesTownData(townUuid, name, rs.getInt("n_flaunts"));
      insStmt.setString(1, townUuid.toString());
      insStmt.setString(2, name);
      checkRows(insStmt.executeUpdate(), "Cannot create Nomes town!");
      return new NomesTownData(townUuid, name, 0);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void incrementFlauntCount(UUID townUuid, int points) {
    try (Connection connection = mManager.getConnection();
         PreparedStatement stmt = connection.prepareStatement("UPDATE nomes_towns SET n_flaunts=n_flaunts+? WHERE uuid=?")) {
      stmt.setInt(1, points);
      stmt.setString(2, townUuid.toString());
      checkRows(stmt.executeUpdate(), "Cannot increment flaunt count!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<NomesTownData> fetchTopFlauntTowns(int limit) {
    List<NomesTownData> townDataList = new ArrayList<>(limit + 1);
    try (Connection connection = mManager.getConnection();
         PreparedStatement stmt = connection.prepareStatement("SELECT * FROM nomes_towns ORDER BY n_flaunts DESC LIMIT ?")) {
      stmt.setInt(1, limit);
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
        townDataList.add(new NomesTownData(UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getInt("n_flaunts")));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return townDataList;
  }
}
