CREATE TABLE IF NOT EXISTS nomes_towns(uuid VARCHAR(36) NOT NULL UNIQUE, n_flaunts INT NOT NULL DEFAULT 0,
 INDEX n_flaunt_i(n_flaunts), INDEX t_uuid_i(uuid)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS nomes_nations(uuid VARCHAR(36) NOT NULL UNIQUE, world_uuid VARCHAR(36) NOT NULL, x REAL NOT NULL,
    y REAL NOT NULL, z REAL NOT NULL, yaw REAL NOT NULL, pitch REAL NOT NULL, INDEX n_uuid_i(uuid)) ENGINE=InnoDB;
