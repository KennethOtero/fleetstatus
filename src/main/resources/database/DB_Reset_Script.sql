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
IF OBJECT_ID('TUsers')		IS NOT NULL DROP TABLE TUsers;

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

-- -------------------------------------------------------------------------
-- Define Relationships and Create Foreign Keys
-- -------------------------------------------------------------------------

-- # Child						# Parent					# Columns
-- -------						--------					---------
-- 1 TAircraft					TCarriers					intCarrierId

ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TCarriers_FK
FOREIGN KEY (intCarrierId) REFERENCES TCarriers (intCarrierId)

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

-- -------------------------------------------------------------------------
-- Create Stored Procedures, Views, and Functions
-- -------------------------------------------------------------------------

-- Stored procedure to show all aircraft from a carrier

-- View to show all aircraft

-- View to show all out of service aircraft

-- View to show all back in service aircraft