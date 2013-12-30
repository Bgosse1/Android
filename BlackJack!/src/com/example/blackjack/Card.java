package com.example.blackjack;

public class Card {
	int suit;
	int value;
	int[] cardVal = {1,2,3,4,5,6,7,8,9,10,11,12,13};
	int cardVal(){
		int valuefinal = cardVal[value];
		int CardValue;
		if (valuefinal >= 10 ){
			CardValue = 10;	
		}
		else
		{
			CardValue = valuefinal;
		}
		
		if (CardValue ==1)
		{
			CardValue = 11;
		}
		return CardValue;
	}
	
	public String toString(){
		switch(value){
		case 0: 
			return "A";
		case 10: 
			return "J";
		case 11: 
			return "Q";
		case 12: 
			return "K";
		default: 
			return "" + (value+1);
		}
	}
}