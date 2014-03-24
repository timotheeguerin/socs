
SELECT * FROM user u WHERE u.id in 
(SELECT id FROM (SELECT u0.id, EXTRACTVALUE(info,'/profile/sexe') as sexe, EXTRACTVALUE(info,'/profile/timezone') as timezone FROM user u0) as get_sexe 
WHERE sexe = 'F' AND timezone = '+1');