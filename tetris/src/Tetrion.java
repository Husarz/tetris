import java.io.Serializable;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;

public class Tetrion implements Serializable {
	int Score;
	int intLine;         // ilosc lini zredukawanych;
	int X, Y;
	static int Speed;    // prêdkoœæ, nie trzeba static
	boolean End;		 // jeszeli true tz. ze gra skonczona 
	Matrix matrix;
	
	static short which, after, yourfigur = 0;
	Matrix.Figura NextFigure[];

	private PropertyChangeSupport propertyChange = 
		new PropertyChangeSupport(this);

	public Tetrion() {
		super();
		intLine = 0;      // ilosc lini zredukawanych;
		Score = 0;
		X=10; Y=20;       // wymiary tetrionu.
		Speed = 400;	  // prêkoœæ domyœlna.
		End = false;
		matrix = new Matrix();
		NextFigure = new Matrix.Figura[2];
		NextFigure[yourfigur] = RandomFigure();
	}
	/**
	 * Klasa Kom reprezentujê pojedyñcz¹ komórkê in. cegie³ke.
	 */
	class Kom {
		private short C;
		Kom Left, Right, Down; // Left , Right , Downown
		Kom(){
			C = 7;
			Left = Right = Down = null;
		}
		public void setC(short i){
			C = i;
		}
		
		public short getC(){
			return C;
		}
		boolean inFigure(){
			for(int i=0; i<matrix.actionObject.length; i++){
				for(int j=0; j<4; j++){
					if(this==matrix.actionObject[i].w[j])
						return true;
				}
			}
			return false;
		}
	}

	/**
	 * Klasa Matrix reprezentuje uk³aD komórek. 
	 * Dwuwymiarowa tablica typu Kom. 
	 */
	class Matrix {
		Kom [][] kom;
		
		Class actionClass[];   // klasa obs³ugi klocka
		Figura actionObject[]; // obiekt obs³ugi

		boolean fall;
		
		Matrix() {
			actionClass = new Class[2];
			actionObject = new Figura[2];
			
			
			Kom tmp = null;
			kom = new Kom[Y][X];
			for (int i=0; i<Y; i++){
				for (int j=0; j<X; j++){
					kom[i][j] = new Kom();
					if(j>0){
						kom[i][j].Left = kom[i][j-1];
						kom[i][j-1].Right = kom[i][j];
					}
					if(i>0){
						kom[i-1][j].Down = kom[i][j];
					}
				}
			}
			fall = false;
		}

		abstract class Figura{
			short c;
			Kom [] w;
			short p; // zmienna mieszcz¹ca info. o obrocie klocka. {0,...,3}
			abstract public boolean fall(String s);	
		}
		
