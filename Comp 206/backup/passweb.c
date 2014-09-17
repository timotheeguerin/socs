#include "stdio.h"
#include "stdlib.h"
#include "cipher.c"

#include "string.h"

typedef struct
{
    char name[50];
    char pass[50];
    char type[50];
    int emptyUser;
} User;

void loadFile(User users[150]);
void writeFile(User users[150]);

void add(char *u, char *p, char *t);
void del(char *u);
void edit(char *u1, char *p1, char *u2, char *p2, char *t2);
void verify(char *u, char *p);

void toUserList(User users[150],char matrix[150][150]);
void toMatrix(char matrix[150][150], User users[150]);

void splitLine(User* user,char line[150]);
void split(char str[150], char first[150]);

void mergeLine(char line[150],User* user);

#include "menu.c"


int main(int argc, char *argv[])
{
    if(argc > 1)
    {

            if(!strcmp(argv[1], "-menu"))
            {
                mainMenu();
            }
            else if(!strcmp(argv[1], "-add") && argc > 4)
            {

                add(argv[2], argv[3], argv[4]);
            }
            else if(!strcmp(argv[1], "-del")&& argc > 2)
            {
                del(argv[2]);
            }
            else if(!strcmp(argv[1], "-edit")&& argc > 7)
            {
                edit(argv[2], argv[3], argv[5], argv[6], argv[7]);
            }
            else if(!strcmp(argv[1], "-verify")&& argc > 3)
            {
                verify(argv[2], argv[3]);
            }


    }
    else
    {
        printf("Too few arguments!\n");
    }
    exit(EXIT_FAILURE);
}


void add(char *u, char *p, char *t)
{

    User userToAdd;
    strcpy(userToAdd.name, u);
    strcpy(userToAdd.pass, p);
    strcpy(userToAdd.type, t);
    userToAdd.emptyUser = 0;

    int i;
    User users[150];
    loadFile(users);

    for(i =0; i < 150; i++)
    {
        if(users[i].emptyUser != 1)
        {
            if(!strcmp(users[i].name, userToAdd.name))
            {
                printf("User already exist\n");
                return;
            }
        }
        else //we reach the end of the file
        {
            break;
        }
    }
    if(i < 49)
    {
        users[i+1] = users[i]; //shift the emptyuser to the new last position
    }
    users[i] = userToAdd;

    writeFile(users);
}

void del(char *u)
{
    User userToDel;
    strcpy(userToDel.name, u);

    int i = 0, index = -1;
    User users[150];
    loadFile(users);

    for(i =0; i < 150; i++)
    {

        if(users[i].emptyUser != 1)
        {
            if(!strcmp(users[i].name, userToDel.name))
            {
                index = i;
                break;
            }
        }
        else //we reach the end of the file
        {
            break;
        }
    }

    if(index != -1)
    {
        for(i =index; i < 49; i++)
        {

            if(users[i].emptyUser != 1)
            {
               users[i] =  users[i+1];
            }
            else //we reach the end of the file
            {
                break;
            }
        }
    }
    else
    {

        printf("No user have this name!");
    }


    writeFile(users);
}



void edit(char *u1, char *p1, char *u2, char *p2, char *t2)
{

    User userToAdd;
    strcpy(userToAdd.name, u2);
    strcpy(userToAdd.pass, p2);
    strcpy(userToAdd.type, t2);
    userToAdd.emptyUser = 0;

    User userToDel;
    strcpy(userToDel.name, u1);
    strcpy(userToDel.pass, p1);
    userToAdd.emptyUser = 0;

    int i = 0, index = -1;
    User users[150];
    loadFile(users);

    for(i =0; i < 150; i++)
    {
        if(users[i].emptyUser != 1)
        {
            if(!strcmp(users[i].name, userToAdd.name))
            {
                printf("User already exist\n");
                return;
            }
            else if(index == -1 && !strcmp(users[i].name, userToDel.name) && !strcmp(users[i].pass, userToDel.pass))
            {
                index = i; //dont break so we can check all the entry if the new name already exist
            }
        }
        else //we reach the end of the file
        {
            break;
        }
    }

    if(index != -1)
    {
        strcpy(users[index].name, userToAdd.name);
        strcpy(users[index].pass, userToAdd.pass);
        strcpy(users[index].type, userToAdd.type);
    }
    else
    {

        printf("No user have this name!");
    }


    writeFile(users);
}

