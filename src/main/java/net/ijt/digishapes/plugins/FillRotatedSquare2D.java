/**
 * 
 */
package net.ijt.digishapes.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import net.ijt.geom2d.polygon.OrientedBox2D;

/**
 * Fills a rotated square defined by user parameters on the current image.
 * 
 * @author dlegland
 *
 */
public class FillRotatedSquare2D implements PlugIn
{
    @Override
    public void run(String arg)
    {
        ImagePlus imagePlus = IJ.getImage();
        
        // create the dialog
        GenericDialog gd = new GenericDialog("Fill Rotated Square");
        gd.addNumericField("Center_X", 100, 2);
        gd.addNumericField("Center_Y", 100, 2);
        gd.addNumericField("Side_Length", 60, 2);
        gd.addNumericField("Orientation (degrees)", 30, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double sideLength = gd.getNextNumber();
        double orient = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
        // generate geometric shape
        OrientedBox2D box = new OrientedBox2D(centerX, centerY, sideLength, sideLength, orient);
        
        // retrieve image data
        ImageProcessor array = imagePlus.getProcessor();
        int sizeX = array.getWidth();
        int sizeY = array.getHeight();
        
        // iterate over image pixels
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                if (box.isInside(x + 0.5, y + 0.5))
                {
                    array.setf(x, y, fillValue);
                }
            }
        }
        
        // refresh display
        imagePlus.updateImage();
        imagePlus.updateAndDraw();
    }
}