		class RSnake extends Figura{	//0
			public RSnake() {
				super();
				p=0; c=0;;
				w = new Kom[4];
				w[0] = kom[2][4];
				w[1] = kom[1][4];
				w[2] = kom[1][5];
				w[3] = kom[0][5];
			}
			public boolean fall(String s){
				if(!fall) return false;
				if(s.equals("rotate")){
					try{
						switch(p){
							case 0:	if(w[3].Left.getC()!=7) return false;
								    if(w[2].Right.getC()!=7) return false;
									w[1].setC((short)7); w[0].setC((short)7);
									w[0]=w[3].Left; w[1]=w[2]; w[2]=w[3]; w[3]=w[1].Right;
									w[3].setC((short)0); w[0].setC((short)0); 
									p=1; return false;
							case 1: if(w[1].Left.getC()!=7) return false;
								 	if(w[1].Left.Down.getC()!=7) return false;
								 	w[0].setC((short)7); w[3].setC((short)7);
							     	w[1]=w[1].Left; w[0]=w[1].Down; w[3]=w[2]; w[2]=w[2].Down; 
							     	w[0].setC((short)0); w[1].setC((short)0);
							     	p=0; return false;
						}
					}catch( Exception e){
						return false;
					}
				}
				else{
					if(!CanMove(w,s)) return false;
				}
				return true;
			}
		}
		class LSnake extends Figura{	//1
			public LSnake() {
				super();
				p = 0; c=1;
				w = new Kom[4];
				w[0] = kom[1][4];
				w[1] = kom[0][4];
				w[2] = kom[2][5];
				w[3] = kom[1][5];
			}
			public boolean fall(String s){
				if(!fall) return false;
				if(s.equals("rotate")){
					try{
						switch(p){
							case 0:	if(w[1].Right.Right.getC()!=7) return false;
								    if(w[1].Right.getC()!=7) return false;
									w[1].setC((short)7); w[2].setC((short)7);
									w[2]=w[1].Right; w[1]=w[3]; w[3]=w[2].Right;
									w[2].setC((short)1); w[3].setC((short)1); 
									p=1; return false;
							case 1: if(w[1].Down.getC()!=7) return false;
								 	if(w[2].Left.getC()!=7) return false;
								 	w[2].setC((short)7); w[3].setC((short)7);
							     	w[1]=w[2].Left; w[3]=w[0].Right; w[2]=w[3].Down; 
							     	w[1].setC((short)1); w[2].setC((short)1);
							     	p=0; return false;
						}
					}catch( Exception e){
						return false;
					}
				}
				else{
					if(!CanMove(w,s)) return false;
				}
				return true;
			}
		}
		class Line extends Figura{		//2
			public Line() {
				super();
				p = 0; c = 2;
				w = new Kom[4];
				w[0] = kom[3][4];
				w[1] = kom[2][4];
				w[2] = kom[1][4];
				w[3] = kom[0][4];
			}
			
