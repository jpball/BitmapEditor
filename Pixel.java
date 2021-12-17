public class Pixel {
    //==================
    // DATA MEMBERS
    //==================
    private int rgbVal;
    private int redMask =   0x00FF0000;
    private int greenMask = 0x0000FF00;
    private int blueMask =  0x000000FF;

    //==================
    // METHODS
    //==================
    public Pixel(){
        setRed(0);
        setGreen(0);
        setBlue(0);
    }
    //--
    // Sets the rgb value in a single function (using the others)
    public void setAll(int r, int g, int b){
        setRed(r);
        setGreen(g);
        setBlue(b);
    }
    //--
    // Returns pixel rgb values in hexcode (Ex. #FFA3B4)
    public String toString(){
        return String.format("#%06X", rgbVal);
    }
    //==================
    // GETTERS & SETTERS
    //==================
    public int getRed(){
        // Nothing  | red      | green    | blue 
        // 00000000 | 00011000 | 00000000 | 00000000    rgbVal  (Ex. r: 24)
        // 00000000 | 11111111 | 00000000 | 00000000    redMask
        return ((rgbVal & redMask)  >> 16);
    }
    //--
    public void setRed(int val){
        // Nothing  | red      | green    | blue 
        // 00000000 | 00011000 | 00000000 | 00000000    rgbVal  (Ex. r: 24)
        // 11111111 | 00000000 | 11111111 | 11111111    ~redMask
        if(val >= 0 && val <= 255){
            rgbVal = (rgbVal & ~redMask) | (val << 16);
        }
    }
    //--
    public int getGreen(){
        // Nothing  | red      | green    | blue 
        // 00000000 | 00000000 | 00110000 | 00000000    rgbVal  (Ex. g: 24)
        // 00000000 | 00000000 | 11111111 | 00000000    greenMask
        return ((rgbVal & greenMask)  >> 8);
    }
    //--
    public void setGreen(int val){
        // Nothing  | red      | green    | blue 
        // 00000000 | 00000000 | 00011000 | 00000000    rgbVal  (Ex. g: 24)
        // 11111111 | 11111111 | 00000000 | 11111111     ~greenMask
        if(val >= 0 && val <= 255){
            rgbVal = (rgbVal & ~greenMask) | (val << 8);
        }
    }
    //--
    public int getBlue(){
        // Nothing  | red      | green    | blue 
        // 00000000 | 00000000 | 00000000 | 00011000    rgbVal  (Ex. b: 24)
        // 00000000 | 00000000 | 00000000 | 11111111    greenMask
        return (rgbVal & blueMask);
    }
    //--
    public void setBlue(int val){
        // Nothing  | red      | green    | blue 
        // 00000000 | 00000000 | 00000000 | 00011000    rgbVal  (Ex. b: 24)
        // 11111111 | 11111111 | 11111111 | 00000000     ~blueMask
        if(val >= 0 && val <= 255){
            rgbVal = (rgbVal & ~blueMask) | (val);
        }
    }
    //--
}