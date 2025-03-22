-- -------------------------------------------------------------------------
-- Create Tables
-- -------------------------------------------------------------------------
CREATE TABLE TCarriers
(
    intCarrierId INTEGER AUTO_INCREMENT NOT NULL,
    strCarrier   NVARCHAR(250) NOT NULL,
    CONSTRAINT TCarriers_PK PRIMARY KEY (intCarrierId)
);

CREATE TABLE TTypes
(
    intTypeId INTEGER AUTO_INCREMENT NOT NULL,
    strType   NVARCHAR(250) NOT NULL,
    CONSTRAINT TTypes_PK PRIMARY KEY (intTypeId)
);

CREATE TABLE TAircraft
(
    intAircraftId INTEGER AUTO_INCREMENT NOT NULL,
    strTailNumber NVARCHAR(250) NOT NULL,
    intTypeId     INTEGER NOT NULL,
    intCarrierId  INTEGER NOT NULL,
    CONSTRAINT TAircraft_UQ UNIQUE (strTailNumber),
    CONSTRAINT TAircraft_PK PRIMARY KEY (intAircraftId)
);

CREATE TABLE TEvents
(
    intEventId       INTEGER AUTO_INCREMENT NOT NULL,
    intAircraftId    INTEGER NOT NULL,
    strRemark        NVARCHAR(250) NOT NULL,
    dtmNextUpdate    DATETIME,
    blnBackInService INTEGER NOT NULL,
    dtmStartTime     DATETIME,
    dtmEndTime       DATETIME,
    CONSTRAINT TEvents_PK PRIMARY KEY (intEventId)
);

CREATE TABLE TUsers
(
    intUserId   INTEGER AUTO_INCREMENT NOT NULL,
    strUsername NVARCHAR(250) NOT NULL,
    strPassword NVARCHAR(250) NOT NULL,
    strRoles    NVARCHAR(250) NOT NULL,
    CONSTRAINT TUsers_PK PRIMARY KEY (intUserId)
);

CREATE TABLE TRoles
(
    intRoleId INTEGER AUTO_INCREMENT NOT NULL,
    strRole   NVARCHAR(250) NOT NULL,
    CONSTRAINT TRoles_PK PRIMARY KEY (intRoleId)
);

CREATE TABLE TReasons
(
    intReasonId INTEGER AUTO_INCREMENT NOT NULL,
    strReason   NVARCHAR(250) NOT NULL,
    CONSTRAINT TReasons_PK PRIMARY KEY (intReasonId)
);

CREATE TABLE TEventReasons
(
    intEventReasonId INTEGER AUTO_INCREMENT NOT NULL,
    intEventId       INTEGER NOT NULL,
    intReasonId      INTEGER NOT NULL,
    CONSTRAINT TEventReasons_PK PRIMARY KEY (intEventReasonId)
);

-- -------------------------------------------------------------------------
-- Define Relationships and Create Foreign Keys
-- -------------------------------------------------------------------------
-- 1
ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TCarriers_FK
    FOREIGN KEY (intCarrierId) REFERENCES TCarriers (intCarrierId);

-- 2
ALTER TABLE TAircraft ADD CONSTRAINT TAircraft_TTypes_FK
    FOREIGN KEY (intTypeId) REFERENCES TTypes (intTypeId);

-- 3
ALTER TABLE TEvents ADD CONSTRAINT TEvents_TAircraft_FK
    FOREIGN KEY (intAircraftId) REFERENCES TAircraft (intAircraftId);

-- 4
ALTER TABLE TEventReasons ADD CONSTRAINT TEventReasons_TEvents_FK
    FOREIGN KEY (intEventId) REFERENCES TEvents (intEventId);

-- 5
ALTER TABLE TEventReasons ADD CONSTRAINT TEventReasons_TReasons_FK
    FOREIGN KEY (intReasonId) REFERENCES TReasons (intReasonId);

-- -------------------------------------------------------------------------
-- Insert Data
-- -------------------------------------------------------------------------
INSERT INTO TCarriers (strCarrier)
VALUES ('Cargojet'),
       ('Mesa Airlines'),
       ('Ameriflight'),
       ('ABX Air'),
       ('Atlas Air'),
       ('Swift Air'),
       ('Kalitta Air');

