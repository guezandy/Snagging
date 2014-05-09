package model;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("CartItem")
public class CartItem extends ParseObject {
 
    public CartItem() {
        // A default constructor is required.
    }
 
    public ParseObject getTo() {
        return getParseObject("to");
    }
    public Date getDate(){
        return this.getDate();
    }

}
