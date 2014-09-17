package lecture2.lecture02b;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PokerHandGenerator extends JFrame implements ActionListener
{
	private static final int MARGIN = 10;
	
	private static final String P_TWO_PAIRS = "^(two|2)\\s+pairs$";
	
	private List<JLabel> aCards = new ArrayList<JLabel>();
	private JPanel aDisplay = new JPanel();
	private Random aRandom = new Random();
	
	private JTextField aInput = new JTextField();
	
	public PokerHandGenerator()
	{
		super("Poker hand generator");
		ImageIcon image = CardImages.getBack();
		for( int i = 0; i < 5; i++ )
		{
			JLabel label = new JLabel();
			label.setIcon(image);
			aDisplay.add(label);
			aCards.add(label);
		}
			
		setLayout(new BorderLayout(MARGIN, MARGIN));
		add(aDisplay, BorderLayout.CENTER);
		add(aInput, BorderLayout.SOUTH);
		aInput.addActionListener(this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Card[] cards = null;
	
		if( twoPairText(aInput.getText()))
		{
			cards = twoPairs();
		}
		else
		{
			cards = anything();
		}
		
		int i = 0;
		for(JLabel label : aCards)
		{
			label.setIcon( CardImages.getCard(cards[i]));
			i++;
		}
		aInput.setText("");
		
	}
	
	public boolean twoPairText(String pText)
	{
	   Pattern p = Pattern.compile(P_TWO_PAIRS,Pattern.CASE_INSENSITIVE);
	   Matcher m = p.matcher(pText);
	   return m.matches();
	}

	private Card[] twoPairs()
	{
		return new Card[0]; // TODO You probably want to change this
	}
	
	private Card[] anything()
	{
		Deck deck = new Deck();
		deck.shuffle();
		Card[] lReturn = new Card[5];
		for(int i = 0; i < 5; i ++)
		{
			lReturn[i] = deck.draw();
		}
		return lReturn;
	}
	
	public static void main(String[] args)
	{
		new PokerHandGenerator();
	}
}
