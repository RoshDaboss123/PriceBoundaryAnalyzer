package stockmarketalgorithm;
/**
 * @author roshd
 */
import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class StandardDeviations {
    public static File file = new File("C:\\Users\\roshd\\OneDrive\\Documents\\Stocks.txt");
    public static int count = 0;
    static ArrayList <double []> ar = new ArrayList<>();
    static String tag="";
    public static File ff = new File("vv.out"); 
    public static File ex = new File("fx.out");
    public static double SDbeta = 0;
    public static double SDpeRatio = 0;
    public static ArrayList <String> tags = new ArrayList<>();
    public static ArrayList <String> indtags = new ArrayList<>();
    public static double SDeps = 0;
    public static double averageP = 0;
    public static double averageE = 0;
    public static double averageB = 0;
    StandardDeviations (String category)throws IOException{
        String url = "https://finance.yahoo.com/screener/predefined/ms_"+category+"/?offset=0&count=100";
                    
        try{
            Document doc = Jsoup.connect(url).get();
            PrintWriter pws = new PrintWriter(ex);
            pws.print(doc.outerHtml());
            pws.close();
            Scanner pwss = new Scanner(ex);
            while(pwss.hasNextLine()){
                String stm = pwss.nextLine();
                
                for(int i = 0;i<stm.length()-6;i++){
                    if(stm.substring(i,i+6).equalsIgnoreCase("</svg>")){
                        tags.add(stm);
                    }
                }
            }
            for(int i = 0;i<tags.size();i++){
                 String stm = tags.get(i);
                 int init1 = 0;
                 int init2 = 0;
                 for(int j = 0;j<stm.length()-13;j++){
                     if(stm.substring(j,j+13).equals("C($linkColor)")){
                        for(int b = j+13;b<stm.length();b++){
                            if(stm.charAt(b)=='>'){
                                init1 = b+1;
                                break;
                            }
                        }
                        for(int k = j+9;k<stm.length();k++){
                            if(stm.charAt(k)=='<'){
                                init2 = k;
                                break;
                            }
                        }
                     }
                 } 
                 System.out.println(stm.substring(init1,init2));
                 indtags.add(stm.substring(init1,init2));
            }            
        }
        catch(Exception e){
            System.out.println("This algorithm doens't operate for real-estate groups");
        }
        for(int i = 0;i<indtags.size();i++){
            String name = indtags.get(i);
            tag = name;
            ar.add(CollectData(name));
        }
        System.out.println(count+" stocks analyzed");
        averageB = 0;
        averageP = 0;
        averageE = 0;
        for(int i = 0;i<count;i++){
            averageB+=ar.get(i)[0];
            averageP+=ar.get(i)[1];
            averageE+=ar.get(i)[2];
        }
        averageB = averageB/(count*1.0);
        averageP = averageP/(count*1.0);
        averageE = averageE/(count*1.0);
        System.out.println("Averages (Beta/PERatio/EPS): "+ averageB +" "+averageP+" "+averageE);
        for(int i = 0;i<count;i++){
            SDbeta += Math.pow((ar.get(i)[0]-averageB), 2.0);
            SDpeRatio += Math.pow((ar.get(i)[1]-averageP), 2.0);
            SDeps += Math.pow((ar.get(i)[2]-averageE), 2.0);
        }
        SDbeta /= count;
        SDpeRatio /= count;
        SDeps /=count;
        SDbeta = Math.sqrt(SDbeta);
        SDpeRatio = Math.sqrt(SDpeRatio);
        SDeps = Math.sqrt(SDeps);
        System.out.println("Standard Deviations (Beta/PERatio/EPS): "+SDbeta+" "+SDpeRatio+" "+SDeps);
    }
    
    public static double[] CollectData(String name) throws IOException{
        double[]ans = new double[3];
        String url = "https://finance.yahoo.com/quote/"+name+"?p="+name+"&.tsrc=fin-srch";
        try{
        String furl ="https://www.marketwatch.com/investing/stock/"+name+"?mod=over_search";
            Document doc = Jsoup.connect(furl).get();
            File myFile = new File("Html.out");
            PrintWriter pw = new PrintWriter(myFile);
            pw.println(doc.outerHtml());
            pw.close();
            ArrayList <String> v = new ArrayList<>();
            Scanner in = new Scanner(myFile);
            while(in.hasNextLine()){
                String asd = in.nextLine();
                v.add(asd);
            }
            in.close();
            String be="";
            int n = v.size();
            for(int i =0;i<n;i++){
                String find = v.get(i);
                for(int k = 0;k<find.length()-12;k++){
                    if(find.substring(k,k+4).equalsIgnoreCase("BETA")){
                        for(int j = k;j<find.length()-14;++j){
                            if(find.substring(j,j+7).equalsIgnoreCase("primary")){
                                be = find.substring(j+10,j+14);
                                break;
                            }  
                        }  
                   } 
                }
            }
            
            String pr="";
            int index = 0;
            for(int i =0;i<n;i++){
                String find = v.get(i);
                for(int k = 0;k<find.length()-8;k++){
                    if(find.substring(k,k+8).equals("lastsale")){
                        pr = v.get(i+1);
                        index = i+6;
                                break;
                   } 
                }
            }
            pr = pr.replace(",", "");
            double price = Double.valueOf(pr);
            System.out.println(price);
            
            double dollarchange = Double.valueOf(v.get(index));
            System.out.println(dollarchange);
            
            
            double beta = Double.valueOf(be);
           
           String pe="";
            for(int i =0;i<n;i++){
                String find = v.get(i);
                for(int k = 0;k<find.length()-12;k++){
                    if(find.substring(k,k+9).equalsIgnoreCase("P/E Ratio")){
                        for(int j = k;j<find.length()-14;++j){
                            if(find.substring(j,j+10).equalsIgnoreCase("primary \">")){
                                for(int l = j+10;l<find.length()-14;l++){
                                    if(find.charAt(l)!='<'){
                                        
                                        pe+=String.valueOf(find.charAt(l));
                                    }
                                    else{
                                        break;
                                    }
                                    
                                }
                                break;
                            }  
                        }  
                   } 
                }
            }
            double peRatio = Double.valueOf(pe);
            
            String eps="";
            for(int i =0;i<n;i++){
                String find = v.get(i);
                for(int k = 0;k<find.length()-12;k++){
                    if(find.substring(k,k+5).equals(">EPS<")){
                        for(int j = k;j<find.length()-14;++j){
                            if(find.substring(j,j+10).equalsIgnoreCase("primary \">")){
                                
                                for(int l = j+10;l<find.length()-14;l++){
                                    if(find.charAt(l)!='<'){
                                        
                                        eps+=String.valueOf(find.charAt(l));
                                    }
                                    else{
                                        break;
                                    }
                                    
                                }
                                break;
                            }  
                        }  
                   } 
                }
            }
            eps = eps.substring(1,eps.length());
            double earningsPerShare = Double.valueOf(eps);
            ans[0] = beta;
            ans[1] = peRatio;
            ans[2] = earningsPerShare;
            System.out.print(tag);
            System.out.print(" "+beta);
            System.out.print(" "+eps);
            System.out.print(" "+peRatio);
            System.out.println();
            
            count++;
            }
        catch(Exception e){
            System.out.println(tag+" Algorithm missing data from stock");
        }
        return ans;
    }
    public double[] getSD(){
        double a[] = {SDbeta,SDpeRatio,SDeps};
        return a;
    }
    public double[] getAverage(){
        double a[] = {averageB, averageP, averageE};
        return a;
    }
}
