#include "stdio.h"
#include "stdlib.h"
#include "linked.h"

int main()
{

    char name[50], pass[50];
    printf("Welcome to tim login page\n");
    createList();
    int i;
    for(i = 0; i < 3; i++)
    {
        printf("Username: ");
        scanf("%s", name);
        printf("Password: ");
        scanf("%s", pass);

        if(valid(name, pass))
        {
            printf("Congratulations, you've made it\n");
            break;
        }
        else
        {
	    if(i >= 2)
        	printf("Calling the police!\n");
            else
               printf("Sorry, please try again.\n");
        }
    }
    

    exit(EXIT_SUCCESS);


}
