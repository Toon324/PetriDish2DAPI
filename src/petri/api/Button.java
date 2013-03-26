package petri.api;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

/**
 * Graphic representing a clickable area for game control.
 * 
 * @author Cody Swendrowski
 */
public class Button {
	protected int width, height, x_pos, y_pos;
	protected String text;
	protected boolean clicked, enabled;
	protected Polygon bounds;
	protected Color c1, c2, font;
	protected Font f = new Font("Serif", Font.BOLD, 25);

	/**
	 * Creates a new Button.
	 * 
	 * @param s
	 *            String to display in Button
	 * @param x
	 *            x_pos to draw at
	 * @param y
	 *            y_pos to draw at
	 */
	public Button(String s, int x, int y) {
		enabled = true;
		clicked = false;
		c1 = Color.black;
		c2 = Color.white;
		font = Color.black;
		bounds = new Polygon();
		text = s;
		x_pos = x;
		y_pos = y;
	}

	/**
	 * Draws the Button using the current color scheme.
	 * 
	 * @param g
	 *            Graphics to draw with
	 */
	public void draw(Graphics g) {
		Color temp = g.getColor();
		Font tempF = g.getFont();

		g.setFont(f);

		// Autosizes width and height of Button to fit text
		FontMetrics fm = g.getFontMetrics(f);
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);
		width = (int) rect.getWidth() + 20;
		height = (int) rect.getHeight();

		// Calculates the outside of the Button
		Polygon outside = new Polygon();

		outside.addPoint(x_pos - 1, y_pos - 1);
		outside.addPoint(x_pos - 21, y_pos + ((height) / 2));
		outside.addPoint(x_pos - 1, y_pos + height + 1);

		outside.addPoint(x_pos + width + 1, y_pos + height + 1);
		outside.addPoint(x_pos + width + 21, y_pos + ((height) / 2));
		outside.addPoint(x_pos + width + 1, y_pos - 1);

		bounds = outside;

		// Draws the outside section of the Button
		g.setColor(c1);
		g.fillPolygon(outside);

		g.setColor(c2);
		if (!enabled) // If a Button can not be clicked, it is colored
						// differently
			g.setColor(c2.brighter().brighter().brighter());

		// Calculates inner wings
		Polygon inside = new Polygon();

		inside.addPoint(x_pos, y_pos);
		inside.addPoint(x_pos - 19, y_pos + ((height) / 2));
		inside.addPoint(x_pos, y_pos + height);

		inside.addPoint(x_pos + width, y_pos);
		inside.addPoint(x_pos + width + 19, y_pos + ((height) / 2));
		inside.addPoint(x_pos + width, y_pos + height);

		// Draws inner section of the Button
		g.fillPolygon(inside);
		g.fillRect(x_pos, y_pos, width, height);
		g.setColor(font);
		g.drawString(text, x_pos + 10, y_pos + 23);

		// Resets graphics to whatever Color it was originally
		g.setColor(temp);
		g.setFont(tempF);
	}

	/**
	 * Returns true if Button was clicked; else, returns false. If it is true,
	 * it sets it to false immediately. One-use method.
	 * 
	 * @return clicked
	 */
	public boolean isClicked() {
		if (clicked) {
			clicked = false;
			return true;
		}
		return clicked;
	}

	/**
	 * Checks to see if mouse is hovering over Button.
	 * 
	 * @param x
	 *            X position of mouse
	 * @param y
	 *            Y position of mouse
	 * @return True if mouse is over Button
	 */
	public boolean checkOver(int x, int y) {
		if (bounds.contains(new Point(x, y)))
			return true;
		else
			return false;
	}

	/**
	 * Checks mouseclick against Button location.
	 * 
	 * @param mx
	 *            Mouseclick x_pos
	 * @param my
	 *            Mouseclick y_pos
	 */
	public void checkClick(int mx, int my) {
		if (bounds.contains(new Point(mx, my)))
			clicked = true;
	}

	/**
	 * A helper method that checks to see if a Button is clicked, given a list
	 * of Buttons
	 * 
	 * @param buts
	 *            List to check
	 * @return True if at least one Button is clicked
	 */
	public static boolean isOneClicked(List<Button> buts) {
		try {
			for (Button but : buts) {
				// Intentionally does not use the .isClicked method to avoid
				// changing Button clicked state
				if (but.clicked)
					return true;
			}
			return false;
		} catch (java.lang.NullPointerException e) {
			return false;
		}
	}

	/**
	 * Sets the enabled status of the Button
	 * 
	 * @param enable
	 *            boolean to set to
	 */
	public void setEnabled(boolean enable) {
		if (!enable)
			clicked = false;
		enabled = enable;
	}

	/**
	 * Returns x_pos of Button.
	 * 
	 * @return x_pos
	 */
	public int getX() {
		return x_pos;
	}

	/**
	 * Returns y_pos of Button.
	 * 
	 * @return y_pos
	 */
	public int getY() {
		return y_pos;
	}

	/**
	 * Returns width of Button.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns height of Button.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the text displayed in the button
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets top-left corner coordinates of Button.
	 * 
	 * @param x
	 *            x_pos
	 * @param y
	 *            y_pos
	 */
	public void set(int x, int y) {
		x_pos = x;
		y_pos = y;
	}

	/**
	 * Sets the color scheme used to draw the Button.
	 * 
	 * @param newC1
	 *            Color to use as outline around Button
	 * @param newC2
	 *            Color to fill Button with
	 * @param newFont
	 *            Color to draw text with
	 */
	public void setColorScheme(Color newC1, Color newC2, Color newFont) {
		c1 = newC1;
		c2 = newC2;
		font = newFont;
	}

	/**
	 * Sets the string inside the button
	 * 
	 * @param s
	 *            text for the button
	 */
	public void setString(String s) {
		text = s;
	}

	/**
	 * Copies the current color scheme of this Button to another Button.
	 * 
	 * @param other
	 *            Button to transfer color scheme to
	 */
	public void copyColorSchemeTo(Button other) {
		other.setColorScheme(c1, c2, font);
	}
}
