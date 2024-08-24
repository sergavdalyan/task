package com.lightspeed.parser;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {

	public Query parse(String sql) {
		Query query = new Query();
		sql = sql.trim();

		parseColumns(query, sql);
		parseFromSources(query, sql);
		parseJoins(query, sql);
		parseWhereClause(query, sql);
		parseGroupBy(query, sql);
		parseOrderBy(query, sql);
		parseLimitOffset(query, sql);

		return query;
	}

	private void parseColumns(Query query, String sql) {
		Matcher matcher = Pattern.compile("SELECT\\s+(.*?)\\s+FROM", Pattern.CASE_INSENSITIVE).matcher(sql);
		if (matcher.find()) {
			String selectPart = matcher.group(1);
			String[] columns = selectPart.split(",");
			for (String column : columns) {
				query.getColumns().add(column.trim());
			}
		}
	}

	private void parseFromSources(Query query, String sql) {
		Matcher matcher = Pattern.compile("FROM\\s+(.*?)(\\s+JOIN|\\s+WHERE|\\s+GROUP BY|\\s+ORDER BY|\\s+LIMIT|$)",
				Pattern.CASE_INSENSITIVE).matcher(sql);
		if (matcher.find()) {
			String fromPart = matcher.group(1);
			String[] sources = fromPart.split(",");
			for (String source : sources) {
				String[] parts = source.trim().split("\\s+");
				String tableName = parts[0];
				String alias = parts.length > 1 ? parts[1] : null;
				query.getFromSources().add(new Source(tableName, alias));
			}
		}
	}

	private void parseJoins(Query query, String sql) {
		Matcher matcher = Pattern.compile(
				"(LEFT JOIN|RIGHT JOIN|INNER JOIN|FULL JOIN|JOIN)\\s+(\\w+)\\s+ON\\s+\\((.*?)\\)",
				Pattern.CASE_INSENSITIVE).matcher(sql);

		while (matcher.find()) {
			String joinType = matcher.group(1).trim();
			String tableName = matcher.group(2).trim();
			String onClause = matcher.group(3).trim();

			query.getJoins().add(new Join(joinType, new Source(tableName, null), onClause));
		}
	}


	private void parseWhereClause(Query query, String sql) {
		Matcher matcher = Pattern.compile("WHERE\\s+(.*?)(\\s+GROUP BY|\\s+ORDER BY|\\s+LIMIT|$)",
				Pattern.CASE_INSENSITIVE).matcher(sql);
		if (matcher.find()) {
			String wherePart = matcher.group(1);
			String[] conditions = wherePart.split("AND");
			for (String condition : conditions) {
				query.getWhereClauses().add(new WhereClause(condition.trim()));
			}
		}
	}

	private void parseGroupBy(Query query, String sql) {
		Matcher matcher = Pattern.compile("GROUP BY\\s+(.*?)(\\s+HAVING|\\s+ORDER BY|\\s+LIMIT|$)",
				Pattern.CASE_INSENSITIVE).matcher(sql);
		if (matcher.find()) {
			String groupByPart = matcher.group(1);
			String[] groupByColumns = groupByPart.split(",");
			for (String column : groupByColumns) {
				query.getGroupByColumns().add(column.trim());
			}
		}
	}

	private void parseOrderBy(Query query, String sql) {
		Matcher matcher = Pattern.compile("ORDER BY\\s+(.*?)(\\s+LIMIT|$)", Pattern.CASE_INSENSITIVE).matcher(sql);
		if (matcher.find()) {
			String orderByPart = matcher.group(1);
			String[] orderByColumns = orderByPart.split(",");
			for (String column : orderByColumns) {
				String[] colParts = column.trim().split("\\s+");
				String colName = colParts[0].trim();
				String direction = colParts.length > 1 ? colParts[1].trim() : "ASC";
				query.getSortColumns().add(new Sort(colName, direction));
			}
		}
	}

	private void parseLimitOffset(Query query, String sql) {
		Matcher matcher = Pattern.compile("LIMIT\\s+(\\d+)(\\s+OFFSET\\s+(\\d+))?", Pattern.CASE_INSENSITIVE)
				.matcher(sql);
		if (matcher.find()) {
			query.setLimit(Integer.parseInt(matcher.group(1)));
			if (matcher.group(3) != null) {
				query.setOffset(Integer.parseInt(matcher.group(3)));
			}
		}
	}
}

