package rail.train.rollingstock;

public class FreightWagon extends Wagon {
    public enum Corpus {
        MAIL_BAGGAGE("Mail-baggage wagon"),
        PLATFORM("Platform"),
        BOX("Box wagon"),
        HALF_BOX("Half box wagon"),
        TANK("Tank"),
        HOPPER("Hopper"),
        REEFER("Reefer wagon");

        private String caption;
        Corpus(String caption) {
            this.caption = caption;
        }

        public String toString() {
            return "Corpus type: " + caption;
        }
    }

    private Corpus corpus;
    private double cargoCapacity;
    private double cargoWeight;

    public FreightWagon(WayType type, double length, double width, double height, double weight, int axisCount,
        boolean automotive, CuplerType cupler, BrakeType brake, Corpus corpus, double cargoCapacity) {

        super(type, length, width, height, weight, axisCount, automotive, cupler, brake);
        this.corpus = corpus;
        this.cargoCapacity = cargoCapacity;
        this.cargoWeight = 0.0;
    }

    public FreightWagon(FreightWagon other) {
        super(other);
        this.corpus = other.corpus;
        this.cargoCapacity = other.cargoCapacity;
        this.cargoWeight = other.cargoWeight;
    }

    public FreightWagon() {
        super();
        this.corpus = Corpus.BOX;
        this.cargoCapacity = 40000.0;
        this.cargoWeight = 0.0;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCargoWeight() {
        return cargoWeight;
    }

    @Override
    public double loadingCargo(double cargo) throws WagonException {
        if( cargo < 0) {
            throw new WagonException("The cargo has negative weight.");
        }

        if ( cargoWeight + cargo > cargoCapacity) {
            cargo = cargoCapacity - cargoWeight;
        }

        cargoWeight += cargo;

        return cargo;
    }

    public double unloadingCargo(double cargo) throws WagonException {
        if( cargo < 0) {
            throw new WagonException("The cargo has negative weight.");
        }

        if(cargoWeight < cargo) {
            cargo = cargoWeight;
        }

        cargoWeight -= cargo;

        return cargo;
    }

    @Override
    public double unloadingCargo() throws WagonException {
        if(cargoWeight == 0) {
            throw new WagonException("The wagon is already empty.");
        }

        double result = cargoWeight;
        cargoWeight = 0;

        return result;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return "Freight " + baseInfo + ", " + getCorpus() +
                ", cargo capacity " + this.getCargoCapacity();

    }

}
