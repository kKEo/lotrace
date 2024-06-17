



# Create POLYGON


```sql
CREATE TABLE routes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    route LINESTRING
)
```


### Insert first row

```sql
INSERT INTO routes (name) VALUES ('Wisla 2023')
```



### Insert poligon

```sql
update routes set route = ST_LineFromText((Select
    CONCAT(
        'LINESTRING(',
        GROUP_CONCAT(CONCAT(longitude, ' ', latitude) ORDER BY distanceFromStart SEPARATOR ','),
        ')' 
    )from w23_route))
```




