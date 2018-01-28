package net.rocketeer.nomes.database.nation;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class NomesNationData {
  private final UUID mUuid;
  private final String mWorldUuid;
  private final double mX;
  private final double mY;
  private final double mZ;
  private final double mYaw;
  private final double mPitch;

  public NomesNationData(UUID id, String worldUuid, double x, double y, double z, double yaw, double pitch) {
    mUuid = id;
    mWorldUuid = worldUuid;
    mX = x;
    mY = y;
    mZ = z;
    mYaw = yaw;
    mPitch = pitch;
  }

  public UUID uuid() {
    return mUuid;
  }

  public String worldUuid() {
    return mWorldUuid;
  }

  public double x() {
    return mX;
  }

  public double y() {
    return mY;
  }

  public double z() {
    return mZ;
  }

  public double yaw() {
    return mYaw;
  }

  public double pitch() {
    return mPitch;
  }

  public Location location() {
    return new Location(Bukkit.getWorld(UUID.fromString(mWorldUuid)), mX, mY, mZ, (float) mYaw, (float) mPitch);
  }

  public static NomesNationData fromBukkit(UUID nationUuid, Location location) {
    return new NomesNationData(nationUuid, location.getWorld().getUID().toString(), location.getX(), location.getY(),
        location.getZ(), location.getYaw(), location.getPitch());
  }
}
