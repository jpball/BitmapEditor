import java.awt.Color;
public class BitmapEdit implements Modifier{
    private int xVal;
    private int yVal;
    private Color color;
    public BitmapEdit(int x, int y, Color col){
        setXVal(x);
        setYVal(y);
        setColor(col);
    }
    @Override
    public void modify(BitmapEdit bEdit) {
        // Do nothing
    }
    //--
    public int getXVal(){
        return xVal;
    }
    //--
    public int getYVal(){
        return yVal;
    }
    //--
    public Color getColor(){
        return color;
    }
    //--
    public void setXVal(int val){
        xVal = val;
    }
    //--
    public void setYVal(int val){
        yVal = val;
    }
    //--
    public void setColor(Color col){
        color = col;
    }
}
