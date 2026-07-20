package model;

public class OrderItem {
    String id;
    private MenuItem menuItem;
    private Integer quantity;

    public OrderItem(String id, MenuItem menuItem, Integer quantity){
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public String getId(){
        return this.id;
    }

    public MenuItem getMenuItem(){
        return this.menuItem;
    }

    public Integer getQuantity(){
        return this.quantity;
    }
}
