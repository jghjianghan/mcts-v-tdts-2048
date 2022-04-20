
import controller.*;
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
        System.out.println("=========Game-Playing Agent Tester for 2048==========");
        System.out.println("Choose the algorithm:");
        System.out.println("1. Random");
        System.out.println("2. MCTS [UCT]");
        System.out.println("3. TDTS [Sarsa-UCT(lambda)]");
        System.out.println("Or");
        System.out.println("0. Play the 2048 Game");
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Command: ");
        
        String input = sc.nextLine();
        try {
            int command = Integer.parseInt(input);
            
            switch(command){
                case 1:
                    System.out.print("Number of experiment: ");
                    int iteration = Integer.parseInt(sc.nextLine());
                    RandomExperiment.averageAgentScore(iteration);
                    break;
                case 2: 
                    System.out.print("Number of experiment: ");
                    iteration = Integer.parseInt(sc.nextLine());
                    
                    System.out.print("Number of time steps [1000000]: ");
                    input = sc.nextLine();
                    int maxTick = (input.isEmpty()) ? 1000000 : Integer.parseInt(input);
                    
                    System.out.print("Exploration constant [sqrt(2)]: ");
                    input = sc.nextLine();
                    double explorationConst = (input.isEmpty()) ? Math.sqrt(2) : Double.parseDouble(input);
                    
                    System.out.println("Best-Child Policy");
                    System.out.println("1. Robust child (most visit)");
                    System.out.println("2. Max child (maximum utility)");
                    System.out.print("Choice [1]: ");
                    input = sc.nextLine();
                    
                    boolean isRobustChild = (input.isEmpty()) ? true : Integer.parseInt(input) == 1;
                    
                    System.out.println("Normalization Policy");
                    System.out.println("1. Space-Local Value Normalization");
                    System.out.println("2. No normalization");
                    System.out.print("Choice [1]: ");
                    input = sc.nextLine();
                    boolean isSpaceLocalNorm = (input.isEmpty()) ? true : Integer.parseInt(input) == 1;
                    
                    Experimentor.getMCTSAverageScore(iteration, maxTick, explorationConst, isRobustChild, isSpaceLocalNorm);
                    break;
                    
                case 3: 
                    System.out.print("Number of experiment: ");
                    iteration = Integer.parseInt(sc.nextLine());
                    
                    System.out.print("Number of time steps [1000000]: ");
                    input = sc.nextLine();
                    maxTick = (input.isEmpty()) ? 1000000 : Integer.parseInt(input);
                    
                    System.out.print("Exploration constant [sqrt(2)]: ");
                    input = sc.nextLine();
                    explorationConst = (input.isEmpty()) ? Math.sqrt(2) : Double.parseDouble(input);
                    
                    System.out.print("Reward discount rate (gamma) [1]: ");
                    input = sc.nextLine();
                    double gamma = (input.isEmpty()) ? 1 : Double.parseDouble(input);
                    
                    System.out.print("Eligibility trace decay rate (lambda) [1]: ");
                    input = sc.nextLine();
                    double lambda = (input.isEmpty()) ? 1 : Double.parseDouble(input);
                    
                    System.out.println("Best-Child Policy");
                    System.out.println("1. Robust child (most visit)");
                    System.out.println("2. Max child (maximum utility)");
                    System.out.print("Choice [1]: ");
                    input = sc.nextLine();
                    isRobustChild = (input.isEmpty()) ? true : Integer.parseInt(input) == 1;
                    
                    System.out.println("Normalization Policy");
                    System.out.println("1. Space-Local Value Normalization");
                    System.out.println("2. No normalization");
                    System.out.print("Choice [1]: ");
                    input = sc.nextLine();
                    isSpaceLocalNorm = (input.isEmpty()) ? true : Integer.parseInt(input) == 1;
                    
                    Experimentor.getTDTSAverageScore(iteration, maxTick, explorationConst, gamma, lambda, isRobustChild, isSpaceLocalNorm);
                    break;
                    
                case 0:
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
