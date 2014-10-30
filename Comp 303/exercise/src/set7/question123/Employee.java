package set7.question123;

public class Employee implements Cloneable
{
    protected String aName;
    protected Employee aMentor;

    public Employee(String pName)
    {
        aName = pName;
    }

    public String getName()
    {
        return aName;
    }

    public void setMentor(Employee pMentor)
    {
        if (!pMentor.getClass().equals(this.getClass()))
        {
            throw new IllegalArgumentException("mentor should be an " + this.getClass());
        }
        aMentor = pMentor;
    }

    /**
     * We need to return an Employee type or we won't be able to return an Employee type in the Manager class.
     *
     * @return a manager recommended
     */
    public Employee recommend()
    {
        return new Manager("Some manager");
    }

    @Override
    public boolean equals(Object pObject)
    {
        if (pObject == null)
            return false;
        if (pObject == this)
            return true;
        if (!(pObject instanceof Employee))
            return false;
        return aName.equals(((Employee) pObject).getName());
    }

    @Override
    public int hashCode()
    {
        return aName.hashCode();
    }

    @Override
    public Employee clone()
    {
        return new Employee(getName());
    }
}

class Manager extends Employee
{
    public Manager(String pManager)
    {
        super(pManager);
    }

    public Employee recommend()
    {
        return new Employee("Some employee");
    }

    @Override
    public boolean equals(Object pObject)
    {
        if (pObject == null)
            return false;
        if (pObject == this)
            return true;
        if (!(pObject instanceof Manager))
            return false;
        return aName.equals(((Employee) pObject).getName());
    }

    @Override
    public Employee clone()
    {
        return new Manager(getName());
    }
}
