package recommendationengine.model;

import recommendationengine.enums.Category;

public class Item {
    private String itemId;
    private String addedOn;
    private Integer popularityScore;
    private Category category;

    public Item(String itemId, String addedOn, Integer popularityScore, Category category) {
        this.itemId = itemId;
        this.addedOn = addedOn;
        this.popularityScore = popularityScore;
        this.category = category;
    }

    public String getItemId(){
        return this.itemId;
    }

    public Category getCategory(){
        return this.category;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public Integer getPopularityScore(){
        return this.popularityScore;
    }

    public void setPopularityScore(Integer popularityScore){
        this.popularityScore = popularityScore;
    }

    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(other == null || !(other instanceof Item)){
            return false;
        }
        Item otherItem = (Item) other;
        if(this.itemId == null){
            return otherItem.itemId == null;
        }
        return this.itemId.equals(otherItem.itemId);
    }

    @Override
    public int hashCode(){
        if(this.itemId == null){
            return 0;
        }
        return this.itemId.hashCode();
    }

}
