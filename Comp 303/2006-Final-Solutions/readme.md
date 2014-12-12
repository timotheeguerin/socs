# Question 1: Very short answers `16 points`
Answer each sub-question using a maximum of two sentences. Make sure your answer is clear and
precise

1. **What is the difference between subtype polymorphism and parametric polymorphism?**  
Subtype polymorhism is when a type have multiple subtype that implement some method differently while parametric polymorhsim doesn't depend on any greater type but is a general encapsulation(`generics` in java or `template` in c++)

2. **What is the difference between a join point and a pointcut.?**  
In aspect-oriented computer programming, a pointcut is a set of join points.

3. **What is the advantage of following the Law of Demeter?**  
The law of demeter make sure object access and modify only what is needed. This way you have a more maintainable and adaptable code. Since objects are less dependent on the internal structure of other objects, object containers can be changed without reworking their callers.

4. **What software engineering problem does the ConcernMapper tool address?**  

5. **What collection of mechanisms/features does the word “reflection” (as in Java reflection) refer to?**  

6. **What is the purpose of refactoring, as a software engineering activity?**
As a code is getting modified more and more lines are added which end up being very hard to read. Refactoring will redesign the code to get a cleaner code.

7. **If you need to find out how often a specific method m() is called during a test case, will you use hprof
or will you write an aspect? Justify your answer.** 

8. **What is the difference between a fault and a failure?**

# Question 2 `10 points`
Design a `CompositeIcon` class that can contain multiple icons. Note that a standard application of the
COMPOSITE design pattern will result in the composed icons being painted on top of each other. Solve this
problem with a `ShiftedIcon` decorator that will support drawing an icon as shifted by (parametric) x
and y values. Extend the following diagram to complete the design. You do not need to write the code but
make sure you list all the methods (including constructors) that will be necessary to make this work. Add
a few lines of explanation to clarify anything not obvious from the diagram.

