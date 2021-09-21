import com.phr.gui.Window

object Main {

    private var window : Window = Window;

    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting");

        window.run();

    }
}