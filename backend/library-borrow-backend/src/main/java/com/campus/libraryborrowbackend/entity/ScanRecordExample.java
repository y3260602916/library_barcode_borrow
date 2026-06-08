package com.campus.libraryborrowbackend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScanRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ScanRecordExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andScanIdIsNull() {
            addCriterion("scan_id is null");
            return (Criteria) this;
        }

        public Criteria andScanIdIsNotNull() {
            addCriterion("scan_id is not null");
            return (Criteria) this;
        }

        public Criteria andScanIdEqualTo(Integer value) {
            addCriterion("scan_id =", value, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdNotEqualTo(Integer value) {
            addCriterion("scan_id <>", value, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdGreaterThan(Integer value) {
            addCriterion("scan_id >", value, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("scan_id >=", value, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdLessThan(Integer value) {
            addCriterion("scan_id <", value, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdLessThanOrEqualTo(Integer value) {
            addCriterion("scan_id <=", value, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdIn(List<Integer> values) {
            addCriterion("scan_id in", values, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdNotIn(List<Integer> values) {
            addCriterion("scan_id not in", values, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdBetween(Integer value1, Integer value2) {
            addCriterion("scan_id between", value1, value2, "scanId");
            return (Criteria) this;
        }

        public Criteria andScanIdNotBetween(Integer value1, Integer value2) {
            addCriterion("scan_id not between", value1, value2, "scanId");
            return (Criteria) this;
        }

        public Criteria andImgPathIsNull() {
            addCriterion("img_path is null");
            return (Criteria) this;
        }

        public Criteria andImgPathIsNotNull() {
            addCriterion("img_path is not null");
            return (Criteria) this;
        }

        public Criteria andImgPathEqualTo(String value) {
            addCriterion("img_path =", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotEqualTo(String value) {
            addCriterion("img_path <>", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathGreaterThan(String value) {
            addCriterion("img_path >", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathGreaterThanOrEqualTo(String value) {
            addCriterion("img_path >=", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathLessThan(String value) {
            addCriterion("img_path <", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathLessThanOrEqualTo(String value) {
            addCriterion("img_path <=", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathLike(String value) {
            addCriterion("img_path like", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotLike(String value) {
            addCriterion("img_path not like", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathIn(List<String> values) {
            addCriterion("img_path in", values, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotIn(List<String> values) {
            addCriterion("img_path not in", values, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathBetween(String value1, String value2) {
            addCriterion("img_path between", value1, value2, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotBetween(String value1, String value2) {
            addCriterion("img_path not between", value1, value2, "imgPath");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeIsNull() {
            addCriterion("scan_barcode is null");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeIsNotNull() {
            addCriterion("scan_barcode is not null");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeEqualTo(String value) {
            addCriterion("scan_barcode =", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeNotEqualTo(String value) {
            addCriterion("scan_barcode <>", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeGreaterThan(String value) {
            addCriterion("scan_barcode >", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeGreaterThanOrEqualTo(String value) {
            addCriterion("scan_barcode >=", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeLessThan(String value) {
            addCriterion("scan_barcode <", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeLessThanOrEqualTo(String value) {
            addCriterion("scan_barcode <=", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeLike(String value) {
            addCriterion("scan_barcode like", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeNotLike(String value) {
            addCriterion("scan_barcode not like", value, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeIn(List<String> values) {
            addCriterion("scan_barcode in", values, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeNotIn(List<String> values) {
            addCriterion("scan_barcode not in", values, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeBetween(String value1, String value2) {
            addCriterion("scan_barcode between", value1, value2, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanBarcodeNotBetween(String value1, String value2) {
            addCriterion("scan_barcode not between", value1, value2, "scanBarcode");
            return (Criteria) this;
        }

        public Criteria andScanTimeIsNull() {
            addCriterion("scan_time is null");
            return (Criteria) this;
        }

        public Criteria andScanTimeIsNotNull() {
            addCriterion("scan_time is not null");
            return (Criteria) this;
        }

        public Criteria andScanTimeEqualTo(Date value) {
            addCriterion("scan_time =", value, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeNotEqualTo(Date value) {
            addCriterion("scan_time <>", value, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeGreaterThan(Date value) {
            addCriterion("scan_time >", value, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scan_time >=", value, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeLessThan(Date value) {
            addCriterion("scan_time <", value, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeLessThanOrEqualTo(Date value) {
            addCriterion("scan_time <=", value, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeIn(List<Date> values) {
            addCriterion("scan_time in", values, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeNotIn(List<Date> values) {
            addCriterion("scan_time not in", values, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeBetween(Date value1, Date value2) {
            addCriterion("scan_time between", value1, value2, "scanTime");
            return (Criteria) this;
        }

        public Criteria andScanTimeNotBetween(Date value1, Date value2) {
            addCriterion("scan_time not between", value1, value2, "scanTime");
            return (Criteria) this;
        }

        public Criteria andResultIsNull() {
            addCriterion("result is null");
            return (Criteria) this;
        }

        public Criteria andResultIsNotNull() {
            addCriterion("result is not null");
            return (Criteria) this;
        }

        public Criteria andResultEqualTo(String value) {
            addCriterion("result =", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotEqualTo(String value) {
            addCriterion("result <>", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThan(String value) {
            addCriterion("result >", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThanOrEqualTo(String value) {
            addCriterion("result >=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThan(String value) {
            addCriterion("result <", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThanOrEqualTo(String value) {
            addCriterion("result <=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLike(String value) {
            addCriterion("result like", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotLike(String value) {
            addCriterion("result not like", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultIn(List<String> values) {
            addCriterion("result in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotIn(List<String> values) {
            addCriterion("result not in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultBetween(String value1, String value2) {
            addCriterion("result between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotBetween(String value1, String value2) {
            addCriterion("result not between", value1, value2, "result");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}