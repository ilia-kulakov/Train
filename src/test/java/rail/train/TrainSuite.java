package rail.train;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rail.train.rollingstock.MotorWagon;
import rail.train.rollingstock.PassengerWagon.ComfortLevel;
import rail.train.rollingstock.Wagon;
import rail.train.rollingstock.WagonException;

import java.util.List;
import java.util.Random;

public class TrainSuite {
    static final Logger logger = LogManager.getLogger(TrainSuite.class.getName());

    private Train newRandomTrain(MotorWagon.EngineType engine, int[] seatsCount, double[] cargoCapacity) {
        Train train = new Train().joinTail(WagonFactory.newWagon(engine));

        int seatsIndex = 0;
        int capacityIndex = 0;
        while(seatsIndex < seatsCount.length || capacityIndex < cargoCapacity.length) {

            if(Math.random() < 0.5 && seatsIndex < seatsCount.length) {
                // Add passenger wagon with random comfort
                int rndIndex = (int) ( Math.random() * (ComfortLevel.values().length - 1) );
                ComfortLevel comfort = ComfortLevel.fromOrdinal(rndIndex);
                train.joinTail(WagonFactory.newWagon(comfort, seatsCount[seatsIndex]));
                seatsIndex++;

            } else if(capacityIndex < cargoCapacity.length) {
                // Add freight wagon
                train.joinTail(WagonFactory.newWagon(cargoCapacity[capacityIndex]));
                capacityIndex++;
            }
        }

        return train;
    }

    private class PassengerWagonInfo {
        public ComfortLevel comfort;
        public int seatsCount;
        PassengerWagonInfo(ComfortLevel comfort, int seatsCount) {
            this.comfort = comfort;
            this.seatsCount = seatsCount;
        }
    }

    private Train newRandomTrain(MotorWagon.EngineType engine, PassengerWagonInfo[] pwi, double[] cargoCapacity) {
        Train train = new Train().joinTail(WagonFactory.newWagon(engine));

        int pwIndex = 0;    // index for passenger wagons
        int fwIndex = 0;    // index for freight wagons
        while(pwIndex < pwi.length || fwIndex < cargoCapacity.length) {

            if(Math.random() < 0.5 && pwIndex < pwi.length) {
                // Add passenger wagon with
                train.joinTail(WagonFactory.newWagon(pwi[pwIndex].comfort, pwi[pwIndex].seatsCount));
                pwIndex++;

            } else if(fwIndex < cargoCapacity.length) {
                // Add freight wagon
                train.joinTail(WagonFactory.newWagon(cargoCapacity[fwIndex]));
                fwIndex++;
            }
        }

        return train;
    }

    @Test
    public void seatsCount() {
        Train train = newRandomTrain(MotorWagon.EngineType.DIESEL,
                new int[] {20, 50, 10, 30, 70, 80, 12},
                new double[] {75000.0, 50000.0, 30000.0, 20000.0});

        Assert.assertEquals(train.getSeatsCount(), 272);
    }

    @Test
    public void cargoCapacity() {
        Train train = newRandomTrain(MotorWagon.EngineType.DIESEL,
                new int[] {20, 50, 10, 30, 70, 80, 12},
                new double[] {75000.0, 50000.0, 30000.0, 20000.0});

        Assert.assertEquals(train.getCargoCapacity(), 175000.0, 0.001);
    }

    @DataProvider(name = "dataProviderTrainWithoutSeats")
    public Object[][] dataProviderTrainWithoutSeats() {
        return new Object[][]{
                {newRandomTrain(MotorWagon.EngineType.DIESEL,
                        new int[] {0},
                        new double[] {75000.0, 50000.0, 20000.0})},

                {newRandomTrain(MotorWagon.EngineType.ELECTRIC,
                        new int[] {},
                        new double[] {90000.0, 50000.0, 30000.0, 20000.0})}
        };
    }

