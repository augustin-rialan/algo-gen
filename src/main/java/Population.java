import java.util.ArrayList;

public class Population {
    private ArrayList<Chromosome> population;
    public Population() {
        population=new ArrayList<Chromosome>();
    }

    public Population(ArrayList<Chromosome> population) {
        this.population = population;
    }

    public void add(Chromosome c){
        population.add(c);
    }

    public void affiche() {
      System.out.println("Affichage de la population:\n");
      for(Chromosome chromosome :population){
          chromosome.affiche();
        }
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
