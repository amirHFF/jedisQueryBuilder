import org.ah.jedisQueryBuilder.AutomateQueryBuilder;
import org.ah.jedisQueryBuilder.CustomQueryBuilder;
import org.ah.jedisQueryBuilder.QueryOperator;
import org.ah.jedisQueryBuilder.Queryable;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.SearchResult;

import java.util.Arrays;

public class QueryTester {

	UnifiedJedis jedis;

	@Test
	public void testObjectQuery() {
		connect();
		dto dto = new dto();
		dto.setName("amir");
		dto.setAge(12);
		Queryable automateQueryable = new AutomateQueryBuilder(jedis, "test2-idx", dto);
		SearchResult result = automateQueryable.search();
		for (Document document : result.getDocuments()) {
			System.out.println(document.getProperties());
		}
	}

	private void connect() {
		jedis = new UnifiedJedis(new HostAndPort("127.0.0.1", 6379));
	}

	@Test
	public void testListQuery() {
		connect();
		dto dto = new dto();
		dto dto2 = new dto();
		dto2.setAge(23);
		dto2.setName("reza");
//		dto2.setMarried(true);
		dto.setName("*mi*");
		dto.setMarried(false);

//		dto.setOperator(QueryOperator.OR);
		Queryable automateQueryable = new AutomateQueryBuilder(jedis, "test2-idx", Arrays.asList(dto, dto2));
		automateQueryable.build();
		SearchResult result = automateQueryable.search();
		for (Document document : result.getDocuments()) {
			System.out.println(document.getProperties());
		}
	}
	@Test
	public void testCustomQuery(){
		connect();
		Queryable query=new CustomQueryBuilder(jedis,"test2-idx");
		query.addOperation(QueryOperator.OPEN_PARENTHESES).addFilter("age",23).addOperation(QueryOperator.AND).addFilter("name","reza")
				.addOperation(QueryOperator.CLOSE_PARENTHESES).addOperation(QueryOperator.OR).addOperation(QueryOperator.OPEN_PARENTHESES).addFilter("age",12).addOperation(QueryOperator.CLOSE_PARENTHESES).build();
		SearchResult result= query.search();
		for (Document document : result.getDocuments()) {
			System.out.println(document.getProperties());
		}
	}
}
