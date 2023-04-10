import java.util.ArrayList;
/**
 *
 * @author hayden
 */
public class tradingBot {
    
    public static double optionPrice(double s, double k, double r, double v, double t, int n, boolean call) {
        double deltaT = t / n;
        double u = Math.exp(v * Math.sqrt(deltaT));
        double d = 1 / u;
        double p = (Math.exp(r * deltaT) - d) / (u - d);
        
        double[][] prices = new double[n + 1][n + 1];
        double[][] values = new double[n + 1][n + 1];
        
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                prices[i][j] = s * Math.pow(u, j) * Math.pow(d, i - j);
            }
        }
        
        for (int j = 0; j <= n; j++) {
            values[n][j] = Math.max(0, (call ? 1 : -1) * (prices[n][j] - k));
        }
        
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                values[i][j] = (Math.exp(-r * deltaT) * (p * values[i + 1][j + 1] + (1 - p) * values[i + 1][j]));
            }
        }
        
        return values[0][0];
    }
    
    public static boolean executeTrade(double intrinsicVal, double actualMarketVal, double amount){
     boolean execute = false;
    
     if(amount < 1){
        
        System.out.println("Not buying/selling at this point");
    }else{
    if(intrinsicVal < actualMarketVal){
            
         //code that executes a buying of contracts function as intrinsic value < actual market value
            System.out.println("Buying " + amount + " contracts... " );
            execute = true;
        }else{
            
         //code that executes a selling function as intrinsic value > actual market value
            System.out.println("Selling " + amount + " contracts...");
            execute = true; 
        }
     }
     
       return execute; 
   }
    

    
    public static double calculateAmount(double investmentAmount, double marketValue, double intrinsicValue, int optMultiplier, ArrayList<Double> historicalPrices){
        double sd = calculateSd(historicalPrices);
        double delta = (marketValue - intrinsicValue) / intrinsicValue;
        double n = 0.0; //number of options to buy/sell
        
       n =(investmentAmount *(1 + delta))/ (intrinsicValue * optMultiplier * sd);
       
       return n;
    }
    
    public static double calculateSd(ArrayList<Double> historicalPrices){
        
        double mean = calculateMean(historicalPrices);
        double sumOfSquares = 0.0;
        int n = historicalPrices.size();
        
        for(double price : historicalPrices){
            
            sumOfSquares += Math.pow(price - mean, 2);
        }
        
        double var = sumOfSquares / (n-1);
        double sd = Math.sqrt(var);
        
        return sd;
        
        
    }
    public static double calculateMean(ArrayList<Double> historicalPrices){
        
        double sum = 0.0;
        int n = historicalPrices.size();
        
        for(double price : historicalPrices){
         
            sum += price;
            
        }
       double mean = sum/n;
       
       return mean; 
    }
    public static void main(String[] args) {
        double s = 2; // underlying asset price
        double k = 3; // strike price
        double r = 0.05; // risk-free interest rate
        double v = 0.2; // volatility
        double t = 1; // time to expiration (in years)
        int n = 100; // number of time steps
        boolean call = true; // true if call option, false if put option
        
        double optionPrice = optionPrice(s, k, r, v, t, n, call);
        double investmentAmount = 500.0;
        int optMultiplier = 100;
        
        ArrayList<Double> historicalPrices = new ArrayList<Double>();
        historicalPrices.add(89.0);
        historicalPrices.add(76.0);
        historicalPrices.add(56.0);
        historicalPrices.add(90.0);
        historicalPrices.add(95.0);
        historicalPrices.add(100.0);
        
        double calcAmount = calculateAmount(investmentAmount, s, optionPrice, optMultiplier, historicalPrices); 
        boolean execute = executeTrade(optionPrice, s, calcAmount);
        
        System.out.println("Option price: " + optionPrice);
    }
}