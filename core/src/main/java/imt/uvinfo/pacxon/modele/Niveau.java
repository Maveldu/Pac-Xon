package imt.uvinfo.pacxon.modele;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Niveau {
	
	// Terrain du niveau
	private Terrain terrain;
	
	// Entit�s
	private Heros heros;
	private ArrayList<Personnage> monstres;
	
	// Pourcentage de blocs � remplir pour gagner le niveau
	private double pourcentageObjectif;
	
	private int nbBlocsRemplis = 0;
	private int nbBlocsTotal;
	
	public Niveau(int num, int x, int y, Heros heros, ArrayList<Personnage> monstres, double pourcentObjectif) {
	
		this.heros = heros;
		
		// R�cup�ration des monstres donn�es par le Jeu
		this.monstres = new ArrayList<Personnage>();
		while(monstres.size() > 0) {
			this.monstres.add(monstres.remove(0));
		}
		
		pourcentageObjectif = pourcentObjectif;
		nbBlocsTotal = x*y;
		terrain = new Terrain(x, y);

	}
	
	// Cette m�thode doit etre execut�e une fois que le niveau est utilis�
	// Initialise les entit�s / Personnages
	protected void initier() {
		int i = 0;
		heros.initier();
		for(i = 0; i < monstres.size(); i++) {
			monstres.get(i).initier();
		}
	}
	
	// Met � jour le niveau � chaque frame
	public void update(float elapsedTime) {
		int i = 0;
		heros.update(elapsedTime);
		for(i = 0; i < monstres.size(); i++) {
			monstres.get(i).update(elapsedTime);
		}
	}
	
	// Le h�ros a finit de tracer, cette m�thode d�finit les zones � remplir et les remplit
	public void remplirTracage(int xTraceDepart, int yTraceDepart) {
		// On transforme d�j� la trace par des blocs normaux
		terrain.remplacerTracageParBloc();
		
		// Ici on replit � partir du dernier bloc de tra�age, mais il est envisageable de trouver une meilleure m�thode

		if(terrain.getBloc(xTraceDepart, yTraceDepart + 1) == TypeBloc.Vide) {
			// Le bloc de d�part est bien vide
			if(parcourirZone(xTraceDepart, yTraceDepart + 1)) {
				// La zone doit �tre remplie (ne contient pas de monstre)
				terrain.remplacerTracageParBloc();
			} else {
				// La zone ne doit pas �tre remplie
				terrain.remplacerTracageParVide();
			}
		}
		
		if(terrain.getBloc(xTraceDepart, yTraceDepart - 1) == TypeBloc.Vide) {
			if(parcourirZone(xTraceDepart, yTraceDepart - 1)) {
				terrain.remplacerTracageParBloc();
			} else {
				terrain.remplacerTracageParVide();
			}
		}
		
		if(terrain.getBloc(xTraceDepart + 1, yTraceDepart) == TypeBloc.Vide) {
			if(parcourirZone(xTraceDepart + 1, yTraceDepart)) {
				terrain.remplacerTracageParBloc();
			} else {
				terrain.remplacerTracageParVide();
			}
		}
		
		if(terrain.getBloc(xTraceDepart - 1, yTraceDepart) == TypeBloc.Vide) {
			if(parcourirZone(xTraceDepart - 1, yTraceDepart)) {
				terrain.remplacerTracageParBloc();
			} else {
				terrain.remplacerTracageParVide();
			}
		}
		
		
	}
	
	// V�rification de la pr�sence de monstre / remplissage de blocs "Vide" en blocs "Tracage"
	// Algorithme bas� sur "http://raphaello.univ-fcomte.fr/IG/Algorithme/Algorithmique.htm#remplissage"
	private boolean parcourirZone(int xDepart, int yDepart) {
		ArrayDeque<int[]> pile = new ArrayDeque<int[]>();
		int[] tmpCase, leftCase, rightCase;
		int x, y;
		
		// Le bloc de d�part contient un monstre ou n'est pas vide : pas de remplissage
		if(isMonstreSurBloc(xDepart, yDepart)) {
			return false;
		} else if(terrain.getBloc(xDepart, yDepart) != TypeBloc.Vide) {
			return false;
		}
		
		tmpCase = new int[2];
		tmpCase[0] = xDepart;
		tmpCase[1] = yDepart;
		pile.add(tmpCase);
		
		while(!pile.isEmpty()) {
			tmpCase = pile.remove();
			
			if(terrain.getBloc(tmpCase[0], tmpCase[1]) == TypeBloc.Vide) {
				// On r�cup�re les blocs les plus � gauche et le plus � droite du bloc d�pil�
				leftCase = new int[2];
				leftCase[0] = tmpCase[0];
				leftCase[1] = tmpCase[1];
				rightCase = new int[2];
				rightCase[0] = tmpCase[0];
				rightCase[1] = tmpCase[1];
				while(terrain.getBloc(leftCase[0] - 1, leftCase[1]) == TypeBloc.Vide) {
					leftCase[0]--;
				}
				while(terrain.getBloc(rightCase[0] + 1, rightCase[1]) == TypeBloc.Vide) {
					rightCase[0]++;
				}
				y = tmpCase[1];
				// Pour chaque bloc du plus � gauche au plus � droite
				for(x = leftCase[0]; x <= rightCase[0]; x++) {
					if(isMonstreSurBloc(x, y)) {
						// Il contient un monstre : pas de remplissage
						return false;
					} else {
						// Il n'en contient pas, on le met de type "trace" (temporaire)
						terrain.setBloc(TypeBloc.Trace, x, y);
					}
					// Bloc au dessus
					if(terrain.getBloc(x, y + 1) == TypeBloc.Vide) {
						if(isMonstreSurBloc(x, y + 1)) {
							// Il contient un monstre : pas de remplissage
							return false;
						}
						// On garde ce bloc dans la pile : � traiter par la suite
						tmpCase = new int[2];
						tmpCase[0] = x;
						tmpCase[1] = y + 1;
						pile.add(tmpCase);
					}
					// Bloc en dessous
					if(terrain.getBloc(x, y - 1) == TypeBloc.Vide) {
						if(isMonstreSurBloc(x, y - 1)) {
							// Il contient un monstre : pas de remplissage
							return false;
						}
						// On garde ce bloc dans la pile : � traiter par la suite
						tmpCase = new int[2];
						tmpCase[0] = x;
						tmpCase[1] = y - 1;
						pile.add(tmpCase);
					}
				}
				
			}
		}
		// L'algo est arriv� jusqu'ici : la zone n'avait pas de monstre et doit donc etre remplie
		return true;
	}
	
	// V�rifie la pr�sence d'un monstre sur un bloc
	private boolean isMonstreSurBloc(int x, int y) {
		int i = 0;
		// V�rifie pour tous les monstres
		for(i = 0; i < monstres.size(); i++) {
			if(monstres.get(i).estDansBloc(x, y)) {
				return true;
			}
		}
		return false;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public Heros getHeros() {
		return heros;
	}
	
	public Personnage getMonstre(int i) {
		return monstres.get(i);
	}
	
	public int getNbMonstres() {
		return monstres.size();
	}

	public double getPourcentageObjectif() {
		return pourcentageObjectif;
	}
	
	public double getPourcentageRempli() {
		return nbBlocsRemplis/nbBlocsTotal;
	}
}
