import java.math.BigDecimal;

public class Constante {
        private BigDecimal firstValue;
        private BigDecimal fluctuation;
        private BigDecimal granularite;
        private BigDecimal value;
        private int stepNumber;

        public Constante(BigDecimal firstValue, BigDecimal fluctuation, BigDecimal granularite) {
            this.firstValue = firstValue;
            this.fluctuation = fluctuation;
            this.granularite=granularite;
            this.value=firstValue;
            this.stepNumber=((fluctuation.divide(granularite))).intValue();

        }

        public BigDecimal getFluctuation() {
            return fluctuation;
        }

        public BigDecimal getFirstValue() {
            return firstValue;
        }

        public void generate(){
            Aleatoire random=new Aleatoire(0,stepNumber);
            if (new Aleatoire(1,2).generateInt()%2!=0) {
                value = firstValue.add(BigDecimal.valueOf(random.generateInt()).movePointLeft(0).multiply(granularite));
            }
            else{
                value = firstValue.subtract(BigDecimal.valueOf(random.generateInt()).movePointLeft(0).multiply(granularite));

            }



        }

        public BigDecimal getValue(){
            return value;
        }

        public BigDecimal getGranularite() {
            return granularite;
        }
    }

