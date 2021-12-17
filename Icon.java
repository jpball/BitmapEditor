import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Icon {
    //--
    //==============
    // DATA MEMBERS
    //==============
    //--
    private ArrayList< ArrayList< Pixel >> screen = new ArrayList< ArrayList< Pixel >>();
    private int numRows;
    private int numCols;
    private int padding;
    //--
    //==============
    // CONSTRUCTORS
    //==============
    //--
    // Default Constructor has 40x40
    // All pixels initialized with rgb= 0,0,0 (black)
    public Icon(){
        this(40, 40);
    }
    //--
    // Constructor with specified numRows && numCols
    // All pixels initialized with rgb= 0,0,0 (black)
    public Icon(int rows, int cols){
        numRows = rows;
        numCols = cols;
        padding = (4-((3*(numCols)) % 4)) % 4;    // Padding ensures that the bytes per row is a multiple of 4
        for(int i = 0; i < numRows; i++)
        {
            ArrayList<Pixel> row = new ArrayList<Pixel>();
            for(int j = 0; j < numCols; j++)
            {
                row.add(new Pixel());
            }
            screen.add(row);
        }
    }
    //--
    //==============
    // SETTERS AND GETTERS
    //==============
    //--
    // Return the pixel in the given location
    public Pixel getPixel(int row, int col){
        Pixel retVal = null;
        if(row >= 0 && row < numRows && col >= 0 && col < numCols){
            retVal = screen.get(row).get(col);
        }
        return retVal;
    }
    //--
    // Set the rgb value of the pixel at the desired location
    public void setPixel(int row, int col, int r, int g, int b){
        Pixel p = getPixel(col, row); // Get the pixel at the desired location
        p.setAll(r, g, b);
    }
    //--
    //==============
    // METHODS
    //==============
    //--
    // Represent entire Icon array as string using each Pixel's toString method
    public String toString(){
        String output = ""; // Initialize our returned string
        for(int i = 0; i < numRows; i++){ 
            for(int j = 0; j < numCols; j++)
            {
                String str = getPixel(i, j).toString() + ", ";
                output = output.concat(str);
            }
            output = output + "\n"; // Need to add endline in order to designate rows
        }
        return output;
    }
    //--
    // Write the icon data to a BMP file
    // See https://en.wikipedia.org/wiki/BMP_file_format
    //  for format
    public void toBitMap(String fileName){
        ArrayList<Byte> fileBytes = new ArrayList<Byte>(); // Used to store all file data 
        writeFileHeader(fileBytes);
        writeInfoHeader(fileBytes);
        writePixelData(fileBytes);
        File outputFile = new File(fileName); // Create the output file
        writeBMPFile(outputFile, fileBytes);
    }
    //--
    private void writeFileHeader(ArrayList<Byte> fileBytes){
        // 0-1 'B', 'M'
        fileBytes.add((byte)'B');
        fileBytes.add((byte)'M');

        // 2-5 size of file in bytes (int is 4 bytes)
        // fileHeader   + infoHeader    + pixelData
        // 14           + 40            + *varies*
        int pixelDataSize = 3*(numRows * numCols) + (numRows*padding);
        int size = 14 + 40 + pixelDataSize;
        fileBytes.add((byte)size);
        fileBytes.add((byte)(size >> 8));
        fileBytes.add((byte)(size >> 16));
        fileBytes.add((byte)(size >> 24));

        // 6-7 Unused (2 bytes)
        // 8-9 Unused (2 bytes)
        // Single int will make 4 bytes
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);

        // 10-13 Offset to pixel data (4 byte integer)
        // fileHeader (14) + infoHeader (40) = 54
        int offset = 14 + 40;
        fileBytes.add((byte) offset);
        fileBytes.add((byte) (offset >> 8));
        fileBytes.add((byte) (offset >> 16));
        fileBytes.add((byte) (offset >> 24));
    }
    //--
    private void writeInfoHeader(ArrayList<Byte> fileBytes){
        // 0-3 size of info header as 4 byte int (40)
        int headerSize = 40;
        fileBytes.add((byte)headerSize);
        fileBytes.add((byte)(headerSize >> 8));
        fileBytes.add((byte)(headerSize >> 16));
        fileBytes.add((byte)(headerSize >> 24));

        // 4-7 width of bitmap as 4 byte int
        int width = numCols;
        fileBytes.add((byte) width);
        fileBytes.add((byte) (width >> 8));
        fileBytes.add((byte) (width >> 16));
        fileBytes.add((byte) (width >> 24));

        // 8-11 height of bitmap as 4 byte int
        int height = numRows;
        fileBytes.add((byte) height);
        fileBytes.add((byte) (height >> 8));
        fileBytes.add((byte) (height >> 16));
        fileBytes.add((byte) (height >> 24));

        // 12-13 number of planes (always 1) as 2 byte int
        fileBytes.add((byte) ((short)1));
        fileBytes.add((byte) (((short)1) >> 8));
 
        // 14-15 bits/color (always 24) as 2 byte int
        fileBytes.add((byte)((short)24));
        fileBytes.add((byte) ((short)24 >> 8));

        // 16-19 compression (always 0) as 4 byte int
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);

        // 20-23 pixel data size in bytes as 4 byte int
        int pdSize = 3 * (numCols * numRows) + (numRows*padding);
        fileBytes.add((byte)pdSize);
        fileBytes.add((byte)(pdSize >> 8));
        fileBytes.add((byte)(pdSize >> 16));
        fileBytes.add((byte)(pdSize >> 24));

        // 24-27 all 0 (4 bytes)
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);

        // 28-31 all 0 (4 bytes)
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);

        // 32-35 all 0 (4 bytes)
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);

        // 36-39 all 0 (4 bytes)
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
        fileBytes.add((byte)0);
     }
     //--
     // Write pixel data
     // 3 bytes per rgb values (FORMMATED bgr)
     // Padding if not a multiple of 4
     private void writePixelData(ArrayList<Byte> fileBytes)
     {
         // Write pixels to fileBytes
         /*
           ^ [][][][][]
           | [][][][][]
           | [][][][][]
           | [][][][][]
           | [][][][][]
            --------->
            Bottom to top, left to right
         */
        for(int i = numRows - 1; i >= 0; i--){
            for(int j = 0; j < numCols; j++){
                // Gather the rgb values from the pixel
                Pixel p = getPixel(i, j);
                Byte r = (byte)(p.getRed());
                Byte g = (byte)(p.getGreen());
                Byte b = (byte)(p.getBlue());
                // Add these values IN LITTLE ENDIAN ORDER (bgr) to fileBytes arraylist
                fileBytes.add(b);
                fileBytes.add(g);
                fileBytes.add(r);
            }
            if(padding > 0){
                for(int k = 0; k < padding; k++){
                    fileBytes.add((byte)0);
                }
            }
        }
        
        /*
         for(int i = numRows - 1; i >= 0; i--){
             for(int j = 0; j < numCols; j++){
                 // Gather the rgb values from the pixel
                 Pixel p = getPixel(i, j);
                 Byte r = (byte)(p.getRed());
                 Byte g = (byte)(p.getGreen());
                 Byte b = (byte)(p.getBlue());
                 // Add these values IN LITTLE ENDIAN ORDER (bgr) to fileBytes arraylist
                 fileBytes.add(b);
                 fileBytes.add(g);
                 fileBytes.add(r);
             }
             // Check to see if padding is needed
             // If so, add the padding (0s)
             if(padding > 0){
                for(int k = 0; k < padding; k++){
                    fileBytes.add((byte)0);
                }
             }
         }
         */
     }
     //--
     // Take the fileBytes array and write them out to the BMP file
     private void writeBMPFile(File outputFile, ArrayList<Byte> fileBytes)
     {
         // Note: exceptions can arise so be prepared
         try{
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            // Create a primitive byte[] array to serve as buffer
            byte[] buffer = new byte[fileBytes.size()];

            // Transfer over the arraylist Bytes to the buffer array
            for(int m = 0; m < fileBytes.size(); m++){
                buffer[m] = fileBytes.get(m);
            }
            outputStream.write(buffer); // Write over the byte[] to the file
            outputStream.close();   // Be sure to close the output stream
         }
         // Catch any exceptions caused by the file stream
         catch(Exception ex){
             System.err.println("ERROR: " + ex); // Just print out the error to the error console
         }
     }
}