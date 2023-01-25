import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Driver {
    public static void main(String[] args) throws Exception {
        EditorQueue queue = new EditorQueue();


        boolean willPrompt = true;
        int imgHeight = -1;
        int imgWidth = -1;
        int numInstances = -1;

        while(willPrompt){
            // Display the input dialog to gather WIDTH, HEIGHT, and NUMBER OF INSTANCES
            String imgHeightStr = new String("Please enter image height:");
            JTextField heightTF = new JTextField();
            String imgWidthStr = new String("Please enter image width:");
            JTextField widthTF = new JTextField();
            String numGUIStr = new String("Please enter the number of windows you would like:");
            JTextField numGUITF = new JTextField();

            Object[] items = new Object[] {imgHeightStr, heightTF, imgWidthStr, widthTF, numGUIStr, numGUITF};

            JOptionPane.showMessageDialog(null, items, "Bitmap Maker", JOptionPane.QUESTION_MESSAGE);

            try{
                imgHeight = Integer.parseInt(heightTF.getText());
                imgWidth = Integer.parseInt(widthTF.getText());
                numInstances = Integer.parseInt(numGUITF.getText());
                willPrompt = false;
            }
            catch(Exception exc){
                JOptionPane.showMessageDialog(null, "ERROR:\nOne or more inputs was not a number!", "ERROR", JOptionPane.ERROR_MESSAGE);
                continue; // Prompt again
            }
            finally{
                if(imgHeight <= 0 || imgWidth <= 0 || numInstances <= 0){
                    JOptionPane.showMessageDialog(null, "ERROR:\nOne or more inputs was not greater than 0 (Zero)!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    willPrompt = true;
                    continue; // Prompt again
                }
            }
        }

        // Create the Bitmap Makers
        for(int i = 0; i < numInstances; i++){
            BitmapGUI gui = new BitmapGUI(imgHeight, imgWidth, queue);
        }
        

    }
}
