#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


void printValue(FILE* pfile, char* value);

int main(int argc, char* argv[], char ** envp)
{
    pid_t pid = getpid();
    pid_t ppid = getppid();
    printf("Process ID: %d\n", pid);
    printf("Parent process ID: %d\n", ppid);
    
    char filename[50];
    sprintf (filename, "/proc/%d/status", pid);
    FILE* file_ptr;
    file_ptr = fopen(filename, "r");
    if(file_ptr == NULL)
    {
        printf("[Error]: LOAD OUTPUT FILE\n");
        return 0;
    }
    printf("Satus :");    
    printValue(file_ptr, "Name");
    printValue(file_ptr, "State");
    printValue(file_ptr, "VmStk");
    printValue(file_ptr, "voluntary_ctxt_switches");
    printValue(file_ptr, "nonvoluntary_ctxt_switches");
  
    fclose(file_ptr);
    printf("Environement Variables: \n");    
    char** env;
    for (env = envp; *env != 0; env++)
    {
        char* envname = *env;
        printf("\t%s\n", envname);    
    }
    return 0;
} 


void printValue(FILE* pfile, char* str)
{
    rewind(pfile);
    
    char name[50];
    char value[300];
    while(!feof(pfile))
    {
   
        fscanf (pfile, "%[^:]: %[^\n]\n", name, value);
        if(strcmp(name, str) == 0)
        {
            printf("\t%s: %s\n", name, value);
        }
    }
}