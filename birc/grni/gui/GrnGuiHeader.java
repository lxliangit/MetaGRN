package birc.grni.gui;





import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;



public class GrnGuiHeader extends JPanel {
	
	
	private JLabel mainTitle ;
	private JLabel description ;
	
	private Color topColor ;
	private Color bottomColor;
	
	
	public GrnGuiHeader() {
		
		//LIU
//		topColor = new Color(64, 141, 6); // dark green
//		downColor = new Color(10, 53, 90); // green(light)
		topColor = new Color(148, 234, 49); // Light green
		bottomColor = new Color(66, 137, 8); // Dark green
		
		mainTitle = new JLabel("GRN Inference and Visualization");
		description = new JLabel("");
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(0, 70));
		
		mainTitle.setBorder(new EmptyBorder(10, 15, 0, 0));
		mainTitle.setForeground(Color.WHITE);
		mainTitle.setBackground(UIManager.getColor("Button.background"));
		mainTitle.setFont(new Font("Sans", Font.BOLD, 20));
		add(mainTitle, BorderLayout.NORTH);

		description.setBorder(new EmptyBorder(0, 15, 0, 0));
		description.setForeground(Color.WHITE);
		description.setFont(new Font("Sans", Font.BOLD, 14));
		add(description, BorderLayout.WEST);
	}
	
	   //paints the content of the header.
	
	 
    public void paintComponent(Graphics grap) {
        super.paintComponent(grap);
        
		Graphics2D grap2 = (Graphics2D)grap;
		Paint pt = grap2.getPaint();
		grap2.setPaint( new GradientPaint(this.getSize().width, 0, topColor, this.getSize().width, this.getSize().height, bottomColor));
		grap2.fillRoundRect(0, 0, this.getSize().width, this.getSize().height, 0, 0);
		grap2.setPaint(pt);
	}
         
     
    public void setTitle(String title) { mainTitle.setText(title); }
    public void setInfo(String info) { description.setText(info); }
    public void setTopColor(Color color) { topColor = color; }
    public void setBottomColor(Color color) { bottomColor= color; }
    
    
    
    
}


