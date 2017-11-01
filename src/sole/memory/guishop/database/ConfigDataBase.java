package sole.memory.guishop.database;

import cn.nukkit.utils.Config;
import sole.memory.guishop.GUIShop;
import sole.memory.guishop.shop.AdminSetShop;
import sole.memory.guishop.shop.data.ShopData;
import sole.memory.guishop.utils.StringUtils;
import sole.memory.guishop.windows.button.ButtonInfo;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SoleMemory
 * on 2017/10/31.
 */
public class ConfigDataBase {
    private static String path = GUIShop.getInstance().getDataFolder()+"/shop.yml";

    private static HashMap<String,ShopData> data = new HashMap<>();



    @SuppressWarnings("unchecked")
    public static void initData(){
        if (!new File(ConfigDataBase.path).exists()){
            Config config = new Config(path,Config.YAML);
            config.save();
            return;
        }
        Map<String,Object> map= new Config(path,Config.YAML).getAll();

        map.forEach((index,info)->{
           HashMap map1 =  (HashMap<String, Object>) info;
           ShopData data1 = new ShopData();
           data1.index = index;
           data1.id = map1.get("id").toString();
           data1.name = map1.get("name").toString();
           data1.price = Float.valueOf(map1.get("price").toString());
           ConfigDataBase.data.put(index,data1);
        });
        GUIShop.getInstance().getLogger().info("data is load...");
    }

    public static void delteData(String index){
        if (data.containsKey(index)){
            data.remove(index);
            save();
        }
    }

    public static void addNewData(AdminSetShop adminSetShop){
        ShopData data1 = new ShopData();
        data1.index = data.size()+"";
        data1.name = adminSetShop.name;
        data1.id = adminSetShop.id;
        data1.price = adminSetShop.price;
        ConfigDataBase.data.put(data1.index,data1);
        save();
    }

    @SuppressWarnings("unchecked")
    private static void save(){
        HashMap<String,HashMap<String,Object>> map = new HashMap<>();
        data.forEach((index,info)->{
            map.put(index,info.toMap());
        });

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.putAll(map);
        Config config = new Config(path,Config.YAML);
        config.setAll(linkedHashMap);
        config.save();
    }

    public static HashMap<Integer,ButtonInfo> getShopChooseButton(){
        HashMap<Integer,ButtonInfo> map = new HashMap<>();
        data.forEach((index,info)->{
            ButtonInfo buttonInfo = new ButtonInfo();
            buttonInfo.haveImg = false;
            buttonInfo.text = StringUtils.getShopButtonInfo(info);
            map.put(map.size(),buttonInfo);
        });
        return map;
    }

    public static ShopData getShopDataByIndex(String index){
        if (data.containsKey(index)) return data.get(index);
        return new ShopData();
    }

    public static boolean isDataIndex(String string){
        return data.containsKey(string);
    }
}
