/**
 * 
 */
package net.ijt.digishapes.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import net.ijt.digishapes.shapes2d.Capsule2D;
import net.ijt.digishapes.util.ImageUtils;

/**
 * Fills a capsule shape defined by user parameters on the current image.
 * 
 * @author dlegland
 *
 */
public class FillCapsule2D implements PlugIn
{
    @Override
    public void run(String arg)
    {
        ImagePlus imagePlus = IJ.getImage();
        
        // create the dialog
        GenericDialog gd = new GenericDialog("Fill Capsule");
        gd.addNumericField("Center_X", 100, 2);
        gd.addNumericField("Center_Y", 100, 2);
        gd.addNumericField("Side_Length", 50, 2);
        gd.addNumericField("Thickness", 20, 2);
        gd.addNumericField("Orientation (degrees)", 30, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double sideLength = gd.getNextNumber();
        double thickness = gd.getNextNumber();
        double orient = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
        // generate geometric shape
        Capsule2D box = new Capsule2D(centerX, centerY, sideLength, thickness * 0.5, orient);
        
        // retrieve image data
        ImageProcessor array = imagePlus.getProcessor();
        
        // iterate over image pixels
        ImageUtils.fill(array, (x,y) -> box.isInside(x + 0.5, y + 0.5), fillValue);
        
        // refresh display
        imagePlus.updateImage();
        imagePlus.updateAndDraw();
    }
}
