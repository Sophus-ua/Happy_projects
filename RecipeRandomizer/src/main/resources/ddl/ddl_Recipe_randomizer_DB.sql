delimiter |
drop database if exists Recipe_randomizer;
|

create database Recipe_randomizer;
|

use Recipe_randomizer;
|

create table meal_categories
(
    id int not null AUTO_INCREMENT,
    name nVARCHAR(30) not null,
    constraint pk_id primary key (id)
);
|

create table regional_cuisines
(
    id int not null AUTO_INCREMENT,
    name nVARCHAR(30) not null,
      constraint pk_id primary key (id)
);
|

create table users
(
    id int not null AUTO_INCREMENT,
    username nvarchar(100) not null unique COLLATE utf8_general_ci,
    password nvarchar(255) not null,
    own_name nvarchar(100) not null,
    role ENUM('ADMIN', 'MODERATOR', 'USER') not null,
    enabled TINYINT(1) not null DEFAULT 1,
    registration_date DATE,
      constraint pk_id primary key (id)
);
|

create table recipes
(
    id INT not null AUTO_INCREMENT,
    name nVARCHAR(100) not null,
    user_id int not null,
    meal_category_id int not null,
    regional_cuisine_id int,
    cooking_time int,
    portions int,
    calories int,
    image_data mediumblob,
    recipe_text nvarchar(3000) not null,
      constraint pk_id primary key (id),
      constraint fk_user_id foreign key (user_id) references users(id)
      on delete cascade on update cascade,
      constraint fk_meal_category_id foreign key (meal_category_id) references meal_categories(id)
      on delete cascade on update cascade,
      constraint fk_regional_cuisine_id foreign key (regional_cuisine_id) references regional_cuisines(id)
      on delete set null on update cascade
);
|

create index recipe_name on recipes(name);
create index meal_category_in_recipes on recipes(meal_category_id);
create index regional_cuisine_in_recipes on recipes(regional_cuisine_id);
create index cooking_time_in_recipes on recipes(cooking_time);
create index user_id_in_recipes on recipes(user_id);
|

create table dishes_by_ingredients
(
    id INT not null AUTO_INCREMENT,
    name nVARCHAR(50) not null,
      constraint pk_id primary key (id)
);
|

create table common_allergens
(
    id INT not null AUTO_INCREMENT,
    name nVARCHAR(50) not null,
      constraint pk_id primary key (id)
);
|

create table custom_tags
(
    id INT not null AUTO_INCREMENT,
    name nVARCHAR(50) not null,
    user_id int not null,
      constraint pk_id primary key (id),
      constraint fk_user_id_in_custom_tags foreign key (user_id) references users(id)
      on delete cascade on update cascade
);
|

create table image_buffer
(
    image_key nVARCHAR(100) not null,
    user_id int not null,
    image_data mediumblob not null,
      constraint pk_embedded_id primary key (image_key, user_id),
      constraint fk_user_id_in_image_buffer foreign key (user_id) references users(id)
      on delete cascade on update cascade
);
|

create table recipes_dishes_by_ingredients
(
    recipe_id int not null,
    dish_by_ingredients_id int not null,
      constraint pk_embedded_id primary key (recipe_id, dish_by_ingredients_id),
      constraint fk_recipe_in_ingredients foreign key (recipe_id) references recipes(id)
      on delete cascade on update cascade,
      constraint fk_dish_by_ingredients foreign key (dish_by_ingredients_id) references dishes_by_ingredients(id)
      on delete cascade on update cascade
);


create table recipes_common_allergens
(
    recipe_id int not null,
    allergen_id int not null,
      constraint pk_embedded_id primary key (recipe_id, allergen_id),
      constraint fk_recipe_in_allergens foreign key (recipe_id) references recipes(id)
      on delete cascade on update cascade,
      constraint fk_allergen foreign key (allergen_id) references common_allergens(id)
      on delete cascade on update cascade
);


create table recipes_custom_tags
(
    recipe_id int not null,
    custom_tag_id int not null,
      constraint pk_embedded_id primary key (recipe_id, custom_tag_id),
      constraint fk_recipe_in_custom_tags foreign key (recipe_id) references recipes(id)
      on delete cascade on update cascade,
      constraint fk_custom_tag foreign key (custom_tag_id) references custom_tags(id)
      on delete cascade on update cascade
);
|












insert into meal_categories
(name)
values
('сніданки'),
('холодні закуски'),
('салати, боули'),
('супи'),
('другі страви'),
('випічка'),
('соуси'),
('напої (б/а)'),
('коктейлі (а)');


insert into regional_cuisines
(name)
values
('європейська'),
('середземноморська'),
('балканська'),
('саамська'),
('кавказька'),
('азійська'),
('арабська');


insert into dishes_by_ingredients
(name)
values
('вегетаріанські'),
('гриби'),
('морепродукти'),
('пташине м`ясо'),
('свинина'),
('яловичина');


