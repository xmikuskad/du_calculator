package application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
	
	int FIELD_COUNT = 3;
	
	//Zaciatocne hodnoty
	List<List<Double>> positions = new ArrayList<List<Double>>();
	List<Double> other = new ArrayList<Double>();

	//Prve pocty
	double[] sValue = new double[FIELD_COUNT];
	double[] aValue = new double[FIELD_COUNT];
	double[] bValue = new double[FIELD_COUNT];
	double[] cValue = new double[FIELD_COUNT];
		
	
	//Po matici
	double x,y,z;
	
	//Vysledky
	double a,b,c;
	
	//Hlavna funkcia programu
	public void startSolve(List<String> varList, MainScreenController con)
	{	
		try {
			checkInput(varList,con); //Skusanie, ci je vstup OK
		}
		catch(Exception e)
		{
			System.out.println("Wrong input!");
			con.setResult("Wrong input!");
			e.printStackTrace();
			return;
		}
		
		try {
			prepare(); //OK
			makeCalculations();
			finishIt();
			con.setResult("::pos{"+positions.get(0).get(0)+","+positions.get(0).get(1)+","+a+","+b+","+c+"}");
		}
		catch(Exception e)
		{
			System.out.println("Calculation problem!");
			con.setResult("Calculation problem!");
			e.printStackTrace();
			return;
		}
		
		
	}
	
	private void finishIt()
	{
		double s = Math.sqrt(x*x + y*y + z*z);
		a = 90 - Math.acos(z/s)  * 180 / Math.PI;
		b = Math.atan(y/x)* 180 / Math.PI;
		c = s - 120000;
		
		BigDecimal aa = new BigDecimal(a).setScale(4, RoundingMode.HALF_EVEN);
		a=aa.doubleValue();
		
		BigDecimal bb = new BigDecimal(b).setScale(4, RoundingMode.HALF_EVEN);
		b=bb.doubleValue();
		
		BigDecimal cc = new BigDecimal(c).setScale(4, RoundingMode.HALF_EVEN);
		c=cc.doubleValue();
		
		System.out.println("S = "+s);
		
		//Vypisanie vysledku
		System.out.println("\n::pos{"+positions.get(0).get(0)+","+positions.get(0).get(1)+","+a+","+b+","+c+"}");
	}
	
	private void prepare()
	{
		for(int i=0; i<FIELD_COUNT;i++)
		{
			
			sValue[i] = 120000 + positions.get(i).get(4);
			aValue[i] = sValue[i] * Math.sin(Math.PI/2 - positions.get(i).get(2) * Math.PI/180) * Math.cos(positions.get(i).get(3) * Math.PI / 180);
			bValue[i] = sValue[i] * Math.sin(Math.PI/2 - positions.get(i).get(2) * Math.PI/180) * Math.sin(positions.get(i).get(3) * Math.PI / 180);
			cValue[i] = sValue[i] * Math.cos(Math.PI/2 - positions.get(i).get(2) * Math.PI/180);
			
		}
		
		System.out.println("A");
		for(int i=0;i<FIELD_COUNT;i++)
			System.out.println(aValue[i]);
		
		System.out.println("B");
		for(int i=0;i<FIELD_COUNT;i++)
			System.out.println(bValue[i]);
		
		System.out.println("C");
		for(int i=0;i<FIELD_COUNT;i++)
			System.out.println(cValue[i]);
		
		System.out.println("R");
		for(int i=0;i<FIELD_COUNT;i++)
			System.out.println(sValue[i]);

	}

	private void makeCalculations()
	{
		double[] deltaA = new double[2];
		double[] deltaB = new double[2];
		double[] deltaC = new double[2];	
		double alpha,beta,gamma,x1,x2,y1,y2,z1,z2,o1,o2,p1,p2;
		double v1,v2,D1,D2, V1, V2,A1,A2,B1,C2;
		
		for(int i =0; i<2; i++)
		{
			deltaA[i] = aValue[0] - aValue[1+i]; 
			deltaB[i] = bValue[0] - bValue[1+i]; 
			deltaC[i] = cValue[0] - cValue[1+i]; 
		}	
		
		p1 = other.get(1) * other.get(1) - deltaA[0] * deltaA[0] - deltaB[0] * deltaB[0] - deltaC[0] * deltaC[0] - other.get(0) * other.get(0); 
		p2 = other.get(2) * other.get(2) - deltaA[1] * deltaA[1] - deltaB[1] * deltaB[1] - deltaC[1] * deltaC[1] - other.get(0) * other.get(0); 		
		
		A1 = 4*deltaA[0]*deltaC[1] - 4*deltaA[1]*deltaC[0];
		B1 = 4*deltaB[0]*deltaC[1] - 4*deltaB[1]*deltaC[0];
		A2 = 4*deltaA[0]*deltaB[1] - 4*deltaA[1]*deltaB[0];
		C2 = 4*deltaC[0]*deltaB[1] - 4*deltaC[1]*deltaB[0];
		D1 =-1*(-2*p1*deltaC[1] + 2*p2*deltaC[0]);
		D2 =-1*(-2*p1*deltaB[1] + 2*p2*deltaB[0]);
		
		System.out.println("A1 = "+A1);
		System.out.println("B1 = "+B1);
		System.out.println("A2 = "+A2);
		System.out.println("C2 = "+C2);
		System.out.println("D1 = "+D1);
		System.out.println("D2 = "+D2);
		
		alpha = (-1 *A1/B1)*(-1 *A1/B1) + (-1 *A2/C2) * (-1 *A2/C2) + 1;
		beta = 2*( (-A1/B1) * (D1/B1) + (-A2/C2) * (D2/C2) );
		gamma = (D1/B1) * (D1/B1) + (D2/C2) * (D2/C2) - other.get(0) * other.get(0);
		
		System.out.println("\nalpha = "+alpha);
		System.out.println("beta = "+beta);
		System.out.println("gamma = "+gamma);
		
		v1 = (-beta + Math.sqrt(beta*beta - 4 * alpha * gamma)) / (2*alpha);
		v2 = (-beta - Math.sqrt(beta*beta - 4 * alpha * gamma)) / (2*alpha);
		
		System.out.println("\nv1 = "+v1);
		System.out.println("v2 = "+v2);
		
		x1 = aValue[0] + v1;
		y1 = bValue[0] + v1 * (-A1/B1) + D1/B1;
		z1 = cValue[0] + v1 * (-A2/C2) + D2/C2;
		

		System.out.println("!! "+(v1 * (-A1/B1) + D1/B1));
		System.out.println("\nx1 = "+x1);
		System.out.println("y1 = "+y1);
		System.out.println("z1 = "+z1);
		
		x2 = aValue[0] + v2;
		y2 = bValue[0] + v2 * (-A1/B1) + D1/B1;
		z2 = cValue[0] + v2 * (-A2/C2) + D2/C2;
		
		System.out.println("\nx2 = "+x2);
		System.out.println("y2 = "+y2);
		System.out.println("z2 = "+z2);
		
		V1 = (aValue[0] -x1) * (aValue[0] -x1) + (bValue[0] - y1) * (bValue[0] - y1) + (cValue[0]-z1) * (cValue[0]-z1) +  (aValue[1] -x1) * (aValue[1] -x1) + (bValue[1] - y1) * (bValue[1] - y1) + (cValue[1]-z1) * (cValue[1]-z1) + (aValue[2] -x1) * (aValue[2] -x1) + (bValue[2] - y1) * (bValue[2] - y1) + (cValue[2]-z1) * (cValue[2]-z1);
		V2 = (aValue[0] -x2) * (aValue[0] -x2) + (bValue[0] - y2) * (bValue[0] - y2) + (cValue[0]-z2) * (cValue[0]-z2) +  (aValue[1] -x2) * (aValue[1] -x2) + (bValue[1] - y2) * (bValue[1] - y2) + (cValue[1]-z2) * (cValue[1]-z2) + (aValue[2] -x2) * (aValue[2] -x2) + (bValue[2] - y2) * (bValue[2] - y2) + (cValue[2]-z2) * (cValue[2]-z2);
		
		System.out.println("\nV1 = "+V1);
		System.out.println("V2 = "+V2);
		
		o1 = Math.sqrt(V1 - positions.get(0).get(4) - positions.get(1).get(4) - positions.get(2).get(4));
		o2 = Math.sqrt(V2 - positions.get(0).get(4) - positions.get(1).get(4) - positions.get(2).get(4));
		
		System.out.println("\no1 = "+o1);
		System.out.println("o2 = "+o2);
		
		if(o1 < o2) {
			x = x1;
			y = y1;
			z = z1;
			System.out.println("Choosing variant 1");
		} 
		else {
			x = x2;
			y = y2;
			z = z2;
			System.out.println("Choosing variant 2");
		}
	}
	
	private void checkInput(List<String> varList, MainScreenController con) throws Exception
	{
		for(int i =0;i<FIELD_COUNT;i++) {
		
			//Priprava
			positions.add(new ArrayList<Double>());
			
			//Vybratie konkretnych hodnot zo vstupu
			String pos = varList.get(i*2);
			String otherVar = varList.get(i*2 +1);
			
			//Kontrola prazdnosti
			if(otherVar.isEmpty()) {
				throw new Exception("Other field is empty!");
			}
			
			//Vybratie hodnot zo stringu
			List<String> tmp = Arrays.asList(pos.split("\\{"));
			String positionsRaw = Arrays.asList(tmp.get(1).split("\\}")).get(0);
					
			List<String> tmpList = Arrays.asList(positionsRaw.split(","));
			
			//Premena na double a ulozenie do listov
			for(String str : tmpList) {
				positions.get(i).add(Double.valueOf(str));
			}		
			other.add(Double.valueOf(otherVar));
		
		}

	}
}
