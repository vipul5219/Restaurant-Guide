package ca.mobile.restaurantguide;

public class RestaurantDatabase {
    int id;
    String name,description,address,tags;
    Double rating;

    public RestaurantDatabase(int id,String name, String address, String description, String tags, Double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.tags = tags;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "RestaurantDatabase{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", tags='" + tags + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getTags() {
        return tags;
    }

    public Double getRating() {
        return rating;
    }



}




