DROP PROCEDURE IF EXISTS prc_calculate_recommendation;

DELIMITER $$ 
CREATE PROCEDURE prc_calculate_recommendation(IN vguidemode INT) 	
BEGIN
	DECLARE done INT DEFAULT FALSE;
	DECLARE vuser_id INT;
	DECLARE vlevel_id INT;

	DECLARE cur CURSOR FOR (SELECT id, level_id FROM user WHERE guidemode = vguidemode);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	
	OPEN cur;
	read_loop: LOOP
		FETCH cur INTO vuser_id, vlevel_id;
		IF done THEN
		  LEAVE read_loop;
		END IF;
		SET SQL_SAFE_UPDATES=0;
			DELETE FROM user_recommended_recipe WHERE user_id = vuser_id;
			SET SQL_SAFE_UPDATES=1;
			INSERT INTO user_recommended_recipe (user_id, recipe_id) 
			(
				SELECT DISTINCT vuser_id, r.id FROM recipe r 
				JOIN recipe_ingredient ri ON ri.recipe_id = r.id
				WHERE ri.ingredient_id in (SELECT ui.ingredient_id FROM user_like_ingredient ui WHERE ui.user_id = vuser_id) AND point_required <= vlevel_id
				GROUP BY r.id
				ORDER BY COUNT(r.id) DESC
				LIMIT 3
			);
	END LOOP;
END$$ 
DELIMITER ; 

CALL prc_calculate_recommendation(0);

INSERT INTO user_recommended_recipe (user_id, recipe_id) 
	(
SELECT 1, r.id FROM recipe r 
		JOIN recipe_ingredient ri ON ri.recipe_id = r.id
		WHERE ri.ingredient_id in (SELECT ui.ingredient_id FROM user_like_ingredient ui WHERE ui.user_id = 1) AND point_required <= 5
		GROUP BY r.id
		ORDER BY COUNT(r.id) DESC
		LIMIT 3
);
