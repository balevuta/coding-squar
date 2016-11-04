package sample.than.codingmyquar;

public class EventClickObject {

    private String urlClicked;
    private int index;

    public EventClickObject(String pUrlClicked, int pIndex) {
        urlClicked = pUrlClicked;
        index = pIndex;
    }

    public String getUrlClicked() {
        return urlClicked;
    }

    public void setUrlClicked(String pUrlClicked) {
        urlClicked = pUrlClicked;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int pIndex) {
        index = pIndex;
    }
}
