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