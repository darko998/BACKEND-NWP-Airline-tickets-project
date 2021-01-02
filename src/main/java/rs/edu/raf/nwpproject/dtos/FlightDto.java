package rs.edu.raf.nwpproject.dtos;

public class FlightDto {

    private Long id;
    private int originCityId;
    private String originCityName;
    private int destinationCityId;
    private String destinationCityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOriginCityId() {
        return originCityId;
    }

    public void setOriginCityId(int originCityId) {
        this.originCityId = originCityId;
    }

    public int getDestinationCityId() {
        return destinationCityId;
    }

    public void setDestinationCityId(int destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    @Override
    public String toString() {
        return "FlightDto{" +
                "id=" + id +
                ", originCityId=" + originCityId +
                ", originCityName='" + originCityName + '\'' +
                ", destinationCityId=" + destinationCityId +
                ", destinationCityName='" + destinationCityName + '\'' +
                '}';
    }
}
