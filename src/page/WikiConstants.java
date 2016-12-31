package page;

import drops.DropProbability;
import drops.item.Item;
import drops.npc.NPCDrop;
import javafx.util.Pair;
import org.apache.commons.lang3.StringEscapeUtils;
import utils.ProbabilityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew on 12/24/2016.
 */
public class WikiConstants {

    private WikiConstants() {}

    public static final String baseUrl, rareDropTableUrl;
    public static final String rareDropTableId, rareDropTableName;
    /**
     * This number makes every other drop probability.
     */
    public static final double COMMON_PROB;
    /**
     * This pattern uses a regex to find all the drop names with their drop rarity from a url of an NPC. <br />
     * Matcher group layout: <br />
     * 1) Item Name <br />
     * 2) Item Image URL <br />
     * 3) Quantity <br />
     * 4) Probability <br />
     */
    private static final Pattern dropPattern;
    public static final int notSoldPrice;

    private static final int DROP_NAME = 1, DROP_IMAGE = 2, DROP_MIN_AMT = 3, DROP_MAX_AMT = 4, DROP_OPTIONAL_AMT = 5, DROP_PROB = 6, DROP_PRICE = 7;

    static {
        baseUrl = "http://2007.runescape.wikia.com/wiki/";
        rareDropTableUrl = "Rare_drop_table";
        rareDropTableId = "id=\"Rare_drop_table\"";
        rareDropTableName = "Rare drop table";
        dropPattern = Pattern.compile(
"title=\"([^\"]*)\"\\s*><img\\s*src=\".*\"\\s*data-src=\"([^\"]*)\".*\\n.*\\n.*<td>\\s*(\\d{1,3}[,\\d{3}]*)–*(\\d{1,3}[,\\d{3}]*)*;*\\s*(\\d{1,3}[,\\d{3}]*)*[\\s()\\w]*\\n.*</td><td.*\">\\s(.*)\\n.*>\\s(.*)"
        );
        notSoldPrice = 0;
        COMMON_PROB =  0.7507331378299120;
//        COMMON_PROB = 0.4047424366312347;
    }

    private static Map<String, Item> itemMap = new HashMap<>();

    public static List<NPCDrop> getNPCDrops(String pagesource, String npcName) {
        Matcher matcher = WikiConstants.dropPattern.matcher(pagesource);
        List<NPCDrop> dropList = new ArrayList<>();
        matcherLoop: while(matcher.find()) {
            Item item;
            String item_name = StringEscapeUtils.unescapeHtml4(matcher.group(DROP_NAME));
            if (!itemMap.containsKey(item_name)) {
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new URL(matcher.group(DROP_IMAGE)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                item = new Item(item_name, image);
                itemMap.put(item_name, item);
            } else
                item = itemMap.get(item_name);
            try {
                int min, max, optionalAmt = 0;
                double probability;
                min = NumberFormat.getNumberInstance(Locale.US).parse(matcher.group(DROP_MIN_AMT)).intValue();
                if (matcher.group(DROP_MAX_AMT) != null)
                    max = NumberFormat.getNumberInstance(Locale.US).parse(matcher.group(DROP_MAX_AMT)).intValue();
                else
                    max = min;
                if (matcher.group(DROP_OPTIONAL_AMT) != null)
                    optionalAmt = NumberFormat.getNumberInstance(Locale.US).parse(matcher.group(DROP_OPTIONAL_AMT)).intValue();

                Pair<Double, Double> probabilityInfo = ProbabilityUtils.getProb(matcher.group(DROP_PROB));
                probability = probabilityInfo.getValue();

                //get market price of item.
                String priceString = matcher.group(DROP_PRICE);
                int price = 1;
                if (priceString.toLowerCase().contains("not sold")) {
                    //if the item is coins, set the price to the max quantity.
                    if (item.name.equalsIgnoreCase("coins"))
                        price = max;
                    else
                        price = notSoldPrice;
                } else {
                    //get the price from the priceString.
                    price = NumberFormat.getNumberInstance(Locale.US).parse(priceString).intValue();
                }

                item.individualPrice = price / min;
                if (item.name.contains("fang")) {
                    System.out.println("fang: " + item.individualPrice);
                }
                if (npcName.equalsIgnoreCase("zulrah") && item.name.equals("Pure essence"))
                    probability = DropProbability.COMMON.probability;
                if(npcName.toLowerCase().contains("barrows")) {
                    String litemName = item.name.toLowerCase();
                    if(litemName.contains("karil") || litemName.contains("ahrim") ||
                            litemName.contains("dharok") || litemName.contains("verac") ||
                            litemName.contains("torag") || litemName.contains("guthan")) {
                        probability = DropProbability.VERY_RARE.probability / 2;
                    } else if(litemName.contains("bolt")) {
                        probability = DropProbability.UNCOMMON.probability;
                    }
                }

                NPCDrop currentDrop = new NPCDrop(item, min, max, optionalAmt, price, probability);
                dropList.add(currentDrop);
//                if(item.name.contains("scale")) {
//                    System.out.println(currentDrop);
//                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dropList;
    }
}
