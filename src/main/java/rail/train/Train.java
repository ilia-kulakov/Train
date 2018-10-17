package rail.train;

import rail.train.rollingstock.PassengerWagon;
import rail.train.rollingstock.Wagon;
import rail.train.rollingstock.WagonException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Train class
 */
public class Train {
    private List<Wagon> wagons;

    Train() {
        this.wagons = new ArrayList<>();
    }

    public Wagon getWagon(int index) {
        return wagons.get(index);
    }

    public int length() {
        return wagons.size();
    }

    public boolean hasWagons() {
        return !wagons.isEmpty();
    }

    public Train joinHead(Wagon wagon) {
        wagons.add(0, wagon);
        return this;
    }

    public Train joinTail(Wagon wagon) {
        wagons.add(wagon);
        return this;
    }

    public Wagon unjoinHead() throws WagonException {
        return unjoin(0);
    }

    public Wagon unjoinTail() throws WagonException {
        return unjoin(wagons.size() - 1);
    }

    private Wagon unjoin(int index) throws WagonException {
        if(index < 0 || index >= wagons.size()) {
            throw new WagonException("There is no wagon with index " + index);
        }

        Wagon w = wagons.get(index);
        wagons.remove(index);

        return w;
    }

    public void sortByComfort() {
        Collections.sort(wagons);
    }

    public List<Wagon> getWagonsWithPassengers(int beg, int end) {
        List<Wagon> result = new ArrayList<>();
        for(Wagon w : wagons) {
            int count = w.getPassengersCount();
            if( beg <= count && count < end) {
                result.add(w);
            }
        }

        return result;
    }

    public String toString() {
        if(wagons.size() == 0) {
            return "The train doesn't have any wagons.";
        }

        StringBuilder result = new StringBuilder();
        int i = 1;
        for(Wagon w : wagons) {
            result.append("\n\t" + i + ": " + w);
            i++;
        }

        return "Train has " + wagons.size() + ((wagons.size() > 1) ? " wagons." : " wagon.") + result.toString();
    }

    public int getPassangersCount() {
        int result = 0;
        for( Wagon wagon : wagons ) {
            result += wagon.getPassengersCount();
        }
        return result;
    }

    public double getCargoWeight() {
        double result = 0;
        for( Wagon wagon : wagons ) {
            result += wagon.getCargoWeight();
        }
        return result;
    }

    public double boardingOfPassenger(PassengerWagon.ComfortLevel level, double baggageWeight) throws TrainException {
        double result = 0.0;
        for(Wagon wagon : wagons) {
            try {
                result = wagon.boardingOfPassenger(level, baggageWeight);
                return result;
            } catch(WagonException we) {}
        }

        throw new TrainException("There is no free seat.");
    }

    public double loadingCargo(double cargo)  throws TrainException {
        double result = 0.0;
        for(Wagon wagon : wagons) {
            try {
                double loadedCargo = wagon.loadingCargo(cargo);
                result += loadedCargo;
                cargo -= loadedCargo;
                if( cargo < 0.001) {
                    return result;
                }
            } catch(WagonException we) {}
        }

        throw new TrainException("There is no space for " + cargo + " kg of cargo.");
    }

    public void deboardingOfEveryone() {
        for(Wagon wagon : wagons) {
            try {
                wagon.deboardingOfEveryone();
            } catch(WagonException we) {}
        }
    }

    public double unloadingCargo() {
        double cargo = 0.0;
        for(Wagon wagon : wagons) {
            try {
                cargo += wagon.unloadingCargo();
            } catch(WagonException we) {}
        }

        return cargo;
    }

    public int getSeatsCount() {
        int result = 0;
        for(Wagon wagon : wagons) {
            result += wagon.getSeatsCount();
        }
        return result;
    }

    public double getCargoCapacity() {
        double result = 0.0;
        for(Wagon wagon : wagons) {
            result += wagon.getCargoCapacity();
        }

        return result;
    }



}
