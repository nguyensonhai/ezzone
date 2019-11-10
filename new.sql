create database `laptop_sales`;

drop  database`laptop_sales`;

use `laptop_sales`;

create table `accounts` (
	id int(10) auto_increment primary key not null,
    username varchar(20) not null,
    password varchar(20) not null,
    email varchar(50),
    fullname varchar(50),
    birthday date,
    position varchar(20) not null,
    address varchar(100),
    phone VARCHAR(11),
    avatar longtext
);

INSERT INTO `accounts` ( `username`, `password`, `email`, `fullname`, `birthday`, `position`, `address`, `phone`, `avatar`)
VALUES ('nsh', '1', 'nguyensonhai1009@gmail.com', 'Nguyễn Sơn Hải', '1995-09-10', 'admin', 'TP. Hồ Chí Minh', '0766701009', '');

UPDATE `accounts` SET `password` = ?, `email` = ?, `fullname` = ?, `birthday` = ?, `position` = ?, `address` = ?, `phone` = ? WHERE (`username` = ?);


select * from accounts;
select * from categories;
select * from products;

CREATE TABLE `categories` (
	id VARCHAR(50) NOT NULL,
	name VARCHAR(50) NOT NULL ,
	PRIMARY KEY (id)
);


CREATE TABLE `products` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`category_id` VARCHAR(50) NOT NULL,
	`name` VARCHAR(100) NOT NULL,
	`producer` VARCHAR(50) NOT NULL,
	`info` VARCHAR(500) NOT NULL ,
	`img` VARCHAR(500) NOT NULL ,
	`price` DOUBLE NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_products_categories` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
);

CREATE TABLE `laptop_sales`.`bill` (
  `idbill` INT(11) NOT NULL AUTO_INCREMENT,
  `idUser` INT(10) NOT NULL,
  `date` DATETIME NOT NULL,
  `total` INT(11) NOT NULL,
  PRIMARY KEY (`idbill`),
  INDEX `FK_bill_accounts_idx` (`idUser` ASC) VISIBLE,
  CONSTRAINT `FK_bill_accounts`
    FOREIGN KEY (`idUser`)
    REFERENCES `laptop_sales`.`accounts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `laptop_sales`.`billinfo` (
  `idbill` INT(11) NOT NULL,accounts
  `productID` INT(11) NOT NULL,
  `amount` INT(11) NOT NULL,
  `total` INT(11) NOT NULL,
  PRIMARY KEY (`idbill`, `productID`),
  INDEX `FK_billinfo_products_idx` (`productID` ASC) VISIBLE,
  CONSTRAINT `FK_billinfo_products`
    FOREIGN KEY (`productID`)
    REFERENCES `laptop_sales`.`products` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FX_billinfo_bill`
    FOREIGN KEY (`idbill`)
    REFERENCES `laptop_sales`.`bill` (`idbill`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
    CREATE TABLE `laptop_sales`.`cart` (
  `idcart` INT NOT NULL AUTO_INCREMENT,
  `img` LONGTEXT NULL,
  `name` VARCHAR(45) NULL,
  `total` VARCHAR(45) NULL,
  PRIMARY KEY (`idcart`));


