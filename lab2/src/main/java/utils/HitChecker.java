package utils;

public class HitChecker {
    public static boolean hit(float x, float y, float r) {
        if (x > 0 && y > 0) {
            return false;
        } else if (x >= 0 && y <= 0) {
            return x <= r && (y*y + x * x <= r * r) ;
        } else if (x <= 0 && y <= 0) {
            return x >= -r && y >= -r;
        } else {
            return Math.abs(x) >= -r && Math.abs(y) <= r && (y <= x + r);
        }
    }
}
