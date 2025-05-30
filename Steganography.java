import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
public class Steganography {
    public static void main(String[] args) {
        //first part
        Picture beach2 = new Picture ("beach.jpg");  
        beach2.explore();  
        Picture copy2 = testSetLow(beach2, Color.PINK);  
        copy2.explore();  
        Picture copy3 = revealPicture(copy2);  
        copy3.explore();  

        //hide picture
        Picture beach = new Picture("beach.jpg");  
        Picture robot = new Picture("robot.jpg");  
        Picture flower1 = new Picture("flower1.jpg");  
        beach.explore(); 
        Picture hidden1 = hidePicture(beach, robot, 65, 208);  
        Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);  
        hidden2.explore();  
        Picture unhidden = revealPicture(hidden2);  
        unhidden.explore();  

        //find differences
        Picture arch = new Picture("arch.jpg"); 
        Picture koala = new Picture("koala.jpg") ;  
        Picture robot1 = new Picture("robot.jpg");  
        Picture arch2 =  new Picture("arch.jpg"); 
        ArrayList<Point> pointList = findDifferences(arch, arch2);  
        System.out.println("PointList after comparing two identical pictures " +  "has a size of " + pointList.size());  
        pointList = findDifferences(arch, koala);  
        System.out.println("PointList after comparing two different sized pictures " +  "has a size of " + pointList.size());  
        arch2 = hidePicture(arch, robot1, 65, 102); 
        pointList = findDifferences(arch, arch2);  
        System.out.println("Pointlist after hiding a picture has a size of "  + pointList.size());  
        arch.show();  
        arch2.show();
        
