#include <stdlib.h>
#include <stdio.h>

/* Size of the string to match against.  A small size is used here for debugging; you will need
   a _much_ larger size for any performance testing . */
#define STRINGSIZE 160000000

/* Probability of repeating a character */
#define PROB rand()%1000<197

/* Construct a sample string to match against.  Note that this uses characters, encoded in ASCII,
   so to get 0-based characters you'd need to subtract 'a'. */
char *buildString() {
    int i;
    char *s = (char *) malloc(sizeof(char) * (STRINGSIZE+1));
    if (s == NULL) {
        printf("\nOut of memory!\n");
        exit(1);
    }
    int max = STRINGSIZE;

    /* seed the rnd generator (use a fixed number rather than the time for testing) */
    srand((unsigned int) time(NULL));

    /* And build a long string that might actually match */
    int j = 0;
    while (j < max) {
        s[j++] = 'a';
        while (PROB && j < max - 2)
            s[j++] = 'a';
        s[j++] = 'b';
        while (PROB && j < max - 1)
            s[j++] = 'b';
        s[j++] = (rand() % 2 == 1) ? 'c' : 'd';
        while (((PROB && j < max) || max - j < 3) && j < max)
            s[j++] = (rand() % 2 == 1) ? 'c' : 'd';
    }
    s[j] = '\0';
    return s;
}

/* Debug output */
void printString(char *s) {
    int i;
    for (i = 0; i < STRINGSIZE; i++) {
        putchar(s[i]);
    }
}