package com.stackroute.datamunger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataMunger {

	public static void main(String[] args) {
		// read the query from the user into queryString variable

		String queryString;
		System.out.println("Enter a queryString");
		queryString = new Scanner(System.in).nextLine();
		DataMunger dataMunger = new DataMunger();
		dataMunger.parseQuery(queryString);
		// call the parseQuery method and pass the queryString variable as a
		// parameter
	}

	/*
	 * we are creating multiple methods, each of them are responsible for
	 * extracting a specific part of the query. However, the problem statement
	 * requires us to print all elements of the parsed queries. Hence, to reduce
	 * the complexity, we are using the parseQuery() method. From inside this
	 * method, we are calling all the methods together, so that we can call this
	 * method only from main() method to print the entire output in console
	 */
	public void parseQuery(String queryString) {
		// call the methods
		System.out.println("...");
		System.out.println(queryString.toLowerCase());
		getSplitStrings(queryString.toLowerCase());
		getFile(queryString.toLowerCase());
		getBaseQuery(queryString.toLowerCase());
		getConditionsPartQuery(queryString.toLowerCase());
		getConditions(queryString.toLowerCase());
		getLogicalOperators(queryString.toLowerCase());

		getFields(queryString.toLowerCase());
		getOrderByFields(queryString.toLowerCase());
		getGroupByFields(queryString.toLowerCase());
		getAggregateFunctions(queryString.toLowerCase());

	}

	/*
	 * this method will split the query string based on space into an array of
	 * words and display it on console
	 */
	public String[] getSplitStrings(String queryString) {
		System.out.println("in getSplitStrings");
		String[] arrWords = queryString.toLowerCase().split(" ");

		for (String splitString : arrWords) {
			System.out.println(splitString);
		}

		return arrWords;
	}

	/*
	 * extract the name of the file from the query. File name can be found after
	 * a space after "from" clause. Note: ----- CSV file can contain a field
	 * that contains from as a part of the column name. For eg:
	 * from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */
	public String getFile(String queryString) {
		System.out.println("in getFile");
		DataMunger dataMunger = new DataMunger();
		String fileName = "";
		for (String splitStringList : dataMunger.getSplitStrings(queryString)) {
			if (splitStringList.contains(".csv")) {
				fileName = splitStringList;
			}
		}
		return fileName;
	}

	/*
	 * This method is used to extract the baseQuery from the query string.
	 * BaseQuery contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. the query might not contain where clause but contain
	 * order by or group by clause 2. the query might not contain where, order
	 * by or group by clause 3. the query might not contain where, but can
	 * contain both group by and order by clause
	 */
	public String getBaseQuery(String queryString) {
		System.out.println("in getBaseQuery");
		DataMunger dataMunger = new DataMunger();
		String baseQuery = "";
		String[] arrWords = dataMunger.getSplitStrings(queryString);
		for (int index = 0; index < arrWords.length; index++) {
			if (arrWords[index].equals("where".toLowerCase()) || arrWords[index].equals("group".toLowerCase())
					|| arrWords[index].equals("order".toLowerCase()))
				break;
			baseQuery += arrWords[index] + " ";
		}
		// baseQuery=queryString.substring(0, queryString.indexOf("from"));
		System.out.println("Base Query : " + baseQuery);
		// return "Base Query :" + baseQuery;
		return baseQuery;
	}

	/*
	 * This method is used to extract the conditions part from the query string.
	 * The conditions part contains starting from where keyword till the next
	 * keyword, which is either group by or order by clause. In case of absence
	 * of both group by and order by clause, it will contain till the end of the
	 * query string. Note: ----- 1. The field name or value in the condition can
	 * contain keywords as a substring. For eg: from_city,job_order_no,group_no
	 * etc. 2. The query might not contain where clause at all.
	 */

	public String getConditionsPartQuery(String queryString) {
		System.out.println("in getConditionsPartQuery");
		if (queryString.toLowerCase().contains("where")) {
			String conditionPart = queryString.toLowerCase().split("where".toLowerCase())[1];
			System.out.println("+++" + conditionPart);
			conditionPart = conditionPart.split("group|order")[0];
			System.out.println("+++" + conditionPart.trim());
			return conditionPart;
		} else {
			return null;
		}
	}

	/*
	 * This method will extract condition(s) from the query string. The query
	 * can contain one or multiple conditions. In case of multiple conditions,
	 * the conditions will be separated by AND/OR keywords. for eg: Input:
	 * select city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014",
	 * "city ='Bangalore'"] and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain
	 * keywords as a substring. For eg: from_city,job_order_no,group_no etc. 2.
	 * The query might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {
		System.out.println("in getConditions");
		String[] conditions = null;
		String conditionPartQuery = new DataMunger().getConditionsPartQuery(queryString);
		if (conditionPartQuery != null) {
			conditions = conditionPartQuery.trim().split("\\sand\\s|\\sor\\s");
			for (String condition : conditions) {
				System.out.println(condition);
			}
		}
		return conditions;
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string.
	 * The extracted logical operators will be stored in a String array which
	 * will be returned by the method and the same will be printed Note: -------
	 * 1. AND/OR keyword will exist in the query only if where conditions exists
	 * and it contains multiple conditions. 2. AND/OR can exist as a substring
	 * in the conditions as well. For eg: name='Alexander',color='Red' etc.
	 * Please consider these as well when extracting the logical operators.
	 * 
	 */
	public String[] getLogicalOperators(String queryString) {
		System.out.println("in getLogicalOperators");
		List<String> logiOperator = new ArrayList<String>();
		String chkOperator = new DataMunger().getConditionsPartQuery(queryString);
		if (chkOperator != null) {
			String[] logicalOperators = chkOperator.split("\\s+");
			for (String logiop : logicalOperators) {
				if (logiop.equals("and".toLowerCase()) || logiop.equals("or".toLowerCase())) {
					logiOperator.add(logiop);
				}
			}
			String[] logicalOp = new String[logiOperator.size()];
			for (int i = 0; i < logicalOp.length; i++) {
				logicalOp[i] = logiOperator.get(i);
			}

			for (String loOperator : logicalOp) {
				System.out.println(loOperator);
			}

			return logicalOp;
		} else
			return null;
	}

	/*
	 * This method will extract the fields to be selected from the query string.
	 * The query string can have multiple fields separated by comma. The
	 * extracted fields will be stored in a String array which is to be printed
	 * in console as well as to be returned by the method
	 * 
	 * Note: ------ 1. The field name or value in the condition can contain
	 * keywords as a substring. For eg: from_city,job_order_no,group_no etc. 2.
	 * The field name can contain '*'
	 * 
	 */
	public String[] getFields(String queryString) {
		System.out.println("in getFields");
		String selectColumn = queryString.split("select".toLowerCase())[1].trim();
		// System.out.println(selectColumn);
		String selectColumnList = selectColumn.split("from".toLowerCase())[0].trim();
		// System.out.println(selectColumnList);
		if (selectColumnList.contains("*") || selectColumnList.length() <= 0) {
			String colList[] = new String[1];
			colList[0] = "*";
			return colList;
		} else {
			String colList[] = selectColumnList.split(",");

			for (String collist : colList) {
				System.out.println(collist);
			}

			return colList;
		}
	}

	/*
	 * This method extracts the order by fields from the query string. Note:
	 * ------ 1. The query string can contain more than one order by fields. 2.
	 * The query string might not contain order by clause at all. 3. The field
	 * names,condition values might contain "order" as a substring. For
	 * eg:order_number,job_order Consider this while extracting the order by
	 * fields
	 */
	public String[] getOrderByFields(String queryString) {
		System.out.println("in getOrderByFields");
		if (queryString.contains("order")) {
			String orderByCol = queryString.split("order by".toLowerCase())[1].trim();
			String[] orderByColList = orderByCol.split(",");

			for (String str : orderByColList) {
				System.out.println(str);
			}

			return orderByColList;
		} else
			return null;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * ------ 1. The query string can contain more than one group by fields. 2.
	 * The query string might not contain group by clause at all. 3. The field
	 * names,condition values might contain "group" as a substring. For eg:
	 * newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */
	public String[] getGroupByFields(String queryString) {
		System.out.println("in getGroupByFields");
		if (queryString.contains("group".trim().toLowerCase())) {
			String orderByCol = queryString.split("group by".toLowerCase())[1].trim();
			String[] groupByColList = orderByCol.split(",");
			for (String groupbyColList : groupByColList) {
				System.out.println(groupbyColList);
			}
			return groupByColList;
		} else {
			return null;
		}
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 * ------ 1. aggregate functions will start with
	 * "sum"/"count"/"min"/"max"/"avg" followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */
	public String[] getAggregateFunctions(String queryString) {
		System.out.println("in getAggregateFunctions");
		String selectColumn = queryString.split("select".toLowerCase())[1].trim();
		// System.out.println(selectColumn);
		String selectColumnList = selectColumn.split("from".toLowerCase())[0].trim();
		// System.out.println(selectColumnList);
		if (selectColumnList.contains("*") || selectColumnList.length() <= 0) {
			String allColumns[] = new String[1];
			allColumns[0] = "*";
			return allColumns;
		} else {
			String colList[] = selectColumnList.split(",");
			ArrayList<String> aggregateColumns = new ArrayList<String>();
			for (String collist : colList) {
				if (collist.startsWith("min(") || collist.startsWith("max(") || collist.startsWith("avg(")
						|| collist.startsWith("sum(") || collist.startsWith("count(")) {
					aggregateColumns.add(collist);
				}
			}
			String[] aggreColumns = new String[aggregateColumns.size()];
			for (int index = 0; index < aggregateColumns.size(); index++) {
				aggreColumns[index] = aggregateColumns.get(index);
			}
			for (String colNames : aggreColumns) {
				System.out.println(colNames);
			}
			return aggreColumns;
		}
	}
}