insert into common_allergens
(name)
values
('глютен'),
('риба'),
('молоко коров`яче'),
('горіхи'),
('арахіс і люпин'),
('кунжут'),
('яйця'),
('ракоподібні'),
('селера'),
('молюски'),
('гірчиця'),
('cоя'),
('сульфіти');
|


insert into users
(username, password, own_name, role, enabled, registration_date)
values
('Admin', 'qwe', 'Олег', 'ADMIN', 1 ,'2023-11-12'),
('Moderator', 'qwe', 'Олег', 'MODERATOR', 1 , '2023-11-12'),
('Anna', '123', 'Аня', 'USER', 1 , '2023-11-12'),
('Oleh', '456', 'Олег', 'USER', 1 , '2023-11-14');

insert into custom_tags
(name, user_id)
values
('швидкий перекус', 3),
('святкові', 3),
('в дорогу', 3),
('на пікнік', 3);

insert into recipes
(name, user_id, meal_category_id, regional_cuisine_id, cooking_time, portions, calories, image_data, recipe_text)
values
('Яйця', 3, 1, 5, 30, 1, 100, null, 'варимо'),
('Риба', 3, 2 , 4, 60, 2, 150, null, 'варимо '),
('М`ясо', 3, 3 , 3, 120, 3, 200, null, 'варимо '),
('Суп', 3, 4, 2, 20, 1, 300, null, 'варимо '),
('Ковбаса', 3, 5 , 1, 15, 21, 250, null, 'варимо '),
('Картопля', 3, 6 , 5, 50, 3, 1170, null, 'варимо '),
('Кампот', 3, 1 , 4, 110, 1, 520, null, 'варимо '),
('Шашлики', 3, 7 , 4, 110, 1, 520, null, 'варимо ');

insert into recipes_dishes_by_ingredients
(recipe_id, dish_by_ingredients_id)
values
(1,6),
(2,5),
(3,4),
(4,3),
(5,2),
(6,1);

insert into recipes_common_allergens
(recipe_id, allergen_id)
values
(1,13),
(2,12),
(3,11),
(4,10),
(5,9),
(6,8),
(7,7),
(6,6),
(5,5),
(4,4),
(3,3),
(2,2),
(1,1);

insert into recipes_custom_tags
(recipe_id, custom_tag_id)
values
(8,1),
(7,1),
(7,2),
(7,4),
(8,3),
(5,4),
(6,2);
|

Update recipes
set recipe_text = 'Бріош — це французька булочка зі здобного дріжджового тіста. Вирізняє її надзвичайно ніжна пухка текстура та благородна історія, що сягає аж 17 ст. Цілком імовірно, що сама Марія Антуанетта та король Людовик ласували бріошами. Хороші новини, ви абсолютно точно можете приготувати бріоші вдома. З тістом треба трохи повозитися, але ми ж не боїмося викликів?  До того ж кожна хвилина зусиль варта цього вишуканого смаку. На відмінну від профітролів з заварним кремом, бріош начиняти не потрібно, просто викладіть ложкою заварний крем на скибку та насолоджуйтеся!

ТОНКОЩІ ПРИГОТУВАННЯ БУЛОЧОК БРІОШ ІЗ ЗАВАРНИМ КРЕМОМ
Коротко згадаймо правила роботи з дріжджовим тістом. Розводити дріжджі треба в теплому, молоці, оптимальна температура 35-36 градусів. Якщо немає термометра, просто крапніть молоко на тильну сторону долоні: має бути тепло, але не гаряче. Перевірте пакування дріжджів, вони повинні бути свіжі.

Тісто ставте підійматися у тепле місце без протягів та різких коливань температури. Якщо в дома холодно — увімкніть духовку, прочиніть дверцята та поставте миску з тістом поруч.

Ми використовували 2 прямокутні металеві форми, розміром 24 на 11 см.

З такої кількості інгредієнтів вийшло 2 бріоші.

ЯК ПРИГОТУВАТИ БУЛОЧКУ БРІОШ
ІНГРЕДІЄНТИ
Для булочок
120 мл молока
12 г сухих дріжджів
100 г цукру
180 г масла
6 яєць
600 г борошна
½ ч.л. солі
1 жовток
Для заварного крему
500 мл молока
100 г цукру
50 г вершкового масла
4 ст. л. пшеничного борошна
½ ч. л. ванільного цукру
1 яйце'
where id = 1;
|




delete from meal_categories;
ALTER TABLE meal_categories AUTO_INCREMENT=1;

delete from regional_cuisines;
ALTER TABLE regional_cuisines AUTO_INCREMENT=1;

delete from recipes;
ALTER TABLE recipes AUTO_INCREMENT=1;

delete from dishes_by_ingredients;
ALTER TABLE dishes_by_ingredients AUTO_INCREMENT=1;

delete from common_allergens;
ALTER TABLE common_allergens AUTO_INCREMENT=1;

delete from custom_tags;
ALTER TABLE custom_tags AUTO_INCREMENT=1;

delete from users;
ALTER TABLE users AUTO_INCREMENT=1;