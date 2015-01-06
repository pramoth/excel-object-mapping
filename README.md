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
    <version>1.0-SNAPSHORT</version>
</dependency>
```
- Write code

```java
List<YOUR_MODEL> objects = ExOM.mapFromExcel(EXCEL_FILE)
                .toObjectOf(YOUR_MODEL)
                .map();
```
<h3>example</h3>

Model.java
```java
public class Model {

    @Column(name = "first name") //must same excel header
    private String fistName;
    @Column(name = "last name")
    private String lastName;
    private Integer age; //use wrapper class only (*** not primitive type)
    @Column(name = "birth date", pattern = "dd/MM/yyyy")
    private Date birthdate;

    ...
    //getter and setter method
}
```
Test
```java
File excelFile = new File(getClass().getResource("/excel.xlsx").getPath());
List<Model> items = ExOM.mapFromExcel(excelFile)
        .toObjectOf(Model.class)
        .map();
        
for (Model item : items) {
    LOG.debug("first name --> {}", item.getFistName());
    LOG.debug("last name --> {}", item.getLastName());
    LOG.debug("age --> {}", item.getAge());
    LOG.debug("birth date --> {}", item.getBirthdate());
    LOG.debug("");
}  
```
