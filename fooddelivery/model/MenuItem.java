package model;

public class MenuItem {
    private String id;
    private String name;
    private Double price;
    private boolean isAvailable;

    public MenuItem(String id, String name, Double price, boolean isAvailable){
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Double getPrice(){
        return this.price;
    }

    public boolean getIsAvailable(){
        return this.isAvailable;
    }
    
    
}
