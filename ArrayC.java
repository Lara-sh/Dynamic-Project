package application;

public class ArrayC {

    private int[] coins;

    public ArrayC() {
        this.coins = new int[0]; 
    }

    public int[] getCoins() {
        return coins;
    }
    public void setCoins(int[] coins) {
        this.coins = coins;
    }
    public int length() {
        return coins.length;
    }
}
