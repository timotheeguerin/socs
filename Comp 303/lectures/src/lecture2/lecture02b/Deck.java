package lecture2.lecture02b;

import java.util.Collections;
import java.util.Stack;

import lecture2.lecture02b.Card.Rank;
import lecture2.lecture02b.Card.Suit;

/**
 * Models a deck of 52 cards (no joker).
 */
public class Deck {
    private Stack<Card> aCards = new Stack<Card>();

    public Deck() {
        reset();
    }

    public void reset() {
        aCards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                aCards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(aCards);
    }

    public Card draw() {
        return aCards.pop();
    }

    public int size() {
        return aCards.size();
    }
}
