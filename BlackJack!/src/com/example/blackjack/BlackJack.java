package com.example.blackjack;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BlackJack extends Activity {
	ArrayList<Card> DealersHand;
	ArrayList<Card> PlayersHand;
	ArrayList<Card> Deck;
	Random value = new Random(); 
	TextView playershand_text;
	TextView dealershand_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		DealersHand = new ArrayList<Card>();
		PlayersHand = new ArrayList<Card>();
		Deck = new ArrayList<Card>();
		Newdeck();
		Card card = getCard();//
		PlayersHand.add(card);
		card = getCard();
		PlayersHand.add(card);
		card = getCard();
		DealersHand.add(card);
		
		
		playershand_text = (TextView) findViewById(R.id.yourhand);
		playerHand();
		dealershand_text = (TextView) findViewById(R.id.dealerhand);
		dealerHand();
		
		final Button hit;
		final Button stay;
		Button clear;
		hit = (Button) findViewById(R.id.hit);
		stay = (Button) findViewById(R.id.stay);
		clear = (Button) findViewById(R.id.button1);
		
		hit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Get a new card from deck (using generate)
				Card card = getCard();
				PlayersHand.add(card);
				playerHand();
				if (ValueOfHand(PlayersHand) > 21){
					Toast.makeText(BlackJack.this, "You lose!", Toast.LENGTH_LONG).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
			}			
		});
		
		clear.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
			hit.setEnabled(true);
			stay.setEnabled(true);
			PlayersHand = new ArrayList<Card>();
			Card card = getCard();
			PlayersHand.add(card);
			card = getCard();
			PlayersHand.add(card);
			playerHand();
			DealersHand = new ArrayList<Card>();
			DealersHand.add(card);
			dealerHand();
			}
		});
		
		stay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				while (ValueOfHand(DealersHand) <= 17){
					Card card = getCard();
					DealersHand.add(card); 
				}
				dealerHand();
				int dealer = ValueOfHand(DealersHand);
				int	player = ValueOfHand(PlayersHand);
				
				if (dealer == player){
					Toast.makeText(BlackJack.this, "Its a Tie!", Toast.LENGTH_LONG).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
				else if(dealer > 21)
				{
					Toast.makeText(BlackJack.this,"Dealer Busted You win!", Toast.LENGTH_SHORT).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
				else if (dealer > player && dealer < 21){
					Toast.makeText(BlackJack.this, "You lose!", Toast.LENGTH_LONG).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
				else if(dealer == 21){
					Toast.makeText(BlackJack.this, "You lose!", Toast.LENGTH_LONG).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
				else if(player == 21){
					Toast.makeText(BlackJack.this, "Black Jack You win!", Toast.LENGTH_LONG).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
				else if (player == 21 && dealer == 21){
					Toast.makeText(BlackJack.this, "Dealer Wins because its the rules", Toast.LENGTH_LONG).show();
					stay.setEnabled(false);
					hit.setEnabled(false);
				}
			}
		});
	}

	
	
	private int ValueOfHand(ArrayList<Card> hand){
		int sum = 0;
		for(int i =0; i < hand.size(); i ++){
			Card card = hand.get(i);
			int val = card.cardVal();
			sum = sum + val;

		}
		
		for(int i = 0; i < hand.size(); i ++){
			Card card = hand.get(i);
			if ( sum > 21 && card.value == 0 ){
				sum = sum - 10;
			}
		}
		
		return sum;
	}
	
	
	private void dealerHand(){
		String your_cards="";
		your_cards="";
		for(int i = 0; i  < DealersHand.size(); i++){
			Card card = DealersHand.get(i);
			
			your_cards = your_cards + " " + card;
		}
		your_cards += " (Dealers Hand: " + ValueOfHand(DealersHand) + ")";
		dealershand_text.setText(your_cards);

	}
	
	private void playerHand(){
		String your_cards="";
		for(int i = 0; i  < PlayersHand.size(); i++){
			Card card = PlayersHand.get(i);
			
			your_cards = your_cards + " " + card;
		}
		your_cards += " (Your Hand:  " + ValueOfHand(PlayersHand) + ")";
		playershand_text.setText(your_cards);
	}
	

	private Card getCard(){
		int num_cards = Deck.size();
		int rand_pos = value.nextInt(num_cards);
		Card card = Deck.remove(rand_pos);
		return card;
	}

	
	
	private void Newdeck() {
		for(int suit = 0; suit < 4; suit++){
			for(int rank = 0; rank < 13; rank ++){
				Card card = new Card();
				card.suit = suit;
				card.value = rank;
				Deck.add(card);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}
}
