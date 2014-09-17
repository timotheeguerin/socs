#include "MatrixLinkedList.h"

//note that this file is not a complete cc file and won't compile by itself
//You will have to define your struct as well as define a few of the methods
//asked of you on the assignment
//You'll also need to include iostream

using namespace std;
void printMatrix(MatrixElement* m1)
{
  if (m1 == NULL)
  {
    return;
  }

  int max = getMaxColumns(m1);
  int lastRow = 0;
  int lastColumn = -1;

  while (m1 != NULL)
  {
    //first print an appropriate number of 0s.
    if (lastRow != m1->row)
    {
      //finish the line off
      printZeros(max - lastColumn);
      cout << endl;

      //print complete rows of zeros
      for (int i=0; i < m1->row - lastRow - 1; i++)
      {
	printZeros(max);
	cout << endl;
      }

      printZeros(m1->column);
      cout << m1->value << " ";
    }
    else {
      printZeros(m1->column - lastColumn - 1);
      cout << m1->value << " ";
    }

    lastRow = m1->row;
    lastColumn = m1->column;
    m1 = m1->next;
  }

  //now just finish printing the column
  for (int i= lastColumn; i < max; i++)
  {
    cout << "0 ";
  }

  cout << endl;
}

void printZeros(int numberZeros)
{
  for (int i=0; i < numberZeros; i++)
  {
    cout << "0 ";
  }
}


int compareTo(const MatrixElement& me1, const MatrixElement& me2)
{
    if(me1.row < me2.row)
    {
        return -1;
    }
        else if(me1.row > me2.row)
    {
        return 1;
    }
    else
    {
        if(me1.column < me2.column)
        {
            return -1;
        }
        else if(me1.column > me2.column)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}

MatrixElement* append(MatrixElement* root, int r,int c,int v)
{
    MatrixElement* me = new MatrixElement();
    me->row = r;
    me->column = c;
    me->value = v;
    me->next = NULL;
    if(!root)
    {
        return me;
    }
    else
    {
        getLast(root)->next = me;
        return root;
    }

}

void print(MatrixElement* root)
{

    MatrixElement* me = root;
    while(me)
    {
        cout << "Row: " << me->row << " ,Column: " << me->column << " ,Value: " << me->value << endl;
        me = me->next;
    }

}


int getMaxColumns( MatrixElement* m)
{
    if(!m)
    {
        return -1;
    }
    MatrixElement* me = m;
    int max = me->column;
    while(me != NULL)
    {

        if(me->column > max)
        {
            max = me->column;
        }
        me = me->next;
    }
    return max + 1;

}


MatrixElement* deleteFirst(MatrixElement* root)
{
    if(root)
    {
        MatrixElement* newRoot = root->next;
        delete root;
        return newRoot;
    }
    else
    {
        return NULL;
    }
}

void deleteList(MatrixElement* root)
{
    while(root)
    {
        root = deleteFirst(root);
    }
}

MatrixElement* getLast(MatrixElement* root)
{

    if(root == NULL)
    {
        return NULL;
    }
    else
    {
        MatrixElement* last = root;
        while(last->next)
        {
            last = last->next;
        }

        return last;
    }

}

MatrixElement* insert(MatrixElement* root, int r, int c, int v)
{
    MatrixElement* me = new MatrixElement();
    me->row = r;
    me->column = c;
    me->value = v;
    me->next = NULL;
    if(!root)
        return me;

    MatrixElement* e = root;

    while(e)
    {
        int result1 = compareTo(*me,*e);
        //Check if its equals to the current element
        if (result1 == 0)
        {
            e->value += me->value;
            break;
        }
        else if(result1 == -1) //mean its in the first place
        {
            me->next = root;
            root = me;
            break;
        }

        //Check if the next element is null: mean that it need to be added at the end
        if(e->next == NULL)
        {

            e->next = me;
            break;
        }

        int result2 = compareTo(*me,*e->next);
        //finally check if its between the current and next element
        if(result1 == 1 && result2 == -1)
        {
            me->next = e->next;
            e->next = me;
            break;
        }
        //iterate
        e = e->next;
    }

    return root;

}

MatrixElement* add( MatrixElement* m1,  MatrixElement* m2)
{
    while(m2)
    {
        m1 = insert(m1, m2->row,  m2->column, m2->value);
        m2 = m2->next;
    }
    return m1;

}

