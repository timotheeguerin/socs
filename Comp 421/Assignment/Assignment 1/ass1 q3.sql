SELECT pid FROM Project WHERE dep_id ='D2';

SELECT DISTINCT Project.pid FROM Project JOIN Evaluation ON Project.pid = Evaluation.pid WHERE grade='excelent';

SELECT e_name FROM Department JOIN Employee ON manager = e_id WHERE Department.dep_id = 'D2';

SELECT DISTINCT Employee.e_id FROM Employee  JOIN Evaluation ON Evaluation.e_id = Employee.e_id WHERE Evaluation.pid = 'P1' OR Evaluation.pid = 'P2';

SELECT Project.pid FROM Project JOIN Evaluation ON Evaluation.pid = Project.pid WHERE Evaluation.e_id in (SELECT manager from Department);

SELECT Project.pid FROM Project JOIN Evaluation ON Evaluation.pid = Project.pid 
	WHERE Evaluation.e_id in (SELECT manager from Department WHERE Department.dep_id = Project.dep_id);

SELECT Project.pid, Project.start_year FROM Project WHERE Project.pid not in 
(SELECT DISTINCT Project.pid FROM Project JOIN Evaluation ON Project.pid = Evaluation.pid WHERE grade!='excelent');

SELECT Project.pid FROM Project WHERE (SELECT COUNT(Evaluation.pid) FROM Evaluation WHERE Evaluation.pid = Project.pid) > 1;




