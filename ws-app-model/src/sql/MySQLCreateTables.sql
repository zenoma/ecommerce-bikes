-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE Rent;
DROP TABLE Bike;


------------------------------------ Bike ------------------------------------

