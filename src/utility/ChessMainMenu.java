package utility;

import java.util.Scanner;
import service.GameService;
import service.PlayerService;
import service.impl.GameServiceImpl;
import service.impl.PlayerServiceImpl;

public class ChessMainMenu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PlayerService playerService = new PlayerServiceImpl();

    public static void showMenu() {
        int choice = -1;
        while (choice != 6) {
            System.out.println("========================================================");
            System.out.println("                      CHESS MENU                        ");
            System.out.println("========================================================");
            System.out.println(" 1. Start a new game");
            System.out.println(" 2. Create a player");
            System.out.println(" 3. Continue previous game");
            System.out.println(" 4. review finished games");
            System.out.println(" 5. Leaderboard");
            System.out.println(" 6. Quit");
            System.out.println("========================================================");
            System.out.print(" Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println(" ▶ Invalid input. Please enter a number (1–6).");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    GameService gameService = new GameServiceImpl();
                    gameService.startGameSetup();
                    break;
                case 2:
                    System.out.println(" ▶ Please enter player name:");
                    String name = scanner.nextLine();
                    System.out.println(" ▶ Please enter player password:");
                    String password = scanner.nextLine();
                    playerService.createPlayer(name,password);
                    break;
                case 3:
                    GameService gameService3 = new GameServiceImpl();
                    ((GameServiceImpl) gameService3).continuePendingGame();
                    break;
                case 4:
                    GameService gameService4 = new GameServiceImpl();
                    ((GameServiceImpl) gameService4).loadFinishedGames();
                    break;
                case 5:
                    System.out.println(" ▶ Showing leaderboard...");
                    playerService.displayLeaderboard();
                    break;
                case 6:
                    System.out.println(" ▶ Thanks for playing! Goodbye.");
                    break;
                default:
                    System.out.println(" ▶ Invalid choice. Please pick between 1–6.");
            }
        }
    }
}
