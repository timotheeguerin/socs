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

    BigInteger two = new BigInteger("2");

    private List<BigInteger> factors = new ArrayList<BigInteger>();

    public static void main(String[] args) {
        PrimeFinder finder = new PrimeFinder();
        finder.n = new BigInteger("2532648290334938494");
        finder.p = 16;
        int sample_size = 1;
        int sum = 0;
        for (int i = 0; i < sample_size; i++) {
            long start = System.currentTimeMillis();
            finder.getPrimeFactor();
            long end = System.currentTimeMillis();
            sum += (end - start);
            System.out.println(end - start);
            return;
        }
        System.out.println(sum / sample_size);
    }

    public void getPrimeFactor() {

        if (n.mod(two).equals(BigInteger.ZERO)) {
            factors.add(two);
        }

        Thread[] threads = new Thread[p];
        BigInteger max = sqrt(n);
        for (int i = 0; i < p; i++) {
            BigInteger val = new BigInteger(String.valueOf(i * 2 + 3));
            threads[i] = new Thread(new PrimeFinderThread(val, max));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(factors);
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

        for (BigInteger val : results) {
            System.out.print(val + "  ");
        }
        System.out.println();
    }

    class PrimeFinderThread implements Runnable {
        private BigInteger current;
        private BigInteger max;
        private BigInteger offset;

        public PrimeFinderThread(BigInteger current, BigInteger max) {
            this.current = current;
            this.max = max;
            this.offset = new BigInteger(String.valueOf(p * 2));
        }

        @Override
        public void run() {
            while (current.compareTo(max) == -1) {
                if (n.mod(current).equals(BigInteger.ZERO)) {
                    factors.add(current);
                }
                current = current.add(offset);
            }
        }
    }


    /**
     * Computes the square root of a big integer
     *
     * @throws IllegalArgumentException
     */
    public static BigInteger sqrt(BigInteger x) throws IllegalArgumentException {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }

        // square roots of 0 and 1 are trivial
        if (x == BigInteger.ZERO || x == BigInteger.ONE) {
            return x;
        }

        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;

        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = x.divide(two); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y)).add(y)).divide(two)) ;

        return y;
    }
}