    @Test(expectedExceptions = TrainException.class,
            dataProvider = "dataProviderTrainWithoutSeats")
    public void boardingPassengerIntoTrainWithoutSeats(Train train)
            throws TrainException{
        train.boardingOfPassenger(ComfortLevel.SHARED, 0.0);
    }

    @DataProvider(name = "dataProviderTrainWithoutEnoughCargoCapacity")
    public Object[][] dataProviderTrainWithoutEnoughCargoCapacity() {
        return new Object[][]{
                {newRandomTrain(MotorWagon.EngineType.DIESEL,
                        new int[] {12, 34, 60},
                        new double[] {0.0, 0.0})},

                {newRandomTrain(MotorWagon.EngineType.DIESEL,
                        new int[] {12, 34, 60},
                        new double[] {})},

                {newRandomTrain(MotorWagon.EngineType.ELECTRIC,
                        new int[] {45, 21, 32},
                        new double[] {2000.0, 3000.0, 5000.0})}
        };
    }

    @Test(expectedExceptions = TrainException.class,
            dataProvider = "dataProviderTrainWithoutEnoughCargoCapacity")
    public void loadingCargoToTrainWithoutEnoughCargoCapacity(Train train)
            throws TrainException{
        train.loadingCargo(10000.1);
    }

    @DataProvider(name = "dataProviderTrainWithPassengerWagonInfo")
    public Object[][] dataProviderTrainWithPassengerWagonInfo() {
        PassengerWagonInfo[] pwis1 = new PassengerWagonInfo[] {
                new PassengerWagonInfo(ComfortLevel.SHARED, 60),
                new PassengerWagonInfo(ComfortLevel.SLEEPING, 12),
                new PassengerWagonInfo(ComfortLevel.RESERVED_SEATS, 8)
        };

        PassengerWagonInfo[] pwis2 = new PassengerWagonInfo[] {
                new PassengerWagonInfo(ComfortLevel.DINING, 0),
                new PassengerWagonInfo(ComfortLevel.COMPARTMENT, 12),
                new PassengerWagonInfo(ComfortLevel.SHARED, 12),
                new PassengerWagonInfo(ComfortLevel.RESERVED_SEATS, 8)
        };

        PassengerWagonInfo[] pwis3 = new PassengerWagonInfo[] {
                new PassengerWagonInfo(ComfortLevel.COMPARTMENT, 30),
                new PassengerWagonInfo(ComfortLevel.SHARED, 46),
                new PassengerWagonInfo(ComfortLevel.DINING, 0),
                new PassengerWagonInfo(ComfortLevel.RESERVED_SEATS, 40)
        };

        return new Object[][]{
                {newRandomTrain(MotorWagon.EngineType.DIESEL,
                        pwis1, new double[] {0.0, 0.0}), pwis1},

                {newRandomTrain(MotorWagon.EngineType.DIESEL,
                        pwis2, new double[] {}), pwis2},

                {newRandomTrain(MotorWagon.EngineType.ELECTRIC,
                        pwis3, new double[] {2000.0, 3000.0, 5000.0}), pwis3}
        };
    }

    @Test(dataProvider = "dataProviderTrainWithPassengerWagonInfo")
    public void boardingToTrainCorrect(Train train, PassengerWagonInfo[] pwis)
            throws TrainException{
        double baggageTotal = 0.0;
        int passengerTotal = 0;
        // Fill the train
        for(PassengerWagonInfo pwi : pwis) {
            for(int i = 0; i < pwi.seatsCount; i++) {
                double baggage = Math.random() * pwi.comfort.allowedBaggageWeight();
                baggageTotal += train.boardingOfPassenger(pwi.comfort, baggage);
                passengerTotal++;
            }
        }
        // Check info
        Assert.assertEquals(train.getPassangersCount(), passengerTotal);
        Assert.assertEquals(train.getCargoWeight(), baggageTotal, 0.001);
    }

