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
public class Cube3D
{
    // ===================================================================
    // Class variables

    /**
     * The center of the cube.
     */
    final Point3D center;

    final double sideLength;

    /**
     * The first rotation applied to the cube, around the X-axis, in degrees.
     */
    final double eulerAngleX; 
    /**
     * The second rotation applied to the cube, around the Y-axis, in degrees.
     */
    final double eulerAngleY; 
    /**
     * The third rotation applied to the cube, around the Z-axis, in degrees.
     */
    final double eulerAngleZ;
    
    
    // ===================================================================
    // Constructors

    /**
     * Creates a new cube aligned with the main axes.
     * 
     * @param center
     *            the center of the cube
     * @param sideLength
     *            the side length of the cube
     */
    public Cube3D(Point3D center, double sideLength)
    {
        this.center = center;
        this.sideLength = sideLength;
        this.eulerAngleX = 0.0;
        this.eulerAngleY = 0.0;
        this.eulerAngleZ = 0.0;
    }

    /**
     * Creates a new cube with a specific orientation given by three Euler
     * angles (in degrees).
     * 
     * @param centerX
     *            the x-coordinate of cube center
     * @param centerY
     *            the y-coordinate of cube center
     * @param centerZ
     *            the z-coordinate of cube center
     * @param sideLength
     *            the side length of the cube
     * @param eulerAngleX
     *            the first rotation applied to the cube, around the
     *            X-axis, in degrees.
     * @param eulerAngleY
     *            the second rotation applied to the cube, around the
     *            Y-axis, in degrees.
     * @param eulerAngleZ
     *            the third rotation applied to the cube, around the
     *            Z-axis, in degrees.
     */
    public Cube3D(double centerX, double centerY, double centerZ, double sideLength, double eulerAngleX, double eulerAngleY, double eulerAngleZ)
    {
        this.center = new Point3D(centerX, centerY, centerZ);
        this.sideLength = sideLength;
        this.eulerAngleX = eulerAngleX;
        this.eulerAngleY = eulerAngleY;
        this.eulerAngleZ = eulerAngleZ;
    }

    
    // ===================================================================
    // Methods specific to Cube3D
    
    /**
     * Returns the center of this cube as a Point3D.
     * 
     * @return the center of the cube.
     */
    public Point3D center()
    {
        return this.center;
    }
    
    public double sideLength()
    {
        return sideLength;
    }
    
    /**
     * Returns the orientation of this cube, as a Rotation3D object. 
     * 
     * @return the orientation of the cube.
     */
    public Rotation3D orientation()
    {
        return Rotation3D.fromEulerAngles(Math.toRadians(eulerAngleX), Math.toRadians(eulerAngleY), Math.toRadians(eulerAngleZ));
    }
    
    /**
     * Returns the three Euler angles that define the orientation of this cube,
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
     * cube instance.
     * 
     * @return the affine transform that will map a centered unit cube to this
     *         cube instance.
     */
    private AffineTransform3D localToGlobalTransform()
    {
        AffineTransform3D sca = AffineTransform3D.createScaling(sideLength * 0.5);
        AffineTransform3D rot = AffineTransform3D.fromMatrix(orientation().affineMatrix());
        AffineTransform3D tra = AffineTransform3D.createTranslation(center);
        return tra.concatenate(rot).concatenate(sca);
    }
    
    private AffineTransform3D globalToLocalTransform()
    {
        return localToGlobalTransform().inverse();
    }

}
