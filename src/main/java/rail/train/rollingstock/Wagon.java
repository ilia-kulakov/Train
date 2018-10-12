package rail.train.rollingstock;
import java.time.LocalDate;

public abstract class Wagon extends Stock implements Comparable<Wagon>{
    public enum CuplerType {
        MANUAL("manual"),
        AUTO("auto"),
        NONE("absent");

        private String caption;
        CuplerType(String caption) {
            this.caption = caption;
        }

        @Override
        public String toString() {
            return caption;
        }
    }

    public enum BrakeType {
        MANUAL("manual"),
        ELECTROMAGNETIC("electromagnetic"),
        ELECTRIC("electric"),
        PNEUMATIC("pneumatic"),
        ELECTRO_PNEUMATIC("electro-pneumatic");

        private String caption;
        BrakeType(String caption) {
            this.caption = caption;
        }

        @Override
        public String toString() {
            return caption;
        }
    }

    private CuplerType cupler;
    private BrakeType brake;
    private boolean automotive;
    private LocalDate commissioningDate;
    private LocalDate lastServiceDate;

    public Wagon() {
        super();

        this.cupler = CuplerType.MANUAL;
        this.automotive = false;
        this.brake = BrakeType.MANUAL;
        this.commissioningDate = LocalDate.now();
        this.lastServiceDate = LocalDate.now();
    }

    public Wagon(Wagon other) {
        super(other);
        this.automotive = other.automotive;
        this.cupler = other.cupler;
        this.brake = other.brake;
        this.commissioningDate = LocalDate.now();
        this.lastServiceDate = LocalDate.now();
    }

    public Wagon(WayType type, double length, double width, double height, double weight, int axisCount,
                 boolean automotive, CuplerType cupler, BrakeType brake) {
        super(type, length, width, height, weight, axisCount);
        this.automotive = automotive;
        this.cupler = cupler;
        this.brake = brake;
        this.commissioningDate = LocalDate.now();
        this.lastServiceDate = LocalDate.now();
    }

    public CuplerType getCupler() {
        return cupler;
    }

    public BrakeType getBrake() {
        return brake;
    }

    public boolean isAutomotive() {
        return automotive;
    }

    public int getComfort() {
        return -1;
    }

    public int getPassengersCount() {
        return 0;
    }

    public int getStaffCount() {
        return 0;
    }

    public int getSeatsCount() {
        return 0;
    }

    public double getCargoCapacity() {
        return 0.0;
    }

    public double getCargoWeight() {
        return 0.0;
    }

    public double boardingOfPassenger(PassengerWagon.ComfortLevel comfort, double baggage)
            throws WagonException {
        throw new WagonException("The wagon is not designed to carry passengers.");
    }

    public double[] deboardingOfEveryone() throws WagonException {
        throw new WagonException("The wagon is not designed to carry passengers.");
    }

    public double loadingCargo(double cargo) throws WagonException {
        throw new WagonException("The wagon is not designed to carry cargo.");
    }

    public double unloadingCargo() throws WagonException {
        throw new WagonException("The wagon is not designed to carry cargo.");
    }


    public int compareTo(Wagon other) {
        return other.getComfort() - this.getComfort();
    }

    @Override
    public String toString() {
        return "Wagon: comfort " + this.getComfort() +
                ", passengers " + this.getPassengersCount() +
                ", cargo weight " + this.getCargoWeight() +
                ", cupler is " + cupler +
                ", brake is " + brake +
                ", wagon " + ((automotive) ? "is" : "isn't") + " automotive" +
                ", commissioning date: " + commissioningDate +
                ", last service date: " + lastServiceDate;
    }
}
