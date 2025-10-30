/**
 * 
 */
package net.ijt.digishapes.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import net.ijt.digishapes.util.ImageUtils;
import net.ijt.geometry.geom2d.curve.Ellipse2D;

/**
 * Fills an ellipse defined by user parameters on the current image.
 * 
 * @author dlegland
 *
 */
public class FillEllipse2D implements PlugIn
{
    @Override
    public void run(String arg)
    {
        ImagePlus imagePlus = IJ.getImage();
        
        // create the dialog
        GenericDialog gd = new GenericDialog("Fill Ellipse");
        gd.addNumericField("Center_X", 100, 2);
        gd.addNumericField("Center_Y", 100, 2);
        gd.addNumericField("Major Semi-Axis", 50, 2);
        gd.addNumericField("Minor Semi-Axis", 20, 2);
        gd.addNumericField("Orientation (degrees)", 30, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double semiAxis1 = gd.getNextNumber();
        double semiAxis2 = gd.getNextNumber();
        double orient = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
        // generate geometric shape
        Ellipse2D elli = new Ellipse2D(centerX, centerY, semiAxis1, semiAxis2, orient);
        
        // retrieve image data
        ImageProcessor array = imagePlus.getProcessor();
        
        // iterate over image pixels
        ImageUtils.fill(array, (x,y) -> elli.isInside(x + 0.5, y + 0.5), fillValue);
        
        // refresh display
        imagePlus.updateImage();
        imagePlus.updateAndDraw();
    }
}
