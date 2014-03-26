-- Select all recipes that have all the 3 given ingredients
SELECT r.`name` FROM recipe r 
JOIN recipe_ingredient ri ON ri.recipe_id = r.id
JOIN ingredient i ON i.id = ri.ingredient_id
WHERE i.`name` in ('cream', 'bacon', 'pasta') 
GROUP BY r.`id`
HAVING COUNT(r.`id`) >= 3;

-- Select recipe where the user 1 has all the recipe equipement
SELECT `name` FROM recipe WHERE id not in (
	SELECT r.id FROM recipe r  
	JOIN recipe_equipement re ON re.recipe_id = r.id
	WHERE 
	(SELECT COUNT(ue.equipement_id) FROM user_equipement ue WHERE ue.equipement_id = re.equipement_id AND ue.user_id = 1) = 0
);


-- Select all recipes that contains an ingredient the user like and order them by the count of ingreditent the user like found in the recipe 
SELECT r.name FROM recipe r 
JOIN recipe_ingredient ri ON ri.recipe_id = r.id
WHERE ri.ingredient_id in (SELECT ui.ingredient_id FROM user_like_ingredient ui WHERE ui.user_id = 1)
GROUP BY r.id
ORDER BY COUNT(r.id) DESC;

-- Select the most 3 liked ingredient
SELECT i.`name` FROM user_like_ingredient ui
JOIN ingredient i ON i.id = ui.ingredient_id
GROUP BY i.id
ORDER BY COUNT(i.id) DESC
LIMIT 3;

-- Get the top 10 user at the specific level that make the best recipe
-- This is calculated by getting the sum of the rating of all recipe a user as made and dividing it by the square root of its count. 
-- So a user who made 10 recipe with rating 5 will be better ranked that a user who made one recipe with rating 5
SELECT u.email
FROM recipe r 
LEFT JOIN user u ON u.id = r.user_id 
JOIN `level` l ON l.id = u.level_id
WHERE l.name = 'Beginner'
GROUP BY r.user_id
ORDER BY SUM(r.rating)/SQRT(COUNT(r.id)) DESC
LIMIT 10;



