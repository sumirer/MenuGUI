package sole.memory.menugui.windows;

/**
 * Created by SoleMemory
 * on 2017/11/17.
 */
public enum  WindowsType {
    SIMPLE("Simple"),
    CUSTOM("Custom"),
    MODAL("Modal");


    WindowsType(String s){
        this.value = s;
    }



    private String value;


    public String getValue() {
        return value;
    }
}
