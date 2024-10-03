-- -------------------------------------------------------------------------
-- Fleet Status Database Initializer
-- ---------------------------------
-- Run this SQL script to "reset" the database.
-- -------------------------------------------------------------------------

-- -------------------------------------------------------------------------
-- Options
-- -------------------------------------------------------------------------
USE dhl_fleetstatus;	-- Specify our database name
SET NOCOUNT ON;			-- Report only errors

-- -------------------------------------------------------------------------
-- Drop Tables
-- -------------------------------------------------------------------------
IF OBJECT_ID('TAircraft')	IS NOT NULL DROP TABLE TAircraft;
IF OBJECT_ID('TCarriers')	IS NOT NULL DROP TABLE TCarriers;
IF OBJECT_ID('TUserRoles')	IS NOT NULL DROP TABLE TUserRoles;
IF OBJECT_ID('TUsers')		IS NOT NULL DROP TABLE TUsers;
IF OBJECT_ID('TRoles')		IS NOT NULL DROP TABLE TRoles;

-- -------------------------------------------------------------------------
-- Drop Views
-- -------------------------------------------------------------------------
IF OBJECT_ID('vAllAircraft')			IS NOT NULL DROP VIEW vAllAircraft;
IF OBJECT_ID('vOutOfServiceAircraft')	IS NOT NULL DROP VIEW vOutOfServiceAircraft;
IF OBJECT_ID('vInServiceAircraft')		IS NOT NULL DROP VIEW vInServiceAircraft;

-- -------------------------------------------------------------------------
-- Drop Stored Procedures
-- -------------------------------------------------------------------------
IF OBJECT_ID('uspShowCarrierAircraft')		IS NOT NULL DROP PROCEDURE uspShowCarrierAircraft;
IF OBJECT_ID('uspShowCarrierAircraftOOS')	IS NOT NULL DROP PROCEDURE uspShowCarrierAircraftOOS;
IF OBJECT_ID('uspShowCarrierAircraftIS')	IS NOT NULL DROP PROCEDURE uspShowCarrierAircraftIS;

-- -------------------------------------------------------------------------
-- Create Tables
-- -------------------------------------------------------------------------
CREATE TABLE TCarriers
(
	intCarrierId		INTEGER	IDENTITY	NOT NULL,
	strCarrier			NVARCHAR(250)		NOT NULL,
	CONSTRAINT TCarriers_PK PRIMARY KEY (intCarrierId)
);

CREATE TABLE TAircraft
(
	intAircraftId		INTEGER IDENTITY	NOT NULL,
	strStatus			NVARCHAR(250)		NOT NULL,
	strTailNumber		NVARCHAR(250)		NOT NULL,
	strReason			NVARCHAR(250)		NOT NULL,
	strNextUpdate		NVARCHAR(250)		NOT NULL,
	strRemark			NVARCHAR(250)		NOT NULL,
	blnBackInService	INTEGER				NOT NULL,
	intDownTime			INTEGER				,
	intCarrierId		INTEGER				NOT NULL,
	CONSTRAINT TAircraft_PK PRIMARY KEY (intAircraftId)
);

CREATE TABLE TUsers
(
	intUserId			INTEGER IDENTITY	NOT NULL,
	strUsername			NVARCHAR(250)		NOT NULL,
	strPassword			NVARCHAR(250)		NOT NULL,
	CONSTRAINT TUsers_PK PRIMARY KEY (intUserId)
);

CREATE TABLE TRoles
(
	intRoleId			INTEGER IDENTITY	NOT NULL,
	strRole				NVARCHAR(250)		NOT NULL,
	CONSTRAINT TRoles_PK PRIMARY KEY (intRoleId)
);

CREATE TABLE TUserRoles
(
	intUserRolesId		INTEGER IDENTITY	NOT NULL,
	intUserId			INTEGER				NOT NULL,
	intRoleId			INTEGER				NOT NULL,
	CONSTRAINT intUserRolesId PRIMARY KEY (intUserRolesId)
);

