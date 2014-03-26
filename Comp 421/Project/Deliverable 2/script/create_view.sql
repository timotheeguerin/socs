CREATE VIEW recipe_dessert AS SELECT * FROM recipe WHERE type_id = 2;

CREATE VIEW easy_recipe AS SELECT * FROM recipe WHERE point_required < 2 AND (SELECT COUNT(ingredient_id) FROM recipe_ingredient WHERE recipe_id = id) <=  3;

-- Select the dessert with a rating of at least 10
SELECT `name` FROM recipe_dessert WHERE rating >= 10;


-- Select the easy recipes made by user 1
SELECT `name` FROM easy_recipe WHERE user_id = 1;