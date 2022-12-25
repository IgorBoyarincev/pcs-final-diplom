import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    static int port = 8989;

    public static void main(String[] args) throws Exception {
/**
        try {
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            //BooleanSearchEngine.wordIndexingStorage.forEach((k,v)-> System.out.println(k+" "+v));
            List<PageEntry> resultList = engine.search("бизнес");
            int size = resultList.size();
            System.out.println("бизнес");
            System.out.print("[");
            for (PageEntry pageEntry : resultList) {
                size -= 1;
                if (size == 0) {
                    System.out.print(pageEntry.toString());
                } else {
                    System.out.print(pageEntry.toString() + ", ");
                }
            }
            System.out.println("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------------------");
*/
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            BooleanSearchEngine.wordIndexingStorage.forEach((k, v) -> System.out.println(k + " " + v));
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String word = in.readLine();
                    List<PageEntry> page = engine.search(word);
                    if (page == null && page.isEmpty()) {
                        out.println("Совпадения отсутствуют");
                    }
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(page);
                    out.println(json + "\n");
                    out.flush();
                    System.out.println();
                    System.out.println(json);
                }
            }
        } catch (IOException e) {
            System.out.println("can't start");
            e.printStackTrace();
        }
    }
}