package ar.edu.itba.ss;

public class Utils {

    public static void printLoadingBar(double percentage, int loadingBarSize) {
        StringBuilder loadingBar = new StringBuilder("[");
        for (int h = 0; h < loadingBarSize; h++) {
            if (h < Math.ceil(percentage * loadingBarSize) - 1)
                loadingBar.append("█");
            else if (h < Math.ceil(percentage * loadingBarSize)) {
                if (h < percentage * loadingBarSize - 0.5) {
                    loadingBar.append("▄");
                } else {
                    loadingBar.append(" ");
                }
            }
            else
                loadingBar.append(" ");
        }
        loadingBar.append("]");
        System.out.printf("%s %d%% Completed\r", loadingBar, (int) (percentage * 100));
    }

}
