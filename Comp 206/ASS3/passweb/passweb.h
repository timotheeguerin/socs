#ifndef PASSWEB_H_INCLUDED
#define PASSWEB_H_INCLUDED

#include "Menu.h"

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

#endif