INSERT INTO TTypes (strType)
VALUES ('767'),
       ('650'),
       ('762');

INSERT INTO TAircraft (strTailNumber, intTypeId, intCarrierId)
VALUES ('N767AX', 1, 1),
       ('N650GT', 2, 2),
       ('N762CK', 3, 3);

INSERT INTO TEvents (intAircraftId, strRemark, dtmNextUpdate, blnBackInService, dtmStartTime, dtmEndTime)
VALUES (1, 'Bird strike to the #1 engine', '2024-10-15 21:00:00', 0, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
       (2, '#1 Generator inop', '2024-10-15 21:00:00', 1, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
       (3, 'Awaiting replacement FMC and required engineering order from Boeing', '2024-10-15 21:00:00', 0, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);

INSERT INTO TReasons (strReason)
VALUES ('Maintenance'),
       ('AOG'),
       ('Damage');

INSERT INTO TEventReasons (intEventId, intReasonId)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3);

INSERT INTO TRoles (strRole)
VALUES ('ROLE_Admin'),
       ('ROLE_User');

INSERT INTO TUsers (strUsername, strPassword, strRoles)
VALUES ('admin', '$2a$10$ci0jIZ.atngw0kgESVpr3OuK5rg7YvlnP.2TnSeBy9JGk4DEWjQIK', 'ROLE_Admin,ROLE_User'),
       ('user', '$2a$12$pxog/SkowL.TfN51pf.Gp.nG5cd/2IBK5rDu.0JDJcm8cUPcTPClO', 'ROLE_User');

-- -------------------------------------------------------------------------
-- Views
-- -------------------------------------------------------------------------

-- View to show all aircraft events
CREATE VIEW vAllAircraft AS
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
    TE.dtmEndTime
FROM
    TEvents AS TE
        JOIN TAircraft AS TA ON TA.intAircraftId = TE.intAircraftId
        JOIN TCarriers AS TC ON TC.intCarrierId = TA.intCarrierId
        JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId;

-- View to show all out of service aircraft
CREATE VIEW vOutOfServiceAircraft AS
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
    TE.dtmEndTime
FROM
    TEvents AS TE
        JOIN TAircraft AS TA ON TA.intAircraftId = TE.intAircraftId
        JOIN TCarriers AS TC ON TC.intCarrierId = TA.intCarrierId
        JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId
WHERE
    TE.blnBackInService = 0;

-- View to show all back in service aircraft
CREATE VIEW vInServiceAircraft AS
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
    TE.dtmEndTime
FROM
    TEvents AS TE
        JOIN TAircraft AS TA ON TA.intAircraftId = TE.intAircraftId
        JOIN TCarriers AS TC ON TC.intCarrierId = TA.intCarrierId
        JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId
WHERE
    TE.blnBackInService = 1;

-- View to show all Reason
CREATE VIEW vAllReason AS
SELECT * FROM TReasons;

-- View to show all Carrier
CREATE VIEW vAllCarrier AS
SELECT * FROM TCarriers;

-- View to show all events as history
CREATE VIEW vEventHistory AS
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
    TE.dtmEndTime
FROM
    TEvents AS TE
        JOIN TAircraft AS TA ON TE.intAircraftId = TA.intAircraftId
        JOIN TCarriers AS TC ON TC.intCarrierId = TA.intCarrierId
        JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId;

-- -------------------------------------------------------------------------
-- Stored Procedures
-- -------------------------------------------------------------------------
-- Stored procedure to show all aircraft from a specific carrier
CREATE ALIAS uspShowCarrierAircraft AS $$
void uspShowCarrierAircraft(Connection conn, Integer intCarrierId) throws SQLException {
    String sql = "SELECT TA.intAircraftId, TA.strTailNumber, TC.intCarrierId, TC.strCarrier, TT.intTypeId, TT.strType " +
                 "FROM TAircraft AS TA JOIN TCarriers AS TC ON TA.intCarrierId = TC.intCarrierId " +
                 "JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId WHERE TC.intCarrierId = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, intCarrierId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            // Process the result set
        }
    }
}
$$;