-- -------------------------------------------------------------------------
-- Define Relationships and Create Foreign Keys
-- -------------------------------------------------------------------------

-- # Child						# Parent					# Columns
-- -------						--------					---------
-- 1 TAircraft					TCarriers					intCarrierId
-- 2 TUserRoles					TUsers						intUserId
-- 3 TUserRoles					TRoles						intRoleId

-- 1
ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TCarriers_FK
FOREIGN KEY (intCarrierId) REFERENCES TCarriers (intCarrierId)

-- 2
ALTER TABLE TUserRoles ADD CONSTRAINT TUserRoles_TUsers_FK
FOREIGN KEY (intUserId) REFERENCES TUsers (intUserId)

--3 
ALTER TABLE TUserRoles ADD CONSTRAINT TUserRoles_TRoles_FK
FOREIGN KEY (intRoleId) REFERENCES TRoles (intRoleId)

-- -------------------------------------------------------------------------
-- Insert Data
-- -------------------------------------------------------------------------
INSERT INTO TCarriers	(strCarrier)
VALUES					('Cargojet'),
						('Mesa Airlines'),
						('Ameriflight'),
						('ABX Air'),
						('Atlas Air'),
						('Swift Air'),
						('Kalitta Air')

INSERT INTO TAircraft	(strStatus, strTailNumber, strReason, strRemark, strNextUpdate, blnBackInService, intCarrierId)
VALUES					('', 'N767AX', 'DAMAGED', 'Bird strike to the #1 engine', '13:21z', 0, 1),
						('', 'N650GT', 'MAINTENANCE', '#1 Generator inop', '15:00z', 1, 2),
						('', 'N762CK', 'AOG', 'Awaiting replacement FMC and required engineering order from Boeing', '21:00z', 0, 3)

INSERT INTO TRoles		(strRole)
VALUES					('Admin')

INSERT INTO TUsers		(strUsername, strPassword)
VALUES					('admin', 'password')

INSERT INTO TUserRoles	(intUserId, intRoleId)
VALUES					(1, 1)

-- -------------------------------------------------------------------------
-- Create Stored Procedures, Views, and Functions
-- -------------------------------------------------------------------------
-- View to show all aircraft
GO

CREATE VIEW vAllAircraft
AS
SELECT * FROM TAircraft

GO

-- View to show all out of service aircraft
GO

CREATE VIEW vOutOfServiceAircraft
AS
SELECT * FROM TAircraft WHERE TAircraft.blnBackInService = 0

GO

-- View to show all back in service aircraft
GO

CREATE VIEW vInServiceAircraft
AS
SELECT * FROM TAircraft WHERE TAircraft.blnBackInService = 1

GO

-- Stored procedure to show all aircraft from a specific carrier
GO

CREATE PROCEDURE uspShowCarrierAircraft
	@intCarrierId AS INTEGER
AS

BEGIN

SELECT * 
FROM TAircraft as TA JOIN TCarriers as TC
	ON TA.intCarrierId = TC.intCarrierId
WHERE
	TC.intCarrierId = @intCarrierId

END;

GO

-- Stored procedure to show all aircraft from a specific carrier that is out of service
GO

CREATE PROCEDURE uspShowCarrierAircraftOOS
	@intCarrierId AS INTEGER
AS

BEGIN

SELECT * 
FROM TAircraft as TA JOIN TCarriers as TC
	ON TA.intCarrierId = TC.intCarrierId
WHERE
	TC.intCarrierId = @intCarrierId
	AND TA.blnBackInService = 0

END;

GO

-- Stored procedure to show all aircraft from a specific carrier that is in service
GO

CREATE PROCEDURE uspShowCarrierAircraftIS
	@intCarrierId AS INTEGER
AS

BEGIN

SELECT * 
FROM TAircraft as TA JOIN TCarriers as TC
	ON TA.intCarrierId = TC.intCarrierId
WHERE
	TC.intCarrierId = @intCarrierId
	AND TA.blnBackInService = 1

END;

GO