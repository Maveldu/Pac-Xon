package imt.uvinfo.pacxon.modele;

public class Heros extends Personnage {
	private boolean isTracing = false;
	private int coordonneeSpawnX = 0;
	private int coordonneeSpawnY = -1;
	
	public Heros(Jeu jeu) {
		super(jeu);
		
		/* Configuration h�ros */
		/* TODO : possibilit� d'utiliser un fichier de config */
		apparu = true;
		largeur = 1;
		hauteur = 1;
		coordonneeSpawnX = 0;
		coordonneeSpawnY = -1; // Coordonn�e en partant du haut de l'�cran
		double vitesse = 0.01;
		iconeName = "iconeHeros";
		/* End Configuration h�ros */
		
		directionX = vitesse;
		directionY = 0.0;

		goToPlaceDepart();
	}
	
	private void goToPlaceDepart() {
		int largeurNiveau = this.jeu.getNiveauActuel().getTerrain().getLargeur();
		int hauteurNiveau = this.jeu.getNiveauActuel().getTerrain().getHauteur();
		
		posX = (coordonneeSpawnX + largeurNiveau) % largeurNiveau;
		posY = (coordonneeSpawnY + hauteurNiveau) % hauteurNiveau;
	}
	
	public void remplirTracage() {
		// TODO Algo
	}
	
	
}
