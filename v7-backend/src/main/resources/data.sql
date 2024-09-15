--INSERT INTO users VALUES (DEFAULT, '$2a$10$WyGU0850Gt6l9niernBpb.58pCPz8XXEaI4qvOyj5rdEYIygCat/u', 'ADMIN', 'admin');
--INSERT INTO users VALUES (DEFAULT, '$2a$10$APZmkzJfSiMIH0RzO8fb5eOZ0QCe3273LLLDgYzzhJxB7XA5RDPHy', 'USER', 'user');

INSERT INTO club VALUES (DEFAULT, 'Beograd', 'Partizan');
INSERT INTO club VALUES (DEFAULT, 'Beograd', 'Crvena Zvezda');
INSERT INTO club VALUES (DEFAULT, 'Subotica', 'Spartak');
INSERT INTO club VALUES (DEFAULT, 'Novi Sadk', 'Proleter');

INSERT INTO users (id,username, password, email, last_login, first_name, last_name, display_name, description, role,dtype,profile_photo)
VALUES (DEFAULT,'user', '$2a$12$Bv2O1CWiqO04DaX/MeBvRe2EiKyuY888BXgd1ExCTf87tlcg5mId6', 'a@gmail.com', '2023-05-06', 'Andjela', 'Lozanov', 'andj', 'a', 'USER','User','https://styles.redditmedia.com/t5_1aj4te/styles/profileIcon_snoo416450f7-4442-4183-ac89-70e8a207d540-headshot-f.png?width=256&height=256&frame=1&auto=webp&crop=256:256,smart&s=5c263b25c621c3d562ab20b3fc1a79b5b756d7dc');
INSERT INTO users (id, username, password, email, last_login, first_name, last_name, display_name, description, role,dtype,profile_photo)
VALUES (DEFAULT, 'admin', '$2a$12$Bv2O1CWiqO04DaX/MeBvRe2EiKyuY888BXgd1ExCTf87tlcg5mId6', 'a@gmail.com', '2023-06-06', 'Admin', 'Admin', 'marko', 'a', 'ADMIN','Administrator','https://styles.redditmedia.com/t5_2wgxu3/styles/profileIcon_snoobdf08453-e439-44b0-95bd-d48f74368f1c-headshot.png?width=256&height=256&frame=1&auto=webp&crop=256:256,smart&s=48f8d521ae4de72a2b3d7d4e56ca69db2246ea2a');
INSERT INTO users (id, username, password, email, last_login, first_name, last_name, display_name, description, role,dtype,profile_photo)
VALUES (DEFAULT, 'user2', '$2a$12$Bv2O1CWiqO04DaX/MeBvRe2EiKyuY888BXgd1ExCTf87tlcg5mId6', 'a@gmail.com', '2023-06-06', 'Aleksa', 'Aleksic', 'aleksa', 'a', 'GROUPADMIN','GroupAdmin','https://styles.redditmedia.com/t5_3b6av4/styles/profileIcon_snoo849619b5-75b6-4de8-8583-8fd5b8315b4f-headshot.png?width=256&height=256&frame=1&auto=webp&crop=256:256,smart&s=a76d6437b444f75abae84cd8375c17482e062f85');
INSERT INTO users (id, username, password, email, last_login, first_name, last_name, display_name, description, role,dtype,profile_photo)
VALUES (DEFAULT, 'user3', '$2a$12$Bv2O1CWiqO04DaX/MeBvRe2EiKyuY888BXgd1ExCTf87tlcg5mId6', 'a@gmail.com', '2023-06-06', 'Marko', 'Maric', 'mare', 'm', 'USER','User','https://styles.redditmedia.com/t5_66kd5/styles/profileIcon_snooa206b814-b514-4751-a209-11ad2023458e-headshot.png?width=256&height=256&frame=1&auto=webp&crop=256:256,smart&s=4446ae6fcf48add7560e70172dd27efb00caf29c');
INSERT INTO users (id, username, password, email, last_login, first_name, last_name, display_name, description, role,dtype,profile_photo)
VALUES (DEFAULT, 'user4', '$2a$12$Bv2O1CWiqO04DaX/MeBvRe2EiKyuY888BXgd1ExCTf87tlcg5mId6', 'a@gmail.com', '2023-06-06', 'Nemanja', 'Nemanjic', 'nemanja', 'n', 'USER','User','https://styles.redditmedia.com/t5_571itd/styles/profileIcon_snoob0e6a05b-9cd0-4907-8efa-1e5f393cce99-headshot.png?width=256&height=256&frame=1&auto=webp&crop=256:256,smart&s=54dd6d5bea8dcf9d5af1537c7bfa43be8276e93d');

INSERT INTO user_friends (user_id, friend_id) VALUES (1, 3);
INSERT INTO user_friends (user_id, friend_id) VALUES (3, 1);
INSERT INTO user_friends (user_id, friend_id) VALUES (4, 5);
INSERT INTO user_friends (user_id, friend_id) VALUES (5, 4);

INSERT INTO grupe (id, name, description, creation_date,user_id,suspended,isdeleted) VALUES 
(DEFAULT, 'Grupa Ime', 'opis', '2023-06-30T10:00:00',3,false,false);

