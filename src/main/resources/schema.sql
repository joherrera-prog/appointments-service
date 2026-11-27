-- Create the table if it does not already exist.
CREATE TABLE IF NOT EXISTS professionals (
                                             id TEXT NOT NULL PRIMARY KEY,
                                             name TEXT NOT NULL,
                                             time_zone TEXT NOT NULL
);