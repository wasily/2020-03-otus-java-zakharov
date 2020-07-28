package hw15;

public class Generator {
    private int last = 1;
    private int limit;
    private final int lBorder = 1;
    private final int rBorder;

    public Generator(int size) {
        this.limit = size;
        this.rBorder = size;
    }

    public int getNext() {
        if (last == limit) {
            if (last == rBorder) {
                limit = lBorder;
                return last--;
            } else {
                limit = rBorder;
                return last++;
            }
        }
        if (last > limit) {
            return last--;
        }
        return last++;
    }
}
