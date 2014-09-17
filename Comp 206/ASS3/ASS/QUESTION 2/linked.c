#include "linked.h"

NODE *head = NULL;


void createList(void)
{
    loadFile();
}

BOOLEAN add(struct NODE *p)
{
    if(head == NULL)
    {
        head = p;
        return 1;
    }

    NODE* lastUser = head;

    while(lastUser->next != NULL)
    {

        lastUser = lastUser->next;
    }

    lastUser->next = p;
    return 1;
}
BOOLEAN valid(char username[50], char password[50])
{
    NODE* lastUser = head;

    while(lastUser != NULL)
    {
        if(!strcmp(lastUser->username, username) && !strcmp(lastUser->password, password))
            return 1;
        lastUser = lastUser->next;
    }
    return 0;
}



void toUserList(char matrix[150][150])
{
    int i;
    for(i =0; i < 50; i++)
    {

        if(strlen(matrix[i]) !=0)
        {

            NODE *user = malloc(sizeof(NODE));

            splitLine(user,matrix[i]);
            user->next = NULL;
            add(user);
        }
        else //we reach the end of the file
        {
            break;
        }
    }

}


void splitLine(NODE* user,char line[150])
{
    //Copy the line in a other array so we dont modify the line
    char str[150];
    strcpy(str,line);
    split(str, user->username);
    split(str, user->password);
    strcpy(user->usertype,str);
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


void loadFile()
{
    char matrix[150][150];
    initialise( matrix);
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
    toUserList(matrix);
}
