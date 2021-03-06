--SET MODE PostgreSQL;
INSERT INTO gbc_wall_item(id, file, path, description, date_creation, date_updated, ratio, nb_like, item_type) VALUES
(206, 'bender-futurama-poker-4d8463.jpg', '/web/images/wall', 'Poker time', '2013-11-12 16:57:00.0', null, 1.777778, 0, 'PICTURE'),
(207, 'futurama-572f7e.png', '/web/images/wall', 'le trio', '2013-11-12 17:01:00.0', null, 1.777132, 2, 'PICTURE'),
(208, 'air-bender-617cca.jpg', '/web/images/wall', 'Air bender', '2013-11-22 14:08:00.0', null, 1.333333, 0, 'PICTURE'),
(209, 'Nibbler-futurama-3305545-1024-768-8c783d.jpg', '/web/images/wall', 'Nibbler', '2013-11-22 14:08:00.0', null, 1.333333, 0, 'PICTURE'),
(210, 'img3-22baa9.jpg', '/web/images/wall', 'TV Show screenshot', '2013-11-13 14:08:00.0', null, 1.333333, 0, 'PICTURE'),
(211, 'Futurama-iPod-1024x768-05f116.jpg', '/web/images/wall', 'Ipod, style', '2013-11-22 14:09:00.0', null, 1.333333, 0, 'PICTURE'),
(212, 'Universe-680be1.png', '/web/images/wall', 'universe', '2013-11-22 14:09:00.0', null, 2.420575, 0, 'PICTURE'),
(213, 'img1-598e2e.jpg', '/web/images/wall', 'Affiche', '2013-11-01 14:10:00.0', null, 1.333333, 0, 'PICTURE'),
(214, 'futurama-bfbd77.jpg', '/web/images/wall', 'Encore une affiche', '2013-11-10 14:10:00.0', null, 1.333333, 0, 'PICTURE'),
(215, 'futurama-bender2-b0b52e.jpg', '/web/images/wall', 'Bender switch off', '2013-11-10 14:11:00.0', null, 1.25, 0, 'PICTURE'),
(216, 'im-futurama-b80876.jpg', '/web/images/wall', 'cheers', '2013-11-22 14:11:00.0', null, 1.489489, 0, 'PICTURE'),
(217, '2013-10-Futurama-Wallpaper-HD-d9a892.jpg', '/web/images/wall', 'HD intro', '2013-11-22 14:11:00.0', null, 1.777778, 0, 'PICTURE'),
(218, 'http://www.youtube.com/embed/GLpO-OvJU74', '/web/images/wall', 'on youtube', '2013-10-22 14:11:00.0', null, 1.0, 0, 'VIDEO'),
(219, 'http://player.vimeo.com/video/12915013', '/web/images/wall', 'on vimeo', '2013-10-22 14:14:00.0', null, 1.0, 0, 'VIDEO'),
(220, 'weird-futurama-119266.jpg', '/web/images/wall', 'weird', '2013-10-19 14:49:00.0', null, 1.6, 0, 'PICTURE'),
(221, 'the-simpsons-futurama-crossover-9de538.jpg', '/web/images/wall', 'Simpson cross over', '2013-10-19 14:48:00.0', null, 1.8125, 0, 'PICTURE'),
(222, 'SIMPSONS-FUTURAMA-612x381-d8f865.jpg', '/web/images/wall', 'simpson fight', '2013-10-19 14:49:00.0', null, 1.778351, 0, 'PICTURE'),
(223, 'key-art-futurama-f06d67.jpg', '/web/images/wall', 'key art', '2013-10-19 14:50:00.0', null, 2.571429, 0, 'PICTURE'),
(224, 'futurama-wallpaper-c60817.jpg', '/web/images/wall', 'crash', '2013-10-19 14:50:00.0', null, 1.777778, 0, 'PICTURE'),
(225, 'Futurama-Season-6-0745d1.jpg', '/web/images/wall', 'season 6', '2013-10-19 14:50:00.0', null, 1.6, 0, 'PICTURE'),
(226, 'Futurama-Fry-2a657f.jpg', '/web/images/wall', 'Thinking', '2013-10-19 14:50:00.0', null, 1.332398, 0, 'PICTURE'),
(227, 'futurama-south-park-78f236.jpg', '/web/images/wall', 'South Park', '2013-10-19 14:51:00.0', null, 1.362398, 0, 'PICTURE'),
(228, 'futurama-logo-c3bf57.jpg', '/web/images/wall', 'Logo', '2013-10-19 14:51:00.0', null, 1.333333, 0, 'PICTURE'),
--le plus vieux
(229, 'big3-1cd860.gif', '/web/images/wall', 'Big 3', '2013-10-19 09:51:00.0', null, 1.333333, 0, 'PICTURE'),
--le plus récent
(230, 'bender-futurama-robot-p-753df5.jpg', '/web/images/wall', 'bender', '2015-10-19 14:51:00.0', null, 1.544554, 0, 'PICTURE'),
(231, '730999-robot-futurama-wallpapers-desktop-cartoon-cartoons-wallpaper-p-f6d36e.jpg', '/web/images/wall', 'bender cartoon', '2013-10-19 14:52:00.0', null, 1.777982, 0, 'PICTURE'),
(232, '254638-bender-fry-leela-futurama-futurama-p-6fb68e.jpg', '/web/images/wall', 'slogan', '2013-10-19 14:52:00.0', null, 1.59901, 0, 'PICTURE'),
(233, '508t2zlp-f5d399.jpg', '/web/images/wall', 'Leela', '2013-10-19 14:52:00.0', null, 1.333333, 0, 'PICTURE');


INSERT INTO `gbc_wall_comment` (`id`, `item_id`, `author`, `comment`, `is_approved`, `date_creation`, `date_updated`) VALUES
(1, 230, 'brother', 'Great picture', 1, '2015-12-01 13:08:49', '2013-11-22 13:08:49'),
(2, 230, 'father', 'I prefer this picture', 1, '2015-12-02 23:41:09', '2017-01-20 23:41:09'),
(3, 229, 'father', 'Where was the picture taken?', 1, '2015-12-03 23:41:09', '2017-01-20 23:41:09'),
(4, 229, 'me', 'Jamaica', 1, '2017-01-20 23:41:09', '2015-12-04 23:41:09');
