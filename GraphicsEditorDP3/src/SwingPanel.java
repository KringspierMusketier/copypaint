import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//sets up the Swing Panel that displays the HUD, the drawing area, and all buttons
public class SwingPanel {
	
	private static final int WINDOW_X = 1024;
	private static final int WINDOW_Y = 768;
	private static final String WINDOW_TITLE = "Copypaint";
	
	private JButton clearBtn, redBtn, blackBtn, rectBtn, ellBtn, drawBtn, selectBtn, loadBtn, saveBtn, undoBtn, redoBtn, groupBtn, ungroupBtn;
	private DrawingArea window;
	
	//Maps functions from DrawingArea to each button
	private ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clearBtn) {
				window.clear();
			} else if (e.getSource() == redBtn) {
				window.selectRed();
			} else if (e.getSource() == blackBtn) {
				window.selectBlack();
			} else if (e.getSource() == rectBtn) {
				window.selectRectangle();
			} else if (e.getSource() == ellBtn) {
				window.selectEllipse();
			} else if (e.getSource() == drawBtn) {
				window.selectDraw();
			} else if (e.getSource() == selectBtn) {
				window.selectSelect();
			} else if (e.getSource() == loadBtn) {
				window.load();
			} else if (e.getSource() == saveBtn) {
				window.save();
			} else if (e.getSource() == undoBtn) {
				window.undo();
			} else if (e.getSource() == redoBtn) {
				window.redo();
			} else if (e.getSource() == groupBtn) {
				window.group();
			} else if (e.getSource() == ungroupBtn) {
				window.ungroup();
			}
		}
	};
	
	public static void main(String[] args) {
		new SwingPanel().show();
	}
	
	public void show() {
		JFrame frame = new JFrame(WINDOW_TITLE);
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		window = new DrawingArea();
		content.add(window, BorderLayout.CENTER);
		JPanel controls = new JPanel();
		
		//maps a listener to each button
		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(actionListener);
		blackBtn = new JButton("Black");
		blackBtn.addActionListener(actionListener);
		redBtn = new JButton("Red");
		redBtn.addActionListener(actionListener);
		rectBtn = new JButton("Rectangle");
		rectBtn.addActionListener(actionListener);
		ellBtn = new JButton("Ellipse");
		ellBtn.addActionListener(actionListener);
		drawBtn = new JButton("Draw");
		drawBtn.addActionListener(actionListener);
		selectBtn = new JButton("Select");
		selectBtn.addActionListener(actionListener);
		loadBtn = new JButton("Load");
		loadBtn.addActionListener(actionListener);
		saveBtn = new JButton("Save");
		saveBtn.addActionListener(actionListener);
		undoBtn = new JButton("Undo");
		undoBtn.addActionListener(actionListener);
		redoBtn = new JButton("Redo");
		redoBtn.addActionListener(actionListener);
		groupBtn = new JButton("Group");
		groupBtn.addActionListener(actionListener);
		ungroupBtn = new JButton("Ungroup");
		ungroupBtn.addActionListener(actionListener);
		
		//maps buttons to a JPanel, which are later added to the content of the Swing Panel to the North
		controls.add(clearBtn);
		controls.add(redBtn);
		controls.add(blackBtn);
		controls.add(rectBtn);
		controls.add(ellBtn);
		controls.add(drawBtn);
		controls.add(selectBtn);
		controls.add(loadBtn);
		controls.add(saveBtn);
		controls.add(undoBtn);
		controls.add(redoBtn);
		controls.add(groupBtn);
		controls.add(ungroupBtn);
		
		content.add(controls, BorderLayout.NORTH);
		frame.setSize(WINDOW_X, WINDOW_Y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
