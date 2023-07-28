/**
 * 
 */
package net.ijt.digishapes.shapes3d;

import net.ijt.geom3d.AffineTransform3D;
import net.ijt.geom3d.Point3D;
import net.ijt.geom3d.Rotation3D;

/**
 * @author dlegland
 *
 */
public class Cuboid3D
{
    // ===================================================================
    // Class variables

    /**
     * The center of the cuboid.
     */
    final Point3D center;

    final double sideLength1;

    final double sideLength2;

    final double sideLength3;

    /**
     * The first rotation applied to the cuboid, around the X-axis, in degrees.
     */
    final double eulerAngleX; 
    /**
     * The second rotation applied to the cuboid, around the Y-axis, in degrees.
     */
    final double eulerAngleY; 
    /**
     * The third rotation applied to the cuboid, around the Z-axis, in degrees.
     */
    final double eulerAngleZ;
    
    
    // ===================================================================
    // Constructors

    /**
     * Creates a new cuboid aligned with the main axes.
     * 
     * @param center
     *            the center of the cuboid
     * @param sideLength
     *            the side length of the cuboid
     */
    public Cuboid3D(Point3D center, double sideLength1, double sideLength2, double sideLength3)
    {
        this.center = center;
        this.sideLength1 = sideLength1;
        this.sideLength2 = sideLength2;
        this.sideLength3 = sideLength3;
        this.eulerAngleX = 0.0;
        this.eulerAngleY = 0.0;
        this.eulerAngleZ = 0.0;
    }

    /**
     * Creates a new cuboid with a specific orientation given by three Euler
     * angles (in degrees).
     * 
     * @param centerX
     *            the x-coordinate of cuboid center
     * @param centerY
     *            the y-coordinate of cuboid center
     * @param centerZ
     *            the z-coordinate of cuboid center
     * @param sideLength
     *            the side length of the cuboid
     * @param eulerAngleX
     *            the first rotation applied to the cuboid, around the
     *            X-axis, in degrees.
     * @param eulerAngleY
     *            the second rotation applied to the cuboid, around the
     *            Y-axis, in degrees.
     * @param eulerAngleZ
     *            the third rotation applied to the cuboid, around the
     *            Z-axis, in degrees.
     */
    public Cuboid3D(double centerX, double centerY, double centerZ, double sideLength1, double sideLength2, double sideLength3, double eulerAngleX, double eulerAngleY, double eulerAngleZ)
    {
        this.center = new Point3D(centerX, centerY, centerZ);
        this.sideLength1 = sideLength1;
        this.sideLength2 = sideLength2;
        this.sideLength3 = sideLength3;
        this.eulerAngleX = eulerAngleX;
        this.eulerAngleY = eulerAngleY;
        this.eulerAngleZ = eulerAngleZ;
    }

    
    // ===================================================================
    // Methods specific to Cuboid3D
    
    /**
     * Returns the center of this cuboid as a Point3D.
     * 
     * @return the center of the cuboid.
     */
    public Point3D center()
    {
        return this.center;
    }
    
    public double sideLength1()
    {
        return sideLength1;
    }
    
    public double sideLength2()
    {
        return sideLength2;
    }
    
    public double sideLength3()
    {
        return sideLength3;
    }
    
    /**
     * Returns the orientation of this cuboid, as a Rotation3D object. 
     * 
     * @return the orientation of the cuboid.
     */
    public Rotation3D orientation()
    {
        return Rotation3D.fromEulerAngles(Math.toRadians(eulerAngleX), Math.toRadians(eulerAngleY), Math.toRadians(eulerAngleZ));
    }
    
    /**
     * Returns the three Euler angles that define the orientation of this cuboid,
     * in an array with three elements. Angles are given in degrees, in XYZ
     * order.
     * 
     * @return the three Euler angles defining the orientation, in degrees in
     *         XYZ order.
     */
    public double[] eulerAngles()
    {
        return new double[] {eulerAngleX, eulerAngleY, eulerAngleZ};
    }
    
    /**
     * Checks if the specified point is contained within the domain bounded by
     * this boundary.
     * 
     * @param point
     *            the point to test
     * @return true is the point is within the domain corresponding to this
     *         boundary.
     */
    public boolean isInside(Point3D point)
    {
        Point3D pt = globalToLocalTransform().transform(point);
        if (Math.abs(pt.x()) > 1) return false;
        if (Math.abs(pt.y()) > 1) return false;
        if (Math.abs(pt.z()) > 1) return false;
        return true;
    }

    /**
     * Checks if the specified point is contained within the domain bounded by
     * this boundary.
     * 
     * @param x
     *            the x-coordinate of the point to test
     * @param y
     *            the y-coordinate of the point to test
     * @return true is the point is within the domain corresponding to this
     *         boundary.
     */
    public boolean isInside(double x, double y, double z)
    {
        return isInside(new Point3D(x, y, z));
    }
    
    /**
     * Creates the affine transform that will map a centered unit cube to this
     * cuboid instance.
     * 
     * @return the affine transform that will map a centered unit cube to this
     *         cuboid instance.
     */
    private AffineTransform3D localToGlobalTransform()
    {
        AffineTransform3D sca = AffineTransform3D.createScaling(sideLength1 * 0.5, sideLength2 * 0.5, sideLength3 * 0.5);
        AffineTransform3D rot = AffineTransform3D.fromMatrix(orientation().affineMatrix());
        AffineTransform3D tra = AffineTransform3D.createTranslation(center);
        return tra.concatenate(rot).concatenate(sca);
    }
    
    private AffineTransform3D globalToLocalTransform()
    {
        return localToGlobalTransform().inverse();
    }

}
