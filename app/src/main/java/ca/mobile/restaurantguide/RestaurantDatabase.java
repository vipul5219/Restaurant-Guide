package ca.mobile.restaurantguide;

public class RestaurantDatabase {
    private String resName;
    private int id=0;
    private String description;
    private String tags;
    private float rating;
    private  String address;
    public RestaurantDatabase(String resName, String description, String tags,String address) {
        this.resName = resName;
        this.id++;
        this.description = description;
        this.tags = tags;
        this.address=address;

    }

    public String getResName() {
        return resName;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public float getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "RestaurantDatabase{" +
                "resName='" + resName + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
