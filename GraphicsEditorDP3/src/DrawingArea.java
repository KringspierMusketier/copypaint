import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

//The controller object for handling the painting of shapes and editing of shapes
public class DrawingArea extends JComponent {
	
	private static final long serialVersionUID = 9044308264550141828L;
	
	//defines the border stroke for selected shapes 
	private static final Stroke DASHED_STROKE = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	
	private Image image; //the frame which will actually get drawn
	private Graphics2D g2; //the toolset for drawing 2D shapes
	private int currentX, currentY, oldX, oldY; //values for storing mouse positions
	private CommandManager manager = new CommandManager(); //handles commands
	private Receiver r = new Receiver(); //the model which holds all data
	
	private final JFileChooser fc = new JFileChooser(); //A JFrame dialog for loading and saving data
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
	
	//booleans for denoting whether the user is currently moving or expanding objects
	private boolean moving = false;
	private boolean expanding = false;
	
	public DrawingArea() {
		setDoubleBuffered(false);
		fc.setFileFilter(filter);
		addMouseListener(new MouseAdapter( ) {
			
			//actions to take when the mouse button is pressed
			public void mousePressed(MouseEvent e) {
				if (r.getTool() == Tool.DRAW) {
					//add a new shape to shapes based on the current mouse coordinates
					oldX = e.getX();
					oldY = e.getY();
					currentX = e.getX();
					currentY = e.getY();
					
					//New shape to be drawn is created as a StagingShapeLeaf
					ShapeLeaf s = new ShapeLeaf(oldX, oldY, (currentX - oldX), (currentY - oldY), r.getShapeType(), r.getSelectedColor());
					r.getStagingShapes().add(new StagingShapeLeaf(s, -1));
					repaint(); //function that needs to be called in order to refresh the drawing area
				} else if (r.getTool() == Tool.SELECT) {
					oldX = e.getX();
					oldY = e.getY();
					currentX = e.getX();
					currentY = e.getY();
					moving = false;
					expanding = false;
					
					int[] bounds = {oldX, oldY, 1, 1};
					r.getSelectRect().setXY(new Point(-1, -1)); //reset selection rectangle pos to outside the screen
					
					/**
					 * Checks if the user has clicked on the expansion rectangle first. If not, it checks for whether the mouse click is
					 * inside any of the shapes that have been selected before. These checks happen separately because you cannot expand
					 * and drag a shape at the same time, and the check for whether the expansion box has been clicked takes priority.
					 */
					if (AABBcheck(bounds, r.getExpandRect().getCoords())) {
						expanding = true;

					} else {
						for (ShapeLeaf s : r.getAllShapeLeaves()) {
							if (AABBcheck(bounds, s.getCoords()) && s.isSelected()) {
								moving = true;
								break;
							}
						}
					}
					
					//If a move or expand operation is about to be performed, StagingShapes are created of the selected Shapes which in turn
					//are hidden. After the operation is complete, the original Shapes will have the differences of the moved/resized
					//StagingShapes applied to them and be made visible again, and the StagingShapes will be deleted
					if (moving || expanding) {
						for (Shape s : r.getShapes()) {
							if (s.isSelected()) {
								if (s instanceof ShapeLeaf) {
									r.getStagingShapes().add(new StagingShapeLeaf((ShapeLeaf)s, r.getShapes().indexOf(s)));
									s.setVisible(false); //hides original shape while StagingShape is visibly being edited
								} else if (s instanceof ShapeGroup) {
									r.getStagingShapes().add(new StagingShapeGroup((ShapeGroup)s, r.getShapes().indexOf(s)));
									s.setVisible(true); 
								}
							}
						}
					}
					repaint();
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if (r.getTool() == Tool.DRAW) {
					//Finalize drawing a Shape by creating a Shape in place of the top StagingShape
					DrawCommand cmd = new DrawCommand(r.getStagingShapes().get(r.getStagingShapes().size() - 1), r);
					manager.execute(cmd);
				} else if (r.getTool() == Tool.SELECT) {
					ShapeLeaf expandRect = r.getExpandRect();
					if (!moving && !expanding) {
						//Select all shapes in the selection rectangle
						r.checkSelected(r.getSelectRect().getCoords());
					}
					
					if (moving) {
						//finalize the shape moving operation
						MoveCommand cmd = new MoveCommand(r);
						manager.execute(cmd);
					}
					
					if (expanding) {
						//finalize the shape resizing operation
						ResizeCommand cmd = new ResizeCommand(r);
						manager.execute(cmd);
					}
					
					//updates the old coordinates for all shapes
					for (ShapeLeaf s: r.getAllShapeLeaves()) {
						s.setOldXY(s.getXY());
						s.setOldWH(s.getWH());
					}
					expandRect.setOldXY(expandRect.getXY());
				}
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (r.getTool() == Tool.DRAW) {
					currentX = e.getX();
					currentY = e.getY();
					
					//dynamically draws a shape on screen based on the mouse position
					//the values of the x/y and width/height are switched when the user is trying to move the mouse to the left of 
					//the original click position
					Shape stage = r.getStagingShapes().get(r.getStagingShapes().size() - 1);
					((ShapeLeaf)stage).setX(Math.min(oldX, currentX));
					((ShapeLeaf)stage).setW(Math.abs(currentX - oldX));
					((ShapeLeaf)stage).setY(Math.min(oldY, currentY));
					((ShapeLeaf)stage).setH(Math.abs(currentY - oldY));
					repaint();
				} else if (r.getTool() == Tool.SELECT) {
					currentX = e.getX();
					currentY = e.getY();
					
					if (moving) {
						int offsetX = currentX - oldX;
						int offsetY = currentY - oldY;
						
						//The selected dragged shapes have their position updated using the difference between the old click position
						//and your current mouse position as a measure. The original position of the shapes before the moving started
						//is used as a reference instead of the newest position to prevent shapes from being dragged an exponentially
						//increasing distance
						for (Shape s : r.getStagingShapes()) {
							if (s instanceof StagingShapeLeaf) {
								Point old = ((StagingShapeLeaf)s).getOldXY();
								((StagingShapeLeaf)s).setX(old.x + offsetX);
								((StagingShapeLeaf)s).setY(old.y + offsetY);
							} else if (s instanceof StagingShapeGroup) {
								((StagingShapeGroup)s).appendXY(new Point(offsetX, offsetY));
							}
						}
						//the position of the expansion box is updated as well
						ShapeLeaf expandRect = r.getExpandRect();
						Point old = expandRect.getOldXY();
						expandRect.setX(old.x + offsetX);
						expandRect.setY(old.y + offsetY);
					} else if (expanding) {
						int offsetX = currentX - oldX;
						int offsetY = currentY - oldY;
						boolean x_limit = false;
						boolean y_limit = false;
						
						//updates the width/height of the selected shapes based on the dragged mouse distance
						//shapes have a minimal size of 2px and thus can't be shrunk beyond that
						for (Shape s : r.getStagingShapes()) {
							if (s instanceof StagingShapeLeaf) {
								Point oldWH = ((StagingShapeLeaf)s).getOldWH();
								Point newWH = new Point(oldWH.x + offsetX, oldWH.y + offsetY);
								if (newWH.x > 2) {
									((StagingShapeLeaf)s).setW(newWH.x);
								} else {
									x_limit = true;
								}
								
								if (newWH.y > 2) {
									((StagingShapeLeaf)s).setH(newWH.y);
								} else {
									y_limit = true;
								}
							} else if (s instanceof StagingShapeGroup) {
								((StagingShapeGroup)s).appendWH(new Point(offsetX, offsetY));
							}
						}
						ShapeLeaf expandRect = r.getExpandRect();
						Point old = expandRect.getOldXY();
						if (!x_limit) {
							expandRect.setX(old.x + offsetX);
						}
						if (!y_limit) {
							expandRect.setY(old.y + offsetY);
						}
						
					} else {
						//if during a mouse drag there's no move or resize operations going on, then the only other option is that the
						//selection rectangle is being dragged, so its dimensions are updated accordingly
						ShapeLeaf stage = r.getSelectRect();
						stage.setVisible(true);
						stage.setX(Math.min(oldX, currentX));
						stage.setW(Math.abs(currentX - oldX));
						stage.setY(Math.min(oldY, currentY));
						stage.setH(Math.abs(currentY - oldY));
					}
					repaint();
				}
			}
			
		});
		
	}
		
	//this method is called each time repaint() is called
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		//After painting operations the paint color is set back to the color selected before the operation started
		Color old = r.getSelectedColor().getAwtColor();
		Stroke oldStroke = g2.getStroke();
		
		g2.setPaint(Color.white);
		g2.fillRect(0,  0, getSize().width, getSize().height);
		
		//draws all shapes based on ShapeLeaf attributes
		for (ShapeLeaf s : r.getAllShapeLeaves()) {
			if (s.isVisible()) {
				int[] coords = s.getCoords();
				ShapeType shape = s.getShapeType();
				Color color = s.getShapeColor().getAwtColor();
				if (s.isSelected()) {
					//a dashed stroke is set for selected shapes
					g2.setStroke(DASHED_STROKE);
				}
				
				g2.setPaint(color);
				if (shape == ShapeType.RECTANGLE) {
					g2.drawRect(coords[0], coords[1], coords[2], coords[3]);
					if (s.isSelected()) {
						g2.setPaint(NamedColor.SELECT.getAwtColor());
						g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
					}
				} else if (shape == ShapeType.ELLIPSE) {
					g2.drawOval(coords[0], coords[1], coords[2], coords[3]);
					if (s.isSelected()) {
						g2.setPaint(NamedColor.SELECT.getAwtColor());
						g2.fillOval(coords[0], coords[1], coords[2], coords[3]);
					}
				}
				
				g2.setStroke(oldStroke);
			}
		}
		
		//if the expansion box is set to be visible, draw it too
		
		ShapeLeaf expandRect = r.getExpandRect();
		if (expandRect.isVisible()) {
			int[] coords = expandRect.getCoords();
			Color color = expandRect.getShapeColor().getAwtColor();
			g2.setPaint(color);
			g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
		}
		
		ShapeLeaf selectRect = r.getSelectRect();
		if (selectRect.isVisible()) {
			int[] coords = selectRect.getCoords();
			Color color = selectRect.getShapeColor().getAwtColor();
			g2.setPaint(color);
			g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
		}
		
		g2.setPaint(old);
		g.drawImage(image, 0, 0, null);
		
	}
	
