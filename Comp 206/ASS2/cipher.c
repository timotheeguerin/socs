#include "stdio.h"
#include "string.h"

int printMenu();
void inputText(char matrix[50][50]);
void encrypt(char matrix[50][50]);
void decrypt(char matrix[50][50]);

void initialise(char matrix[50][50]);
void displayMatrix(char matrix[50][50]);


void transpose(char matrix[50][50]);
int columnIsEmpty(char[50][50], int col);

int main()
{
    char matrix[50][50];
    initialise(matrix);

    int result = 0;
    while(result != 4)
    {
        result = printMenu();

        switch(result)
        {
            case 1:
                inputText(matrix);
                break;
            case 2:
                encrypt(matrix);
                break;
            case 3:
                decrypt(matrix);
                break;
        }

    }
    return 0;
}

void initialise(char matrix[50][50])
{
    int i,j= 0;

    for(i = 0; i < 50; i++)
    {
        for(j = 0; j < 50; j++)
        {
            matrix[i][j] = ' ';
        }
    }
    for(i = 0; i < 50; i++)
    {

        matrix[i][0] = '\0';

    }
}

void displayMatrix(char matrix[50][50])
{
    int i,j= 0;

    for(i = 0; i < 50; i++)
    {
        if(strlen(matrix[i]) == 0)
            break;
        for(j = 0; j < 50; j++)
        {
            if(matrix[i][j] == '\0')
                break;
            else
                printf("%c", matrix[i][j]);
        }
        printf("\n");
    }
}

int printMenu()
{
    printf("MAIN MENU\n");
    printf("=========\n");
    printf("1. Input Text\n");
    printf("2. Encrypt\n");
    printf("3. Decrypt\n");
    printf("4. Exit\n");
    printf("Selection: ");
    int input = 0;
    scanf("%d", &input);

    return input;

}

void inputText(char matrix[50][50])
{
    printf("Input your text:\n");

    int i;
    initialise(matrix);

    char str[50] = "\0";
    fgets(str, sizeof str, stdin);
    int line = 0;
    while(fgets(str, sizeof str, stdin))
    {
        //Remove any '\n' fegts could have added
        for ( i = 0; i < 50; i++ )
        {
            if (str[i] == '\n')
            {
                str[i] = '\0';
                break;
            }
        }

        //Check if the line is empty and quit
        if(strlen(str) == 0)
        {
            break;
        }
        else
        {
            strcpy(matrix[line],str);
            line++;
        }
    }
}

void encrypt(char matrix[50][50])
{
    printf("Input a number:\n");
    int number;
    scanf("%d", &number);
    number = number%26; //Reduce the big numbers
    int i,j = 0;
    for(i = 0; i < 50; i++)
    {
        for(j = 0; j < 50; j++)
        {
            char c = matrix[i][j];
            if((c >= 'a' && c<= 'z'))
            {
                c -= 'a'; //shift so its only from 0 to 25
                c += number;
                c = c%26;
                c += 'a';
                matrix[i][j] = c;
            }
            else if((c >= 'A' && c<= 'Z'))
            {
                c -= 'A'; //shift so its only from 0 to 25
                c += number;
                c = c%26;
                c += 'A';
                matrix[i][j] = c;
            }
        }
    }

    transpose(matrix);


    for(i = 0; i < 50; i++)
    {
        if(strlen(matrix[i]) == 0)
            break;
        for(j = 0; j < 50; j++)
        {
            if(matrix[i][j] == '\0')
                break;
            else
                printf("%c", matrix[i][j]);
        }
    }
    printf("\n");

}

void decrypt(char matrix[50][50])
{
    printf("Input a number:\n");
    int number;
    scanf("%d", &number);
    int i,j = 0;
    number = number%26; //Reduce the big numbers
    for(i = 0; i < 50; i++)
    {
        for(j = 0; j < 50; j++)
        {
            char c = matrix[i][j];
            if((c >= 'a' && c<= 'z'))
            {
                c -= 'a'; //shift so its only from 0 to 25
                c += 26; //Add 26 so it doesnt go to negative a crash
                c -= number;
                c = c%26;
                c += 'a';
                matrix[i][j] = c;
            }
            else if((c >= 'A' && c<= 'Z'))
            {
                c -= 'A'; //shift so its only from 0 to 25
                c += 26; //Add 26 so it doesnt go to negative a crash
                c -= number;
                c = c%26;
                c += 'A';
                matrix[i][j] = c;
            }
        }
    }

    transpose(matrix);
    displayMatrix(matrix);
    printf("\n");

}

void transpose(char matrix[50][50])
{
    int i,j =0;
    char trans[50][50];
    initialise(trans);
    for(i = 0; i < 50; i++)
    {
        //Check if the coloumn is empty and
        if(columnIsEmpty(matrix, i))
        {
            break;
        }


        //char str[50] = "\0";
        for(j = 0; j < 50; j++)
        {
            //Check if the line is empty and in this case add a \0 at the end of the transpose matrix
            if(strlen(matrix[j]) == 0)
            {
                //trans[i][j] == '\0';
                break;
            }
            else if(matrix[j][i] == '\0')
            {
                trans[i][j] = ' ';
            }
            else
            {
                //Just tranpose
                trans[i][j] = matrix[j][i];
            }
        }

        if(j < 49)
        {
            trans[i][j] = '\0';
        }
    }

    for(i = 0; i < 50; i++)
    {
        for(j = 0; j < 50; j++)
        {
            matrix[i][j]=  trans[i][j];
        }
    }
}

int columnIsEmpty(char matrix[50][50], int col)
{
    int i = 0;
    for(i = 0; i < 50; i++)
    {
        if(!(matrix[i][col] == ' ' || matrix[i][col] == '\0'))
        {
            return 0;
        }
    }
    return 1;
}
