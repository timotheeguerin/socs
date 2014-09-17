#ifndef MATRIX_LINKED_LIST_H
#define MATRIX_LINKED_LIST_H

#include <iostream>

struct MatrixElement
{
    int row;
    int column;
    int value;
    MatrixElement* next;
};


void printZeros(int numberZeros);
void printMatrix(MatrixElement* m1);

int compareTo(const MatrixElement& me1, const MatrixElement& me2);
MatrixElement* append(MatrixElement* root, int r,int c,int v);
void print(MatrixElement* root);
int getMaxColumns( MatrixElement* m);
MatrixElement* deleteFirst(MatrixElement* root);
void deleteList(MatrixElement* root);
MatrixElement* getLast(MatrixElement* root);
MatrixElement* insert(MatrixElement* root, int r, int c, int v);
MatrixElement* add( MatrixElement* m1,  MatrixElement* m2);
#endif
