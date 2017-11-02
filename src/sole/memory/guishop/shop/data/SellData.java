package sole.memory.guishop.shop.data;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/11/2.
 */
public class SellData {

    public String index = "";

    public float price = 0;

    public String id = "";

    public String name = "";

    //when pick player inventory item use it..





    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("id",id);
        map.put("index",index);
        map.put("price",price);
        return map;
    }



}
