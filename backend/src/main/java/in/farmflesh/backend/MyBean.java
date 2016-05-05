package in.farmflesh.backend;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;
    private int myIntData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public int getIntData() {
        return myIntData;
    }

    public void setIntData(int data) {
        myIntData = data;
    }
}