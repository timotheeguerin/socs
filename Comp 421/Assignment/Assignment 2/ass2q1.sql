-- 1
SELECT DISTINCT id FROM "Games" WHERE abs(goals1-goals2) >= 4 ORDER BY id;

-- 2
SELECT DISTINCT round FROM "Series" s JOIN "Games" g ON s.id = g.id WHERE ot=1;

-- 3
SELECT DISTINCT id FROM "Series" 
EXCEPT SELECT s.id FROM "Series" s JOIN "Games" g ON s.id = g.id WHERE ot=1;


-- 4
SELECT DISTINCT g.id FROM "Games" g
EXCEPT SELECT g1.id 
FROM "Games" as g1 JOIN "Games" as g2 ON g1.id = g2.id
WHERE g1.goals1 != g2.goals1 OR g1.goals2 != g2.goals2;

-- 5
SELECT id, game, (goals1 + goals2) as total FROM "Games" WHERE (goals1 + goals2) = (SELECT MIN (goals1 + goals2)  FROM "Games" );


-- 6
SELECT team1, goals1, team2, goals2, game FROM "Series" s JOIN "Games" g ON s.id = g.id
WHERE team1 = 'Pittsburgh Penguins' OR team2 = 'Pittsburgh Penguins' ORDER BY s.id, game; 


-- 7
SELECT team1, goals1, team2, goals2 FROM "Series" s JOIN "Games" g ON s.id = g.id
WHERE (team1 = 'Pittsburgh Penguins' OR team2 = 'Pittsburgh Penguins') AND g.id = (
SELECT MAX(s.id) FROM "Series" s WHERE (s.team1 = 'Pittsburgh Penguins' OR s.team2 = 'Pittsburgh Penguins'))
ORDER BY s.id, game; 


-- 8
SELECT AVG(goals1+goals2) FROM "Games";

-- 9
SELECT id, COUNT(game) FROM  "Games" GROUP BY id;


-- 10
SELECT id FROM "Series" s
WHERE (SELECT COUNT(game) FROM  "Games" g WHERE s.id = g.id ) != 7;

-- 11
SELECT id FROM "Series" s
WHERE (team1 = 'Montreal Canadiens' OR team2 = 'Montreal Canadiens' ) 
AND (SELECT COUNT(game) FROM  "Games" g WHERE s.id = g.id ) != 7;

-- 12
SELECT id FROM "Series" s
WHERE (SELECT SUM(ot) FROM  "Games" g WHERE s.id = g.id ) >= 2;

-- 13
SELECT id FROM "Series" s WHERE
(SELECT COUNT(game) as Games_count FROM  "Games" g WHERE s.id = g.id GROUP BY g.id) =
(SELECT MAX(Games_count) FROM (SELECT COUNT(game) as Games_count FROM  "Games" g GROUP BY g.id) as getGamescount);

-- 14
SELECT id, winner, SUM(win) as win FROM 
(
	SELECT id, 'team1'as winner, 0 as win FROM "Games" GROUP BY id UNION 
	SELECT id, 'team2'as winner, 0 as win FROM "Games" GROUP BY id UNION
	SELECT id, winner , COUNT(winner)as win FROM (
		SELECT id, (CASE WHEN goals1 > goals2 THEN 'team1' ELSE 'team2' END) as winner
		FROM "Games") AS getall GROUP BY id, winner ORDER BY id) AS getwinner GROUP BY id, winner ORDER BY id;


-- 15
CREATE OR REPLACE FUNCTION update_win_count() RETURNS trigger AS $$
    BEGIN
	IF NEW.goals1 > NEW.goals2 THEN
		UPDATE "Series" SET Games_won1 = Games_won1 +1 WHERE id=NEW.id;
	ELSE
		UPDATE "Series" SET Games_won2 = Games_won2 +1 WHERE id=NEW.id;	
	END IF;
	RETURN NEW;
    END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS update_win_count on "Games" ;
CREATE TRIGGER update_win_count
    AFTER INSERT ON "Games"
    FOR EACH ROW
    EXECUTE PROCEDURE update_win_count();