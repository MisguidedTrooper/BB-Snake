import java.awt.EventQueue;
import javax.swing.JFrame;
//Runs the snake program
public class Snake extends JFrame {

    public Snake() {
        
        initUI();
    }
    //Makes the user interface
    private void initUI() {

        add(new Board());
              
        setResizable(false);
        pack();
        
        
        setTitle("BB-Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
            
        });
    }
}
