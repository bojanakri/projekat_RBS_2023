insert into users(id, username, password)
values (1, 'bruce', 'wayne'),
       (2, 'peter', 'security_rules'),
       (3, 'tom', 'guessmeifyoucan'),
        (4, 'toeLover', 'blood_everywhere');

insert into persons(id, firstName, lastName, email)
values (1, 'bruce', 'wayne', 'notBatman@gmail.com'),
       (2, 'Peter', 'Petigrew', 'oneFingernailFewerToClean@gmail.com'),
       (3, 'Tom', 'Riddle', 'theyGotMyNose@gmail.com'),
        (4, 'Quentin', 'Tarantino', 'qt5@gmail.com');

insert into movies(id, title, description)
values (1, 'Four rooms', 'Following New Years celebration in a hotel in four different perspectives'),
       (2, 'Porodicno blago specijal', 'Stojkovici kupuju prase'),
       (3, 'Conan the Barbarian', 'Original fantasy hero');

insert into genres(id, name)
values (1, 'action'),
       (2, 'adventure'),
       (3, 'soap opera'),
       (4, 'comedy'),
       (5, 'horror');

insert into movies_to_genres(movieId, genreId)
values (1, 4),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 1),
       (3, 2);

insert into ratings(movieId, userId, rating)
values (1, 3, 5),
        (3, 2, 1),
        (3, 1, 3),
        (1, 1, 5),
        (1, 2, 4);

insert into comments(movieId, userId, comment)
values (1, 1, 'There are four rooms. P.S. I am not Batman');

insert into roles(id, name)
values (1, 'ADMIN'),
       (2, 'MANAGER'),
       (3, 'REVIEWER');

insert into user_to_roles(userId, roleId)
values (1, 3),
       (2, 3),
       (3, 1),
       (4, 2);

