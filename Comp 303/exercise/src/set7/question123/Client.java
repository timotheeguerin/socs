package set7.question123;

public class Client
{
    public static void main(String[] args)
    {
        Employee e1 = new Employee("Alice");
        Employee m1 = new Manager("Alice");

        compareThem(e1, m1);
        setMentors();
    }

    public static void compareThem(Employee e1, Employee e2)
    {
        System.out.println(e1.equals(e2));
        System.out.println(e2.equals(e1));
        System.out.println(e2.equals(e2.clone()));
    }

    public static void setMentors()
    {
        Employee robert = new Employee("Robert");
        Employee diana = new Employee("Diana");
        Employee mark = new Manager("Mark");
        Employee frank = new Manager("Frank");
        setMentor(robert, diana);
        setMentor(robert, mark);
        setMentor(mark, frank);
        setMentor(mark, diana);
    }

    public static void setMentor(Employee a, Employee b)
    {
        try
        {
            a.setMentor(b);
            System.out.println("Worked");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
