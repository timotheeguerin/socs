/*
SELECT DISTINCT serie_id FROM ass2."Game" WHERE abs(goal1-goal2) >= 4;

SELECT DISTINCT round FROM ass2."Serie" JOIN ass2."Game" ON id = serie_id WHERE ot=1;


SELECT DISTINCT round FROM ass2."Serie" 
EXCEPT SELECT round FROM ass2."Serie" JOIN ass2."Game" ON id = serie_id WHERE ot=1;

SELECT DISTINCT g.serie_id FROM ass2."Game" g
EXCEPT SELECT g1.serie_id 
FROM ass2."Game" as g1 JOIN ass2."Game" as g2 ON g1.serie_id = g2.serie_id
WHERE g1.goal1 != g2.goal1 OR g1.goal2 != g2.goal2;

SELECT serie_id, game, (goal1 + goal2) as total_goals FROM ass2."Game" g1 WHERE  (goal1 + goal2) = 
(SELECT MIN (goal1 + goal2)  FROM ass2."Game" );

SELECT team1, goal1, team2, goal2, game FROM ass2."Serie" JOIN ass2."Game" ON id = serie_id
WHERE team1 = 'Pittsburgh Penguins' OR team2 = 'Pittsburgh Penguins' ORDER BY id, game; 


SELECT team1, goal1, team2, goal2, game FROM ass2."Serie" JOIN ass2."Game" ON id = serie_id
WHERE (team1 = 'Pittsburgh Penguins' OR team2 = 'Pittsburgh Penguins') AND serie_id = (
SELECT MAX(s.id) FROM ass2."Serie" s WHERE (s.team1 = 'Pittsburgh Penguins' OR s.team2 = 'Pittsburgh Penguins'))
ORDER BY id, game; 

SELECT AVG(goal1+goal2) FROM ass2."Game";

SELECT serie_id, COUNT(game) FROM  ass2."Game" GROUP BY serie_id;

SELECT id FROM ass2."Serie"
WHERE (SELECT COUNT(game) FROM  ass2."Game" WHERE id = serie_id ) != 7;

SELECT id FROM ass2."Serie" 
WHERE (team1 = 'Montreal Canadiens' OR team2 = 'Montreal Canadiens' ) 
AND (SELECT COUNT(game) FROM  ass2."Game" WHERE id = serie_id ) != 7;

SELECT id FROM ass2."Serie"
WHERE (SELECT SUM(ot) FROM  ass2."Game" WHERE id = serie_id ) >= 2;

SELECT id FROM ass2."Serie" WHERE
(SELECT COUNT(game) as game_count FROM  ass2."Game" WHERE serie_id = id GROUP BY serie_id) =
(SELECT MAX(game_count) 
FROM (SELECT COUNT(game) as game_count FROM  ass2."Game" GROUP BY serie_id) as getgamecount);


SELECT serie_id, winner, SUM(win) FROM (SELECT serie_id, 'team1'as winner, 0 as win FROM ass2."Game" GROUP BY serie_id UNION 
SELECT serie_id, 'team2'as winner, 0 as win FROM ass2."Game" GROUP BY serie_id UNION
SELECT serie_id, winner , COUNT(winner)as win FROM (
SELECT serie_id, (CASE WHEN goal1 > goal2 THEN 'team1' ELSE 'team2' END) as winner
FROM ass2."Game") AS getall GROUP BY serie_id, winner ORDER BY serie_id) AS getwinner GROUP BY serie_id, winner ORDER BY serie_id;

*/

DROP TRIGGER update_win_count on ass2."Game";

CREATE OR REPLACE FUNCTION ass2.update_win_count() RETURNS trigger AS $$
    BEGIN
	IF NEW.goal1 > NEW.goal2 THEN
		UPDATE ass2."Serie" SET game_won1 = game_won1 +1 WHERE id=NEW.serie_id;
	ELSE
		UPDATE ass2."Serie" SET game_won2 = game_won2 +1 WHERE id=NEW.serie_id;	
	END IF;
	RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_win_count
    AFTER INSERT ON ass2."Game"
    FOR EACH ROW
    EXECUTE PROCEDURE ass2.update_win_count();


