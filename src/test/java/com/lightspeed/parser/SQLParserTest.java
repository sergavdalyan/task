package com.lightspeed.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLParserTest {

	private final SQLParser parser = new SQLParser();

	@Test
	public void testParseColumns() {
		final String sql = "SELECT author.name, count(book.id), sum(book.cost) FROM author";
		final Query query = parser.parse(sql);

		assertEquals(3, query.getColumns().size());
		assertTrue(query.getColumns().contains("author.name"));
		assertTrue(query.getColumns().contains("count(book.id)"));
		assertTrue(query.getColumns().contains("sum(book.cost)"));
	}

	@Test
	public void testParseFromSources() {
		final String sql = "SELECT * FROM author";
		final Query query = parser.parse(sql);

		assertEquals(1, query.getFromSources().size());
		assertEquals("author", query.getFromSources().get(0).getTableName());
	}

	@Test
	public void testParseJoins() {
		final String sql = "SELECT * FROM author LEFT JOIN book ON (author.id = book.author_id)";
		final Query query = parser.parse(sql);

		assertEquals(1, query.getJoins().size());
		Join join = query.getJoins().get(0);
		assertEquals("LEFT JOIN", join.getType());
		assertEquals("book", join.getSource().getTableName());
		assertEquals("author.id = book.author_id", join.getOnClause());
	}

	@Test
	public void testParseWhereClause() {
		final String sql = "SELECT * FROM book WHERE book.cost > 100 AND book.title LIKE '%Java%'";
		final Query query = parser.parse(sql);

		assertEquals(2, query.getWhereClauses().size());
		assertEquals("book.cost > 100", query.getWhereClauses().get(0).getCondition());
		assertEquals("book.title LIKE '%Java%'", query.getWhereClauses().get(1).getCondition());
	}

	@Test
	public void testParseGroupBy() {
		final String sql = "SELECT author.name, count(book.id) FROM author GROUP BY author.name";
		final Query query = parser.parse(sql);

		assertEquals(1, query.getGroupByColumns().size());
		assertEquals("author.name", query.getGroupByColumns().get(0));
	}

	@Test
	public void testParseOrderBy() {
		final String sql = "SELECT * FROM book ORDER BY book.cost DESC, book.title ASC";
		final Query query = parser.parse(sql);

		assertEquals(2, query.getSortColumns().size());
		Sort sort1 = query.getSortColumns().get(0);
		Sort sort2 = query.getSortColumns().get(1);
		assertEquals("book.cost", sort1.getColumn());
		assertEquals("DESC", sort1.getDirection());
		assertEquals("book.title", sort2.getColumn());
		assertEquals("ASC", sort2.getDirection());
	}

	@Test
	public void testParseLimitOffset() {
		final String sql = "SELECT * FROM book LIMIT 10 OFFSET 5";
		final Query query = parser.parse(sql);

		assertEquals(10, query.getLimit());
		assertEquals(5, query.getOffset());
	}
}
