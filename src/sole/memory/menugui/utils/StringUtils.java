package sole.memory.menugui.utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import sole.memory.menugui.menu.data.SellData;
import sole.memory.menugui.menu.data.ShopData;
import sole.memory.menugui.menu.item.ItemName;

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
        return "物品名字: "+data.name+"-----出售单价:"+data.price+"   @"+data.index;
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

    public static String getCommand(String command){

        if (command.substring(0,1).equals(" ")){
            return command.substring(1,command.length());
        }
        return command;
    }

    public static String getPlayerInfo(Player player){
        return "ID: "+player.getName()+"\n\n手机型号: "+player.getLoginChainData().getDeviceModel()+"\n游戏版本:"+player.getLoginChainData().getGameVersion()+"\nIP:"+player.getAddress()+"\n所处地图: "+player.getLevel().getFolderName()+"\n手持物品ID: "+player.getInventory().getItemInHand().getId()+":"+player.getInventory().getItemInHand().getDamage();
    }

    public static String getItemInfo(Item item,Integer index){
        String id;
        if (item.getDamage()==0){
            id= ItemName.getName(item.getId()+"");
        }else {
            id = ItemName.getName(item.getId()+":"+item.getDamage());
        }
        if (id.equals("不存在此物品")){
            id = "未识别物品";
        }
        return "名字:"+id+"   物品ID>#"+item.getId()+":"+item.getDamage()+"#<  数量: "+item.getCount()+" ----@"+index;
    }

    public static Integer[] getButtonItemID(String button){
        return getItemInfo(button.split("#")[1]);
    }

    public static Integer getButtonItemIndex(String button){
        return Integer.valueOf(getSellInfoIndexText(button));
    }

    public static int StringToInteger(String ff){
        return Integer.valueOf(ff.substring(0,ff.indexOf(".")));
    }

    public static boolean isEmail( String str ) {
        // "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        String regex = "^[a-zA-Z0-9][a-zA-Z0-9_\\.]+[a-zA-Z0-9]@[a-z0-9]{2,7}(\\.[a-z]{2,3}){1,3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match=pattern.matcher(str);
        return match.matches();
    }

    public static String getRandonNumber(){
        return String.valueOf((int) ((Math.random()*9+1)*100000));
    }
}
