package country.customer.project.support;



import java.util.ArrayList;
import java.util.List;

public class World {

    public ArrayList<String> stringList;
    public StringBuilder stringBuilder;
    public String text;
    public String age;
    public ArrayList<String> selectieList;
    public ArrayList<Integer> selectieListPrices;
    public ArrayList<String> minValuesList;


    public World() {
        WorldHelper.setWorld(this);
    }

    public String value;
    public String cost;
    public String name;

    public String search;
    public String result;

    public List<String> List;
}
