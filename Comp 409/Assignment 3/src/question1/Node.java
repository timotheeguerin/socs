package question1;

/**
 * question1.Node
 * Created by tim on 14-10-30.
 */
public class Node<T>
{
    public T value;

    //public AtomicStampedReference<question1.Node> next;
    public Node<T> next;

    public Node(T value)
    {
        this.value = value;
        next = null;
    }
}
