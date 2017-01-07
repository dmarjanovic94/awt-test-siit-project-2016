-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlDialectInspectionForFile
/* Insert new companies*/
INSERT INTO `companies` (`id`, `address`, `name`, `phone_number`) VALUES ('1', 'Trg Dositeja Obradovića', 'company', '5461963');
INSERT INTO `companies` (`id`, `address`, `name`, `phone_number`) VALUES ('2', 'Bulevar oslobođenja', 'company7', '1122335');
INSERT INTO `companies` (`id`, `address`, `name`, `phone_number`) VALUES ('3', 'Bulevar Despota Stefana', 'company8', '5000963');

/* USED as DEFAULT for TEST purposes */
INSERT INTO `companies` (`id`, `address`, `name`, `phone_number`) VALUES ('4', 'Trg Dositeja Obradovića', 'TEST kompanija', '06000000');
INSERT INTO `companies` (`id`, `address`, `name`, `phone_number`) VALUES ('5', 'Trg Dositeja Obradovića', 'Druga kompanija', '06000000');


/* Insert new users */
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (1, 0, 'isco@gmail.com', 'Isco', 'Alarcon', '$2a$10$iZ.kdcySt1n2BnLIkksLdOSe22jVI4kUYa1OBpZ64QboxruXetmFO', '065066665', 'verifier', 'isco', '1', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`) VALUES (2, 0, 'sr4@gmail.com', 'Sergio', 'Ramos', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065066665', 'verifier', 'sr4');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (3, 0, 'david@gmail.com', 'David', 'Beckham', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065066665', 'oglašavač', 'david', '3', 'pending');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (4, 0, 'bjelica@gmail.com', 'Nemanja', 'Bjelica', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065066765', 'verifikator', 'bjelica', '1', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (5, 0, 'lillard@gmail.com', 'Damian', 'Lillard', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065066635', 'advertiser', 'damian', '1', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`) VALUES (6, 0, 'lebron@gmail.com', 'LeBron', 'James', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065061665', 'advertiser', 'james');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (7, 0, 'wiggins@gmail.com', 'Andrew', 'Wiggins', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065161665', 'verifier', 'wiggins', '3', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (8, 0, 'russ@gmail.com', 'Russell', 'Westbrook', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065161665', 'advertiser', 'russ', '3', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (9, 1, 'davies@gmail.com', 'Antony', 'Davies', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065161665', 'verifier', 'eyebrow', '3', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (10, 1, 'towns@gmail.com', 'Karl Anthony', 'Towns', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '065161665', 'advertiser', 'kat', '3', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`) VALUES (11, 0, 'admin@admin.com', 'admin', 'admin', '$2a$10$iZ.kdcySt1n2BnLIkksLdOSe22jVI4kUYa1OBpZ64QboxruXetmFO', '065161665', 'admin', 'admin');



INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (12, 0, 'test_advertiser@gmail.com', 'Test', 'TAdvertiser', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '060000000', 'advertiser', 'test_advertiser_company_member', '4', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (13, 0, 'test_advertiser_pending@gmail.com', 'Test', 'TAdvertiser', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '060000000', 'advertiser', 'test_advertiser_pending_membership', '4', 'pending');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`) VALUES (14, 0, 'test_verifier_outside@gmail.com', 'Test', 'TVerifier', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '060000000', 'verifier', 'user_outside_company');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (15, 0, 'test_advertiser_other@gmail.com', 'Test', 'TAdvertiser', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '060000000', 'advertiser', 'test_advertiser_other_company_member', '5', 'accepted');
INSERT INTO `users` (`id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `type`, `username`, `company_id`, `company_verified`) VALUES (16, 0, 'test_advertiser_same@gmail.com', 'Test', 'TAdvertiser', '$10$wDeKOR2hyQaoEQSw827CHubLgjV5F2fOeKm.tIu1QDszHHUdr.Rjq', '060000000', 'advertiser', 'test_advertiser_same_company_member', '4', 'accepted');

INSERT INTO `locations` (`id`, `city`, `city_region`, `country`, `street`, `street_number`, `latitude`, `longitude`) VALUES (1, 'Novi Sad', 'Grbavica', 'Srbija', 'Trg Dositeja Obradovica', '15', 45.246740, 19.851894); -- Stimac (SRB)
INSERT INTO `locations` (`id`, `city`, `city_region`, `country`, `street`, `street_number`, `latitude`, `longitude`) VALUES (2, 'Novi Sad', 'Centar', 'Srbija', 'Petra Drapsina', '5', 45.252711, 19.841499);
INSERT INTO `locations` (`id`, `city`, `city_region`, `country`, `street`, `street_number`, `latitude`, `longitude`) VALUES (3, 'Ugljevik', 'Senjak', 'Bosna i Hercegovina', 'Svetog Save', '51', 44.693972, 18.995955); -- Stimac (CZV)

/* Insert new real estates */
INSERT INTO `real_estates` (`id`, `area`, `deleted`, `heating_type`, `type`, `location_id`) VALUES ('1', '220', 0, 'coal', 'flat', 1);
INSERT INTO `real_estates` (`id`, `area`, `deleted`, `heating_type`, `type`, `location_id`) VALUES ('2', '220', 0, 'central_heating', 'house', 2);
INSERT INTO `real_estates` (`id`, `area`, `deleted`, `heating_type`, `type`, `location_id`) VALUES ('3', '240', 1, 'central_heating', 'flat', 2);
INSERT INTO `real_estates` (`id`, `area`, `deleted`, `heating_type`, `type`, `location_id`) VALUES ('4', '240', 0, 'coal', 'flat', 3);

/* Insert new announcements */
INSERT INTO `announcements` (`id`, `date_announced`, `date_modified`, `deleted`, `expiration_date`, `price`, `phone_number`, `type`, `verified`, `author_id`, `real_estate_id`, `name`, `description`) VALUES ('1', '1994-01-01', '1994-01-03', 0, '1994-01-20', '50', '0654887612', 'flat', 'not-verified', '1', '1', 'name1', 'desc1');
INSERT INTO `announcements` (`id`, `date_announced`, `date_modified`, `deleted`, `expiration_date`, `price`, `phone_number`, `type`, `verified`, `author_id`, `real_estate_id`, `name`, `description`) VALUES ('2', '1997-01-01', '1997-01-03', 0, '1997-01-20', '60', '0654887111', 'flat', 'verified', '1', '2', 'name2', 'desc2');
INSERT INTO `announcements` (`id`, `date_announced`, `date_modified`, `deleted`, `expiration_date`, `price`, `phone_number`, `type`, `verified`, `author_id`, `real_estate_id`, `name`, `description`) VALUES ('3', '1994-01-01', '1994-01-03', 1, '1994-01-20', '50', '0654887612', 'flat', 'not-verified', '12', '1', 'name3', 'desc3');
INSERT INTO `announcements` (`id`, `date_announced`, `date_modified`, `deleted`, `expiration_date`, `price`, `phone_number`, `type`, `verified`, `author_id`, `real_estate_id`, `name`, `description`) VALUES ('4', '2017-01-01', '2017-01-01', 0, '2017-01-20', '50', '0654887612', 'flat', 'not-verified', '12', '4', 'name4', 'desc4');

/* Insert new comments*/
INSERT INTO `comments` (`id`, `content`, `date`, `announcement_id`, `author_id`) VALUES ('1', 'Another one comment', '2016-05-05', '2', '5');
INSERT INTO `comments` (`id`, `content`, `date`, `announcement_id`, `author_id`) VALUES ('2', 'Another one comment', '2016-07-15', '2', '7');
INSERT INTO `comments` (`id`, `content`, `date`, `announcement_id`, `author_id`) VALUES ('3', 'Another one comment', '2016-01-25', '2', '6');

/* Insert images */
INSERT INTO `images` (`id`, `image_path`) VALUES ('1', 'slika1.png');
INSERT INTO `images` (`id`, `image_path`) VALUES ('2', 'slika2.png');

INSERT INTO `announcements_images` (`announcement_id`, `images_id`) VALUES ('1', '1');
INSERT INTO `announcements_images` (`announcement_id`, `images_id`) VALUES ('2', '2');

/* Insert new reports*/
INSERT INTO `reports` (`id`,`email`, `content`, `status`, `type`, `created_at`, `announcement_id`, `reporter_id`) VALUES ('1', 'user6@mail.com', 'Inappropriate content', 'active', 'admin', '1994-01-01', '2', '6');

/* Insert new marks*/
INSERT INTO `marks`  VALUES ('1', '4', '2', '1', '3');
INSERT INTO `marks`  VALUES ('2', '5', '2', '1', '4');
INSERT INTO `marks`  VALUES ('3', '3', '2', '1', '3');