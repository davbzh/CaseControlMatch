package nkcx;

/**
 * Created by davbzh on 2017-02-09.
 */

public class Main {
    public static void main(String[] args)  {
        Cli cli = new Cli(args);
        try {
            cli.parse();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