    @Test(expectedExceptions = TrainException.class,
            dataProvider = "dataProviderTrainWithPassengerWagonInfo")
    public void boardingUnlimitedToTrain(Train train, PassengerWagonInfo[] pwis)
            throws TrainException{
        // Fill the train
        for(PassengerWagonInfo pwi : pwis) {
            for(int i = 0; i < pwi.seatsCount; i++) {
                double baggage = Math.random() * pwi.comfort.allowedBaggageWeight();
                train.boardingOfPassenger(pwi.comfort, baggage);
            }
        }
        // Add one passenger more (there is an exception here)
        train.boardingOfPassenger(pwis[0].comfort, 0.0);
    }

    private Train[][] randomTrainList(int size) {
        final Random random = new Random();
        Train[][] list = new Train[size][];

        for(int i = 0; i < size; i++) {
            list[i] = new Train[1];

            MotorWagon.EngineType engine = ( random.nextInt(2) == 0 ) ?
                    MotorWagon.EngineType.DIESEL : MotorWagon.EngineType.ELECTRIC;

            int[] seats = new int[random.nextInt(20)];
            for(int j = 0; j < seats.length; j++ ) {
                seats[j] = random.nextInt(50);
            }

            double[] capacities = new double[random.nextInt(10)];
            for(int j = 0; j < capacities.length; j++ ) {
                capacities[j] = random.nextDouble() * 100000.0;
            }

            list[i][0] = newRandomTrain(engine, seats, capacities);
        }

        return list;
    }

    @DataProvider(name = "dataProviderRandomTrain")
    public Object[][] dataProviderRandomTrain() {
        return randomTrainList(10);
    }

    @Test(dataProvider = "dataProviderRandomTrain")
    public void sortComfort(Train train) {
        logger.trace(train);
        train.sortByComfort();
        logger.trace(train);

        for(int i = 0; i < train.length() - 1; i++) {
            int c1 = train.getWagon(i).getComfort();
            int c2 = train.getWagon(i+1).getComfort();
            Assert.assertTrue(c1 >= c2);
        }
    }

    @DataProvider(name = "dataProviderRandomTrainWithPassengers")
    public Object[][] dataProviderRandomTrainWithPassengers() throws WagonException {
        Train[][] list = randomTrainList(10);
        // Boarding passengers
        for(int i = 0; i < list.length; i++) {
            Train t = list[i][0];
            for(int j = 0; j < t.length(); j++) {
                Wagon w = t.getWagon(j);
                if(w.getSeatsCount() > 0) {
                    int pas = (int)(Math.random() * w.getSeatsCount());
                    ComfortLevel comfort = ComfortLevel.fromOrdinal(w.getComfort());
                    double maxWeight = comfort.allowedBaggageWeight();
                    for(int k = 0; k < pas; k++) {
                        double baggage = Math.random() * maxWeight;
                        w.boardingOfPassenger(comfort, baggage);
                    }
                }
            }
        }

        return list;
    }

    @Test(dataProvider = "dataProviderRandomTrainWithPassengers")
    public void listOfWagonsWithPassengersFitInterval(Train train) {
        logger.trace("Begin listOfWagonsWithPassengersFitInterval");
        final int beg = 10;
        final int end = 26;
        logger.trace(train);
        List<Wagon> wagons = train.getWagonsWithPassengers(beg, end);

        logger.trace("List of wagons, which include passengers more than or equal " +
                beg + ", and less than " + end );
        for(int i = 0; i < wagons.size(); i++) {
            logger.trace( (i + 1) + ": " + wagons.get(i));
        }

        logger.trace("Test: List of wagons, which include passengers more than or equal " +
                beg + ", and less than " + end );
        int numFind = 0;
        for(int i = 0; i < train.length(); i++) {
            Wagon w = train.getWagon(i);
            if( beg <= w.getPassengersCount() && w.getPassengersCount() < end ) {
                logger.trace( (i + 1) + ": " + w);
                Assert.assertTrue( wagons.contains(w) );
                numFind++;
            }
        }

        Assert.assertEquals(numFind, wagons.size() );

        logger.trace("End listOfWagonsWithPassengersFitInterval");
    }


}
