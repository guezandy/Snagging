package model;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Meal 
 */

@ParseClassName("ClothingEntity")
public class ClothingEntity extends ParseObject {

    public ClothingEntity() {
        // A default constructor is required.
    }

    public String getDescription() {
        return getString("description");
    }

    public float getPrice() {
        return (Float) getNumber("price");
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

}