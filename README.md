- Clone this project to your computer<br/>
- Build project by maven

```bash
$ mvn clean install -U
```
- Add dependency to your project (pom.xml)

```xml
<dependency>
    <groupId>com.blogspot.na5cent</groupId>
    <artifactId>excel-object-mapping</artifactId>
    <version>1.0.0-releases</version>
</dependency>
```
- Write code

```java
List<YOUR_MODEL> objects = ExcelMapper.mapFromExcel(EXCEL_FILE)
                .toObjectOf(YOUR_MODEL)
                .fromSheet(SHEET_NUMBER)
                .map();
```

<h3>example1: Basic Usage</h3>

Model.java
```java
public class Model {

	@Column(name = "first name") // must same excel header (by default is first row)
	private String fistName;
	@Column(name = "last name")
	private String lastName;
	private Integer age; //use wrapper class only (*** not primitive type)
	@Column(name = "birth date", pattern = "dd/MM/yyyy")
	private Date birthdate;
	@Column(index = 4) // must same excel header index (first column index is zero)
	private String fatherName;
	@Column(index = 5)
	private Boolean iq; //use wrapper class only (*** not primitive type)

 	...
	//getter and setter method
}
```
Test
```java
File excelFile = new File(getClass().getResource("/excel.xlsx").getPath());

List<Model> items = ExcelMapper.mapFromExcel(excelFile)
        .toObjectOf(Model.class)
        .fromSheet(0) // if this method not used , called all sheets
        .map();

for (Model item : items) {
    LOG.debug("first name --> {}", item.getFistName());
    LOG.debug("last name --> {}", item.getLastName());
    LOG.debug("age --> {}", item.getAge());
    LOG.debug("birth date --> {}", item.getBirthdate());
    LOG.debug("father name --> {}", item.getFatherName());
    LOG.debug("IQ --> {}", item.getIq());
    LOG.debug("");
}  
```


<h3>example2: Without Annotation</h3>

Model2.java
```java
public class Model2 {

	private String fistName;
	private String lastName;
	private Integer age;
	private Date birthdate;
	private String fatherName;
	private Boolean iq;

 	...
	//getter and setter method
}
```
Test
```java
File excelFile2 = new File(getClass().getResource("/excel2.xlsx").getPath());

Map<String, Integer> fieldToColumnIndex = new HashMap<>(); // map all property of model to index of excel column
fieldToColumnIndex.put("fistName", 0);
fieldToColumnIndex.put("lastName", 1);
fieldToColumnIndex.put("age", 2);
fieldToColumnIndex.put("birthdate", 5);
fieldToColumnIndex.put("fatherName", 8);
fieldToColumnIndex.put("iq", 11);

List<Model2> items2 = ExcelMapper.mapFromExcel(excelFile2)
		.toObjectOf(Model2.class)
		.fromSheet(0)
		.mapFieldFrom(fieldToColumnIndex) // map dynamically
		.map();

for (Model2 item : items2) {
	LOG.debug("first name --> {}", item.getFistName());
	LOG.debug("last name --> {}", item.getLastName());
	LOG.debug("age --> {}", item.getAge());
	LOG.debug("birth date --> {}", item.getBirthdate());
	LOG.debug("father name --> {}", item.getFatherName());
	LOG.debug("IQ --> {}", item.getIq());
	LOG.debug("");
}
```


<h3>example3: Customization headerRowNum & startRowNum & endRowNum</h3>

Model.java
```java
public class Model {

	@Column(name = "first name") // must same excel header (by default is first row)
	private String fistName;
	@Column(name = "last name")
	private String lastName;
	private Integer age; //use wrapper class only (*** not primitive type)
	@Column(name = "birth date", pattern = "dd/MM/yyyy")
	private Date birthdate;
	@Column(index = 4) // must same excel header index (first column index is zero)
	private String fatherName;
	@Column(index = 5)
	private Boolean iq; //use wrapper class only (*** not primitive type)

 	...
	//getter and setter method
}
```
Test
```java
File excelFile3 = new File(getClass().getResource("/excel3.xlsx").getPath());

List<Model> items3 = ExcelMapper.mapFromExcel(excelFile3)
        .toObjectOf(Model.class)
        .fromSheet(0)
        .headerRowNumber(1)
        .startRowNumber(3)
        .endRowNumber(4)
        .map();

for (Model item : items3) {
    LOG.debug("first name --> {}", item.getFistName());
    LOG.debug("last name --> {}", item.getLastName());
    LOG.debug("age --> {}", item.getAge());
    LOG.debug("birth date --> {}", item.getBirthdate());
    LOG.debug("father name --> {}", item.getFatherName());
    LOG.debug("IQ --> {}", item.getIq());
    LOG.debug("");
}
```


<h3>example4: Exception Management</h3>

Model.java
```java
public class Model {

	@Column(name = "first name") // must same excel header (by default is first row)
	private String fistName;
	@Column(name = "last name")
	private String lastName;
	private Integer age; //use wrapper class only (*** not primitive type)
	@Column(name = "birth date", pattern = "dd/MM/yyyy")
	private Date birthdate;
	@Column(index = 4) // must same excel header index (first column index is zero)
	private String fatherName;
	@Column(index = 5)
	private Boolean iq; //use wrapper class only (*** not primitive type)

 	...
	//getter and setter method
}
```
Test
```java
File excelFile4 = new File(getClass().getResource("/excel4.xlsx").getPath());

ArrayList<Model> listExceptions = new ArrayList<Model>();

List<Model> items4 = ExcelMapper.mapFromExcel(excelFile4)
		.toObjectOf(Model.class)
		.fromSheet(0)
		.handleCellExceptions() // for manage exception, must called this method first
		.cellExceptionsColor(IndexedColors.YELLOW) // manage color cell
		.getWorkbookExceptions(new WorkbookCallback() { // get workbook that exception shown that
			@Override
			public void getWorkbook(Workbook workbook) throws Throwable {
				// write workbook on disk
				FileOutputStream fileOut = new FileOutputStream(
						getClass().getResource("/").getPath() + "exception.xlsx");
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				LOG.debug("file writed on disk");
			}
		})
		.getAllExceptions(listExceptions) // get all list rows that have exceptions
		.map();

LOG.debug("List Count --> {} ", items4.size());
LOG.debug("Exceptions List Count --> {} ", listExceptions.size());
LOG.debug("");

for (Model item : listExceptions) {
	LOG.debug("exception first name --> {}", item.getFistName());
	LOG.debug("exception last name --> {}", item.getLastName());
	LOG.debug("exception age --> {}", item.getAge());
	LOG.debug("exception birth date --> {}", item.getBirthdate());
	LOG.debug("exception father name --> {}", item.getFatherName());
	LOG.debug("exception IQ --> {}", item.getIq()); // iq data has exception and is null
	LOG.debug("");
}
```

- Good Luck..!