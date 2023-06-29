--INSERT INTO users VALUES (DEFAULT, '$2a$10$WyGU0850Gt6l9niernBpb.58pCPz8XXEaI4qvOyj5rdEYIygCat/u', 'ADMIN', 'admin');
--INSERT INTO users VALUES (DEFAULT, '$2a$10$APZmkzJfSiMIH0RzO8fb5eOZ0QCe3273LLLDgYzzhJxB7XA5RDPHy', 'USER', 'user');

INSERT INTO club VALUES (DEFAULT, 'Beograd', 'Partizan');
INSERT INTO club VALUES (DEFAULT, 'Beograd', 'Crvena Zvezda');
INSERT INTO club VALUES (DEFAULT, 'Subotica', 'Spartak');
INSERT INTO club VALUES (DEFAULT, 'Novi Sadk', 'Proleter');

INSERT INTO users (id,username, password, email, last_login, first_name, last_name, display_name, description, role)
VALUES (DEFAULT,'admin', '$2a$10$WyGU0850Gt6l9niernBpb.58pCPz8XXEaI4qvOyj5rdEYIygCat/u', 'a@gmail.com', '2023-06-06', 'Andjela', 'Lozanov', 'andjela', 'a', 'ADMIN');


INSERT INTO users (id,username, password, email, last_login, first_name, last_name, display_name, description, role)
VALUES (DEFAULT,'user', '$2a$10$APZmkzJfSiMIH0RzO8fb5eOZ0QCe3273LLLDgYzzhJxB7XA5RDPHy', 'a@gmail.com', '2023-05-06', 'Andjela', 'Lozanov', 'andj', 'a', 'USER');

INSERT INTO user_friends (user_id, friend_id) VALUES (1, 2);
INSERT INTO user_friends (user_id, friend_id) VALUES (2, 1);

INSERT INTO posts values (DEFAULT,"content","2023-06-06",2);

INSERT INTO images (id, path, post_id, user_id) VALUES 
(DEFAULT, 'https://www.visitdubai.com/-/media/images/leisure/campaigns/dubai-presents/itineraries/nature/nature-header-2.jpg?&cw=256&ch=256', 1, 2);

INSERT INTO images (id, path, post_id, user_id) VALUES 
(DEFAULT, 'https://cdn131.picsart.com/299493580179201.jpg?type=webp&to=crop&r=256', 1, 2);
INSERT INTO post_images (post_id, image_id) VALUES (1, 1);
INSERT INTO post_images (post_id, image_id) VALUES (1, 2);