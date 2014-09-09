package lecture2.lecture02b;

import java.util.ArrayList;
import java.util.HashMap;

import lecture2.lecture02b.Card.Rank;



/**
 * Given 10 random cards, compute the number of sets
 * of two cards or more, without overlapping (e.g., a set
 * of 3 only counts as one, not two (e.g., set of two, set
 * of 3).
 */
public class NumberOfSetCalculator
{
	public static void main(String[] args)
	{
		ArrayList<Card> cards = new ArrayList<Card>();
		Deck deck = new Deck();
		deck.shuffle();
		for( int i = 0; i < 10; i++ )
		{
			cards.add(deck.draw());
		}
		
		// How many sets of size >= 2 do I have?
		// Print the answer on the console
		// Nested loops are not allowed (and generally best avoided)
		
		HashMap<Rank, Integer> map = new HashMap<Rank, Integer>();
		for(Card card : cards)
		{
			int cardinality = 0;
			if(map.containsKey(card.getRank()))
			{
				cardinality = map.get(card.getRank());
			}
			cardinality++;
			map.put(card.getRank(), new Integer(cardinality));
		}
		
		int total = 0;
		for(Integer cardinality : map.values())
		{
			if( cardinality >= 2)
			{
				total++;
			}
		}
		System.out.println(total);
	}
}