			public boolean fall(String s){
				if(!fall) return false;
				if(s.equals("rotate")){
					try{
						switch(p){
							case 0:	if(w[2].Left.Left.getC()!=7) return false;
								    if(w[2].Left.getC()!=7) return false;
								    if(w[2].Right.getC()!=7) return false;
									w[0].setC((short)7); w[1].setC((short)7); w[3].setC((short)7);
									w[1]=w[2].Left; w[3]=w[2].Right; w[0]=w[1].Left;
									w[0].setC((short)2); w[1].setC((short)2); w[3].setC((short)2); 
									p=1; return false;
							case 1: if(w[2].Down.Down.Down.getC()!=7) return false;
						    		if(w[2].Down.Down.getC()!=7) return false;
						    		if(w[2].Down.getC()!=7) return false;
						    		w[0].setC((short)7); w[1].setC((short)7); w[3].setC((short)7);
							     	w[3]=w[2]; w[2]=w[3].Down; w[1]=w[2].Down; w[0]=w[1].Down;
							     	w[0].setC((short)2); w[1].setC((short)2); w[2].setC((short)2);
							     	p=0; return false;
						}
					}catch( Exception e){
						return false;
					}
				}
				else{
					if(!CanMove(w,s)) return false;
				}
				return true;
			}
		}
		class Piramid extends Figura{	//3
			public Piramid() {
				super();
				p = 0; c = 3;
				w = new Kom[4];
				w[0] = kom[1][3];
				w[1] = kom[1][4];
				w[2] = kom[0][4];
				w[3] = kom[1][5];
			}
			public boolean fall(String s){
				if(!fall) return false;
				if(s.equals("rotate")){
					try{
						switch(p){
							case 0:	if(w[1].Down.getC()!=7) return false;
									w[0].setC((short)7);
									w[0]=w[1].Down;
									w[0].setC((short)3); 
									p=1; return false;
							case 1: if(w[2].Right.getC()!=7) return false;
								 	if(w[2].Left.getC()!=7) return false;
								 	w[0].setC((short)7); w[3].setC((short)7);
							     	w[0]=w[2].Left; w[3]=w[2].Right;
							     	w[0].setC((short)3); w[3].setC((short)3);
							     	p=2; return false;
							case 2: if(w[0].Down.getC()!=7) return false;
								 	if(w[1].Down.getC()!=7) return false;
								 	w[0].setC((short)7); w[3].setC((short)7);
							     	w[0]=w[0].Down; w[3]=w[2]; w[2]=w[1]; w[1]=w[1].Down;
							     	w[0].setC((short)3); w[1].setC((short)3);
							     	p=3; return false;
							case 3: if(w[2].Right.getC()!=7) return false;
								 	w[1].setC((short)7);
							     	w[1]=w[2]; w[2]=w[3]; w[3]=w[1].Right;
							     	w[3].setC((short)3);
							     	p=0; return false;
						}
					}catch( Exception e){
						return false;
					}
				}
				else{
					if(!CanMove(w,s)) return false;
				}
				return true;
			}
		}
		class LLike extends Figura{		//4
			public LLike() {
				super();
				p = 0; c = 4;
				w = new Kom[4];
				w[0] = kom[2][4];
				w[1] = kom[1][4];
				w[2] = kom[0][4];
				w[3] = kom[2][5];
			}
			public boolean fall(String s){
				if(!fall) return false;
				if(s.equals("rotate")){
					try{
						switch(p){
						case 0:	if(w[1].Left.getC()!=7) return false;
								if(w[2].Left.getC()!=7) return false;
								if(w[2].Right.getC()!=7) return false;
								w[0].setC((short)7); w[1].setC((short)7); w[3].setC((short)7);
								w[0]=w[1].Left; w[1]=w[2].Left; w[3]=w[2].Right; 
								w[0].setC((short)4); w[1].setC((short)4); w[3].setC((short)4);
								p=1; return false;
						case 1: if(w[2].Down.Down.getC()!=7) return false;
								if(w[2].Down.getC()!=7) return false;
								w[0].setC((short)7); w[3].setC((short)7);
								w[0]=w[1]; w[3]=w[2]; w[2]=w[2].Down; w[1]=w[2].Down; 
								w[2].setC((short)4); w[1].setC((short)4);
								p=2; return false;
						case 2: if(w[0].Down.getC()!=7) return false;
							 	if(w[3].Right.getC()!=7) return false;
							 	if(w[2].Right.getC()!=7) return false;
							 	w[0].setC((short)7); w[3].setC((short)7);  w[1].setC((short)7);
						     	w[0]=w[0].Down; w[1]=w[2]; w[2]=w[1].Right; w[3]=w[3].Right;
						     	w[0].setC((short)4); w[2].setC((short)4);  w[3].setC((short)4);
						     	p=3; return false;
						case 3: if(w[0].Down.getC()!=7) return false;
							 	if(w[1].Down.getC()!=7) return false;
							 	if(w[2].Down.getC()!=7) return false;
							 	w[0].setC((short)7); w[2].setC((short)7);  w[3].setC((short)7);
						     	w[0]=w[1].Down; w[2]=w[3].Left; w[3]=w[0].Right;
						     	w[0].setC((short)4); w[2].setC((short)4);  w[3].setC((short)4);
						     	p=0; return false;
					}
					}catch( Exception e){
						return false;
					}
				}
				else{
					if(!CanMove(w,s)) return false;
				}
				return true;
			}
		}
		class RLike extends Figura{		//5
			public RLike() {
				super();
				p = 0; c=5;
				w = new Kom[4];
				w[0] = kom[2][4];
				w[1] = kom[2][5];
				w[2] = kom[1][5];
				w[3] = kom[0][5];
			}
			public boolean fall(String s){
				if(!fall) return false;
				if(s.equals("rotate")){
					try{
						switch(p){
						case 0:	if(w[3].Left.getC()!=7) return false;
								if(w[2].Left.getC()!=7) return false;
								if(w[2].Right.getC()!=7) return false;
								w[0].setC((short)7); w[1].setC((short)7); w[3].setC((short)7);
								w[0]=w[2].Left; w[1]=w[3].Left; w[3]=w[2].Right; 
								w[0].setC((short)5); w[1].setC((short)5); w[3].setC((short)5);
								p=1; return false;
						case 1: if(w[0].Down.getC()!=7) return false;
								if(w[1].Right.getC()!=7) return false;
								w[2].setC((short)7); w[3].setC((short)7);
								w[0]=w[0].Down; w[2]=w[1]; w[1]=w[2].Down; w[3]=w[2].Right;
								w[0].setC((short)5); w[3].setC((short)5);
								p=2; return false;
						case 2: if(w[2].Left.getC()!=7) return false;
							 	if(w[1].Right.getC()!=7) return false;
							 	w[0].setC((short)7); w[1].setC((short)7);
						     	w[0]=w[2].Left; w[1]=w[2]; w[2]=w[3].Down;
						     	w[0].setC((short)5); w[2].setC((short)5);
						     	p=3; return false;
						case 3: if(w[2].Down.getC()!=7) return false;
							 	if(w[1].Down.Down.getC()!=7) return false;
							 	w[0].setC((short)7); w[1].setC((short)7);
						     	w[1]=w[2].Down; w[0]=w[1].Left;
						     	w[0].setC((short)5); w[1].setC((short)5);
						     	p=0; return false;
					}
					}catch( Exception e){
						return false;
					}
				}
				else{
					if(!CanMove(w,s)) return false;
				}
				return true;
			}
		}
		class Square extends Figura{	//6
			public Square() {
				super();
				c=6;
				w = new Kom[4];
				w[0] = kom[1][4];
				w[1] = kom[1][5];
				w[2] = kom[0][4];
				w[3] = kom[0][5];
			}
			public boolean fall(String s){
				if(!fall) return false;
				if(!CanMove(w,s)) return false;
				return true;
			}
		}


