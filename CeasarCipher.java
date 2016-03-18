// Name: Tyler Gauntlett
// NID: ty340586
// Course: COP3503C-16Spring 0001
// Assignment: AuotCeasarCipher

public class CeasarCipher {

	public final int OFFSET = 97;
	public final int ALPHASIZE = 26;

	public static void main(String[] args) {

		CeasarCipher cipher = new CeasarCipher();
		
		String decodemessage = "myxqbkdevkdsyxc yx mywzvodsxq dro ohkw!";

		System.out.println(cipher.crack(decodemessage));

	}

	public int let2nat(char letter) {

		int letterToInt = (int) letter;

		return letterToInt - OFFSET;
	}

	public char nat2let(int number) {
		int totalNumber = number + OFFSET;

		return (char) totalNumber;
	}

	public char shift(int shiftAmount, char letter) {
		// Convert to a number using a previously made function.
		int letterToInt = let2nat(letter);

		// Check if the number is between the range of acceptable ints.
		if (letterToInt >= 0 && letterToInt <= 25) {
			// Shift in my amount.
			int modInt = (letterToInt + shiftAmount);

			// When decoding, if the shift amount is greater than the
			// amount char decimal value, it will result in a negative
			// number. To compensate, add the number to the size of alphabet.
			if (modInt < 0) {
				modInt = ALPHASIZE + modInt;

				// Account for wrap around.
				modInt = modInt % ALPHASIZE;

				// Offset mod number.
				modInt = modInt + OFFSET;

				// Cast to char and return.
				return (char) modInt;
			}

			// Account for wrap around.
			modInt = modInt % ALPHASIZE;

			// Offset mod number.
			modInt = modInt + OFFSET;

			// Cast to char and return.
			return (char) modInt;
		}

		// If it is not acceptable, return as is.
		return letter;
	}

	public String encode(int shiftAmount, String message) {

		StringBuilder sb = new StringBuilder();

		// Loop through the string.
		for (int i = 0; i < message.length(); i++) {
			// Break off the chars.
			char temp = message.charAt(i);
			// Run through shift function by positive shift amount
			temp = shift(shiftAmount, temp);
			// Add to the string builder.
			sb.append(temp);

		}

		// Return string builder.
		return sb.toString();
	}

	public String decode(int shiftAmount, String message) {

		StringBuilder sb = new StringBuilder();
		// Loop through the string.
		for (int i = 0; i < message.length(); i++) {
			// Break off the chars.
			char temp = message.charAt(i);
			// Run through shift function by negative shift amount.
			temp = shift(-shiftAmount, temp);
			// Add to the string builder.
			sb.append(temp);

		}
		// Return string builder.
		return sb.toString();
	}

	public int lowers(String countLower) {
		int count = 0;

		for (int i = 0; i < countLower.length(); i++) {
			char tempChar = countLower.charAt(i);

			int charValue = let2nat(tempChar);

			// Check if the number is between the range of acceptable ints.
			if (charValue >= 0 && charValue <= 25) {

				count++;
			}
		}

		return count;
	}

	public int count(char letter, String word) {
		int count = 0;

		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == letter)
				count++;
		}

		return count;
	}

	public double percent(double divisor, double dividend) {
		// Divide and multiple by 100 to get the percent.
		double unroundedAnswer = (divisor / dividend) * 100.0;
		// Round off in the 5th decimal place.
		double roundOff = Math.round(unroundedAnswer * 10000.0) / 10000.0;

		return roundOff;
	}

	public double[] freqs(String word) {
		double freqForEachChar[] = new double[ALPHASIZE];
		// Run a loop based on the size of the alphabet.
		for (int i = 0; i < ALPHASIZE; i++) {
			// Alternate through all lower case letters by off setting
			// and adding by i.
			char alternatingChar = (char) (OFFSET + i);
			// Get the frequency in which each letter appears in the given word.
			int freqOfChar = count(alternatingChar, word);
			// Find the percent of frequency by casting everything to doubles.
			double percentOfFreq = percent((double) freqOfChar, (double) word.length());
			// Place in array.
			freqForEachChar[i] = percentOfFreq;

		}

		return freqForEachChar;
	}

	public double[] rotate(int amount, double[] freqArray) {
		// Shift the array left by a given amount.
		for (int i = 0; i < amount; i++) {
			if (freqArray == null || freqArray.length <= 1) {
				return freqArray;
			}
			double start = freqArray[0];
			System.arraycopy(freqArray, 1, freqArray, 0, freqArray.length - 1);
			freqArray[freqArray.length - 1] = start;
		}

		return freqArray;
	}

	public double chisqr(double[] observedFreq) {
		double[] table = { 8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2.0, 6.1, 7.0, 
			0.2, 0.8, 4.0, 2.4, 6.7, 7.5, 1.9, 0.1, 6.0,
			6.3, 9.1, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1 };
		
		double total = 0;

		for (int i = 0; i < ALPHASIZE; i++) {
			double divisor = (observedFreq[i] - table[i]);

			divisor = divisor * divisor;

			total = total + (divisor / table[i]);
		}

		return total;
	}

	public int position(double value, double[] freqArray) {
		for (int i = 0; i < freqArray.length; i++) {
			if (freqArray[i] == value)
				return i;
		}

		return -1;
	}

	public  String crack(String breakWord) {
		double freqForEachChar[] = new double[ALPHASIZE];
		double minChi = (double) Integer.MAX_VALUE;
		String decode = "";
		int smallestIndex = -1;

		for (int i = 0; i < ALPHASIZE; i++) {

			freqForEachChar = rotate(i, freqs(breakWord));

			double tempChi = chisqr(freqForEachChar);

			if (tempChi < minChi) {
				minChi = tempChi;
				smallestIndex = i;
			}
		}

		decode = decode(smallestIndex, breakWord);

		return decode;
	}

}
