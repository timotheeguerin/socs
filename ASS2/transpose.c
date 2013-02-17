#include "stdio.h"

void transpose(int m, int a[m][m]);
void display(int m, int a[m][m]);

int main()
{
    int a[5][5] = {{11, 12,13,14,15}, {21,22,23,24,25},{31,32,33,34,35}, {0, 0,0,0,0},{0, 0,0,0,0} };
    display(5,a);

    transpose(5,a);
    display(5,a);
    return 0;
}

void transpose(int m, int a[m][m])
{
    int i,j = 0;
    for(i = 0; i < m; i++)
    {
        for(j = i; j < m; j++)
        {
            int temp = a[i][j];
            a[i][j] = a[j][i];
            a[j][i] = temp;
        }

    }
}

void display(int m, int a[m][m])
{
    int i,j = 0;
    for(i = 0; i < m; i++)
    {
        for(j = 0; j < m; j++)
        {
            printf("%d ",a[i][j]);
        }
        printf("\n");
    }

}
