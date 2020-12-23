import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Stack;


public class MyFrame extends JPanel implements MouseListener, ActionListener, MouseMotionListener {

    // Stores path of the image that is imported.
    public String path;
    // Stores shapes for undo/redo.
    public Stack<MyShapes> undoStack = new Stack<>();
    public Stack<MyShapes> redoStack = new Stack<>();

    // JButtons on the main panel.
    JButton saveBtn, clearBtn, colourBtn, quitBtn, undoBtn, redoBtn, lineBtn, ovalBtn, rectBtn, triangleBtn, importBtn, addBtn;
    // Can be toggled on or off to choose whether to fill shapes or not.
    JRadioButton fillBtn;
    // Frame that all the drawing is done on.
    private final JFrame frame;
    private int oldX, oldY, newX, newY; // Co-ordinates of point before and after drag.
    private int choice; // Stores the choice the user has made.
    private static final Color BACKGROUND_COLOR = Color.WHITE;	// Default background colour is white.
    private boolean fill = false;

    MyFrame() {
        
        frame = new JFrame("Vector Drawing Package");
        ImageIcon image = new ImageIcon("ms.png"); // Create image icon for program.
        frame.setIconImage(image.getImage());

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Open program in full screen.
//        frame.setUndecorated(true); // Can be uncommented for a full free screen experience (remove bar at the top).
        frame.setBackground(BACKGROUND_COLOR);
        frame.getContentPane().add(this);

        // This menu bar holds all buttons.
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Create buttons for the menu bar.

        ImageIcon im1 = new ImageIcon("rectangle.png"); // Use an image icon for the button.
        Image img1 = im1.getImage() ;
        Image newImg1 = img1.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH ); // Scale image to fit the button properly.
        im1 = new ImageIcon( newImg1 );
        rectBtn = new JButton(im1);
        rectBtn.addActionListener(this); // Add action listener to get co-ordinates of mouse drag.
        rectBtn.setBorderPainted(false); // Set border painted and content area filled for a flat finish (looks better in my opinion).
        rectBtn.setContentAreaFilled(false);

        ImageIcon im2 = new ImageIcon("ellipse.png");
        Image img2 = im2.getImage() ;
        Image newImg2 = img2.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im2 = new ImageIcon( newImg2 );
        ovalBtn = new JButton(im2);
        ovalBtn.addActionListener(this);
        ovalBtn.setBorderPainted(false);
        ovalBtn.setContentAreaFilled(false);

        ImageIcon im3 = new ImageIcon("triangle.png");
        Image img3 = im3.getImage() ;
        Image newImg3 = img3.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im3 = new ImageIcon( newImg3 );
        triangleBtn = new JButton(im3);
        triangleBtn.addActionListener(this);
        triangleBtn.setBorderPainted(false);
        triangleBtn.setContentAreaFilled(false);

        ImageIcon im4 = new ImageIcon("line.png");
        Image img4 = im4.getImage() ;
        Image newImg4 = img4.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im4 = new ImageIcon( newImg4 );
        lineBtn = new JButton(im4);
        lineBtn.addActionListener(this);
        lineBtn.setBorderPainted(false);
        lineBtn.setContentAreaFilled(false);

        fillBtn = new JRadioButton("Fill Shape");
        fillBtn.addActionListener(this);
        fillBtn.setBorderPainted(false);
        fillBtn.setContentAreaFilled(false);

        ImageIcon im5 = new ImageIcon("save.png");
        Image img5 = im5.getImage() ;
        Image newImg = img5.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im5 = new ImageIcon( newImg );
        saveBtn = new JButton(im5);
        // Use a new mouse listener as we do not want to get co-ordinates of mouse drag and want to perform a different action.
        saveBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                // Replace the standard options of "Yes","No" and "Quit" with custom ones.
                // Code for this modified from: https://stackoverflow.com/questions/32062761/change-button-text-in-joptionpane-showconfirmdialog
                Object[] options = {"Save", "Load", "Exit"};
                int input = JOptionPane.showOptionDialog(frame, // Parent container of JOptionPane
                        "Would you like to save this drawing or load a previous one?",
                        null,
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // Do not use a custom Icon
                        options,// The titles of buttons
                        options[2]);// Default button title

