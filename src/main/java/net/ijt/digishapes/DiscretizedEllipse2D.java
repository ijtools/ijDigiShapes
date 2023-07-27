/**
 * 
 */
package net.ijt.digishapes;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ByteProcessor;
import net.ijt.geom2d.curve.Ellipse2D;

/**
 * @author dlegland
 *
 */
public class DiscretizedEllipse2D implements PlugIn
{
    static int count = 0;
    
    @Override
    public void run(String arg)
    {
        // create the dialog
        GenericDialog gd = new GenericDialog("Discretized Ellipse");
        gd.addNumericField("Image Size X", 200, 0);
        gd.addNumericField("Image Size Y", 200, 0);
        gd.addNumericField("Center X", 100, 2);
        gd.addNumericField("Center Y", 100, 2);
        gd.addNumericField("Major Semi-Axis", 50, 2);
        gd.addNumericField("Minor Semi-Axis", 20, 2);
        gd.addNumericField("Orientation (degrees)", 30, 2);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        int sizeX = (int) gd.getNextNumber();
        int sizeY = (int) gd.getNextNumber();
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double semiAxis1 = gd.getNextNumber();
        double semiAxis2 = gd.getNextNumber();
        double orient = gd.getNextNumber();
        
        // allocate image array
        ByteProcessor array = new ByteProcessor(sizeX, sizeY);
        // generate geometric shape
        Ellipse2D elli = new Ellipse2D(centerX, centerY, semiAxis1, semiAxis2, orient);
        
        // iterate over image pixels
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                if (elli.isInside(x + 0.5, y + 0.5))
                {
                    array.set(x, y, 255);
                }
            }
        }
        
        // compute image name
        String newName = createName("Ellipse", count++);

        // create Image
        ImagePlus image = new ImagePlus(newName, array);
        image.show();
    }
    
    private String createName(String baseName, int count)
    {
        String name = baseName;
        if (count > 0)
        {
            name += String.format("-%02d", count);
        }
        return name;
    }

}
