package sole.memory.menugui.lang;

import cn.nukkit.utils.Config;
import sole.memory.menugui.MenuGUI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SoleMemory
 * on 2017/11/17.
 */
public class Lang {

    public static HashMap<String,String> lang = new HashMap<>();

    public static void init(String type){
        MenuGUI.getInstance().saveResource("lang/Language_"+type+".yml",false);
        Map<String,Object> map = new Config(MenuGUI.getInstance().getDataFolder()+"/lang/Language_"+type+".yml",Config.YAML).getAll();
        map.forEach((key,value)-> lang.put(key,value.toString()));
    }

    public static String translate(String text){
        return translate(text,"");
    }

    public static String translate(String text,String replace){
        if (!lang.containsKey(text)){
            throw new NullPointerException("Language file is error,please check file");
        }
        if (!replace.equals("")){
            return lang.get(text).replace("%c",replace);
        }
        return lang.get(text);
    }


}
