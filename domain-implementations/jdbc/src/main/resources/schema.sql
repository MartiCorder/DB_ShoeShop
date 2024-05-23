-- PHPMYADMIN SQL DUMP
-- VERSION 5.0.1
-- HTTPS://WWW.PHPMYADMIN.NET/
--
-- HOST: 127.0.0.1
-- GENERATION TIME: MAR 24, 2020 AT 06:55 PM
-- SERVER VERSION: 10.4.11-MARIADB
-- PHP VERSION: 7.4.3
DROP DATABASE IF EXISTS Shoe_Shop;

CREATE DATABASE Shoe_Shop;

USE Shoe_Shop;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET TIME_ZONE = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES UTF8MB4 */;

CREATE TABLE INVENTORY (
                           INVENTORY_ID INT AUTO_INCREMENT PRIMARY KEY,
                           CAPACITY INT
);

CREATE TABLE ADDRESS (
                         ADDRESS_ID INT AUTO_INCREMENT PRIMARY KEY,
                         LOCATION VARCHAR(255)
);

CREATE TABLE MODEL (
                       MODEL_ID INT AUTO_INCREMENT PRIMARY KEY,
                       NAME VARCHAR(255),
                       BRAND VARCHAR(255)
);

CREATE TABLE SHOE_STORE (
                            SHOE_STORE_ID INT AUTO_INCREMENT PRIMARY KEY,
                            NAME VARCHAR(255),
                            OWNER VARCHAR(255),
                            LOCATION VARCHAR(255),
                            INVENTORY_ID INT
);

CREATE TABLE SUPPLIER (
                          SUPPLIER_ID INT AUTO_INCREMENT PRIMARY KEY,
                          NAME VARCHAR(255),
                          PHONE VARCHAR(15)
);

CREATE TABLE CLIENT (
                        CLIENT_ID INT AUTO_INCREMENT PRIMARY KEY,
                        DNI VARCHAR(20),
                        NAME VARCHAR(255),
                        PHONE VARCHAR(15),
                        ADDRESS_ID INT,
                        FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS(ADDRESS_ID)
);

CREATE TABLE SHOE (
                      SHOE_ID INT PRIMARY KEY,
                      MODEL_ID INT,
                      INVENTORY_ID INT,
                      PRICE DECIMAL(10, 2),
                      COLOR VARCHAR(50),
                      SIZE VARCHAR(10),
                      FOREIGN KEY (MODEL_ID) REFERENCES MODEL(MODEL_ID),
                      FOREIGN KEY (INVENTORY_ID) REFERENCES INVENTORY(INVENTORY_ID)
);

CREATE TABLE SHOE_STORE_CLIENT (
                                   SHOE_STORE_ID INT,
                                   CLIENT_ID INT,
                                   PRIMARY KEY (SHOE_STORE_ID, CLIENT_ID),
                                   FOREIGN KEY (SHOE_STORE_ID) REFERENCES SHOE_STORE(SHOE_STORE_ID),
                                   FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT(CLIENT_ID)
);

CREATE TABLE SHOE_STORE_INVENTORY (
                                      SHOE_STORE_ID INT,
                                      INVENTORY_ID INT,
                                      PRIMARY KEY (SHOE_STORE_ID, INVENTORY_ID),
                                      FOREIGN KEY (SHOE_STORE_ID) REFERENCES SHOE_STORE(SHOE_STORE_ID),
                                      FOREIGN KEY (INVENTORY_ID) REFERENCES INVENTORY(INVENTORY_ID)
);

CREATE TABLE SHOE_STORE_SUPPLIER (
                                     SHOE_STORE_ID INT,
                                     SUPPLIER_ID INT,
                                     PRIMARY KEY (SHOE_STORE_ID, SUPPLIER_ID),
                                     FOREIGN KEY (SHOE_STORE_ID) REFERENCES SHOE_STORE(SHOE_STORE_ID),
                                     FOREIGN KEY (SUPPLIER_ID) REFERENCES SUPPLIER(SUPPLIER_ID)
);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
