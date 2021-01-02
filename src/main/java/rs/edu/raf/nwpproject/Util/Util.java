package rs.edu.raf.nwpproject.Util;

public class Util {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.equals("NaN");
    }
}
