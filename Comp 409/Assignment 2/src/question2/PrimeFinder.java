package question2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Check if the given number is a prime
 * Created by tim on 14-10-12.
 */
public class PrimeFinder {

    public BigInteger n;
    public int p;

    private static final BigInteger TWO = BigInteger.valueOf(2L);

    private List<BigInteger> factors = new ArrayList<BigInteger>();

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error missing arguments: expecting 2.");
            return;
        }
        PrimeFinder finder = new PrimeFinder();
        finder.n = new BigInteger(args[0]);
        finder.p = Integer.parseInt(args[1]);
        long start = System.currentTimeMillis();
        finder.getPrimeFactor();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public List<BigInteger> getPrimeFactor() {

        if (n.mod(TWO).equals(BigInteger.ZERO)) {
            factors.add(TWO);
        }

        Thread[] threads = new Thread[p];
        BigInteger max = sqrt(n);
        for (int i = 0; i < p; i++) {
            //Each thread is given a starting index
            BigInteger val = BigInteger.valueOf(i * 2 + 3);
            threads[i] = new Thread(new PrimeFinderThread(val, max));
        }

        for (Thread thread : threads) thread.start();

        try {
            for (Thread thread : threads) thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Sort factors as they could have been inserted in the wrong order
        //e.g. the thread testing 5 run before the thread testing 3 for 15
        Collections.sort(factors);
        //Only keep prime numbers(not divisible by any of the other numbers)
        List<BigInteger> results = new ArrayList<BigInteger>();
        for (int i = 0; i < factors.size(); i++) {
            boolean found = false;
            for (int j = 0; j < i; j++) {
                if (factors.get(i).mod(factors.get(j)).equals(BigInteger.ZERO)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                results.add(factors.get(i));
            }
        }

        System.out.println("Prime factors: ");
        for (BigInteger val : results) {
            System.out.print(val + "  ");
        }
        System.out.println("\n");
        return results;
    }


    class PrimeFinderThread implements Runnable {
        private BigInteger current;
        private BigInteger max;
        private BigInteger offset;

        public PrimeFinderThread(BigInteger current, BigInteger max) {
            this.current = current; //Current divisor to check
            this.max = max; //Max value to check
            this.offset = new BigInteger(String.valueOf(p * 2)); //Skip offset values each divisor
        }

        @Override
        public void run() {
            //Each thread try each value every offset
            while (current.compareTo(max) == -1) {
                if (n.mod(current).equals(BigInteger.ZERO)) {
                    factors.add(current);
                }
                current = current.add(offset);
            }
        }
    }


    /**
     * Helper function.
     * Computes the square root of a big integer
     *
     * @throws IllegalArgumentException
     */
    public static BigInteger sqrt(BigInteger x) throws IllegalArgumentException {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }

        // square roots of 0 and 1 are trivial
        if (x.equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
            return x;
        }


        BigInteger y;

        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = x.divide(TWO); y.compareTo(x.divide(y)) > 0; ) {
            y = ((x.divide(y)).add(y)).divide(TWO);
        }

        return y;
    }
}
