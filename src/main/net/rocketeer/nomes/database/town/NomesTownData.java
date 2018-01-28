package net.rocketeer.nomes.database.town;

import java.util.UUID;

public class NomesTownData {
  private final int mFlauntCount;
  private final UUID mUuid;
  private final String mName;

  public NomesTownData(UUID uuid, String name, int nFlaunts) {
    mFlauntCount = nFlaunts;
    mUuid = uuid;
    mName = name;
  }

  public UUID uuid() {
    return mUuid;
  }

  public int flauntCount() {
    return mFlauntCount;
  }

  public String name() {
    return mName;
  }
}
