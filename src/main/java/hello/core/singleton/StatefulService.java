package hello.core.singleton;

public class StatefulService {

    private int price;  // 10000 -> 20000

    /*public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제!!
    }*/

    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price;   // 공유 필드를 거치지 않고 지역변수 이기 때문에 문제가 없음!!
    }

    public int getPrice() {
        return price;
    }

}
