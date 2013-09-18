#ifndef CIPHER_H_INCLUDED
#define CIPHER_H_INCLUDED

#include "stdio.h"
#include "string.h"


void encryptAll(char matrix[150][150]);
void encrypt(char str[150]);

void decryptAll(char matrix[150][150]);
void decrypt(char str[150]);

void initialise(char matrix[150][150]);

extern int SHIFT;
#endif // CIPHER_H_INCLUDED
