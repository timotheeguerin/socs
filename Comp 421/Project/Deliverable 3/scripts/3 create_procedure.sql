DROP PROCEDURE IF EXISTS prc_calculate_recommendation;

DELIMITER $$ 
CREATE PROCEDURE prc_calculate_recommendation(IN vuser_id INT) 	
BEGIN
	DECLARE vlevel_id INT;
	SET vlevel_id = (SELECT level_id FROM user WHERE id = vuser_id);

	SET SQL_SAFE_UPDATES=0;
		DELETE FROM user_recommended_recipe WHERE user_id = vuser_id;
		SET SQL_SAFE_UPDATES=1;
		INSERT INTO user_recommended_recipe (user_id, recipe_id) 
		(
			SELECT USER_ID, r.id FROM recipe r 
			JOIN recipe_ingredient ri ON ri.recipe_id = r.id
			WHERE ri.ingredient_id in (SELECT ui.ingredient_id FROM user_like_ingredient ui WHERE ui.user_id = vuser_id) AND point_required <= vlevel_id
			GROUP BY r.id
			ORDER BY COUNT(r.id) DESC
			LIMIT 3
		);
END$$ 
DELIMITER ; 

CALL prc_calculate_recommendation(1);

INSERT INTO user_recommended_recipe (user_id, recipe_id) 
	(
SELECT 1, r.id FROM recipe r 
		JOIN recipe_ingredient ri ON ri.recipe_id = r.id
		WHERE ri.ingredient_id in (SELECT ui.ingredient_id FROM user_like_ingredient ui WHERE ui.user_id = 1) AND point_required <= 5
		GROUP BY r.id
		ORDER BY COUNT(r.id) DESC
		LIMIT 3
);
