package net.rocketeer.nomes.database.nation;

import com.palmergames.bukkit.towny.object.Nation;
import net.rocketeer.nomes.database.NomesDatabase;
import org.bukkit.Location;

import java.util.function.Consumer;

public class NomesNation {
  private final NomesDatabase mDatabase;
  private final Nation mNation;
  private NomesNationData mData;

  public NomesNation(Nation nation, NomesDatabase database) {
    mDatabase = database;
    mNation = nation;
  }

  public NomesNationData cachedData() {
    return mData;
  }

  public void cachedData(NomesNationData data) {
    mData = data;
  }

  public void setSpawn(Location location) {
    mDatabase.scheduleAsyncTask(() -> {
      mDatabase.updateOrCreateNomesNation(NomesNationData.fromBukkit(mNation.getUuid(), location));
    });
  }

  public void fetchSpawn(Consumer<Location> callback) {
    mDatabase.scheduleAsyncTask(() -> {
      NomesNationData data = mDatabase.fetchNomesNation(mNation.getUuid());
      mDatabase.scheduleSyncTask(() -> callback.accept(data.location()));
    });
  }
}
