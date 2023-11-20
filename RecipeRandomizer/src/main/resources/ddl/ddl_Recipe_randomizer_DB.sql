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
    last_login_date DATE,
      constraint pk_id primary key (id)
);
|

create table recipes
(
    id INT not null AUTO_INCREMENT,
    common_recipe_id int,
    name nVARCHAR(200) not null,
    user_id int not null,
    meal_category_id int not null,
    regional_cuisine_id int,
    cooking_time int,
    portions int,
    calories int,
    image_data mediumblob,
    recipe_text nvarchar(5000) not null,
      constraint pk_id primary key (id),
      constraint fk_user_id foreign key (user_id) references users(id)
      on delete cascade on update cascade,
      constraint fk_meal_category_id foreign key (meal_category_id) references meal_categories(id)
      on delete cascade on update cascade,
      constraint fk_regional_cuisine_id foreign key (regional_cuisine_id) references regional_cuisines(id)
      on delete set null on update cascade,
      constraint fk_common_recipe_id foreign key (common_recipe_id) references recipes(id)
      on delete set null on update cascade
);
|

create index recipe_name on recipes(name);
create index meal_category_in_recipes on recipes(meal_category_id);
create index regional_cuisine_in_recipes on recipes(regional_cuisine_id);
create index cooking_time_in_recipes on recipes(cooking_time);
create index user_id_in_recipes on recipes(user_id);
create index common_recipe_id on recipes(common_recipe_id);
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