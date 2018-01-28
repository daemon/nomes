package net.rocketeer.nomes.towny;

import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.rocketeer.nomes.database.NomesDatabase;
import net.rocketeer.nomes.database.nation.NomesNation;
import net.rocketeer.nomes.database.town.NomesTown;
import net.rocketeer.nomes.database.town.NomesTownData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TownyDataManager {
  private final TownyDataSource mSource;
  private final NomesDatabase mDatabase;

  public TownyDataManager(NomesDatabase database) {
    mSource = TownyUniverse.getDataSource();
    mDatabase = database;
  }

  public NomesTown getTown(Player player) {
    try {
      Resident resident = mSource.getResident(player.getName());
      Town town = resident.getTown();
      return new NomesTown(town, mDatabase);
    } catch (NotRegisteredException e) {
      return null;
    }
  }

  public NomesNation getNation(Player player) {
    try {
      Resident resident = mSource.getResident(player.getName());
      Nation nation = resident.getTown().getNation();
      return new NomesNation(nation, mDatabase);
    } catch (NotRegisteredException e) {
      return null;
    }
  }

  public boolean isNationLeader(Player player) {
    try {
      Town town = mSource.getResident(player.getName()).getTown();
      return (town.getMayor().getName().equals(player.getName()) && town.isCapital());
    } catch (Exception e) {
      return false;
    }
  }

  public void fetchTopFlauntTowns(Consumer<List<NomesTown>> callback) {
    mDatabase.scheduleAsyncTask(() -> {
      List<NomesTownData> townDataList = mDatabase.fetchTopFlauntTowns(15);
      List<NomesTown> towns = new ArrayList<>(16);
      townDataList.forEach(data -> {
        try {
          NomesTown town = new NomesTown(mSource.getTown(data.uuid()), mDatabase);
          town.cachedData(data);
          towns.add(town);
        } catch (NotRegisteredException ignored) {}
      });
      mDatabase.scheduleSyncTask(() -> callback.accept(towns));
    });
  }
}
