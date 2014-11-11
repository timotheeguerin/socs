package test;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Test
 * Created by tim on 14-11-03.
 */
public class Confusing {
    public Confusing(Object o) {
        System.out.println("Object");
    }

    public Confusing(int[] a) {
        System.out.println("array");
    }

    public static void main(String[] args) {
        Sub s = new Sub();
        s.doSomething("MyString");
    }
}

class Super {
    void doSomething(String parameter) {
        System.out.println("Super");
    }
}

class Sub extends Super {
    void doSomething(Object parameter) {
        System.out.println("Sub");
    }
}