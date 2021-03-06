package imt.uvinfo.pacxon.modele;

import java.util.Random;

//import java.util.Random;

public class MonstreBackground extends Personnage {
	
	private double vitesse;
	private double coeffDirectionDepart;
	
	public MonstreBackground(Jeu jeu) {
		super(jeu);
		
		/* Configuration monstre normal */
		/* TODO : possibilité d'utiliser un fichier de config */
		apparu = true;
		largeur = 1;
		hauteur = 1;

		vitesse = 0.25;
		coeffDirectionDepart = 0.25 * Math.PI; // Angle de départ [0*pi, 2*pi[
		iconeName = "monstrebackground";
		/* End Configuration monstre normal */
		
		directionX = vitesse * Math.cos(coeffDirectionDepart);
		directionY = vitesse * Math.sin(coeffDirectionDepart);
		
		posX = 0.0;
		posY = 0.0;
	}
	
	protected void initier() {
		essayerDApparaitre();
	}
	
	protected boolean essayerDApparaitre() {
		Terrain terrain = this.jeu.getNiveauActuel().getTerrain();
		int largeurNiveau = terrain.getLargeur();
		int hauteurNiveau = terrain.getHauteur();
		
		// Position aléatoire à l'intérieur de la bordure du bas
		Random rand = new Random();
		int coordonneeSpawnX = rand.nextInt(largeurNiveau - 2) + 1;
		int coordonneeSpawnY = 0;
		
		posX = (double) ((coordonneeSpawnX + largeurNiveau) % largeurNiveau) / largeurNiveau;
		posY = (double) ((coordonneeSpawnY + hauteurNiveau) % hauteurNiveau) / hauteurNiveau;
		
		// Apparait toujours avec succes
		return true;
	}
	
	public void update(float elapsedTime) {
		Terrain terrain = this.jeu.getNiveauActuel().getTerrain();
		int largeurNiveau = terrain.getLargeur();
		int hauteurNiveau = terrain.getHauteur();
		double largeurUniteBloc = 1.0 / largeurNiveau;
		double hauteurUniteBloc = 1.0 / hauteurNiveau;
		
		double premierX, dernierX, premierY, dernierY;
		
		// On détermine les limites du personnage, en fonction de sa direction
		if(directionX < 0.0) {
			premierX = ((posX + directionX * elapsedTime) + (largeur * largeurUniteBloc));
			dernierX = (posX + directionX * elapsedTime);
		} else {
			premierX = (posX + directionX * elapsedTime);
			dernierX = ((posX + directionX * elapsedTime) + (largeur * largeurUniteBloc));
		}
		
		if(directionY < 0.0) {
			premierY = ((posY + directionY * elapsedTime) + (hauteur * hauteurUniteBloc));
			dernierY = (posY + directionY * elapsedTime);
		} else {
			premierY = (posY + directionY * elapsedTime);
			dernierY = ((posY + directionY * elapsedTime) + (hauteur * hauteurUniteBloc));
		}
		
		// Actions en fonction de la présence de blocs sur le trajet
		verifierBlocs(premierX, dernierX, premierY, dernierY);
		
		/* Le monstre avance (position incrémentée) */
		posX += directionX * elapsedTime;
		posY += directionY * elapsedTime;
		
		if(posX <= 0.0) {
			posX = 0.0;
		} else if (posX >= 1.0 - ((double)largeur / largeurNiveau)) {
			posX = 1.0 - ((double)largeur / largeurNiveau);
		}
		
		if(posY <= 0.0) {
			posY = 0.0;
		} else if (posY >= 1.0 - ((double)hauteur / hauteurNiveau)) {
			posY = 1.0 - ((double)hauteur / hauteurNiveau);
		}
	}
	
