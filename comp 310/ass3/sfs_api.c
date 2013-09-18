#include "sfs_api.h"
#include "disk_emu.h"

root_dir root;
fat_table fat;
freeblocklist freebl;

static int opened_files = 0;
static int BLOCK_SIZE;

void mksfs(int fresh)
{
	BLOCK_SIZE = max(sizeof(root_dir), sizeof(fat_table));

	if (fresh) {
		init_fresh_disk("root.sfs", BLOCK_SIZE, MAX_DISK);

		int i;

		root.next_cursor = 0;
		for(i = 0; i < MAX_DISK; i++) 
		{
			freebl.list[i] = 0;
		}
		for(i = 0; i < MAX_DISK; i++) 
		{
			fat.table[i].db_index = EMPTY;
			fat.table[i].next = EMPTY;
		}
		fat.next_cursor = 0;

		write_blocks( 0, 1, (void *)&root );
		write_blocks( 1, 1, (void *)&fat );
		write_blocks(MAX_DISK-1, 1, (void *)&freebl);
	} else {
		init_disk("root.sfs", BLOCK_SIZE, MAX_DISK);
		read_blocks( 0, 1, (void *)&root );
		read_blocks( 1, 1, (void *)&fat );
		read_blocks( MAX_DISK-1, 1, (void *)&freebl );
	}
}

void sfs_ls()
{
	int i;
	for (i = 0; i < MAX_FILE; i++) 
	{
		if(root.table[i].size > 0)
		{
			int filesize = root.table[i].size / 1000;
			printf("%s  %dKB  %s", root.table[i].filename, 
				filesize, ctime(&root.table[i].timestamp) );
		}
	}
}

int sfs_open(char *name)
{
	int fileID = getFileIndex(name);
	//Check if the file with the name is already open then close
	if ( fileID != -1) {
		if(root.table[fileID ].fd.opened == 1)
		{
			printf("Error file '%s' already open!\n", name);
			return -1;
		}
		else
		{
			root.table[fileID ].fd.opened = 1;
			return 0;
		}

	}


	//Setup data
	fileID = opened_files;
	strcpy( root.table[fileID ].filename, name );
	root.table[fileID ].fd.opened = 1;
	root.table[fileID ].fd.rd_ptr = 0;

	opened_files++;
	root.table[fileID ].fd.wr_ptr = 0;

	fat.table[fat.next_cursor].db_index = -1;
	int i;
	for(i = 2; i < MAX_DISK - 1; i++) {
		if (freebl.list[i] == 0) {
			freebl.list[i] = 1;
			fat.table[fat.next_cursor].db_index = i;
			break;
		}
	}

	fat.table[fat.next_cursor].next = -1;
	printf("Open 4.1 : %d\n", root.next_cursor);
	strcpy( root.table[ root.next_cursor ].filename, name ); 
	printf("Open 4.2\n");
	root.table[ root.next_cursor ].fat_index = fat.next_cursor;
	root.table[ root.next_cursor ].size = 0;
	root.table[ root.next_cursor ].timestamp = time(NULL);

	fat.next_cursor += 1;
	if (fat.next_cursor >= MAX_DISK) {

		for (i = 0; i < MAX_DISK; i++) {
			if (fat.table[i].db_index == EMPTY) {
				break;
			}
		}
		fat.next_cursor = i;
		if ( i == MAX_DISK ) {
			exit(1);
		}
	}
	root.next_cursor ++;
	if (root.next_cursor >= MAX_FILE) {

		for (i = 0; i < MAX_FILE; i++) {
			if (root.table[i].size > 0) {
				break;
			}
		}
		root.next_cursor = i;
		if (i == MAX_FILE) {
			exit(1);
		}
	}
	write_blocks( 0, 1, (void *)&root );
	write_blocks( 1, 1, (void *)&fat );
	write_blocks(MAX_DISK-1, 1, (void *)&freebl);

	return fileID;

}