                String fileName; // Store the name of the file we want to save to.
                if (input == JOptionPane.YES_OPTION) {
                    // If no images are on the canvas then don't save.
                    if (undoStack.size() == 0) {
                        System.out.println("Nothing to save");
                        JOptionPane.showMessageDialog(frame, "Add something to canvas first");
                    }
                    else
                    {
                        // Get the name of the file.
                        fileName = JOptionPane.showInputDialog("Please name the file you want to save this as");
                        System.out.println(fileName);
                        // Call the serialize method to save the drawing.
                        Serialize(fileName);
                    }
                }

                // If the user picks the "load" option then call deserialize.
                else if (input == JOptionPane.NO_OPTION) {
                    Deserialize();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        saveBtn.setBorderPainted(false);
        saveBtn.setContentAreaFilled(false);

        ImageIcon im6 = new ImageIcon("eraser.png");
        Image img6 = im6.getImage() ;
        Image newImg6 = img6.getScaledInstance( 27, 27,  java.awt.Image.SCALE_SMOOTH );
        im6 = new ImageIcon( newImg6 );
        clearBtn = new JButton(im6);
        clearBtn.addActionListener(this);
        clearBtn.setBorderPainted(false);
        clearBtn.setContentAreaFilled(false);

        ImageIcon im7 = new ImageIcon("quit.png");
        Image img7 = im7.getImage() ;
        Image newImg7 = img7.getScaledInstance( 28, 28,  java.awt.Image.SCALE_SMOOTH );
        im7 = new ImageIcon( newImg7 );
        quitBtn = new JButton(im7);
        quitBtn.addActionListener(this);
        quitBtn.setBorderPainted(false);
        quitBtn.setContentAreaFilled(false);

        ImageIcon im8 = new ImageIcon("color-circle.png");
        Image img8 = im8.getImage() ;
        Image newImg8 = img8.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im8 = new ImageIcon( newImg8 );
        colourBtn = new JButton(im8);
        colourBtn.addActionListener(this);
        colourBtn.setBorderPainted(false);
        colourBtn.setContentAreaFilled(false);

        ImageIcon im9 = new ImageIcon("undo.png");
        Image img9 = im9.getImage() ;
        Image newImg9 = img9.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im9 = new ImageIcon( newImg9 );
        undoBtn = new JButton(im9);
        undoBtn.addActionListener(this);
        undoBtn.setBorderPainted(false);
        undoBtn.setContentAreaFilled(false);

        ImageIcon im10 = new ImageIcon("redo.png");
        Image img10 = im10.getImage() ;
        Image newImg10 = img10.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH );
        im10 = new ImageIcon( newImg10 );
        redoBtn = new JButton(im10);
        redoBtn.addActionListener(this);
        redoBtn.setBorderPainted(false);
        redoBtn.setContentAreaFilled(false);

        importBtn = new JButton("Import");
        importBtn.addMouseListener(new Import()); // Add a new import mouse listener which is defined in the import class.
        importBtn.setBorderPainted(false);
        importBtn.setContentAreaFilled(false);

