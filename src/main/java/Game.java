public class Game {
    public static void main(String[] args) {
        Sorter sorter = new Sorter();
        printBigHello();
        sorter.init();
    }

    public static void printBigHello() {
        String[] bottleGameArt = {
                "\n",
                "██████╗   ██████╗  ████████╗ ████████╗ ██═╗       ███████╗      ██████╗   █████╗  ███╗   ███╗ ███████╗",
                "██╔══██╗ ██╔═══██╗ ╚══██╔══╝ ╚══██╔══╝ ██ ║       ██╔════╝     ██╔════╝  ██╔══██╗ ████╗ ████║ ██╔════╝",
                "██████╔╝ ██║   ██║    ██║       ██║    ██ ║       █████╗       ██║  ███╗ ███████║ ██╔████╔██║ █████╗  ",
                "██╔══██╗ ██║   ██║    ██║       ██║    ██ ║       ██╔══╝       ██║   ██║ ██╔══██║ ██║╚██╔╝██║ ██╔══╝  ",
                "██████╔╝ ╚██████╔╝    ██║       ██║    ████████╗  ███████╗     ╚██████╔╝ ██║  ██║ ██║ ╚═╝ ██║ ███████╗",
                "╚═════╝   ╚═════╝     ╚═╝       ╚═╝    ╚═══════╝  ╚══════╝      ╚═════╝  ╚═╝  ╚═╝ ╚═╝     ╚═╝ ╚══════╝",
                "\n"
        };

        for (String line : bottleGameArt) {
            System.out.println(line);
        }
    }


}


