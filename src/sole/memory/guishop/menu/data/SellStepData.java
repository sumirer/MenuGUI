package sole.memory.guishop.menu.data;

/**
 * Created by SoleMemory
 * on 2017/11/2.
 */
public class SellStepData {

    public SellData data = null;

    public int sellCount = 0;

    public float price = 0;


    public void getPrice() {
        this.price = data.price*sellCount;
    }

    public int getSellCount() {
        return sellCount;
    }

    public SellData getData() {
        return data;
    }




}
