package test_classes.jakasPaczka;

public class DemoClass extends Demo {

    public DemoClass() {
        System.out.println("DemoClass constructor");
    }

    public void demoMethod(int n, String s) {
        System.out.println("demoMethod " + s);
    }

    public void demoMethod3(int[] tab){
        System.out.println("test");
    }

    public String demoMethod2(int demoParam, float cos) {
        System.out.println("Parameter passed: " + demoParam);

        return DemoClass.class.getName();
    }
}
