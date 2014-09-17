-- Level
CREATE TABLE `level`
(
	`id` INT NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	`point_required` INT DEFAULT 0,
	PRIMARY KEY (`id`)
);


-- User
CREATE TABLE `user`
(
	`id` INT NOT NULL,
	`email` VARCHAR(45) NOT NULL,
	`password` VARCHAR(45) NOT NULL,
	`guidemode` TINYINT DEFAULT 0,
	`level_id` INT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`level_id`) REFERENCES `level`(`id`)
);


-- Equipement
CREATE TABLE `equipement`
(
	`id` INT NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	PRIMARY KEY (`id`)
);


-- Receipe type
CREATE TABLE `recipe_type`
(
	`id` INT NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	PRIMARY KEY (`id`)
);

-- Ingredient
CREATE TABLE `ingredient`
(
	`id` INT NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	`isbasic` TINYINT DEFAULT 0,
	PRIMARY KEY (`id`)
);

-- Unit
CREATE TABLE `unit`
(
	`id` INT NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	`symbol` VARCHAR(5) NOT NULL,
	PRIMARY KEY (`id`)
);

-- Unit conversion
CREATE TABLE `unit_conversion`
(
	`id` INT NOT NULL,
	`from_id` INT NOT NULL,
	`to_id` INT NOT NULL,
	`a` FLOAT NOT NULL,
	`b` FLOAT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`from_id`) REFERENCES `unit`(`id`),
	FOREIGN KEY (`to_id`) REFERENCES `unit`(`id`)
);


-- Recipe ingredient
CREATE TABLE `recipe`
(
	`id` INT NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`preparation_time` TIME NOT NULL,
	`cooking_time` TIME NOT NULL,
	`preparation` TEXT NOT NULL,
	`rating` INT NOT NULL,
	`point_required` INT NOT NULL,
	`type_id` INT NOT NULL,
	`user_id` INT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`type_id`) REFERENCES `recipe_type`(`id`),
	FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

-- Recipe ingredient
CREATE TABLE `recipe_ingredient`
(
	`quantity` FLOAT NOT NULL,
	`recipe_id` INT NOT NULL,
	`unit_id` INT NOT NULL,
	`ingredient_id` INT NOT NULL,
	PRIMARY KEY (`recipe_id`, `ingredient_id`),
	FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient`(`id`),
	FOREIGN KEY (`unit_id`) REFERENCES `unit`(`id`),
	FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`)
);



-- Recipe ingredient
CREATE TABLE `user_recipe_rating`
(
	`rating` INT NOT NULL,
	`user_id` INT NOT NULL,
	`recipe_id` INT NOT NULL,
	FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
	FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`)
);

-- User equipement
CREATE TABLE `user_equipement`
(
	`user_id` INT NOT NULL,
	`equipement_id` INT NOT NULL,
	PRIMARY KEY (`user_id`, `equipement_id`),
	FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
	FOREIGN KEY (`equipement_id`) REFERENCES `equipement`(`id`)
);

-- User recommmened recipe
CREATE TABLE `user_recommened_recipe`
(
	`user_id` INT NOT NULL,
	`recipe_id` INT NOT NULL,
	PRIMARY KEY (`user_id`, `recipe_id`),
	FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
	FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`)
);

-- User like ingredent
CREATE TABLE `user_like_ingredient`
(
	`user_id` INT NOT NULL,
	`ingredient_id` INT NOT NULL,
	PRIMARY KEY (`user_id`, `ingredient_id`),
	FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
	FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient`(`id`)
);


-- User equipement
CREATE TABLE `recipe_equipement`
(
	`recipe_id` INT NOT NULL,
	`equipement_id` INT NOT NULL,
	PRIMARY KEY (`recipe_id`, `equipement_id`),
	FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`),
	FOREIGN KEY (`equipement_id`) REFERENCES `equipement`(`id`)
);

