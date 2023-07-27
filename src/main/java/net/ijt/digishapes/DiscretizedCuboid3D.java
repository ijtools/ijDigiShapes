/**
 * 
 */
package net.ijt.digishapes;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import net.ijt.digishapes.shapes3d.Cuboid3D;

/**
 * @author dlegland
 *
 */
public class DiscretizedCuboid3D implements PlugIn
{
    static int count = 0;
    
    @Override
    public void run(String arg)
    {
        // create the dialog
        GenericDialog gd = new GenericDialog("Discretized Cuboid");
        gd.addNumericField("Image Size X", 100, 0);
        gd.addNumericField("Image Size Y", 100, 0);
        gd.addNumericField("Image Size Z", 100, 0);
        gd.addNumericField("Center X", 50, 2);
        gd.addNumericField("Center Y", 50, 2);
        gd.addNumericField("Center Z", 50, 2);
        gd.addNumericField("Side Length 1", 80, 2);
        gd.addNumericField("Side Length 2", 40, 2);
        gd.addNumericField("Side Length 3", 20, 2);
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
        double sideLength1 = gd.getNextNumber();
        double sideLength2 = gd.getNextNumber();
        double sideLength3 = gd.getNextNumber();
        double eulerAngleX = gd.getNextNumber();
        double eulerAngleY = gd.getNextNumber();
        double eulerAngleZ = gd.getNextNumber();
        
        // allocate image array
        ImageStack stack = ImageStack.create(sizeX, sizeX, sizeY, 8);
        
        // generate geometric shape
        Cuboid3D cuboid = new Cuboid3D(centerX, centerY, centerZ, sideLength1, sideLength2, sideLength3, eulerAngleX, eulerAngleY, eulerAngleZ);
        
        // iterate over image voxels
        for (int z = 0; z < sizeZ; z++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    if (cuboid.isInside(x + 0.5, y + 0.5, z + 0.5))
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }

        // compute image name
        String newName = createName("Cuboid", count++);

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
