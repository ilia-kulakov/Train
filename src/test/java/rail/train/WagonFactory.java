package rail.train;

import rail.train.rollingstock.*;

public class WagonFactory {
    public static PassengerWagon newWagon(PassengerWagon.ComfortLevel comfort, int seats) {
        return  new PassengerWagon(Stock.WayType.BROAD_GAUGE,
                24.5, 3.2, 2.8, 42000,
                4, true, Wagon.CuplerType.AUTO, Wagon.BrakeType.PNEUMATIC,
                comfort, seats,
                1, true, true);
    }

    public static PassengerWagon newWagonWithPassengers(PassengerWagon.ComfortLevel comfort, int seats, int passengers)
            throws WagonException
    {
        PassengerWagon wagon = newWagon(comfort, seats);
        for (int i = 0; i < passengers; i++) {
            double baggage = Math.random() * 1.5 * comfort.allowedBaggageWeight();
            wagon.boardingOfPassenger(comfort, baggage);
        }

        return wagon;
    }

    public static PassengerWagon newWagonWithPassengers(PassengerWagon.ComfortLevel comfort, int seats, double... baggages)
            throws WagonException {
        PassengerWagon wagon = newWagon(comfort, seats);
        for(double baggage : baggages) {
            wagon.boardingOfPassenger(comfort, baggage);
        }

        return wagon;
    }

    public static FreightWagon newWagon(double capacity) {
        return  new FreightWagon(Stock.WayType.BROAD_GAUGE,
                24.5, 3.2, 2.8, 10000,
                4, true, Wagon.CuplerType.AUTO, Wagon.BrakeType.PNEUMATIC,
                FreightWagon.Corpus.PLATFORM, capacity);
    }

    public static MotorWagon newWagon(MotorWagon.EngineType engine) {
        return new MotorWagon(Stock.WayType.BROAD_GAUGE,
                24.5, 3.2, 2.8, 10000,
                4, true, Wagon.CuplerType.AUTO, Wagon.BrakeType.PNEUMATIC,
                engine, 2);
    }
}
