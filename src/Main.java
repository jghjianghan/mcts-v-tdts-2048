
import controller.Experimentor;
import controller.GameController;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import io.GraphicalUI;
import java.util.Scanner;

/**
 * The entry point of the application
 * @author Jiang Han
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=========TDTS vs MCTS in 2048==========");
        System.out.println("1. Run single experiment [MCTS]");
        System.out.println("2. Run multiple experiment, get average [MCTS]");
        System.out.println("3. Play the 2048 game (GUI)");
        Scanner sc = new Scanner(System.in);
        System.out.print("Command: ");
        
        String input = sc.nextLine();
        try {
            int command = Integer.parseInt(input);
            switch (command){
                case 1:
                    System.out.print("Maximum Tick: ");
                    int maxTick = Integer.parseInt(sc.nextLine());
                    System.out.print("Exploration constant [sqrt(2)]: ");
                    input = sc.nextLine();
                    double explorationConst;
                    if (input.isEmpty()){
                        explorationConst = Math.sqrt(2);
                    } else {
                        explorationConst = Double.parseDouble(sc.nextLine());
                    }
                    Experimentor.runMCTSDetailed(maxTick, explorationConst);
                    
                    break;
                case 2:
                    System.out.print("Number of experiment: ");
                    int iteration = Integer.parseInt(sc.nextLine());
                    System.out.print("Maximum Tick: ");
                    maxTick = Integer.parseInt(sc.nextLine());
                    System.out.print("Exploration constant [sqrt(2)]: ");
                    input = sc.nextLine();
                    
                    if (input.isEmpty()){
                        explorationConst = Math.sqrt(2);
                    } else {
                        explorationConst = Double.parseDouble(sc.nextLine());
                    }
                    Experimentor.getMCTSAverageScore(iteration, maxTick, explorationConst);
                    break;
                case 3:
                    startGame();
                    break;
                default:
                    System.err.println("Unknown command");
            }
        } catch (NumberFormatException ex){
            System.err.println("Invalid input (should be number)");
        }
    }
    
    private static void startGame(){
        JFrame f = new JFrame();
        GraphicalUI panel = new GraphicalUI();
        panel.setFocusable(true);
        f.getContentPane().add("Center", panel);
        f.getContentPane().setPreferredSize(new Dimension(GraphicalUI.SCREEN_WIDTH, GraphicalUI.SCREEN_HEIGHT));
        
        //Frame icon & title
        URL logoUrl = Main.class.getClassLoader().getResource("images/2048_logo.png");
        ImageIcon imgLogo = new ImageIcon(logoUrl);
        f.setIconImage(imgLogo.getImage());
        f.setTitle("Stunning 2048 - Jiang Han (6181801034)");
        
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        new GameController(panel);
    }
}
