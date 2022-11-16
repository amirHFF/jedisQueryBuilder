import org.ah.basic.RedisQuery;
import org.ah.basic.RedisQueryFilter;

public class dto extends RedisQuery {
	@RedisQueryFilter(alias = "age")
	private int age;
	@RedisQueryFilter(alias = "name")
	private String name;
	@RedisQueryFilter
	private boolean married;

	public int getAge() {
		return age;
		getQueryParameterList().replace("age",)
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
