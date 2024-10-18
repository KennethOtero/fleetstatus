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
IF OBJECT_ID('TAircraft_Reason')		IS NOT NULL DROP TABLE TAircraft_Reason;
IF OBJECT_ID('TAircraft')	            IS NOT NULL DROP TABLE TAircraft;
IF OBJECT_ID('TCarriers')	            IS NOT NULL DROP TABLE TCarriers;
IF OBJECT_ID('TUserRoles')	            IS NOT NULL DROP TABLE TUserRoles;
IF OBJECT_ID('TUsers')		            IS NOT NULL DROP TABLE TUsers;
IF OBJECT_ID('TRoles')	            	IS NOT NULL DROP TABLE TRoles;
IF OBJECT_ID('TReason')		            IS NOT NULL DROP TABLE TReason;

-- -------------------------------------------------------------------------
-- Drop Views
-- -------------------------------------------------------------------------
IF OBJECT_ID('vAllAircraft')			IS NOT NULL DROP VIEW vAllAircraft;
IF OBJECT_ID('vOutOfServiceAircraft')	IS NOT NULL DROP VIEW vOutOfServiceAircraft;
IF OBJECT_ID('vInServiceAircraft')		IS NOT NULL DROP VIEW vInServiceAircraft;
IF OBJECT_ID('vAllReason')		    	IS NOT NULL DROP VIEW vAllReason;

-- -------------------------------------------------------------------------
-- Drop Stored Procedures
-- -------------------------------------------------------------------------
IF OBJECT_ID('uspShowCarrierAircraft')			        IS NOT NULL DROP PROCEDURE uspShowCarrierAircraft;
IF OBJECT_ID('uspShowCarrierAircraftOOS')		        IS NOT NULL DROP PROCEDURE uspShowCarrierAircraftOOS;
IF OBJECT_ID('uspShowCarrierAircraftIS')		        IS NOT NULL DROP PROCEDURE uspShowCarrierAircraftIS;
IF OBJECT_ID('uspUpdateAircraftServiceStatus')	        IS NOT NULL DROP PROCEDURE uspUpdateAircraftServiceStatus;
IF OBJECT_ID('uspDeleteAircraft')				        IS NOT NULL DROP PROCEDURE uspDeleteAircraft;
IF OBJECT_ID('uspGetReasonsForAircraft')				IS NOT NULL DROP PROCEDURE uspGetReasonsForAircraft;

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
	strTailNumber		NVARCHAR(250)		NOT NULL,
	strNextUpdate		DATETIME			NOT NULL,
	strRemark			NVARCHAR(250)		NOT NULL,
	blnBackInService	INTEGER				NOT NULL,
	dtmStartTime		DATETIME			,
	dtmEndTime			DATETIME			,
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

CREATE TABLE TReason
(
    intReasonId        INTEGER IDENTITY    NOT NULL,
    strReason          NVARCHAR(250)       NOT NULL,
    CONSTRAINT TReason_PK PRIMARY KEY (intReasonId)
);

CREATE TABLE TAircraft_Reason
(
    intAircraftId      INTEGER             NOT NULL,
    intReasonId        INTEGER             NOT NULL,
    CONSTRAINT TAircraft_Reason_PK PRIMARY KEY (intAircraftId, intReasonId),
);

-- -------------------------------------------------------------------------
-- Define Relationships and Create Foreign Keys
-- -------------------------------------------------------------------------

-- # Child						# Parent					# Columns
-- -------						--------					---------
-- 1 TAircraft					TCarriers					intCarrierId
-- 2 TUserRoles					TUsers						intUserId
-- 3 TUserRoles					TRoles						intRoleId
-- 4 TAircraft_Reason           TAircraft                   intAircraftId
-- 5 TAircraft_Reason           TReason                     intReasonId

-- 1
ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TCarriers_FK
FOREIGN KEY (intCarrierId) REFERENCES TCarriers (intCarrierId)

-- 2
ALTER TABLE TUserRoles ADD CONSTRAINT TUserRoles_TUsers_FK
FOREIGN KEY (intUserId) REFERENCES TUsers (intUserId)

--3 
ALTER TABLE TUserRoles ADD CONSTRAINT TUserRoles_TRoles_FK
FOREIGN KEY (intRoleId) REFERENCES TRoles (intRoleId)

--4
ALTER TABLE TAircraft_Reason ADD CONSTRAINT TAircraft_Reason_TAircraft_FK
FOREIGN KEY (intAircraftId) REFERENCES TAircraft (intAircraftId)

--5
ALTER TABLE TAircraft_Reason ADD CONSTRAINT TAircraft_Reason_TReason_FK
FOREIGN KEY (intReasonId) REFERENCES TReason (intReasonId);

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

