import java.math.BigInteger;
import java.util.Random;

public class Aleatoire {
    private int max;
    private int min;

    public Aleatoire(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public BigInteger generate(){
        Random rand=new Random();
        return BigInteger.valueOf(rand.nextInt(max-min+1)+min);

    }

    public int generateInt(){
        Random rand=new Random();
        return rand.nextInt(max-min+1)+min;

    }
}
