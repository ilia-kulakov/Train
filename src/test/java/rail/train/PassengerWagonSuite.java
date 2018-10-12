package rail.train;

import org.testng.Assert;
import org.testng.annotations.*;
import rail.train.rollingstock.PassengerWagon;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import rail.train.rollingstock.WagonException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassengerWagonSuite {
    static final Logger logger = LogManager.getLogger(PassengerWagonSuite.class.getName());

    @AfterMethod
    private void printLine() {
        logger.trace("-----------------------------------------------------------------------------------------------");
    }

    @DataProvider(name = "data-provider-amountOfBaggagePassengerCanTake")
    public Object[][] dataProviderForAmountOfBaggagePassengerCanTake() {
        ArrayList<Object[]> params = new ArrayList<>();
        for(PassengerWagon.ComfortLevel comfort : PassengerWagon.ComfortLevel.values()) {
            int seats = (int)(Math.random() * 50.0 + 1.0);
            double maxWeight = comfort.allowedBaggageWeight();
            double baggage = 1.5 * maxWeight * Math.random();
            double expect = (maxWeight > baggage) ? baggage : maxWeight;
            params.add(new Object[] {comfort, seats, baggage, expect});
        }

        return  params.toArray(new Object[][]{});
    }

    @Test(dataProvider = "data-provider-amountOfBaggagePassengerCanTake")
    public void amountOfBaggagePassengerCanTake(PassengerWagon.ComfortLevel comfort, int seats,
                                                double baggage, double expect) {
        logger.trace("Begin amountOfBaggagePassengerCanTake");
        logger.trace("Boarding One Passenger");
        PassengerWagon wagon = WagonFactory.newWagon(comfort, seats);
        logger.trace(wagon);
        logger.trace("Passenger " + comfort + ", baggage: " + baggage + " kg");
        double result = 0.0;
        try {
            result = wagon.boardingOfPassenger(comfort, baggage);
            logger.trace("Boarding was successful. The passenger tacked into wagon " + result + " kg");
        } catch(WagonException we) {
            logger.error(we);
        }

        Assert.assertEquals(result, expect, 0.001);
        logger.trace("End amountOfBaggagePassengerCanTake");
    }

    @DataProvider(name = "data-provider-boardingWithWrongComfortLevel")
    public Object[][] dataProviderForBoardingWithWrongComfortLevel() {
        return new Object[][]{
                {PassengerWagon.ComfortLevel.SHARED, PassengerWagon.ComfortLevel.COMPARTMENT},
                {PassengerWagon.ComfortLevel.RESERVED_SEATS, PassengerWagon.ComfortLevel.DINING},
                {PassengerWagon.ComfortLevel.COMPARTMENT, PassengerWagon.ComfortLevel.RESERVED_SEATS},
                {PassengerWagon.ComfortLevel.SLEEPING, PassengerWagon.ComfortLevel.DINING},
                {PassengerWagon.ComfortLevel.DINING, PassengerWagon.ComfortLevel.SLEEPING}
        };
    }

    @Test(expectedExceptions = WagonException.class, dataProvider = "data-provider-boardingWithWrongComfortLevel")
    public void boardingWithWrongComfortLevel(PassengerWagon.ComfortLevel wagonComfort,
                                              PassengerWagon.ComfortLevel passengerComfort)
            throws WagonException {

        logger.trace("Begin boardingWithWrongComfortLevel");
        int seats = (int)(Math.random() * 50.0 + 1.0);
        PassengerWagon wagon = WagonFactory.newWagon(wagonComfort, seats);
        logger.trace(wagon);

        logger.trace("Boarding Passenger with " + passengerComfort);
        double baggage = wagon.boardingOfPassenger(passengerComfort, 10);
        logger.trace("Passenger tacked into train only " + baggage + " kg of baggage ");

        logger.trace(wagon);
        logger.trace("End boardingWithWrongComfortLevel");
    }

    @Test(expectedExceptions = WagonException.class)
    public void boardingWithNegBaggage()
            throws WagonException {

        logger.trace("Begin boardingWithNegBaggage");
        int seats = (int)(Math.random() * 50.0 + 1.0);
        PassengerWagon.ComfortLevel comfort = PassengerWagon.ComfortLevel.RESERVED_SEATS;
        PassengerWagon wagon = WagonFactory.newWagon(comfort, seats);
        wagon.boardingOfPassenger(comfort, -10.0);
        logger.trace("End boardingWithNegBaggage");
    }

    @Test(expectedExceptions = WagonException.class)
    public void boardingOfUnlimitedPassengers()
            throws WagonException {
        logger.trace("Begin boardingOfUnlimitedPassengers");
        WagonFactory.newWagonWithPassengers(PassengerWagon.ComfortLevel.SHARED, 20, 21);
        logger.trace("End boardingOfUnlimitedPassengers");
    }

    @DataProvider(name = "data-provider-deboarding")
    public Object[][] dataProviderForDeboardingOneByOne() {
        // ATTENTION: baggage weight should be less then allowed weight
        ArrayList<Object[]> params = new ArrayList<>();
        for(PassengerWagon.ComfortLevel comfort : PassengerWagon.ComfortLevel.values()) {
            params.add(
                        new Object[] {
                        comfort,
                        Math.random() * comfort.allowedBaggageWeight(),
                        Math.random() * comfort.allowedBaggageWeight(),
                        Math.random() * comfort.allowedBaggageWeight(),
                        Math.random() * comfort.allowedBaggageWeight(),
                        Math.random() * comfort.allowedBaggageWeight()
                        });
        }

        return params.toArray(new Object[][]{});
    }

    @Test(dataProvider = "data-provider-deboarding")
    public void deboardingOneByOne(PassengerWagon.ComfortLevel comfort, double... baggages)
            throws WagonException {
        logger.trace("Begin deboardingOneByOne");
        logger.trace("Passengers enter to wagon with baggages: " + Arrays.toString(baggages));
        PassengerWagon wagon = WagonFactory.newWagonWithPassengers(comfort, 20, baggages);
        logger.trace(wagon);
        Assert.assertEquals(wagon.getPassengersCount(), baggages.length);

        logger.trace("Now deboarding passengers One By One");
        List<Double> unloadBaggages = new ArrayList<>();
        int i = 1;
        while( wagon.getPassengersCount() > 0 ) {
            double baggage = wagon.deboardingOfPassenger();
            logger.trace("Passenger №" + i + " leaves the wagon with " + baggage + " kg of baggage");
            unloadBaggages.add(0, baggage);
            i++;
        }
        logger.trace(wagon);

        Assert.assertEquals(unloadBaggages.size(), baggages.length);
        for(i = 0; i < unloadBaggages.size(); i++) {
            Assert.assertEquals(baggages[i], unloadBaggages.get(i), 0.001);
        }
        logger.trace("End deboardingOneByOne");
    }

    @Test(dataProvider = "data-provider-deboarding")
    public void deboardingEveryOne(PassengerWagon.ComfortLevel comfort, double... baggages)
            throws WagonException {
        logger.trace("Begin deboardingEveryOne");
        logger.trace("Passengers enter to wagon with baggages: " + Arrays.toString(baggages));
        PassengerWagon wagon = WagonFactory.newWagonWithPassengers(comfort, 20, baggages);
        logger.trace(wagon);
        Assert.assertEquals(wagon.getPassengersCount(), baggages.length);

        logger.trace("Now deboarding everyone");
        double[] unloadBaggages = wagon.deboardingOfEveryone();
        logger.trace("Passengers leave the wagon with baggages: " + Arrays.toString(unloadBaggages));
        logger.trace(wagon);

        Assert.assertEquals(unloadBaggages.length, baggages.length);
        for(int i = 0; i < unloadBaggages.length; i++) {
            Assert.assertEquals(baggages[i], unloadBaggages[i], 0.001);
        }
        logger.trace("End deboardingEveryOne");
    }

    @Test(expectedExceptions = WagonException.class)
    public void deboardingOneByOneFromEmptyWagon()
            throws WagonException {
        logger.trace("Begin deboardingFromEmptyWagon");
        PassengerWagon w = WagonFactory.newWagon(PassengerWagon.ComfortLevel.SHARED, 20);
        w.deboardingOfPassenger();
        logger.trace("End deboardingFromEmptyWagon");
    }

    @Test(expectedExceptions = WagonException.class)
    public void deboardingEveryOneFromEmptyWagon()
            throws WagonException {
        logger.trace("Begin deboardingFromEmptyWagon");
        PassengerWagon w = WagonFactory.newWagon(PassengerWagon.ComfortLevel.SHARED, 20);
        w.deboardingOfEveryone();
        logger.trace("End deboardingFromEmptyWagon");
    }
}
