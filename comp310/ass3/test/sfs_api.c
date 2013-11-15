#include "sfs_api.h"
#include "disk_emu.h"

root_dir root;
fat_table FAT;
freeblocklist freebl;
file_descriptor_table_node fdt[MAXFILE];

static int opened_files = 0;
static int BLOCKSIZE;

void mksfs(int fresh)
{
    int rdbsize     = sizeof(root_dir);
    int fatsize     = sizeof(fat_table);
    BLOCKSIZE   = ( rdbsize > fatsize ? rdbsize : fatsize );

    if (fresh) {
        init_fresh_disk("root.sfs", BLOCKSIZE, MAXDISK);

        int i;
        for(i = 1; i < MAXFILE; i++) {
            root.dir_table[i].empty = 1;
        }
        root.next_cursor = 0;
        for(i = 0; i < MAXDISK; i++) {
            freebl.freeblocks[i] = 0;
        }
        for(i = 0; i < MAXDISK; i++) {
            FAT.fat_nodes[i].db_index = EMPTY;
            FAT.fat_nodes[i].next = EMPTY;
        }
        FAT.next_cursor = 0;

        write_blocks( 0, 1, (void *)&root );
        write_blocks( 1, 1, (void *)&FAT );
        write_blocks(MAXDISK-1, 1, (void *)&freebl);
    } else {
        init_disk("root.sfs", BLOCKSIZE, MAXDISK);
        read_blocks( 0, 1, (void *)&root );
        read_blocks( 1, 1, (void *)&FAT );
        read_blocks( MAXDISK-1, 1, (void *)&freebl );
    }
}

void sfs_ls()
{
	int i;
    for (i = 0; root.dir_table[i].empty == 0; i++) {
        int filesize = root.dir_table[i].size / 1000;
        printf("%s  %dKB  %s", root.dir_table[i].filename, 
                filesize, ctime(&root.dir_table[i].created) );
    }
}

int sfs_open(char *name)
{
    int nfileID = -1;
    int i;
    for(i = 0; i < MAXFILE; i++) {
        if ( strcmp( fdt[i].filename, name ) == 0 ) {
            nfileID = i;
            break;
        }
    }
    
    if ( nfileID != -1) {
        return nfileID;
    }
    
    int root_dir_index = -1;
    for(i = 0; i < MAXFILE; i++) {
        if ( strcmp(root.dir_table[i].filename, name) == 0 ) {
            root_dir_index = i;
            break;
        }
    }

    strcpy( fdt[opened_files].filename, name );
    fdt[opened_files].opened = 1;
    fdt[opened_files].rd_ptr = 0;
    nfileID = opened_files;
    ++opened_files;

    if ( root_dir_index == -1 ) {
        fdt[opened_files].wr_ptr = 0;

        FAT.fat_nodes[FAT.next_cursor].db_index = -1;

    for(i = 2; i < MAXDISK - 1; i++) {
        if (freebl.freeblocks[i] == 0) {
            freebl.freeblocks[i] = 1;
            FAT.fat_nodes[FAT.next_cursor].db_index = i;
            break;
        }
    }

        FAT.fat_nodes[FAT.next_cursor].next = -1;
        root.dir_table[ root.next_cursor ].empty = 0;
        strcpy( root.dir_table[ root.next_cursor ].filename, name ); 
        root.dir_table[ root.next_cursor ].fat_index = FAT.next_cursor;
        root.dir_table[ root.next_cursor ].size = 0;
        root.dir_table[ root.next_cursor ].created = time(NULL);

        fdt[nfileID].root_index = root.next_cursor;

    FAT.next_cursor += 1;
    if (FAT.next_cursor > MAXDISK - 1) {
        int z;
        for (z = 0; z < MAXDISK; z++) {
            if (FAT.fat_nodes[z].db_index == EMPTY) {
                break;
            }
        }
        FAT.next_cursor = z;
        if ( z == MAXDISK ) {
            exit(1);
        }
    }

    root.next_cursor += 1;
    if (root.next_cursor > MAXFILE - 1) {
        int z;
        for (z = 0; z < MAXFILE; z++) {
            if (root.dir_table[z].empty == 1) {
                break;
            }
        }
        root.next_cursor = z;
        if (z == MAXFILE) {
            exit(1);
        }
    }

        write_blocks( 0, 1, (void *)&root );
        write_blocks( 1, 1, (void *)&FAT );
        write_blocks(MAXDISK-1, 1, (void *)&freebl);
    } else {
        fdt[nfileID].wr_ptr = root.dir_table[ root_dir_index ].size;
    }
    return nfileID;

}

int sfs_close(int fileID)
{
    if (opened_files <= fileID) {
        fprintf(stderr, "No such file %d", fileID);
    } else {
        fdt[ fileID ].opened = 0;
    }
    return 0;
}

