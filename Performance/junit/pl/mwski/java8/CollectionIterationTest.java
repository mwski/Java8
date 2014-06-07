package pl.mwski.java8;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.h2.H2Consumer;

@AxisRange(min = 0, max = 2)
@BenchmarkMethodChart(filePrefix = "benchmark.CLASSNAME")
@BenchmarkOptions(callgc = false, benchmarkRounds = 20, warmupRounds = 5)
public class CollectionIterationTest {
	public static int NO_OF_ITEMS = 500000;

	static class DataBean {

		private int intField;
		private float floatField;
		private String stringField;
		private Integer integerField;
		private Float floatOField;
		private boolean booleanField;
		private Boolean booleanOField;
		private BigDecimal bigdecimalField;

		public DataBean(int intField, float floatField, String stringField,
				Integer integerField, Float floatOField, boolean booleanField,
				Boolean booleanOField, BigDecimal bigdecimalField) {
			super();
			this.intField = intField;
			this.floatField = floatField;
			this.stringField = stringField;
			this.integerField = integerField;
			this.floatOField = floatOField;
			this.booleanField = booleanField;
			this.booleanOField = booleanOField;
			this.bigdecimalField = bigdecimalField;
		}

		public int getIntField() {
			return intField;
		}
		public void setIntField(int intField) {
			this.intField = intField;
		}
		public float getFloatField() {
			return floatField;
		}
		public void setFloatField(float floatField) {
			this.floatField = floatField;
		}
		public String getStringField() {
			return stringField;
		}
		public void setStringField(String stringField) {
			this.stringField = stringField;
		}
		public Integer getIntegerField() {
			return integerField;
		}
		public void setIntegerField(Integer integerField) {
			this.integerField = integerField;
		}
		public Float getFloatOField() {
			return floatOField;
		}
		public void setFloatOField(Float floatOField) {
			this.floatOField = floatOField;
		}
		public boolean isBooleanField() {
			return booleanField;
		}
		public void setBooleanField(boolean booleanField) {
			this.booleanField = booleanField;
		}
		public Boolean getBooleanOField() {
			return booleanOField;
		}
		public void setBooleanOField(Boolean booleanOField) {
			this.booleanOField = booleanOField;
		}
		public BigDecimal getBigdecimalField() {
			return bigdecimalField;
		}
		public void setBigdecimalField(BigDecimal bigdecimalField) {
			this.bigdecimalField = bigdecimalField;
		}

	}

	private static final List<DataBean> dataBeanList = new ArrayList<>(NO_OF_ITEMS); 

	private final List<Object[]> tabObjList = new ArrayList<>(NO_OF_ITEMS); 

	private static final H2Consumer consumer = new H2Consumer(
			new File("testResult/db"),
			new File("testResult/charts"),
			"custom-build-key");

	@Rule
	public TestRule benchmarkRun = new BenchmarkRule(consumer);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fillDataBeans();
	}

	@Test
	public void iterationForEach() {
		for (DataBean dataBean : dataBeanList) {
			tabObjList.add(new Object[] {
					dataBean.getIntField(), 
					dataBean.getFloatField(), 
					dataBean.getStringField(),
					dataBean.getIntegerField(),
					dataBean.getFloatOField(),
					dataBean.getBooleanOField(),
					dataBean.getBigdecimalField()
			});
		}
	}

	@Test
	public void iterationStream() {
		Stream<DataBean> stream = dataBeanList.stream();
		tabObjList.addAll(
				stream.map(
						dataBean -> new Object[] {
								dataBean.getIntField(), 
								dataBean.getFloatField(), 
								dataBean.getStringField(),
								dataBean.getIntegerField(),
								dataBean.getFloatOField(),
								dataBean.getBooleanOField(),
								dataBean.getBigdecimalField()
						}
						)
						.collect(Collectors.toList())
				);
	}

	@Test
	public void iterationParallelStream() {
		Stream<DataBean> stream = dataBeanList.parallelStream();
		tabObjList.addAll(
				stream.map(
						dataBean -> new Object[] {
								dataBean.getIntField(), 
								dataBean.getFloatField(), 
								dataBean.getStringField(),
								dataBean.getIntegerField(),
								dataBean.getFloatOField(),
								dataBean.getBooleanOField(),
								dataBean.getBigdecimalField()
						}
						)
						.collect(Collectors.toList())
				);
	}

	private static void fillDataBeans() {
		for (int i = 0; i < NO_OF_ITEMS; i++) {
			DataBean e = new DataBean(i, 
					(float)(Math.PI * i * Math.random()), 
					randomLetters(), 
					(int)(i * Math.random()), 
					Float.valueOf((float) (Math.PI * i* Math.random())), 
					(i * Math.random()) % 2 > 0, 
					(i * Math.random()) % 2 < 0, 
					BigDecimal.valueOf((int)(i * Math.random())));
			dataBeanList.add(e);
		}
	}

	private static String randomLetters() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 25; i++) {
			sb.append((char)(65 + Math.round((35 * Math.random()))));
		}
		return sb.toString();
	}

}
