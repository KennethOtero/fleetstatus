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
IF OBJECT_ID('TEventReasons')			IS NOT NULL DROP TABLE TEventReasons
IF OBJECT_ID('TEvents')					IS NOT NULL DROP TABLE TEvents
IF OBJECT_ID('TAircraft')	            IS NOT NULL DROP TABLE TAircraft;
IF OBJECT_ID('TCarriers')	            IS NOT NULL DROP TABLE TCarriers;
IF OBJECT_ID('TTypes')					IS NOT NULL DROP TABLE TTypes;
IF OBJECT_ID('TReasons')		        IS NOT NULL DROP TABLE TReasons;
IF OBJECT_ID('TUsers')		            IS NOT NULL DROP TABLE TUsers;
IF OBJECT_ID('TRoles')	            	IS NOT NULL DROP TABLE TRoles;

-- -------------------------------------------------------------------------
-- Drop Views
-- -------------------------------------------------------------------------
IF OBJECT_ID('vAllAircraft')			IS NOT NULL DROP VIEW vAllAircraft;
IF OBJECT_ID('vOutOfServiceAircraft')	IS NOT NULL DROP VIEW vOutOfServiceAircraft;
IF OBJECT_ID('vInServiceAircraft')		IS NOT NULL DROP VIEW vInServiceAircraft;
IF OBJECT_ID('vAllReason')		    	IS NOT NULL DROP VIEW vAllReason;
IF OBJECT_ID('vAllCarrier')		    	IS NOT NULL DROP VIEW vAllCarrier;

-- -------------------------------------------------------------------------
-- Drop Stored Procedures
-- -------------------------------------------------------------------------
IF OBJECT_ID('uspShowCarrierAircraft')			        IS NOT NULL DROP PROCEDURE uspShowCarrierAircraft;
IF OBJECT_ID('uspShowCarrierAircraftOOS')		        IS NOT NULL DROP PROCEDURE uspShowCarrierAircraftOOS;
IF OBJECT_ID('uspShowCarrierAircraftIS')		        IS NOT NULL DROP PROCEDURE uspShowCarrierAircraftIS;
IF OBJECT_ID('uspUpdateAircraftServiceStatus')	        IS NOT NULL DROP PROCEDURE uspUpdateAircraftServiceStatus;
IF OBJECT_ID('uspDeleteAircraft')				        IS NOT NULL DROP PROCEDURE uspDeleteAircraft;
IF OBJECT_ID('uspGetReasonsForEvent')					IS NOT NULL DROP PROCEDURE uspGetReasonsForEvent;

-- -------------------------------------------------------------------------
-- Create Tables
-- -------------------------------------------------------------------------
CREATE TABLE TCarriers
(
	intCarrierId		INTEGER	IDENTITY	NOT NULL,
	strCarrier			NVARCHAR(250)		NOT NULL,
	CONSTRAINT TCarriers_PK PRIMARY KEY (intCarrierId)
);

CREATE TABLE TTypes
(
	intTypeId			INTEGER IDENTITY	NOT NULL,
	strType				NVARCHAR(250)		NOT NULL,
	CONSTRAINT TTypes_PK PRIMARY KEY (intTypeId)
);

CREATE TABLE TAircraft
(
	intAircraftId		INTEGER IDENTITY	NOT NULL,
	strTailNumber		NVARCHAR(250)		NOT NULL,
	intTypeId			INTEGER				NOT NULL,
	intCarrierId		INTEGER				NOT NULL,
	CONSTRAINT TAircraft_UQ UNIQUE (strTailNumber),
	CONSTRAINT TAircraft_PK PRIMARY KEY (intAircraftId)
);

CREATE TABLE TEvents
(
	intEventId			INTEGER IDENTITY	NOT NULL,
	intAircraftId		INTEGER				NOT NULL,
	strRemark			NVARCHAR(250)		NOT NULL,
	dtmNextUpdate		DATETIME			,
	blnBackInService	INTEGER				NOT NULL,
	dtmStartTime		DATETIME			,
	dtmEndTime			DATETIME			,
	strDownTime			NVARCHAR(250)		,
	CONSTRAINT TEvents_PK PRIMARY KEY (intEventId)
);

CREATE TABLE TUsers
(
	intUserId			INTEGER IDENTITY	NOT NULL,
	strUsername			NVARCHAR(250)		NOT NULL,
	strPassword			NVARCHAR(250)		NOT NULL,
	strRoles			NVARCHAR(250)		NOT NULL,
	CONSTRAINT TUsers_PK PRIMARY KEY (intUserId)
);

CREATE TABLE TRoles
(
	intRoleId			INTEGER IDENTITY	NOT NULL,
	strRole				NVARCHAR(250)		NOT NULL,
	CONSTRAINT TRoles_PK PRIMARY KEY (intRoleId)
);

