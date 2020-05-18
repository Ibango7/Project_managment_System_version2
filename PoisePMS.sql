/*CREATE DATABASE IF NOT EXISTS PoisePMS;*/

CREATE TABLE Manager(
    managerID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    telephone VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    projNumber INT,
    PRIMARY KEY (managerID)
);

CREATE TABLE Architect(
    architectID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    telephone VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    projNumber INT,
    PRIMARY KEY (architectID)
    
);

CREATE TABLE Customer(
    customerID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    telephone VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    projNumber INT,
    PRIMARY KEY (customerID)
);

CREATE TABLE Engineer(
    engineerID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    telephone VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    projNumber INT,
    PRIMARY KEY (engineerID)
);

CREATE TABLE Project(
    projNumber INT NOT NULL,
    projName VARCHAR(255),
    buildtype VARCHAR(255),
    address VARCHAR(255),
    ErFNum VARCHAR(255),
    fee DOUBLE(18,4),
    amountPaid DOUBLE(18,4),
    projDateAssigned DATE,
    projDeadLine DATE,
    isComplete BOOLEAN DEFAULT 0,
    managerID INT,
    architectID INT,
    customerID INT,
    engineerID INT,
    dateFinalized DATE,
    PRIMARY KEY (projNumber),
    FOREIGN KEY (managerID) REFERENCES Manager(managerID),
    FOREIGN KEY (architectID) REFERENCES Architect(architectID),
    FOREIGN KEY (customerID) REFERENCES Customer(customerID),
    FOREIGN KEY (engineerID) REFERENCES Engineer(engineerID)
);

ALTER TABLE Customer
ADD FOREIGN KEY (projNumber) REFERENCES Project (projNumber);

ALTER TABLE Manager
ADD FOREIGN KEY (projNumber) REFERENCES Project (projNumber);

ALTER TABLE Architect
ADD FOREIGN KEY (projNumber) REFERENCES Project (projNumber);

ALTER TABLE Engineer
ADD FOREIGN KEY (projNumber) REFERENCES Project (projNumber);

/* --Two row insert in each table
INSERT INTO Project(projNumber, projName, buildtype, address, ErFNum, fee, amountPaid, projDateAssigned, projDeadLine)
VALUES (1,'Israel House', 'House', '209 Amos st PT', '209R', 129000.99, 0.0, '2020-05-13', '2020-06-13');

INSERT INTO Project(projNumber, projName, buildtype, address, ErFNum, fee, amountPaid, projDateAssigned, projDeadLine, isComplete)
VALUES (2,'Joseph Hotel', 'Hotel', 'Alvalade LD 21', '9R', 300000, 300000, '2018-02-13', '2020-01-12', 1);

INSERT INTO Customer (name, telephone, email, address, projNumber)
VALUES ('Israel', '123456789', 'israelbango@gmail.com','Add St 21',1);
INSERT INTO Customer (name, telephone, email, address, projNumber)
VALUES ('Joseph', '129364583', 'josephbango@gmail.com','Add St 22',2);

INSERT INTO Manager (name, telephone, email, address, projNumber)
VALUES ('Manager1', '1111111', 'manager1@gmail.com','Add St 23',1);
INSERT INTO Manager (name, telephone, email, address, projNumber)
VALUES ('Manager2', '1111111', 'manager2@gmail.com','Add St 24',2);


INSERT INTO Architect (name, telephone, email, address, projNumber)
VALUES ('Architect1', '22222222222', 'architect1@gmail.com','Add St 25',1);
INSERT INTO Architect (name, telephone, email, address, projNumber)
VALUES ('Architect2', '22222222222', 'architect2@gmail.com','Add St 26',2);


INSERT INTO Engineer (name, telephone, email, address, projNumber)
VALUES ('Engineer1', '333333333', 'engineer1@gmail.com','Add St 27',1);
INSERT INTO Engineer (name, telephone, email, address, projNumber)
VALUES ('Engineer2', '333333333', 'engineer2@gmail.com','Add St 28',2);

UPDATE Project
SET  managerID = 1 , architectID =  1, customerID = 1 , engineerID = 1
WHERE  projNumber = 1;

UPDATE Project
SET  managerID = 2 , architectID =  2, customerID = 2 , engineerID = 2
WHERE  projNumber = 2;*/