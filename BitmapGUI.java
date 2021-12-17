import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class BitmapGUI extends JFrame implements ActionListener, Observer
{
    private JCheckBox attachCB;
    private JCheckBox grayscaleCB;
    private JCheckBox randomColorCB;
    private JCheckBox vertInvertCB;
    private JCheckBox horizInvertCB;

    private EditorQueue eque;
    private int DRAWMODE;
    private final int PIXEL_MODE = 1;
    private final int IMAGE_MODE = 2;

    private JButton importButton;
    private JLabel importPreview;
    private Color currentColor;
    private JPanel pixGridPanel;
    private ArrayList< ArrayList<PixelButton> > pixGrid;

    private JPanel colorPickerPanel;
    private JPanel colorSlidersPanel;
    private JSlider redSlider;
    private JSlider greenSlider;
    private JSlider blueSlider;

    private JPanel prevColorPanel;
    private ArrayList<PixelButton> prevColorList;

    private JPanel currentColorPanel;
    private JLabel currentColorLabel;
    private JLabel previewLabel;
    private JButton createBMPButton;

    private JCheckBox displayAdvancedMenuCheckbox;
    private JPanel importPanel;
    private JPanel fillPanel;

    private static int imgHeight = 1; // Default values - Assuming the user does
    private static int imgWidth = 1;
    private int fillSizeRow = 1;
    private int fillSizeCol = 1;

    private int currentPrevColorIndex = 0;

    private Icon bitmapIcon;
    private BufferedImage tempImage;

    public BitmapGUI(int numRows, int numCols, EditorQueue eq){
        super("Bitmap Maker");
        eque = eq;
        eque.attach(this);
        DRAWMODE = PIXEL_MODE; // Initialize the drawing mode to Pixel
        imgHeight = numRows;
        imgWidth = numCols;
        bitmapIcon = new Icon(imgHeight, imgWidth);
        currentColor = new Color(255, 255, 255);
        pixGrid = new ArrayList<ArrayList<PixelButton>>();
        prevColorList = new ArrayList<PixelButton>(5);
        setupGUI();
    }
    //--
    public static int getBitmapHeight(){
        return imgHeight;
    }
    //--
    public static int getBitmapWidth(){
        return imgWidth;
    }
    //--
    @Override
    public void update(int x, int y, Color color) {
        colorPixelGrid(x, y, color);
    }
    // COLOR THE DESIRED PIXEL WHEN CLICKED
    @Override
    public void actionPerformed(ActionEvent e) {
        PixelButton jb = (PixelButton)e.getSource();
        // Check to see which Drawing Mode the user is doing
        if(DRAWMODE == PIXEL_MODE){
            colorPixelGrid(jb.getXVal(), jb.getYVal(), currentColor);
            if(attachCB.isSelected()){
                eque.notifyObservers(jb.getXVal(), jb.getYVal(), currentColor);
            }
        }
        else if(DRAWMODE == IMAGE_MODE){
            drawImage(jb.getXVal(), jb.getYVal());
            DRAWMODE = PIXEL_MODE;
        }
        if(!swatchExists(prevColorList)){
            addSwatch();
        }
    }
    //--
    private void drawImage(int x, int y){
        for(int i = x; i < (x+tempImage.getWidth()); i++){
            for(int k = y; k < (y+tempImage.getHeight()); k++){
                if(i < imgWidth && k < imgHeight){
                    Color c = new Color(tempImage.getRGB(i-x, k-y));
                    pixGrid.get(i).get(k).setBackground(c);
                    bitmapIcon.setPixel(i, k, c.getRed(), c.getGreen(), c.getBlue());
                }
            }
        }
        
    }
    //--
    private void colorPixelGrid(int x, int y, Color col){
        BitmapEdit edit = new BitmapEdit(x,y,col);
        Modifier modifier = new BaseModifier(edit);
        if(grayscaleCB.isSelected()){
            modifier = new GrayBitmapEdit(modifier);
        }
        if(randomColorCB.isSelected()){
            modifier = new RandomColorBitmapEdit(modifier);
        }
        if(vertInvertCB.isSelected()){
            modifier = new VerticalInvertBitmapEdit(modifier);
        }
        if(horizInvertCB.isSelected()){
            modifier = new HorizontalInvertBitmapEdit(modifier);
        }

        modifier.modify(edit);

        x = edit.getXVal();
        y = edit.getYVal();
        col = edit.getColor();
        // Im not quite sure how this works, but it felt good while I was doing it
        // And it works....so voila
        for(int i = x; i < (x+fillSizeRow); i++){
            for(int k = y; k < (y+fillSizeCol); k++){
                if(i < imgWidth && k < imgHeight){
                    pixGrid.get(i).get(k).setBackground(col);
                    bitmapIcon.setPixel(i, k, currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue());
                }
            }
        }
    }
    //--
    private void setupGUI(){
        adjustLookAndFeel();
        createPixGrid(imgWidth, imgHeight);
        updatePixGrid();

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);

        c.gridx = 0;
        c.weightx = 0.5;
        // Add each of the components
        add(pixGridPanel,c);

        c.gridx = 1;
        add(setupColorPanel(),c);
    
        //set the size of the window to be 400 pixels wide 
		//and 300 pixels tall 
		setSize(1000, 800); 
 
		//kill the program when the user clicks the 'x' 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		 
		 
		//set the location on the screen where the window 
		//will show up 
		setLocation(100, 100); 
        pack();
		//make the window visible 
		setVisible(true); 
    }
    //--
    private void updatePixGrid(){
        pixGridPanel = new JPanel();
        pixGridPanel.setLayout(new GridLayout(imgWidth, imgHeight));
        pixGridPanel.setPreferredSize(new Dimension(this.getWidth()/2, this.getHeight()));
        for (ArrayList<PixelButton> row : pixGrid) {
            for (PixelButton pixelButton : row) {
                pixGridPanel.add(pixelButton);
            }
        }
        pixGridPanel.setPreferredSize(new Dimension(400,400));
    }
    //--
    private void createPixGrid(int rows, int cols){
        pixGrid = new ArrayList<ArrayList<PixelButton>>();
        for(int i = 0; i < rows; i++){
            ArrayList<PixelButton> row = new ArrayList<PixelButton>();
            for(int k = 0; k < cols; k++){
                PixelButton jb = new PixelButton(i, k);
                jb.addActionListener(this);
                jb.addMouseListener(new MouseListener(){
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        jb.grabFocus();  
                        if(e.isShiftDown()){
                            colorPixelGrid(jb.getXVal(), jb.getYVal(), currentColor);
                            eque.notifyObservers(jb.getXVal(), jb.getYVal(), currentColor);
                        }
                    }
                    //--
                    @Override
                    public void mouseClicked(MouseEvent e) {}
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e){}
                });
                jb.setOpaque(true);
                jb.setBorderPainted(false);
                jb.setBackground(new Color(0,0,0));
                row.add(jb);
            }
            pixGrid.add(row);
        }
    }
    //--
    private JPanel setupColorPanel(){
        // Set Up the entire panel to hold all color related stuff
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 3));

        // Set Up Panel to hold the Sliders and Prev Colors
        colorPickerPanel = new JPanel();
        colorPickerPanel.setLayout(new GridLayout(3,1));

        // Set up the sliders
        setupColorSliders();
        colorPickerPanel.add(colorSlidersPanel);

        // Setup Prev Color panels/Buttons
        setupPrevColorPanel();
        colorPickerPanel.add(prevColorPanel);

        // Setup Advanced Menu
        setupAdvancedMenu();
        colorPickerPanel.add(displayAdvancedMenuCheckbox);

        // Add the ColorPickerPanel to the large colorPanel
        colorPanel.add(colorPickerPanel);

        // Setup the currentColorPanel
        // NOTE: This will not be in the colorPickerPanel
        setupCurrentColorPanel();
        colorPanel.add(currentColorPanel);

        colorPanel.add(setupModifiedPanel());

        return colorPanel;
    }
    //--
    private JPanel setupModifiedPanel(){
        JPanel decPanel = new JPanel();
        decPanel.setLayout(new GridLayout(5,1));

        attachCB = new JCheckBox("Attached", true); // Initialize as selected
        attachCB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(attachCB.isSelected()){
                    eque.attach(BitmapGUI.this);
                    attachCB.setSelected(true);
                }
                else{
                    eque.detach(BitmapGUI.this);
                    attachCB.setSelected(false);
                }
            }
        });
        decPanel.add(attachCB);

        grayscaleCB = new JCheckBox("Grayscale");
        grayscaleCB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(grayscaleCB.isSelected()){
                    grayscaleCB.setSelected(true);
                }
                else{
                    grayscaleCB.setSelected(false);
                }
            }
        });
        decPanel.add(grayscaleCB);

        randomColorCB = new JCheckBox("Randomize Color");
        randomColorCB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(randomColorCB.isSelected()){
                    randomColorCB.setSelected(true);
                }
                else{
                    randomColorCB.setSelected(false);
                }
            }
        });
        decPanel.add(randomColorCB);

        vertInvertCB = new JCheckBox("Horizontal Invert");
        vertInvertCB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(vertInvertCB.isSelected()){
                    vertInvertCB.setSelected(true);
                }
                else{
                    vertInvertCB.setSelected(false);
                }
            }
        });
        decPanel.add(vertInvertCB);

        horizInvertCB = new JCheckBox("Vertical Invert");
        horizInvertCB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(horizInvertCB.isSelected()){
                    horizInvertCB.setSelected(true);
                }
                else{
                    horizInvertCB.setSelected(false);
                }
            }
        });
        decPanel.add(horizInvertCB);

        return decPanel;
    }
    //--
    private void setupColorSliders(){
        // Setup Color Sliders Panel
        colorSlidersPanel = new JPanel();
        colorSlidersPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Intialize Text
        JLabel redLabel = new JLabel("Red:");
        JLabel greenLabel = new JLabel("Green:");
        JLabel blueLabel = new JLabel("Blue:"); 

        // Initialize the color sliders
        redSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, currentColor.getRed());
        redSlider.setPaintTrack(true);

        redSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                updateColorPreview();
                currentColor = new Color(((JSlider)e.getSource()).getValue(), currentColor.getGreen(), currentColor.getBlue());
            }
        });

        greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, currentColor.getGreen());
        greenSlider.setPaintTrack(true);
        greenSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                updateColorPreview();
                currentColor = new Color(currentColor.getRed(), ((JSlider)e.getSource()).getValue(), currentColor.getBlue());
            }
        });
        
        blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, currentColor.getBlue());
        blueSlider.setPaintTrack(true);
        blueSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                updateColorPreview();
                currentColor = new Color(currentColor.getRed(), currentColor.getGreen(), ((JSlider)e.getSource()).getValue());
            }
        });

        // Add them to the colorSlidersPanel
        c.gridwidth = 1;
        c.gridy = 0;
        colorSlidersPanel.add(redLabel, c);
        c.gridwidth = 2;
        colorSlidersPanel.add(redSlider, c);

        c.gridwidth = 1;
        c.gridy = 1;
        colorSlidersPanel.add(greenLabel, c);
        c.gridwidth = 2;
        colorSlidersPanel.add(greenSlider, c);

        c.gridwidth = 1;
        c.gridy = 2;
        colorSlidersPanel.add(blueLabel, c);
        c.gridwidth = 2;
        colorSlidersPanel.add(blueSlider, c);
    }
    //--
    private void setupPrevColorPanel(){
        prevColorPanel = new JPanel();
        // Initialize the prevColorList with 5 buttons
        for(int i = 0; i < 5; i++){
            PixelButton pb = new PixelButton(0, i);
            pb.setOpaque(true);
            //pb.setBorderPainted(false);
            pb.setBackground(Color.white);
            prevColorList.add(pb);
            pb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // When the swatch is clicked, adjust the sliders and select that color
                    PixelButton pb = (PixelButton)e.getSource();
                    Color pbColor = pb.getBackground();
                    currentColor = pbColor; // Set the current color to whatever the swatch contains
                    redSlider.setValue(pbColor.getRed());
                    greenSlider.setValue(pbColor.getGreen());
                    blueSlider.setValue(pbColor.getBlue());
                }
            });
        }

        // Set up the prevColorPanel
        prevColorPanel.setLayout(new GridLayout(1,5));
        for (PixelButton jButton : prevColorList) {
            prevColorPanel.add(jButton);
        }
    }
    //--
    private void setupCurrentColorPanel(){
        currentColorPanel = new JPanel();
        currentColorLabel = new JLabel();
        previewLabel = new JLabel("Preview", SwingConstants.CENTER);

        // Set the label to an img to be colored
        currentColorLabel.setOpaque(true);
        // Make the default color white
        currentColorLabel.setBackground(currentColor);
        currentColorLabel.setPreferredSize(new Dimension(150, 150));

        // Add the components to the overarching panel
        currentColorPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        currentColorPanel.add(currentColorLabel, c);
        c.gridy = 1;
        currentColorPanel.add(previewLabel, c);

        // SETUP THE BUTTON TO CREATE A BITMAP
        createBMPButton = new JButton("Export Bitmap");

        createBMPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(BitmapGUI.this);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();

                    // PROMPT FOR THE FILE NAME
                    String fileName = JOptionPane.showInputDialog(null, "Please enter a file name!");
                    bitmapIcon.toBitMap(file.getPath() + "/" + fileName);
                }
            }
        });
        c.gridy = 2;
        c.weighty = 0.1;
        currentColorPanel.add(createBMPButton, c);
    }
    //--
    private void updateColorPreview(){
        // Update the current color of the display
        currentColorLabel.setBackground(currentColor);
    }
    //--
    private void addSwatch(){
        // Add the current color to one of the previous color swatches
        if(currentPrevColorIndex >= 5){
            currentPrevColorIndex = 0;
        }
        prevColorList.get(currentPrevColorIndex).setBackground(currentColor);
        currentPrevColorIndex++;
    } 
    //--
    boolean swatchExists(ArrayList<PixelButton> list){
        boolean retVal = false;
        for (PixelButton pixelButton : list) {
            if(pixelButton.getBackground().equals(currentColor)){
                retVal = true;
            }
        }
        return retVal;
    }
    //--
    private void adjustLookAndFeel(){
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(Exception e){
            System.err.println(e.toString());
        }
    }
    //--
    private void setupAdvancedMenu(){
        displayAdvancedMenuCheckbox = new JCheckBox("Advanced Menu");
        displayAdvancedMenuCheckbox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAdvancedMenu();
            }
        });
    }
    //--
    private void displayAdvancedMenu(){
        importPanel = new JPanel();
        fillPanel = new JPanel();
        JDialog menu = new JDialog(BitmapGUI.this, "Advanced Menu", true);
        menu.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                displayAdvancedMenuCheckbox.setSelected(false);
                super.windowClosing(e);
            }
        });
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        // Setup the Fill Tab
        setupFillPanel();
        tabbedPane.addTab("Fill", fillPanel);

        // Setup the Import Tab
        setupImportPanel();
        tabbedPane.addTab("Import", importPanel);

        //set the size and location of the JDialog and make it visible 
        menu.add(tabbedPane);
        menu.setSize(400, 300); 
        menu.setLocation(400, 400); 
        menu.setVisible(true); 
    }
    //--
    private void setupFillPanel(){
        fillPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Setup the fill Row textfield and related items
        JLabel rowLab = new JLabel("Rows");
        JTextField rowTF = new JTextField();
        rowTF.setText(Integer.toString(fillSizeRow)); // Place the current fill size in the TF

        // Setup the fill Column textfield and related items
        JLabel colLab = new JLabel("Cols");
        JTextField colTF = new JTextField();
        colTF.setText(Integer.toString(fillSizeCol)); // Place the current fill size in the TF

        JButton submitButton = new JButton("Set Fill Size");
        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String rowText = rowTF.getText();
                String colText = rowTF.getText();
                try{
                    fillSizeRow = Integer.parseInt(rowText);
                    fillSizeCol = Integer.parseInt(colText);
                }
                catch(Exception exc){
                    fillSizeRow = 1;
                    fillSizeCol = 1;
                    rowTF.setText("1");
                    colTF.setText("1");
                }
            }
        });

        // Insert the labels
        c.insets = new Insets(5,5,5,5);
        c.gridx = 0;
        c.gridy = 0;
        fillPanel.add(rowLab, c);
        c.gridy = 1;
        fillPanel.add(colLab, c);

        // Insert the text fields
        c.gridy = 0;
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        fillPanel.add(rowTF, c);
        c.gridy = 1;
        fillPanel.add(colTF, c);

        // Set up the submit Button
        c.gridy = 3;
        c.gridwidth = 1;
        fillPanel.add(submitButton, c);
    }
    //--
    private void setupImportPanel(){
        importPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        importButton = new JButton("Import");
        importButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Bitmap Files", "bmp");
                chooser.setFileFilter(filter);
                try{
                    int returnVal = chooser.showOpenDialog(BitmapGUI.this);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        BufferedImage image = ImageIO.read(chooser.getSelectedFile());
                        Image scaledImg = image.getScaledInstance(importPreview.getWidth(), importPreview.getHeight(), Image.SCALE_SMOOTH);
                        importPreview.setIcon(new ImageIcon(scaledImg));
                        tempImage = image;
                    }
                }
                catch(Exception exc){
                    System.err.println(exc.toString());
                }
            }
        });
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tempImage != null){
                    DRAWMODE = IMAGE_MODE;
                }
            }
        });

        // Intialize the importPreview JLabel
        importPreview = new JLabel("");
        // Ensure it's the proper Dimension despite the lack of content (Empty string)
        importPreview.setMinimumSize(new Dimension(180, 180));
        importPreview.setPreferredSize(new Dimension(180, 180));
        importPreview.setOpaque(true);
        importPreview.setBackground(Color.white);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.3;
        c.weighty = 0.5;
        importPanel.add(importButton, c);
        c.gridx = 2;
        importPanel.add(submitButton, c);

        c.gridx = 1;
        c.weighty = 0.5;
        c.insets = new Insets(10,5,0,10);
        importPanel.add(importPreview, c);

        // Add the preview text below the importPreview
        JLabel tempPreviewText = new JLabel("Preview", SwingConstants.CENTER);
        c.gridx = 1;
        c.weighty = 0.1;
        c.gridy = 3;
        c.insets = new Insets(0,0,0,0);
        importPanel.add(tempPreviewText, c);

    }
}