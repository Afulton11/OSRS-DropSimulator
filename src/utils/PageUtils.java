package utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import page.WikiConstants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Andrew Fulton
 */
public final class PageUtils {

    private PageUtils(){}

    /**
     * capitalizes the first character of every word in the npc string and replaces all ' ' with '_'
     * @param npc the name of the npc
     * @return the npc string with the first char capitalized. "General graardor" returns "Dragon_Impling"
     */
    private static String getNPCSafeNameTitleCase(String npc) {
        return WordUtils.capitalize(npc.toLowerCase()).replaceAll(" ", "_");
    }

    /**
     * capitalizes the first character in the npc string and replaces all ' ' with '_'
     * @param npc the name of the npc
     * @return the npc string with the first char capitalized. "Dragon Impling" returns "Dragon_impling"
     */
    private static String getNPCSafeNameCapitalize(String npc) {
        return StringUtils.capitalize(npc.toLowerCase()).replaceAll(" ", "_");
    }

    /**
     * @param npc String the npc's name
     * @return String returns the pagesource for the npc
     * @throws IOException thrown if the url doesn't exist.
     */
    public static String getNPCPageSource(String npc) throws IOException {
        URL npcUrl = new URL(WikiConstants.baseUrl + getNPCSafeNameTitleCase(npc));
        String pageSource;
        try {
            pageSource = getPageSource(npcUrl);
        } catch (IOException e) {
            /*try getting the url with Capitalizing only first letter. b/c ***The wiki is not very consistent on redirecting depending on caps.
            for example, try going to : http://2007.runescape.wikia.com/wiki/General_graardor
            then go to : http://2007.runescape.wikia.com/wiki/General_Graardor
            but then try : http://2007.runescape.wikia.com/wiki/King_Black_Dragon  : http://2007.runescape.wikia.com/wiki/King_black_dragon */
            //so, lets add support for the wiki's inconsistencies.
            npcUrl = new URL(WikiConstants.baseUrl + getNPCSafeNameCapitalize(npc));
            //This is where we may throw our IOException.
            pageSource = getPageSource(npcUrl);
        }
        System.out.println("NPC URL: " + npcUrl.toString());
        //remove the rare drop table from the page source, given it exists.
        if(pageSource.contains(WikiConstants.rareDropTableId)) {
            pageSource = pageSource.substring(0, pageSource.indexOf(WikiConstants.rareDropTableId));
        }
        return pageSource;
    }

    public static String getWikiPageSource(String wikiExtension) {
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
    private static String getPageSource(URL pageUrl) throws IOException {
        URLConnection connection = pageUrl.openConnection();
        DataInputStream in = new DataInputStream(connection.getInputStream());
        byte[] temp = new byte[connection.getContentLength()];
        in.readFully(temp);
        return new String(temp);
    }
}
