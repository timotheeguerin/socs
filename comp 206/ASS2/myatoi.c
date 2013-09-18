#include "stdio.h"
#include "string.h"
#include "Math.h"

int myatoi(char str[]);

int main()
{
    printf("Input a number: ");
    char str[8];
    scanf("%s", &str);
    int a = myatoi(str);

    if(a != -1)
        printf("%d\n", a);
    return 0;
}

int myatoi(char str[])
{
    int len = strlen(str);
    int i,result = 0;
    for(i = 0; str[i] != '\0'; i++)
    {
        char a = str[i];

        if(a >= '0' && a <= '9')
        {
            int digit = a - 48;
            result += digit * pow(10, len - i  -1);
        }
        else
        {
            printf("Error not a positive number");
            return -1;
        }
    }
    return result;

}



