#ifndef CIPHER_H_INCLUDED
#define CIPHER_H_INCLUDED
#define SHIFT 1

void encryptAll(char matrix[150][150]);
void encrypt(char str[150]);

void decryptAll(char matrix[150][150]);
void decrypt(char str[150]);

void initialise(char matrix[50][50]);
void displayMatrix(char matrix[50][50]);

#endif // CIPHER_H_INCLUDED