CREATE TABLE TReasons
(
    intReasonId        INTEGER IDENTITY    NOT NULL,
    strReason          NVARCHAR(250)       NOT NULL,
    CONSTRAINT TReasons_PK PRIMARY KEY (intReasonId)
);

CREATE TABLE TEventReasons
(
	intEventReasonId	INTEGER IDENTITY	NOT NULL,
	intEventId			INTEGER				NOT NULL,
	intReasonId			INTEGER				NOT NULL,
	CONSTRAINT TEventReasons_PK PRIMARY KEY (intEventReasonId)
);

-- -------------------------------------------------------------------------
-- Define Relationships and Create Foreign Keys
-- -------------------------------------------------------------------------

-- # Child						# Parent					# Columns
-- -------						--------					---------
-- 1 TAircraft					TCarriers					intCarrierId
-- 2 TAircraft					TTypes						intTypeId
-- 3 TEvents					TAircraft					intAircraftId
-- 4 TEventReasons				TEvents						intEventId
-- 5 TEventReasons				TReasons					intReasonId

-- 1
ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TCarriers_FK
FOREIGN KEY (intCarrierId) REFERENCES TCarriers (intCarrierId)

-- 2
ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TTypes_FK
FOREIGN KEY (intTypeId) REFERENCES TTypes (intTypeId)

-- 3
ALTER TABLE TEvents ADD CONSTRAINT TEvents_TAircraft_FK
FOREIGN KEY (intAircraftId) REFERENCES TAircraft (intAircraftId)

-- 4
ALTER TABLE TEventReasons ADD CONSTRAINT TEventReasons_TEvents_FK
FOREIGN KEY (intEventId) REFERENCES TEvents (intEventId)

-- 5
ALTER TABLE TEventReasons ADD CONSTRAINT TEventReasons_TReasons_FK
FOREIGN KEY (intReasonId) REFERENCES TReasons (intReasonId)

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

INSERT INTO TTypes		(strType)
VALUES					('767'),
						('650'),
						('762')

INSERT INTO TAircraft	(strTailNumber, intTypeId, intCarrierId)
VALUES					('N767AX', 1, 1),
						('N650GT', 2, 2),
						('N762CK', 3, 3)

INSERT INTO TEvents		(intAircraftId, strRemark, dtmNextUpdate, blnBackInService, dtmStartTime, dtmEndTime, strDownTime)
VALUES					(1, 'Bird strike to the #1 engine', '2024-10-15 21:00:00', 0, DATEADD(hour, -1, GETUTCDATE()), GETUTCDATE(), '0d 1h 0m'),
						(2, '#1 Generator inop', '2024-10-15 21:00:0', 1, DATEADD(hour, -2, GETUTCDATE()), GETUTCDATE(), '0d 2h 0m'),
						(3, 'Awaiting replacement FMC and required engineering order from Boeing', '2024-10-15 21:00:0', 0, DATEADD(hour, -3, GETUTCDATE()), GETUTCDATE(), '0d 3h 0m')

INSERT INTO TReasons    (strReason)
VALUES                  ('Maintenance'),
                        ('AOG'),
                        ('Damage')

INSERT INTO TEventReasons	(intEventId, intReasonId)
VALUES						(1, 1),
							(1, 2),
							(2, 2),
							(3, 3)

INSERT INTO TRoles		(strRole)
VALUES					('Admin')

INSERT INTO TUsers		(strUsername, strPassword, strRoles)
VALUES					('admin', 'password', 'admin,user')

-- -------------------------------------------------------------------------
-- Create Stored Procedures, Views, and Functions
-- -------------------------------------------------------------------------

-- -------------------------------------------------------------------------
-- View to show all aircraft events
-- -------------------------------------------------------------------------
GO

CREATE VIEW vAllAircraft
AS
SELECT 
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType,
	TA.intAircraftId,
	TA.strTailNumber,
	TE.intEventid,
	TE.strRemark,
	TE.dtmNextUpdate,
	TE.blnBackInService,
	TE.dtmStartTime,
	TE.dtmEndTime,
	TE.strDownTime
FROM 
	TEvents as TE JOIN
		TAircraft as TA ON TA.intAircraftId = TE.intAircraftId
	JOIN TCarriers as TC
		ON TC.intCarrierId = TA.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId

GO

-- -------------------------------------------------------------------------
-- View to show all out of service aircraft
-- -------------------------------------------------------------------------
GO

CREATE VIEW vOutOfServiceAircraft
AS
SELECT 
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType,
	TA.intAircraftId,
	TA.strTailNumber,
	TE.intEventid,
	TE.strRemark,
	TE.dtmNextUpdate,
	TE.blnBackInService,
	TE.dtmStartTime,
	TE.dtmEndTime,
	TE.strDownTime
FROM 
	TEvents as TE JOIN
		TAircraft as TA ON TA.intAircraftId = TE.intAircraftId
	JOIN TCarriers as TC
		ON TC.intCarrierId = TA.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId
WHERE 
	TE.blnBackInService = 0

GO

-- -------------------------------------------------------------------------
-- View to show all back in service aircraft
-- -------------------------------------------------------------------------
GO

CREATE VIEW vInServiceAircraft
AS
SELECT 
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType,
	TA.intAircraftId,
	TA.strTailNumber,
	TE.intEventid,
	TE.strRemark,
	TE.dtmNextUpdate,
	TE.blnBackInService,
	TE.dtmStartTime,
	TE.dtmEndTime,
	TE.strDownTime
FROM 
	TEvents as TE JOIN
		TAircraft as TA ON TA.intAircraftId = TE.intAircraftId
	JOIN TCarriers as TC
		ON TC.intCarrierId = TA.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId
WHERE 
	TE.blnBackInService = 1

GO

-- -------------------------------------------------------------------------
-- View to show all Reason
-- -------------------------------------------------------------------------
GO

CREATE VIEW vAllReason
AS
SELECT * FROM TReasons

GO

-- -------------------------------------------------------------------------
-- View to show all Carrier
-- -------------------------------------------------------------------------
GO

CREATE VIEW vAllCarrier
AS
SELECT * FROM TCarriers

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
	TA.intAircraftId,
	TA.strTailNumber,
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType
FROM 
	TAircraft as TA JOIN TCarriers as TC
		ON TA.intCarrierId = TC.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId
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
	TA.intAircraftId,
	TA.strTailNumber,
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType,
	TE.intEventid,
	TE.strRemark,
	TE.dtmNextUpdate,
	TE.blnBackInService,
	TE.dtmStartTime,
	TE.dtmEndTime,
	TE.strDownTime
FROM 
	TEvents as TE JOIN
		TAircraft as TA ON TA.intAircraftId = TE.intAircraftId
	JOIN TCarriers as TC
		ON TC.intCarrierId = TA.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId
WHERE
	TC.intCarrierId = @intCarrierId
	AND TE.blnBackInService = 0

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
	TA.intAircraftId,
	TA.strTailNumber,
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType,
	TE.intEventid,
	TE.strRemark,
	TE.dtmNextUpdate,
	TE.blnBackInService,
	TE.dtmStartTime,
	TE.dtmEndTime,
	TE.strDownTime
FROM 
	TEvents as TE JOIN
		TAircraft as TA ON TA.intAircraftId = TE.intAircraftId
	JOIN TCarriers as TC
		ON TC.intCarrierId = TA.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId
WHERE
	TC.intCarrierId = @intCarrierId
	AND TE.blnBackInService = 1

END;

GO

-- -------------------------------------------------------------------------
-- Stored procedure to update an aircraft
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspUpdateAircraftServiceStatus
	@intEventId			AS INTEGER,
	@intAircraftId		AS INTEGER,
	@blnBackInService	AS INTEGER
AS

BEGIN

-- Update the service status and end time
UPDATE 
	TEvents
SET 
	blnBackInService = @blnBackInService,
	-- Set the current UTC time as the end time
	dtmEndTime = GETUTCDATE()
WHERE 
	intAircraftId = @intAircraftId
	AND intEventId = @intEventId

-- Return the aircraft
SELECT 
	TA.intAircraftId,
	TA.strTailNumber,
	TC.intCarrierId,
	TC.strCarrier,
	TT.intTypeId,
	TT.strType,
	TE.intEventid,
	TE.strRemark,
	TE.dtmNextUpdate,
	TE.blnBackInService,
	TE.dtmStartTime,
	TE.dtmEndTime,
	TE.strDownTime
FROM 
	TEvents as TE JOIN
		TAircraft as TA ON TA.intAircraftId = TE.intAircraftId
	JOIN TCarriers as TC
		ON TC.intCarrierId = TA.intCarrierId
	JOIN TTypes as TT
		ON TT.intTypeId = TA.intTypeId
WHERE 
	TA.intAircraftId = @intAircraftId
	AND TE.intEventId = @intEventId;

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

DELETE FROM TEventReasons WHERE intEventId IN (SELECT intEventId FROM TEvents WHERE intAircraftId = @intAircraftId);
DELETE FROM TEvents WHERE intAircraftId = @intAircraftId;
DELETE FROM TAircraft WHERE intAircraftId = @intAircraftId;

END;

GO

-- -------------------------------------------------------------------------
-- Search reason for a aircraft
-- -------------------------------------------------------------------------
GO

CREATE PROCEDURE uspGetReasonsForEvent
    @intEventId		AS INTEGER
AS

BEGIN

SELECT 
	TR.intReasonId,
	TR.strReason 
FROM 
	TEvents as TE JOIN TEventReasons as TER
		ON TER.intEventId = TE.intEventId
	JOIN TReasons as TR
		ON TR.intReasonId = TER.intReasonId
WHERE 
	TE.intEventId = @intEventId;

END;

GO