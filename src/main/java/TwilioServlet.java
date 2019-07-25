import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

import static spark.Spark.get;
import static spark.Spark.post;

public class TwilioServlet {

    public static void main(String[] args) {

        ArrayList<String> items = new ArrayList<>();
        items.add("buy apples");
        items.add("buy peaches");
        items.add("buy oranges");


        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            res.type("application/xml");
            System.out.println(req.body());

            if(req.body().contains("Add")){
                String messageAdd = req.body();
                String[] messageSPLIT = messageAdd.split(" ");
                List<String> itemList = Arrays.asList(messageSPLIT);
                String newItem ="";

                for(int i = 0; i<itemList.size();i++)
                {
                    if(itemList.get(i).equals("Add"))
                    {
                        itemList.remove(i);
                    }
                }

                for(int i = 0; i<itemList.size();i++)
                {
                    newItem += (itemList.get(i)+" ");
                }
                items.add(newItem);

                Body body = new Body
                        .Builder("The item has been added!")
                        .build();
                Message sms = new Message
                        .Builder()
                        .body(body)
                        .build();
                MessagingResponse twiml = new MessagingResponse
                        .Builder()
                        .message(sms)
                        .build();
                return twiml.toXml();
            }
            else if(req.body().contains("List")){
                String output = "";
                for(int i = 0; i<items.size();i++)
                {
                    output +=((i+1)+". "+ items.get(i)+" ");
                }
                Body body = new Body
                        .Builder(output)
                        .build();
                Message sms = new Message
                        .Builder()
                        .body(body)
                        .build();
                MessagingResponse twiml = new MessagingResponse
                        .Builder()
                        .message(sms)
                        .build();
                return twiml.toXml();
            }
            else if(req.body().contains("Remove")){
                String message = req.body();
                String[] messageSPLIT = message.split(" ");
                int element = 0;

                for(int i = 0; i<messageSPLIT.length;i++)
                {
                    try
                    {
                        Integer.parseInt(messageSPLIT[i]);
                        element = Integer.parseInt(messageSPLIT[i]);
                        break;
                    }
                    catch (NumberFormatException ex)
                    {
                        continue;
                    }
                }
                items.remove(element);
                Body body = new Body
                        .Builder("Item "+element+" was removed")
                        .build();
                Message sms = new Message
                        .Builder()
                        .body(body)
                        .build();
                MessagingResponse twiml = new MessagingResponse
                        .Builder()
                        .message(sms)
                        .build();
                return twiml.toXml();
            }
            else
            {
                Body body = new Body
                        .Builder("Bye!")
                        .build();
                Message sms = new Message
                        .Builder()
                        .body(body)
                        .build();
                MessagingResponse twiml = new MessagingResponse
                        .Builder()
                        .message(sms)
                        .build();
                return twiml.toXml();
            }
        });
    }
}