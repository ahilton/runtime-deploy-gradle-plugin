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
    void testHelloDefault(){
//            apply plugin: 'fxoms.deploy'
        buildFile << """
            plugins {
                id 'fxoms.deploy'
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('hello')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains('Hello from GreetingPlugin'))
        assertEquals(SUCCESS, result.task(":hello").outcome)
    }

    @Test
    void testHelloWithArgs(){
        buildFile << """
            plugins {
                id 'fxoms.deploy'
            }

            deploy.message = 'Hi from Gradle'
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('hello')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains('Hi from Gradle'))
        assertEquals(SUCCESS, result.task(":hello").outcome)
    }

    @Test
    void testHelloConfiguredWithDSL(){
        buildFile << """
            plugins {
                id 'fxoms.deploy'
            }

            deploy {
                message = 'Hi from dsl'
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('hello')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains('Hi from dsl'))
        assertEquals(SUCCESS, result.task(":hello").outcome)
    }
}
