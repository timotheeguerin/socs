#include "stdio.h"
#include "stdlib.h"
#include "string.h"

void sendCommands();

int main()
{
    sendCommands();
    exit(EXIT_SUCCESS);
}

void sendCommands()
{
    char command[100];

    printf("Send command to 'server':\n");

    while(fgets(command, sizeof command, stdin))
    {
        //Open the file here so it send the command imediatly
        FILE* file_ptr;

        file_ptr = fopen("port.csv", "a");
        if(file_ptr == NULL)
        {
            printf("[Error]: LOAD FILE");
            return;
        }

        char fullcmd[300] = "server client1 ";
        int i;
        for ( i = 0; i < 50; i++ )
        {
            if (command[i] == '\n')
            {
                command[i] = '\0';
                break;
            }
        }

        if(strlen(command))
        {
            strcat(fullcmd, command);
            fputs(fullcmd, file_ptr);
            fputc('\n', file_ptr);
        }
        if(!strcmp(command,"quit"))
        {
            break;
        }
        fclose(file_ptr);
    }
}




