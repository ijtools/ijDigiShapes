/**
 * 
 */
package net.ijt.digishapes;

import java.util.Locale;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ByteProcessor;
import net.ijt.geom2d.polygon.OrientedBox2D;

/**
 * @author dlegland
 *
 */
public class DiscretizedOrientedBox2D implements PlugIn
{
    static int count = 0;
    
    @Override
    public void run(String arg)
    {
        // create the dialog
        GenericDialog gd = new GenericDialog("Discretized Oriented Box");
        gd.addNumericField("Image Size X", 200, 0);
        gd.addNumericField("Image Size Y", 200, 0);
        gd.addNumericField("Center X", 100, 2);
        gd.addNumericField("Center Y", 100, 2);
        gd.addNumericField("Box Length", 50, 2);
        gd.addNumericField("Box Width", 20, 2);
        gd.addNumericField("Orientation (degrees)", 30, 2);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        int sizeX = (int) gd.getNextNumber();
        int sizeY = (int) gd.getNextNumber();
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double boxSize1 = gd.getNextNumber();
        double boxSize2 = gd.getNextNumber();
        double orient = gd.getNextNumber();
        
        // allocate image array
        ByteProcessor array = new ByteProcessor(sizeX, sizeY);
        // generate geometric shape
        OrientedBox2D box = new OrientedBox2D(centerX, centerY, boxSize1, boxSize2, orient);
        
        // iterate over image pixels
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                if (box.contains(x + 0.5, y + 0.5))
                {
                    array.set(x, y, 255);
                }
            }
        }
        
        // compute image name
        String newName = "orientedBox";
        if (count > 0)
        {
            newName = String.format(Locale.ENGLISH, "orientedBox-%02d", count);
        }
        count++;

        // create Image
        ImagePlus image = new ImagePlus(newName, array);
        image.show();
    }

}
