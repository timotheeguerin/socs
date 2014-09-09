package lecture2.lecture02b;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * GUI application to demonstrate the card utilities at work.
 * You do not need to understand how this code works: it will be explained 
 * later in the course.
 */
@SuppressWarnings("serial")
public class CardDemo extends JFrame implements ActionListener
{
	private static final int MARGIN = 10;
	private static final int WIDTH = 180;
	private static final int HEIGHT = 200;
	
	private Deck aDeck = new Deck();
	private JLabel aImage = new JLabel();
	private JLabel aText = new JLabel();
	private JButton aButton = new JButton("Next");
	
	private long aLastClick = System.currentTimeMillis();
	
	/**
	 * Demo.
	 */
	public CardDemo()
	{
		super("Card Demo");
		aDeck.shuffle();
		aImage.setIcon(CardImages.getBack());
		aText.setText("Click the button to start");
		aImage.setHorizontalAlignment(JLabel.CENTER);
		aText.setHorizontalAlignment(JLabel.CENTER);
		setLayout(new BorderLayout(MARGIN, MARGIN));
		add(aText, BorderLayout.NORTH);
		add(aImage, BorderLayout.CENTER);
		add(aButton, BorderLayout.SOUTH);
		aButton.addActionListener(this);
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * @param pArgs Nothing used
	 */
	public static void main(String[] pArgs)
	{
		new CardDemo();
	}

	@Override
	public void actionPerformed(ActionEvent pEvent)
	{
		if( aDeck.size() == 0 )
		{
			aDeck.shuffle();
		}
		Card lCard = aDeck.draw();
		aImage.setIcon(CardImages.getCard(lCard));
		aText.setText(lCard.toString());
		long now = System.currentTimeMillis();
		double time =  (double)(now-aLastClick)/1000;
		aButton.setText(String.format("%4.1fs", time));
		aLastClick = now;
	}
}
