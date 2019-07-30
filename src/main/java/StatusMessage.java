//https://stackoverflow.com/questions/54444937/unable-to-extract-body-from-twilio-request
//TODO BOT
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import com.twilio.twiml.MessagingResponse;
import static spark.Spark.get;
import static spark.Spark.post;

public class StatusMessage {

    public static void main(String[] args) {
        //initialize base list of items
        ArrayList<String> items = new ArrayList<>();
        items.add("buy apples");
        items.add("buy peaches");
        items.add("buy oranges");

        //web output
        get("/", (req, res) -> "Hello Web");

        //method to recieve incomming messages
        post("/sms", (req, res) -> {
            res.type("application/xml");

            //req.body() gets the entire body of the message: ToCountry=GB&ToState=&SmsMessageSid=SMc52bea78ca1df688d...
            //converts the body of the message filtered in a clear text
            String bodymessage = req.queryParams("Body");

            //For quest Message Received, paste the MessageSID and sig
            //Could not figure out how to code this into the message header
            //Could not figure out how to get the status of the response message
            String messageSID = req.queryParams("MessageSid");
            String sig = req.headers("X-Twilio-Signature");
//            System.out.println(messageSID);
//            System.out.println(sig);
//==========Add=========================================================================================================
            if(bodymessage.contains("add")){
                String[] messageSPLIT = bodymessage.split(" ");
                List<String> itemList = new ArrayList<>();
                String addItem ="";

                for(int i = 0; i <messageSPLIT.length; i++)
                {
                    itemList.add(messageSPLIT[i]);
                }
                itemList.remove("add");

                for(int i = 0; i<itemList.size();i++)
                {
                    if(i == itemList.size()-1)
                    {
                        addItem += (itemList.get(i));
                    }
                    else
                    {
                        addItem += (itemList.get(i)+" ");
                    }
                }

                items.add(addItem);

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

//==========List========================================================================================================
            else if(bodymessage.contains("list")){
                String output = "";
                for(int i = 0; i<items.size();i++)
                {
                    output +=((i+1)+". "+ items.get(i)+" ");
                }
                Body body = new Body
                        .Builder(messageSID+"\n"+sig+"\n"+output+"-delivered")
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

//==========Remove======================================================================================================
            else if(bodymessage.contains("remove")){
                String[] messageSPLIT = bodymessage.split(" ");
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
                items.remove(element-1);
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

//==========Fallback Output=============================================================================================
            else
            {
                Body body = new Body
                        .Builder("What?")
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

//==========Status======================================================================================================
        post("/status", (req, res) -> {
            res.status(200);

            String statusmessage = req.queryParams("SmsStatus");
            {
                Body body = new Body
                        .Builder(statusmessage)
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