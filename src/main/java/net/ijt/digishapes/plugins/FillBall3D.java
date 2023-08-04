/**
 * 
 */
package net.ijt.digishapes.plugins;

import java.util.Locale;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

/**
 * Fills a ball defined by user parameters on the current 3D image.
 * 
 * @author dlegland
 *
 */
public class FillBall3D implements PlugIn
{
    @Override
    public void run(String arg)
    {
        ImagePlus imagePlus = IJ.getImage();
        if (imagePlus.getStackSize() == 1) 
        {
            IJ.error("Requires a Stack");
            return;
        }
        
        // create the dialog
        GenericDialog gd = new GenericDialog("Fill Ball");
        gd.addNumericField("Center_X", 100, 2);
        gd.addNumericField("Center_Y", 100, 2);
        gd.addNumericField("Center_Z", 100, 2);
        gd.addNumericField("Radius", 50, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double centerZ = gd.getNextNumber();
        double radius = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
//         generate geometric shape
//        Point3D center = new Point3D(centerX, centerY, centerZ);
        /// use squared radius to avoid square root computation
        double radius2 = square(radius);
        
        // retrieve image data
        ImageStack array = imagePlus.getStack();
        int sizeX = array.getWidth();
        int sizeY = array.getHeight();
        int sizeZ = array.getSize();
        
        // computation bounds
        int x0 = Math.max((int) Math.floor(centerX - radius), 0);
        int x1 = Math.min((int) Math.ceil(centerX + radius), sizeX - 1);
        int y0 = Math.max((int) Math.floor(centerY - radius), 0);
        int y1 = Math.min((int) Math.ceil(centerY + radius), sizeY - 1);
        int z0 = Math.max((int) Math.floor(centerZ - radius), 0);
        int z1 = Math.min((int) Math.ceil(centerZ + radius), sizeZ - 1);
        
        long t0 = System.currentTimeMillis();
        // iterate over image pixels
        for (int z = z0; z <= z1; z++)
        {
            double z2 = square(z - centerZ + 0.5);
            for (int y = y0; y <= y1; y++)
            {
                // pre-compute distance to z-axis containing point
                double h2 = z2 + square(y - centerY + 0.5);
                for (int x = x0; x <= x1; x++)
                {
                    if (square(x - centerX + 0.5) + h2 <= radius2)
                    {
                        array.setVoxel(x, y, z, fillValue);
                    }
                }
            }
        }
        
        // elapsed time in seconds
        long t1 = System.currentTimeMillis();
        double timeInSecs = (t1 - t0) / 1_000.0;
        
        // refresh display
        imagePlus.updateAndDraw();
        
        // show elapsed time
        String pattern = "%s: %.3f seconds";
        String status = String.format(Locale.ENGLISH, pattern, "Fill Ball", timeInSecs);
        IJ.showStatus(status);
    }
    
    private static final double square(double x)
    {
        return x * x;
    }
    
//    private static final double squaredHypot(double x, double y)
//    {
//        return x * x + y * y;
//    }
}
