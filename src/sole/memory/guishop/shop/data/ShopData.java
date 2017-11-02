package sole.memory.guishop.shop.data;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/10/31.
 */
public class ShopData {



    public float price = 0;

    public String index = "";

    public String name = "";

    public String id = "";

    public String getIndex() {
        return index;
    }



    public float getPrice() {
        return price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("id",id);
        map.put("index",index);
        map.put("price",price);
        return map;
    }
}
