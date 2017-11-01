package sole.memory.guishop.shop.data;

/**
 * Created by SoleMemory
 * on 2017/11/1.
 */
public class BuyData {

    public ShopData data = null;

    public int count = 0;

    public boolean chooseCount = false;


    public int getCount() {
        return count;
    }

    public ShopData getData() {
        return data;
    }

    public boolean isChooseCount() {
        return chooseCount;
    }

    public void setChooseCount(boolean chooseCount) {
        this.chooseCount = chooseCount;
    }
}
