import android.content.ComponentName;
import android.content.Context;

public class TestComponentName {
    public static void main(String[] args) {
        // Just checking if compilation fails
    }
    public void test(Context context, String className) {
        new ComponentName(context, className);
    }
}
