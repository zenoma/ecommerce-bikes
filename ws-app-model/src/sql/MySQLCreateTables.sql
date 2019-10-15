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
	modelName BIGINT NOT NULL,
	description VARCHAR(255) COLLATE latin1_bin NOT NULL,
	startDate DATETIME NOT NULL,
	price FLOAT NOT NULL,
	availableNumber INT NOT NULL,
	adquisitionDate DATETIME NOT NULL,
	numberOfRents INT NOT NULL,
	scoreAverage FLOAT NOT NULL,
	CONSTRAINT BikePK PRIMARY KEY(modelName),
	CONSTRAINT validPrice CHECK (price >= 0),
	CONSTRAINT validAvailableNumber CHECK (availableNumber >= 0),
	CONSTRAINT validNumberOfRents CHECK (numberOfRents >= 0),
	CONSTRAINT validScoreAverage CHECK (scoreAverage >= 0) ) ENGINE = InnoDB;


------------------------------------ Sale ------------------------------------

CREATE TABLE Rent (
	rentId BIGINT NOT NULL,
	userEmail VARCHAR(255) COLLATE latin1_bin NOT NULL,
	modelName VARCHAR(255) COLLATE latin1_bin NOT NULL,
	creditCard INT NOT NULL,
	startRentDate DATETIME NOT NULL,
	finishRentDate DATETIME NOT NULL,
	numberOfBikes INT NOT NULL,
	rentDate DATETIME NOT NULL,
	CONSTRAINT RentPK PRIMARY KEY(rendId),
	CONSTRAINT RentModelName FOREIGN KEY(modelName)
		REFERENCES Bike(modelName) ON DELETE CASCADE),
	CONSTRAINT validNumberOfBikes CHECK (numberOfBikes >= 0) ) ENGINE = InnoDB;