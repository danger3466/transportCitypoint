package pro.nvart.apps.transportCitypoint.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Delor {
    static private String userAgent;
    static private String baseURL;
    static private String login;
    static private String password;
    static private String session;

    public Delor() {
        setUserAgent("Mozilla/5.0 (X11; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0");
        setBaseURL("https://lk.okis-s.ru/");
    }

    public Delor(String login, String password) throws IOException {
        setUserAgent("Mozilla/5.0 (X11; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0");
        setBaseURL("https://lk.okis-s.ru/");
        setLogin(login);
        setPassword(password);
        this.session = getSession();
    }

    public static String getSession() throws IOException {
        String cookie = null;
        if (session == null) {
            Connection.Response res = Jsoup.connect(baseURL)
                    .method(Connection.Method.GET)
                    .userAgent(userAgent)
                    .execute();
            Document doc = res.parse();
            cookie = res.cookie("PSID");
            //session = doc.select("[name=\"usersession\"]").val();

            doc = Jsoup.connect(baseURL)
                    .userAgent(userAgent)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .cookie("PSID", cookie)
                    .referrer(baseURL)
                    .data("login", login)
                    .data("password", password)
                    .data("usersession", cookie)
                    .data("enteruser", "%D0%92%D0%A5%D0%9E%D0%94+%C2%A0%3E")
                    .post();
        } else cookie = session;
        return cookie;
    }

    public long[] getCards() throws IOException {
        Document doc = Jsoup.connect(baseURL + "reports")
                .userAgent(userAgent)
                .cookie("PSID", getSession())
                .get();

        Element select = doc.select("select#selectcardnumber").first();
        Elements option = select.select("option");
        long[] ret = new long[option.size()-1];
        int i = 0;
        for(Element el : option){
            if (!el.val().equals("0")) {
                ret[i] = Long.parseLong(el.val());
                i++;
                //System.out.println(el.val() + " = " + el.text());
            }
        }
        return ret;
    }

    enum enumDetail {
        date,
        volume,
        product
    }

    public ArrayList<HashMap<enumDetail, String>> getDetail(long card) throws IOException {
        ArrayList arrayList = new ArrayList<HashMap<enumDetail, String>>();
        Document doc = Jsoup.connect(baseURL + "view/common/php/reports.lib.php")
                .userAgent(userAgent)
                .header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
                .cookie("PSID", getSession())
                .referrer(baseURL)
                .data("action", "bycards_card")
                .data("period", "-1")
                .data("valute", "vtRuble")
                .data("card", Long.toString(card))
                .data("lines", "value%3D\\'50\\'")
                .data("page", "1")
                .data("sortmarker", "-2")
                .data("sortcolumn", "1")
                .post();
        Elements rows = doc.body().selectFirst("table.existclients").select("tr");
        for (Element row : rows) {
            Elements columns = row.select("td");
            if (columns.size() > 0) {
                String date = columns.get(0).text();
                if (date.equals("ИТОГО:")) return arrayList;
                String product = columns.get(5).text();
                String volume = columns.get(7).text();

                HashMap map = new HashMap<enumDetail, String>();
                map.put(enumDetail.date, date);
                map.put(enumDetail.product, product);
                map.put(enumDetail.volume, volume);
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    public static String getUserAgent() {
        return userAgent;
    }

    public static void setUserAgent(String userAgent) {
        Delor.userAgent = userAgent;
    }

    public static String getBaseURL() {
        return baseURL;
    }

    public static void setBaseURL(String baseURL) {
        Delor.baseURL = baseURL;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Delor.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Delor.password = password;
    }
}
