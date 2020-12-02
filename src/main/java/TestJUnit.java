import java.math.BigDecimal;

class TestJUnit {

    @org.junit.Test
    public void testGenerate() {
        Constante c=new Constante(new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("0.1"));

        for (int i=0;i<50000;i++) {
            c.generate();
            System.out.print(c.getValue()+"\n");
        }
    }

  /*  public void testInitialisation() throws IOException, InvalidFormatException {
        Algorithm algorithm = new Algorithm(0,0,5000000,0);
        Population pop = algorithm.execute();
        double sommeCrr=0;
        double sommea=0;
        double sommeb=0;
        double sommeK=0;
        double sommeres=0;
        double sommerend=0;
        double sommeCo=0;
        int i=0;
        for (Chromosome chro : pop.getPopulation()){
            sommeCrr+=chro.getCrr();
            sommea+=chro.getA();
            sommeb+=chro.getB();
            sommeCo+=chro.getCo();
            sommeK+=chro.getK();
            sommerend+=chro.getRend();
            sommeres+=chro.getRes();
            i++;

           // System.out.println("SommeCrr: " + sommeCrr + "i: " + i);
        }
        double moyenneCrr;
        double moyennea;
        double moyenneb;
        double moyenneK;
        double moyenneres;
        double moyennerend;
        double moyenneCo;

        moyenneCrr=sommeCrr/i;
        moyennea=sommea/i;
        moyenneb=sommeb/i;
        moyenneCo=sommeCo/i;
        moyenneres=sommeres/i;
        moyennerend=sommerend/i;
        moyenneK=sommeK/i;

        System.out.println("Moyenne Crr: " + moyenneCrr);
        System.out.println("Moyenne a: " + moyennea);
        System.out.println("Moyenne b: " + moyenneb);
        System.out.println("Moyenne K: " + moyenneK);
        System.out.println("Moyenne C0: " + moyenneCo);
        System.out.println("Moyenne rend: " + moyennerend);
        System.out.println("Moyenne res: " + moyenneres);





    }



    public void crossingTest(){
        Chromosome a = new Chromosome(1,2,3,4,5,6,7);
        Chromosome b = new Chromosome(10,20,30,40,50,60,70);
        for (int i=0;i <50 ; i++){
            System.out.println(a.crossing(b).code().toString());
        }
    }


*/


}