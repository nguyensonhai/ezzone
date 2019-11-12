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

INSERT INTO `laptop_sales`.`categories` (`id`, `name`) VALUES ('laptop', 'Laptop');
INSERT INTO `laptop_sales`.`categories` (`id`, `name`) VALUES ('mouse', 'Chuột');
INSERT INTO `laptop_sales`.`categories` (`id`, `name`) VALUES ('keyboard', 'Bàn Phím');
INSERT INTO `laptop_sales`.`categories` (`id`, `name`) VALUES ('harddrive', 'Ổ đĩa cứng');
INSERT INTO `laptop_sales`.`categories` (`id`, `name`) VALUES ('cpu', 'CPU');
INSERT INTO `laptop_sales`.`categories` (`id`, `name`) VALUES ('gpu', 'Card đồ hoạ');


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
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('laptop', 'Razer Blade Stealth 13.3 (2019)', 'Razer', 'i7 8565U RAM 16GB SSD 256GB MX150 FHD IPS Touch', 'file:/C:/Users/nguye/Pictures/Products/Laptop/Razer_Blade_Stealth_13.3.jpg', '39990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('laptop', 'Dell Alienware M15 R2', 'Dell', 'i7-9750H RAM 8GB SSD 256GB FHD IPS RTX 2060', 'file:/C:/Users/nguye/Pictures/Products/Laptop/m15_r2.jpg', '51990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('laptop', 'ThinkPad P1', 'Lenovo', 'i7-8750H RAM 16GB SSD 512GB Quadro P1000 FHD IPS', 'file:/C:/Users/nguye/Pictures/Products/Laptop/p1.jpg', '45990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('laptop', 'Asus ROG Strix G G731', 'Asus', 'Intel Core i7-9750H 2.6GHz up to 4.5GHz 12MB', 'file:/C:/Users/nguye/Pictures/Products/Laptop/g731.jpg', '41990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('laptop', 'HP Spectre X360 13 (2019)', 'HP', 'i5-8265U SSD 256GB RAM 8GB FHD IPS TOUCH', 'file:/C:/Users/nguye/Pictures/Products/Laptop/hp_spectre_x360.jpg', '41990000');

INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('cpu', 'Core i7 9700', 'Intel', '12M / 3.0GHz upto 4.70GHz / 8 nhân 8 luồng', 'file:/C:/Users/nguye/Pictures/Products/CPU/i7.jpg', '8990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('cpu', 'Core i5 9500', 'Intel', '9M / 3.0GHz upto 4.40GHz / 6 nhân 6 luồng', 'file:/C:/Users/nguye/Pictures/Products/CPU/i5.jpg', '5690000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('cpu', 'Core i3 9100F', 'Intel', '6M / 3.6GHz upto 4.20GHz / 4 nhân 4 luồng', 'file:/C:/Users/nguye/Pictures/Products/CPU/i3.jpg', '2190000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('cpu', 'AMD Ryzen 9 3900x', 'AMD', '70MB /3.8GHz /12 nhân 24 luồng', 'file:/C:/Users/nguye/Pictures/Products/CPU/ryzen9.jpg', '13090000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('cpu', 'AMD Ryzen 7 3800x', 'AMD', '36MB /3.9GHz /8 nhân 16 luồng', 'file:/C:/Users/nguye/Pictures/Products/CPU/ryzen7.jpg', '10290000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('cpu', 'AMD Ryzen 5 3400G', 'AMD', '6MB /3.7GHz /4 nhân 8 luồng', 'file:/C:/Users/nguye/Pictures/Products/CPU/ryzen5.jpg', '13090000');

INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('gpu', 'ROG Strix 2080Ti Matrix 11GB', 'Asus', '', 'file:/C:/Users/nguye/Pictures/Products/GPU/2080ti.png', '54990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('gpu', 'ROG Strix GeForce RTX 2070 SUPER 8GB', 'Asus', '', 'file:/C:/Users/nguye/Pictures/Products/GPU/2070.png', '16590000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('gpu', 'GeForce RTX 2060 SUPER WINFORCE OC 8GB', 'NDIVIA', '', 'file:/C:/Users/nguye/Pictures/Products/GPU/2060.png', '11990000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('gpu', 'MSI GTX 1660 Ti GAMING X 6G', 'MSI', '', 'file:/C:/Users/nguye/Pictures/Products/GPU/1660ti.png', '8390000');
INSERT INTO `products` (`category_id`, `name`, `producer`, `info`, `img`, `price`)
VALUES ('gpu', 'ROG Strix GeForce® GTX 1650 Advance 4GB', 'Asus', '', 'file:/C:/Users/nguye/Pictures/Products/GPU/1650.jpg', '4770000');




CREATE TABLE `bill` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user` INT(10) NOT NULL,
  `customer_name` varchar(50) NOT NULL,
  `customer_phone` int(11) NOT NULL,
  `date` DATETIME NOT NULL,
  `total` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_bill_accounts_idx` (`user` ASC) VISIBLE,
  CONSTRAINT `FK_bill_accounts`
    FOREIGN KEY (`user`)
    REFERENCES `accounts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `bill_info` (
  `bill_id` INT(11) NOT NULL,
  `product_id` INT(11) NOT NULL,
  `amount` INT(11) NOT NULL,
  `total` INT(50) NOT NULL,
  PRIMARY KEY (`bill_id`, `product_id`),
  INDEX `FK_billinfo_products_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `FK_billinfo_products`
    FOREIGN KEY (`product_id`)
    REFERENCES `products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FX_billinfo_bill`
    FOREIGN KEY (`bill_id`)
    REFERENCES `bill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
