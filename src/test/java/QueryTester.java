import org.ah.jedisQueryBuilder.QueryBuilder;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.UnifiedJedis;

public class QueryTester {

	UnifiedJedis jedis;

	private void connect() {
		jedis =new UnifiedJedis(new HostAndPort("127.0.0.1",6379));
	}

	@Test
	public void testQuery() {
		connect();
		dto dto=new dto();
		QueryBuilder queryBuilder = new QueryBuilder(jedis, "test2-idx", dto);
		queryBuilder.setTextQuery(dto.getQueryParameterList().get(1));
	}
}
