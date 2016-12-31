package utils;

import org.apache.commons.lang3.StringUtils;
import page.WikiConstants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Andrew on 12/27/2016.
 */
public class PageUtils {

//    public static String getTitleCase(String s) {
//        String[] split = s.toLowerCase().split(" ");
//        String titleCase = "";
//        for (int i = 0; i < split.length; i++) {
//            String word = split[i];
//            char lower_char = word.charAt(0);
//            titleCase += word.replaceFirst("" + lower_char, "" + Character.toUpperCase(lower_char));
//            if(i + 1 < split.length) titleCase += " ";
//
//        }
//        return titleCase;
//    }

    public static String getNPCSafeName(String npc) {
        return StringUtils.capitalize(npc).replaceAll(" ", "_");
    }

    /**
     * @param npc String the npc's name
     * @return String returns the pagesource for the npc
     * @throws IOException thrown if the url doesn't exist.
     */
    public static final String getNPCPageSource(String npc) throws IOException {
        URL npcUrl = null;
            npcUrl = new URL(WikiConstants.baseUrl + getNPCSafeName(npc));
        System.out.println("NPC URL: " + npcUrl.toString());
        String pagesource = getPageSource(npcUrl);
        //remove the rare drop table from the page source, given it exists.
        if(pagesource.contains(WikiConstants.rareDropTableId)) {
            pagesource = pagesource.substring(0, pagesource.indexOf(WikiConstants.rareDropTableId));
        }
        return pagesource;
    }

    public static final String getWikiPageSource(String wikiExtension) {
        try {
            URL url = new URL(WikiConstants.baseUrl + wikiExtension);
            return getPageSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns the url's page source
     * @param pageUrl the url of the page to get the source of.
     * @return the url's source.
     */
    public static String getPageSource(URL pageUrl) throws IOException {
        URLConnection connection = pageUrl.openConnection();
        DataInputStream in = new DataInputStream(connection.getInputStream());
        byte[] temp = new byte[connection.getContentLength()];
        in.readFully(temp);
        return new String(temp);
    }
}
