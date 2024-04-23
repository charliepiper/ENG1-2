# Testing framework

## When you require a headless libGDX environment
### GdxTestRunner.java
This is adapted from  https://github.com/TomGrill/gdx-testing/tree/master

Whenever you require a headless libGDX environment for your tests to pass annotate your test class with:

```java 
@RunWith(GdxTestRunner.class)
public class TestClass {
	@Test
	public void Test() {
	
	}
}
```

### testExample.java
a test using the headless libgdx environment, for testing things like assets 

### testExample2.java
an example of a unit test 