	//clears all current data
	public void clear() {
		ClearCommand cmd = new ClearCommand(r);
		manager.execute(cmd);
		repaint();
	}
	
	//for drawing rectangles with the draw tool
	public void selectRectangle() {
		r.setShapeType(ShapeType.RECTANGLE);
	}
	
	//for drawing ellipses with the draw tool
	public void selectEllipse() {
		r.setShapeType(ShapeType.ELLIPSE);
	}
	
	//for selecting the draw tool, also unselects all shapes
	public void selectDraw() {
		r.setTool(Tool.DRAW);
		r.clearSelected();
		repaint();
	}
	
	//for selecting the select tool
	public void selectSelect() {
		r.setTool(Tool.SELECT);
		repaint();
	}
	
	//for giving shapes a red color
	public void selectRed() {
		r.setNamedColor(NamedColor.RED);
	}
	
	//for giving shapes a black color
	public void selectBlack() {
		r.setNamedColor(NamedColor.BLACK);
	}
	
	//Brings up a JFileChooser dialog to save the state of all current Shapes in a .txt file by calling their print() function
	//Each ShapeLeaf is textualized as "ShapeType ShapeColor X Y W H"\n
	//Each ShapeGroup starts with "group X" (X being equal to the amount of items the Group contains),
	//and all items it contains are prefaced with a \t character
	public void save() {
		if (r.getShapes().size() > 0) {
			int returnVal = fc.showSaveDialog(this);
			r.clearSelected();
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try (FileWriter fw = new FileWriter(fc.getSelectedFile() + ".txt")) {
					for (Shape s : r.getShapes()) {
						for (String str : s.print()) {
							fw.write(str);
						}
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Failed to save file");
					e.printStackTrace();
				}
			}
			
		} else {
			JOptionPane.showMessageDialog(this, "Draw some shapes first before saving!");
		}
	}
	
