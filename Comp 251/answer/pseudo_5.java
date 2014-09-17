/**
*	Question 1
*/
ComputeExamSchedule(D,R,C) {
	//Create graph and flow
	Graph G=createGraph(D,R,C);
	int maxFlow=FordFulkerson(G);
	//recover graph G' with weights that indicate flow.
		
	for all c in class  
		list<nodes> incoming= new list; 
		Fill incoming with all nodes that have an edge from time t to class c with flow of 1;
		for all node x in incoming
			schedule temporary=new schedule (class c at time t);
			add temporary to calendar;
	
	return calendar;
	
	
	return calendar;//list that will give day, time and room associated to each class
}
createGraph(D,R,C)
	G = new Graph();
	Node start,finish = new nodes in G;
	Set Times, Classes, Students;
	
	//Add all classes
	for all class c in C
		Node n = new node c in G;
		Add node to a set Y;
		add edge from n to finish with weight 1;
	
	//Add the time nodes(3 per days)
	for all day d in D
		Node n1,n2,n3 = create new Node in G.
		Add these node to set Times;
		create edge from start to n1, n2, n3 with weight |R|;
		
	//Map all time to all students
	for each node t in Times
		for(each node c in class)
			create edge from t to s with weight 1;
	
	return G;

schedule{
	string class;
	string time;
	string room;
	Constructor that takes four strings as arguments and sets them;
}

/**
*	Question 2
*/
ComputeExamSchedule(D,R,C) {
	//Create graph and flow
	Graph G=createGraph(D,R,C);
	int maxFlow=FordFulkerson(G);
	//recover graph G' with weights that indicate flow.
	
	/*At index i of following array, we will have the number of rooms 
	 * we have already assigned at timeslot i. The loop below will 
	 * give room 0 to the first course that we found that was 
	 * associated with the time slot and then increment that position 
	 * of the list like that the second course associated with this 
	 * time slot will get room 1 and so on.
	 */
	list<integer> roomindex = new list of size |3D|;
	list<schedule> calendar = new list of size |C|;
	fill roomindex with 0s;
	for all s in student 
		list<nodes> outcoming= new list; 
		list<nodes> incoming= new list; 
		Fill incoming with all nodes that have an edge to a student s with flow of 1;
		Fill outcoming with all nodes that have an edge from a student s with flow of 1;
		for all node x in incoming
			schedule temporary=new schedule (class associated to first node in outgoing, time associated to node x, room #roomindex[x]);
			add temporary to calendar;
		
	return calendar;
	
	
	return calendar;//list that will give day, time and room associated to each class
}
createGraph(D,R,C)
	G = new Graph();
	Node start,finish = new nodes in G;
	Set Times, Classes, Students;
	
	//Add all classes
	for all class c in C
		Node n = new node c in G;
		Add node to a set Y;
		add edge from n to finish with weight 1;
	
	//Add the time nodes(3 per days)
	for all day d in D
		Node n1,n2,n3 = create new Node in G.
		Add these node to set Times;
		create edge from start to n1, n2, n3 with weight |R|;

	//Map all student to the courses he is taking
	for each s in student
		Node s = create new node in G;
		add s node to set Students;
		for each class c in student s )
			create egde from s to c with weight 1;
			
	//Map all time to all students
	for each node t in Times
		for(each node s in Students)
			create edge from t to s with weight 1;
	
	return G;

schedule{
	string class;
	string time;
	string room;
	Constructor that takes four strings as arguments and sets them;
}