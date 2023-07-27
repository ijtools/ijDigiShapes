/**
 * 
 */
package net.ijt.digishapes;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import net.ijt.digishapes.shapes3d.Cube3D;

/**
 * @author dlegland
 *
 */
public class DiscretizedCube3D implements PlugIn
{
    static int count = 0;
    
    @Override
    public void run(String arg)
    {
        // create the dialog
        GenericDialog gd = new GenericDialog("Discretized Cube 3D");
        gd.addNumericField("Image Size X", 100, 0);
        gd.addNumericField("Image Size Y", 100, 0);
        gd.addNumericField("Image Size Z", 100, 0);
        gd.addNumericField("Center X", 50, 2);
        gd.addNumericField("Center Y", 50, 2);
        gd.addNumericField("Center Z", 50, 2);
        gd.addNumericField("Side Length", 80, 2);
        gd.addNumericField("Euler Angle X (degrees)", 0, 2);
        gd.addNumericField("Euler Angle Y (degrees)", 0, 2);
        gd.addNumericField("Euler Angle Z (degrees)", 0, 2);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        int sizeX = (int) gd.getNextNumber();
        int sizeY = (int) gd.getNextNumber();
        int sizeZ = (int) gd.getNextNumber();
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double centerZ = gd.getNextNumber();
        double sideLength = gd.getNextNumber();
        double eulerAngleX = gd.getNextNumber();
        double eulerAngleY = gd.getNextNumber();
        double eulerAngleZ = gd.getNextNumber();
        
        // allocate image array
        ImageStack stack = ImageStack.create(sizeX, sizeX, sizeY, 8);
        
        // generate geometric shape
        Cube3D cube = new Cube3D(centerX, centerY, centerZ, sideLength, eulerAngleX, eulerAngleY, eulerAngleZ);
        
        // iterate over image voxels
        for (int z = 0; z < sizeZ; z++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    if (cube.isInside(x + 0.5, y + 0.5, z + 0.5))
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }

        // compute image name
        String newName = createName("Cube", count++);

        // create Image
        ImagePlus image = new ImagePlus(newName, stack);
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
