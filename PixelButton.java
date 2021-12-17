import javax.swing.JButton;

public class PixelButton extends JButton{
    private int xVal;
    private int yVal;
    //--
    public PixelButton(int x, int y){
        xVal = x;
        yVal = y;
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
    public void setXVal(int val){
        if(val >= 0){
            xVal = val;
        }
        else{
            xVal = 0;
        }
    }
    //--
    public void setYVal(int val){
        if(val >= 0){
            yVal = val;
        }
        else{
            yVal = 0;
        }
    }
}
