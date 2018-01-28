package net.rocketeer.nomes.database.town;

import java.util.UUID;

public class NomesTownData {
  private final int mFlauntCount;
  private final UUID mUuid;

  public NomesTownData(UUID uuid, int nFlaunts) {
    mFlauntCount = nFlaunts;
    mUuid = uuid;
  }

  public UUID uuid() {
    return mUuid;
  }

  public int flauntCount() {
    return mFlauntCount;
  }
}