-- Stored procedure to show all aircraft from a specific carrier that is out of service
CREATE ALIAS uspShowCarrierAircraftOOS AS $$
void uspShowCarrierAircraftOOS(Connection conn, Integer intCarrierId) throws SQLException {
    String sql = "SELECT TA.intAircraftId, TA.strTailNumber, TC.intCarrierId, TC.strCarrier, TT.intTypeId, TT.strType, " +
                 "TE.intEventid, TE.strRemark, TE.dtmNextUpdate, TE.blnBackInService, TE.dtmStartTime, TE.dtmEndTime " +
                 "FROM TEvents AS TE JOIN TAircraft AS TA ON TA.intAircraftId = TE.intAircraftId " +
                 "JOIN TCarriers AS TC ON TC.intCarrierId = TA.intCarrierId " +
                 "JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId WHERE TC.intCarrierId = ? AND TE.blnBackInService = 0";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, intCarrierId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            // Process the result set
        }
    }
}
$$;

-- Stored procedure to show all aircraft from a specific carrier that is in service
CREATE ALIAS uspShowCarrierAircraftIS AS $$
void uspShowCarrierAircraftIS(Connection conn, Integer intCarrierId) throws SQLException {
    String sql = "SELECT TA.intAircraftId, TA.strTailNumber, TC.intCarrierId, TC.strCarrier, TT.intTypeId, TT.strType, " +
                 "TE.intEventid, TE.strRemark, TE.dtmNextUpdate, TE.blnBackInService, TE.dtmStartTime, TE.dtmEndTime " +
                 "FROM TEvents AS TE JOIN TAircraft AS TA ON TA.intAircraftId = TE.intAircraftId " +
                 "JOIN TCarriers AS TC ON TC.intCarrierId = TA.intCarrierId " +
                 "JOIN TTypes AS TT ON TT.intTypeId = TA.intTypeId WHERE TC.intCarrierId = ? AND TE.blnBackInService = 1";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, intCarrierId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            // Process the result set
        }
    }
}
$$;

-- Stored procedure to update an aircraft
CREATE ALIAS uspUpdateAircraftServiceStatus AS $$
void uspUpdateAircraftServiceStatus(Connection conn, Integer intEventId, Integer intAircraftId, Integer blnBackInService) throws SQLException {
    String sql = "UPDATE TEvents SET blnBackInService = ?, dtmEndTime = CURRENT_TIMESTAMP WHERE intAircraftId = ? AND intEventId = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, blnBackInService);
        pstmt.setInt(2, intAircraftId);
        pstmt.setInt(3, intEventId);
        pstmt.executeUpdate();
}
}
$$;

-- Stored procedure to remove an aircraft
CREATE ALIAS uspDeleteAircraft AS $$
void uspDeleteAircraft(Connection conn, Integer intAircraftId) throws SQLException {
    String sql1 = "DELETE FROM TEventReasons WHERE intEventId IN (SELECT intEventId FROM TEvents WHERE intAircraftId = ?)";
    String sql2 = "DELETE FROM TEvents WHERE intAircraftId = ?";
    String sql3 = "DELETE FROM TAircraft WHERE intAircraftId = ?";
    try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);
         PreparedStatement pstmt2 = conn.prepareStatement(sql2);
         PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
        pstmt1.setInt(1, intAircraftId);
        pstmt1.executeUpdate();
        pstmt2.setInt(1, intAircraftId);
        pstmt2.executeUpdate();
        pstmt3.setInt(1, intAircraftId);
        pstmt3.executeUpdate();
}
}
$$;

-- Search reason for an aircraft
CREATE ALIAS uspGetReasonsForEvent AS $$
void uspGetReasonsForEvent(Connection conn, Integer intEventId) throws SQLException {
    String sql = "SELECT TR.intReasonId, TR.strReason FROM TEvents AS TE JOIN TEventReasons AS TER ON TER.intEventId = TE.intEventId " +
                 "JOIN TReasons AS TR ON TR.intReasonId = TER.intReasonId WHERE TE.intEventId = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, intEventId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            // Process the result set
        }
    }
}
$$;