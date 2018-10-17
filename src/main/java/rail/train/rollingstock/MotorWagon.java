package rail.train.rollingstock;

/**
 * Motor-wagon class
 */
public class MotorWagon extends Wagon {
    public enum EngineType {
        ELECTRIC("Electric"),
        DIESEL("Diesel");

        private String caption;

        EngineType(String caption) {
            this.caption = caption;
        }

        public String toString() {
            return "Engine type: " + caption;
        }
    }

    private EngineType engine;
    private int staffCount;

    public MotorWagon() {
        super();
        this.engine = EngineType.DIESEL;
        this.staffCount = 1;
    }

    public MotorWagon(MotorWagon other) {
        super(other);
        this.engine = other.engine;
        this.staffCount = other.staffCount;
    }



    public MotorWagon(WayType wayType, double length, double width, double height, double weight, int axisCount,
                      boolean automotive, CuplerType cupler, BrakeType brake, EngineType engine, int staffCount) {
        super(wayType, length, width, height, weight, axisCount, automotive, cupler, brake);

        this.engine = engine;
        this.staffCount = staffCount;
    }

    @Override
    public int getComfort() {
        return 1000;
    }

    public EngineType getEngine() {
        return engine;
    }

    public void setEngine(EngineType engine) {
        this.engine = engine;
    }

    @Override
    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return "Motor-" + baseInfo + getEngine();
    }
}
