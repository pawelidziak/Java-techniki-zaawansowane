import java.beans.*;

public class AddressBookBeanBeanInfo {

    private final static Class beanClass = AddressBookBean.class;

    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(beanClass);
        bd.setDisplayName("Title");
        return bd;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor textPD = new PropertyDescriptor("title", beanClass);
            PropertyDescriptor rv[] = {textPD};
            return rv;
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }

}
