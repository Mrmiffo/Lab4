package Memory;


import java.awt.Color;
import javax.swing.*;

public class Kort extends JButton{

	private Icon storedIcon;
	public enum Status {
		SYNLIGT,
		DOLT,
		SAKNAS
	};
	Status cardStatus;
	
	public Kort (Icon img){
		storedIcon = img;
		this.setStatus(Status.DOLT);
		
	}
	public Kort (Icon img, Status status){
		storedIcon = img;
		this.setStatus(status);
	}
	public Kort copy(){
		return new Kort(this.storedIcon, this.getStatus());
		
	}
	public void setStatus(Status status){
		if 	(status == Status.SYNLIGT){
			cardStatus = status;
			super.setOpaque(true);
			super.setBackground(Color.BLUE);
			super.setIcon(storedIcon);
		} else if (status == Status.DOLT){
			cardStatus = status;
			super.setIcon(null);
			super.setOpaque(true);
			super.setBackground(Color.BLUE);
		} else if (status == Status.SAKNAS){
			cardStatus = status;
			super.setIcon(null);
			super.setOpaque(true);
			super.setBackground(Color.WHITE);
		} else System.out.println("Derp 1");
	}
	public Status getStatus(){
		return cardStatus;
	}
	public boolean sammaBild(Kort card){
		return this.storedIcon == card.storedIcon;
		//return this.getIcon() == card.getIcon();
	}
	public Icon getTempIcon(){
		return storedIcon;
	}
}
