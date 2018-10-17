package rail.train.rollingstock;

import java.util.Arrays;

/**
 * Passenger Wagon class
 */
public class PassengerWagon extends Wagon {
    public enum ComfortLevel {
        SHARED("Shared wagon", 10.0),
        RESERVED_SEATS("Reserved seats wagon",30.0),
        COMPARTMENT("Compartment wagon", 40.0),
        SLEEPING("Sleeping wagon", 50.0),
        DINING("Dining wagon", 0.0);

        private double baggageWeight;
        private String caption;

        public static ComfortLevel fromOrdinal(int n) {return values()[n];}

        ComfortLevel(String caption, double weight) {
            this.caption = caption;
            this.baggageWeight = weight;
        }

        public double allowedBaggageWeight() {
            return baggageWeight;
        }
        public String toString() {
            return "Comfort level: " + caption;
        }
    }

    private ComfortLevel comfortLevel;
    private int seatsCount;
    private int passengersCount;
    private double[] baggageWeight;
    private int staffCount;
    private boolean airCondition;
    private boolean heating;

    public PassengerWagon() {
        super();
        this.comfortLevel = ComfortLevel.SHARED;
        this.seatsCount = 40;
        this.staffCount = 1;
        this.airCondition = false;
        this.heating = false;
        this.passengersCount = 0;
        this.baggageWeight = new double[this.seatsCount];
    }

    public PassengerWagon(PassengerWagon other) {
        super(other);
        this.comfortLevel = other.comfortLevel;
        this.seatsCount = other.seatsCount;
        this.staffCount = other.staffCount;
        this.airCondition = other.airCondition;
        this.heating = other.heating;
        this.passengersCount = other.passengersCount;
        this.baggageWeight = new double[this.seatsCount];
    }

    public PassengerWagon(WayType wayType, double length, double width, double height, double weight, int axisCount,
                          boolean automotive, CuplerType cupler, BrakeType brake, ComfortLevel comfortLevel, int seatsCount,
                          int staffCount, boolean airCondition, boolean heating) {
        super(wayType, length, width, height, weight, axisCount, automotive, cupler, brake);

        this.comfortLevel = comfortLevel;
        this.seatsCount = seatsCount;
        this.staffCount = staffCount;
        this.airCondition = airCondition;
        this.heating = heating;
        this.passengersCount = 0;
        this.baggageWeight = new double[this.seatsCount];
    }

    @Override
    public int getSeatsCount() {
        return seatsCount;
    }

    public boolean setSeatsCount(int count) {
        if( count < 0 || count < this.passengersCount )
            return false;

        if(this.passengersCount > 0) {
            this.baggageWeight = Arrays.copyOf(this.baggageWeight, count);
        }

        this.seatsCount = count;
        return true;
    }

    @Override
    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }

    public boolean isAirCondition() {
        return airCondition;
    }

    public void setAirCondition(boolean airCondition) {
        this.airCondition = airCondition;
    }

    public boolean isHeating() {
        return heating;
    }

    public void setHeating(boolean heating) {
        this.heating = heating;
    }

    public ComfortLevel getComfortLevel() {
        return comfortLevel;
    }

    @Override
    public int getComfort() {
        return getComfortLevel().ordinal();
    }

    @Override
    public int getPassengersCount() {
        return passengersCount;
    }

    @Override
    public double boardingOfPassenger(PassengerWagon.ComfortLevel comfort, double baggage)
            throws WagonException {

        if( passengersCount >= seatsCount ) {
            throw new WagonException("All seats are occupied.");
        }

        if( baggage < 0) {
            throw new WagonException("The baggage has negative weight.");
        }

        if( comfort != getComfortLevel() ) {
            throw new WagonException("The passenger's comfort level is different from the wagon level");
        }

        double maxWeight = comfortLevel.allowedBaggageWeight();
        baggageWeight[passengersCount] = ( baggage < maxWeight ) ? baggage : maxWeight;
        return baggageWeight[passengersCount++];
    }

    public double deboardingOfPassenger()  throws WagonException {
        if(passengersCount <= 0) {
            throw new WagonException("The wagon is already empty.");
        }

        passengersCount--;
        return baggageWeight[passengersCount];
    }

    @Override
    public double[] deboardingOfEveryone() throws WagonException {
        if(passengersCount <= 0) {
            throw new WagonException("The wagon is already empty.");
        }

        double[] result = Arrays.copyOf(baggageWeight, passengersCount);
        passengersCount = 0;

        return result;
    }


    @Override
    public double getCargoWeight() {
        double result = 0.0;
        for(int i = 0; i < passengersCount; i++) {
            result += baggageWeight[i];
        }

        return result;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        baseInfo = baseInfo.replaceFirst("cargo", "baggage");
        return "Passenger " + baseInfo + ", " + getComfortLevel();
    }
}
