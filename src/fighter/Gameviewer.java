package fighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.Method;

// Take a look in the JRE (Java Runtime Environment) Library classes.jar for javax.swing in Eclipse IDE
// You will see all the .class files for each swing class, like JFrame, JComponent etc. (compiled Java bytecode)
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Class that contains the main method for the program and creates the frame
 * containing the component.
 */
@SuppressWarnings("deprecation")
public class Gameviewer extends JFrame {
	private static final long serialVersionUID = 1L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	JFrame frame;
	private final static int ONE_SECOND = 1000;
	private static Timer timer = null;
	private int screensizeChangeBuffer = 0;
	Game thegame = new Game();

	public Gameviewer(String s) throws Exception {

		super(s);

		/*
		 * JPanel p = new JPanel(); label = new JLabel("Key Listener!");
		 * p.add(label); add(p); addKeyListener(this); setSize(200, 100);
		 * setVisible(true);
		 */
		frame = new JFrame();
		frame.add(thegame);
		frame.setSize(1200, 675);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Smash");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thegame.addKeyListener(thegame);
		thegame.setFocusable(true);
		thegame.setPreferredSize(new Dimension(1200 * 100, 675 * 100));
		if (isMacOSX()) {
			enableFullScreenMode(frame);
		}
		frame.getContentPane().setBackground(Color.black);
		thegame.setDoubleBuffered(true);
		thegame.setVisible(true);
		frame.setVisible(true);
		Font myFont = new Font("Futura", Font.BOLD, 25);
		thegame.setFont(myFont);
		frame.addComponentListener(new ComponentListener() {

			public void componentResized(ComponentEvent e) {
				// do stuff
				if (screensizeChangeBuffer >= 5) {
					thegame.isnormalscreen = !thegame.isnormalscreen;
					screensizeChangeBuffer = 0;
					if (!thegame.isnormalscreen)
						frame.setSize((int) width, (int) height);
					else
						frame.setSize(1200, 675);
					System.out.println("Window has ben resized, scaling accordingly");
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		timer = new Timer(ONE_SECOND / 60, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (screensizeChangeBuffer < 5)
					screensizeChangeBuffer++;
				thegame.repaint();
			}
		});
	}

	public static void main(String[] args) throws Exception {
		new Gameviewer("GameViewer");
		timer.start();

	}

	public static void enableFullScreenMode(Window window) {
		String className = "com.apple.eawt.FullScreenUtilities";
		String methodName = "setWindowCanFullScreen";

		try {
			Class<?> clazz = Class.forName(className);
			Method method = clazz.getMethod(methodName, new Class<?>[] { Window.class, boolean.class });
			method.invoke(null, window, true);
		} catch (Throwable t) {
			System.err.println("Full screen mode is not supported");
			t.printStackTrace();
		}
	}

	private static boolean isMacOSX() {
		return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
	}

}
