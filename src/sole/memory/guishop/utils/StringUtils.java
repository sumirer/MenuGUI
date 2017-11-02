package sole.memory.guishop.utils;

import cn.nukkit.utils.TextFormat;
import sole.memory.guishop.shop.data.SellData;
import sole.memory.guishop.shop.data.ShopData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SoleMemory
 *
 * on 2017/6/10.
 */
public class StringUtils {

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String getShopButtonInfo(ShopData data){
        return "物品名字: "+data.name+"-----回收单价:"+data.price+"   @"+data.index;
    }

    public static String getShopIndexByButtonText(String str){
        return str.substring(str.indexOf("@")+1, str.length());
    }

    public static String getSellInfoStepText(SellData data){
        return "物品名字: "+data.name+"-----回收单价:"+data.price+" @"+data.index;
    }

    public static String getSellInfoIndexText(String str){
        return str.substring(str.indexOf("@")+1, str.length());
    }

    public static String getSellShopButtonInfo(SellData data){
        return TextFormat.RED+"物品名字: "+data.name+"\n"+TextFormat.RESET+TextFormat.AQUA+"单价(/个): "+data.price+"\n"+TextFormat.DARK_GRAY+"商店代号:@"+data.index;
    }

    public static Integer[] getItemInfo(String str){
        String[] ids = str.split(":");
        int id;
        int mate;
        if (ids.length == 1) {
            id = Integer.valueOf(ids[0]);
            mate = 0;
        } else {
            id = Integer.valueOf(ids[0]);
            mate = Integer.valueOf(ids[1]);
        }
        return new Integer[]{id,mate};
    }
}
