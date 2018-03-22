package imt.uvinfo.pacxon.modele;

import java.util.Random;

public class MonstreNormal extends Personnage {
	
	private double vitesse;
	private double coeffDirectionDepart;

	public MonstreNormal(Jeu jeu) {
		super(jeu);
		
		/* Configuration monstre normal */
		/* TODO : possibilit� d'utiliser un fichier de config */
		apparu = true;
		largeur = 1;
		hauteur = 1;

		vitesse = 0.5;
		coeffDirectionDepart = 0.25 * Math.PI; // Angle de d�part [0*pi, 2*pi[
		iconeName = "iconeHeros";
		/* End Configuration monstre normal */
		
		directionX = vitesse * Math.cos(coeffDirectionDepart);
		directionY = vitesse * Math.sin(coeffDirectionDepart);
		
		posX = 0.0;
		posY = 0.0;
	}
	
	protected void initier() {
		int largeurNiveau = this.jeu.getNiveauActuel().getTerrain().getLargeur();
		int hauteurNiveau = this.jeu.getNiveauActuel().getTerrain().getHauteur();
		
		Random rand = new Random();
		int coordonneeSpawnX = rand.nextInt(largeurNiveau - 1) + 1; // "-1" et "+1" pour exclure les bordures
		int coordonneeSpawnY = rand.nextInt(hauteurNiveau - 1) + 1; // "-1" et "+1" pour exclure les bordures
		
		posX = (double) ((coordonneeSpawnX + largeurNiveau) % largeurNiveau) / largeurNiveau;
		posY = (double) ((coordonneeSpawnY + hauteurNiveau) % hauteurNiveau) / hauteurNiveau;
		
		System.out.println(posX);
		System.out.println(posY);
	}
	
	public void update(float elapsedTime) {
		int largeurNiveau = this.jeu.getNiveauActuel().getTerrain().getLargeur();
		int hauteurNiveau = this.jeu.getNiveauActuel().getTerrain().getHauteur();
		double maxPosX = 1.0 - 1.0 / largeurNiveau;
		double maxPosY = 1.0 - 1.0 / hauteurNiveau;
		
		posX += directionX * elapsedTime;
		if(posX > maxPosX) {
			posX = maxPosX;
			directionX = -directionX;
		} else if(posX < 0.0) {
			posX = 0.0;
			directionX = -directionX;
		}
		posY += directionY * elapsedTime;
		if(posY > maxPosY) {
			posY = maxPosY;
			directionY = -directionY;
		} else if(posY < 0.0) {
			posY = 0.0;
			directionY = -directionY;
		}
	}
}