int sfs_close(int fileID)
{
	if (opened_files <= fileID) {
		fprintf(stderr, "No such file %d", fileID);
		return -1;
	} else {
		root.table[fileID ].fd.opened = 0;
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

	int fat_index = root.table[ fileID ].fat_index;
	int db_index = fat.table[ fat_index ].db_index;

	//Move to the end of file
	while( fat.table[fat_index].next != -1 ) {
		db_index = fat.table[fat_index].db_index;
		fat_index = fat.table[fat_index].next;
	}

	char tmp_buf[BLOCK_SIZE];

	read_blocks(db_index, 1, (void *)tmp_buf);

	int write_pointer = root.table[fileID ].fd.wr_ptr % BLOCK_SIZE;

	if (root.table[fileID ].fd.wr_ptr == 0){
		write_pointer = 0;
	}
	else if (root.table[fileID ].fd.wr_ptr % BLOCK_SIZE == 0){
		write_pointer = -1;
	}

	//If we have free space in the current block copy there 
	if ( write_pointer != -1 ) {
		//Get the lenght of the buffer to write to be the minimum of either the remaining block size or the the lenght
		int lenghtToWrite = min( (BLOCK_SIZE - write_pointer), length);
		memcpy( (tmp_buf + write_pointer), buf, lenghtToWrite);
		write_blocks( db_index, 1, (void *)tmp_buf );
		length -= lenghtToWrite;
		buf += lenghtToWrite;
	}
	while (length > 0) 
	{
		int lenghtToWrite = min(BLOCK_SIZE, length);
		memcpy( tmp_buf, buf, lenghtToWrite );

		db_index = -1;

		int i;
		for(i = 2; i < MAX_DISK - 1; i++) {
			if (freebl.list[i] == 0) {
				freebl.list[i] = 1;
				db_index = i;
				break;
			}
		}

		fat.table[ fat_index ].next = fat.next_cursor;
		fat.table[ fat.next_cursor ].db_index = db_index;
		fat_index = fat.next_cursor;
		fat.table[ fat_index ].next = -1;

		fat.next_cursor += 1;
		if (fat.next_cursor >= MAX_DISK) {
			int z;
			for (z = 0; z < MAX_DISK; z++) {
				if (fat.table[z].db_index == EMPTY) {
					break;
				}
			}
			fat.next_cursor = z;
		}

		length = length - lenghtToWrite;
		buf += lenghtToWrite;
		printf("lenght: %d\n", length);

		write_blocks( db_index, 1, (void *)tmp_buf );
	}

	root.table[ fileID ].size += nlength;
	root.table[fileID ].fd.wr_ptr = root.table[ fileID ].size;

	write_blocks( 0, 1, (void *)&root );
	write_blocks( 1, 1, (void *)&fat );
	write_blocks(MAX_DISK-1, 1, (void *)&freebl);

	return nlength - length;
}

int sfs_read(int fileID, char *buf, int length)
{
	int nlength = length;
	if (opened_files <= fileID && root.table[fileID].fd.opened == 0 ) {
		fprintf(stderr, "No such file %d is opened\n", fileID);
		return 0;
	}

	char * buf_ptr = buf;

	char tmp_buf[BLOCK_SIZE];

	int fat_index = root.table[fileID].fat_index;
	int db_index;
	db_index = fat.table[fat_index].db_index;

	int read_pointer = root.table[fileID ].fd.rd_ptr % BLOCK_SIZE;

	if (root.table[fileID ].fd.rd_ptr == 0){
		read_pointer = 0;
	}
	else if (root.table[fileID ].fd.rd_ptr % BLOCK_SIZE == 0){
		read_pointer = -1;
	}

	int rd_block_pointer = root.table[fileID].fd.rd_ptr / BLOCK_SIZE;

	while (rd_block_pointer > 0) {
		if ( fat.table[ fat_index ].next != -1 ) {
			fat_index = fat.table[fat_index].next;
			--rd_block_pointer;
		}
	}
	db_index = fat.table[fat_index].db_index;

	read_blocks( db_index, 1, tmp_buf );
	int lengthToRead = min((BLOCK_SIZE - read_pointer), length);
	memcpy(buf_ptr, (tmp_buf + read_pointer), lengthToRead);

	length -= lengthToRead;

	buf_ptr += (BLOCK_SIZE - read_pointer);

	fat_index = fat.table[ fat_index ].next;
	printf("READ: %d, %d\n", fat.table[ fat_index ].next,length);
	while( fat.table[ fat_index ].next != -1 && length > 0) {
		db_index = fat.table[ fat_index ].db_index;

		read_blocks(db_index, 1, tmp_buf);
		lengthToRead = min(BLOCK_SIZE, length);
		memcpy(buf_ptr, tmp_buf, lengthToRead);

		length -= lengthToRead;
		printf("%d - %d\n", length, lengthToRead);
		buf_ptr += BLOCK_SIZE;

		fat_index = fat.table[ fat_index ].next;
		printf("READ: %d, %d\n", fat.table[ fat_index ].next,length);
	}


	root.table[fileID ].fd.rd_ptr += length;
	return nlength -length;
}


int getFileIndex(char* name)
{
	int fileID = -1;
	int i;
	for(i = 0; i < MAX_FILE; i++) {
		if ( strcmp( root.table[i].filename, name ) == 0 ) {
			fileID = i;
			break;
		}
	}
	return fileID;
}
