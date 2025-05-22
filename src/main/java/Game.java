import java.util.*;

public class Game {
    public static void main(String[] args) {
        Sorter sorter = new Sorter();

        sorter.init();

//        printBigHello();

    }

    public static void printBigHello() {
        String[] helloArt = {
                "██████╗  ██████╗   ██╗  ██╗   ██╗  ███████╗  ████████╗",
                "██╔══██╗ ██╔══██╗  ██║  ██║   ██║  ██╔════╝  ╚══██╔══╝",
                "██████╔╝ ██████╔╝  ██║  ██║   ██║  █████╗       ██║   ",
                "██╔═══╝  ██╔══██╗  ██║  ╚██╗ ██╔╝  ██╔══╝       ██║   ",
                "██║      ██║  ██║  ██║   ╚████╔╝   ███████╗     ██║   ",
                "╚═╝      ╚═╝  ╚═╝  ╚═╝    ╚═══╝    ╚══════╝     ╚═╝   ",
                "\n".repeat(6)};

        for (String line : helloArt) {
            System.out.println(line);
        }
    }


}


