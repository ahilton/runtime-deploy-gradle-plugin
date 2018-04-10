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
                id 'runtime.deploy'
            }
            
            repositories {
                mavenCentral()
            }
            
            deploy {
                defaultGroup = 'com.google.guava'
                defaultExtension = 'jar'
            }
            
            components {
                guava {
                    version = 18.0
                    isDeployable = true
                    
                    dependencies { 
                        'eclipse-collections-api' {
                            version = '9.1.0'
                            group = 'org.eclipse.collections'
                        }
                    }
                }
                breeze {
                }
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('deployGuava')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains(' >>> Resolving org.eclipse.collections:eclipse-collections-api:9.1.0:@jar'))
        assertTrue(result.output.contains(' >>> Resolving com.google.guava:guava:18.0:@jar'))
        assertEquals(SUCCESS, result.task(":deployGuava").outcome)
    }

    @Test
    void testDeployAllComponents(){
        buildFile << """
            plugins {
                id 'runtime.deploy'
            }
            
            repositories {
                mavenCentral()
            }
            
            deploy {
                defaultGroup = 'com.google.guava'
                defaultExtension = 'jar'
            }
            
            components {
                guava {
                    version = '18.0'
                }
                'eclipse-collections-api' {
                    version = '9.1.0'
                    group = 'org.eclipse.collections'
                }
            }
        """

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('deployAll')
                .withPluginClasspath()
                .build()

        println result.output
        assertTrue(result.output.contains(' >>> Resolving org.eclipse.collections:eclipse-collections-api:9.1.0:@jar'))
        assertTrue(result.output.contains(' >>> Resolving com.google.guava:guava:18.0:@jar'))
        assertEquals(SUCCESS, result.task(":deployEclipseCollectionsApi").outcome)
        assertEquals(SUCCESS, result.task(":deployGuava").outcome)
        assertEquals(SUCCESS, result.task(":deployAll").outcome)
    }
}
