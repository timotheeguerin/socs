package lecture2.lecture02b;

import java.util.Random;

public class AGoodWayToFormatStrings
{
	public static void main(String[] args)
	{
		Random random = new Random();
		double kms = random.nextDouble() * 10;
		double minutes = random.nextDouble() * 50;
		
//		System.out.println("I can run " + kms + " km in " + minutes + " minutes");
		System.out.println(String.format("I can run %.1f km in %.1f minutes", kms, minutes));
	}
}
