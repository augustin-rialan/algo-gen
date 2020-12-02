            import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

            import java.io.IOException;
            import java.math.BigDecimal;
            import java.util.ArrayList;
            import java.util.Iterator;

            public class Algorithm {
                private double crossingProbability;
                private double mutationProbability;
                private int sizePopulation;
                private int numberGeneration;

                public Algorithm(double crossingProbability, double mutationProbability, int sizePopulation, int numberGeneration) {
                    this.crossingProbability = crossingProbability;
                    this.mutationProbability = mutationProbability;
                    this.sizePopulation = sizePopulation;
                    this.numberGeneration = numberGeneration;
                }

                public Chromosome execute(int sheet) throws IOException, InvalidFormatException {

                    Constante crr=new Constante(new BigDecimal("0.02"),new BigDecimal("0.02"), new BigDecimal("0.0001"));
                    Constante a=  new Constante(new BigDecimal("30"),new BigDecimal("30"), new BigDecimal("1"));
                    Constante b=  new Constante(new BigDecimal("200"),new BigDecimal("199"),new BigDecimal("1"));
                    Constante K=  new Constante(new BigDecimal("10"),new BigDecimal("5"),new BigDecimal("0.001"));
                    Constante R=  new Constante(new BigDecimal("0.2"),new BigDecimal("0.199"),new BigDecimal("0.001"));
                    Constante r=  new Constante(new BigDecimal("0.4"),new BigDecimal("0.3"),new BigDecimal("0.0005"));
                    Constante SOH=new Constante(new BigDecimal("0.5"),new BigDecimal("0.4999"),new BigDecimal("0.001"));
                    Constante m = new Constante(new BigDecimal("1550"),new BigDecimal("100"),new BigDecimal("1"));



                  ArrayList<ArrayList<ArrayList<BigDecimal>>> data;
                    data=Chromosome.data(sheet);

                    ArrayList<ArrayList<BigDecimal>> vitesse = data.get(0);
                    ArrayList<ArrayList<BigDecimal>> altitude = data.get(1);
                    ArrayList<ArrayList<BigDecimal>> distance = data.get(2);
                    ArrayList<ArrayList<BigDecimal>> realSoc = data.get(3);
                    ArrayList<ArrayList<BigDecimal>> temperatureExt = data.get(4);
                    ArrayList<ArrayList<BigDecimal>> capacite = data.get(5);

                    double fitnessmaximumatteinte=100000000;
                    Chromosome championGlobal=null;
                    //Initialisation
                    //Generation de chromosomes aléatoires
                    Population population=new Population();
                    int realnumberGeneration=0;
                    for (int i=0;i<sizePopulation;i++) {
                        crr.generate();
                        K.generate();
                        a.generate();
                        b.generate();
                        R.generate();
                        r.generate();
                        SOH.generate();
                        m.generate();

                        Chromosome chromosome = new Chromosome(crr.getValue(), K.getValue(), a.getValue(), b.getValue(), R.getValue(), r.getValue(), SOH.getValue(),m.getValue());
                        population.add(chromosome);
                    }
                    //population.affiche();

                    while (numberGeneration>0) {
                        BigDecimal currentFobj;
                        //Fitness evaluation
                        ArrayList<Double> fObj = new ArrayList<>();

                        for (Chromosome ch : population.getPopulation()) {
                            currentFobj=ch.evaluateFobj(vitesse,altitude,distance,realSoc,temperatureExt,capacite);
                                fObj.add(currentFobj.doubleValue());

                            if (currentFobj.doubleValue()<fitnessmaximumatteinte){
                                fitnessmaximumatteinte=currentFobj.doubleValue();
                                championGlobal=ch;
                            }
                        }

                        ArrayList<Double> fitness = new ArrayList<Double>();
                        double total = 0;
                        for (Double obj : fObj) {
                            fitness.add(1 / obj);
                            total += 1 / obj;

                        }

                        //probability of each chromosome to be selected
                        ArrayList<Double> probability = new ArrayList<Double>();
                        for (Double fit : fitness) {
                            probability.add(fit / total);
                        }

                        ArrayList<Double> cumulativeProbability = new ArrayList<Double>();
                        double cumulative = 0;
                        for (Double proba : probability) {
                            cumulative += proba;
                            cumulativeProbability.add(cumulative);
                        }


                        ArrayList<Double> random = new ArrayList<Double>();
                        for (Chromosome ch : population.getPopulation()) {
                            random.add(Math.random());
                        }

                        ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
                        Iterator<Chromosome> iterpopulation = population.getPopulation().iterator();



                        for (Double rand : random) {
                            iterpopulation = population.getPopulation().iterator();
                            for (Double cumul : cumulativeProbability) {
                                if (rand < cumul) {
                                    if (iterpopulation.hasNext()) {
                                        newPopulation.add(iterpopulation.next());
                                    }
                                    break;

                                } else {
                                    iterpopulation.next();
                                }

                            }
                        }

             //           System.out.println(newPopulation.toString());


                        Population nPopulation = new Population(newPopulation);
              //          nPopulation.affiche();

                        //Crossing
                        ArrayList<Double> crossingProbabilityList = new ArrayList<Double>();
                        for (Chromosome ch : population.getPopulation()) {
                            crossingProbabilityList.add(Math.random());
                        }

                        ArrayList<Chromosome> listParents = new ArrayList<>();

                        Iterator<Chromosome> iternewpopulation = nPopulation.getPopulation().iterator();
                        for (double randomnumber : crossingProbabilityList) {
                            if (randomnumber < crossingProbability) {
                                //this chromosome is a parent
                                listParents.add(iternewpopulation.next());
                            } else {
                                iternewpopulation.next();
                            }
                        }

              //          System.out.println("List of Parent" + listParents.toString());
               //         for (Chromosome cro : listParents) {
                //            cro.affiche();
                 //       }
                        int index = 0;

                        if (listParents.size() >= 2) {
                            //On cross
                            Iterator<Chromosome> iteratorListOfParent = listParents.iterator();
                            iteratorListOfParent.next();
                            Chromosome children;
                            for (Chromosome chrom : listParents) {
                                if (iteratorListOfParent.hasNext()) {
                                    children = chrom.crossing(iteratorListOfParent.next());

                                }
                                //sinon c'est le dernier, on le cross avec le premier
                                else {
                                    children = chrom.crossing(listParents.get(0));

                                }

                                //on insere children au bon endroit dans la new population
                                for (Chromosome ch : newPopulation) {
                                    if (ch.equals(chrom)) {
                                        index = newPopulation.indexOf(ch);
                                    }
                                }

                        //        System.out.println("Affichage code children" + children.toString());
                                newPopulation.remove(index);
                                newPopulation.add(index, children);
                            }

                        }


                    //    System.out.println(newPopulation.toString());
                    //    nPopulation = new Population(newPopulation);
                    //    System.out.println("Affichage de la population juste avant les mutations");
                    //    nPopulation.affiche();


                        //mutation
                        //multiplier taille population par nombre de gènes par chromosome
                        int totalGenes = sizePopulation * 8;
                        int numberofMutations = (int) Math.floor(mutationProbability * totalGenes);
                    //    System.out.println("Nombre de mutation: " + numberofMutations);

                        ArrayList<Integer> randomMutation = new ArrayList<Integer>();
                        for (int i = 0; i < numberofMutations; i++) {
                            Aleatoire geneNumber = new Aleatoire(0, totalGenes-1);
                            randomMutation.add(geneNumber.generateInt());
                        }

                        for (int geneNumber : randomMutation) {
                            int chromosomeNumber = geneNumber /  8;
                      //      System.out.println("Real gene number:" + geneNumber);
                      //      System.out.println("Chromosome number: " + chromosomeNumber);
                            int geneNumberChro = (geneNumber ) % 8;
                            //attention on commence au chromosome 0 et au gene 1

                            Chromosome chromMuted = nPopulation.getPopulation().get(chromosomeNumber);
                            ArrayList<Double> codage = chromMuted.code();
                            Constante constante;
                 //           System.out.println("Gene number: " + geneNumberChro);
                            switch (geneNumberChro) {
                                case 0:
                                    constante = crr;
                                    break;
                                case 1:
                                    constante = K;
                                    break;
                                case 2:
                                    constante = a;
                                    break;
                                case 3:
                                    constante = b;
                                    break;
                                case 4:
                                    constante = R;
                                    break;
                                case 5:
                                    constante = r;
                                    break;
                                case 6:
                                    constante = SOH;
                                    break;
                                case 7:
                                    constante=m;
                                    break;
                                    default:
                                    constante = null;
                                    System.out.println("ERREUR:GENE NUMBER INCORRECT");
                                    System.exit(0);
                            }
                            Double value = codage.get(geneNumberChro);
                            codage.remove(geneNumberChro);
                            if (new Aleatoire(1, 2).generateInt() % 2 != 0) {
                                if (value+constante.getGranularite().doubleValue()<constante.getFirstValue().doubleValue()+constante.getFluctuation().doubleValue()) {
                                    value = value + constante.getGranularite().doubleValue();
                                }

                                else {
                                    value = value - constante.getGranularite().doubleValue();
                                }
                            }
                            else {
                                if (value-constante.getGranularite().doubleValue()>constante.getFirstValue().doubleValue()-constante.getFluctuation().doubleValue()) {
                                    value = value - constante.getGranularite().doubleValue();
                                }

                                else {
                                    value= value + constante.getGranularite().doubleValue();
                                }

                            }

                            codage.add(geneNumberChro, value);
                            Chromosome newChromosome = Chromosome.decode(codage);
                            index = nPopulation.getPopulation().indexOf(chromosomeNumber);
                            nPopulation.getPopulation().remove(chromosomeNumber);
                            nPopulation.getPopulation().add(chromosomeNumber, newChromosome);

                        }


                     //   System.out.println("Conclusion: Fitness global de " + evaluation);
                        population = nPopulation;
                        /**
                        ArrayList<Integer> indexRemoveList=new ArrayList<Integer>();
                        //Nouveaux chromosomes aléatoires
                        Iterator<Chromosome> itPopulation=population.getPopulation().iterator();
                        while (itPopulation.hasNext()){
                            if (new Aleatoire(0,10).generateInt()==0){
                                indexRemoveList.add(population.getPopulation().indexOf(itPopulation.next()));

                            }
                        }
                        Iterator<Integer> indexRemoveIterator=indexRemoveList.iterator();
                        while (indexRemoveIterator.hasNext()){
                            crr.generate();
                            K.generate();
                            a.generate();
                            b.generate();
                            R.generate();
                            r.generate();
                            co.generate();
                            Chromosome chromosome = new Chromosome(crr.getValue(), K.getValue(), a.getValue(), b.getValue(), R.getValue(), r.getValue(), co.getValue());
                            int indexx=indexRemoveIterator.next();
                            population.getPopulation().remove(indexx);

                            population.getPopulation().add(indexx,chromosome);
                        }

                         **/
                    numberGeneration--;
                    realnumberGeneration++;


                    //    champion.affiche();

                   // }

                     //   for (Chromosome chro : nPopulation.getPopulation()){
                     //       if (chro.evaluateFobj()<fitnessmaximumatteinte){
                     //           championGlobal=chro;
                     //           fitnessmaximumatteinte=chro.evaluateFobj();
                     //       }
                     //   }

                        System.out.println("Generation numero " + realnumberGeneration);
                        System.out.println("Fobj min atteinte: " + fitnessmaximumatteinte);
                        System.out.println("Champion: " );
                        championGlobal.affiche();



                    }
                    return championGlobal;


                }
            }
