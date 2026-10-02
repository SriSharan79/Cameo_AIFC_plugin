package cameoUIDialogs.PGDialogs;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `OSIRISFrame` class is responsible for managing user interactions within the OSIRIS plugin by handling frame operations in Cameo Systems Modeler, providing essential functionalities to support Functional Hazard Assessment (FHA) workflows.
 */
public class OSIRISFrame extends JFrame {


	private static final long serialVersionUID = 1L;

/**
 * @return The `OSIRISFrame` method returns an instance of the `OSIRISFrame` class. This instance represents a graphical user interface window for the OSIRIS plugin, specifically designed to integrate with Cameo Systems Modeler for Functional Hazard Assessment (FHA). The returned object is configured with an icon image that has been loaded from the classpath, enhancing the visual representation and user experience of the application.

The method's primary purpose is to initialize and return a fully configured `OSIRISFrame` object, which includes setting up the window icon if the image resource is successfully loaded. This ensures that the frame is ready for further customization or display within the Cameo Systems Modeler environment.
 * @summary The `OSIRISFrame` method initializes an OSIRIS frame and sets its window icon using a specified image from the classpath.
 */
	public OSIRISFrame()  {
		// TODO Auto-generated constructor stub
		Image osirisIcon = null;

        try {
            // Get the URL for the image from the classpath.
            // This is a robust way to load the image, especially if the app is a JAR.
            // Replace "OSIRIS symbol.png" with the actual filename.
            // The image file should be in the 'resources' folder of your project.
            URL imageUrl = OSIRISFrame.class.getResource("/resources/OSIRIS symbol.png");
            
            if (imageUrl != null) {
                // ImageIO can now read the .png file successfully.
                osirisIcon = ImageIO.read(imageUrl);
                System.out.println("Image loaded successfully.");
            } else {
                System.err.println("Image file not found on the classpath.");
            }

        } catch (IOException e) {
            // Handle any errors during image loading
            System.err.println("Error reading the image file: " + e.getMessage());
            e.printStackTrace();
        }

        // Only try to set the icon if it was loaded successfully.
        if (osirisIcon != null) {
            this.setIconImage(osirisIcon);
        }
	}



}
