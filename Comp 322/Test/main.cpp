#include <iostream>

using namespace std;

class A {
public:
A() {cout << "cons" << endl;}
 ~A() { cout << "0" << endl; }

};
class B : public A {
public:
 B() {cout << "cons 1" << endl;}
 ~B() { cout << "1" << endl; };

};
int main () {
A *pa = new B;
delete pa;
}
