package ca.mobile.restaurantguide;

public class RestaurantDatabase {
    String name,description,address,tags;
    Double rating;

    public RestaurantDatabase(String name, String description, String address, String tags, Double rating) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.tags = tags;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}




