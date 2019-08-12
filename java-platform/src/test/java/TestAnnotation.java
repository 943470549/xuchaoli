import com.platform.annotation.Author;
import entity.AbstractAuthor;
import entity.ChildAuthor;
import entity.ChildAuthor2;
import org.junit.jupiter.api.Test;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/9
 **/
public class TestAnnotation {

    @Test
    public void annotationExtendsTest() {
        Class<?> clz;
        try {
            clz = Class.forName(AbstractAuthor.class.getName());
            System.out.println(clz.isAnnotationPresent(Author.class));
            Author au = clz.getAnnotation(Author.class);
            System.out.println(au.name() + " " + au.webSite());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            clz = Class.forName(ChildAuthor.class.getName());
            System.out.println(clz.isAnnotationPresent(Author.class));
            Author au = clz.getAnnotation(Author.class);
            System.out.println(au.name() + " " + au.webSite());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            clz = Class.forName(ChildAuthor2.class.getName());
            System.out.println(clz.isAnnotationPresent(Author.class));
            Author au = clz.getAnnotation(Author.class);
            System.out.println(au.name() + " " + au.webSite());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
