package rail.train.rollingstock;

public abstract class Stock {
    public enum WayType {
        MONORAIL,
        NARROW_GAUGE,
        BROAD_GAUGE
    }

    private WayType type;
    private double length;
    private double width;
    private double height;
    private double weight;
    private int axisCount;

    public Stock(WayType type, double length, double width, double height, double weight, int axisCount) {
        this.type = type;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.axisCount = axisCount;
    }

    public Stock(Stock other) {
        this.type = other.type;
        this.length = other.length;
        this.width = other.width;
        this.height = other.height;
        this.weight = other.weight;
        this.axisCount = other.axisCount;
    }

    public Stock() {
        this.type = WayType.BROAD_GAUGE;
        this.length = 24.75;
        this.width = 3;
        this.height = 3.1;
        this.weight = 30000;
        this.axisCount = 4;
    }

    public WayType getType() {
        return type;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public int getAxisCount() {
        return axisCount;
    }
}