	//Clears all current Shapes and loads a set of new ones from a .txt file
	public void load() {
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try (FileReader fr = new FileReader(file)) {
				clear();
				BufferedReader br = new BufferedReader(fr);
				ArrayList<String> lines = new ArrayList<String>();
				String line = null;
				while ((line = br.readLine()) != null) {
					lines.add(line);
				}
				
				/**
				 * Each ShapeGroup in the loaded file is added on top of a Stack. All ShapeLeafs are then
				 * added to the top ShapeGroup. The amount of \t-characters at the start of each line are
				 * counted to determine which Group the next line belongs to. If the tabchar count is smaller
				 * than the stack size, then the top Group is popped and added to the next top Group. 
				 */
				Stack<ShapeGroup> composites = new Stack<ShapeGroup>();
				for (int z = 0; z < lines.size(); z++) {
					String s = lines.get(z);
					int tabs = 0;
					for (int i = 0; i < s.length(); i++) {
						if (s.charAt(i) == '\t') {
							tabs++;
						} else {
							break;
						}
					}
					
					if (tabs < composites.size()) {
						int diff = composites.size() - tabs;
						for (int i = 0; i < diff; i++) {
							if (composites.size() > 1) {
								ShapeGroup branch = composites.pop();
								composites.peek().add(branch);
							} else if (composites.size() == 1) {
								r.getShapes().add(composites.pop());
							}
						}
					}
					
					String[] src = s.trim().split(" ");
					if (src[0].contentEquals("group")) {
						//Create a new ShapeGroup when the next line starts with "group"
						ShapeGroup group = new ShapeGroup();
						composites.push(group);
					} else if (tabs == 0) {
						//Add a ShapeLeaf directly to the central ShapeList
						ShapeLeaf shape = new ShapeLeaf(Integer.parseInt(src[2]), Integer.parseInt(src[3]), Integer.parseInt(src[4]), Integer.parseInt(src[5]), ShapeType.valueOf(src[0].toUpperCase()), NamedColor.valueOf(src[1].toUpperCase()));
						r.getShapes().add(shape);
					} else if (tabs == composites.size()) {
						//Add a ShapeLeaf to the top ShapeGroup if the amount of tabchars is equal to the size of the stack
						ShapeLeaf shape = new ShapeLeaf(Integer.parseInt(src[2]), Integer.parseInt(src[3]), Integer.parseInt(src[4]), Integer.parseInt(src[5]), ShapeType.valueOf(src[0].toUpperCase()), NamedColor.valueOf(src[1].toUpperCase()));
						composites.peek().add(shape);
					}
					
					//If this line is the last one in the file, then the remainder of the ShapeGroups are popped back into
					//their parent Groups, and the topmost Group is added back into the Shapes list
					if (z == lines.size() - 1) {
						int b = composites.size();
						for (int i = 0; i < b; i++) {
							if (composites.size() > 1) {
								ShapeGroup branch = composites.pop();
								composites.peek().add(branch);
							} else if (composites.size() == 1) {
								r.getShapes().add(composites.pop());
							}
						}
					}
					
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Selected file does not exist");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Failed to load file");
			}
		}
	}
	
	//Tell CommandManager to undo the last action
	public void undo() {
		manager.undo();
		repaint();
	}
	
	//Tell CommandManager to redo the last action
	public void redo() {
		manager.redo();
		repaint();
	}
	
	//Tell CommandManager to group several selected shapes (if more than one has been selected)
	public void group() {
		int selected = 0;
		for (Shape s : r.getShapes()) {
			if (s.isSelected()) selected++;
		}
		
		if (selected > 1) {
			GroupCommand cmd = new GroupCommand(r);
			manager.execute(cmd);
			repaint();
		}
	}
	
	//Tell CommandManager to ungroup several selected shapes
	public void ungroup() {
		int selected = 0;
		for (Shape s : r.getShapes()) {
			if (s.isSelected()) selected++;
		}
		
		if (selected > 1) {
			UngroupCommand cmd = new UngroupCommand(r);
			manager.execute(cmd);
			repaint();
		}
	}
	
	//checks whether two rectangles intersect
	private boolean AABBcheck(int[] a, int[] b) {
		if (a[0] < b[0] + b[2] &&
			a[0] + a[2] > b[0] &&
			a[1] < b[1] + b[3] &&
			a[1] + a[3] > b[1]) {
			return true;
		}
		return false;
	}
}
