#include "stdio.h"
#include "stdlib.h"
#include "string.h"

int main()
{
    char name[50], pass[50], command[200] = "./passweb -verify ";

    printf("Username: ");
    scanf("%s", name);
    printf("Password: ");
    scanf("%s", pass);

    strcat(command, name);
    strcat(command, " ");
    strcat(command, pass);

    int result = system(command);
    if(result == EXIT_SUCCESS)
    {
        printf("Verified!\n");
    }

    else
    {
        printf("Invalid!\n");
    }

    exit(EXIT_SUCCESS);
}
