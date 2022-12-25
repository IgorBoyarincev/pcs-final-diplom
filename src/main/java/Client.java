import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        int port = 8989;
        String host = "localhost";
        try (
                Socket clientSocket = new Socket("localhost", 8989);
                Scanner scan = new Scanner(System.in);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("INPUT WORD FOR SEARCH: ");
            String word = scan.nextLine();
            out.write(word + "\n");
            out.flush();

            String page = in.readLine();
            List<PageEntry> list = new ArrayList<>();
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(page);
                JSONArray jsonArray = (JSONArray) obj;
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                for (Object oneObject : jsonArray) {
                    JSONObject jsonObject = (JSONObject) oneObject;
                    PageEntry pageEntry = gson.fromJson(String.valueOf(jsonObject), PageEntry.class);
                    list.add(pageEntry);
                }
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(list);
            System.out.println(json);
        }
    }
}



