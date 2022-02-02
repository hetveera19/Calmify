/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

#
# SQL Script to Create and Populate the Articles Table
# in the Calmify Database (CalmifyDB)
# Created by Anshika Tyagi, Anubhav Nanda and Het Veera
# ---------------------------------------------------

DROP TABLE IF EXISTS UserPhoto, Comment, Blog, Cart, ItemDetails, OrderDetails, ShopItems, User;

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(256) NOT NULL,          /* To store Salted and Hashed Password Parts */
    first_name VARCHAR(32) NOT NULL,
    middle_name VARCHAR(32),
    last_name VARCHAR(32) NOT NULL,
    address1 VARCHAR(128) NOT NULL,
    address2 VARCHAR(128),
    city VARCHAR(64) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,            /* e.g., 24060-1804 */
    security_question_number INT NOT NULL,   /* Refers to the number of the selected security question */
    security_answer VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,
    subscribe boolean default '1',
    PRIMARY KEY (id)
);

/* The UserPhoto table contains attributes of interest of a user's photo. */
CREATE TABLE UserPhoto
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Blog
(
    id               int unsigned NOT NULL auto_increment,
    publication_date date NOT NULL,
    title            varchar(255) NOT NULL,
    summary          text NOT NULL,
    content          mediumtext NOT NULL,
    user_id          INT UNSIGNED,
    extension ENUM('jpeg', 'jpg', 'png', 'gif'),
    published boolean,
    PRIMARY KEY     (id),
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Comment
(
    id               int unsigned NOT NULL auto_increment,
    blog_id          int unsigned NOT NULL,
    publication_date date NOT NULL,
    content          text NOT NULL,
    user_id          INT UNSIGNED,
    rating           FLOAT UNSIGNED,
    PRIMARY KEY     (id),
    FOREIGN KEY (blog_id) REFERENCES Blog(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

create table ShopItems
(
    id                int unsigned auto_increment primary key,
    name              varchar(256)     not null,
    description       varchar(2048)    not null,
    category          varchar(64)      not null,
    image_url         varchar(256)     null,
    price             double default 0 not null,
    short_description varchar(128)     not null
);

create index user_id
    on Orders (user_id);


create table Cart
(
    id      int unsigned auto_increment primary key,
    user_id int unsigned not null,
    item_id int unsigned not null,
    quantity int unsigned not null default 1,
    foreign key (user_id) references User (id) on delete cascade,
    foreign key (item_id) references ShopItems (Id)  on delete cascade
);

create index item_id
    on Cart (item_id);

create index user_id
    on Cart (user_id);

create table OrderDetails
(
    Id          int unsigned auto_increment primary key,
    total_price double default 0 not null,
    user_id     int unsigned     not null,
    order_date  date             null,
    foreign key (user_id) references User (id)
        on delete cascade
);

create index user_id
    on OrderDetails (user_id);


create table ItemDetails
(
    Id                int unsigned auto_increment primary key,
    Name              varchar(256)     not null,
    description       varchar(2048)    not null,
    category          varchar(64)      not null,
    image_url         varchar(256)     null,
    price             double default 0 not null,
    short_description varchar(1024)    not null,
    order_id          int unsigned     not null,
    item_id           int unsigned     not null,
    quantity          int              not null,
    foreign key (order_id) references OrderDetails (Id)
        on delete cascade,
    foreign key (item_id) references ShopItems (Id)
        on delete cascade
);

create index item_id
    on ItemDetails (item_id);

create index order_id
    on ItemDetails (order_id);


INSERT INTO `ShopItems`
(`Name`,
 `description`,
 `category`,
 `image_url`,
 `price`,
 `short_description`)
VALUES
    ('Relax B8tch Gift Box','    Relax B*tch Gift Box, Stress Relief Gift Basket, Anxiety Gift, Stress Care Package, Nurse Care Package, Relaxation Gift -Bitch-W','Self-Care','https://i.etsystatic.com/23453173/r/il/1cab5e/2794257864/il_794xN.2794257864_1gyl.jpg',21.9,'Relax B*tch - Selft-Care gift Box'),
    ('Calm Balm- Stress Relief','Calm Balm- Stress Relief- Anti-anxiety- Relaxation- Sleep Aid- Relieves Restlessness- All natural- Beeswax Balm- Healing Salve-Travel Size','Anxiety Relievers','https://i.etsystatic.com/23134617/r/il/19633e/3312500920/il_794xN.3312500920_lzfq.jpg','5.95','Calm Balm- Stress Relief- Anti-anxiety'),
    ('Self Care Pokemon Stickers','Self Care Pokemon Stickers | Cyndaquil Mudkip Ditto Snorlax Mareep Magikarp Lugia Charizard Mew Totodile. Self care themed Pokemon stickers! Made of weatherproof vinyl, Glossy Finish','Stickers','https://i.etsystatic.com/31882122/r/il/2d308e/3461463594/il_1140xN.3461463594_bwdu.jpg',2,'Self Care Pokemon Stickers | Cyndaquil Mudkip Ditto Snorlax'),
    ('Stoner sticker','Self care stoner sticker. Waterproof and Weatherproof vinyl. Something different in its league','Stickers','https://i.etsystatic.com/23960746/r/il/83addc/2952469272/il_794xN.2952469272_6mf3.jpg',3,'Self care stoner sticker'),
    ('Health Sticker','Mental Health Sticker Set: Laptop Stickers, Self Care Kit. Smudge Resistant -- the long-lasting laptop sticker version','Stickers','https://i.etsystatic.com/7916159/r/il/fc74be/3472088115/il_794xN.3472088115_sq9r.jpg',8.99,'Mental Health Sticker Set: Laptop Stickers'),
    ('Attitude stickers','My Anxiety Is Chronic But This Ass Is Iconic | Peach Sticker | anxiety sticker | Squats Sticker | Workout Sticker | Sassy Peach','Stickers','https://i.etsystatic.com/21904674/r/il/8253c6/2840178078/il_794xN.2840178078_7bkl.jpg',4,'Anxiety sticker | Squats Sticker'),
    ('Relax Stickers','Self Care Bunny Stickers- UPDATED! (7th Sticker available!)','Stickers','https://i.etsystatic.com/18022829/r/il/8d967d/2470501913/il_794xN.2470501913_s7kb.jpg',3,'Self Care Bunny'),
    ('Crystal Fidget','Crystal Fidget Spinner Ring Anxiety Thumb Ring ADHD gift Stacklable Rings Fidget Jewelry Stress Ring Handmade Crystal Jewelry','Jewelry','https://i.etsystatic.com/27023832/r/il/f6198d/3397477104/il_794xN.3397477104_49c7.jpg',10.08,'Crystal Fidget Spinner Ring'),
    ('Anxiety Necklace','Fuck Anxiety Necklace, Morse Code Jewelry, Anxiety Awareness Necklace, Profanity Jewelry, Sterling Silver, Gold Filled','Jewelry','https://i.etsystatic.com/19152875/r/il/18c4c1/2360859280/il_794xN.2360859280_flr8.jpg',44,'Fuck Anxiety Necklace'),
    ('Spinner Ring','Spinner Ring Anxiety, Anxiety Ring, Meditation Ring, Anti Anxiety Ring, Gift For Her','Jewelry','https://i.etsystatic.com/30049877/r/il/195014/3228739314/il_794xN.3228739314_7vgl.jpg',4,'Spinner Ring Anxiety'),
    ('Stress Straws','Stress Straws 3-Pack Jewelry Gift Set-Bamboo Calming Necklace- Mindfulness Deep Breathing Tool For All-Natural Stress & Anxiety Relief','Accessories','https://i.etsystatic.com/25810405/r/il/8edf06/2803747184/il_794xN.2803747184_1a8w.jpg',14.99,'Stress Straws 3-Pack Jewelry Gift Set'),
    ('Anxiety Whistle','Anxiety Whistle Necklace | Mindfulness Deep Breathing Tool | Natural Anxiety Relief | Meditation Jewelry | Stress Relief Pendant','Accessories','https://i.etsystatic.com/27309661/r/il/d24bcf/3189388502/il_794xN.3189388502_nr9a.jpg',49.99,'Anxiety Whistle Necklace |'),
    ('Pets (Stress balls)','Anxiety Pets (Stress Balls) Desk Buddy. These These adorable weighted soft squishy plushes are perfect for fighting anxiety and stress. Good for sensory and autism as well.','Toys','https://i.etsystatic.com/26460823/r/il/f9f0cd/2799545050/il_794xN.2799545050_3i93.jpg',8,'Anxiety Pets (Stress Balls)'),
    ('Scented Slime','Bye Bye Anxiety, Therapy Dough, Scented Butter Slime, Aromatherapy Anxiety Relief Gift, Stress Relief Popular Slime Shops, Slime Fantasies','Relievers','https://i.etsystatic.com/16946905/r/il/0f622e/3356126314/il_794xN.3356126314_kfpb.jpg',9.99,'Therapy Dough, Scented Butter Slime'),
    ('Self-care guide','70 Acts of Self-Care Mini-Zine, Reference Guide, Self Compassion Gift, Self Care Guide. If you find meeting your self-care needs is something you struggle to fit in.','Books','https://i.etsystatic.com/17926815/r/il/8cacac/2691284779/il_794xN.2691284779_2qoz.jpg',2.95,'70 Acts of Self-Care Mini-Zine'),
    ('Shower Steamers','Shower Steamers 4-Pack, Peppermint Rosemary Sinus Relief, Aromatherapy Gift, Gift Under 10, Thank You Gift, Self Care Gift, Gift for Travel','Relievers','https://i.etsystatic.com/9823194/r/il/c4a0a0/3106359428/il_794xN.3106359428_pxum.jpg',10,'Shower Steamers 4-Pack'),
    ('Self care book of witch','The Witchs Book of Self-Care: Magical Ways to Pamper, Soothe, and Care for Your Body and Spirit','Books','https://i.etsystatic.com/14593719/r/il/5815d2/2447668945/il_794xN.2447668945_3905.jpg',18.99,'The Witchs Book of Self-Care'),
    ('Morning Mindset mug','The Morning MINDSET Mug - Mental Health, Law of Attraction, Motivational Mug, Self Care, Manifest, Affirmation, Wellbeing, Mindset','Cutlery','https://i.etsystatic.com/23623656/r/il/4c5b28/2910247744/il_794xN.2910247744_a6z6.jpg',15.16,'The Morning MINDSET Mug - Mental Health'),
    ('Bath bundle','Hygge Holiday Bath Bundle (Sustainable Spa Basket/ Gift for Her/ Care Package/ Anniversary/ Self Care/Fall Gift Basket) Personalization','Relievers','https://i.etsystatic.com/25481126/r/il/067955/3483225438/il_794xN.3483225438_abm6.jpg',29,'Hygge Holiday Bath Bundle'),
    ('Mindful Cards','LSW Mind Cards - Daily cards for a more fulfilling life | Increase wellbeing & boost your mood | gratitude | stocking filler | self gift','Books','https://i.etsystatic.com/18853949/r/il/ee06d1/3268906236/il_794xN.3268906236_o324.jpg',13.83,'LSW Mind Cards - Daily cards for a more fulfilling life'),
    ('Self care kit','Self Care Kit, Self Care Package, Bath Salts, Sugar Scrub, Whipped Body Butter, Self Care Box, Self Care Gift Box','Relievers','https://i.etsystatic.com/17417983/r/il/ef5e81/2583213473/il_794xN.2583213473_lyf3.jpg',35.99,'Self Care Kit, Self Care Package'),
    ('Self care T-Shirt','Self-Care Sweatshirt, Self-Love Sweatshirt, Vintage Sweatshirt, Aesthetic Sweatshirt, Boho Sweatshirt, Self Care Gift, Gift for Minimalist','Clothing','https://i.etsystatic.com/22019258/r/il/a18596/3185837422/il_794xN.3185837422_kq4j.jpg',22,'Self-Care Sweatshirt, Self-Love Sweatshirt'),
    ('Mindful tasks','Mindfulness Gift: Rustic jar full of daily mindful tasks and challenges','Activities','https://m.media-amazon.com/images/I/71zKr+0D90L._AC_SL1500_.jpg',16.95,'jar full of daily mindful tasks'),
    ('Mindful Balls','MindPanda 3X Empowering Gel Stress Ball Bundle | Different Strengths & Sizes for Squeeze Hand Therapy - Scented for Extra Focus','Accessories','https://m.media-amazon.com/images/I/71t6tx+iGsS._AC_SL1500_.jpg',22.99,'MindPanda 3X Empowering Gel Stress Ball Bundle'),
    ('Joycuffs','Joycuff Bracelets for Women Personalized Inspirational Jewelry Mantra Cuff Bangle Friend Encouragement Gift for Her','Accessories','https://m.media-amazon.com/images/I/71S6riLqCbL._AC_SX480_SY360_.jpg',12.72,'Joycuff Bracelets for Women'),
    ('Yoga Tee','Let That Sht Go | Funny Zen Buddha Yoga Mindfulness Peace Hippy Women T-Shirt','Clothing','https://m.media-amazon.com/images/I/81XMSQiYZ6L._AC_UY550_.jpg',18.95,'Funny Zen Buddha Yoga Mindfulness'),
    ('Yoga Hat','Ann Arbor T-shirt Co. Let That Sht Go | Funny Zen Buddha Yoga Mindfulness Peace Hippy Women Men Baseball Cap Dad Hat','Clothing','https://m.media-amazon.com/images/I/71Idf3ItxGL._AC_UX679_.jpg',19.95,'Funny Zen Buddha Yoga Mindfulness Peace Hippy Women Men Baseball Cap'),
    ('Mindful chocolate','Chocolate makes you smile: a sweet lesson in mindfulness and meditation','Food','https://m.media-amazon.com/images/I/71uNkMug8RL.jpg',12.50,'Chocolate makes you smile'),
    ('NOW system','n.o.w. Tone Therapy System | Its Yoga for Your Mind','Accessories','https://m.media-amazon.com/images/I/71fYweeuI+L._AC_SL1500_.jpg',79,'Tone Therapy System'),
    ('Therapy Diffuser','Pure daily care Ultimate Aromatherapy Diffuser& Essential Oil Set - Ultrasonic Diffuser&Top 10 Essential Oils - 400ml Diffuser with 4 Timer & 7 Ambient Light Settings - Therapeutic Grade - Lavender','Relievers','https://m.media-amazon.com/images/I/71nZ9I+OmbL._AC_SL1500_.jpg',39.95,'Pure daily care Ultimate Aromatherapy Diffuser');
