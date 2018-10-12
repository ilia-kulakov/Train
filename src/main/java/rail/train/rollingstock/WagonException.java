package rail.train.rollingstock;

public class WagonException extends Exception {
    private String description;

    public WagonException(String description) {
        this.description = description;
    }

    public String toString() {
        return "WagonException: " + description;
    }
}
