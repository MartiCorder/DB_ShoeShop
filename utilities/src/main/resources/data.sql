USE Shoe_Shop;

INSERT INTO INVENTORY (ID_INVENTORY, CAPACITY) VALUES
                                                   (1, 120),
                                                   (2, 8);
INSERT INTO ADDRESS (ID_ADDRESS, LOCATION) VALUES (1, 'Taradell');

INSERT INTO MODEL (ID_MODEL, NAME, BRAND) VALUES
                                              (1, 'Air Force', 'Nike'),
                                              (2, 'Cuck Converse', 'Converse'),
                                              (3, 'Dunk', 'Nike');

INSERT INTO SHOE_STORE (ID_STORE, NAME, OWNER, LOCATION, ID_INVENTORY) VALUES
                                                                           (1, 'Footwear Corder', 'Martí Corder', 'Vic', 1),
                                                                           (2, 'Footwear Corder', 'Martí Corder', 'Taradell', 2);

INSERT INTO SUPPLIER (ID_SUPPLIER, NAME, PHONE) VALUES
                                                    (1, 'Nike Supplier', '933881025'),
                                                    (2, 'Shoes Branding', '9876543210');

INSERT INTO CLIENT (ID_CLIENT, DNI, NAME, PHONE) VALUES
                                                     (1, '48044773X', 'Roger Gost', '630089333'),
                                                     (2, '45678933R', 'Oriol SAla', '683773443'),
                                                     (3, '44287764P', 'Andreu Vilaregut', '678875432');

INSERT INTO SHOE (ID_SHOE, ID_MODEL, ID_INVENTORY, PRICE, COLOR, SIZE) VALUES
                                                                           (1, 1, 1, 99.99, 'White', '42'),
                                                                           (2, 2, 2, 120, 'Black', '45');