![Diagram](https://cloud.githubusercontent.com/assets/1031227/5417004/27c9d1fe-8203-11e4-92be-7ac4121eec9f.png)


# Question 3 `18 points`
An employee management system supports tracking employees through a management hierarchy
(managers can have multiple employees), as well as recursively printing the name of each employee in
the hierarchy, and searching the hierarchy for a person with a specific name. The diagram below shows
the class hierarchy, and the code of the search method of the Manager class.

![capture](https://cloud.githubusercontent.com/assets/1031227/5417166/808bdc82-8204-11e4-9aae-b1ed13199b52.PNG)

```java
public IPerson search( String pName ) {
	IPerson lReturn = super.search( pName );
	if( lReturn == null ) {
		for( Employee e : aEmployees ) {
			lReturn = e.search(pName);
			if( lReturn != null ) return lReturn;
		}
	}
	return lReturn;
}
```

1. **Revise the design to add VISITOR support to this class hierarchy to factor out the print and search
operations. Draw the revised class diagram.`8 points`**  
![employeemanager](https://cloud.githubusercontent.com/assets/1031227/5420853/a3b2f3d4-8228-11e4-95b3-230ed20976ce.png)

2. **Write the code of the new search method of class Manager. If you decided to move the recursion out
of the method, include the code implementing the recursion (clearly identify in which method it is
located).`4 points`**  
PersonSearchVisitor class:
  ```java
  class PersonSearchVisitor {
    String query;
    
    public void visitEmployee(Employee e) {
      if (e.getName().equals(query)) {
        System.out.println(query);
      }
    }
    
    public void visitManager(Manager m) {
      visitEmployee(m);
      for (Employee e : m.getEmployees()) {
        if (e instanceof Manager) {
          visitManager((Manager) e);
        } else {
          visitEmployee(e);
        }
        
      }
    }
  }
  ```

  Running:
  ```java
  m.search(new PersonSearchVisitor(somename));
  ```

3. **Assume a variable bigBoss of type Manager is the root of the hierarchy. Draw a sequence diagram
representing the behavior resulting from a call to bigBoss.search(“Bilbo”). Assume that bigBoss
is not Bilbo himself.`6 points`**  
![searchvisitor](https://cloud.githubusercontent.com/assets/1031227/5420852/a12a4afe-8228-11e4-9927-2fdddcdde7c1.png)

# Question 4 `15 points`
Design a system capable of storing the model for a weather pattern that can be viewed by a variety of
clients. Your system must meet the following requirements.

1. Your design must realize the OBSERVER design pattern. However, the logic implementing the
mechanism supporting the subject must not be in the same class as the model.
2. A basic weather model consists of a single array of 100 Objects. It should be possible to update
the model by updating individual elements. 
3. The client should have control over whether or not a state change triggers a notification of the
observers.
4. Use the pull model.

Questions:  
1. **Draw a UML class diagram for a design that will respect all the above requirements. Your design
should include a `BasicModel` class and two extensions: `Model1` and `Model2`. You should also
include two views: `View1` and `View2`. Note that you will need more classes and/or interfaces. Use the
`private`, `protected`, `public`, `final`, and `abstract` keywords as appropriate to clarify your
design. Only include the information that supports answering the question (for example, you don’t need to
design what each view actually does, besides the functionality needed to connect it to the model) Briefly
explain how your design meets the requirements. `9 points`**  

2. **Write the code of the class in (a) that encapsulates the implementation of the subject.`4 points`**  

3. **Complete this code snippet in a client that would connect a view with the model. `2 points`**  
```java
public class Client
{
 public void connect( BasicModel pModel, View1 pView )
 {
```

# Question 5 `15 points`
Consider the following (partial) implementation of a system allowing farmers to manage their flock of
sheep. Note that a HashSet is an implementation of the Set interface that is backed by a hash table.
Appendix 1 provides segments of the Java 1.5.0 API that may be useful for this question.

```java
public class Flock
{
    Set<Sheep> aSheep = new HashSet<Sheep>();
    public void addSheep( Sheep pSheep ) { aSheep.add( pSheep ); }
}
class Sheep
{
    private String aName;
    private Date aBirth; // Date/Time of birth of the sheep
    public Sheep( String pName ) { aName = pName; aBirth = new Date();}
    public void rename( String pName ) { aName = pName; }
    public String getName() { return aName; }
}
```
1. **Given the current implementation, if you add the same Sheep object multiple times to a Flock, it will
only be added once. Explain why. `2 points`**  
When adding a new sheep to a Flock object it use a set to store which means element inside are unique. Then if the exact same object is added twice to the flock it will only be added the first time to the set.

2. **At some points scientists discover how to clone sheep. Consider that a cloned sheep is “born” at the
time when it is cloned. The system is thus modified by making the Sheep class implement the
Cloneable interface, and by adding the following method to class Sheep:**
  ```java
  public Object clone() {
   try { return super.clone(); }
   catch( CloneNotSupportedException e ) { return null; }
  }
  ```
**Briefly explain what happens when this method is called, and why this behaviour is probably incorrect
with respect to the representation of cloned sheep.**  
Calling `super.clone()` is not going to clone the sheep correctly as it will copy each attributes into the new object.

3. **Rewrite the clone method so that the problem identified in (c) is fixed.`3 points`**  
  ```java
  public Object clone() {
    try {
      Sheep clone = (Sheep) super.clone();
      clone.aBirth = new Date();
      return clone;
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }
  ```

4. **As it turns out, farmers freak out at the idea of having multiple copies of a sheep running around in
their pasture and require modifications to the system so that it is not possible to have cloned sheep in a
flock. How would you solve that problem? Describe (in English) the changes you would make to the
system to implement this requirement.**  
We would need to add a `private` boolean field to know if a sheep is a clone or not. When we clone we can set this boolean to `true`.

5. **In a different scenario (assume the system does not implement the solution for (4), the users of the
system find it too inconvenient to allow multiple sheep with the same name in a flock. The system is thus
changed so an equals and hashCode methods are implemented in Sheep so that equals simply
compares sheep names, and hashCode simply returns aName.hashCode(). Why does this make the
rename method problematic?`3 points`**
If we rename a sheep in a flock to a name of another sheep in the same flock then we would have a problem as the set would not check for uniqueness(Only during add) and we would have 2 sheeps with the same name in the same flock.

# Question 6 `16 points`
This question will focus on the implementation of a bounded buffer. A bounded buffer is a data structure
that can store a fixed number of elements, and that supports a first-in, first-out (FIFO) behaviour. On a
bounded buffer, a `put` operation puts an object at the tail of the buffer; A `take` operation takes (and
removes) an object from the head of the buffer. `put` is not allowed if the buffer is full, and `take` is not
allowed if the buffer is empty. The figure below shows an example of a bounded buffer of capacity 4
filled with 3 elements.


1. **Bounded buffers are typically used in multi-threaded designs, where one thread puts elements in the
buffer while another thread takes elements from the buffer. In a multi-threaded environment, why is it
important to synchronize the buffer? Describe an incorrect execution scenario involving an
unsynchronized bounded buffer. `4 points`**
If we don't synchronize then data could be lost or duplicated. For example 2 threads tries to push a new value they both receive the index where the value should be stored but the first thread which store the value will lose the data as it will be erased by the second thread.

2. **Assume we synchronize our bounded buffer so that only one thread can access it at the same time.
Furthermore, we define the `put` and `take` operations to be blocking. If a thread calls the put method on
a full buffer, it blocks by yielding its time slice (by calling Thread.yield()) and then tries again, until
the element is inserted. Similarly, if a thread calls the take method on an empty buffer, it blocks by
yielding its time slice (by calling Thread.yield) and then tries again, until an element can be taken.
See question (3) for the code. What is the potential problem with this design? Describe, and illustrate
with a scenario of incorrect execution.`4 points`**  
We will get a deadlock if the buffer is full and we try to add a new value or empty and try to remove a new value. As the thread trying to push the new value to the full buffer will first lock the buffer then yield until the buffer is being emptied. However as we are locking but not unlocking before yielding any other thread trying to `take` will lock and so won't be able to remove any element.

3. **The following code represents the (faulty) implementation described in b). Complete and modify the
code to solve the problem (cross out unwanted statements, add missing statements). See Appendix 2 for
details of the Lock API. `8 points`**
  ```java
  public class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    final Object[] aItems = new Object[100];
    int aPutIndex, aTakeIndex, aCount;
    
    public void put(Object x) throws InterruptedException {
      lock.lock();
      try {
        while (aCount == aItems.length) Thread.yield();
        aItems[aPutIndex] = x;
        if (++aPutIndex == aItems.length) aPutIndex = 0;
        ++aCount;
      } finally {
        lock.unlock();
      }
    }
    
    public Object take() throws InterruptedException {
      lock.lock();
      try {
        while (aCount == 0) Thread.yield();
        Object x = aItems[aTakeIndex];
        if (++aTakeIndex == aItems.length) aTakeIndex = 0;
        --aCount;
        return x;
      } finally {
        lock.unlock();
      }
    }
  }
  ```

  Solution 1(Manual locking/unlocking):
  ```java
  class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    final Object[] aItems = new Object[100];
    int aPutIndex, aTakeIndex, aCount;
    
    public void put(Object x) throws InterruptedException {
      lock.lock();
      try {
        while (aCount == aItems.length) {
          lock.unlock(); //Give up the lock and try again later
          Thread.yield();
          lock.lock();
        }
        aItems[aPutIndex] = x;
        if (++aPutIndex == aItems.length) aPutIndex = 0;
        ++aCount;
      } finally {
        lock.unlock();
      }
    }
    
    public Object take() throws InterruptedException {
      lock.lock();
      try {
        while (aCount == 0)  {
          lock.unlock(); //Give up the lock and try again later
          Thread.yield();
          lock.lock();
        }
        Object x = aItems[aTakeIndex];
        if (++aTakeIndex == aItems.length) aTakeIndex = 0;
        --aCount;
        return x;
      } finally {
        lock.unlock();
      }
    }
  }
  ```

  Solution 2(Using synchronized, cleaner and more efficient - no busy waiting):
  ```java
  class BoundedBuffer {
    final Object[] aItems = new Object[100];
    int aPutIndex, aTakeIndex, aCount;
    
    public synchronized void put(Object x) throws InterruptedException {
      while (aCount == aItems.length) {
        wait();
      }
      aItems[aPutIndex] = x;
      if (++aPutIndex == aItems.length) aPutIndex = 0;
      notifyAll(); //Notify waiting thread they can try again
      ++aCount;
    }
    
    public synchronized Object take() throws InterruptedException {
      while (aCount == 0) {
        wait();
      }
      Object x = aItems[aTakeIndex];
      if (++aTakeIndex == aItems.length) aTakeIndex = 0;
      --aCount;
      notifyAll(); //Notify waiting thread they can try again
      return x;
    }
  }
  ```

# Question 7 `10 points`
You are asked to test the `Math.abs(int)` function. The description of this function is the following:  
_Returns the absolute value of an int value. If the argument is not negative, the argument is returned._
_If the argument is negative, the negation of the argument is returned. Note that if the argument is_
_equal to the value of Integer.MIN_VALUE, the most negative representable int value, the result is_
_that same value, which is negative. The range of Java integer is contained between the values_
_Integer.MIN_VALUE and Integer.MAX_VALUE._

1. **Determine equivalence partitions and boundaries for this functions. The equivalence partitions must
respect the coverage and disjointedness criteria. You should include all possible boundary cases. Provide
your answer by completing the following table:`4 points`**
| Partition/Boundary Description | Input value       | Expected Output   |
|================================|===================|===================|
| Most negative integer          | Integer.MIN_VALUE | Integer.MIN_VALUE |
| Negative integer               |                   |                   |
| Zero                           | 0                 | 0                 |


2. **Complete the jUnit test method below to implement your equivalence partitions and boundaries. You
should use the jUnit assertEquals(int pExpected, int pValue) method, which will
automatically verify that pExpected is equal to pValue and take the appropriate steps if it is not. `3 points`**
  ```java
  public void testAbs()
  {
    assertEquals(Math.abs(Integer.MIN_VALUE), Integer.MIN_VALUE);
    assertEquals(Math.abs(-5), 5);
    assertEquals(Math.abs(0), 0);
    assertEquals(Math.abs(5), 5);
    assertEquals(Math.abs(Integer.MAX_VALUE), Integer.MAX_VALUE);
  }
  ```

3. **You’re in charge of purchasing a test coverage analysis tool. Tool A offers statement coverage analysis
for 200$ and tool B offers branch coverage analysis for 100$. Everything else being equal, which tool
would you purchase to analyze the test coverage for your crib project. Justify your decision.`3 points`**  
Statement coverage will test if all the lines are being tested(Run at least 1 time) but branch coverage will also test for each possible outcome of a if statement. So here you can get a more powerful tool for twice as cheap. Then we should choose the branch coverage. 
