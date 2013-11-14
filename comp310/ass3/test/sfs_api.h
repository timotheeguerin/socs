#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAXDISK 1000
#define MAXFILE 100
#define EMPTY (-2)

typedef struct
{
    char filename[32];
    int fat_index;
    time_t created;
    int size;
    int empty;
} disk_file;

typedef struct
{
    disk_file dir_table[MAXFILE];
    int next_cursor;
} root_dir;

typedef struct
{
    int db_index;
    int next;
} fat_node;

typedef struct
{
    fat_node fat_nodes[MAXDISK];
    int next_cursor;
} fat_table;

typedef struct
{
    char filename[32];
    int root_index;
    int opened;
    int wr_ptr;
    int rd_ptr;
} file_descriptor_table_node;

typedef struct
{
    int freeblocks[MAXDISK];
} freeblocklist;

extern root_dir root;
extern fat_table FAT;
extern freeblocklist freebl;
extern file_descriptor_table_node fdt[MAXFILE];
void mksfs(int fresh);
void sfs_ls();
int sfs_open(char *name);
int sfs_close(int fileID);
int sfs_write(int fileID, char *buf, int length);
int sfs_read(int fileID, char *buf, int length);
