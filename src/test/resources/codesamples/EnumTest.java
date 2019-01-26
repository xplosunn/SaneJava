public class EnumTest {

    public enum E {
        A,
        B,
        C
    }

    public int faultyMethod(E e) {
        switch(e) {
            case A:
            case B:
                return 1;
            default:
                return 0;
        }
    }

    public void goodMethod(E e) {
        switch (e) {
            case A:
                break;
            case B:
                break;
            case C:
                break;
        }
    }

    public void anotherGoodMethod(E e) {
        switch (e) {
            case A:
                break;
            case B:
                break;
            case C:
                break;
            default:

        }
    }
}
