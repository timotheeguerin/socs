#include "stdio.h"
#include "stdlib.h"
#include "time.h"
#include "string.h"


typedef struct
{
    char sender[100];
    char cmd[100];
} Command;


int readCommands(Command commands[50]);
void writeCommands(Command commands[50]);
void clearPort();
void wait (int s);
int splitCommand(char str[300], Command* command);
void split(char str[300], char first[100]);


int main()
{
    printf("[INFO]: SERVER LAUNCH\n");
    int running = 1;
    while(running)
    {

        wait(1);
        Command commands[50];
        if(readCommands(commands) == -1) //If we found a quit cmd
        {
            running = 0;
        }
        clearPort();
        writeCommands(commands);
    }
    printf("[INFO]: SERVER QUIT\n");
    exit(EXIT_SUCCESS);

}

void wait (int s)
{
  clock_t end;
  end = clock () + s * CLOCKS_PER_SEC;
  while(clock() < end){}
}

int readCommands(Command commands[50])
{
    FILE* file_ptr;
    int count = 0;
    int quit = 0;
    file_ptr = fopen("port.csv", "r");
    if(file_ptr == NULL)
    {
        printf("[ERROR]: READ PORT");
        return 0;
    }
    char line[300];
    //fgets(line,300,file_ptr);

    int i;

    while(fgets(line,300,file_ptr))
    {
        for (i = 0; i < 300; i++ )
        {
            if (line[i] == '\n')
            {
                line[i] = '\0';
                break;
            }
        }

        if(splitCommand(line, &commands[count]) != -1)
        {
            if(!strcmp(commands[count].cmd,"quit"))
            {
                quit = 1;
                count ++;
                break;
            }

            count ++;
        }
    }
    commands[count].sender[0] = '\0';
    commands[count].cmd[0] = '\0';

    fclose(file_ptr);

    if(quit)
        return -1;

    return 0;

}

void clearPort()
{
    FILE* file_ptr;
    file_ptr = fopen("port.csv", "w");
    if(file_ptr == NULL)
    {
        printf("[Error]: EMPTY FILE\n");
        return;
    }
    fclose(file_ptr);
}

int splitCommand(char message[300], Command* command)
{
    //Copy the message in a other array so we keep a trace of it
    char str[300];
    strcpy(str,message);

    char server[100];
    char sender[100];

    split(str, server);

    //Check the command is for this server
    if(!strcmp(server,"server"))
    {
        split(str, sender);

        strcat(sender,".txt");
        strcpy((*command).sender,sender);

        //Finaly add the command to the struct
        strcpy((*command).cmd,str);
    }
    else
    {
        printf("Message '%s' ignored.\n", message);
        return -1;
    }
    return 0;

}

//This function split char[] in two pieces when it find a space
void split(char str[300], char first[100])
{
    char last[300] = "\0";
    int found = 0;
    int i = 0;
    for(i = 0; i < strlen(str); i ++)
    {
        if(found == 0 && str[i] == ' ')
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



void writeCommands(Command commands[50])
{

    int i;
    for(i = 0; i < 50; i++)
    {
        //If the command is not empty
        if(strlen(commands[i].sender))
        {
            FILE* file_ptr;
            file_ptr = fopen(commands[i].sender, "a");
            if(file_ptr == NULL)
            {
                printf("[Error]: LOAD OUTPUT FILE\n");
                return;
            }

            //Print the command in the server file
            fputs(commands[i].cmd, file_ptr);
            fputc('\n', file_ptr);
            fclose(file_ptr);

        }
        else //Mean we have reach the last command
        {
            break;
        }
    }

}
