CREATE TABLE IF NOT EXISTS spacecrafts (
    id SERIAL PRIMARY KEY,    -- ID autoincremental para identificar cada nave
    name VARCHAR(100) NOT NULL,  -- Nombre de la nave
    type VARCHAR(50),           -- Tipo de nave (por ejemplo: Fighter, Freighter)
    series VARCHAR(50)          -- Serie de la nave (por ejemplo: Star Wars)
);
INSERT INTO spacecrafts (name, type, series) VALUES ('X-Wing', 'Fighter', 'Star Wars');
INSERT INTO spacecrafts (name, type, series) VALUES ('Millennium Falcon', 'Freighter', 'Star Wars');