        //show different area
        Picture hall = new Picture("femaleLionAndHall.jpg");  
        Picture robot2 = new Picture("robot.jpg");  
        Picture flower2 = new Picture("flower1.jpg");    
        Picture hall2 = hidePicture(hall, robot2, 50, 300);  
        Picture hall3 = hidePicture(hall2, flower2, 115, 275);  
        hall3.explore();  
        if(!isSame(hall, hall3))  
        {  
        Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));  
        hall4.show();  
        Picture unhiddenHall3 = revealPicture(hall3);  
        unhiddenHall3.show();  
        }  


        //is same checks
        Picture swan = new Picture("swan.jpg"); 
        Picture swan2 = new Picture("swan.jpg"); 
        System.out.println("Swan and swan2 are the same: " +  
        isSame(swan, swan2)); 
        swan = testClearLow(swan); 
        System.out.println("Swan and swan2 are the same (after clearLow run on swan): "  + isSame(swan, swan2)); 


        //hide text
        Picture hall1 = new Picture("femaleLionAndHall.jpg");   
        Picture hall5 = hideText(hall, "asdfasdfasdf"); 
        hall.explore();
        System.out.println(revealText(hall5)); 

    }
    public static void clearLow(Pixel p){
        p.setRed((p.getRed()/4)*4);
        p.setGreen((p.getGreen()/4)*4);
        p.setBlue((p.getBlue()/4)*4);
    } 

    public static boolean canPicture (Picture a , Picture b){
        if(a.getWidth()>=b.getWidth()&&a.getHeight()>=b.getHeight()){
            return true;
        }else{
            return false;
        }
    }

    public static Picture showDifferentArea (Picture a, ArrayList<Point> g){
        Picture n = new Picture (a);
        Pixel[][] p = n.getPixels2D();
        int maxr = 0;
        int minr = a.getHeight(); 
        int minc = a.getWidth();
        int maxc = 0;
        for(Point x : g){
            if (x.getY() < minr){
                minr = (int) x.getY();
            }
            if(x.getY() > maxr){
                maxr = (int) x.getY();
            }
            if (x.getX() < minc){
                minc = (int) x.getX();
            }
            if(x.getX() > maxc){
                maxc = (int) x.getX();
            }
        }

        for(int r = minr; r < maxr; r++){
            p[minc][r].setColor(Color.RED);
            p[maxc][r].setColor(Color.RED);
        }
        for(int c = minc; c < maxc; c++){
            p[c][minr].setColor(Color.RED);
            p[c][maxr].setColor(Color.RED);
        }
        return n;
    }

    public static boolean isSame (Picture a , Picture b){
    Pixel[][] pa = a.getPixels2D();
    Pixel[][] pb = b.getPixels2D();
    for(int r=0; r<pa.length; r++){
        for(int c=0; c<pa[0].length; c++){
            if(pa[r][c].getRed()!=pb[r][c].getRed()||pa[r][c].getBlue()!=pb[r][c].getBlue()||pa[r][c].getGreen()!=pb[r][c].getGreen()){
                
                return false;
            }
        }
    }    
    return true;
    }


    private static int[] getBitPairs(int num) 
    { 
    int[] bits = new int[3]; 
    int code = num; 
    for (int i = 0; i < 3; i++) 
    { 
    bits[i] = code % 4; 
    code = code / 4; 
    }  
    return bits; 
    } 
    

    public static String decodeString(ArrayList<Integer> codes) 
    { 
    String result="";  
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
    for (int i=0; i < codes.size(); i++)  
    {  
     if (codes.get(i) == 27)  
    {  
     result = result + " "; 
     } 
     else  
    { 
     result = result + alpha.substring(codes.get(i)-1,codes.get(i));  
    } 
     } 
     return result;  
    }  
    

    public static ArrayList<Integer> encodeString(String s)  
    {  
        s = s.toUpperCase();  
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        ArrayList<Integer> result = new ArrayList<Integer>(); 
        for (int i = 0; i < s.length(); i++)  
        {  
            if (s.substring(i,i+1).equals(" "))  
            {  
                result.add(27);  
            }  
            else  
            { 
                result.add(alpha.indexOf(s.substring(i,i+1))+1); 
            }  
        } 
        result.add(0); 
        return result;  
    }  


    public static ArrayList<Point> findDifferences (Picture a, Picture b){
        Pixel[][] pa = a.getPixels2D();
        Pixel[][] pb = b.getPixels2D();
        ArrayList<Point> f = new ArrayList<Point>();
        if(canPicture(a, b)){
        for(int r=0; r<pa.length; r++){
            for(int c=0; c<pa[0].length; c++){
                if(pa[r][c].getRed()!=pb[r][c].getRed()||pa[r][c].getBlue()!=pb[r][c].getBlue()||pa[r][c].getGreen()!=pb[r][c].getGreen()){
                         f.add(new Point(r,c));
                }
            }
        }    
    }
        return f;
    }

    public static Picture hidePicture (Picture a, Picture b, int SR, int SC){
        Picture hidden = new Picture(a);
        Pixel[][] pa = hidden.getPixels2D();
        Pixel[][] pb = b.getPixels2D();

        for(int sr=SR, br = 0; sr<pa.length && br < pb.length; sr++, br++){
            for(int sc=SC, bc = 0; sc<pa[0].length && bc < pb[0].length; sc++, bc++){
                setLow(pa[sr][sc], pb[br][bc].getColor());
            }
        }
            return hidden;
    }

    public static Picture hideText(Picture source, String s){
        Picture hidden = new Picture(source);
        Pixel[][] pa = hidden.getPixels2D();
        int i = 0;

        int[] h;
        ArrayList<Integer> ss=new ArrayList<Integer>(encodeString(s));
        for(int r=0; r<pa.length && i < ss.size(); r++){
            for(int c=0; c<pa[0].length && i < ss.size(); c++){
                h=getBitPairs(ss.get(i));
                pa[r][c].setBlue((pa[r][c].getBlue()/4)*4+h[0]%4);  
                pa[r][c].setRed((pa[r][c].getRed()/4)*4+h[1]%4);  
                pa[r][c].setGreen((pa[r][c].getGreen()/4)*4+h[2]%4);  
                if(i<ss.size()){
                i++;
                }
            }
        }
        return hidden;
        
    }

    public static String revealText(Picture source) {
        Integer ii = 0;
        ArrayList <Integer> gg = new ArrayList<Integer>();
        Picture copy = new Picture(source);  
        Pixel[][] pixels = copy.getPixels2D();  

        for (int r = 0; r < pixels.length; r++)  {  
            for (int c = 0; c < pixels[0].length; c++ ){  
                Pixel p = pixels[r][c];  
                ii = ((p.getBlue() % 4))+((p.getRed() % 4)*4)+((p.getGreen() % 4)*16);
                if(ii == 0)
                    return decodeString(gg);
                gg.add(ii);
        
            }  
        }  
        return decodeString(gg);
    }


    /** 
* Set the lower 2 bits in a pixel to the highest 2 bits in c 
*/ 
    public static void setLow (Pixel p, Color c) { 
        p.setRed(((p.getRed()/4)*4+c.getRed()/64));
        p.setBlue(((p.getBlue()/4)*4+c.getBlue()/64));
        p.setGreen(((p.getGreen()/4)*4+c.getGreen()/64));
    } 

    public static Picture testClearLow(Picture p){
        Pixel[][] pixelArray = p.getPixels2D();
        for(int r = 0; r < pixelArray.length; r++){
            for(int c = 0; c<pixelArray[0].length; c++){
                clearLow(pixelArray[r][c]);
            }
        }
        return p;
    }

    /** 
 * Sets the highest two bits of each pixel’s colors 
 * to the lowest two bits of each pixel’s color o s  
*/  
    public static Picture revealPicture(Picture hidden)  {  
        Picture copy = new Picture(hidden);  
        Pixel[][] pixels = copy.getPixels2D();  
        Pixel[][] source = hidden.getPixels2D();  
        for (int r = 0; r < pixels.length; r++)  {  
            for (int c = 0; c < pixels[0].length; c++ ){  
                Color col = source[r][c].getColor();
                Pixel p = pixels[r][c];  
                p.setRed(col.getRed() % 4 * 64);
                p.setBlue(col.getBlue() % 4 * 64);
                p.setGreen(col.getGreen() % 4 * 64);
            }  
        }  
        return copy; 
    }

    public static Picture testSetLow(Picture p, Color co){
        Pixel[][] pixelArray = p.getPixels2D();
        for(int r = 0; r < pixelArray.length; r++){
            for(int c = 0; c<pixelArray[0].length; c++){
                setLow(pixelArray[r][c], co);
            }
        }
        return p;
    }

}
