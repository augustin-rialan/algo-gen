    import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
    import org.apache.poi.ss.usermodel.*;

    import java.io.File;
    import java.io.IOException;
    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.Iterator;

    public class Main {
        public static void main(String[] args) throws IOException, InvalidFormatException {
            TestJUnit test = new TestJUnit();
            //    test.testGenerate();
            //   test.testInitialisation();
            // test.crossingTest();
            /**
            Algorithm algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(0).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(1).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(2).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(3).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(4).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(5).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(6).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(7).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(8).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(9).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(10).affiche();
             algorithm=new Algorithm(0.8,0.85,10000,10);
            algorithm.execute(11).affiche();

             */


       Algorithm algorithm=new Algorithm(0.8,0.85,10000,10);
          algorithm.execute(12).affiche();





      //  Chromosome chromosome = new Chromosome(new BigDecimal("0.0234583"), new BigDecimal("10"), new BigDecimal("1"), new BigDecimal("370"), new BigDecimal("2"),new BigDecimal("0.4"),new BigDecimal( "1.01"),new BigDecimal("1550"),new BigDecimal("1"),new BigDecimal("0"));
      //    System.out.println(chromosome.evaluateFobj(Chromosome.data(0).get(0),Chromosome.data(0).get(1),Chromosome.data(0).get(2),Chromosome.data(0).get(3),Chromosome.data(0).get(4),Chromosome.data(0).get(5)));

        }
    }
