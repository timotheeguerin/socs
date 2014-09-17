package lecture2.lecture02a;

import java.util.Random;

// Hi! Make sure you sit somewhere where you can read this text.

// Write a program that will offer a prediction for tomorrow's weather
// as one of: Rainy, Cloudy, or Sunny. 
// The prediction does not have to be accurate, but it should change
// over time.

public class WeatherPredictor
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String[] weatherValue = {"Rainy", "Cloudy", "Sunny"};
		System.out.println(weatherValue[new Random().nextInt(weatherValue.length)]);
	}
}
