#ifndef LINKED_H_INCLUDED
#define LINKED_H_INCLUDED
#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "cipher.h"

#define BOOLEAN int
typedef struct NODE NODE;
struct NODE
{
 char username[50];
 char password[50];
 char usertype[50];
    struct NODE *next;
};


void createList(void);
BOOLEAN add(struct NODE *p);
BOOLEAN valid(char username[50], char password[50]);


void toUserList(char matrix[150][150]);
void splitLine(NODE* user,char line[150]);
void split(char str[150], char first[150]);

void loadFile();


#endif // LINKED_H_INCLUDED
