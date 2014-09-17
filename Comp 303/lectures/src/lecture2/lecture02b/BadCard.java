package lecture2.lecture02b;

/**
 * Demonstrates poor usage of symbolic constants.
 */
public class BadCard
{
	public static final int CLUBS 		= 0;
	public static final int DIAMONDS 	= 1;
	public static final int HEARTS 		= 2;
	public static final int SPADES 		= 3;
	
	private int aSuit = CLUBS;
	private int aRank = 0;
	
	public BadCard( int pSuit, int pRank )
	{
		aSuit = pSuit;
		aRank = pRank;
	}
	
	public static void main(String[] args)
	{
		new BadCard(28, 3000);
		new Player("Bob", CLUBS);
		int total = CLUBS + DIAMONDS;
	}
}

class Player
{
	private int aAmount = 0;
	private String aName = "Default";
	
	public Player(String pName, int pAmount)
	{
		aName = pName;
		aAmount = pAmount;
	}
}