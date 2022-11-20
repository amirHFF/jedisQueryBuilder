import org.ah.jedisQueryBuilder.QueryObject;
import org.ah.jedisQueryBuilder.ObjectBinder;

public class dto extends QueryObject {
	@ObjectBinder(alias = "age")
	private Integer age;
	@ObjectBinder(alias = "name")
	private String name;
	@ObjectBinder
	private Boolean married;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}
}
