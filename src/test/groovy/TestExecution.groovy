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
    void testDeployComponent(){
        buildFile << """
            plugins {
                id 'fxoms.deploy'
            }
            
            components {
                stp {
                    group = 'stpAdaptor'
                }
                breeze {
                }
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('deployStp')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains('>>> Task deploy. component name: stp. group: stpAdaptor'))
        assertEquals(SUCCESS, result.task(":deployStp").outcome)
    }

    @Test
    void testDeployAllComponents(){
        buildFile << """
            plugins {
                id 'fxoms.deploy'
            }
            
            components {
                stp {
                    group = 'stpAdaptor'
                }
                breeze {
                }
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('deploy')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains('>>> Task deploy. component name: stp. group: stpAdaptor'))
        assertEquals(SUCCESS, result.task(":deploy").outcome)
    }

    @Test
    void testDeployFromCustomTask(){
        buildFile << """
            plugins {
                id 'fxoms.deploy'
            }
            task deployAlex( type : DeployTask ) {
                component {
                    name = 'breeze'                
                    group = 'breeze-group'
                }
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('deployAlex')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains('>>> Task deploy. component name: stp. group: stpAdaptor'))
        assertEquals(SUCCESS, result.task(":deployStp").outcome)
    }
}
