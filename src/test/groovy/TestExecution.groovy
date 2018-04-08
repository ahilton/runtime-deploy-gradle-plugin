import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Test

import static org.gradle.testkit.runner.TaskOutcome.*
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.*

class TestExecution  {
    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    @Before
    void setup() {
        buildFile = testProjectDir.newFile('build.gradle')
    }

    @Test
    void test(){

        buildFile << """
            task helloWorld {
                doLast {
                    println 'Hello world!'
                }
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('helloWorld')
                .build()

        assertTrue(result.output.contains('Hello world!'))
        assertEquals(SUCCESS, result.task(":helloWorld").outcome)
    }
}