	// Actions en fonction de la présence de blocs sur le trajetAvec 
	private void verifierBlocs(double premierX, double dernierX, double premierY, double dernierY) {
		Terrain terrain = this.jeu.getNiveauActuel().getTerrain();
		int largeurNiveau = terrain.getLargeur();
		int hauteurNiveau = terrain.getHauteur();
		double largeurUniteBloc = 1.0 / largeurNiveau;
		double hauteurUniteBloc = 1.0 / hauteurNiveau;
		double maxPosX = 1.0 - ((double)largeur / largeurNiveau);
		double maxPosY = 1.0 - ((double)hauteur / hauteurNiveau);		
		
		/* Vérification de la présence de blocs */
		TypeBloc tmpBloc = null;
		double curseur;
		double incrementeValeur;
		boolean isDevieX = false;
		boolean isDevieY = false;

		// La trace a été touchée ?
		boolean traceTouche = false;
		
		curseur = premierX;
		
		// Vérifications de la présence de bloc en haut ou bas du monstre (selon la direction)
		if(directionX < 0.0) {
			incrementeValeur = -largeurUniteBloc;
			while((curseur >= (dernierX + largeurUniteBloc)) && (curseur >= (0.0))) {
				tmpBloc = terrain.getBloc(curseur, dernierY);
				// Action selon le bloc
				// Non vide : rebondit
				if( ((tmpBloc != TypeBloc.Bordure) && (tmpBloc != TypeBloc.BlocNormal))  || ( posY <= 0) || (posY >= maxPosY) ) {
					directionY = -directionY;
					isDevieY = true;
					if(tmpBloc.isTrace() && !traceTouche) {
						traceTouche = true;
						terrain.toucherTrace(curseur, dernierY);
					}
					break;
				}
				curseur += incrementeValeur;
			}
		} else {
			incrementeValeur = largeurUniteBloc;
			while((curseur <= (dernierX - largeurUniteBloc)) && (curseur <= (maxPosX))) {
				tmpBloc = terrain.getBloc(curseur, dernierY);
				// Action selon le bloc
				// Non vide : rebondit
				if( ((tmpBloc != TypeBloc.Bordure) && (tmpBloc != TypeBloc.BlocNormal))  || ( posY <= 0) || (posY >= maxPosY) ) {
					directionY = -directionY;
					isDevieY = true;
					if(tmpBloc.isTrace() && !traceTouche) {
						traceTouche = true;
						terrain.toucherTrace(curseur, dernierY);
					}
					break;
				}
				curseur += incrementeValeur;
			}
		}
		
		curseur = premierY;
		
		// Vérifications de la présence de bloc à la droite ou à la gauche du monstre (selon la direction)
		if(directionY < 0.0) {
			incrementeValeur = -hauteurUniteBloc;
			while((curseur >= (dernierY + hauteurUniteBloc)) && (curseur >= (0.0))) {
				tmpBloc = terrain.getBloc(dernierX, curseur);
				// Action selon le bloc
				// Non vide : rebondit
				if( ((tmpBloc != TypeBloc.Bordure) && (tmpBloc != TypeBloc.BlocNormal))  || ( posX <= 0) || (posX >= maxPosX) ) {
					directionX = -directionX;
					isDevieX = true;
					if(tmpBloc.isTrace() && !traceTouche) {
						traceTouche = true;
						terrain.toucherTrace(dernierX, curseur);
					}
					break;
				}
				curseur += incrementeValeur;
			}
		} else {
			incrementeValeur = hauteurUniteBloc;
			while((curseur <= (dernierY - hauteurUniteBloc)) && (curseur <= (maxPosY))) {
				tmpBloc = terrain.getBloc(dernierX, curseur);
				// Action selon le bloc
				// Non vide : rebondit
				if( ((tmpBloc != TypeBloc.Bordure) && (tmpBloc != TypeBloc.BlocNormal)) || ( posX <= 0) || ( posX >= maxPosX) ) {
					directionX = -directionX;
					isDevieX = true;
					if(tmpBloc.isTrace() && !traceTouche) {
						traceTouche = true;
						terrain.toucherTrace(dernierX, curseur);
					}
					break;
				}
				curseur += incrementeValeur;
			}
		}
		//System.out.println("maxposX"+ maxPosX );
		//System.out.println("maxposY"+ maxPosY );
		//System.out.println("curseur"+ curseur );
		/* TODO : Attention, si le monstre est dans un espace d'une case de largeur, il peut glitch */

		if(!isDevieX && !isDevieY) {
			/* Si le monstre touche directement un coin */
			tmpBloc = terrain.getBloc(dernierX, dernierY);
			if( ((tmpBloc != TypeBloc.Bordure) && (tmpBloc != TypeBloc.BlocNormal)) || ( posX <= 0) || ( posX >= maxPosX) || ( posY <= 0) || ( posY >= maxPosY) ) {
				directionX = -directionX;
				directionY = -directionY;
				if(tmpBloc.isTrace() && !traceTouche) {
					traceTouche = true;
					terrain.toucherTrace(dernierX, dernierY);
				}
			}
		}		
	}
}
