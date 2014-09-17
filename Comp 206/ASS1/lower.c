#include <stdio.h>

int main()
{
	char car = 0;
	scanf("%c", &car);
	if(car >= 97 && car <= 122)	{
		car -= 32;
		printf("%c\n",car);
	}
	else{
		printf("Not a lower case letter\n");
	}	
	return 0;
}
