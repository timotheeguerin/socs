#include "cipher.h"

int SHIFT = 1;

void initialise(char matrix[150][150])
{
    int i,j= 0;

    for(i = 0; i < 150; i++)
    {
        for(j = 0; j < 150; j++)
        {
            matrix[i][j] = '\0';
        }
    }
}

void encryptAll(char matrix[150][150])
{
    int i = 0;

    for(i = 0; i < 150; i++)
    {
        encrypt(matrix[i]);
    }
}

void encrypt(char str[150])
{
    int i = 0;
    for(i = 0; i < 150; i++)
    {
        char c = str[i];
        c += SHIFT;
        str[i] = c;
    }

}

void decryptAll(char matrix[150][150])
{
    int i = 0;

    for(i = 0; i < 150; i++)
    {
        decrypt(matrix[i]);
    }
}

void decrypt(char str[150])
{
    int i = 0;
    for(i = 0; i < 150; i++)
    {
        char c = str[i];
        c -= SHIFT;
        str[i] = c;
    }
}
