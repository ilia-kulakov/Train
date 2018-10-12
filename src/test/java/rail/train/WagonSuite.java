package rail.train;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rail.train.rollingstock.*;

public class WagonSuite {

    @DataProvider(name = "data-provider-dataProviderNotPassengerWagon")
    public Object[][] dataProviderNotPassengerWagon() {
        return new Object[][]{
                {WagonFactory.newWagon(80000)},
                {WagonFactory.newWagon(MotorWagon.EngineType.DIESEL)}
        };
    }

    @Test(expectedExceptions = WagonException.class, dataProvider = "data-provider-dataProviderNotPassengerWagon")
    public void boardingPassengerToNotPassengerWagon(Wagon wagon) throws WagonException {
        wagon.boardingOfPassenger(PassengerWagon.ComfortLevel.COMPARTMENT, 20.0);
    }

    @DataProvider(name = "data-provider-dataProviderNotFreightWagon")
    public Object[][] dataProviderNotFreightWagon() {
        return new Object[][]{
                {WagonFactory.newWagon(PassengerWagon.ComfortLevel.RESERVED_SEATS, 20)},
                {WagonFactory.newWagon(MotorWagon.EngineType.DIESEL)}
        };
    }

    @Test(expectedExceptions = WagonException.class, dataProvider = "data-provider-dataProviderNotFreightWagon")
    public void loadingCargoToNotFreightWagon(Wagon wagon) throws WagonException {
        wagon.loadingCargo(40000);
    }

    @Test()
    public void boardingPassengerToPassengerWagon() throws WagonException {
        Wagon wagon = WagonFactory.newWagon(PassengerWagon.ComfortLevel.COMPARTMENT, 10);
        wagon.boardingOfPassenger(PassengerWagon.ComfortLevel.COMPARTMENT, 10.0);
        Assert.assertEquals(wagon.getPassengersCount(), 1);
    }

    @Test()
    public void loadingCargoToFreightWagon() throws WagonException {
        double capacity = 80000;
        double cargo = 62000;
        Wagon wagon = WagonFactory.newWagon(capacity);
        wagon.loadingCargo(cargo);
        Assert.assertEquals(wagon.getCargoWeight(), cargo, 0.001);
    }

    @Test(expectedExceptions = WagonException.class,
            dataProvider = "data-provider-dataProviderNotPassengerWagon")
    public void deboardingOfEveryoneFromNotPassengerWagon(Wagon wagon) throws WagonException {
        wagon.deboardingOfEveryone();
    }

    @Test(expectedExceptions = WagonException.class,
            dataProvider = "data-provider-dataProviderNotFreightWagon")
    public void unloadingCargoFromNotFreightWagon(Wagon wagon) throws WagonException {
        wagon.unloadingCargo();
    }

}
