--Create table
CREATE TABLE Students
(
	id INT NOT NULL,
 	name VARCHAR(20),
 	login CHAR(10),
 	major VARCHAR(20) DEFAULT 'undefined',
 	school_id INT, 
 	PRIMARY KEY(id),
 	FOREIGN KEY(school_id) REFERENCES School(id)
)

--Drop table
DROP TABLE Students

--Alter table
ALTER TABLE Students ADD COLUMN firstyear:integer


--INSERT
INSERT INTO Students (id, name, faculty) VALUES (8908998, 'Dupont', 'Science')

--Delete 
DELETE FROM Students WHERE id = 0894984

--Update
UPDATE Students SET faculty = 'Arts' WHERE id = 9849849


