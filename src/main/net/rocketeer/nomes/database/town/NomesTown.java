package net.rocketeer.nomes.database.town;

import com.palmergames.bukkit.towny.object.Town;
import net.rocketeer.nomes.database.NomesDatabase;

import java.util.UUID;
import java.util.function.Consumer;

public class NomesTown {
  private final NomesDatabase mDatabase;
  private final Town mTown;
  private NomesTownData mData;

  public NomesTown(Town town, NomesDatabase database) {
    mDatabase = database;
    mTown = town;
  }

  public NomesTownData cachedData() {
    return mData;
  }

  public UUID uuid() {
    return mTown.getUuid();
  }

  public void cachedData(NomesTownData data) {
    mData = data;
  }

  public String townName() {
    return mTown.getName();
  }

  private void syncTownData() {
    cachedData(mDatabase.fetchOrCreateNomesTown(mTown.getUuid(), mTown.getName()));
  }

  public void fetchFlauntCount(Consumer<Integer> callback) {
    mDatabase.scheduleAsyncTask(() -> {
      syncTownData();
      mDatabase.scheduleSyncTask(() -> callback.accept(mData.flauntCount()));
    });
  }

  public void incrementFlauntCount(int points) {
    mDatabase.scheduleAsyncTask(() -> {
      syncTownData();
      mDatabase.incrementFlauntCount(mTown.getUuid(), points);
    });
  }
}
