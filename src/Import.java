import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Import extends MouseAdapter {

    // Path the file is being imported from.
    private static String path;

    public void getPath() throws IOException {

        // Create a new file chooser.
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose the file you want to import onto the canvas");

        // Only display image files.
        // Filter code from https://stackoverflow.com/questions/18575655/how-to-restrict-file-choosers-in-java-to-specific-files by Josh M.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        // Set it to open to the current directory.
        fileChooser.setCurrentDirectory(new java.io.File("."));

        // When this button is pressed the file selected is confirmed.
        JButton open = new JButton();
        if (fileChooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose" + fileChooser.getSelectedFile().getAbsolutePath());
            path = fileChooser.getSelectedFile().getAbsolutePath();
        }

    }

    // Call getPath() when the button is clicked.
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked");
        try {
            getPath();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Getter used to retrieve the path.
    public static String returnPath() {
        return path;
    }


}