INSERT INTO TAircraft	(strTailNumber, strRemark, strNextUpdate, blnBackInService, intCarrierId, dtmStartTime, dtmEndTime)
VALUES					('N767AX', 'Bird strike to the #1 engine', '2024-10-15 21:00:00', 0, 1, GETUTCDATE(), DATEADD(hour, 1, GETUTCDATE())),
						('N650GT', '#1 Generator inop', '2024-10-15 21:00:0', 1, 2, GETUTCDATE(), DATEADD(hour, 2, GETUTCDATE())),
						('N762CK', 'Awaiting replacement FMC and required engineering order from Boeing', '2024-10-15 21:00:0', 0, 3, GETUTCDATE(), DATEADD(hour, 3, GETUTCDATE()))

INSERT INTO TReason     (strReason)
VALUES                  ('Maintenance'),
                        ('AOG'),
                        ('Damage')

INSERT INTO TAircraft_Reason    (intAircraftId, intReasonId)
VALUES                          (1, 1),
                                (1, 2),
                                (2, 2),
                                (3, 3)

INSERT INTO TRoles		(strRole)
VALUES					('Admin')

INSERT INTO TUsers		(strUsername, strPassword)
VALUES					('admin', 'password')

INSERT INTO TUserRoles	(intUserId, intRoleId)
VALUES					(1, 1)

-- -------------------------------------------------------------------------
-- Create Stored Procedures, Views, and Functions
-- -------------------------------------------------------------------------

-- -------------------------------------------------------------------------
-- View to show all aircraft
-- -------------------------------------------------------------------------
GO

CREATE VIEW vAllAircraft
AS
SELECT * FROM TAircraft

GO

-- -------------------------------------------------------------------------
-- View to show all out of service aircraft
-- -------------------------------------------------------------------------
GO

CREATE VIEW vOutOfServiceAircraft
AS
SELECT * FROM TAircraft WHERE TAircraft.blnBackInService = 0

GO

-- -------------------------------------------------------------------------
-- View to show all back in service aircraft
-- -------------------------------------------------------------------------
GO

CREATE VIEW vInServiceAircraft
AS
SELECT * FROM TAircraft WHERE TAircraft.blnBackInService = 1

GO

-- -------------------------------------------------------------------------
-- View to show all Reason
-- -------------------------------------------------------------------------
GO

CREATE VIEW vAllReason
AS
SELECT * FROM TReason

GO

-- -------------------------------------------------------------------------
-- Stored procedure to show all aircraft from a specific carrier
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspShowCarrierAircraft
	@intCarrierId AS INTEGER
AS

BEGIN

SELECT 
	TA.*
FROM TAircraft as TA JOIN TCarriers as TC
	ON TA.intCarrierId = TC.intCarrierId
WHERE
	TC.intCarrierId = @intCarrierId

END;

GO

-- -------------------------------------------------------------------------
-- Stored procedure to show all aircraft from a specific carrier that is out of service
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspShowCarrierAircraftOOS
	@intCarrierId AS INTEGER
AS

BEGIN

SELECT 
	TA.* 
FROM TAircraft as TA JOIN TCarriers as TC
	ON TA.intCarrierId = TC.intCarrierId
WHERE
	TC.intCarrierId = @intCarrierId
	AND TA.blnBackInService = 0

END;

GO

-- -------------------------------------------------------------------------
-- Stored procedure to show all aircraft from a specific carrier that is in service
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspShowCarrierAircraftIS
	@intCarrierId AS INTEGER
AS

BEGIN

SELECT 
	TA.* 
FROM TAircraft as TA JOIN TCarriers as TC
	ON TA.intCarrierId = TC.intCarrierId
WHERE
	TC.intCarrierId = @intCarrierId
	AND TA.blnBackInService = 1

END;

GO

-- -------------------------------------------------------------------------
-- Stored procedure to update an aircraft
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspUpdateAircraftServiceStatus
	@intAircraftId		AS INTEGER,
	@blnBackInService	AS INTEGER
AS

BEGIN

-- Update the service status and end time
UPDATE 
	TAircraft
SET 
	blnBackInService = @blnBackInService,
	-- Set the current UTC time as the end time
	dtmEndTime = GETUTCDATE()
WHERE 
	intAircraftId = @intAircraftId

-- Return the aircraft
SELECT * FROM TAircraft WHERE intAircraftId = @intAircraftId;

END;

GO

-- -------------------------------------------------------------------------
-- Stored procedure to remove an aircraft
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspDeleteAircraft
	@intAircraftId		AS INTEGER
AS

BEGIN

DELETE FROM TAircraft WHERE intAircraftId = @intAircraftId

END;

GO

-- -------------------------------------------------------------------------
-- Search reason for a aircraft
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspGetReasonsForAircraft
    @intAircraftId		AS INTEGER
AS

BEGIN

SELECT TReason.intReasonId, TReason.strReason
FROM TReason TReason
JOIN TAircraft_Reason TAircraft_Reason ON TReason.intReasonId = TAircraft_Reason.intReasonId
WHERE TAircraft_Reason.intAircraftId = @intAircraftId;

END;

GO