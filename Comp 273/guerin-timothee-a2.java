package median;  //  name of my package

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class findRankK{
	
	static int findRankK( int k, int[] list)
	{
		int size = list.length;
		if(size == 1)
		{
			return list[0];
		}
		else
		{
			int pivot = list[0];
		
			int i1=0,i2=0,i3=0;
			
			for(int i = 0; i < list.length; i++)
			{
				if(pivot > list[i])
				{
					i1 ++;
				}
				else if(pivot < list[i])
				{
					i3 ++;
				}
				else
				{
					i2 ++;
				}
			}
			System.out.println("i1: " + i1 + " i2: " + i2 + " i3: " + i3);
			int list1[] = new int[i1];
			int list2[] = new int[i2];
			int list3[] = new int[i3];
			
			i1 = 0;
			i2 = 0;
			i3 = 0;
			
			for(int i = 0; i < list.length; i++)
			{
				if(pivot > list[i])
				{
					list1[i1] = list[i];
					i1 ++;
				}
				else if(pivot < list[i])
				{
					list3[i3] = list[i];
					i3 ++;
				}
				else
				{
					list2[i2] = list[i];
					i2 ++;
				}
			}
			
			/*for(int i =0; i < list1.length; i++)
			{
				System.out.print(" " + list1[i]);
				
			}
			System.out.println("\nENDLIST1");*/
			
			//System.out.println("k: " +k + " list1: " + list1.length + " list1+2: " + list1.length + list2.length);
			
			if(k < list1.length)
			{
				return findRankK(k, list1);
			}
			else if(k < list1.length + list2.length)
			{
				return pivot;
			}
			else
			{
				return findRankK( k-list1.length-list2.length, list3);
			}

		}
		
	}
}

