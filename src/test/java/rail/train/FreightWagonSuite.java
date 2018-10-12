package rail.train;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rail.train.rollingstock.*;

public class FreightWagonSuite {
    static final Logger logger = LogManager.getLogger(FreightWagonSuite.class.getName());

    @AfterMethod
    private void printLine() {
        logger.trace("-----------------------------------------------------------------------------------------------");
    }

    @DataProvider(name = "data-provider-loadingCargoTestWeight")
    public Object[][] dataProviderLoadingCargoTestWeight() {
        return new Object[][] {
                { 80000, 50000, 50000 },
                { 80000, 100000, 80000 },
                { 0, 80000, 0 }
        };
    }

    @Test(dataProvider = "data-provider-loadingCargoTestWeight")
    public void loadingCargoTestWeight(double capacity, double cargo, double expected) {
        logger.trace("Begin loadingCargoTestWeight");
        logger.trace("Loading cargo. Wagon cargo capacity is " + capacity +
                " kg, cargo weight is " + cargo + " kg");
        FreightWagon wagon = WagonFactory.newWagon(capacity);
        logger.trace(wagon);
        try {
            cargo = wagon.loadingCargo(cargo);
            logger.trace("Loading " + cargo +" was successful.");
        } catch(WagonException we) {
            logger.error(we);
        }
        logger.trace(wagon);
        Assert.assertEquals(wagon.getCargoWeight(), expected, 0.0001);
        logger.trace("Finish Loading cargo");
    }

    @DataProvider(name = "data-provider-loadingNegCargo")
    public Object[][] dataProviderLoadingNegCargo() {
        return new Object[][] {
                { 80000, -50000 },
                { 20000, -100000 },
                { 0, -80000 }
        };
    }

    @Test(dataProvider = "data-provider-loadingNegCargo", expectedExceptions = WagonException.class)
    public void loadingNegCargo(double capacity, double cargo)
            throws WagonException {
        logger.trace("Begin loadingNegCargo");
        logger.trace("Loading cargo. Wagon cargo capacity is " + capacity +
                " kg, cargo weight is " + cargo + " kg");
        FreightWagon wagon = WagonFactory.newWagon(capacity);
        wagon.loadingCargo(cargo);
        logger.trace("Finish loadingNegCargo");
    }

}