		protected boolean CanMove(Object [] w, String s){
			
			Field fild;
			int i,j;
			try{
				boolean b = true;
				for(i=0;i<4;i++){
					fild = w[i].getClass().getDeclaredField(s);
	
					for(j=0;j<4;j++){
						if(i!=j){

							if(fild.get(w[i])==w[j]){
								b=false;
								break;
							}
						}
					}
					if(b && ((Kom) fild.get(w[i])).getC()!=7){
						if(s.equals("Down")){
							if(!((Kom) fild.get(w[i])).inFigure()){
								fall = false;
								after = which;
							}
						}
						return false;
					}
					b=true;
				}
			}catch(Exception e){
				if(s.equals("Down")){
					fall = false;
					after = which;
				}
				return false;
			}
			if(s.equals("Down"))
				Down(w);
			if(s.equals("Left"))
				Left(w);
			if(s.equals("Right"))
				Right(w);
			return true;
		}
	
		/**
		 * przesuniêcie w du³ o jedno pole.
		 */
		protected void Down(Object [] p){
			Kom [] w = (Kom [])p;
			for(int i=0;i<4;i++){
				w[i].Down.setC(w[i].getC()); // inicjuje wartoœæ nastêpnej komórki
				w[i].setC((short) 7);        // wartoœæ danej komorki zmieniana jest na pusta
				w[i] = w[i].Down;			 // teraz referencja wskazuje na dolna kom.
			}
		}
		/**
		 * Analogicznie jak powy¿ej tylko przesuniêcie w prawo
		 * @param p
		 */
		protected void Right(Object [] p){
			Kom [] w = (Kom [])p;
			for(int i=3;i>=0;i--){
				w[i].Right.setC(w[i].getC());
				w[i].setC((short) 7);
				w[i] = w[i].Right;
			}
		}
		/**
		 * Analogicznie jak powy¿ej tylko przesuniêcie w lewo
		 * @param p
		 */
		protected void Left(Object [] p){
			Kom [] w = (Kom [])p;
			for(int i=0;i<4;i++){
				w[i].Left.setC(w[i].getC());
				w[i].setC((short) 7);
				w[i] = w[i].Left;
			}
		}
		protected void pom(int y){
			intLine++;
			Score += Y-y;
			for(int i=y-1; i>0; i--){
				for(int j=X-1;j>=0;j--)
					kom[i][j].Down.setC(kom[i][j].getC());
			}
		}
		protected void addMatrix(Figura figur){
			if(End) return;
			for(int i=0; i<4; i++){
				if(figur.w[i].getC() != 7){
					if(!figur.w[i].inFigure())
						return;
					End = true;
				}
				figur.w[i].setC(figur.c);
			}
			Score++;
			fall = true;
		}
		public void init(Figura Figure) throws Exception{
			if(!fall){
				/**
				 * Sprawdza, czy ktorakolwiek z linii jest wypelniona
				 */
				boolean b = true;
				for(int i=Y-1; i>0; i--){
					for(int j=X-1;j>=0 ;j--)
						if (kom[i][j].getC() == 7){
							b = false;
							break;
						}
					if(b){
						pom(i);
						i++;
					}
					b = true;
				}
				actionObject[which] = Figure;
				addMatrix( (Figura)actionObject[which] );
				actionClass[which] = 
					actionObject[which].getClass();

			}
		}
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener listener){
		propertyChange.addPropertyChangeListener(listener);
	}
	
