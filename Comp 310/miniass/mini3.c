#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <bsd/stdlib.h>

int main()
{
	int processNb = arc4random_uniform(6) + 5;
	int rscrNb = 4;
	int i;
	int j;
	int k;

	int max[processNb][rscrNb];
	int content[processNb][rscrNb];
	int need[processNb][rscrNb];
	int rscrAvailable[rscrNb];
	int req[processNb][rscrNb];
	int finish[processNb];

	//init 
	printf("Maximum Matrix: \n");
	for(i = 0; i < processNb; i++)
	{
		for(j = 0; j < rscrNb; j++)
		{
			//Setup maxiuum ressource requirement
			max[i][j] = arc4random_uniform(8) + 5;

			//Process start with nothing
			content[i][j] = 0;

			//Process want the maximum possible
			need[i][j] = max[i][j];
			printf("%-4d", max[i][j]);
		}
		printf("\n");
	}

	
	for(j = 0; j < rscrNb; j++)
	{
		int sum = 0;
		for(i = 0; i < processNb; i++)
		{
			sum += max[i][j];
		}
		rscrAvailable[j] = ceil(sum * 0.6);
	}

	for(k = 0; k < processNb; k++)
	{
		finish[k] = 0;
	}

	// Request Generation, Check and Allocation
	int loop = 1;
	int safe = 1;
	int check = 1;
	while(loop)
	{
		loop = 0;
		for(i = 0; i < processNb; i++)
		{
			for(j = 0; j < rscrNb; j++)
			{
				//Generate random request
				req[i][j] = arc4random_uniform(need[i][j]) + 1;
				
				//If the request is valid else ignore
				if(req[i][j] <= rscrAvailable[j] && req[i][j] <= need[i][j])
				{
					//Update the available ressources
					rscrAvailable[j] -= req[i][j];

					//Update ressources proceess have and need
					content[i][j] += req[i][j];
					need[i][j] -= req[i][j];
					
					//Check if we are safe
					int temp = rscrAvailable[j];
					while(check)
					{
						check = 0;
						for(k = 0; k < processNb; k++)
						{

							//If the process is not done and we need less than temp
							if(!finish[k] && need[k][j] <= temp)
							{
								temp += content[k][j];
								finish[k] = 1;
								check = 1;
								break;
							}
						}
					}
					
					safe = 1;
					for(k = 0; k < processNb; k++)
					{
						if(!finish[k])
						{
							safe = 0;
						}
					}
					

					//If its not safe revert
					if(!safe)
					{
						rscrAvailable[j] += req[i][j];
						content[i][j] -= req[i][j];
						need[i][j] += req[i][j];
					}
					else
					{
						loop = 1;
					}
				}

				//Update the need
				need[i][j] = max[i][j] - content[i][j];
			}
		}
	}

	// Finish Simulation
	printf("Ressources allocated: \n");
	for(i = 0; i < processNb; i++)
	{
		for(j = 0; j < rscrNb; j++)
		{
			printf("%-4d", content[i][j]);
		}
		printf("\n");
	}

	return 0;
}
