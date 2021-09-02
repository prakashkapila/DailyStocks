package com.stocks.core.math;

public abstract class ContFract {
	double prec = 1E-30, nume, denom, interim, lentzval, oldans, vars[] = new double[2];
	int n = 1;

	abstract void computeFraction(int n);

	public void setInitial(double num, double deno) {
		nume = num;
		denom = deno;
	}

	public void setFract(double init) {
		lentzval = init;
	}

	public double getFract() {
		return lentzval;
	}

	public double floorValue(double val) {
		Math.abs(val);
		return Math.floor(val);
	}

	public void evaluateFraction() // lentz method
	{
		int i = n;
		while (Math.abs(oldans - 1.0) > prec)// terminating criteria
		{
			computeFraction(++i);
			// set up the a, b, x and initial values for
			denom = Math.floor((vars[1] + vars[0] * denom));
			denom = 1 / denom;
			nume = Math.floor((vars[1] + (vars[0] / nume)));
			oldans = nume * denom;
			lentzval *= nume * denom;
		}
		setFract(lentzval);
	}
}