int sfs_write(int fileID, char *buf, int length)
{
    if (opened_files <= fileID) {
        fprintf(stderr, "No such file %d is opened\n", fileID);
        return 0;
    }

    int nlength = length;

    int root_index = fdt[fileID].root_index;
    int fat_index = root.dir_table[ root_index ].fat_index;
    int db_index = FAT.fat_nodes[ fat_index ].db_index;

    while( FAT.fat_nodes[ fat_index ].next != -1 ) {
        db_index = FAT.fat_nodes[ fat_index ].db_index;
        fat_index = FAT.fat_nodes[ fat_index ].next;
    }
    
    char temp_buffer[BLOCKSIZE];

    read_blocks(db_index, 1, (void *)temp_buffer);

    int write_pointer = fdt[ fileID ].wr_ptr % BLOCKSIZE;

    if (fdt[ fileID ].wr_ptr == 0){
	write_pointer = 0;
    }
    else if (fdt[ fileID ].wr_ptr % BLOCKSIZE == 0){
        write_pointer = -1;
    }

    if ( write_pointer != -1 ) {
        memcpy( (temp_buffer + write_pointer), buf, (BLOCKSIZE - write_pointer) );
        write_blocks( db_index, 1, (void *)temp_buffer );
        length = length - (BLOCKSIZE - write_pointer);
        buf = buf + (BLOCKSIZE - write_pointer);
    }

    while (length > 0) {
        memcpy( temp_buffer, buf, BLOCKSIZE );

        db_index = -1;

    int i;
    for(i = 2; i < MAXDISK - 1; i++) {
        if (freebl.freeblocks[i] == 0) {
            freebl.freeblocks[i] = 1;
            db_index = i;
            break;
        }
    }

        FAT.fat_nodes[ fat_index ].next = FAT.next_cursor;
        FAT.fat_nodes[ FAT.next_cursor ].db_index = db_index;
        fat_index = FAT.next_cursor;
        FAT.fat_nodes[ fat_index ].next = -1;

    FAT.next_cursor += 1;
    if (FAT.next_cursor > MAXDISK - 1) {
        int z;
        for (z = 0; z < MAXDISK; z++) {
            if (FAT.fat_nodes[z].db_index == EMPTY) {
                break;
            }
        }
        FAT.next_cursor = z;
    }

        length = length - BLOCKSIZE;
        buf = buf + BLOCKSIZE;

        write_blocks( db_index, 1, (void *)temp_buffer );
    }

    root.dir_table[ root_index ].size += nlength;
    fdt[ fileID ].wr_ptr = root.dir_table[ root_index ].size;

    write_blocks( 0, 1, (void *)&root );
    write_blocks( 1, 1, (void *)&FAT );
    write_blocks(MAXDISK-1, 1, (void *)&freebl);

	return 0;
}

int sfs_read(int fileID, char *buf, int length)
{
    if (opened_files <= fileID && fdt[ fileID ].opened == 0 ) {
        fprintf(stderr, "No such file %d is opened\n", fileID);
        return 0;
    }

    char * buf_ptr = buf;

    char temp_buffer[BLOCKSIZE];

    int root_index = fdt[fileID].root_index;
    int fat_index = root.dir_table[ root_index ].fat_index;
    int db_index;
    db_index = FAT.fat_nodes[ fat_index ].db_index;

    int read_pointer = fdt[ fileID ].rd_ptr % BLOCKSIZE;

    if (fdt[ fileID ].rd_ptr == 0){
	read_pointer = 0;
    }
    else if (fdt[ fileID ].rd_ptr % BLOCKSIZE == 0){
        read_pointer = -1;
    }

    int rd_block_pointer = fdt[ fileID ].rd_ptr / BLOCKSIZE;

    while (rd_block_pointer > 0) {
        if ( FAT.fat_nodes[ fat_index ].next != -1 ) {
            fat_index = FAT.fat_nodes[ fat_index ].next;
            --rd_block_pointer;
        }
    }

    db_index = FAT.fat_nodes[ fat_index ].db_index;

    read_blocks( db_index, 1, temp_buffer );
    memcpy(buf_ptr, (temp_buffer + read_pointer), (BLOCKSIZE - read_pointer));
    length = length - (BLOCKSIZE - read_pointer);
    buf_ptr = buf_ptr + (BLOCKSIZE - read_pointer);
    fat_index = FAT.fat_nodes[ fat_index ].next;

    while( FAT.fat_nodes[ fat_index ].next != -1 && length > 0 && length > BLOCKSIZE ) {
        db_index = FAT.fat_nodes[ fat_index ].db_index;

        read_blocks(db_index, 1, temp_buffer);

        memcpy(buf_ptr, temp_buffer, BLOCKSIZE);

        length -= BLOCKSIZE;

        buf_ptr = buf_ptr + BLOCKSIZE;

        fat_index = FAT.fat_nodes[ fat_index ].next;
    }

    db_index = FAT.fat_nodes[ fat_index ].db_index;
    read_blocks(db_index, 1, temp_buffer);
    memcpy(buf_ptr, temp_buffer, length);


    fdt[ fileID ].rd_ptr += length;
	return 0;
}
