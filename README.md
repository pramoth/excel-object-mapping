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
// sample 1 : basic usage
File excelFile = new File(getClass().getResource("/excel.xlsx").getPath());
LOG.debug("------Sample 1------");
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