	public Matrix.Figura RandomFigure(){
		int R = new Random().nextInt(7);
		switch(R){
			case 0: return matrix.new RSnake();
			case 1: return matrix.new LSnake();
			case 2: return matrix.new Line();
			case 3: return matrix.new Piramid();
			case 4: return matrix.new LLike();
			case 5: return matrix.new RLike();
			case 6: return matrix.new Square();
		}
		return null;
	}
	
	public synchronized void Update(String arg, short which){
		try{
			if(End) return;
			if(!matrix.fall){
				if(this.after == 0){
					matrix.init(NextFigure[0]);
					NextFigure[0] = RandomFigure();
				}else{
					matrix.init(NextFigure[1]);
				}
			}
			else{
				matrix.actionObject[which].fall(arg);
			}
		} 
		catch(Exception exc) {
		  	System.out.println("Wadliwa klasa obs³ugi..." + exc);
		}
		if(!End)
			propertyChange.firePropertyChange("upDate",2,3);
		else 
			propertyChange.firePropertyChange("End",intLine,Score);
	}
	
	public short getNum(int x, int y){
		return matrix.kom[x][y].getC();
	}
	
	public short getColFigur(int x, int y){
		for(int i=0; i<4; i++)
			if(matrix.kom[x][y+2] == NextFigure[yourfigur].w[i])
				return NextFigure[yourfigur].c;
		return 7;
	}
	
	public boolean setSizeMatrix(int x, int y){
		if(!End)
			return false;
		X = x;
		Y = y;
		matrix = new Matrix();
		propertyChange.firePropertyChange("Size",2,3);
		return true;
	}
	public void TakeFigure(short i){
		switch((int)i){
			case 0: NextFigure[1]= matrix.new RSnake();
			case 1: NextFigure[1]= matrix.new LSnake();
			case 2: NextFigure[1]= matrix.new Line();
			case 3: NextFigure[1]= matrix.new Piramid();
			case 4: NextFigure[1]= matrix.new LLike();
			case 5: NextFigure[1]= matrix.new RLike();
			case 6: NextFigure[1]= matrix.new Square();
		}
	}
	public void NewGame(){
		intLine = 0;
		Score = 0;
		End = false;
		this.matrix=new Matrix();
		NextFigure[yourfigur] = (Matrix.Figura) RandomFigure();
		propertyChange.firePropertyChange("NewGame",2,3);
	}
}
