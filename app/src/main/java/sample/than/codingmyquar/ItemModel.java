package sample.than.codingmyquar;

public class ItemModel {

    private String name;
    private String url;

    public ItemModel(String pName, String pUrl) {
        name = pName;
        url = pUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
    }
}
