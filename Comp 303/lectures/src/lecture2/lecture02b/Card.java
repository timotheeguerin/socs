package lecture2.lecture02b;

/**
 * An immutable description of a playing card.
 * 1. Enums imitate a subclass of enum.
 * 2. Enums are objects: they have some useful util methods: values and ordinals. This 
 *    obviates the need for case-switching over enums, an error prone practice.
 * 3  You can define speciality functions on enums
 * 4. Enums are reference types: they can be null.
 * 5. Enums are flyweight objects. Rank.ACE == Rank.valueOf("ACE") == Rank.ACE\
  */
public final class Card
{
	/**
	 * Enum type representing the rank of the card.
	 */
	public enum Rank 
	{ ACE, TWO, THREE, FOUR, FIVE, SIX,
		SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }
	
	/**
	 * Enum type representing the suit of the card.
	 */
	public enum Suit 
	{ CLUBS, DIAMONDS, SPADES, HEARTS; 	
	
		public Suit converse()
		{
			return Suit.values()[(ordinal()+2)%4];
		}
	}

// Simulates this code
// public class Suit extends Enum 
//	{
//		private Suit(){}
//		
//		public static final Suit CLUBS = new Suit();
//		public static final Suit DIAMONDS = new Suit();
//		public static final Suit HEARTS = new Suit();
//		public static final Suit SPADES = new Suit();
//	}
	
	private final Rank aRank;
	private final Suit aSuit;
	
	public static void main(String[] args)
	{
		for(Rank rank : Rank.values())
		{
			System.out.println(rank.ordinal());
		}
	}
	
	/**
	 * Create a new card object. 
	 * @param pRank The rank of the card.
	 * @param pSuit The suit of the card.
	 */
	public Card(Rank pRank, Suit pSuit )
	{
		aRank = pRank;
		aSuit = pSuit;
	}
	
	/**
	 * Obtain the rank of the card.
	 * @return An object representing the rank of the card.
	 */
	public Rank getRank()
	{
		return aRank;
	}
	
	/**
	 * Obtain the suit of the card.
	 * @return An object representing the suit of the card 
	 */
	public Suit getSuit()
	{
		return aSuit;
	}
	
	/**
	 * @see Object#toString()
	 * @return See above.
	 */
	public String toString()
	{
		return aRank + " of " + aSuit;
	}


	/**
	 * Two cards are equal if they have the same suit and rank.
	 * @param pCard The card to test.
	 * @return true if the two cards are equal
	 * @see Object#equals(Object)
	 */
	public boolean equals( Object pCard ) 
	{
		if( pCard == null )
		{
			return false;
		}
		if( pCard == this )
		{
			return true;
		}
		if( pCard.getClass() != getClass() )
		{
			return false;
		}
		return (((Card)pCard).getRank() == getRank()) && (((Card)pCard).getSuit() == getSuit());
	}

	/** 
	 * The hashcode for a card is the suit*13 + that of the rank (perfect hash).
	 * @return the hashcode
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
		return getSuit().ordinal() * Rank.values().length + getRank().ordinal();
	}
}