INSERT INTO posts (id,content,creation_date,user_id,group_id,isdeleted,comments_for_post,likes,dislikes,hearts) values (DEFAULT,"content","2023-04-30T10:00:00",1,1,false,false,1,0,0);
INSERT INTO posts (id,content,creation_date,user_id,isdeleted,comments_for_post,likes,dislikes,hearts) values (DEFAULT,"post","2023-06-06T10:00:00",1,false,false,0,0,0);
INSERT INTO posts (id,content,creation_date,user_id,isdeleted,comments_for_post,likes,dislikes,hearts) values 
(DEFAULT,"hello","2023-06-06T10:00:00",3,false,false,0,0,0);
INSERT INTO posts (id,content,creation_date,user_id,isdeleted,comments_for_post,likes,dislikes,hearts) values 
(DEFAULT,"hi","2023-06-06T10:00:00",4,false,false,0,0,0);
INSERT INTO posts (id,content,creation_date,user_id,isdeleted,comments_for_post,likes,dislikes,hearts) values 
(DEFAULT,"my cat","2023-06-06T10:00:00",5,false,false,0,0,0);


INSERT INTO comments (id,text,_timestamp,isdeleted,user_id,post_id,show_replies,likes,dislikes,hearts) values (DEFAULT,"text", "2023-04-30T10:00:00",false,1,1,false,1,0,0);
INSERT INTO comments (id,text,_timestamp,isdeleted,user_id,post_id,show_replies,likes,dislikes,hearts) values (DEFAULT,"text2","2023-04-30T11:00:00",false,3,1,false,0,0,0);
INSERT INTO comments (id,text,_timestamp,isdeleted,user_id,post_id,show_replies,likes,dislikes,hearts) values (DEFAULT,"reply text","2023-05-20T09:00:00",false,1,1,false,0,0,0);
INSERT INTO comments (id,text,_timestamp,isdeleted,user_id,post_id,show_replies,likes,dislikes,hearts) values (DEFAULT,"reply to reply","2023-06-20T21:00:00",false,3,1,false,0,0,0);
INSERT INTO comments (id,text,_timestamp,isdeleted,user_id,post_id,show_replies,likes,dislikes,hearts) values (DEFAULT,"reply to reply's reply","2023-07-20T20:00:00",false,1,1,false,0,0,0);
INSERT INTO comments (id,text,_timestamp,isdeleted,user_id,post_id,show_replies,likes,dislikes,hearts) values (DEFAULT,"opet","2023-07-20T21:00:00",false,3,1,false,0,0,0);

INSERT INTO comment_replies (comment_id, reply_id) VALUES (2, 3);
INSERT INTO comment_replies (comment_id, reply_id) VALUES (3, 4);
INSERT INTO comment_replies (comment_id, reply_id) VALUES (4, 5);
INSERT INTO comment_replies (comment_id, reply_id) VALUES (5, 6);
INSERT INTO post_comments (post_id,comment_id) values (1,1);
INSERT INTO post_comments (post_id,comment_id) values (1,2);

INSERT INTO reactions (id,type_,timestamp_,post_id,user_id,isdeleted) values (DEFAULT,"LIKE", "2023-06-30T10:00:00",1,1,false);
INSERT INTO reactions (id,type_,timestamp_,comment_id,user_id,isdeleted) values (DEFAULT,"LIKE", "2023-06-30T10:00:00",1,1,false);
INSERT INTO post_reactions (post_id, reaction_id) VALUES (1, 1);
INSERT INTO comment_reactions (comment_id, reaction_id) VALUES (1, 2);

INSERT INTO group_request (user_id_id,group_id,approved,created,isdeleted) values (2,1,false,"2023-06-30T10:00:00",false);
INSERT INTO friend_request (to_who,from_who,approved,created,isdeleted) values (1,2,false,"2023-06-30T10:00:00",false);
INSERT INTO group_members (group_id,user_id) values(1,1);
INSERT INTO images (id, image_path, user_id) VALUES 
(DEFAULT, 'https://www.visitdubai.com/-/media/images/leisure/campaigns/dubai-presents/itineraries/nature/nature-header-2.jpg?&cw=256&ch=256', 1);

INSERT INTO images (id, image_path, user_id) VALUES 
(DEFAULT, 'https://cdn131.picsart.com/299493580179201.jpg?type=webp&to=crop&r=256', 1);
INSERT INTO images (id, image_path, user_id) VALUES 
(DEFAULT, 'https://www.rts.rs/upload/thumbnail/2013/09/25/2617536/2617536_Sare-zooloski_vrt_t.jpg', 1);
INSERT INTO images (id, image_path, user_id) VALUES 
(DEFAULT, 'https://i.pinimg.com/1200x/2f/6e/53/2f6e5390f6e129f0066177069de59073.jpg', 3);
INSERT INTO images (id, image_path, user_id) VALUES 
(DEFAULT, 'https://media.licdn.com/dms/image/C4E03AQFuTxjPrR1PzQ/profile-displayphoto-shrink_800_800/0/1613687992487?e=2147483647&v=beta&t=JIgCaDdmQjdRMFMtZbmeND6XRXsc3rzHhR57rWEkmfQ', 4);
INSERT INTO images (id, image_path, user_id) VALUES 
(DEFAULT, 'https://d2ph5fj80uercy.cloudfront.net/06/cat2630.jpg',5);

INSERT INTO post_images (post_id, image_id) VALUES (1, 1);
INSERT INTO post_images (post_id, image_id) VALUES (1, 2);
INSERT INTO post_images (post_id, image_id) VALUES (2, 3);
INSERT INTO post_images (post_id, image_id) VALUES (3, 4);
INSERT INTO post_images (post_id, image_id) VALUES (4, 5);
INSERT INTO post_images (post_id, image_id) VALUES (5, 6);

INSERT INTO group_posts (group_id, post_id) VALUES (1, 1);

INSERT INTO user_groups (user_id, group_id) VALUES (3, 1);