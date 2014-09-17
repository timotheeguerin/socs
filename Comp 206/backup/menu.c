#include "stdio.h"
#include "stdlib.h"

void mainMenu();
void addMenu();
void delMenu();
void editMenu();
void verifyMenu();

void mainMenu()
{
    int sel = -1;

    while(sel != 0)
    {
        printf("MENU:\n");
        printf("1. Add a user\n");
        printf("2. Del a user\n");
        printf("3. Edit a user\n");
        printf("4. Verify a user\n");
        printf("Selection: ");

        scanf("%d", &sel);

        switch(sel)
        {
            case 1:
                addMenu();
                break;
            case 2:
                delMenu();
                break;
            case 3:
                editMenu();
                break;
            case 4:
                verifyMenu();
                break;
            default:
                sel = 0;
        }
    }

}

void addMenu()
{
    char name[50], pass[50], type[50];
    printf("Add a user:\n");

    printf("User name: ");
    scanf("%s", name);
    printf("Password: ");
    scanf("%s", pass);
    printf("Type: ");
    scanf("%s", type);
    add(name, pass, type);
}

void delMenu()
{
        char name[50];
        printf("Delete a user:\n");

        printf("User name: ");
        scanf("%s", name);
        del(name);
}

void editMenu()
{
    char name1[50], pass1[50], name2[50], pass2[50], type2[50];
    printf("Edit a user:\n");

    printf("User name: ");
    scanf("%s", name1);
    printf("Password: ");
    scanf("%s", pass1);

    printf("New user name: ");
    scanf("%s", name2);
    printf("New password:");
    scanf("%s", pass2);
    printf("New type: ");
    scanf("%s", type2);
    edit(name1, pass1, name2,pass2, type2);
}

void verifyMenu()
{
    char name[50], pass[50];
    printf("Verify a user:\n");

    printf("User name: ");
    scanf("%s", name);
    printf("Password: ");
    scanf("%s", pass);

    verify(name, pass);
}
