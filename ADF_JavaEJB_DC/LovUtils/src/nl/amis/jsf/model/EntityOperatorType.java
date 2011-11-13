package nl.amis.jsf.model;

public enum EntityOperatorType {
    EQUALS_NUMBER("QueryOperator.equals_LABEL", 1, " ({0} = {1,number,#}) "),
    EQUALS_STRING("QueryOperator.equals_LABEL", 1, " (upper({0}) = upper(''{1}'')) "),
    EQUALS_DATE("QueryOperator.equals_LABEL", 1, " ({0} = '{'d ''{1,date,yyyy-MM-dd}'''}') "),
    NOT_EQUALS_NUMBER("QueryOperator.not_equals_LABEL", 1, " ({0} <> {1,number,#}) "),
    NOT_EQUALS_STRING("QueryOperator.not_equals_LABEL", 1, " (upper({0}) <> upper(''{1}'')) "),
    NOT_EQUALS_DATE("QueryOperator.not_equals_LABEL", 1, " ({0} <> '{'d ''{1,date,yyyy-MM-dd}'''}') "),
    GREATER_THAN_NUMBER("QueryOperator.greater_than_LABEL", 1, " ({0} > {1,number,#}) "),
    GREATER_THAN_DATE("QueryOperator.after_LABEL", 1, " ({0} > '{'d ''{1,date,yyyy-MM-dd}'''}) "),
    LESS_THAN_NUMBER("QueryOperator.less_than_LABEL", 1, " ({0} < {1,number,#}) "),
    LESS_THAN_DATE("QueryOperator.before_LABEL", 1, " ({0} < '{'d ''{1,date,yyyy-MM-dd}'''}) "),
    GREATER_THAN_OR_EQUAL_NUMBER("QueryOperator.greater_than_equal_LABEL", 1, " ({0} >= {1,number,#}) "),
    GREATER_THAN_OR_EQUAL_DATE("QueryOperator.on_or_after_LABEL", 1, " ({0} >= '{'d ''{1,date,yyyy-MM-dd}'''}) "),
    LESS_THAN_OR_EQUAL_NUMBER("QueryOperator.less_than_equal_LABEL", 1, " ({0} <= {1,number,#}) "),
    LESS_THAN_OR_EQUAL_DATE("QueryOperator.on_or_before_LABEL", 1, " ({0} <= '{'d ''{1,date,yyyy-MM-dd}'''}) "),
    BETWEEN_NUMBER("QueryOperator.between_LABEL", 2, " ({0} between {1,number,#} and {2,number,#}) "),
    BETWEEN_DATE("QueryOperator.between_LABEL", 2, " ({0} between '{'d ''{1,date,yyyy-MM-dd}'''} and '{'d ''{2,date,yyyy-MM-dd}'''}) "),
    STARTS_WITH("QueryOperator.starts_with_LABEL", 1, " (upper({0}) like upper(''{1}%'')) "),
    ENDS_WITH("QueryOperator.ends_with_LABEL", 1, " (upper({0}) like upper(''%{1}'')) "),
    CONTAINS("QueryOperator.contains_LABEL", 1, " (upper({0}) like upper(''%{1}%'')) "),
    NOT_CONTAINS("QueryOperator.does_not_contain_LABEL", 1, " (upper({0}) not like upper(''%{1}%'')) "),
    IN("QueryOperator.in_LABEL", 1, " ({0} in ({1}))"),
    NOT_IN("QueryOperator.not_in_LABEL", 1, " ({0} not in ({1}))"),
    NO_OPERATOR("QueryOperator.no_operator_LABEL", 0, "");

    @SuppressWarnings("compatibility:9170155285801677101")
    private static final long serialVersionUID = 2L;
    
    private String label;
    private int operandCount;
    private String jpql;

    EntityOperatorType(String label, int operandCount, String jpql) {
        this.label = label;
        this.operandCount = operandCount;
        this.jpql = jpql;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return "";
    }

    public int getOperandCount() {
        return operandCount;
    }

    public String getJpql() {
        return jpql;
    }
}