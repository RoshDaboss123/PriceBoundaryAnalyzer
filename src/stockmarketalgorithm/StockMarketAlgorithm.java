package stockmarketalgorithm;

import java.io.File;
import java.io.PrintWriter;
import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.concurrent.TimeUnit;
import java. io. IOException;
/**
 * @author roshdaboss
 * @purpose To find whether a stock is good to buy or sell
 */
public class StockMarketAlgorithm {
    public static void main(String[] args)throws IOException {
        
            System.out.println("THIS ALGORITHM DOES NOT WORK FOR ANALYZING STOCKS FROM COMPANIES IN THE REAL ESTATE INDUSTRY OR FOR COMPANIES WHICH SELL RAW MATERIALS. This algorithm will not analyze companies with no found EPS, PE ratios, or Beta values.");
            System.out.println("This algorithm takes roughly 11 minutes to fully compile, all data it is reading will be printed. Time for some Snacks!");
            Scanner sc = new Scanner(System.in);
            String time = String.valueOf(LocalTime.now());
            String[] times = time.split(":");
            System.out.print("Enter the stock tag of the stock you want to purchase: ");
            String tag = sc.next();
            tag = tag.toLowerCase();
            sc.close();
            String url ="https://www.marketwatch.com/investing/stock/"+tag+"?mod=over_search";
            Document doc = Jsoup.connect(url).get();
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
                    if(find.substring(k,k+4).equalsIgnoreCase("Beta")){
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
            System.out.println("$"+price);
            
            double dollarchange = Double.valueOf(v.get(index));
            System.out.println("Todays change $"+dollarchange);
            
            
            double beta = Double.valueOf(be);
            System.out.println(beta+" beta");
           
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
            double peRatio = 0;
            peRatio = Double.valueOf(pe);
            System.out.println(peRatio+" P/E");
            
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
            System.out.println(eps+" Earnings Per Share");
            
            
            
            final String sURL = "https://finance.yahoo.com/quote/"+tag+"/profile?p=TSLA";
            Document sdoc = Jsoup.connect(sURL).get();
            File sOut = new File("sHTML.out");
            PrintWriter spw = new PrintWriter(sOut);
            spw.print(sdoc.outerHtml());
            spw.close();
            Scanner sFile = new Scanner(sOut);
            ArrayList <String> sWrite = new ArrayList<>();
            while(sFile.hasNextLine()){
                String sowow = sFile.nextLine();
                sWrite.add(sowow);
            }
            String sector1="";
            for(int i =400;i<sWrite.size();i++){
                String current = sWrite.get(i);
                for(int j=0;j<current.length()-7;j++){
                    if(current.substring(j,j+7).equalsIgnoreCase("Fw(600)")){
                        for(int k = j+9;k<current.length();k++){
                            if(current.charAt(k)=='>'||current.charAt(k)=='<'){
                                
                                break;
                            }
                            else{
                                sector1 = sector1 + String.valueOf(current.charAt(k));
                            }
                            
                        }
                        break;
                    }
                    
                }
                if(!sector1.equals("")){
                    break;
                }
            }
            System.out.println(sector1);
            sector1 = sector1.toLowerCase();
            sector1 = sector1.replace(" ", "_");
            
            

            int hour = Integer.valueOf(times[0]);
            int minute = Integer.valueOf(times[1]);
            int second = Integer.valueOf(times[2].substring(0,2));
            System.out.println(hour +" : "+minute+" "+second);

            StandardDeviations sds = new StandardDeviations(sector1);
            double [] sd = sds.getSD();
            double SDbeta = sd[0];
            double SDpeRatio = sd[1];
            double SDeps = sd[2];
            double [] averages = sds.getAverage();
            double averageB = averages[0];
            double averageP = averages[1];
            double averageE = averages[2];
            double SDB = (beta-averageB)/SDbeta;
            double SDP = (peRatio-averageP)/SDpeRatio;
            double SDE = (earningsPerShare-averageE)/SDeps;

            /*
            System.out.println("The number of Standard Deviations "+ tag+" is away from the average(Beta/PERatio/EPS): "+SDB+" "+SDP+" "+ SDE);
            final String um = "https://eresearch.fidelity.com/eresearch/markets_sectors/sectors/sectors_in_market.jhtml";
            File fum = new File("um.out");
            Document daily  = Jsoup.connect(um).get();
            PrintWriter dpw = new PrintWriter(fum);
            dpw.print(daily.outerHtml());
            dpw.close();
            Scanner averaged = new Scanner(fum); 
            ArrayList <String> SA = new ArrayList<>();
            while(averaged.hasNextLine()){
                String l = averaged.nextLine();
                SA.add(l);
            }
            averaged.close();
            */
            
            boolean complete = true;
            int indf = 0;
            ArrayList <Double> prices  = new ArrayList<>();
            
            while(complete){

            String timef = String.valueOf(LocalTime.now());
            String[] timesf = timef.split(":");

            System.out.println("enter command: ");
            try{
            TimeUnit.SECONDS.sleep(5);
            }
            catch(Exception e){
                System.out.println("possible overload");
            }
            Scanner incomplete = new Scanner(System.in);            
                if(incomplete.hasNext()){
                    System.out.println("PROCESSING");
                if(incomplete.next().equalsIgnoreCase("finish")){
                    complete=false;
                }
                }
            incomplete.close();
            try{
            TimeUnit.SECONDS.sleep(5);
            }
            catch(Exception e){
                System.out.println("possible overload");
            }
            String urlz ="https://www.marketwatch.com/investing/stock/"+tag+"?mod=over_search";
            Document docz = Jsoup.connect(urlz).get();
            File myFilez = new File("Htmlz.out");
            PrintWriter pwz = new PrintWriter(myFilez);
            pwz.println(docz.outerHtml());
            pwz.close();
            ArrayList <String> vz = new ArrayList<>();
            Scanner inz = new Scanner(myFilez);
            while(inz.hasNextLine()){
                String asdz = inz.nextLine();
                vz.add(asdz);
            }
            inz.close();
            String prf="";
            int indexf = 0;
            for(int i =0;i<n;i++){
                String find = vz.get(i);
                for(int k = 0;k<find.length()-8;k++){
                    if(find.substring(k,k+8).equals("lastsale")){
                        prf = vz.get(i+1);
                        index = i+6;
                                break;
                   } 
                }
            }
            prf = prf.replace(",", "");
            double pricef = Double.valueOf(prf);     
            System.out.println(pricef);
            prices.add(pricef);
            }
            }
            }
