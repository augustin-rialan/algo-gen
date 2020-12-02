    import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
    import org.apache.poi.ss.usermodel.*;

    import javax.sound.midi.SysexMessage;
    import java.io.File;
    import java.io.IOException;
    import java.math.BigDecimal;
    import java.math.MathContext;
    import java.math.RoundingMode;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.Objects;

    public class Chromosome {
        private static final BigDecimal SCX = new BigDecimal("0.75");
        private static final BigDecimal g=new BigDecimal("9.81");
        private static final BigDecimal rho=new BigDecimal("1.18");


        //les différentes constantes à retrouver
        private BigDecimal Crr;
        private BigDecimal K;
        private BigDecimal a;
        private BigDecimal b;
        private BigDecimal res;
        private BigDecimal rend;
        private BigDecimal SOH;
        private BigDecimal m;




        public Chromosome(BigDecimal crr, BigDecimal k, BigDecimal a, BigDecimal b, BigDecimal res, BigDecimal rend, BigDecimal soh, BigDecimal m) {
            this.Crr = crr;
            this.K = k;
            this.a = a;
            this.b = b;
            this.res = res;
            this.rend = rend;
            this.SOH = soh;
            this.m= m;

        }

        public static ArrayList<ArrayList<ArrayList<BigDecimal>>> data(int sheetnumber) throws IOException, InvalidFormatException {
            ArrayList<ArrayList<ArrayList<BigDecimal>>> data = new ArrayList<ArrayList<ArrayList<BigDecimal>>>();
            Workbook workbook = WorkbookFactory.create(new File("C:\\Users\\augus\\Downloads\\DataAll.xlsx"));

            ArrayList<ArrayList<BigDecimal>> vitesse= new ArrayList<ArrayList<BigDecimal>>();
            ArrayList<ArrayList<BigDecimal>> altitude= new ArrayList<ArrayList<BigDecimal>>();
            ArrayList<ArrayList<BigDecimal>> distance= new ArrayList<ArrayList<BigDecimal>>();
            ArrayList<ArrayList<BigDecimal>> realSoc= new ArrayList<ArrayList<BigDecimal>>();
            ArrayList<ArrayList<BigDecimal>> temperatureExterieure= new ArrayList<ArrayList<BigDecimal>>();
            ArrayList<ArrayList<BigDecimal>> capacite = new ArrayList<ArrayList<BigDecimal>>();







            ArrayList<Integer> sheetIndexList=new ArrayList<>();
            for (int i=0;i<17;i++) {
                sheetIndexList.add(i);
            }



            Iterator<Integer> sheetIndexListIterator=sheetIndexList.iterator();

            while (sheetIndexListIterator.hasNext()){

                Sheet sheet = workbook.getSheetAt(sheetIndexListIterator.next());

                ArrayList<BigDecimal> sousVitesse=new ArrayList<BigDecimal>();
                ArrayList<BigDecimal> sousAltitude=new ArrayList<BigDecimal>();
                ArrayList<BigDecimal> sousDistance=new ArrayList<BigDecimal>();
                ArrayList<BigDecimal> sousRealSoc=new ArrayList<BigDecimal>();
                ArrayList<BigDecimal> sousTemperatureExterieure=new ArrayList<BigDecimal>();
                ArrayList<BigDecimal> sousCapacite=new ArrayList<BigDecimal>();





                // 1. You can obtain a rowIterator and columnIterator and iterate over them
                Iterator<Row> rowIterator = sheet.rowIterator();
                rowIterator.next();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    // Now let's iterate over the columns of the current row
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getColumnIndex() == 20) {
                            double cellValue = cell.getNumericCellValue();
                            sousVitesse.add(BigDecimal.valueOf(cellValue));
                        }
                        if (cell.getColumnIndex() == 21) {
                            double cellValue = cell.getNumericCellValue();
                            sousAltitude.add(BigDecimal.valueOf(cellValue));
                        }

                        if (cell.getColumnIndex() == 19) {
                            double cellValue = cell.getNumericCellValue();
                            sousDistance.add(BigDecimal.valueOf(cellValue));
                        }

                        if (cell.getColumnIndex() == 29) {
                            double cellValue = cell.getNumericCellValue();
                            sousRealSoc.add(BigDecimal.valueOf(cellValue));
                        }

                        if (cell.getColumnIndex() == 6){
                            double cellValue=cell.getNumericCellValue();
                            sousTemperatureExterieure.add(BigDecimal.valueOf(cellValue));

                        }

                        if (cell.getColumnIndex() == 12) {
                            double cellValue=cell.getNumericCellValue();
                            sousCapacite.add(BigDecimal.valueOf(cellValue));
                        }

                    }

                }
                vitesse.add(sousVitesse);
                altitude.add(sousAltitude);
                distance.add(sousDistance);
                realSoc.add(sousRealSoc);
                temperatureExterieure.add(sousTemperatureExterieure);
                capacite.add(sousCapacite);

            }

            data.add(vitesse);
            data.add(altitude);
            data.add(distance);
            data.add(realSoc);
            data.add(temperatureExterieure);
            data.add(capacite);


            return data;
        }


        public ArrayList<BigDecimal> dSocModel(ArrayList<BigDecimal> vitesse,ArrayList<BigDecimal> altitude,ArrayList<BigDecimal> distance, ArrayList<BigDecimal> temperatureExt, ArrayList<BigDecimal> capacite) throws IOException, InvalidFormatException {
            //tempBatterie=tempExt (en vérité fonction affine? Faire régression linéaire)
            BigDecimal tempExt=temperatureExt.get(0);
            BigDecimal tempBatterie=tempExt;
            ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();
            ArrayList<BigDecimal> dSocModel=new ArrayList<>();
            BigDecimal Co= capacite.get(0);

            ArrayList<BigDecimal> Vcarre=new ArrayList<>();
            Iterator<BigDecimal> iteratorVitesse= vitesse.iterator();
            while (iteratorVitesse.hasNext()){
                Vcarre.add(iteratorVitesse.next().pow(2));
            }
            ArrayList<BigDecimal> dVcarre=new ArrayList<>();
            Iterator<BigDecimal> iteratorVitessecarre= Vcarre.iterator();
            BigDecimal current=new BigDecimal("0");
            BigDecimal next= new BigDecimal("0");

            while (iteratorVitessecarre.hasNext()){
                next=iteratorVitessecarre.next();
                dVcarre.add(next.subtract(current));
                current=next;

            }



            //On calcule
            ArrayList<BigDecimal> pente = new ArrayList<>();
            ArrayList<BigDecimal> deriveeAltitude = new ArrayList<>();
            ArrayList<BigDecimal> distanceDeuxpoints = new ArrayList<>();

            Iterator<BigDecimal> itealtitude = altitude.iterator();
            Iterator<BigDecimal> itedistance = distance.iterator();

            BigDecimal currentaltitude = new BigDecimal("0");
            BigDecimal precedentaltitude=altitude.get(0);
            deriveeAltitude.add(new BigDecimal("0"));
            itealtitude.next();
            while (itealtitude.hasNext()){
                currentaltitude=itealtitude.next();
                deriveeAltitude.add(currentaltitude.subtract(precedentaltitude));
                precedentaltitude=currentaltitude;
            }

            Iterator<BigDecimal> iteDAltitude = deriveeAltitude.iterator();
            Iterator<BigDecimal> iteDis=distanceDeuxpoints.iterator();



            BigDecimal u0=(a.multiply(tempBatterie).multiply(a)).add(b);

            BigDecimal premierFacteur;
            premierFacteur=(u0.pow(2)).divide(new BigDecimal("2").multiply(this.res),20,RoundingMode.HALF_DOWN);
            ArrayList<BigDecimal> Pground = new ArrayList<BigDecimal>();
            ArrayList<BigDecimal>  Paero=new ArrayList<BigDecimal>();
            ArrayList<BigDecimal> Ppot=new ArrayList<BigDecimal>();
            ArrayList<BigDecimal> Pheatcool = new ArrayList<BigDecimal>();
            ArrayList<BigDecimal> Pkinetic = new ArrayList<BigDecimal>();
            ArrayList<BigDecimal> Puissance = new ArrayList<BigDecimal>();
            ArrayList<BigDecimal> Pbat=new ArrayList<BigDecimal>();

            Iterator<BigDecimal> vitesseIterator= vitesse.iterator();

            BigDecimal currentVitesse;

            Iterator<BigDecimal> dVcarreIterator=dVcarre.iterator();
            Iterator<BigDecimal> iteratorDeriAltitude=deriveeAltitude.iterator();

            while (vitesseIterator.hasNext()){
                currentVitesse=vitesseIterator.next();
                Pground.add(this.getCrr().multiply(this.m).multiply(this.g).multiply(currentVitesse));
                Paero.add(new BigDecimal("0.5").multiply(this.rho).multiply(this.SCX).multiply(currentVitesse.pow(3)));

                Ppot.add(iteratorDeriAltitude.next().multiply(this.m).multiply(this.g));
                //pas de climatisation
                Pheatcool.add(new BigDecimal("0"));
                Pkinetic.add(new BigDecimal(0.5).multiply(this.m).multiply(dVcarreIterator.next()));
                Puissance.add(Pground.get(Pground.size()-1).add(Paero.get(Paero.size()-1)).add(Ppot.get(Ppot.size()-1)).add(Pheatcool.get(Pheatcool.size()-1)).add(Pkinetic.get(Pkinetic.size()-1)));
                if ((Puissance.get(Puissance.size()-1).compareTo(new BigDecimal("0")))==1){

                    if (((new BigDecimal("4").multiply(this.res).multiply(Puissance.get(Puissance.size()-1))).divide(u0.pow(2),20,RoundingMode.HALF_DOWN)).compareTo(new BigDecimal("1"))==1){
                        Pbat.add(premierFacteur);

                    }
                    else {
                        Pbat.add(premierFacteur.multiply(new BigDecimal("1").subtract(((new BigDecimal("1").subtract((new BigDecimal("4").multiply(this.res).multiply(Puissance.get(Puissance.size() - 1)).divide (u0.pow(2),20,RoundingMode.HALF_DOWN)))).sqrt(MathContext.DECIMAL128)))));
                    }
                }

                else {
                    Pbat.add(this.rend.multiply(Puissance.get(Puissance.size()-1)));
                }

                dSocModel.add(((Pbat.get(Pbat.size()-1)).negate()).divide((Co.multiply(new BigDecimal("1000").multiply(this.SOH).multiply(new BigDecimal("3600")))),20,RoundingMode.HALF_DOWN));
            }

     //   System.out.println("Pground: " + Pground.toString() + "\nPaereo: " + Paero.toString() + "\nPpot: "+ Ppot.toString() + "\nPheatcool: " + Pheatcool.toString() + "\nPkinetic: " + Pkinetic.toString() + "\nVitese: " + Vcarre + "\nPuissance: " + Puissance + "\nPuissancebat: " + Pbat);
     //   System.out.println(dSocModel);
            return dSocModel;
        }

        public BigDecimal evaluateFobj(ArrayList<ArrayList<BigDecimal>> vitesse,ArrayList<ArrayList<BigDecimal>> altitude,ArrayList<ArrayList<BigDecimal>> distance,ArrayList<ArrayList<BigDecimal>> realSoc,ArrayList<ArrayList<BigDecimal>> temperatureExt,ArrayList<ArrayList<BigDecimal>> capacite) throws IOException, InvalidFormatException {
            BigDecimal scr=new BigDecimal("0");





                for (int i=0;i<17;i++) {
                    ArrayList<BigDecimal> sousVitesse = vitesse.get(i);
                    ArrayList<BigDecimal> sousAltitude = altitude.get(i);
                    ArrayList<BigDecimal> sousDistance = distance.get(i);
                    ArrayList<BigDecimal> sousTemperatureExt = temperatureExt.get(i);
                    ArrayList<BigDecimal> sousRealSoc = realSoc.get(i);
                    ArrayList<BigDecimal> sousCapacite = capacite.get(i);


                    ArrayList<BigDecimal> dSocModel = this.dSocModel(sousVitesse, sousAltitude, sousDistance, sousTemperatureExt, sousCapacite);


                    ArrayList<BigDecimal> dRealSoc = new ArrayList<>();
                    dRealSoc.add(new BigDecimal("0"));
                    Iterator<BigDecimal> iterator = sousRealSoc.iterator();
                    BigDecimal precedentValue = iterator.next();
                    while (iterator.hasNext()) {
                        BigDecimal currentValue = iterator.next();
                        dRealSoc.add(currentValue.subtract(precedentValue));
                        precedentValue = currentValue;

                    }

                    //   System.out.println("\n Test:" + dRealSoc);

                    ArrayList<BigDecimal> socModel = new ArrayList<>();
                    socModel.add(sousRealSoc.get(0));
                    Iterator<BigDecimal> iteratorDSOCMODEL = dSocModel.iterator();
                    while (iteratorDSOCMODEL.hasNext()) {
                        // System.out.println(iteratorDSOCMODEL.next());
                        socModel.add((socModel.get(socModel.size() - 1).add(iteratorDSOCMODEL.next())));
                    }

                    socModel.remove(socModel.size() - 1);


                    //on regarde si les deux tableaux on la meme taille
                    if (socModel.size() != sousRealSoc.size()) {
                        System.out.println(socModel.size());
                        System.out.println(sousRealSoc.size());

                        System.out.println("ERREUR: Pas la meme taille");
                        System.exit(0);
                    }

                    //     System.out.println(sousRealSoc.toString());
                    //     System.out.println(socModel.toString());

                    Iterator<BigDecimal> iteratorRealSoc = sousRealSoc.iterator();
                    Iterator<BigDecimal> iteratorModelSoc = socModel.iterator();


                    while (iteratorRealSoc.hasNext()) {
                        scr = scr.add((iteratorRealSoc.next().subtract(iteratorModelSoc.next())).pow(2));
                    }

                }


         //   System.out.println("Fin de l'évaluation, temps écoulé: " + Float.toString(seconds) + " secondes.");
            return scr;
        }

        public BigDecimal getCrr() {
            return Crr;
        }


        public BigDecimal getK() {
            return K;
        }


        public BigDecimal getA() {
            return a;
        }



        public BigDecimal getB() {
            return b;
        }


        public BigDecimal getRes() {
            return res;
        }


        public BigDecimal getRend() {
            return rend;
        }



        public void affiche() {
            System.out.println("Crr: " + getCrr() + " " + "K: " + getK()  + " " + "a: " + getA()  + " " + "b: " + getB()  + " " + "res: " + getRes()  + " " + "rend: " + getRend()  + " " + "SOH: " + getSOH() + " " + "m: " + getm());
        }

        private BigDecimal getm() {
            return this.m;
        }

        private BigDecimal getSOH() {
            return this.SOH;
        }

        public ArrayList<Double> code(){
            ArrayList<Double> encodage = new ArrayList<>();
            encodage.add(Crr.doubleValue());
            encodage.add(K.doubleValue());
            encodage.add(a.doubleValue());
            encodage.add(b.doubleValue());
            encodage.add(res.doubleValue());
            encodage.add(rend.doubleValue());
            encodage.add(SOH.doubleValue());
            encodage.add(m.doubleValue());


            return encodage;
        }

        public static Chromosome decode(ArrayList<Double> codage) {
            Chromosome retour = new Chromosome(new BigDecimal("0"),new BigDecimal("0"),new BigDecimal("0"),new BigDecimal("0"),new BigDecimal("0"),new BigDecimal("0"),new BigDecimal("0"),new BigDecimal("0"));
            Iterator<Double> it= codage.iterator();
            retour.Crr=new BigDecimal(it.next());
            retour.K=new BigDecimal(it.next());
            retour.a=new BigDecimal(it.next());
            retour.b=new BigDecimal(it.next());
            retour.res=new BigDecimal(it.next());
            retour.rend=new BigDecimal(it.next());
            retour.SOH=new BigDecimal(it.next());
            retour.m=new BigDecimal(it.next());


            return retour;

        }

        public Chromosome crossing(Chromosome parent) {
            ArrayList<Double> child = new ArrayList<Double>();
            Aleatoire crossingPointAlea = new Aleatoire(0,7);
            int crossingPoint= crossingPointAlea.generateInt();
            ArrayList<Double> parent1 = this.code();
            ArrayList<Double> parent2=parent.code();

            Iterator<Double> itparent2=parent2.iterator();

            for (int i=0; i < crossingPoint ; i++){
                child.add(parent1.get(i));
                itparent2.next();
            }
            while (itparent2.hasNext()){
                child.add(itparent2.next());
            }

            return Chromosome.decode(child);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Chromosome that = (Chromosome) o;
            return Objects.equals(Crr, that.Crr) &&
                    Objects.equals(K, that.K) &&
                    Objects.equals(a, that.a) &&
                    Objects.equals(b, that.b) &&
                    Objects.equals(res, that.res) &&
                    Objects.equals(rend, that.rend) &&
                    Objects.equals(SOH, that.SOH) &&
                    Objects.equals(m, that.m) ;

        }

        @Override
        public int hashCode() {
            return Objects.hash(Crr, K, a, b, res, rend, SOH, m);
        }
    }

