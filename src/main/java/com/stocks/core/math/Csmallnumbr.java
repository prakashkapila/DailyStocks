package com.stocks.core.math;

public class Csmallnumbr {
	static double negmacprec = 0.0, smallnumber = 0.0;
	static private int radix = 0;

	private static void cRad() {
		double a = 1.0d, b = 1.0d, tmp1, tmp2;
		do {
			a += a;
			tmp1 = a + 1.0d;
			tmp2 = tmp1 - a;

		} while (radix == 0);
		while (radix == 0) {
			b += b;
			tmp1 = a + b;
			radix = (int) (tmp1 - a);
		}
	}

	private static void snum() {
		double floatRadix = getRadix();
		double invRadix = 1.0 / floatRadix;
		double fullmantessa = 1.0d - floatRadix * getnegmachineprec();
		while (fullmantessa != 0.0) {
			smallnumber = fullmantessa;
			fullmantessa *= invRadix;

		}
	}

	public static double getnegmachineprec() {
		if (negmacprec == 0) {
			compnegprec();
		}
		return negmacprec;
	}

	private static int getRadix() {
		if (radix == 0)
			cRad();
		return radix;
	}

	private static void compnegprec() {
		double frad = getRadix();
		double invrad = 1.0 / frad;
		negmacprec = 1.0;
		double tmp = 1.0 - negmacprec;
		while (tmp - 1.0d != 0.0) {
			negmacprec *= invrad;
			tmp = 1 - negmacprec;
		}
	}

}
