package ziruiz.cmu.edu.project4android;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by ziruizhu on 4/8/17.
 */

public class GetWiki {
    MainActivity ma = null;

    /*
     * search is the public GetWiki method.  Its arguments are the search term, and the MainActivity object that called it.  This provides a callback
     * path such that the wikiReady method in that object is called when the wiki is available from the search.
     */
    public void search(String searchTerm, MainActivity ma) {
        System.out.println("hit! getwiki search........");
        this.ma = ma;
        new AsyncFlickrSearch().execute(searchTerm);
    }

    /*
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncFlickrSearch extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return search(urls[0]);
        }

        protected void onPostExecute(String wiki) {
            ma.wikiReady(wiki);
        }

        /*
         * Search heroku for the searchTerm argument, and return a String that can be put in an TextView
         */
        private String search(String searchTerm) {
            StringBuilder result = new StringBuilder();
            Document doc = getRemoteXML("https://tranquil-coast-92159.herokuapp.com/Project4Task1Servlet?searchWord=" + searchTerm);
            Element rootElement = doc.getDocumentElement();
            NodeList nl = rootElement.getElementsByTagName("wiki");
            if (nl.getLength() == 0) {
                return null; // no pictures found
            } else {
                int item = new Random().nextInt(nl.getLength()); //choose a random picture
                Element e = (Element) nl.item(item);
                String title = e.getElementsByTagName("title").item(0).getTextContent();
                System.out.println("title:"+title);
                String snippet = e.getElementsByTagName("snippet").item(0).getTextContent();
                System.out.println("snippet:"+snippet);
                result.append("Title: " + title + "\nSnippet: " + snippet);

            }
            System.out.println("My result:!!!!"+result.toString());
            return result.toString();
        }

        /*
         * Given a url that will request XML, return a Document with that XML, else null
         */
        private Document getRemoteXML(String urlString) {
            try {
               String response = "";
                URL url = new URL(urlString);
                System.out.println("@@@"+urlString);
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // SET HEADER
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "text/plain");
                    // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String str;
                // Read each line of "in" until done, adding each to "response"
                while ((str = in.readLine()) != null) {
                    // str is one line of text readLine() strips newline characters
                    response += str;
                }
                in.close();
                System.out.println("!!!"+response);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource(new ByteArrayInputStream(response.getBytes("utf-8")));
                System.out.println(is.toString());
                return db.parse(is);
            } catch (Exception e) {
                System.out.print("Yikes, hit the error: " + e);
                return null;
            }
        }

    }
}
