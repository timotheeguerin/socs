-- Update the level of a user by getting the logarithm of the amount of recipes he made
UPDATE user 
SET level_id = FLOOR(LN((SELECT COUNT(r.id) FROM recipe r WHERE r.user_id = id))) + 1
WHERE id = 1;

-- Upate recipe rating by getting the sum of its rating
UPDATE recipe SET rating = (SELECT SUM(ur.rating) FROM user_recipe_rating ur WHERE ur.recipe_id = id) WHERE id = 1;

SET SQL_SAFE_UPDATES=0;
-- delete all the recipe a user made.
DELETE FROM recipe WHERE user_id = 7 AND rating < -100;

-- delte all recipe with rating with very bad rating
DELETE FROM recipe_ingredient WHERE ingredient_id in (SELECT id FROM ingredient WHERE `name`='weed');
