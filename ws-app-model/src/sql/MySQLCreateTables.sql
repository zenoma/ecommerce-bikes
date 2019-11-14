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
CREATE TABLE Bike (
	bikeId BIGINT NOT NULL AUTO_INCREMENT,
	modelName VARCHAR(255) COLLATE latin1_bin NOT NULL,
	description VARCHAR(255) COLLATE latin1_bin NOT NULL,
	startDate DATETIME NOT NULL,
	price FLOAT NOT NULL,
	availableNumber INT NOT NULL,
	adquisitionDate DATETIME NOT NULL,
	numberOfRents INT NOT NULL,
	averageScore FLOAT NOT NULL,
	CONSTRAINT BikePK PRIMARY KEY(bikeId),
	CONSTRAINT validPrice CHECK (price >= 0),
	CONSTRAINT validAvailableNumber CHECK (availableNumber >= 0),
	CONSTRAINT validNumberOfRents CHECK (numberOfRents >= 0),
	CONSTRAINT validAverageScore CHECK (averageScore >= 0) ) ENGINE = InnoDB;


------------------------------------ Sale ------------------------------------

CREATE TABLE Rent (
	rentId BIGINT NOT NULL AUTO_INCREMENT,
	userEmail VARCHAR(255) COLLATE latin1_bin NOT NULL,
	bikeId BIGINT NOT NULL,
	creditCard BIGINT NOT NULL,
	startRentDate DATETIME NOT NULL,
	finishRentDate DATETIME NOT NULL,
	numberOfBikes INT NOT NULL,
	rentDate DATETIME NOT NULL,
	price FLOAT NOT NULL,
	CONSTRAINT RentPK PRIMARY KEY(rentId),
	CONSTRAINT validNumberOfBikes CHECK (numberOfBikes >= 0),
	CONSTRAINT RentBikeId FOREIGN KEY(bikeId)
		REFERENCES Bike(bikeId) ON DELETE CASCADE ) ENGINE = InnoDB;