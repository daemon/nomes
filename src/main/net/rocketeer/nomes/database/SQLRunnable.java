package net.rocketeer.nomes.database;

@FunctionalInterface
public interface SQLRunnable<T> {
  T run() throws Exception;
}
