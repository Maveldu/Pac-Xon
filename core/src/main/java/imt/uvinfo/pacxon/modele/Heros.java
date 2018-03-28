package imt.uvinfo.pacxon.modele;

import java.util.ArrayDeque;

public class Heros extends Personnage {
	private boolean isTracing = false;
	// Pile des coordonnées des blocs tracés
	private ArrayDeque<int[]> pileTrace = new ArrayDeque<int[]>();
	
	// Position de départ
	private int coordonneeSpawnX = 0;
	private int coordonneeSpawnY = -1;
	// Position précédente
	private double precedentX = 0.0;
	private double precedentY = 0.0;
	private double vitesse;
	
	// Spécification des touches appuyées
	private boolean FLAG_KEY_PRESSED_ARROW_RIGHT 	= false;
	private boolean FLAG_KEY_PRESSED_ARROW_UP 		= false;
	private boolean FLAG_KEY_PRESSED_ARROW_LEFT 	= false;
	private boolean FLAG_KEY_PRESSED_ARROW_DOWN 	= false;
	
	public Heros(Jeu jeu) {
		super(jeu);
		
		/* Configuration héros */
		/* TODO : possibilité d'utiliser un fichier de config */
		apparu = true;
		largeur = 1;
		hauteur = 1;
		coordonneeSpawnX = 0;
		coordonneeSpawnY = -1; // Coordonnée en partant du haut de l'écran
		vitesse = 0.4;
		iconeName = "iconeHeros";
		/* End Configuration héros */
		
		directionX = 0.0;
		directionY = 0.0;
		
		posX = 0.0;
		posY = 0.0;
	}

	protected void initier() {
		essayerDApparaitre();
	}
	
