
#ifndef max
	#define max( a, b ) ( ((a) > (b)) ? (a) : (b) )
#endif

#ifndef min
	#define min( a, b ) ( ((a) < (b)) ? (a) : (b) )
#endif

#ifndef SFS_API_H
#define SFS_API_H



#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>



#define MAX_DISK 2000
#define MAX_FILE 200
#define EMPTY (-2)

typedef struct
{
    int opened;
    int wr_ptr;
    int rd_ptr;
} file_descriptor;

typedef struct
{
    char filename[32];
    int fat_index;
    time_t timestamp;
    int size;
	file_descriptor fd;
} disk_file;

typedef struct
{
    disk_file table[MAX_FILE];
    int next_cursor;
} root_dir;

typedef struct
{
    int db_index;
    int next;
} fat_node;

typedef struct
{
    fat_node table[MAX_DISK];
    int next_cursor;
} fat_table;



typedef struct
{
    int list[MAX_DISK];
} freeblocklist;

void mksfs(int fresh);
void sfs_ls();
int sfs_open(char *name);
int sfs_close(int fileID);
int sfs_write(int fileID, char *buf, int length);
int sfs_read(int fileID, char *buf, int length);

//helper functions
int getFileIndex(char* name);

#endif
