import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{

    public static void main(String[] args)
    {
        List<String> messages = new ArrayList<String>();
        messages.add("Bob this is Mary");
        messages.add("Download data file now");
//        System.out.println(Cryptography.hash(messages));
//        System.out.println(Cryptography.hash2(messages));
        System.out.println(Arrays.toString(Cryptography.privatePublicEncrypt(messages)));
        System.out.println(Cryptography.privatePublicDecrypt(Cryptography.privatePublicEncrypt(messages)));
//        System.out.println(Cryptography.gridTransposition(messages));

        //        for (int i = 1; i < 100; i++)
        //        {
        //            int n = 50 * i + 1;
        //            if (!isPrime(n))
        //            {
        //                List<Integer> factors = primeFactors(n);
        //                if (factors.size() > 3)
        //                {
        //                    System.out.println(n + "    -    " + factors);
        //                }
        //            }
        //        }
    }

    static boolean isPrime(int n)
    {
        //check if n is a multiple of 2
        if (n % 2 == 0)
            return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= n; i += 2)
        {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static List<Integer> primeFactors(int number)
    {
        int n = number;
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++)
        {
            while (n % i == 0)
            {
                factors.add(i);
                n /= i;
            }
        }
        return factors;
    }
}
