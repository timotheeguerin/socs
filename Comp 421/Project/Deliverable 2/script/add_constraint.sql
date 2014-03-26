ALTER TABLE user_recipe_rating 
ADD CHECK (rating = 1 OR rating = -1);

ALTER TABLE recipe_ingredient 
ADD CHECK (quantity > 0);