	protected boolean essayerDApparaitre() {
		int largeurNiveau = this.jeu.getNiveauActuel().getTerrain().getLargeur();
		int hauteurNiveau = this.jeu.getNiveauActuel().getTerrain().getHauteur();
		
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
		double maxPosX = 1.0 - ((double)largeur / largeurNiveau);
		double maxPosY = 1.0 - ((double)hauteur / hauteurNiveau);
		
		double directionprecedenteX = directionX;
		double directionprecedenteY = directionY;
		
		if(!isTracing) {
			// Le héros ne trace pas, il peut aller dans n'importe quelle direction et s'arrêter
			if(FLAG_KEY_PRESSED_ARROW_RIGHT) {
				/* Changement de direction */
				if(directionX <= 0.0) {
					posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
					posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
				}
				directionX = vitesse;
				directionY = 0.0;
			} else if(FLAG_KEY_PRESSED_ARROW_UP) {
				/* Changement de direction */
				if(directionY <= 0.0) {
					posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
					posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
				}
				directionX = 0.0;
				directionY = vitesse;
			} else if(FLAG_KEY_PRESSED_ARROW_LEFT) {
				/* Changement de direction */
				if(directionX >= 0.0) {
					posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
					posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
				}
				directionX = -vitesse;
				directionY = 0.0;
			} else if(FLAG_KEY_PRESSED_ARROW_DOWN) {
				/* Changement de direction */
				if(directionY >= 0.0) {
					posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
					posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
				}
				directionX = 0.0;
				directionY = -vitesse;
			} else {
				if(directionX != 0.0 || directionY != 0.0) {
					posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
					posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
				}
				// Pas de touche appuyée, le héros de fait rien
				directionX = 0.0;
				directionY = 0.0;
			}
		} else {
			// Le héros trace, il ne peut aller que sur sa droite, sa gauche, et ne peut pas s'arrêter
			if(directionX != 0.0) {
				if(!FLAG_KEY_PRESSED_ARROW_RIGHT && !FLAG_KEY_PRESSED_ARROW_LEFT) {
					if(FLAG_KEY_PRESSED_ARROW_UP) {
						posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
						posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
						directionX = 0.0;
						directionY = vitesse;
					} else if(FLAG_KEY_PRESSED_ARROW_DOWN) {
						posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
						posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
						directionX = 0.0;
						directionY = -vitesse;
					}
				}
			} else if(directionY != 0.0) {
				if(!FLAG_KEY_PRESSED_ARROW_UP && !FLAG_KEY_PRESSED_ARROW_DOWN) {
					if(FLAG_KEY_PRESSED_ARROW_RIGHT) {
						posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
						posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
						directionX = vitesse;
						directionY = 0.0;
					} else if(FLAG_KEY_PRESSED_ARROW_LEFT) {
						posX = (double) (Math.round(posX * largeurNiveau)) / largeurNiveau;
						posY = (double) (Math.round(posY * hauteurNiveau)) / hauteurNiveau;
						directionX = -vitesse;
						directionY = 0.0;
					}
				}
			} else {
				// Si le héros est arrêté : ne devrait pasarriver
				if(FLAG_KEY_PRESSED_ARROW_RIGHT) {
					directionX = vitesse;
					directionY = 0.0;
				} else if(FLAG_KEY_PRESSED_ARROW_UP) {
					directionX = 0.0;
					directionY = vitesse;
				} else if(FLAG_KEY_PRESSED_ARROW_LEFT) {
					directionX = -vitesse;
					directionY = 0.0;
				} else if(FLAG_KEY_PRESSED_ARROW_DOWN) {
					directionX = 0.0;
					directionY = -vitesse;
				}
			}
		}
		
		/* Vérification du bloc suivant */
		double blocActuelX;
		double blocSuivantX;
		// Récupération des informations relatives à la position du héros
		if(directionX > 0.0) {
			blocActuelX = (posX + directionX * elapsedTime);
			blocSuivantX = ((posX + directionX * elapsedTime) + (largeur * largeurUniteBloc));
		} else if(directionX == 0.0) {
			blocActuelX = posX;
			blocSuivantX = posX;
		} else {
			blocActuelX = ((posX + directionX * elapsedTime) + (largeur * largeurUniteBloc));
			blocSuivantX = (posX + directionX * elapsedTime);
		}
		double blocActuelY;
		double blocSuivantY;
		if(directionY > 0.0) {
			blocActuelY = (posY + directionY * elapsedTime);
			blocSuivantY = ((posY + directionY * elapsedTime) + (hauteur * hauteurUniteBloc));
		} else if(directionY == 0.0) {
			blocActuelY = posY;
			blocSuivantY = posY;
		} else {
			blocActuelY = ((posY + directionY * elapsedTime) + (hauteur * hauteurUniteBloc));
			blocSuivantY = (posY + directionY * elapsedTime);
		}
		
		TypeBloc blocActuel = terrain.getBloc(blocActuelX, blocActuelY);
		
		if(blocActuel == TypeBloc.Vide) {
			// Le bloc sur lequel est le héros est vide, son mode traçage s'active
			this.isTracing = true;
			/* En mode traçage le héros ne peut pas être arrêté */
			if(directionX == 0 && directionY == 0) {
				directionX = directionprecedenteX;
				directionY = directionprecedenteY;
			}
		}
		
		
		/* On avance et bloque aux limites du terrain */
		if(blocSuivantX < 0.0) {
			posX = 0.0;
			directionX = 0.0;
			directionY = 0.0;
		} else if(blocSuivantX >= 1.0) {
			posX = maxPosX;
			directionX = 0.0;
			directionY = 0.0;
		} else {
			posX += directionX * elapsedTime;
		}

		if(blocSuivantY < 0.0) {
			posY = 0.0;
			directionX = 0.0;
			directionY = 0.0;
		} else if(blocSuivantY >= 1.0) {
			posY = maxPosY;
			directionX = 0.0;
			directionY = 0.0;
		} else {
			posY += directionY * elapsedTime;
		}
		

		if(isTracing) {
			// Comportement si le héros est en mode Tracage
			
			if((terrain.getXint(blocActuelX) != terrain.getXint(precedentX)) || (terrain.getYint(blocActuelY) != terrain.getYint(precedentY))) {
				// Le héros entre dans un nouveau bloc
				TypeBloc blocPrecedent = terrain.getBloc(precedentX, precedentY);
				if (blocPrecedent == TypeBloc.Vide) {
					// Remplit le bloc précédent en "Trace"
					terrain.setBloc(TypeBloc.Trace, precedentX, precedentY);
					// Empile le bloc tracé
					int tmpCoords[] = {terrain.getXint(precedentX), terrain.getYint(precedentY)};
					pileTrace.add(tmpCoords);
				}
				
				if((blocActuel == TypeBloc.BlocNormal) || (blocActuel == TypeBloc.Bordure)) {
					// Il atteint un bloc rempli : il remplit le terrain et désactive son mode tracage
					jeu.getNiveauActuel().remplirTracage(pileTrace);
					pileTrace.clear();
					this.isTracing = false;
				}
				

			}
		}
		
		precedentX = blocActuelX;
		precedentY = blocActuelY;
	}
	
	public void setFlagArrowRight() {
		FLAG_KEY_PRESSED_ARROW_RIGHT = true;
	}
	
	public void unSetFlagArrowRight() {
		FLAG_KEY_PRESSED_ARROW_RIGHT = false;
	}
	
	public boolean isSetFlagArrowRight() {
		return FLAG_KEY_PRESSED_ARROW_RIGHT;
	}
	
	public void setFlagArrowUp() {
		FLAG_KEY_PRESSED_ARROW_UP = true;
	}
	
	public void unSetFlagArrowUp() {
		FLAG_KEY_PRESSED_ARROW_UP = false;
	}
	
	public boolean isSetFlagArrowUp() {
		return FLAG_KEY_PRESSED_ARROW_UP;
	}
	
	public void setFlagArrowLeft() {
		FLAG_KEY_PRESSED_ARROW_LEFT = true;
	}
	
	public void unSetFlagArrowLeft() {
		FLAG_KEY_PRESSED_ARROW_LEFT = false;
	}

	public boolean isSetFlagArrowLeft() {
		return FLAG_KEY_PRESSED_ARROW_LEFT;
	}
	
	public void setFlagArrowDown() {
		FLAG_KEY_PRESSED_ARROW_DOWN = true;
	}
	
	public void unSetFlagArrowDown() {
		FLAG_KEY_PRESSED_ARROW_DOWN = false;
	}
	
	public boolean isSetFlagArrowDown() {
		return FLAG_KEY_PRESSED_ARROW_DOWN;
	}

	
}
