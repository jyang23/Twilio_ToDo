import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {


    public static void main(String[] args)
    {
        ArrayList<String> items = new ArrayList<>();
        items.add("buy apples");
        items.add("buy peaches");
        items.add("buy oranges");

        String messageAdd = "Add buy milk";
        String[] messageSPLIT = messageAdd.split(" ");
        List<String> itemList = Arrays.asList(messageSPLIT);
        String newItem ="";
        itemList.remove(itemList.indexOf("Add"));


        for(int i = 0; i<itemList.size();i++)
        {
            if(itemList.get(i).equals("Add"))
            {

                itemList.remove(itemList.indexOf("Add"));
            }
        }

        for(int i = 0; i<itemList.size();i++)
        {
            newItem += (itemList.get(i)+" ");
        }
        items.add(newItem);
    }
}
