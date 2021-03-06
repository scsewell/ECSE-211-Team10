package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import graph.Vertex;
import lejos.robotics.geometry.*;

/**
 * Contains useful utility methods frequently used throughout the code.
 * 
 * @author Scott Sewell
 */
public class Utils
{
    /**
     * Clamps a value within a range.
     * 
     * @param value
     *            the value to limit.
     * @param min
     *            the minimum value.
     * @param max
     *            the maximum value.
     * @return the clamped value.
     */
    public static float clamp(float value, float min, float max)
    {
        return Math.max(Math.min(value, max), min);
    }

    /**
     * Normalizes any angle to a [0, 360] angle.
     * 
     * @param angle
     *            an angle in degrees.
     * @return the normalized angle.
     */
    public static float normalizeAngle(float angle)
    {
        return (((angle % 360) + 360) % 360);
    }

    /**
     * Transforms any angle to a [-180, 180] angle.
     * 
     * @param angle
     *            an angle in degrees.
     * @return the transformed angle.
     */
    public static float toBearing(float angle)
    {
        return (((normalizeAngle(angle) + 180) % 360) - 180);
    }

    /**
     * Calculate the smallest angle between two vectors.
     * 
     * @param v1
     *            a vector.
     * @param v2
     *            a vector.
     * @return the angle from v1 to v2 in degrees.
     */
    public static float toBearing(Vector2 v1, Vector2 v2)
    {
        return toBearing(v2.angle() - v1.angle());
    }

    /**
     * Creates a rectangle defined by two opposite corners.
     * 
     * @param corner1
     *            a corner of the rectangle.
     * @param corner2
     *            the opposite corner of the rectangle.
     * @return a new rectangle.
     */
    public static Rectangle toRect(Vector2 corner1, Vector2 corner2)
    {
        return new Rectangle(
                Math.min(corner1.getX(), corner2.getX()),
                Math.min(corner1.getY(), corner2.getY()),
                Math.abs(corner1.getX() - corner2.getX()),
                Math.abs(corner1.getY() - corner2.getY())
                );
    }
    
    /**
     * Expands all sides of a rectangle outwards by a certain amount.
     * 
     * @param rect
     *            the rectangle to add padding to.
     * @param padding
     *            the amount to push each edge outwards. Can be negative to
     *            shrink rectangle.
     * @return a new padded rectangle.
     */
    public static Rectangle padRect(Rectangle rect, float padding)
    {
        return new Rectangle(
                rect.x - padding,
                rect.y - padding,
                rect.width + (padding * 2),
                rect.height + (padding * 2)
                );
    }

    /**
     * Checks if a point is contained within a rectangle.
     * 
     * @param point
     *            the point to check if contained.
     * @param rect
     *            the rectangle to check against.
     * @return true if point is contained.
     */
    public static boolean rectContains(Vector2 point, Rectangle rect)
    {
        return rect.contains(new Point(point.getX(), point.getY()));
    }

    /**
     * Checks if a line of a specific width intersects a rectangle defined by two opposite corners. This is done by testing if lines at the center and each edge of the line intersect.
     * 
     * @param lineStart
     *            the start of the line.
     * @param lineEnd
     *            the end of the line.
     * @param rect
     *            the rectangle to check against.
     * @return true if the given line is intersecting.
     */
    public static boolean lineIntersectsRect(Vector2 lineStart, Vector2 lineEnd, Rectangle rect)
    {
        return rect.intersectsLine(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY());
    }

    /**
     * Get the nearest point from a set to a given point.
     * 
     * @return the closest point.
     */
    public static Vector2 getNearestPoint(Vector2 point, List<Vector2> set)
    {
        float closestDistance = Float.MAX_VALUE;
        Vector2 closest = null;
        for (Vector2 v : set)
        {
            float distance = Vector2.distance(v, point);
            if (distance < closestDistance)
            {
                closestDistance = distance;
                closest = v;
            }
        }
        return closest;
    }

    /**
     * Returns the index of a list containing the value closest to a given
     * value.
     * 
     * @param value
     *            the value to look for.
     * @param list
     *            a non empty list.
     * @return the index where the closest value is located in the list.
     */
    public static int closestIndex(float value, List<Float> list)
    {
        float min = Float.MAX_VALUE;
        int closest = -1;

        for (int i = 0; i < list.size(); i++)
        {
            final float diff = Math.abs(list.get(i) - value);
            if (diff < min)
            {
                min = diff;
                closest = i;
            }
        }
        return closest;
    }

    /**
     * Halts the calling thread for short time.
     * 
     * @param milliseconds
     *            how long to wait until continuing.
     */
    public static void sleep(long milliseconds)
    {
        if (milliseconds > 0)
        {
            try
            {
                Thread.sleep(milliseconds);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Halts the thread until the next period begins.
     * 
     * @param period
     *            the desired period of the executing loop in milliseconds.
     * @param periodStart
     *            the system time in milliseconds at the start of the current
     *            period.
     */
    public static void sleepToNextPeroid(int period, long periodStart)
    {
        sleep(period - (System.currentTimeMillis() - periodStart));
    }

    /**
     * This method writes a map to a file.
     * 
     * @param data
     *            a non-null map.
     */
    public static void writeToFile(Map<Float,Float> data, String filename)
    {
        // Print output on to a txt file
        File file = new File(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename)))
        {
            if (!file.exists())
            {
                file.createNewFile();
            }
            int index = 0;
            for (Map.Entry<Float,Float> entry : data.entrySet())
            {
                writer.write(index + "\t" + String.format("%.1f", entry.getKey()) + "\t" + String.format("%.1f", entry.getValue()) + "\n");
                index++;
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Method for writing to a debug file. Appends to same file.
     * 
     * @param data
     *            any string.
     */
    public static void writeDebug(String data)
    {
        // Print output on to a txt file
        File file = new File("Debug.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Debug.txt", true)))
        {
            if (!file.exists())
            {
                file.createNewFile();
            }

            writer.write(data + "\n");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
