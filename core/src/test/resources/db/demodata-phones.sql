INSERT INTO phones (id, brand, model, price) VALUES (101, 'Xiaomi', 'Mi 6', 101);
INSERT INTO phones (id, brand, model, price) VALUES (102, 'Samsung', 'S 20', 102);
INSERT INTO phones (id, brand, model, price) VALUES (103, 'Iphone', 'X', 103);
INSERT INTO phones (id, brand, model, price) VALUES (104, 'Huawei', 'P 20', 104);
INSERT INTO phones (id, brand, model, price) VALUES (105, 'Asus', 'ZenPhone 2', 105);
INSERT INTO phones (id, brand, model, price) VALUES (106, 'Colorful', 'Pixel', 0);

INSERT INTO stocks (phoneId, stock, reserved) VALUES (101, 101, 0);
INSERT INTO stocks (phoneId, stock, reserved) VALUES (102, 102, 0);
INSERT INTO stocks (phoneId, stock, reserved) VALUES (103, 103, 0);
INSERT INTO stocks (phoneId, stock, reserved) VALUES (104, 104, 0);
INSERT INTO stocks (phoneId, stock, reserved) VALUES (105, 105, 0);
INSERT INTO stocks (phoneId, stock, reserved) VALUES (106, 0, 0);

INSERT INTO colors VALUES (1, 'white');
INSERT INTO colors VALUES (2, 'black');
INSERT INTO colors VALUES (3, 'gray');
INSERT INTO colors VALUES (4, 'yellow');

INSERT INTO phone2color VALUES (106, 1);
INSERT INTO phone2color VALUES (106, 2);

INSERT INTO orders (id, secureId, firstName, lastName, deliveryAddress, contactPhoneNo, status, dateTime) VALUES (1, '913cdf30-b93c-4bbd-b1cd-0bf2099c0417', 'Ulas', 'Kastsiukovich', '1-May Street', '+375331111111', 'NEW', '2021-12-08 01:46:45.147412900');
INSERT INTO orders (id, secureId, firstName, lastName, deliveryAddress, contactPhoneNo, status, dateTime) VALUES (2, 'a000c9fe-9119-4d7b-9477-3f9329b49188', 'Anton', 'Martinov', 'Melisha 5', '+375332222222', 'DELIVERED', '2015-4-09 12:31:05.163261253');
INSERT INTO orders (id, secureId, firstName, lastName, deliveryAddress, contactPhoneNo, status, dateTime) VALUES (3, '2bac50d9-6066-42e6-966c-d8ffc5e21a78', 'Valya', 'Protasenya', 'Ostrovskogo 1', '+375333333333', 'REJECTED', '2011-11-11 05:01:13.185643500');

INSERT INTO orderItems VALUES (101, 1, 11);
INSERT INTO orderItems VALUES (102, 1, 12);
INSERT INTO orderItems VALUES (103, 1, 13);
INSERT INTO orderItems VALUES (104, 2, 2);
INSERT INTO orderItems VALUES (104, 3, 1);
INSERT INTO orderItems VALUES (105, 3, 2);


