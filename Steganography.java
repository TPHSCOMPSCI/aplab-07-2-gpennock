import java.awt.Color;
import java.util.ArrayList;
public class Steganography {
    public static void main(String[] args) {
        Picture hall = new Picture("femaleLionAndHall.jpg");   
        // hide pictures  
        Picture hall2 = hideText(hall, "asdfasdfasdf"); 
        hall.explore();
        System.out.println(revealText(hall2)); 

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

    public static Picture showDifferentArea (Picture a, ArrayList<int[]> g){
        Picture n = new Picture (a);
        for(int i =0; i<g.size(); i++){
            n.setBasicPixel(g.get(i)[1],g.get(i)[0],75);
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


    public static ArrayList<int[]> findDifferences (Picture a, Picture b){
        Pixel[][] pa = a.getPixels2D();
        Pixel[][] pb = b.getPixels2D();
        ArrayList f = new ArrayList<int[]>();
        int[] i;
        if(canPicture(a, b)){
        for(int r=0; r<pa.length; r++){
            for(int c=0; c<pa[0].length; c++){
                if(pa[r][c].getRed()!=pb[r][c].getRed()||pa[r][c].getBlue()!=pb[r][c].getBlue()||pa[r][c].getGreen()!=pb[r][c].getGreen()){
                    f.add(i = new int[]{r,c});
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
