package sole.memory.guishop.menu.data;

import cn.nukkit.Player;
import money.Money;
import sole.memory.guishop.utils.StringUtils;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/11/3.
 */
public class AdminData {


    public String user = "";

    public boolean isOP = false;

    public String mode = "生存";

    public float health = 20;

    public boolean checkInventory = false;

    public boolean kill = false;

    public boolean kick = false;

    public boolean ban = false;

    public boolean teleport = false;

    public float money = 0;

    public boolean changeMoney = false;

    public boolean changeMode = false;

    public boolean changeOp = false;

    public boolean countError = false;

    public boolean changeHealth = false;

    public void inputPlayer(Player player){
        isOP = player.isOp();
        mode = getPlayerModel(player);
        health = player.getHealth();
        money = Money.getInstance().getMoney(player);
        user = player.getName().toLowerCase();
    }


    private static String getPlayerModel(Player player){
        switch (player.getGamemode()){
            case 0:
                return "生存";
            case 1:
                return "创造";
            case 2:
                return "冒险";
            default:
                return "观察";
        }
    }

    public static int getMode(String str){
        switch (str){
            case "生存":
                return 0;
            case "创造":
                return 1;
            case "冒险":
                return 2;
            default:
                return 3;
        }
    }

    public void changeData(HashMap<Integer,Object> map){
        if (isOP!=(boolean)map.get(1)){
            isOP = (boolean)map.get(1);
            changeOp = true;
        }
        if (!mode.equals(map.get(2).toString())){
            mode = map.get(2).toString();
            changeMode = true;
        }
        if (map.containsKey(10)){
            if (!StringUtils.isNumeric(map.get(10).toString())){
                countError = true;
                money = Float.valueOf(map.get(10).toString());
                return;
            }
            if (money!=Float.valueOf(map.get(10).toString())){
                money = Float.valueOf(map.get(10).toString());
                changeMoney = true;
            }
        }
        checkInventory = (boolean)map.get(5);
        teleport = (boolean)map.get(6);
        kill = (boolean)map.get(7);
        kick = (boolean)map.get(8);
        ban = (boolean)map.get(9);
        if (health==Float.valueOf(map.get(3).toString())){
            health = Float.valueOf(map.get(3).toString());
            changeHealth = true;
        }
    }
}
