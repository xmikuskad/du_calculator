package application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
	
	int FIELD_COUNT = 4;
	
	//Zaciatocne hodnoty
	List<List<Double>> positions = new ArrayList<List<Double>>();
	List<Double> other = new ArrayList<Double>();

	//Prve pocty
	double[] rValue = new double[FIELD_COUNT];
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
			prepareForMatrices(); //OK
			calcMatrices();
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
		
		//Vypisanie vysledku
		System.out.println("::pos{"+positions.get(0).get(0)+","+positions.get(0).get(1)+","+a+","+b+","+c+"}");
	}
	
	private void prepareForMatrices()
	{
		for(int i=0; i<FIELD_COUNT;i++)
		{
			
			rValue[i] = 120000 + positions.get(i).get(4);
			aValue[i] = rValue[i] * Math.sin(Math.PI/2 - positions.get(i).get(2) * Math.PI/180) * Math.cos(positions.get(i).get(3) * Math.PI / 180);
			bValue[i] = rValue[i] * Math.sin(Math.PI/2 - positions.get(i).get(2) * Math.PI/180) * Math.sin(positions.get(i).get(3) * Math.PI / 180);
			cValue[i] = rValue[i] * Math.cos(Math.PI/2 - positions.get(i).get(2) * Math.PI/180);
			
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
			System.out.println(rValue[i]);

	}
	
	private void calcMatrices()
	{
		//Vysledky na pravej strane matic
		double res1,res2,res3;
		
		res1 = other.get(0) * other.get(0) - other.get(1) * other.get(1) + aValue[1] * aValue[1] - aValue[0] * aValue[0] + bValue[1] * bValue[1] - bValue[0] * bValue[0] + cValue[1] * cValue[1] - cValue[0] * cValue[0];  
		res2 = other.get(1) * other.get(1) - other.get(2) * other.get(2) - aValue[1] * aValue[1] - bValue[1] * bValue[1] - cValue[1] * cValue[1] + aValue[2] * aValue[2] + bValue[2] * bValue[2] + cValue[2] * cValue[2];
		res3 = other.get(2) * other.get(2) - other.get(3) * other.get(3) - aValue[2] * aValue[2] - bValue[2] * bValue[2] - cValue[2] * cValue[2] + aValue[3] * aValue[3] + bValue[3] * bValue[3] + cValue[3] * cValue[3];

				
		System.out.println("res1 = " +res1);
		System.out.println("res2 = " +res2);
		System.out.println("res3 = " +res3);
		
		double[][] deltaMatrice = new double[3][3];
		deltaMatrice[0][0] = 2*aValue[1] - 2*aValue[0];
		deltaMatrice[0][1] = 2*bValue[1] - 2*bValue[0];
		deltaMatrice[0][2] = 2*cValue[1] - 2*cValue[0];		
		deltaMatrice[1][0] = 2*aValue[2] - 2*aValue[1];
		deltaMatrice[1][1] = 2*bValue[2] - 2*bValue[1];
		deltaMatrice[1][2] = 2*cValue[2] - 2*cValue[1];
		deltaMatrice[2][0] = 2*aValue[3] - 2*aValue[2];
		deltaMatrice[2][1] = 2*bValue[3] - 2*bValue[2];
		deltaMatrice[2][2] = 2*cValue[3] - 2*cValue[2];
		
		for(int i=0; i<3;i++) {
			for (int j =0; j<3;j++)
			{
				System.out.print(deltaMatrice[i][j]+" | ");
			}
			System.out.println("");
		}
		System.out.println("----");
				
		double deltaValue = calc3x3Determinant(deltaMatrice);
		
		double[][] geneticMatrice = new double[3][3];
		geneticMatrice[0][0] = res1;
		geneticMatrice[0][1] = 2*bValue[1] - 2*bValue[0];
		geneticMatrice[0][2] = 2*cValue[1] - 2*cValue[0];		
		geneticMatrice[1][0] = res2;
		geneticMatrice[1][1] = 2*bValue[2] - 2*bValue[1];
		geneticMatrice[1][2] = 2*cValue[2] - 2*cValue[1];
		geneticMatrice[2][0] = res3;
		geneticMatrice[2][1] = 2*bValue[3] - 2*bValue[2];
		geneticMatrice[2][2] = 2*cValue[3] - 2*cValue[2];
		
		x = calc3x3Determinant(geneticMatrice) / deltaValue;
		
		for(int i=0; i<3;i++) {
			for (int j =0; j<3;j++)
			{
				System.out.print(geneticMatrice[i][j]+" | ");
			}
			System.out.println("");
		}
		System.out.println("----");
		
		geneticMatrice[0][0] = 2*aValue[1] - 2*aValue[0];
		geneticMatrice[0][1] = res1;
		geneticMatrice[0][2] = 2*cValue[1] - 2*cValue[0];		
		geneticMatrice[1][0] = 2*aValue[2] - 2*aValue[1];
		geneticMatrice[1][1] = res2;
		geneticMatrice[1][2] = 2*cValue[2] - 2*cValue[1];
		geneticMatrice[2][0] = 2*aValue[3] - 2*aValue[2];
		geneticMatrice[2][1] = res3;
		geneticMatrice[2][2] = 2*cValue[3] - 2*cValue[2];
		
		y = calc3x3Determinant(geneticMatrice) / deltaValue;
		
		for(int i=0; i<3;i++) {
			for (int j =0; j<3;j++)
			{
				System.out.print(geneticMatrice[i][j]+" | ");
			}
			System.out.println("");
		}
		System.out.println("----");
		
		geneticMatrice[0][0] = 2*aValue[1] - 2*aValue[0];
		geneticMatrice[0][1] = 2*bValue[1] - 2*bValue[0];
		geneticMatrice[0][2] = res1;	
		geneticMatrice[1][0] = 2*aValue[2] - 2*aValue[1];
		geneticMatrice[1][1] = 2*bValue[2] - 2*bValue[1];
		geneticMatrice[1][2] = res2;
		geneticMatrice[2][0] = 2*aValue[3] - 2*aValue[2];
		geneticMatrice[2][1] = 2*bValue[3] - 2*bValue[2];
		geneticMatrice[2][2] = res3;
		
		z = calc3x3Determinant(geneticMatrice) / deltaValue;
		
		for(int i=0; i<3;i++) {
			for (int j =0; j<3;j++)
			{
				System.out.print(geneticMatrice[i][j]+" | ");
			}
			System.out.println("");
		}
		System.out.println("----");
		
		
		System.out.println("X = "+x);
		System.out.println("Y = "+y);
		System.out.println("Z = "+z);
	}
	
	
	private double calc2x2Determinant(double d1,double d2,double d3,double d4)
	{		
		return d1 * d4 - d2 * d3;
	}
	
	private double calc3x3Determinant(double[][] matrice)
	{
		double res1,res2,res3;
	
		res1 = matrice[0][0] * calc2x2Determinant(matrice[1][1],matrice[1][2],matrice[2][1],matrice[2][2]);
		res2 = matrice[1][0] * calc2x2Determinant(matrice[0][1],matrice[0][2],matrice[2][1],matrice[2][2]);
		res3 = matrice[2][0] * calc2x2Determinant(matrice[0][1],matrice[0][2],matrice[1][1],matrice[1][2]);
		
		return res1-res2+res3;
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
				
		con.setResult("seems good");
	}
}
