package recommendationengine.model;

public class User {
    private String userId;
    private String createdOn;

    public User(String userId, String createdOn) {
        this.userId = userId;
        this.createdOn = createdOn;
    }

    public String getUserId(){
        return this.userId;
    }

    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(other == null || !(other instanceof User)){
            return false;
        }
        User otherUser = (User) other;
        if(this.userId == null){
            return otherUser.userId == null;
        }
        return this.userId.equals(otherUser.userId);
    }

    @Override
    public int hashCode(){
        if(this.userId == null){
            return 0;
        }
        return this.userId.hashCode();
    }

}