void verify(char *u, char *p)
{
    User userToVerify;
    strcpy(userToVerify.name, u);
    strcpy(userToVerify.pass, p);

    int i = 0;
    User users[150];
    loadFile(users);

    for(i =0; i < 150; i++)
    {

        if(users[i].emptyUser != 1)
        {
            if(!strcmp(users[i].name, userToVerify.name) && !strcmp(users[i].pass, userToVerify.pass))
            {
                printf("User valid\n");
                exit(EXIT_SUCCESS);
            }
        }
        else //we reach the end of the file
        {
            break;
        }
    }
    printf("User invalid\n");
    exit(EXIT_FAILURE);
}

void toUserList(User users[150],char matrix[150][150])
{
    int i;
    for(i =0; i < 150; i++)
    {

        if(strlen(matrix[i]) !=0)
        {
            User user;
            splitLine(&user,matrix[i]);
            users[i] = user;

        }
        else //we reach the end of the file
        {
            break;
        }
    }
    User userEmpty;
    userEmpty.emptyUser = 1;
    users[i] = userEmpty;
}

void toMatrix(char matrix[150][150], User users[150])
{
    int i;
    for(i =0; i < 150; i++)
    {
        if(users[i].emptyUser != 1)
        {
            mergeLine(matrix[i],&users[i]);
        }
        else //we reach the end of the file
        {
            break;
        }
    }
}

void splitLine(User* user,char line[150])
{
    //Copy the line in a other array so we dont modify the line
    char str[150];
    strcpy(str,line);
    split(str, user->name);
    split(str, user->pass);
    strcpy(user->type,str);
}

void split(char str[150], char first[150])
{
    char last[150] = "\0";
    int found = 0;
    int i = 0;
    for(i = 0; i < strlen(str); i ++)
    {
        if(found == 0 && str[i] == ',')
        {
                found = i+1;
                first[i] = '\0';
        }
        else
        {

            if(found == 0)
            {
                first[i] = str[i];
            }
            else
            {
                last[i-found] = str[i];
            }
        }
    }
    strcpy(str,last);
}

void mergeLine(char line[150],User* user)
{
    strcpy(line, user->name);
    strcat(line, ",");
    strcat(line, user->pass);
    strcat(line, ",");
    strcat(line, user->type);
}

void loadFile(User users[150])
{
    char matrix[150][150];
    initialise(matrix);
    FILE* file_ptr;

    file_ptr = fopen("password.csv", "r");
    if(file_ptr == NULL)
    {
        printf("File Doesnt exist create it\n");

        file_ptr = fopen("password.csv", "w");
        fclose(file_ptr);

        file_ptr = fopen("password.csv", "r");

        if(file_ptr == NULL) //if file still doesnt open
        {
            printf("[ERROR]: READ FILE\n");
            return;
        }
    }

    int counter = 0, i=0;
    while(fgets(matrix[counter],150,file_ptr))
    {
        decrypt(matrix[counter]);

        for (i = 0; i < 150; i++ )
        {
            //Check for \n that fgets add but as we already decrypt it will be - SHIFT
            if (matrix[counter][i] == '\n' - SHIFT)
            {
                matrix[counter][i] = '\0';
                break;
            }
        }
        counter ++;
        if(counter > 149)
            break;
    }
    toUserList(users, matrix);
}

void writeFile(User users[150])
{
    char matrix[150][150];
    initialise( matrix);
    toMatrix(matrix, users);
    encryptAll(matrix);

    FILE* file_ptr;

    file_ptr = fopen("password.csv", "w");
    if(file_ptr == NULL)
    {
        printf("[ERROR]: READ FILE\n");
        return;
    }

    int i = 0, j =0;
    for (i = 0; i < 150; i++ )
    {
        for (j = 0; j < 150; j++ )
        {
            if(matrix[i][j] == '\0' + SHIFT)
                break;
            fputc(matrix[i][j], file_ptr);
        }
        fputc('\n', file_ptr);

    }
}
