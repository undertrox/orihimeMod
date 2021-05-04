package de.undertrox.orihimemod.document;

import java.util.Objects;

public class Point2d {
    public double x, y;

    @Override
    public String toString() {
        return "Point2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2d point2d = (Point2d) o;
        return Double.compare(point2d.x, x) == 0 && Double.compare(point2d.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceSquared(Point2d other) {
        return Math.pow(x-other.x, 2) + Math.pow(y-other.y, 2);
    }
}
