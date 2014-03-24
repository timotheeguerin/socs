DROP TRIGGER IF EXISTS update_recipe_rating_ai;
DROP TRIGGER IF EXISTS update_recipe_rating_au;

-- Recalcutate recipe reating after user rated the recipe
DELIMITER $$
CREATE TRIGGER update_recipe_rating_ai AFTER INSERT
ON user_recipe_rating
FOR EACH ROW
BEGIN
	UPDATE recipe r
	SET r.rating = (SELECT SUM(ur.rating) FROM user_recipe_rating ur WHERE ur.recipe_id = NEW.recipe_id) WHERE r.id = NEW.recipe_id;
END$$;
DELIMITER ;

DELIMITER $$
CREATE TRIGGER update_recipe_rating_au AFTER UPDATE
ON user_recipe_rating
FOR EACH ROW
BEGIN
	UPDATE recipe r
	SET r.rating = (SELECT SUM(ur.rating) FROM user_recipe_rating ur WHERE ur.recipe_id = NEW.recipe_id) WHERE r.id = NEW.recipe_id;
END$$;
DELIMITER ;