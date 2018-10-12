package rail.train;

public class TrainException extends Exception {
    private String description;

    public TrainException(String description) {
        this.description = description;
    }

    public String toString() {
        return "TrainException: " + description;
    }
}