        addBtn = new JButton("Add");
        addBtn.addActionListener(this);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);

        // Add all buttons to the menu bar.
        menuBar.add(saveBtn);
        menuBar.add(undoBtn);
        menuBar.add(redoBtn);
        menuBar.add(colourBtn);
        menuBar.add(clearBtn);
        menuBar.add(rectBtn);
        menuBar.add(ovalBtn);
        menuBar.add(triangleBtn);
        menuBar.add(lineBtn);
        menuBar.add(fillBtn);
        menuBar.add(importBtn);
        menuBar.add(addBtn);
        menuBar.add(quitBtn);

        addMouseListener(this); 					// Add Mouse listener to frame.
        frame.setVisible(true);						// Frame is set visible.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Automatically close program when exit button is pressed.

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if (grid == null) {
            // Get width and height of the paint area.
            int w = this.getWidth();
            int h = this.getHeight();
            grid = (BufferedImage) (this.createImage(w, h));
            gc = grid.createGraphics();
            // Set default colour to black.
            gc.setColor(Color.BLACK);
        }

        graphics2D.drawImage(grid, null, 0, 0);
        
    }

    BufferedImage grid;
    Graphics2D gc;

    public void mousePressed(MouseEvent e)
    {
        // When mouse is pressed get current x and y co-ordinates.
        oldX = e.getX();
        oldY = e.getY();
    }

    public void mouseReleased(MouseEvent e)
    {
        // When mouse is released get new x and y co-ordinates.
        newX = e.getX();
        newY = e.getY();
        // Call draw to draw the shape.
        draw();
    }

    public void actionPerformed(ActionEvent e)
    {

        super.removeMouseMotionListener(this);

        // If the colour button is clicked get the colour chosen and set gc's current colour to the new colour.
        if (e.getSource() == colourBtn)
        {
            Color bgColor = JColorChooser.showDialog(this, "Choose Background Color", getBackground());
            if (bgColor != null)
                gc.setColor(bgColor);
        }

        // If a button is clicked set the current choice to the corresponding number needed to draw that shape.
        if (e.getSource() == rectBtn) {
            choice = 1;
        }

        if (e.getSource() == ovalBtn) {
            choice = 2;
        }

        if (e.getSource() == lineBtn) {
            choice = 3;
        }


        if (e.getSource() == fillBtn) {
            // If fill is true when pressed, set it to false.
            // If fill is false when pressed, set it to true.
            fill = !fill;
        }

        if (e.getSource() == clearBtn) {
            choice = 4;
            draw();
        }


        if (e.getSource() == triangleBtn) {
            choice = 5;
        }

        if (e.getSource() == addBtn) {
            choice = 6;
        }


        if (e.getSource() == quitBtn) {
            // Exit the program.
            System.exit(1);
        }

        if (e.getSource() == undoBtn) {
            // Call undo if the button is pressed.
            undo();
        }

        if (e.getSource() == redoBtn) {
            // Call redo if the button is pressed.
            redo();
        }

    }

    public void draw() {

        // Set the stroke length to 3 so the shapes are defined slightly more than usual.
        gc.setStroke(new BasicStroke(3));

        // Calculate width and height.
        int w = Math.abs(newX - oldX);
        int h = Math.abs(newY - oldY);

        // For each shape if fill == true then we draw the filled shape, otherwise don't fill.
        if(choice == 1) {

            MyRectangle rectangle;
            if (fill) {
                rectangle = new MyRectangle(oldX, oldY, w, h, gc.getColor(), true);
                gc.fillRect(rectangle.getOldX(), rectangle.getOldY(), rectangle.getW(), rectangle.getH());
            }
            else {
                rectangle = new MyRectangle(oldX, oldY, w, h, gc.getColor(), false);
                gc.drawRect(rectangle.getOldX(), rectangle.getOldY(), rectangle.getW(), rectangle.getH());
            }

            // Push each shape onto the stack and call repaint
            undoStack.push(rectangle);
            repaint(); // Repaint the canvas as changes have been made.
        }

        else if(choice == 2) {

            MyEllipse ellipse;

            if (fill) {
                ellipse = new MyEllipse(oldX, oldY, w, h, gc.getColor(), true);
                gc.fillOval(ellipse.getOldX(), ellipse.getOldY(), ellipse.getNewX(), ellipse.getNewY());
            }
            else {
                ellipse = new MyEllipse(oldX, oldY, w, h, gc.getColor(), false);
                gc.drawOval(ellipse.getOldX(), ellipse.getOldY(), ellipse.getNewX(), ellipse.getNewY());
            }

            undoStack.push(ellipse);
            repaint();
        }

        else if(choice == 3) {

            MyLine line = new MyLine(oldX, oldY, newX, newY, gc.getColor());

            gc.drawLine(line.getOldX(), line.getOldY(), line.getNewX(), line.getNewY());

            undoStack.push(line);
            repaint();

        }

        else if(choice == 4) {
            clear(); // Call clear method to remove all shapes.
        }

        else if (choice == 5) {

            MyTriangle triangle;
            if (fill) {
                triangle = new MyTriangle(oldX, oldY, newX, newY, gc.getColor(), true);
                gc.fillPolygon(new int[]{triangle.getOldX(), triangle.getNewX(), triangle.getNewX()}, new int[]{triangle.getOldY(), triangle.getNewY(), triangle.getOldY()}, 3);
            }
            else {
                triangle = new MyTriangle(oldX, oldY, newX, newY, gc.getColor(), false);
                gc.drawPolygon(new int[]{triangle.getOldX(), triangle.getNewX(), triangle.getNewX()}, new int[]{triangle.getOldY(), triangle.getNewY(), triangle.getOldY()}, 3);
            }

            undoStack.push(triangle);
            repaint();
        }

        else if (choice == 6) {
            // Call drawImport if the user wants to import an image.
            try {
                drawImport(oldX, oldY, newX, newY, gc.getColor());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to clear canvas.
    public void clear() {
        repaint();
        Color temp = gc.getColor(); // Use temp to store initial colour
        gc.setColor(BACKGROUND_COLOR); // Set colour to white and cover canvas.
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setColor(temp); // Set the colour back to the original colour so the user can carry on drawing with that colour.
        repaint();
    }
    
    public void undo() {

        // If the stack is not empty then we can pop off the shape we want to remove in the canvas.
        if (!undoStack.empty()) {

            MyShapes removedShape = undoStack.pop();
            redoStack.push(removedShape); // push this shape into the redoStack so we can retrieve it back if needed. 
            
            Stack<MyShapes> tempStack = new Stack<>();

            clear();

            // Push all shapes in the undoStack onto tempStack.
            for (MyShapes myShapes : undoStack) {
                tempStack.push(myShapes);
            }
            stackIterator(tempStack);
            
        }
    }

    public void redo() {

        if (!redoStack.isEmpty()) {

            MyShapes removedShape = redoStack.pop();
            undoStack.push(removedShape);

            Stack<MyShapes> tempStack = new Stack<>();

            clear();

            // Push all shapes in the undoStack onto tempStack.
            for (MyShapes myShapes : undoStack) {
                tempStack.push(myShapes);
            }
            stackIterator(tempStack);

        }
    }
    
    public void stackIterator(Stack<MyShapes> stack) {
        // Iterate through the stack and draw each shape in it.
        while (!stack.isEmpty()) {

            MyShapes tempShape = stack.pop();

            if (tempShape.getClass() == MyRectangle.class) {

                // Keep track of the original colour.
                Color current = gc.getColor();
                MyRectangle tempRectangle = (MyRectangle) tempShape;
                // Set the current colour to the colour the shape was in originally.
                gc.setColor(tempRectangle.getColor());

                if (tempRectangle.isFilled())
                    gc.fillRect(tempRectangle.getOldX(), tempRectangle.getOldY(), tempRectangle.getW(), tempRectangle.getH());
                else
                    gc.drawRect(tempRectangle.getOldX(), tempRectangle.getOldY(), tempRectangle.getW(), tempRectangle.getH());

                repaint();
                // Set the colour back to what it was originally so the user can continue drawing in that colour.
                gc.setColor(current);
            }
            // Repeat for all shapes.
            else if (tempShape.getClass() == MyTriangle.class) {

                Color current = gc.getColor();
                MyTriangle tempTriangle = (MyTriangle) tempShape;
                gc.setColor(tempTriangle.getColor());

                if (tempTriangle.isFilled())
                    gc.fillPolygon(new int[] {tempTriangle.getOldX(), tempTriangle.getNewX(), tempTriangle.getNewX()}, new int[] {tempTriangle.getOldY(), tempTriangle.getNewY(), tempTriangle.getOldY()}, 3);
                else
                    gc.drawPolygon(new int[] {tempTriangle.getOldX(), tempTriangle.getNewX(), tempTriangle.getNewX()}, new int[] {tempTriangle.getOldY(), tempTriangle.getNewY(), tempTriangle.getOldY()}, 3);

                repaint();
                gc.setColor(current);
            }

            else if (tempShape.getClass() == MyEllipse.class) {

                Color current = gc.getColor();
                MyEllipse tempEllipse = (MyEllipse) tempShape;
                gc.setColor(tempEllipse.getColor());

                if (tempEllipse.isFilled())
                    gc.fillOval(tempEllipse.getOldX(), tempEllipse.getOldY(), tempEllipse.getNewX(), tempEllipse.getNewY());
                else
                    gc.drawOval(tempEllipse.getOldX(), tempEllipse.getOldY(), tempEllipse.getNewX(), tempEllipse.getNewY());

                repaint();
                gc.setColor(current);
            }

            else if (tempShape.getClass() == MyLine.class) {

                Color current = gc.getColor();
                MyLine tempLine = (MyLine) tempShape;
                gc.setColor(tempLine.getColor());
                gc.drawLine(tempLine.getOldX(), tempLine.getOldY(), tempLine.getNewX(), tempLine.getNewY());
                repaint();
                gc.setColor(current);
            }

            else if (tempShape.getClass() == MyImage.class) {

                MyImage tempImage = (MyImage) tempShape;
                gc.drawImage(tempImage.getScaledImage(), tempImage.getOldX(), tempImage.getNewY(), null);
                repaint();
            }
        }
    }

    public void drawImport(int oldX, int oldY, int newX, int newY, Color color) throws IOException {

        // Get the path from the import class.
        path = Import.returnPath();
        repaint();

        File file = new File(path);
        BufferedImage before = ImageIO.read(file); // Read in the buffered image.

        // Get the width of the old image and get dimensions of the new image.
        int w = before.getWidth();
        int w2 = (Math.abs(newX - oldX));
        int h2 = (Math.abs(newY - oldY));

        // Scale the image by comparing the new width to the old width.
        // Done to ensure the image size is proportional to the mouse drag.
        double scale = (double) w2 / w;

        // Store the new image.
        BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);

        // Code for scaling from https://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
        // Scale this image.
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);
        scaleOp.filter(before, after);

        // Use the scaled image to create a new myImage and push it onto the stack.
        MyImage myImage = new MyImage(after, oldX, newY);
        undoStack.push(myImage);

        gc.drawImage(after, oldX,newY, null);
        repaint();

    }
    
    // Method to serialize the stack containing shapes and save it in a file.
    public void Serialize(String fileName) {
        try
        {
            // Write all objects contained in the stack.
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(undoStack);
            oos.close();
            fos.close();
            // Show message at the end.
            JOptionPane.showMessageDialog(frame, "Saved!");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    // Deserialize data from a file to load objects onto the canvas.
    public void Deserialize() {
        String fileName;
        // Take in the name of the file to be loaded.
        fileName = JOptionPane.showInputDialog("Please type the name of the file you want to load");
        System.out.println("Loading...." + fileName);

        // Store the shapes in a new stack.
        Stack deserialized;
        try
        {
            // Read in the data through an object input stream.
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            deserialized = (Stack) ois.readObject();
            System.out.println("Importing.... " + deserialized.toString());

            gc.setStroke(new BasicStroke(3));
            // Repeat same process as before to draw all images in the stack.
            stackIterator(deserialized);

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            // If there is an issue display the message to the user through a new JOptionPane.
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(frame, ioe.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public void mouseExited(MouseEvent e) 						
    {
    }

    public void mouseEntered(MouseEvent e)						
    {
    }

    public void mouseClicked(MouseEvent e) 						
    {

    }

    public void mouseDragged(MouseEvent re)
    {
    }

    public void mouseMoved(MouseEvent arg0)
    {
    }

}