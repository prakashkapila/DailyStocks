package com.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*Starting with $1 of capital for an investment opportunity, 
 * you can choose a fixed proportion – F – 
of your capital to bet on a fair coin toss repeatedly for 1000 tosses.

Your return is double your bet for heads and you lose your bet for tails. 

For example, if F = 1/4, for the first toss you bet $0.25, and if heads comes up you 
win $0.5 and so then have $1.5. 
You then bet $0.375 and if the second toss is tails, you have $1.125 (1.5-0.375).

Choosing F to maximize your chances of having at least $1,000,000,000 
after 1000 flips, what is the chance that you become a billionaire?



*/
public class SpeculationReturns {
	enum FLIP {HEAD,TAILS};
	final int TOSS=0,TOTAL=1;
	final double HEADPROPORTION=.99,TAILPROPORTION=.01;
	FLIP toss = FLIP.HEAD;// initialize with fail case
	double totalMoney =0.0;
 	final int THRESHOLD = 5;// every heads for 4 tails.
 	final double specMoney = 1000000000.0;
	
	/**Probability of toss is 50% heads & 50% tails, 
	 * for fair coin toss every time the flip will be different**/
	private FLIP getFairCoinToss(FLIP tossed) {
		return tossed == FLIP.HEAD ? FLIP.TAILS:FLIP.HEAD;
	}
	/**If heads you get twice the money, if tails you loose the money**/
	private double calculateBet(FLIP toss,double betMoney) 
	{
		return FLIP.HEAD == toss ?  betMoney*2 : -betMoney;
 	}
	/**Allocation of the total money that is on the bet.**/
	private double chooseProportion(double currMoney,double percent)
	{
		System.out.println("Choosing proportion "+percent+" of total Money "+currMoney);
		return currMoney * percent;
	}
	public double chooseProportion(FLIP toss,double currtotal)
	{
		return toss == FLIP.HEAD ?
				chooseProportion(currtotal,HEADPROPORTION):
					chooseProportion(currtotal,TAILPROPORTION);
	}
	public double calcCurrTotal(FLIP toss,double currMoney) {
		double ret = 0.0;
		ret = chooseProportion(toss,currMoney);
		ret = calculateBet(toss, ret);
		currMoney +=ret;
		return currMoney;
	}
	
	private Object[] calcTotal(double currtotal) {
 		double betMoney=0.0; 
		toss= getFairCoinToss(toss);
		System.out.println("FLIP NOW IS "+toss.toString());
		betMoney = toss == FLIP.HEAD ?
				chooseProportion(currtotal,HEADPROPORTION):
					chooseProportion(currtotal,TAILPROPORTION); 
 		 currtotal += calculateBet(toss, betMoney);
 		 return new Object[] {toss,currtotal};
	}
	public void speculateReturns(double maxMoney) {
	 	Double doub =1.0;
 		int i=0;
		do {
			Object[] specResult = calcTotal(doub);
			doub = (Double)(specResult[TOTAL]);
			System.out.println("After iteration "+ i++ +" total Money ="+doub);
		}while(maxMoney >doub);
		this.totalMoney = doub;
		StringBuilder output = new StringBuilder();
		output.append("By using ").append(HEADPROPORTION)
		.append(" Whenever heads result AND ").append("Choosing ")
		.append(TAILPROPORTION)
		.append(" Whenever tails result" )
		.append("We are able to reach more than 1 BILLION $")
		.append( " after ")
		.append( i-1)
		.append(" Iterations");
		System.out.println(output.toString());
	}

	private List<Integer> getRandomInts(int number)
	{
		Random rand = new Random();
		int numb = rand.nextInt();
		ArrayList<Integer> randoms = new ArrayList<Integer>();
		int ctr=0;
		while(ctr++ <number) {
		while(randoms.contains(numb)) {
			numb = rand.nextInt();
		}
		randoms.add(numb);
		}
		return randoms;
	}
	public List<FLIP> generateRandomTosses(int number) {
		List<Integer> rands = getRandomInts(number);
		List<FLIP> flips = new ArrayList<FLIP>();
		rands.forEach(numb->{
			if(numb % THRESHOLD == 0) {
				flips.add(FLIP.HEAD);
			}
			else
			{ flips.add(FLIP.TAILS);}
		});
		return flips;
	}
	
	private boolean runSpeculations(int numb) {
		List<FLIP> rands = generateRandomTosses(numb);
		double tot = 1.0;
		for(int i=0;i<numb;i++)
		{
			tot = calcCurrTotal(rands.get(i), tot);
			if(tot > specMoney)
			{

				System.out.println("After iterations "+numb+" total is "+tot);
				return true;
			}
		}
		System.out.println("After "+numb+" total is "+tot);
		return false;
	}
	
	public void runSpeculation() {
		int runs = 50;
		boolean resp = runSpeculations(runs);
		while(!resp) {
			runs +=50;
			resp = runSpeculations(runs);
		}
	}
	
	public static void main(String arg[])
	{
		SpeculationReturns returns = new SpeculationReturns();
		//returns.speculateReturns(1000000000);
		returns.runSpeculation();
	}
}
