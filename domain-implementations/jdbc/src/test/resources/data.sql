USE Shoe_Shop;

INSERT INTO INVENTORY (INVENTORY_ID, CAPACITY) VALUES
                                                   (1, 120),
                                                   (2, 8);
INSERT INTO ADDRESS (ADDRESS_ID, LOCATION) VALUES (1, 'Taradell');

INSERT INTO MODEL (MODEL_ID, NAME, BRAND) VALUES
                                              (1, 'Air Force', 'Nike'),
                                              (2, 'Cuck Converse', 'Converse'),
                                              (3, 'Dunk', 'Nike');

INSERT INTO SHOE_STORE (SHOE_STORE_ID, NAME, OWNER, LOCATION) VALUES
                                                                  (1, 'Footwear Corder', 'Martí Corder', 'Vic'),
                                                                  (2, 'Footwear Corder', 'Martí Corder', 'Taradell');

INSERT INTO SUPPLIER (SUPPLIER_ID, NAME, PHONE) VALUES
                                                    (1, 'Nike Supplier', '933881025'),
                                                    (2, 'Shoes Branding', '9876543210');

INSERT INTO CLIENT (CLIENT_ID, DNI, NAME, PHONE) VALUES
                                                     (1, '48044773X', 'Roger Gost', '630089333'),
                                                     (2, '45678933R', 'Oriol SAla', '683773443'),
                                                     (3, '44287764P', 'Andreu Vilaregut', '678875432');

INSERT INTO SHOE (SHOE_ID, MODEL_ID, INVENTORY_ID, PRICE, COLOR, SIZE) VALUES
                                                                                 (1, 1, 1, 99.99, 'White', '42'),
                                                                                 (2, 2, 2, 120, 'Black', '45');

